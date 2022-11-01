package tw.com.interactive;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import tw.com.battle.BulletManager;
import tw.com.raiden.KeyHandler;
import tw.com.raiden.MainPanel;
import tw.com.scenes.TilemapManager;

public class Fighter extends Entity {
	
	private int fighterSpeed = 4;
	
	private MainPanel mainPanel;
	private KeyHandler keyHandler;
	
//	載入與戰機相關圖片。
	private BufferedImage imgFigherCenter,imgFighterLeft1,imgFighterLeft2,imgFighterRight1,imgFighterRight2;
	
	private BufferedImage imgEngineCenterLong,imgEngineCenterShort;
	private BufferedImage imgEngineLeft1Long,imgEngineLeft1Short;
	private BufferedImage imgEngineLeft2Long,imgEngineLeft2Short;
	private BufferedImage imgEngineRight1Long,imgEngineRight1Short;
	private BufferedImage imgEngineRight2Long,imgEngineRight2Short;
	
	private BufferedImage imgBasicShootFirelight;
	
	private BufferedImage[] imgFigherExplosion;
	
//	初始方向設定。
	private String direction;  // 1~9 左至右，上到下，5為正中央狀態。
	
//	設定戰機動畫參數。
	private BufferedImage showFighterImage;
	private BufferedImage showEngineImage;
	private int fighterLeftImageCounter;
	private int fighterRightImageCounter;
	private int engineImageCounter;
	
//	設定攻擊參數(間隔)。
	private BufferedImage showShootImage;
	private int shootImageWidth;
	private int shootImageHeight;
	
