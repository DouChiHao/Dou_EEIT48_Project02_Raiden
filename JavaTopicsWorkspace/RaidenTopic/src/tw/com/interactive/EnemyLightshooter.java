package tw.com.interactive;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import tw.com.battle.BulletManager;
import tw.com.raiden.MainPanel;
import tw.com.scenes.TilemapManager;

public class EnemyLightshooter extends EnemyEntity {
	
	private BufferedImage[] imgLightshooter;
	private BufferedImage[] imgLightshooterHit;
	private BufferedImage[] imgLightshooterExplosion;
	private BufferedImage[] imgLightshooterShadow;
	private BufferedImage[] imgEngineL;
	private BufferedImage[] imgEngineS;
	
	private BufferedImage[] imgShootFirelight;
	private BufferedImage[] showShootFirelight;
	
	private int shadowWidth,shadowHeight,shadowOffsetX,shadowOffsetY;
	
	private boolean positionXArrival,positionYArrival;
	
	private int enemyStayCounter,enemyStayTime;
	
	private int relativelyAngle;  // 當前敵機面對的方向。
	
	private int engineImageCounter;  // 引擎換圖計數。
	
	private int enemyShootDecideCounter,enemyShootDecideTime;
	private int enemyShootNum,enemyAlreadyShootNum;
	private int enemyShootCooldawnCounter,enemyShootCooldawnTime;
	private int firelightWidth,firelightHeight;
	private int[] firelightPosition;
	
	private int[] bulletPosition;  // 子彈位置資訊。
	private int bulletMoveSpeed;  // 子彈速度。
	
	private boolean isExplosion;
	private int isExplosionCounter;

	public EnemyLightshooter(MainPanel mainPanel,TilemapManager tilemapManager,Fighter fighter,BulletManager bulletManager,int enemyNum,int enemyScore) {
		this.mainPanel = mainPanel;
		this.tilemapManager = tilemapManager;
		this.fighter = fighter;
		this.bulletManager = bulletManager;
		this.enemyNum = enemyNum;
		this.enemyScore = enemyScore;
		
		setBasicValues();
		getEnemyImage();
	}
	
	public void setBasicValues() {
		this.enemyWidth = 90;
		this.enemyHeight = 96;
		this.moveSpeed = 4;
		this.enemyStateStage = 1;  // (狀態1)移動到指定位置,(狀態2)待機一段時間。
		
		imgLightshooter = new BufferedImage[16];
		imgLightshooterShadow = new BufferedImage[16];
		imgEngineL = new BufferedImage[16];
		imgEngineS = new BufferedImage[16];
		engineImageCounter = 0;
		
		shadowWidth = shadowHeight = 48;  // 陰影的長寬。
		shadowOffsetX = mainPanel.getRealUnitSize() + (mainPanel.getRealUnitSize()/2);
		shadowOffsetY = mainPanel.getRealUnitSize() - shadowHeight; 		
		
		relativelyAngle = 0;  // 當前旋轉的角度。	
		positionXArrival = false;  // X軸是否到達目標位置。
		positionYArrival = false;  // Y軸是否到達目標位置。
		
		imgShootFirelight = new BufferedImage[2];
		showShootFirelight = new BufferedImage[2];
		enemyShootDecideCounter = 0;
		enemyAlreadyShootNum = 0;
		enemyShootCooldawnCounter = 0;
		enemyShootCooldawnTime = 5;  // 射擊間隔5tick。
		firelightWidth = 36;
		firelightHeight = 33;
		firelightPosition = new int[4];
		
		bulletPosition = new int[4];
		bulletMoveSpeed = 15;  // 子彈移動速度預先抓與飛機相同。
		
//		設定基本血量。
		this.enemyblood = 3;
		this.isHitCounter = 0;
		imgLightshooterHit = new BufferedImage[16];
		imgLightshooterExplosion = new BufferedImage[16];
		isExplosion = false;  // 是否爆炸。
		isExplosionCounter = 0;  // 爆炸計數。
		
//		設定生成位置(首次生成在畫面外面)。
		createPosition();	
//		設定目標位置,計算距離即取得相對的移動速度X及Y。
		createTargetPosition();
		
	}
	
