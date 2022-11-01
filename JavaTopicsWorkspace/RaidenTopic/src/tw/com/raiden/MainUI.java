package tw.com.raiden;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import jdbc.ConnectionDB;

public class MainUI {
	
	private MainPanel mainPanel;
	private ConnectionDB connectionDB;
	
//	載入登入介面圖片。
	private BufferedImage imgLoginBackground,imgRankBakcGround;
	private int loginBGWidth,loginBGHeight,loginBGPositionX,loginBGPositionY;
	private BufferedImage imgLoginLogo,imgPlayerIcon;
	private int loginLogoWidth,loginLogoHeight,loginLogoPositionX,loginLogoPositionY,playerIconWidth,PlayerIconHeight;	
	private JTextField userAccount,userPassword;
	private int textfieldWidth,textfieldHeight;
	private JButton userLogin,userRegister,userOffline,userBack,userPlay,userAgain,userHome;
	private int jbuttonWidth,jbuttonHeight;
	private int loginUiState;
	private final int basicState = 0;
	private final int loginState = 1;
	private final int registerState = 2;
	private final int closeState = 3;
	private final int loginDBState = 4;
	private final int rankState = 5;
	private final int registerDBState = 6;
	private final int transitionsState = 7;
	private final int playState = 8;
	private final int againState = 9;
	private boolean isOffline;
	private int playStateAnimationCounter;
	
	private Component[] componentList;
	
//	提示訊息。
	private String getMessage;
	private boolean getMessageOn;
	private int messageCounter,loginSuccessCounter;	
	
//	字型相關。
	private Font basicArial;	
	private Font fontPixe;	
	
//	轉場圖片。
	private BufferedImage[] imgTransitions;
	private int transitionsCounter;
	
//	DB資料!!
	private String playerAccount;
	private String playerRank;
	private String playerHiScore;
	private String[] getPlayerDate;
	private int playerTimerCounter;
	private int playerTimer;
	private int playerScore;
	private boolean insertPlayerScore;
	private boolean getTopPlayerScore;

	
//	DecimalFormat dFormat = new DecimalFormat("#0.0");  // 可以格式化呈現數值內容，dFormat.format(double)
	
	public MainUI(MainPanel mainPanel) {
		
		this.mainPanel = mainPanel;	

		setBasicValue();
		getImage();
		setFontValue();		
	}
	
	public void setBasicValue() {
		
//		登入介面初始值。
//		   背景地圖。
		loginBGWidth = 672;
		loginBGHeight = 768;
		loginBGPositionX = 0;
		loginBGPositionY = 0;
//		    LOGO。
		loginLogoWidth = 368;
		loginLogoHeight = 392;
		loginLogoPositionX = mainPanel.getWindowWidth()/2-loginLogoWidth/2;
		loginLogoPositionY = 80;
//		    登入按鈕、登入介面。	
		loginUiState = basicState;
		userLogin = new JButton("LOGIN");
		userRegister = new JButton("SIGN");
		userOffline = new JButton("OFFLINE");
		userBack = new JButton("BACK");
		userAgain = new JButton("AGAIN");
		userHome = new JButton("HOME");
		
		isOffline = false;
		playStateAnimationCounter = 0;
		
		jbuttonWidth = 200;
		jbuttonHeight = 40;
		
		userAccount = new JTextField();
		userPassword = new JPasswordField();
		textfieldWidth = 200;
		textfieldHeight = 40;

		
//		提示訊息初始值。
		getMessage = "";
		getMessageOn = false;
		messageCounter = 0;
		
//		設定Rank狀態資料。
		playerIconWidth = 27;
		PlayerIconHeight = 48;
		userPlay = new JButton("PLAY");
		
//		設定轉場圖片。
		imgTransitions = new BufferedImage[15];
		transitionsCounter = 0;
		
//	    將按鈕加入點擊事件。
		setJButtonAction();
//	    設定畫面狀態。
		changeUiState();
		
		connectionDB = new ConnectionDB();
		
//		基本DB資料。
		playerAccount="";
		playerRank = "";
		playerHiScore = "";
		loginSuccessCounter = 0;
		playerTimer = 0;
		playerTimerCounter = 0;
		getPlayerDate = new String[12];
		insertPlayerScore = false;
		getTopPlayerScore = false;

	}
	
	public void getImage() {		

		try {
//			取得登入介面的背景。
			imgLoginBackground = ImageIO.read(getClass().getResourceAsStream("/BackGround/TilemapStage1_LoginPage.png"));
			imgLoginLogo = ImageIO.read(getClass().getResourceAsStream("/Logo/Raiden_logo.png"));	
//			取得rank的背景。
			imgRankBakcGround = ImageIO.read(getClass().getResourceAsStream("/BackGround/TilemapStage2_RankPage.png"));	 
			imgPlayerIcon = ImageIO.read(getClass().getResourceAsStream("/Logo/Player_Icon.png"));
			
			for(int i = 0; i < imgTransitions.length; i++) {
				imgTransitions[i] = ImageIO.read(getClass().getResourceAsStream("/Transitions/Transitions" + i + ".png"));
			}			
			
		}catch (Exception e) {
			System.out.println(e.toString());
		}		
		
	}
	
	public void setJButtonAction() {
		
		userLogin.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {		
				
				if(loginUiState != loginState) {
					loginUiState = loginState;
					changeUiState();
				}else if(loginUiState == loginState) {
					System.out.println("Account:"+userAccount.getText()+"  Password:"+userPassword.getText());
					loginUiState = loginDBState;
					changeUiState();
				}
				
			}
		});
		
		userRegister.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {	
				
				if(loginUiState != registerState) {
					loginUiState = registerState;
					changeUiState();
				}else if(loginUiState == registerState) {
					System.out.println("Account:"+userAccount.getText()+"  Password:"+userPassword.getText());					
					loginUiState = registerDBState;
					changeUiState();					
				}
				
			}
		});
		
		userOffline.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {	
				isOffline = true;  // 離線遊玩狀態。
				loginUiState = transitionsState;
				changeUiState();							
			}
		});
		
		userBack.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				loginUiState = basicState;
				changeUiState();
			}
		});
		
		userPlay.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				loginUiState = transitionsState;
				changeUiState();				
			}
		});
		
		userHome.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
//				重置狀態，回首頁	
//------------------------------BGM
				mainPanel.stopBGM();
				mainPanel.playBGM(0);
				if(!isOffline) {
//					有登入
					getTopPlayerScoreMethod();  // 更新Top5資料。
					doInit();					
					mainPanel.doInit();
					
					mainPanel.gameState = mainPanel.loginState;
					loginUiState = rankState;
					changeUiState();
					
				}else {
//					沒登入
					doInit();
					mainPanel.doInit();
					
					mainPanel.gameState = mainPanel.loginState;
					loginUiState = basicState;
					changeUiState();
				}
			}
		});
		
		userAgain.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
