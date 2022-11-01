package tw.com.battle;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import tw.com.raiden.MainPanel;
import tw.com.scenes.TilemapManager;

public class BulletFiregod extends BulletEntity{
	
//	取得子彈圖片。
	private BufferedImage imgFiregodLevel1; 
	private BufferedImage[] imgDead;
	private int isDeadWidth,isDeadHeight,isDeadCounter;
	
	public BulletFiregod(int fighterPositionX,int fighterPositionY,int bulletImageNum,MainPanel mainPanel,TilemapManager tilemapManager) {
		
		this.fighterPositionX = fighterPositionX;
		this.fighterPositionY = fighterPositionY;
		this.bulletImageNum = bulletImageNum;
		this.mainPanel = mainPanel;
		this.tilemapManager = tilemapManager;
		
		setBasicValues();
		getBulletImage();
	}
	
	public void setBasicValues() {
		
		switch(bulletImageNum) {
		case 1:
			this.bulletWidth = 18;
			this.bulletHeight = 18;
			this.moveSpeed = 15;
			this.startOffsetX = this.startOffsetY = this.endOffsetX = 0;
			
			this.bulletPositionX = fighterPositionX + ((mainPanel.getRealUnitSize()/2)-(bulletWidth/2));
			this.bulletPositionY = fighterPositionY - (bulletHeight/2);
			
			this.bulletAttack = 1;		
			
			imgDead = new BufferedImage[3];			
			
			break;
		default:
			System.out.println("bulletImage Error");
			break;
		}
		
		isDead = 0;  // 沒死狀態。 0:沒死 1~4:死亡演繹中 5:演完可以被清除
		isDeadWidth = 36;
		isDeadHeight = 39;
		isDeadCounter = 0;
	}
	
	public void getBulletImage() {
		
		try {
			imgFiregodLevel1 = ImageIO.read(getClass().getResourceAsStream("/BattleEffect/Bullet_Firegod1.png"));
			for(int i = 0; i < imgDead.length; i++) {
				imgDead[i] = ImageIO.read(getClass().getResourceAsStream("/BattleEffect/Bullet_Firegod_Dead" + i + ".png"));
			}			
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		
	}	

	@Override
	public void run() {
		
//		沒死就繼續移動。
		if(isDead == 0) {
			bulletPositionY -= moveSpeed;
		}else if(isDead == 1){
			bulletPositionX = bulletPositionX - ((isDeadWidth/2)-(bulletWidth/2));
			bulletPositionY = bulletPositionY - ((isDeadHeight/2)-(bulletHeight/2));
			isDead++;
		}else {
			bulletHit();
		}
		
//		如果地圖有跟著移動，則控制子彈隨著地圖左右位移。
		if(tilemapManager.getIsMoveX() == 2) {
			bulletPositionX += tilemapManager.getMoveSpeed();
		}else if(tilemapManager.getIsMoveX() == 1) {
			bulletPositionX -= tilemapManager.getMoveSpeed();
		}
		
	}
	
	@Override
	public void bulletHit() {	
		
		if(isDeadCounter < 3) {
			isDead = 2;
		}else if(isDeadCounter >= 3 && isDeadCounter < 6) {
			isDead = 3;
		}else if(isDeadCounter >= 6 && isDeadCounter < 9) {
			isDead = 4;
		}else {
			isDead = 5;
		}			
			
		isDeadCounter++;
	}	
	
	@Override
	public void draw(Graphics2D g2d) {
		
		switch(isDead) {
		case 0:case 1:
			g2d.drawImage(imgFiregodLevel1, bulletPositionX, bulletPositionY, bulletWidth, bulletHeight, null);
			break;
		case 2:
			g2d.drawImage(imgDead[0], bulletPositionX, bulletPositionY, isDeadWidth, isDeadHeight, null);
			break;
		case 3:
			g2d.drawImage(imgDead[1], bulletPositionX, bulletPositionY, isDeadWidth, isDeadHeight, null);
			break;
		case 4:
			g2d.drawImage(imgDead[2], bulletPositionX, bulletPositionY, isDeadWidth, isDeadHeight, null);
			break;
		case 5:
			break;
		default:
			System.out.println("狀態錯誤");
			break;
		}		
		
	}	
	
	@Override
	public boolean destroyBullet() {
		if(bulletPositionX <= (bulletWidth*-1)) {
			return true;
		}else if(bulletPositionX >= mainPanel.getWindowWidth()) {
			return true;
		}
		
		if(bulletPositionY <= (bulletHeight*-1)) {
			return true;
		}else if(bulletPositionY >= mainPanel.getWindowHight()) {
			return true;
		}
		
		if(isDead == 5) {
			return true;
		}
		
		
		return false;
	}	




}