	public void getEnemyImage() {		
		try {
			
			for(int i = 0; i < imgLightshooter.length; i++) {
				imgLightshooter[i] = ImageIO.read(getClass().getResourceAsStream("/EnemyLightshooter/LightShooter_"+ i +".png"));
				imgLightshooterShadow[i] = ImageIO.read(getClass().getResourceAsStream("/EnemyLightshooter/LightShooter_Shadow_"+ i +".png"));
				imgEngineL[i] = ImageIO.read(getClass().getResourceAsStream("/EnemyLightshooter/LightShooter_FirelighL_"+ i +".png"));
				imgEngineS[i] = ImageIO.read(getClass().getResourceAsStream("/EnemyLightshooter/LightShooter_FirelighS_"+ i +".png"));
				imgLightshooterHit[i] = ImageIO.read(getClass().getResourceAsStream("/EnemyLightshooter/LightShooter_Hit"+ i +".png"));
				imgLightshooterExplosion[i] = ImageIO.read(getClass().getResourceAsStream("/BattleEffect/Enemy_LightShooter_Explosion"+ i +".png"));
				if(i < 2) {
					imgShootFirelight[i] = ImageIO.read(getClass().getResourceAsStream("/BattleEffect/Eenmy_Shoot_Firelight"+ i +".png"));
					showShootFirelight[i] = null;
				}
			}
			
			
			
		}catch(Exception e) {
			System.out.println(e.toString());
		}
	}
	
	public void createPosition() {
		
//		0,left ; 1,center ; 2,right
		int createDirection = (int)(Math.random()*3);
		
		switch(createDirection) {
		case 0:
			
			this.enemyPositionX = enemyWidth*(-1);
			this.enemyPositionY = (Math.random()*(mainPanel.getWindowHight()/3-enemyHeight));  // 0 ~ 159
			
			break;
		case 1:
			
			this.enemyPositionY = enemyHeight*(-1);
			this.enemyPositionX = (Math.random()*(mainPanel.getWindowHight()-enemyWidth));  // 0 ~ 582
			
			break;
		case 2:
			
			this.enemyPositionX = mainPanel.getWindowWidth();
			this.enemyPositionY = (Math.random()*(mainPanel.getWindowHight()/3-enemyHeight));  // 0 ~ 159
			
			break;
		default:
			System.out.println("位置錯誤");
			break;		
		}		

		
	}	
	
	public void createTargetPosition() {
		
//		目標位置為畫面中的上半3/2，且不會切再畫面邊緣。
		this.targetPositionX = ((Math.random()*(mainPanel.getWindowWidth()-enemyWidth*3))+enemyWidth);
		this.targetPositionY = ((Math.random()*(mainPanel.getWindowHight()/3*2-(enemyHeight*2)))+enemyHeight);
		
//		計算距離：
		double xDistance = targetPositionX - enemyPositionX;
		if(xDistance == 0) {
			xDistance += enemyWidth;
			System.out.println("敵機X目標出現0");
		}			
		double yDistance = targetPositionY - enemyPositionY;
		if(yDistance == 0) {
			yDistance += enemyHeight;
			System.out.println("敵機Y目標出現0");
		} 
		double straightDistance = (Math.sqrt(xDistance*xDistance + yDistance*yDistance));
		double straightMoveTime = straightDistance/moveSpeed;  // 整數除取得整數，取得直線移動時需要的時間(小數不管)。
		this.moveSpeedX = xDistance/straightMoveTime;
		this.moveSpeedY = yDistance/straightMoveTime;		
		

	}	
	