//				重置狀態，回首頁	
				if(!isOffline) {
//					有登入
					getTopPlayerScoreMethod();
					doInit();					
					mainPanel.doInit();
					
					mainPanel.gameState = mainPanel.loginState;
					loginUiState = transitionsState;
					changeUiState();
					
				}else {
//					沒登入
					doInit();
					mainPanel.doInit();
					
					mainPanel.gameState = mainPanel.loginState;
					loginUiState = transitionsState;
					changeUiState();
					
				}			
			}
		});
		
	}
	
	public void doInit() {
		playerTimer = 0;
		playerTimerCounter = 0;
		playerScore = 0;
		isOffline = false;
		
	}
	
	public void changeUiState() {
		
//	    取得mainPanel上所有的Component。
		componentList = mainPanel.getComponents();
//		重置所有Component狀態。
		for(Component c:componentList) {
			mainPanel.remove(c);		
		}				

		
//		重新設定Component。
		switch(loginUiState) {
		case basicState:
			userLogin.setBounds((mainPanel.getWindowWidth()/2-jbuttonWidth/2), (mainPanel.getWindowHight()/6*4+jbuttonHeight), jbuttonWidth, jbuttonHeight);
			userRegister.setBounds((mainPanel.getWindowWidth()/2-jbuttonWidth/2), (mainPanel.getWindowHight()/6*4+jbuttonHeight*2+20), jbuttonWidth, jbuttonHeight);
			userOffline.setBounds((mainPanel.getWindowWidth()/2-jbuttonWidth/2), (mainPanel.getWindowHight()/6*4+jbuttonHeight*4), jbuttonWidth, jbuttonHeight);
			mainPanel.add(userLogin);	
			mainPanel.add(userRegister);
			mainPanel.add(userOffline);				
			break;
			
		case loginState:
			userAccount.setBounds((mainPanel.getWindowWidth()/2), (mainPanel.getWindowHight()/6*4+textfieldHeight), textfieldWidth, textfieldHeight);
			userPassword.setBounds((mainPanel.getWindowWidth()/2), (mainPanel.getWindowHight()/6*4+(textfieldHeight*2+20)), textfieldWidth, textfieldHeight);
			userLogin.setBounds((mainPanel.getWindowWidth()/2), (mainPanel.getWindowHight()/6*5+jbuttonHeight), jbuttonWidth, jbuttonHeight);
			userBack.setBounds((mainPanel.getWindowWidth()/2-jbuttonWidth-10), (mainPanel.getWindowHight()/6*5+jbuttonHeight), jbuttonWidth, jbuttonHeight);
			mainPanel.add(userAccount);	
			mainPanel.add(userPassword);
			mainPanel.add(userLogin);
			mainPanel.add(userBack);			
			break;
			
		case registerState:			
			userAccount.setBounds((mainPanel.getWindowWidth()/2), (mainPanel.getWindowHight()/6*4+textfieldHeight), textfieldWidth, textfieldHeight);
			userPassword.setBounds((mainPanel.getWindowWidth()/2), (mainPanel.getWindowHight()/6*4+(textfieldHeight*2+20)), textfieldWidth, textfieldHeight);
			userRegister.setBounds((mainPanel.getWindowWidth()/2), (mainPanel.getWindowHight()/6*5+jbuttonHeight), jbuttonWidth, jbuttonHeight);
			userBack.setBounds((mainPanel.getWindowWidth()/2-jbuttonWidth-10), (mainPanel.getWindowHight()/6*5+jbuttonHeight), jbuttonWidth, jbuttonHeight);
			mainPanel.add(userAccount);	
			mainPanel.add(userPassword);
			mainPanel.add(userRegister);
			mainPanel.add(userBack);			
			break;
			
		case closeState:
			mainPanel.gameState = mainPanel.playState;		
			mainPanel.requestFocus();			
			break;
			
		case loginDBState:
			userAccount.setBounds((mainPanel.getWindowWidth()/2), (mainPanel.getWindowHight()/6*4+textfieldHeight), textfieldWidth, textfieldHeight);
			userPassword.setBounds((mainPanel.getWindowWidth()/2), (mainPanel.getWindowHight()/6*4+(textfieldHeight*2+20)), textfieldWidth, textfieldHeight);
			userLogin.setBounds((mainPanel.getWindowWidth()/2), (mainPanel.getWindowHight()/6*5+jbuttonHeight), jbuttonWidth, jbuttonHeight);
			userBack.setBounds((mainPanel.getWindowWidth()/2-jbuttonWidth-10), (mainPanel.getWindowHight()/6*5+jbuttonHeight), jbuttonWidth, jbuttonHeight);
			mainPanel.add(userAccount);	
			mainPanel.add(userPassword);
			mainPanel.add(userLogin);
			mainPanel.add(userBack);			
			break;
			
		case rankState:
			userPlay.setBounds((mainPanel.getWindowWidth()/2-jbuttonWidth/2), (mainPanel.getWindowHight()/6*5+jbuttonHeight), jbuttonWidth, jbuttonHeight);
			mainPanel.add(userPlay);			
			break;
			
		case registerDBState:
			userAccount.setBounds((mainPanel.getWindowWidth()/2), (mainPanel.getWindowHight()/6*4+textfieldHeight), textfieldWidth, textfieldHeight);
			userPassword.setBounds((mainPanel.getWindowWidth()/2), (mainPanel.getWindowHight()/6*4+(textfieldHeight*2+20)), textfieldWidth, textfieldHeight);
			userRegister.setBounds((mainPanel.getWindowWidth()/2), (mainPanel.getWindowHight()/6*5+jbuttonHeight), jbuttonWidth, jbuttonHeight);
			userBack.setBounds((mainPanel.getWindowWidth()/2-jbuttonWidth-10), (mainPanel.getWindowHight()/6*5+jbuttonHeight), jbuttonWidth, jbuttonHeight);
			mainPanel.add(userAccount);	
			mainPanel.add(userPassword);
			mainPanel.add(userRegister);
			mainPanel.add(userBack);			
			break;
		
		case transitionsState:
			mainPanel.gameState = mainPanel.transitionsState;
			break;
			
		case playState:
			break;
		
		case againState:
			userAgain.setBounds((mainPanel.getWindowWidth()/2), (mainPanel.getWindowHight()/6*4+jbuttonHeight), jbuttonWidth, jbuttonHeight);
			userHome.setBounds((mainPanel.getWindowWidth()/2-jbuttonWidth-10), (mainPanel.getWindowHight()/6*4+jbuttonHeight), jbuttonWidth, jbuttonHeight);
			mainPanel.add(userAgain);
			mainPanel.add(userHome);
			
			break;
			
		default:
			System.out.println("loginUiState狀態錯誤。");
			break;
			
		}		
		
		mainPanel.revalidate();
		mainPanel.repaint();

	}
	
	public void setFontValue() {
		
		basicArial = new Font("Arial", Font.PLAIN, 40);	
		
		try {
//			載入文字。
			InputStream is = getClass().getResourceAsStream("/Font/PixeloidSans-nR3g1.ttf");
			fontPixe = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
	}
	
	public void showMessage(String text) {
		String Stemp = text;
		if(Stemp.equals(getMessage)) {

		}else {
			getMessage = text;
			getMessageOn = true;
			mainPanel.repaint();
		}		
	}
	
	public void update() {
		
		
//		ShowMessage
		if(getMessageOn) {
			messageCounter++;	
			if(messageCounter >= 120) {
				getMessage = "";
				messageCounter = 0;
				mainPanel.repaint();
				getMessageOn = false;
			}
		}		
		
//		登入判斷。
		if(loginUiState == loginDBState) {
			if(loginSuccessCounter == 0) {
				showMessage("Connection...");
			}			
			userAccount.setEnabled(false);
			userPassword.setEnabled(false);
			userLogin.setEnabled(false);
			userBack.setEnabled(false);
			
			String account = userAccount.getText();
			String passwd = userPassword.getText();
			
			if(loginSuccessCounter != 0 || (connectionDB.connSuccess && connectionDB.userLogin(account, passwd) != null)) {
				if(loginSuccessCounter == 0) {
					playerAccount = connectionDB.userLogin(account, passwd);
				}
				loginSuccessCounter++;
//				連線成功與登入成功，才能進入。
				if(loginSuccessCounter == 60) {
					showMessage("Login Success!");
				}else if(loginSuccessCounter == 120) {					
					
					userAccount.setEnabled(true);
					userPassword.setEnabled(true);
					userLogin.setEnabled(true);
					userBack.setEnabled(true);
					getMessage = "";
					loginSuccessCounter = 0;
					mainPanel.repaint();
					
					isOffline = false;  // 非離線狀態登入。
					getTopPlayerScore = true;  // 取得資料。
					loginUiState = rankState;
					changeUiState();
				}
				
			}else {
				showMessage("Login fail! Account or password error");
				userAccount.setEnabled(true);
				userPassword.setEnabled(true);
				userLogin.setEnabled(true);
				userBack.setEnabled(true);
				loginUiState = loginState;
				changeUiState();
			}
			
		}
		
//		註冊判斷。
		if(loginUiState == registerDBState) {
			
			if(loginSuccessCounter == 0) {
				showMessage("Connection...");
			}			
			userAccount.setEnabled(false);
			userPassword.setEnabled(false);
			userRegister.setEnabled(false);
			userBack.setEnabled(false);
			
			String account = userAccount.getText();
			String passwd = userPassword.getText();
			String usermail = "123@gmail.com";
			
			if(account.equals("") || passwd.equals("")) {
				showMessage("Field cannot be blank!");
				userAccount.setEnabled(true);
				userPassword.setEnabled(true);
				userRegister.setEnabled(true);
				userBack.setEnabled(true);
				loginUiState = registerState;
				changeUiState();
				return;
			}
		
			
			if(loginSuccessCounter != 0 || (connectionDB.connSuccess && connectionDB.userRegister(account, passwd,usermail) != null)) {
				if(loginSuccessCounter == 0) {
					playerAccount = connectionDB.userLogin(account, passwd);;
				}
				loginSuccessCounter++;
//				連線成功與登入成功，才能進入。
				if(loginSuccessCounter == 60) {
					showMessage("Register & Login Account...!");
				}else if(loginSuccessCounter == 120) {
					showMessage("Login Success!");
				}else if(loginSuccessCounter == 180) {
					
					userAccount.setEnabled(true);
					userPassword.setEnabled(true);
					userRegister.setEnabled(true);
					userBack.setEnabled(true);
					getMessage = "";
					loginSuccessCounter = 0;
					mainPanel.repaint();
					
					isOffline = false;  // 非離線狀態登入。
					getTopPlayerScore = true;  // 取得資料。
					loginUiState = rankState;
					changeUiState();
				}
				
			}else {
				showMessage("Login fail! Account or password error");
				userAccount.setEnabled(true);
				userPassword.setEnabled(true);
				userRegister.setEnabled(true);
				userBack.setEnabled(true);
				loginUiState = registerState;
				changeUiState();
			}
		}
		
//		時間的計算。
		if(loginUiState == playState && mainPanel.startEventScript == true && mainPanel.endEventScript == 0) {
			playerTimerCounter++;
			if(playerTimerCounter >= 60) {
				playerTimer++;
				playerScore++;  // 每多一秒加1分。
				playerTimerCounter = 0;
			}
		}
		
//		將得分資料INSERT進資料庫。
		if(insertPlayerScore) {
			if(connectionDB.userScoreInsert(playerAccount, playerScore+"", playerTimer+"")) {
				System.out.println("mainUi資料新增成功");
				insertPlayerScore = false;
			}			
		}
		
//		取得最高分玩家資料。
		if(getTopPlayerScore) {
			getTopPlayerScoreMethod();			
		}
		
	}
	
	public void getTopPlayerScoreMethod() {
		getPlayerDate = connectionDB.getTopScore(playerAccount);
		
		if(getPlayerDate != null) {
			playerHiScore = getPlayerDate[10];
			playerRank = getPlayerDate[11];
			getTopPlayerScore = false;
			mainPanel.repaint();
			System.out.println("資料取得成功");
		}else {
			showMessage("Data Error");
			System.out.println("資料取得異常");
		}
	}
	
	public void playerScoreAdd(int enemyScore) {
		playerScore += enemyScore;
	}
	
	public void draw(Graphics2D g2d) {	
		
//		設定UI上要呈現的字型樣式。
//		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);  // 文字抗鋸齒(8-bit文字不明顯)。
		g2d.setFont(fontPixe);
		
		switch(loginUiState) {		
		case basicState:				
//			畫出背景。
			g2d.drawImage(imgLoginBackground, loginBGPositionX, loginBGPositionY, loginBGWidth, loginBGHeight, null);
			g2d.drawImage(imgLoginLogo, loginLogoPositionX, loginLogoPositionY, loginLogoWidth, loginLogoHeight, null);
			
			userLogin.setFont(fontPixe);
			userLogin.setFont(g2d.getFont().deriveFont(Font.PLAIN,32F));
			userLogin.setBackground(Color.DARK_GRAY);
			userLogin.setForeground(Color.YELLOW);
			userLogin.setFocusPainted(false);
			
			userRegister.setFont(fontPixe);
			userRegister.setFont(g2d.getFont().deriveFont(Font.PLAIN,32F));
			userRegister.setBackground(Color.DARK_GRAY);
			userRegister.setForeground(new Color(0,204,0));
			userRegister.setFocusPainted(false);
			
			userOffline.setFont(fontPixe);
			userOffline.setFont(g2d.getFont().deriveFont(Font.PLAIN,32F));
			userOffline.setBackground(Color.DARK_GRAY);
			userOffline.setForeground(Color.WHITE);
			userOffline.setFocusPainted(false);	
			
			userLogin.repaint();
			userRegister.repaint();
			userOffline.repaint();
			break;
			
		case loginState:
//			畫出背景。
			g2d.drawImage(imgLoginBackground, loginBGPositionX, loginBGPositionY, loginBGWidth, loginBGHeight, null);
			g2d.drawImage(imgLoginLogo, loginLogoPositionX, loginLogoPositionY, loginLogoWidth, loginLogoHeight, null);
			
			g2d.setFont(g2d.getFont().deriveFont(Font.BOLD,32F));
			g2d.setColor(Color.BLACK);
			g2d.drawString("ACCOUNT", (mainPanel.getWindowWidth()/2)-textfieldWidth+32, (mainPanel.getWindowHight()/4*3)+9);
			g2d.setColor(Color.YELLOW);
			g2d.drawString("ACCOUNT", (mainPanel.getWindowWidth()/2)-textfieldWidth+30, (mainPanel.getWindowHight()/4*3)+7);
			g2d.setColor(Color.BLACK);
			g2d.drawString("PASSWORD", (mainPanel.getWindowWidth()/2)-textfieldWidth+8, (mainPanel.getWindowHight()/6*5)+7);
			g2d.setColor(Color.YELLOW);
			g2d.drawString("PASSWORD", (mainPanel.getWindowWidth()/2)-textfieldWidth+6, (mainPanel.getWindowHight()/6*5)+5);
		
			userAccount.setBorder(null);
			userAccount.setFont(fontPixe);
			userAccount.setFont(g2d.getFont().deriveFont(Font.PLAIN,28F));
			userAccount.setBackground(Color.LIGHT_GRAY);
			userAccount.setForeground(Color.DARK_GRAY);
			userAccount.setHorizontalAlignment(SwingConstants.CENTER);
			
			userPassword.setBorder(null);
			userPassword.setFont(fontPixe);
			userPassword.setFont(g2d.getFont().deriveFont(Font.PLAIN,28F));
			userPassword.setBackground(Color.LIGHT_GRAY);
			userPassword.setForeground(Color.DARK_GRAY);
			userPassword.setHorizontalAlignment(SwingConstants.CENTER);
			
			userLogin.setFont(fontPixe);
			userLogin.setFont(g2d.getFont().deriveFont(Font.PLAIN,32F));
			userLogin.setBackground(Color.DARK_GRAY);
			userLogin.setForeground(Color.YELLOW);
			userLogin.setFocusPainted(false);
			
			userBack.setFont(fontPixe);
			userBack.setFont(g2d.getFont().deriveFont(Font.PLAIN,32F));
			userBack.setBackground(Color.DARK_GRAY);
			userBack.setForeground(Color.WHITE);
			userBack.setFocusPainted(false);	
			
			userAccount.repaint();
			userPassword.repaint();
			userLogin.repaint();
			userBack.repaint();
			break;
			
		case registerState:
//			畫出背景。
			g2d.drawImage(imgLoginBackground, loginBGPositionX, loginBGPositionY, loginBGWidth, loginBGHeight, null);
			g2d.drawImage(imgLoginLogo, loginLogoPositionX, loginLogoPositionY, loginLogoWidth, loginLogoHeight, null);
			
			g2d.setFont(g2d.getFont().deriveFont(Font.BOLD,32F));
			g2d.setColor(Color.BLACK);
			g2d.drawString("ACCOUNT", (mainPanel.getWindowWidth()/2)-textfieldWidth+32, (mainPanel.getWindowHight()/4*3)+9);
			g2d.setColor(new Color(0,204,0));
			g2d.drawString("ACCOUNT", (mainPanel.getWindowWidth()/2)-textfieldWidth+30, (mainPanel.getWindowHight()/4*3)+7);
			g2d.setColor(Color.BLACK);
			g2d.drawString("PASSWORD", (mainPanel.getWindowWidth()/2)-textfieldWidth+8, (mainPanel.getWindowHight()/6*5)+7);
			g2d.setColor(new Color(0,204,0));
			g2d.drawString("PASSWORD", (mainPanel.getWindowWidth()/2)-textfieldWidth+6, (mainPanel.getWindowHight()/6*5)+5);
		
			userAccount.setBorder(null);
			userAccount.setFont(fontPixe);
			userAccount.setFont(g2d.getFont().deriveFont(Font.PLAIN,28F));
			userAccount.setBackground(Color.LIGHT_GRAY);
			userAccount.setForeground(Color.DARK_GRAY);
			userAccount.setHorizontalAlignment(SwingConstants.CENTER);
			
			userPassword.setBorder(null);
			userPassword.setFont(fontPixe);
			userPassword.setFont(g2d.getFont().deriveFont(Font.PLAIN,28F));
			userPassword.setBackground(Color.LIGHT_GRAY);
			userPassword.setForeground(Color.DARK_GRAY);
			userPassword.setHorizontalAlignment(SwingConstants.CENTER);
			
			userRegister.setFont(fontPixe);
			userRegister.setFont(g2d.getFont().deriveFont(Font.PLAIN,32F));
			userRegister.setBackground(Color.DARK_GRAY);
			userRegister.setForeground(new Color(0,204,0));
			userRegister.setFocusPainted(false);				
			
			userBack.setFont(fontPixe);
			userBack.setFont(g2d.getFont().deriveFont(Font.PLAIN,32F));
			userBack.setBackground(Color.DARK_GRAY);
			userBack.setForeground(Color.WHITE);
			userBack.setFocusPainted(false);	
			
			userAccount.repaint();
			userPassword.repaint();
			userLogin.repaint();
			userBack.repaint();				
			break;
			
		case loginDBState:
//			畫出背景。
			g2d.drawImage(imgLoginBackground, loginBGPositionX, loginBGPositionY, loginBGWidth, loginBGHeight, null);
			g2d.drawImage(imgLoginLogo, loginLogoPositionX, loginLogoPositionY, loginLogoWidth, loginLogoHeight, null);
			
			g2d.setFont(g2d.getFont().deriveFont(Font.BOLD,32F));
			g2d.setColor(Color.BLACK);
			g2d.drawString("ACCOUNT", (mainPanel.getWindowWidth()/2)-textfieldWidth+32, (mainPanel.getWindowHight()/4*3)+9);
			g2d.setColor(Color.YELLOW);
			g2d.drawString("ACCOUNT", (mainPanel.getWindowWidth()/2)-textfieldWidth+30, (mainPanel.getWindowHight()/4*3)+7);
			g2d.setColor(Color.BLACK);
			g2d.drawString("PASSWORD", (mainPanel.getWindowWidth()/2)-textfieldWidth+8, (mainPanel.getWindowHight()/6*5)+7);
			g2d.setColor(Color.YELLOW);
			g2d.drawString("PASSWORD", (mainPanel.getWindowWidth()/2)-textfieldWidth+6, (mainPanel.getWindowHight()/6*5)+5);
		
			userAccount.setBorder(null);
			userAccount.setFont(fontPixe);
			userAccount.setFont(g2d.getFont().deriveFont(Font.PLAIN,28F));
			userAccount.setBackground(Color.LIGHT_GRAY);
			userAccount.setForeground(Color.DARK_GRAY);
			userAccount.setHorizontalAlignment(SwingConstants.CENTER);
			
			userPassword.setBorder(null);
			userPassword.setFont(fontPixe);
			userPassword.setFont(g2d.getFont().deriveFont(Font.PLAIN,28F));
			userPassword.setBackground(Color.LIGHT_GRAY);
			userPassword.setForeground(Color.DARK_GRAY);
			userPassword.setHorizontalAlignment(SwingConstants.CENTER);
			
			userLogin.setFont(fontPixe);
			userLogin.setFont(g2d.getFont().deriveFont(Font.PLAIN,32F));
			userLogin.setBackground(Color.DARK_GRAY);
			userLogin.setForeground(Color.YELLOW);
			userLogin.setFocusPainted(false);
			
			userBack.setFont(fontPixe);
			userBack.setFont(g2d.getFont().deriveFont(Font.PLAIN,32F));
			userBack.setBackground(Color.DARK_GRAY);
			userBack.setForeground(Color.WHITE);
			userBack.setFocusPainted(false);	
			
			userAccount.repaint();
			userPassword.repaint();
			userLogin.repaint();
			userBack.repaint();
			break;
			
		case registerDBState:
//			畫出背景。
			g2d.drawImage(imgLoginBackground, loginBGPositionX, loginBGPositionY, loginBGWidth, loginBGHeight, null);
			g2d.drawImage(imgLoginLogo, loginLogoPositionX, loginLogoPositionY, loginLogoWidth, loginLogoHeight, null);
			
			g2d.setFont(g2d.getFont().deriveFont(Font.BOLD,32F));
			g2d.setColor(Color.BLACK);
			g2d.drawString("ACCOUNT", (mainPanel.getWindowWidth()/2)-textfieldWidth+32, (mainPanel.getWindowHight()/4*3)+9);
			g2d.setColor(new Color(0,204,0));
			g2d.drawString("ACCOUNT", (mainPanel.getWindowWidth()/2)-textfieldWidth+30, (mainPanel.getWindowHight()/4*3)+7);
			g2d.setColor(Color.BLACK);
			g2d.drawString("PASSWORD", (mainPanel.getWindowWidth()/2)-textfieldWidth+8, (mainPanel.getWindowHight()/6*5)+7);
			g2d.setColor(new Color(0,204,0));
			g2d.drawString("PASSWORD", (mainPanel.getWindowWidth()/2)-textfieldWidth+6, (mainPanel.getWindowHight()/6*5)+5);
		
			userAccount.setBorder(null);
			userAccount.setFont(fontPixe);
			userAccount.setFont(g2d.getFont().deriveFont(Font.PLAIN,28F));
			userAccount.setBackground(Color.LIGHT_GRAY);
			userAccount.setForeground(Color.DARK_GRAY);
			userAccount.setHorizontalAlignment(SwingConstants.CENTER);
			
			userPassword.setBorder(null);
			userPassword.setFont(fontPixe);
			userPassword.setFont(g2d.getFont().deriveFont(Font.PLAIN,28F));
			userPassword.setBackground(Color.LIGHT_GRAY);
			userPassword.setForeground(Color.DARK_GRAY);
			userPassword.setHorizontalAlignment(SwingConstants.CENTER);
			
			userRegister.setFont(fontPixe);
			userRegister.setFont(g2d.getFont().deriveFont(Font.PLAIN,32F));
			userRegister.setBackground(Color.DARK_GRAY);
			userRegister.setForeground(new Color(0,204,0));
			userRegister.setFocusPainted(false);				
			
			userBack.setFont(fontPixe);
			userBack.setFont(g2d.getFont().deriveFont(Font.PLAIN,32F));
			userBack.setBackground(Color.DARK_GRAY);
			userBack.setForeground(Color.WHITE);
			userBack.setFocusPainted(false);	
			
			userAccount.repaint();
			userPassword.repaint();
			userLogin.repaint();
			userBack.repaint();
			break;
			
		case rankState:				
//			劃出背景。
			g2d.drawImage(imgRankBakcGround, loginBGPositionX, loginBGPositionY, loginBGWidth, loginBGHeight, null);
			g2d.drawImage(imgPlayerIcon, 20, 20, playerIconWidth, PlayerIconHeight, null);
			
			userPlay.setFont(fontPixe);
			userPlay.setFont(g2d.getFont().deriveFont(Font.PLAIN,32F));
			userPlay.setBackground(Color.DARK_GRAY);
			userPlay.setForeground(Color.YELLOW);
			userPlay.setFocusPainted(false);
			
			userPlay.repaint();
			
			g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN,28F));
			g2d.setColor(Color.BLACK);
			g2d.drawString("PLAYER : ", playerIconWidth+36, PlayerIconHeight+12);
			g2d.setColor(new Color(255,128,0));
			g2d.drawString("PLAYER : ", playerIconWidth+34, PlayerIconHeight+10);
			
//			加入DB玩家Account。
			g2d.setColor(Color.BLACK);
			g2d.drawString(playerAccount, 202, PlayerIconHeight+12);
			g2d.setColor(Color.CYAN);
			g2d.drawString(playerAccount, 200, PlayerIconHeight+10);
//			加入DB玩家Account。
			
			g2d.setFont(g2d.getFont().deriveFont(Font.BOLD,72F));
//			取得字串長度(要再設定字型尺寸的後面)。
			int length = (int)g2d.getFontMetrics().getStringBounds("HALL  OF  ACES", g2d).getWidth();  
			g2d.setStroke(new BasicStroke(5));
			g2d.setColor(Color.BLACK);				
			g2d.drawLine((mainPanel.getWindowWidth()/2)-(length/2)+6, 92, (mainPanel.getWindowWidth()/2)-(length/2)+length, 92);
			g2d.setColor(Color.YELLOW);				
			g2d.drawLine((mainPanel.getWindowWidth()/2)-(length/2)+4, 90, (mainPanel.getWindowWidth()/2)-(length/2)+length-2, 90);
			g2d.setColor(Color.BLACK);
			g2d.drawString("HALL  OF  ACES", (mainPanel.getWindowWidth()/2)-(length/2)+6, 162);
			g2d.setColor(Color.YELLOW);
			g2d.drawString("HALL  OF  ACES", (mainPanel.getWindowWidth()/2)-(length/2)+4, 160);
			
			int row0Y = 230;
			int rowYOffest = 65;
			int col0X = 61; 
			int col1X = col0X+170; 
			int col2X = col1X+230;
			g2d.setFont(g2d.getFont().deriveFont(Font.BOLD,36F)); 				
			g2d.setColor(Color.BLACK);
			g2d.drawString("RANK", col0X+2, row0Y+2);
			g2d.setColor(new Color(255,128,0));
			g2d.drawString("RANK", col0X, row0Y);
			g2d.setColor(Color.BLACK);
			g2d.drawString("ACCOUNT", col1X+2, row0Y+2);
			g2d.setColor(new Color(255,128,0));
			g2d.drawString("ACCOUNT", col1X, row0Y);
			g2d.setColor(Color.BLACK);
			g2d.drawString("SCORE", col2X+2, row0Y+2);
			g2d.setColor(new Color(255,128,0));
			g2d.drawString("SCORE", col2X, row0Y);
			
//			RANK
			g2d.setColor(Color.BLACK);
			g2d.drawString("1ST", col0X+2, row0Y+rowYOffest+2);
			g2d.setColor(Color.RED);
			g2d.drawString("1ST", col0X, row0Y+rowYOffest);
			g2d.setColor(Color.BLACK);
			g2d.drawString("2ND", col0X+2, row0Y+rowYOffest*2+2);
			g2d.setColor(Color.RED);
			g2d.drawString("2ND", col0X, row0Y+rowYOffest*2);
			g2d.setColor(Color.BLACK);
			g2d.drawString("3RD", col0X+2, row0Y+rowYOffest*3+2);
			g2d.setColor(Color.RED);
			g2d.drawString("3RD", col0X, row0Y+rowYOffest*3);
			g2d.setColor(Color.BLACK);
			g2d.drawString("4TH", col0X+2, row0Y+rowYOffest*4+2);
			g2d.setColor(Color.RED);
			g2d.drawString("4TH", col0X, row0Y+rowYOffest*4);
			g2d.setColor(Color.BLACK);
			g2d.drawString("5TH", col0X+2, row0Y+rowYOffest*5+2);
			g2d.setColor(Color.RED);
			g2d.drawString("5TH", col0X, row0Y+rowYOffest*5);
			
//			加入DB玩家Account+排名。
			for(int i = 0; i < 5; i++) {
//				i從0開始
				g2d.setColor(Color.BLACK);
				g2d.drawString(getPlayerDate[i], col1X+2, row0Y+rowYOffest*(i+1)+2);
				g2d.setColor(new Color(0,204,0));
				g2d.drawString(getPlayerDate[i], col1X, row0Y+rowYOffest*(i+1));
				
				g2d.setColor(Color.BLACK);
				g2d.drawString(getPlayerDate[i+5], col2X+2, row0Y+rowYOffest*(i+1)+2);
				g2d.setColor(Color.YELLOW);
				g2d.drawString(getPlayerDate[i+5], col2X, row0Y+rowYOffest*(i+1));
			}
//			加入DB玩家Account+排名。
			
			g2d.setColor(Color.BLACK);				
			g2d.drawLine((mainPanel.getWindowWidth()/2)-(length/2)+6, row0Y+rowYOffest*5+22, (mainPanel.getWindowWidth()/2)-(length/2)+length, row0Y+rowYOffest*5+22);
			g2d.setColor(Color.YELLOW);				
			g2d.drawLine((mainPanel.getWindowWidth()/2)-(length/2)+4, row0Y+rowYOffest*5+20, (mainPanel.getWindowWidth()/2)-(length/2)+length-2, row0Y+rowYOffest*5+20);
			
			
//			加入DB玩家Account+排名。
			g2d.setColor(Color.BLACK);
			g2d.drawString(playerRank, col0X+2, row0Y+rowYOffest*6+2);
			g2d.setColor(Color.CYAN);
			g2d.drawString(playerRank, col0X, row0Y+rowYOffest*6);
			
			g2d.setColor(Color.BLACK);
			g2d.drawString(playerAccount, col1X+2, row0Y+rowYOffest*6+2);
			g2d.setColor(Color.CYAN);
			g2d.drawString(playerAccount, col1X, row0Y+rowYOffest*6);
			g2d.setColor(Color.BLACK);
			
			g2d.drawString(playerHiScore, col2X+2, row0Y+rowYOffest*6+2);
			g2d.setColor(Color.CYAN);
			g2d.drawString(playerHiScore, col2X, row0Y+rowYOffest*6);			
//			加入DB玩家Account+排名。
			
			break;
		
		case transitionsState:					
//			劃出背景。
			g2d.drawImage(imgRankBakcGround, loginBGPositionX, loginBGPositionY, loginBGWidth, loginBGHeight, null);
			g2d.drawImage(imgPlayerIcon, 20, 20, playerIconWidth, PlayerIconHeight, null);
			
			transitionsCounter++;
			if(transitionsCounter < 35) {
				g2d.drawImage(imgTransitions[transitionsCounter/5], loginBGPositionX, loginBGPositionY, loginBGWidth, loginBGHeight, null);
			}else {
//------------------------------BGM
				mainPanel.stopBGM();
				mainPanel.playBGM(1);
				loginUiState = closeState;
				changeUiState();
			}							
			break;
			
		case closeState:
			transitionsCounter++;
			if(transitionsCounter >= 35 && transitionsCounter < 125) {
				g2d.drawImage(imgTransitions[7], loginBGPositionX, loginBGPositionY, loginBGWidth, loginBGHeight, null);
			}else if(transitionsCounter >= 125 && transitionsCounter < 160) {
				g2d.drawImage(imgTransitions[(transitionsCounter-85)/5], loginBGPositionX, loginBGPositionY, loginBGWidth, loginBGHeight, null);
			}else if(transitionsCounter >= 160) {
				transitionsCounter = 0;
				loginUiState = playState;
				changeUiState();
			}				
			break;
			
		case playState:
//			動畫計數器。
			if(!mainPanel.startEventScript) {
				g2d.setFont(g2d.getFont().deriveFont(Font.BOLD,108F));
				playStateAnimationCounter++;
//				進入後的半秒開始倒數。
				if(playStateAnimationCounter >= 30 && playStateAnimationCounter < 90) {
					g2d.setColor(Color.BLACK);
					g2d.drawString("3", mainPanel.getWindowWidth()/2-38, mainPanel.getWindowHight()/2+2);
					g2d.setColor(Color.YELLOW);
					g2d.drawString("3", mainPanel.getWindowWidth()/2-36, mainPanel.getWindowHight()/2);
				}else if(playStateAnimationCounter >= 90 && playStateAnimationCounter < 150) {
					g2d.setColor(Color.BLACK);
					g2d.drawString("2", mainPanel.getWindowWidth()/2-38, mainPanel.getWindowHight()/2+2);
					g2d.setColor(Color.YELLOW);
					g2d.drawString("2", mainPanel.getWindowWidth()/2-36, mainPanel.getWindowHight()/2);
				}else if(playStateAnimationCounter >= 150 && playStateAnimationCounter < 210) {
					g2d.setColor(Color.BLACK);
					g2d.drawString("1", mainPanel.getWindowWidth()/2-38, mainPanel.getWindowHight()/2+2);
					g2d.setColor(Color.YELLOW);
					g2d.drawString("1", mainPanel.getWindowWidth()/2-36, mainPanel.getWindowHight()/2);
				}else if(playStateAnimationCounter >= 210 && playStateAnimationCounter < 270) {
					g2d.setFont(g2d.getFont().deriveFont(Font.BOLD,54F));
					int lengthM = (int)g2d.getFontMetrics().getStringBounds("MISSION START", g2d).getWidth();  
					g2d.setColor(Color.BLACK);
					g2d.drawString("MISSION START", mainPanel.getWindowWidth()/2-lengthM/2+2, mainPanel.getWindowHight()/2+2);
					g2d.setColor(Color.YELLOW);
					g2d.drawString("MISSION START", mainPanel.getWindowWidth()/2-lengthM/2, mainPanel.getWindowHight()/2);
				}else if(playStateAnimationCounter >= 270) {
					playStateAnimationCounter = 0;
					mainPanel.startEventScript = true;
				}
			}			
			
			g2d.setFont(g2d.getFont().deriveFont(Font.BOLD,36F));
						
			g2d.setColor(Color.BLACK);
			g2d.drawString("TIMER", 33, 42);
			g2d.setColor(new Color(255,128,0));
			g2d.drawString("TIMER", 31, 40);
			g2d.setColor(Color.BLACK);
			g2d.drawString("SCORE", 263, 42);
			g2d.setColor(new Color(255,128,0));
			g2d.drawString("SCORE", 261, 40);
			
//			玩家當前資料
			g2d.setColor(Color.BLACK);
			g2d.drawString(playerTimer+"", 33, 84);
			g2d.setColor(Color.CYAN);
			g2d.drawString(playerTimer+"", 31, 82);
			g2d.setColor(Color.BLACK);
			g2d.drawString(playerScore+"", 263, 84);
			g2d.setColor(Color.CYAN);
			g2d.drawString(playerScore+"", 261, 82);
			
//			如果是離線遊玩，則不會有HI-SCORE資料。
			if(!isOffline) {
				g2d.setColor(Color.BLACK);
				g2d.drawString("HI-SCORE", 463, 42);
				g2d.setColor(new Color(255,128,0));
				g2d.drawString("HI-SCORE", 461, 40);
				
				g2d.setColor(Color.BLACK);
				g2d.drawString(playerHiScore, 463, 84);
				g2d.setColor(Color.YELLOW);
				g2d.drawString(playerHiScore, 461, 82);
			}
			
			g2d.setColor(Color.BLACK);
			g2d.drawString("LIFE: "+mainPanel.getFighter().fighterBlood, 22, mainPanel.getWindowHight()-20);
			g2d.setColor(Color.RED);
			g2d.drawString("LIFE: "+mainPanel.getFighter().fighterBlood, 20, mainPanel.getWindowHight()-22);
//			玩家當前資料
			
//			1 玩家死亡，2 關卡完成。
			if(mainPanel.endEventScript != 0) {		
				if(playStateAnimationCounter == 0) {
//------------------------------BGM
					mainPanel.stopBGM();
					mainPanel.playBGM(2);
				}
				
				g2d.setFont(g2d.getFont().deriveFont(Font.BOLD,54F));
				playStateAnimationCounter++;
				
				if(playStateAnimationCounter >= 30) {
				
					if(mainPanel.endEventScript == 1) {						
						int lengthGO = (int)g2d.getFontMetrics().getStringBounds("GAME OVER", g2d).getWidth();  
						g2d.setColor(Color.BLACK);
						g2d.drawString("GAME OVER", mainPanel.getWindowWidth()/2-lengthGO/2+2, mainPanel.getWindowHight()/2-90);
						g2d.setColor(Color.YELLOW);
						g2d.drawString("GAME OVER", mainPanel.getWindowWidth()/2-lengthGO/2, mainPanel.getWindowHight()/2-92);
					}else if(mainPanel.endEventScript == 2) {						
						int lengthMC = (int)g2d.getFontMetrics().getStringBounds("MISSION COMPLETE", g2d).getWidth();  
						g2d.setColor(Color.BLACK);
						g2d.drawString("MISSION COMPLETE", mainPanel.getWindowWidth()/2-lengthMC/2+2, mainPanel.getWindowHight()/2-90);
						g2d.setColor(Color.YELLOW);
						g2d.drawString("MISSION COMPLETE", mainPanel.getWindowWidth()/2-lengthMC/2, mainPanel.getWindowHight()/2-92);	
					}
				}				
				
				if(playStateAnimationCounter >= 90) {
//					秀"TOTAL SCORE:"
					int lengthTS = (int)g2d.getFontMetrics().getStringBounds("TOTAL SCORE", g2d).getWidth();  
					g2d.setColor(Color.BLACK);
					g2d.drawString("TOTAL SCORE", mainPanel.getWindowWidth()/2-lengthTS/2+2, mainPanel.getWindowHight()/2+2);
					g2d.setColor(Color.YELLOW);
					g2d.drawString("TOTAL SCORE", mainPanel.getWindowWidth()/2-lengthTS/2, mainPanel.getWindowHight()/2);
				}
				
				if(playStateAnimationCounter >= 150) {	
//					秀總得分。
					int lengthPS = (int)g2d.getFontMetrics().getStringBounds(playerScore+"", g2d).getWidth();  
					g2d.setColor(Color.BLACK);
					g2d.drawString(playerScore+"", mainPanel.getWindowWidth()/2-lengthPS/2+2, mainPanel.getWindowHight()/2+92);
					g2d.setColor(Color.YELLOW);
					g2d.drawString(playerScore+"", mainPanel.getWindowWidth()/2-lengthPS/2, mainPanel.getWindowHight()/2+90);
				}
				
				if(playStateAnimationCounter >= 210) {
					
//					資料庫INSERT INTO(非離線遊玩狀態)。
					if(!isOffline) {
						insertPlayerScore = true;
					}
					
//					更改狀態，顯示按鈕。
					playStateAnimationCounter = 0;
					mainPanel.gameState = mainPanel.againState;
					loginUiState = againState;
					changeUiState();					
				}
				
			}

			break;
			
		case againState:
			
			g2d.setFont(g2d.getFont().deriveFont(Font.BOLD,36F));
			
			g2d.setColor(Color.BLACK);
			g2d.drawString("TIMER", 33, 42);
			g2d.setColor(new Color(255,128,0));
			g2d.drawString("TIMER", 31, 40);
			g2d.setColor(Color.BLACK);
			g2d.drawString("SCORE", 263, 42);
			g2d.setColor(new Color(255,128,0));
			g2d.drawString("SCORE", 261, 40);
			
//			玩家當前資料
			g2d.setColor(Color.BLACK);
			g2d.drawString(playerTimer+"", 33, 84);
			g2d.setColor(Color.CYAN);
			g2d.drawString(playerTimer+"", 31, 82);
			g2d.setColor(Color.BLACK);
			g2d.drawString(playerScore+"", 263, 84);
			g2d.setColor(Color.CYAN);
			g2d.drawString(playerScore+"", 261, 82);
			
//			如果是離線遊玩，則不會有HI-SCORE資料。
			if(!isOffline) {
				g2d.setColor(Color.BLACK);
				g2d.drawString("HI-SCORE", 463, 42);
				g2d.setColor(new Color(255,128,0));
				g2d.drawString("HI-SCORE", 461, 40);
				
				g2d.setColor(Color.BLACK);
				g2d.drawString(playerHiScore, 463, 84);
				g2d.setColor(Color.YELLOW);
				g2d.drawString(playerHiScore, 461, 82);
			}
			
			g2d.setColor(Color.BLACK);
			g2d.drawString("LIFE: "+mainPanel.getFighter().fighterBlood, 22, mainPanel.getWindowHight()-20);
			g2d.setColor(Color.RED);
			g2d.drawString("LIFE: "+mainPanel.getFighter().fighterBlood, 20, mainPanel.getWindowHight()-22);
//			玩家當前資料
			
			g2d.setFont(g2d.getFont().deriveFont(Font.BOLD,54F));
			
			if(mainPanel.endEventScript == 1) {						
				int lengthGO = (int)g2d.getFontMetrics().getStringBounds("GAME OVER", g2d).getWidth();  
				g2d.setColor(Color.BLACK);
				g2d.drawString("GAME OVER", mainPanel.getWindowWidth()/2-lengthGO/2+2, mainPanel.getWindowHight()/2-90);
				g2d.setColor(Color.YELLOW);
				g2d.drawString("GAME OVER", mainPanel.getWindowWidth()/2-lengthGO/2, mainPanel.getWindowHight()/2-92);
			}else if(mainPanel.endEventScript == 2) {						
				int lengthMC = (int)g2d.getFontMetrics().getStringBounds("MISSION COMPLETE", g2d).getWidth();  
				g2d.setColor(Color.BLACK);
				g2d.drawString("MISSION COMPLETE", mainPanel.getWindowWidth()/2-lengthMC/2+2, mainPanel.getWindowHight()/2-90);
				g2d.setColor(Color.YELLOW);
				g2d.drawString("MISSION COMPLETE", mainPanel.getWindowWidth()/2-lengthMC/2, mainPanel.getWindowHight()/2-92);	
			}
			
			int lengthTS = (int)g2d.getFontMetrics().getStringBounds("TOTAL SCORE", g2d).getWidth();  
			g2d.setColor(Color.BLACK);
			g2d.drawString("TOTAL SCORE", mainPanel.getWindowWidth()/2-lengthTS/2+2, mainPanel.getWindowHight()/2+2);
			g2d.setColor(Color.YELLOW);
			g2d.drawString("TOTAL SCORE", mainPanel.getWindowWidth()/2-lengthTS/2, mainPanel.getWindowHight()/2);
			int lengthPS = (int)g2d.getFontMetrics().getStringBounds(playerScore+"", g2d).getWidth();  
			g2d.setColor(Color.BLACK);
			g2d.drawString(playerScore+"", mainPanel.getWindowWidth()/2-lengthPS/2+2, mainPanel.getWindowHight()/2+92);
			g2d.setColor(Color.YELLOW);
			g2d.drawString(playerScore+"", mainPanel.getWindowWidth()/2-lengthPS/2, mainPanel.getWindowHight()/2+90);
			
//			回首頁及再一次按鈕設定。
			userAgain.setFont(fontPixe);
			userAgain.setFont(g2d.getFont().deriveFont(Font.PLAIN,32F));
			userAgain.setBackground(Color.DARK_GRAY);
			userAgain.setForeground(Color.YELLOW);
			userAgain.setFocusPainted(false);
			
			userHome.setFont(fontPixe);
			userHome.setFont(g2d.getFont().deriveFont(Font.PLAIN,32F));
			userHome.setBackground(Color.DARK_GRAY);
			userHome.setForeground(Color.WHITE);
			userHome.setFocusPainted(false);	

			userAgain.repaint();
			userHome.repaint();
			
			break;
			
		default:
			System.out.println("loginUiState狀態錯誤!");
			break;
			
		}
					
//		ShowMessage相關。
		if(getMessageOn) {

			g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN,28F));
			g2d.setColor(Color.BLACK);
			g2d.drawString(getMessage, 12, mainPanel.getWindowHight()-12);
			g2d.setColor(Color.RED);
			g2d.drawString(getMessage, 10, mainPanel.getWindowHight()-10);
			
		}		
		
	}
	
	


}