	private boolean fighterShoot;  // 空白鍵判斷
	private boolean fighterCanShoot;
	private int fighterAttackCounter;
	private int fighterAttackCooldown;
	private int fighterMainBulletlevel;
		
//  飛機陰影圖片。
	private BufferedImage[] imgFighterShadow;
	private int shadowImageWidth,shadowImageHight,shadowOffsetX,shadowOffsetY;
	private int fighterShadowCount;
	
//	爆炸計數。
	private int isExplosionCounter;
	
	
	public Fighter(MainPanel mainPanel,KeyHandler keyHandler,BulletManager bulletManager) {
		
		this.mainPanel = mainPanel;
		this.keyHandler = keyHandler;
		this.bulletManager = bulletManager;
		
		setBasicValues();
	}
	
//	設定初始值位置、速度及狀態。
	public void setBasicValues() {
		
		entityPositionX = (mainPanel.getWindowWidth()/2)-(mainPanel.getRealUnitSize()/2);  // x軸置中，畫面寬/2-飛機寬/2。
		entityPositionY = mainPanel.getWindowHight()-(mainPanel.getRealUnitSize()*3);  // Y軸置底後向上抬高兩台飛機高度(要*3，包含螢幕外那台)。
		entitySpeed = fighterSpeed;
//		設定初始方向及飛機圖片狀態。
		direction = "5";
		showFighterImage = null;
		showEngineImage = null;
		fighterLeftImageCounter = 0;
		fighterRightImageCounter = 0;
		engineImageCounter = 0;
		
//		初始射擊狀態。
		showShootImage = null;
		fighterShoot = false;
		fighterCanShoot = true;
		fighterAttackCounter = 0;
		fighterAttackCooldown = 10;
		fighterMainBulletlevel = 1; // 最初始的攻擊等級。
		
//		射擊火光尺寸。
		shootImageWidth = 24;
		shootImageHeight = 21;
		
//		陰影圖片。
		imgFighterShadow = new BufferedImage[5];
		shadowImageWidth = 36;
		shadowImageHight = 24;
		shadowOffsetX = mainPanel.getRealUnitSize() + (mainPanel.getRealUnitSize()/2);  // 預抓1.5倍距離。
		shadowOffsetY = mainPanel.getRealUnitSize() - shadowImageHight;  // 預抓飛機高度-陰影高度。
		fighterShadowCount = 0;  // 初始是中央的陰影。
		
//		設定基本血量。
		this.fighterBlood = 3;
		this.isHitCounter = 0;
		imgFigherExplosion = new BufferedImage[16];
		isExplosionCounter = 0;
	}
	
//	Red,Blue,Yellow,Green
	public void getFightImage(String fighterType) {
		
		try {			
//			戰機本體圖片。
			imgFigherCenter = ImageIO.read(getClass().getResourceAsStream("/"+ fighterType +"Fighter/Fighter_Center.png"));
			imgFighterLeft1 = ImageIO.read(getClass().getResourceAsStream("/"+ fighterType +"Fighter/Fighter_Left_1.png"));
			imgFighterLeft2 = ImageIO.read(getClass().getResourceAsStream("/"+ fighterType +"Fighter/Fighter_Left_2.png"));
			imgFighterRight1 = ImageIO.read(getClass().getResourceAsStream("/"+ fighterType +"Fighter/Fighter_Right_1.png"));
			imgFighterRight2 = ImageIO.read(getClass().getResourceAsStream("/"+ fighterType +"Fighter/Fighter_Right_2.png"));
			
//			戰機引擎圖片。
			imgEngineCenterLong = ImageIO.read(getClass().getResourceAsStream("/"+ fighterType +"Fighter/Fighter_Engine_Center_1.png"));
			imgEngineCenterShort = ImageIO.read(getClass().getResourceAsStream("/"+ fighterType +"Fighter/Fighter_Engine_Center_2.png"));
			
			imgEngineLeft1Long = ImageIO.read(getClass().getResourceAsStream("/"+ fighterType +"Fighter/Fighter_Engine_Left_1.png"));
			imgEngineLeft1Short = ImageIO.read(getClass().getResourceAsStream("/"+ fighterType +"Fighter/Fighter_Engine_Left_2.png"));
			imgEngineLeft2Long = ImageIO.read(getClass().getResourceAsStream("/"+ fighterType +"Fighter/Fighter_Engine_Left2_1.png"));
			imgEngineLeft2Short = ImageIO.read(getClass().getResourceAsStream("/"+ fighterType +"Fighter/Fighter_Engine_Left2_2.png"));
			
			imgEngineRight1Long = ImageIO.read(getClass().getResourceAsStream("/"+ fighterType +"Fighter/Fighter_Engine_Right_1.png"));
			imgEngineRight1Short = ImageIO.read(getClass().getResourceAsStream("/"+ fighterType +"Fighter/Fighter_Engine_Right_2.png"));
			imgEngineRight2Long = ImageIO.read(getClass().getResourceAsStream("/"+ fighterType +"Fighter/Fighter_Engine_Right2_1.png"));
			imgEngineRight2Short = ImageIO.read(getClass().getResourceAsStream("/"+ fighterType +"Fighter/Fighter_Engine_Right2_2.png"));
			
//			射擊火光。
			imgBasicShootFirelight = ImageIO.read(getClass().getResourceAsStream("/BattleEffect/Basic_Shoot_Firelight.png"));
			
//			陰影圖片。
			for(int i = 0; i < imgFighterShadow.length; i++) {
				imgFighterShadow[i] = ImageIO.read(getClass().getResourceAsStream("/"+ fighterType +"Fighter/Fighter_Shadow_" + i + ".png"));
			}
			
//			爆炸圖片。
			for(int i = 0; i < imgFigherExplosion.length; i++){
				imgFigherExplosion[i] = ImageIO.read(getClass().getResourceAsStream("/BattleEffect/Enemy_LightShooter_Explosion"+ i +".png"));
			}
			
		}catch(Exception e) {
			System.out.println(e.toString());
		}
	}