	@Override
	public void run() {
		
//		檢查敵人的血量是否大於0
		if(enemyblood <= 0) isExplosion = true;		
		
		switch(enemyStateStage) {
		case 1:
//			生成目標後，持續將目標移動至指定位置。
//			X軸移動。
			if(targetPositionX >= enemyPositionX && !positionXArrival) {
				enemyPositionX += moveSpeedX;				
				if(enemyPositionX >= targetPositionX && !isExplosion) {
					enemyPositionX = targetPositionX;
					positionXArrival = true;
				}
			}else if(targetPositionX <= enemyPositionX && !positionXArrival) {
				enemyPositionX += moveSpeedX;		
				if(enemyPositionX <= targetPositionX  && !isExplosion) {
					enemyPositionX = targetPositionX;
					positionXArrival = true;
				}
			}
			
//			Y軸移動。
			if(targetPositionY >= enemyPositionY && !positionYArrival) {
				enemyPositionY += moveSpeedY;
				if(enemyPositionY >= targetPositionY  && !isExplosion) {
					enemyPositionY = targetPositionY;
					positionYArrival = true;
				}
			}else if(targetPositionY <= enemyPositionY && !positionYArrival) {
				enemyPositionY += moveSpeedY;
				if(enemyPositionY <= targetPositionY  && !isExplosion) {
					enemyPositionY = targetPositionY;
					positionYArrival = true;
				}
			}
			
			if(positionXArrival && positionYArrival  && !isExplosion) {
				enemyStayCounter = 0;
				enemyStayTime = (int)((Math.random()*181)+60);  // 隨機指定停留 1 ~ 3 秒。
//				enemyStayTime = 360000;
				enemyStateStage = 2;
			}			
			
			break;
		case 2:
			
			enemyStayCounter++;
			
			if(enemyStayCounter >= enemyStayTime  && !isExplosion) {
				createTargetPosition();
				positionXArrival = false;
				positionYArrival = false;
				enemyStateStage = 1;
			}
			
			break;
		default:
			System.out.println("狀態錯誤!");
			break;
		}
		
//		如果地圖有跟著移動，則控制飛機隨著地圖左右位移。
		if(tilemapManager.getIsMoveX() == 2) {
			enemyPositionX += tilemapManager.getMoveSpeed();
			targetPositionX += tilemapManager.getMoveSpeed();
		}else if(tilemapManager.getIsMoveX() == 1) {
			enemyPositionX -= tilemapManager.getMoveSpeed();
			targetPositionX -= tilemapManager.getMoveSpeed();
		}
		
//		計算及檢查敵機(以敵機為主)，與我機相對位置。
		relativelyCount();	
		
		
//		攻擊判定(在畫面內才能進行射擊)。
		if(enemyPositionX >= 0 && enemyPositionX <= (mainPanel.getWidth()-enemyWidth) && enemyPositionY >= 0  && !isExplosion) {
			enemyShootDecideCounter++;
			
			if(enemyShootDecideCounter >= enemyShootDecideTime) {
//				決定進行射擊的冷卻時間抵達。
				
				if(enemyShootCooldawnCounter < enemyShootCooldawnTime) {
					enemyShootCooldawnCounter++;
			
				}else if(enemyShootCooldawnCounter >= enemyShootCooldawnTime){
//					射擊間隔冷卻時間到達，且還沒射擊至指定數量。。
					enemyAlreadyShootNum++;					
//					印出圖片。
					showShootFirelight[0] = imgShootFirelight[0];					
					enemyShootCooldawnCounter = 0;  // 重置計時器。
					
//					呼叫子彈管理，創造子彈。
					bulletManager.creatEnemyBullet(creatBulletPosition(0));					
					bulletManager.creatEnemyBullet(creatBulletPosition(1));	
//------------------BGM
					mainPanel.playSE(4);
					if(enemyAlreadyShootNum == enemyShootNum) {
//						射擊數量已經射擊完畢，重置決定項目。
						enemyShootDecideCounter = 0;
						enemyShootDecideTime = (int)(Math.random()*121)+60;  // 隨機1~3秒。
						enemyShootNum = (int)(Math.random()*5)+1;  // 隨機1~5發子彈。
						enemyAlreadyShootNum = 0;
						enemyShootCooldawnCounter = enemyShootCooldawnTime;
					}
				}				
			}			
		}else {
			enemyShootDecideCounter = 0;
			enemyShootDecideTime = (int)(Math.random()*121)+60;  // 隨機1~3秒。
			enemyShootNum = (int)(Math.random()*5)+1;  // 隨機1~5發子彈。
//			enemyShootNum = 10000;
			enemyAlreadyShootNum = 0;
			enemyShootCooldawnCounter = enemyShootCooldawnTime;
		}
		
		
	}	
	
