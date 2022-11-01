package tw.com.raiden;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import tw.com.battle.BulletManager;
import tw.com.battle.CheckHit;
import tw.com.interactive.EnemyManager;
import tw.com.interactive.Fighter;
import tw.com.scenes.TilemapManager;

public class MainPanel extends JPanel implements Runnable {
	
//	畫面比例設定，設定後不再更改，故用final。
	private final int basicUnitSize = 32;  // 一台飛機基礎尺寸(一個單元尺寸)。
	private final int unitScale = 3;  // 整體放大比例，3倍。
	
	private final int realUnitSize = basicUnitSize * unitScale;  // 實際畫面的單元尺寸。
	private final int maxWindowCol = 7;  // 畫面最大寬度為7格單元。
	private final int maxWindowRow = 8;  // 畫面最大高度為8格單元。(7:8)
	private final int windowWidth = realUnitSize * maxWindowCol;  // 96 * 7 = 672px。
	private final int windowHeight = realUnitSize * maxWindowRow;  // 96 * 8 = 768px。
	
//	設定整體遊戲狀態。
	public int gameState;
	public final int loginState = 0;
	public final int titleState = 1;
	public final int playState = 2;
	public final int transitionsState = 3;
	public final int againState = 4;
	public boolean startEventScript;
	public int endEventScript;  // 0→沒有開始 1→玩家死亡 2→關卡結束
	
	
	private KeyHandler keyHandler;  // 按鍵監聽類別。
	
	public Thread gameThread;  // 主控執行序。
	
	public Timer fpsTimer;  // 設定FPS的schedule。
	private final double fps = 60.0;	
	
//	設定玩家飛機初始狀態。
	private Fighter fighter;
//	設定地圖初始狀態。
	private TilemapManager tilemapManager;
//	取得敵人管理。
	private EnemyManager enemyManager;	
//	取得子彈管理。
	private BulletManager bulletManager;
//	取得子彈與碰撞管理。
	private CheckHit checkHit;
//	設定UI介面。
	private MainUI mainUI;
	
	
//	取得聲音播放器，如果要播放多個音效，需要new多個物件ex.BGM、SE...。
	private Sound bgm;
	private Sound sound;
	
	public MainPanel() {		
		
		this.setLayout(null);		
//		.setPreferredSize()如果有使用LayoutManager的狀況下，LayoutManager會取得空間的setPreferredSize，因此大小設定可以生效。
//		.setSize()再不需要LayoutManager的狀況時使用。
		this.setPreferredSize(new Dimension(windowWidth, windowHeight));
//		this.setBackground(Color.BLACK);
		
//		設定雙緩衝區，先在記憶體進行圖片繪製完後，再複製到畫面上，可以避免畫面閃爍的問題。
//		但檢查後，Panel預設的狀態即為true，System.out.println(isDoubleBuffered())  // true。
		this.setDoubleBuffered(true);

//		設定每60fps更新畫面。
		fpsTimer = new Timer();
		
//		把鍵盤監聽事件交給keyHandler類別處理。
		keyHandler = new KeyHandler();
		this.addKeyListener(keyHandler);
		this.setFocusable(true);  // 設定此為焦點組件，也就是正在操作的組件。(誰要使用KeyListener事件，誰就要Focus)
		
//		初始化UI介面。
		mainUI = new MainUI(this);	
		startEventScript = false;
		endEventScript = 0;
		
//		初始化子彈管理。
		bulletManager = new BulletManager(this);
		
//		初始化玩家飛機。
		fighter = new Fighter(this, keyHandler,bulletManager);
		fighter.getFightImage("Red");
//		初始化地圖。
		tilemapManager = new TilemapManager(this, keyHandler);
//		初始化敵人管理。
		enemyManager = new EnemyManager(tilemapManager, this,fighter,bulletManager);
		checkHit = new CheckHit(fighter, enemyManager, bulletManager, mainUI);

//		初始化聲音播放器。
		sound = new Sound();
		bgm = new Sound();
//		playBGM();
		
	}
	
	public void setupGame() {
		gameState = loginState;
		playBGM(0);
	}
	