	public void doInit() {
		entityPositionX = (mainPanel.getWindowWidth()/2)-(mainPanel.getRealUnitSize()/2); 
		entityPositionY = mainPanel.getWindowHight()-(mainPanel.getRealUnitSize()*3);
		direction = "5";
		showFighterImage = null;
		showEngineImage = null;
		fighterLeftImageCounter = 0;
		fighterRightImageCounter = 0;
		engineImageCounter = 0;
		
		showShootImage = null;
		fighterShoot = false;
		fighterCanShoot = true;
		fighterAttackCounter = 0;
		
		shadowOffsetX = mainPanel.getRealUnitSize() + (mainPanel.getRealUnitSize()/2);  // 預抓1.5倍距離。
		shadowOffsetY = mainPanel.getRealUnitSize() - shadowImageHight;  // 預抓飛機高度-陰影高度。
		fighterShadowCount = 0;  // 初始是中央的陰影。
		
		this.fighterBlood = 3;
		this.isHitCounter = 0;
		isExplosionCounter = 0;
	}
	
//	讓物件的控制，由物件本身進行。
	public void update() {
		
//		檢查戰機血量。
		if(fighterBlood != 0) {
			
			if(keyHandler.upPressed) {
				entityPositionY -= entitySpeed;  // 按W向上飛。
			}
			if(keyHandler.downPressed) {
				entityPositionY += entitySpeed;  // 按S向下飛(Y軸用加)。
			}
			if(keyHandler.leftPressed) {
				entityPositionX -= entitySpeed;  // 按A向左飛。
			}
			if(keyHandler.rightPressed) {
				entityPositionX += entitySpeed;  // 按DD向下飛(X軸用加)。
			}
			
			
//			按下空白鍵進行攻擊。
			if(keyHandler.spacePressed) {
				fighterShoot = true; // 戰機攻擊。
			}else if(!keyHandler.spacePressed) {
				fighterShoot = false;
			}
			
//			判斷戰機是否能攻擊。
			fighterAttack();
			
		}	
		
//		限制最大的X及Y值(飛機不會飛超過螢幕)。
		if(entityPositionY <= mainPanel.getRealUnitSize())
			entityPositionY = mainPanel.getRealUnitSize();
		if(entityPositionY >= (mainPanel.getWindowHight() - mainPanel.getRealUnitSize()))
			entityPositionY = (mainPanel.getWindowHight() - mainPanel.getRealUnitSize());
		if(entityPositionX <= 0)
			entityPositionX = 0;
		if(entityPositionX >= (mainPanel.getWindowWidth()-mainPanel.getRealUnitSize()))
			entityPositionX = (mainPanel.getWindowWidth()-mainPanel.getRealUnitSize());
		
		
//		判斷目前戰機移動狀態的9種可能。
		fighterDirection();  


		
	}
	
	
	public void fighterDirection() {
		
//		1)左上角飛行。
		if((keyHandler.leftPressed && !keyHandler.rightPressed) && 
				(keyHandler.upPressed && !keyHandler.downPressed)) direction = "1";
		
		
//		2)往上飛行，左右全按或全不按。
		if((!keyHandler.leftPressed && !keyHandler.rightPressed) && 
				(keyHandler.upPressed && !keyHandler.downPressed)) direction = "2";
		if((keyHandler.leftPressed && keyHandler.rightPressed) && 
				(keyHandler.upPressed && !keyHandler.downPressed)) direction = "2";
	
		
//		3)往右上飛行。
		if((!keyHandler.leftPressed && keyHandler.rightPressed) && 
				(keyHandler.upPressed && !keyHandler.downPressed)) direction = "3";
		
//		4)往左飛行，上下全按或全不按。
		if((keyHandler.leftPressed && !keyHandler.rightPressed) && 
				(!keyHandler.upPressed && !keyHandler.downPressed)) direction = "4";
		if((keyHandler.leftPressed && !keyHandler.rightPressed) && 
				(keyHandler.upPressed && keyHandler.downPressed)) direction = "4";
		
//		5)完全靜止，全不按或全按，上下、左右交錯。
		if((!keyHandler.leftPressed && !keyHandler.rightPressed) && 
				(!keyHandler.upPressed && !keyHandler.downPressed)) direction = "5";
		if((keyHandler.leftPressed && keyHandler.rightPressed) && 
				(keyHandler.upPressed && keyHandler.downPressed)) direction = "5";
		if((!keyHandler.leftPressed && !keyHandler.rightPressed) && 
				(keyHandler.upPressed && keyHandler.downPressed)) direction = "5";
		if((keyHandler.leftPressed && keyHandler.rightPressed) && 
				(!keyHandler.upPressed && !keyHandler.downPressed)) direction = "5";
		
//		6)往右飛行，上下全按或全不按。。
		if((!keyHandler.leftPressed && keyHandler.rightPressed) && 
				(!keyHandler.upPressed && !keyHandler.downPressed)) direction = "6";
		if((!keyHandler.leftPressed && keyHandler.rightPressed) && 
				(keyHandler.upPressed && keyHandler.downPressed)) direction = "6";
		
//		7)左下角飛行。
		if((keyHandler.leftPressed && !keyHandler.rightPressed) && 
				(!keyHandler.upPressed && keyHandler.downPressed)) direction = "7";
		
//		8)往下飛行，左右全按或全不按。
		if((!keyHandler.leftPressed && !keyHandler.rightPressed) && 
				(!keyHandler.upPressed && keyHandler.downPressed)) direction = "8";
		if((keyHandler.leftPressed && keyHandler.rightPressed) && 
				(!keyHandler.upPressed && keyHandler.downPressed)) direction = "8";
		
//		9)右下角飛行。
		if((!keyHandler.leftPressed && keyHandler.rightPressed) && 
				(!keyHandler.upPressed && keyHandler.downPressed)) direction = "9";
		

	}
	
	
//	(應該要被獨立出去)。
	public void fighterAttack() {
		
//		60FPS 10tick間隔。		
		if(!fighterCanShoot){
			fighterAttackCounter++;
			showShootImage = null;
			if(fighterAttackCounter == fighterAttackCooldown) {
				fighterAttackCounter = 0;
				fighterCanShoot = true;
			}
		}
		
		if(fighterShoot && fighterCanShoot) {
//			子彈管理(新增子彈)。
//-----------------音效。
			mainPanel.playSE(3);
			bulletManager.createBullet(entityPositionX,entityPositionY,fighterMainBulletlevel);
//			飛機火光特效。
			showShootImage = imgBasicShootFirelight;
			fighterCanShoot = false;			
		}		
	}
	
	
//	讓物件的繪製，由物件本身進行。
	public void draw(Graphics2D g2d) {			

		fighterAinmator();
		
//		戰機血量檢查。
		if(fighterBlood != 0) {
			
//			繪製飛機陰影。
			for(int i = 0; i < imgFighterShadow.length; i++) {
				if(i == fighterShadowCount) {
					g2d.drawImage(imgFighterShadow[i], entityPositionX+shadowOffsetX, entityPositionY+shadowOffsetY, shadowImageWidth, shadowImageHight, null);
				}
			}	
			
//			是否被擊中檢測
			if(isHitCounter != 0) {
				isHitCounter++;
				if(isHitCounter % 6 < 3) {
					g2d.drawImage(showEngineImage, entityPositionX, entityPositionY, mainPanel.getRealUnitSize(), mainPanel.getRealUnitSize(), null);
					g2d.drawImage(showShootImage, entityPositionX+(mainPanel.getRealUnitSize()/2-shootImageWidth/2), entityPositionY-shootImageHeight/2, shootImageWidth, shootImageHeight, null);
					g2d.drawImage(showFighterImage, entityPositionX, entityPositionY, mainPanel.getRealUnitSize(), mainPanel.getRealUnitSize(), null);
				}
				
				if(isHitCounter >= 30) {
					isHitCounter = 0;
				}			
				
			}else {
//				引擎圖片。
				g2d.drawImage(showEngineImage, entityPositionX, entityPositionY, mainPanel.getRealUnitSize(), mainPanel.getRealUnitSize(), null);

//				射擊火光圖片(應該要被獨立出去)。
				g2d.drawImage(showShootImage, entityPositionX+(mainPanel.getRealUnitSize()/2-shootImageWidth/2), entityPositionY-shootImageHeight/2, shootImageWidth, shootImageHeight, null);

//				飛機本身圖片。
				g2d.drawImage(showFighterImage, entityPositionX, entityPositionY, mainPanel.getRealUnitSize(), mainPanel.getRealUnitSize(), null);
			}
			
		}else {
			
			if(isExplosionCounter/2 <= (imgFigherExplosion.length-1)) {
				if(isExplosionCounter == 0) {
// ----------------BGM
					mainPanel.playSE(5);
				}
				g2d.drawImage(imgFigherExplosion[isExplosionCounter/2], entityPositionX, entityPositionY, mainPanel.getRealUnitSize(),  mainPanel.getRealUnitSize()+3, null);
				isExplosionCounter++;
				if(isExplosionCounter == 32) {
//					玩家死亡，且死亡效果結束，所以是1。
					mainPanel.endEventScript = 1;
				}
			}
			
		}
		
	}	
	
//	規劃飛機圖片的變換。
	public void fighterAinmator() {
				
		switch(direction) {
		case "2":case "5":case "8":
//			飛機本體狀態：
			fighterLeftImageCounter = 0;
			fighterRightImageCounter = 0;
			showFighterImage = imgFigherCenter;
			
//			飛機引擎狀態：
			engineImageCounter++;
			if(direction == "2") {
				if(engineImageCounter < 3) {
					showEngineImage = imgEngineCenterLong;
				}else if(engineImageCounter >= 3 && engineImageCounter < 5) {
					showEngineImage = null;
				}else {
					showEngineImage = null;
					engineImageCounter = 0;
				}				
			}else if(direction == "5") {
				if(engineImageCounter < 5) {
					showEngineImage = imgEngineCenterLong;
				}else if(engineImageCounter >= 5 && engineImageCounter < 9) {
					showEngineImage = imgEngineCenterShort;
				}else {
					showEngineImage = imgEngineCenterShort;
					engineImageCounter = 0;
				}
			}else {
				if(engineImageCounter < 5) {
					showEngineImage = imgEngineCenterShort;
				}else if(engineImageCounter >= 5 && engineImageCounter < 9) {
					showEngineImage = null;
				}else {
					showEngineImage = null;
					engineImageCounter = 0;
				}
			}	
			
//			飛機陰影狀態。
			fighterShadowCount = 0;
			
			break;
		case "1":case "4":case "7":
//			飛機本體向左狀態：
			fighterLeftImageCounter++;
			fighterRightImageCounter = 0;
			
//			飛機引擎計數：
			engineImageCounter++;

			if(fighterLeftImageCounter > 12) {
//				第13tick換圖。
				showFighterImage = imgFighterLeft2;
				
//				飛機引擎狀態：
				if(direction == "1") {
					if(engineImageCounter < 3) {
						showEngineImage = imgEngineLeft2Long;
					}else if(engineImageCounter >= 3 && engineImageCounter < 5) {
						showEngineImage = null;
					}else {
						showEngineImage = null;
						engineImageCounter = 0;
					}				
				}else if(direction == "4") {
					if(engineImageCounter < 5) {
						showEngineImage = imgEngineLeft2Long;
					}else if(engineImageCounter >= 5 && engineImageCounter < 9) {
						showEngineImage = imgEngineLeft2Short;
					}else {
						showEngineImage = imgEngineLeft2Short;
						engineImageCounter = 0;
					}
				}else {
					if(engineImageCounter < 5) {
						showEngineImage = imgEngineLeft2Short;
					}else if(engineImageCounter >= 5 && engineImageCounter < 9) {
						showEngineImage = null;
					}else {
						showEngineImage = null;
						engineImageCounter = 0;
					}
				}
				
//				飛機陰影狀態。
				fighterShadowCount = 2;
				
			}else if(fighterLeftImageCounter > 4) {
//				第5tick換圖。
				showFighterImage = imgFighterLeft1;
				
//				飛機引擎狀態：
				if(direction == "1") {
					showEngineImage = imgEngineLeft1Long;
					engineImageCounter = 0;
				}else if(direction == "4") {
					showEngineImage = imgEngineLeft1Long;
					engineImageCounter = 0;
				}else {
					showEngineImage = imgEngineLeft1Short;
					engineImageCounter = 0;
				}
				
//				飛機陰影狀態。
				fighterShadowCount = 1;
				
			}else {
				showFighterImage = imgFigherCenter;
				
//				飛機陰影狀態。
				fighterShadowCount = 0;
			}						
			
			break;
		case "3":case "6":case "9":
//			飛機本體向右狀態：
			fighterRightImageCounter++;
			fighterLeftImageCounter = 0;
			
//			飛機引擎計數：
			engineImageCounter++;
			
			if(fighterRightImageCounter > 12) {
				showFighterImage = imgFighterRight2;
				
//				飛機引擎狀態：
				if(direction == "3") {
					if(engineImageCounter < 3) {
						showEngineImage = imgEngineRight2Long;
					}else if(engineImageCounter >= 3 && engineImageCounter < 5) {
						showEngineImage = null;
					}else {
						showEngineImage = null;
						engineImageCounter = 0;
					}				
				}else if(direction == "6") {
					if(engineImageCounter < 5) {
						showEngineImage = imgEngineRight2Long;
					}else if(engineImageCounter >= 5 && engineImageCounter < 9) {
						showEngineImage = imgEngineRight2Short;
					}else {
						showEngineImage = imgEngineRight2Short;
						engineImageCounter = 0;
					}
				}else {
					if(engineImageCounter < 5) {
						showEngineImage = imgEngineRight2Short;
					}else if(engineImageCounter >= 5 && engineImageCounter < 9) {
						showEngineImage = null;
					}else {
						showEngineImage = null;
						engineImageCounter = 0;
					}
				}
				
//				飛機陰影狀態。
				fighterShadowCount = 4;
				
			}else if(fighterRightImageCounter > 4) {
				showFighterImage = imgFighterRight1;
				
//				飛機引擎狀態：
				if(direction == "3") {
					showEngineImage = imgEngineRight1Long;
					engineImageCounter = 0;
				}else if(direction == "6") {
					showEngineImage = imgEngineRight1Long;
					engineImageCounter = 0;
				}else {
					showEngineImage = imgEngineRight1Short;
					engineImageCounter = 0;
				}
				
//				飛機陰影狀態。
				fighterShadowCount = 3;
				
			}else {
				showFighterImage = imgFigherCenter;
				
//				飛機陰影狀態。
				fighterShadowCount = 0;
			}				
			
			break;
		}	

	}	
	
	
	
	
	
	
	
//	可以重設飛機的速度值。
	public int getFighterSpeed() {
		return fighterSpeed;
	}

	public void setFighterSpeed(int fighterSpeed) {
		this.fighterSpeed = fighterSpeed;
	}
	
//	取得飛機物件的座標。
	public int getEntityPositionX() {
		return entityPositionX;
	}
	
	public int getEntityPositionY() {
		return entityPositionY;
	}
	
//	取得飛機物件的中心座標。
	public int getEntityCenterPositionX() {
		return entityPositionX + (mainPanel.getRealUnitSize()/2);
	}
//	取得Y軸位置。
	public int getEntityCenterPositionY() {
		return entityPositionY + (mainPanel.getRealUnitSize()/2);
	}
//	取得半徑長度。
	public double getRadius() {
		return (Math.sqrt(Math.pow((mainPanel.getRealUnitSize()/2), 2)  + Math.pow((mainPanel.getRealUnitSize()/2), 2)));
	}
	
	

}