	public void relativelyCount() {
		double relativelyY = enemyPositionY - fighter.getEntityPositionY();
		double relativelyX = enemyPositionX - fighter.getEntityPositionX();
		double tan = 90;
		
		if(relativelyY != 0.0 && relativelyX != 0.0) {
			tan = Math.atan(relativelyY/relativelyX)*180/Math.PI;			
		}
		
		
		if(enemyPositionX <= fighter.getEntityPositionX() && enemyPositionY < fighter.getEntityPositionY()) {
//			第四象限，含X=0
			
			if(tan <= 90 && tan > 78.75) {
				relativelyAngle = 0;
			}else if(tan <= 78.75 && tan > 56.25) {
				relativelyAngle = 15;
			}else if(tan <= 56.25 && tan > 33.75) {
				relativelyAngle = 14;
			}else if(tan <= 33.75 && tan > 11.25) {
				relativelyAngle = 13;
			}else if(tan <= 11.25 && tan > 0) {
				relativelyAngle = 12;
			}
			
		}
		
		if(enemyPositionX < fighter.getEntityPositionX() && enemyPositionY >= fighter.getEntityPositionY()) {
//			第一象限，含Y=0
			
			if(tan <= 0 && tan > -11.25) {
				relativelyAngle = 12;
			}else if(tan <= -11.25 && tan > -33.75) {
				relativelyAngle = 11;
			}else if(tan <= -33.75 && tan > -56.25) {
				relativelyAngle = 10;
			}else if(tan <= -56.25 && tan > -78.75) {
				relativelyAngle = 9;
			}else if(tan <= -78.75 && tan > -90) {
				relativelyAngle = 8;
			}
						
		}
		
		if(enemyPositionX >= fighter.getEntityPositionX() && enemyPositionY > fighter.getEntityPositionY()) {
//			第二象限，含X=0
			
			if(tan <= 90 && tan > 78.75) {
				relativelyAngle = 8;
			}else if(tan <= 78.75 && tan > 56.25) {
				relativelyAngle = 7;
			}else if(tan <= 56.25 && tan > 33.75) {
				relativelyAngle = 6;
			}else if(tan <= 33.75 && tan > 11.25) {
				relativelyAngle = 5;
			}else if(tan <= 11.25 && tan > 0) {
				relativelyAngle = 4;
			}
			
		}
		
		if(enemyPositionX > fighter.getEntityPositionX() && enemyPositionY <= fighter.getEntityPositionY()) {
//			第三象限，含X=0
			
			if(tan <= 0 && tan > -11.25) {
				relativelyAngle = 4;
			}else if(tan <= -11.25 && tan > -33.75) {
				relativelyAngle = 3;
			}else if(tan <= -33.75 && tan > -56.25) {
				relativelyAngle = 2;
			}else if(tan <= -56.25 && tan > -78.75) {
				relativelyAngle = 1;
			}else if(tan <= -78.75 && tan > -90) {
				relativelyAngle = 0;
			}			
			
		}
			
	}
	