	public void startGameThread() {		
//		啟動執行序，因為本類別有實做Runnable，所以創造執行序物件時，直接套入this。
		gameThread = new Thread(this);
		gameThread.start();
	}

// 	用實作Runable的方法而非繼承Thread。
	@Override
	public void run() {
//		Main類別建構式中呼叫startGameThread()並啟動此執行序。
//		設定遊戲中核心運作，GameLoop的部分，ex.角色位移、畫面重畫。		
//		重複執行資料更新update→然後重畫畫面reapint。
		fpsTimer.schedule(new RefreshMainPanel(), 0, (int)(1/fps*1000));  // 以60fps為單位進行更新。
		
	}
	

	
	public void update() {
		
//		遊戲整體狀態判斷。
		switch(gameState) {
		case loginState:			
			mainUI.update();			
			break;
			
		case titleState:
			
			break;
			
		case playState:
			
			if(startEventScript && endEventScript == 0) {
				tilemapManager.update();
				fighter.update();
				enemyManager.update();  // 生成敵人+檢查是否刪除敵人
				
//				檢查子彈及敵機是否要被移除。
				bulletManager.destroyBullet();
				checkHit.fighterBulletHit();  // 檢查子彈是否命中敵機。
				checkHit.enemyBulletHit();  // 檢查子彈是否命中我方。
				
				mainUI.update();
			}else if(startEventScript && endEventScript != 0) {
//				關卡結束或遊戲結束。
				enemyManager.update();
				bulletManager.destroyBullet();
				checkHit.fighterBulletHit();  // 檢查子彈是否命中敵機。
				
				mainUI.update();
			}		
			break;
		
		case againState:
			mainUI.update();
			break;
			
		case transitionsState:
			mainUI.update();
			break;
			
		default:
			System.out.println("GameState狀態錯誤。");
			break;
		}
		

	}
	
	public void doInit() {
		startEventScript = false;
		endEventScript = 0;
		tilemapManager.doInit();
		fighter.doInit();
		enemyManager.doInit();
		bulletManager.doInit();
		
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
//		遊戲整體狀態判斷。
		switch(gameState) {
		case loginState:
//			繪製UI。
			mainUI.draw(g2d);			
			break;
			
		case titleState:
			
			break;
			
		case playState:
			
			tilemapManager.draw(g2d);			
			enemyManager.draw(g2d);
//			繪製子彈。
			bulletManager.draw(g2d);
			fighter.draw(g2d);			
//			繪製UI。
			mainUI.draw(g2d);			
			break;
			
		case transitionsState:
			mainUI.draw(g2d);
			break;
		
		case againState:
			tilemapManager.draw(g2d);			
			enemyManager.draw(g2d);
//			繪製子彈。
			bulletManager.draw(g2d);
			fighter.draw(g2d);	
			mainUI.draw(g2d);
			break;
			
		default:
			System.out.println("GameState狀態錯誤。");
			break;
		}
		

		
		g2d.dispose();  // 釋放一切圖片資源。
	}
	
	public void playBGM(int i) {
		
		bgm.setFile(i);
		bgm.play();
		bgm.loop();
		
	}
	
	public void stopBGM() {
		
		bgm.stop();
		
	}
	
	public void playSE(int i) {
		
		sound.setFile(i);
		sound.play();
		
	}
	
//	內部類別(1)，處理FPS，繼承TimerTask--------------------------------------------------
	
	private class RefreshMainPanel extends TimerTask{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			update();
			
//			限定repaint在遊戲開始時才進行。
			if(gameState == transitionsState || gameState == playState) {
				repaint();
			}		
		}		
	}
	
//	內部類別(1)，處理FPS，繼承TimerTask--------------------------------------------------
	
//	回傳實際畫面的單元尺寸。
	public int getRealUnitSize() {
		return realUnitSize;
	}
//	回傳實際畫面的尺寸。
	public int getWindowWidth() {
		return windowWidth;
	}
	public int getWindowHight() {
		return windowHeight;
	}
	
//	回傳執行序。
	public Timer getFpsTimer() {
		return fpsTimer;
	}
	
	public double getFps() {
		return fps;
	}
	
//	回傳地圖資訊。
	public TilemapManager getTilemapManager() {
		return tilemapManager;
	}
	
//	回傳飛機物件。
	public Fighter getFighter() {
		return fighter;
	}
	
	
}