	public int[] shootPostion() {
		
		switch(relativelyAngle) {
		case 0:				
			firelightPosition[0] = (int)enemyPositionX + 10;
			firelightPosition[1] = (int)enemyPositionY + 68;
			firelightPosition[2] = (int)enemyPositionX + 50;
			firelightPosition[3] = (int)enemyPositionY + 68;
			break;
		case 1:	
			firelightPosition[0] = (int)enemyPositionX + (-5);
			firelightPosition[1] = (int)enemyPositionY + 59;
			firelightPosition[2] = (int)enemyPositionX + 35;
			firelightPosition[3] = (int)enemyPositionY + 74;
			break;
		case 2:	
			firelightPosition[0] = (int)enemyPositionX + (-10);
			firelightPosition[1] = (int)enemyPositionY + 42;
			firelightPosition[2] = (int)enemyPositionX + 22;
			firelightPosition[3] = (int)enemyPositionY + 68;
			break;
		case 3:	
			firelightPosition[0] = (int)enemyPositionX + (-16);
			firelightPosition[1] = (int)enemyPositionY + 29;
			firelightPosition[2] = (int)enemyPositionX + 5;
			firelightPosition[3] = (int)enemyPositionY + 62;
			break;
		case 4:	
			firelightPosition[0] = (int)enemyPositionX + (-10);
			firelightPosition[1] = (int)enemyPositionY + 9 - 4;
			firelightPosition[2] = (int)enemyPositionX + (-10);
			firelightPosition[3] = (int)enemyPositionY + 48;
			break;
		case 5:	
			firelightPosition[0] = (int)enemyPositionX + 3;
			firelightPosition[1] = (int)enemyPositionY + (-4) - 6;
			firelightPosition[2] = (int)enemyPositionX + (-17);
			firelightPosition[3] = (int)enemyPositionY + 26 - 4;
			break;
		case 6:	
			firelightPosition[0] = (int)enemyPositionX + 21;
			firelightPosition[1] = (int)enemyPositionY + (-8) - 5;
			firelightPosition[2] = (int)enemyPositionX + (-10);
			firelightPosition[3] = (int)enemyPositionY + 17 - 6;
			break;
		case 7:	
			firelightPosition[0] = (int)enemyPositionX + 37 - 1;
			firelightPosition[1] = (int)enemyPositionY + (-18) - 4;
			firelightPosition[2] = (int)enemyPositionX + (-6);
			firelightPosition[3] = (int)enemyPositionY + (-5) - 4;
			break;
		case 8:	
			firelightPosition[0] = (int)enemyPositionX + 50;
			firelightPosition[1] = (int)enemyPositionY + (-11) - 6;
			firelightPosition[2] = (int)enemyPositionX + 10;
			firelightPosition[3] = (int)enemyPositionY + (-11) - 6;
			break;
		case 9:	
			firelightPosition[0] = (int)enemyPositionX + 67 - 3;
			firelightPosition[1] = (int)enemyPositionY + (-2) - 4;
			firelightPosition[2] = (int)enemyPositionX + 26;
			firelightPosition[3] = (int)enemyPositionY + (-18) - 4;
			break;
		case 10:	
			firelightPosition[0] = (int)enemyPositionX + 71;
			firelightPosition[1] = (int)enemyPositionY + 13 - 4;
			firelightPosition[2] = (int)enemyPositionX + 40;
			firelightPosition[3] = (int)enemyPositionY + (-11) - 4;
			break;
		case 11:	
			firelightPosition[0] = (int)enemyPositionX + 78;
			firelightPosition[1] = (int)enemyPositionY + 28 - 4;
			firelightPosition[2] = (int)enemyPositionX + 56;
			firelightPosition[3] = (int)enemyPositionY + (-7) - 4;
			break;
		case 12:	
			firelightPosition[0] = (int)enemyPositionX + 71;
			firelightPosition[1] = (int)enemyPositionY + 48;
			firelightPosition[2] = (int)enemyPositionX + 71;
			firelightPosition[3] = (int)enemyPositionY + 9 - 4;
			break;
		case 13:	
			firelightPosition[0] = (int)enemyPositionX + 58 - 2;
			firelightPosition[1] = (int)enemyPositionY + 63;
			firelightPosition[2] = (int)enemyPositionX + 79 - 2;
			firelightPosition[3] = (int)enemyPositionY + 32;
			break;
		case 14:	
			firelightPosition[0] = (int)enemyPositionX + 41;
			firelightPosition[1] = (int)enemyPositionY + 66;
			firelightPosition[2] = (int)enemyPositionX + 71;
			firelightPosition[3] = (int)enemyPositionY + 42;
			break;
		case 15:	
			firelightPosition[0] = (int)enemyPositionX + 24;
			firelightPosition[1] = (int)enemyPositionY + 75;
			firelightPosition[2] = (int)enemyPositionX + 68;
			firelightPosition[3] = (int)enemyPositionY + 62;
			break;
			
		default:
			System.out.println("方向錯誤");
			break;		
		}
		
		firelightPosition[0] -= 3;
		firelightPosition[1] += 5;
		firelightPosition[2] -= 3;
		firelightPosition[3] += 5;
		
		return firelightPosition;
		
	}
	
	public int[] creatBulletPosition(int gunBarrel) {		
		
		double xDistance = (shootPostion()[0]+9) - (fighter.getEntityPositionX()+mainPanel.getRealUnitSize()/3);  // 取得X軸的斜線。
		if(xDistance == 0) {
			xDistance += 1;
		}
		double yDistance = (shootPostion()[1]+9) - (fighter.getEntityPositionY()+mainPanel.getRealUnitSize()/2);  // 取得Y軸的斜線。
		if(yDistance == 0) {
			yDistance += 1;
		}
		double straightDistance = (Math.sqrt(xDistance*xDistance + yDistance*yDistance));  // 取得斜邊長(勾股定理)。
		double straightMoveTime = straightDistance/bulletMoveSpeed;  // 取得直線移動時需要的時間，距離/速度。
		double bulletMoveSpeedX = (xDistance/straightMoveTime)*(-1);
		double bulletMoveSpeedY = (yDistance/straightMoveTime)*(-1);	
		
		if(gunBarrel == 0) {
			bulletPosition[0] = shootPostion()[0]+9;  // 火光的X位置再加上9為子彈位置。
			bulletPosition[1] = shootPostion()[1]+9;  // 火光的Y位置再加上9為子彈位置。
			bulletPosition[2] = (int)bulletMoveSpeedX;
			bulletPosition[3] = (int)bulletMoveSpeedY;
		}else {
			bulletPosition[0] = shootPostion()[2]+9;  // 火光的X位置再加上9為子彈位置。
			bulletPosition[1] = shootPostion()[3]+9;  // 火光的Y位置再加上9為子彈位置。
			bulletPosition[2] = (int)bulletMoveSpeedX;
			bulletPosition[3] = (int)bulletMoveSpeedY;
		}		
		
		return bulletPosition; 
	}
	
	@Override
	public void draw(Graphics2D g2d) {
		
//		敵機引擎圖片計數。
		engineImageCounter++;
		
//		等陰影全部畫完後，再畫飛機。
		for(int i = 0; i < imgLightshooterShadow.length; i++) {
			if(i==relativelyAngle && !isExplosion) {
				g2d.drawImage(imgLightshooterShadow[i], (int)enemyPositionX+shadowOffsetX, (int)enemyPositionY+shadowOffsetY, shadowWidth, shadowHeight, null);
			}
		}
		
		
//		畫出射擊火光圖片,透過檢查圖片是否為null進行0~1的圖片播放。
		if(showShootFirelight[0] != null && showShootFirelight[1] == null && !isExplosion) {
			g2d.drawImage(showShootFirelight[0], shootPostion()[0], shootPostion()[1], firelightWidth, firelightHeight, null);
			g2d.drawImage(showShootFirelight[0], shootPostion()[2], shootPostion()[3], firelightWidth, firelightHeight, null);
			showShootFirelight[1] = imgShootFirelight[1];
		}else if(showShootFirelight[0] != null && showShootFirelight[1] != null && !isExplosion) {
			g2d.drawImage(showShootFirelight[1], shootPostion()[0], shootPostion()[1], firelightWidth, firelightHeight, null);
			g2d.drawImage(showShootFirelight[1], shootPostion()[2], shootPostion()[3], firelightWidth, firelightHeight, null);
			showShootFirelight[0] = null;
		}else {
			showShootFirelight[0] = null;
			showShootFirelight[1] = null;
		}		
		
		
		for(int i = 0; i < imgLightshooter.length; i++) {
			if(i==relativelyAngle && !isExplosion) {
				
//				先繪出對應方向的引擎火光圖片。
				if(engineImageCounter < 3) {
					g2d.drawImage(imgEngineL[i], (int)enemyPositionX, (int)enemyPositionY, enemyWidth, enemyHeight, null);
				}else if(engineImageCounter >= 3 && engineImageCounter < 5) {
					g2d.drawImage(imgEngineS[i], (int)enemyPositionX, (int)enemyPositionY, enemyWidth, enemyHeight, null);
				}else {
					g2d.drawImage(imgEngineS[i], (int)enemyPositionX, (int)enemyPositionY, enemyWidth, enemyHeight, null);
					engineImageCounter = 0;
				}				
				
//				再繪出戰機本身。
				if(isHitCounter != 0) {
					g2d.drawImage(imgLightshooterHit[i], (int)enemyPositionX, (int)enemyPositionY, enemyWidth, enemyHeight, null);
					isHitCounter++;					
					if(isHitCounter>=6) {
						isHitCounter = 0;
					}					
				}else{
					g2d.drawImage(imgLightshooter[i], (int)enemyPositionX, (int)enemyPositionY, enemyWidth, enemyHeight, null);
				}				
			}
		}
		
//		爆炸計數。
		if(isExplosion) {			
			if(isExplosionCounter/2 <= (imgLightshooterExplosion.length-1)) {
				g2d.drawImage(imgLightshooterExplosion[isExplosionCounter/2], (int)enemyPositionX, (int)enemyPositionY, enemyWidth, enemyHeight, null);
				isExplosionCounter++;
			}
		}
		
	}	
	
	@Override
	public boolean destroyEnemy() {
		
		if(isExplosionCounter/2 > (imgLightshooterExplosion.length-1)) {
			return true;
		}else {
			return false;
		}		
		
	}
	
}
