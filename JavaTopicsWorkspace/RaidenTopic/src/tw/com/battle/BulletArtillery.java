package tw.com.battle;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import tw.com.raiden.MainPanel;
import tw.com.scenes.TilemapManager;

public class BulletArtillery extends BulletEntity{
	
	private BufferedImage[] imgArtillery;	
	private BufferedImage[] imgDead;
	private int imgChangeCounter;
	
	public BulletArtillery(int[] bulletPosition,MainPanel mainPanel,TilemapManager tilemapManager) {
		
		this.bulletPositionX = bulletPosition[0];
		this.bulletPositionY = bulletPosition[1];
		this.startOffsetX = bulletPosition[2];
		this.startOffsetY = bulletPosition[3];
		this.mainPanel = mainPanel;
		this.tilemapManager = tilemapManager;
		
		setBasicValues();
		getBulletImage();
	}
	
	public void setBasicValues() {
		this.bulletWidth = 18;
		this.bulletHeight = 15;	
		this.moveSpeed = this.endOffsetX = 0;	
		imgArtillery = new BufferedImage[3];
		imgChangeCounter = 0;
		
		imgDead = new BufferedImage[3];
		isDead = 0;  // 沒死狀態。 0:沒死 1~4:死亡演繹中 5:演完可以被清除
		
//		設定子彈攻擊
		bulletAttack = 1;
//		bulletAttack = 0;
	}
	
	public void getBulletImage() {
		try {			
			for(int i = 0; i < imgArtillery.length; i++) {
				imgArtillery[i] = ImageIO.read(getClass().getResourceAsStream("/BattleEffect/Bullet_Artillery"+ i +".png"));
				imgDead[i] = ImageIO.read(getClass().getResourceAsStream("/BattleEffect/Bullet_Artillery_Dead"+ i +".png"));
			}			
		}catch(Exception e) {
			System.out.println(e.toString());
		}
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(isDead == 0) {
			bulletPositionX += startOffsetX;
			bulletPositionY += startOffsetY;
		}else {
			isDead++;
		}

		
//		如果地圖有跟著移動，則控制子彈隨著地圖左右位移。
		if(tilemapManager.getIsMoveX() == 2) {
			bulletPositionX += tilemapManager.getMoveSpeed();
		}else if(tilemapManager.getIsMoveX() == 1) {
			bulletPositionX -= tilemapManager.getMoveSpeed();
		}
		
	}
	
	
	@Override
	public void draw(Graphics2D g2d) {
		
//		每3tick更換圖片並循環播放(並且沒死)。
		if(isDead == 0) {
			imgChangeCounter++;
			if(imgChangeCounter < 3) {
				g2d.drawImage(imgArtillery[0], bulletPositionX, bulletPositionY, bulletWidth, bulletHeight, null);
			}else if(imgChangeCounter >= 3 && imgChangeCounter < 6) {
				g2d.drawImage(imgArtillery[1], bulletPositionX, bulletPositionY, bulletWidth, bulletHeight, null);
			}else if(imgChangeCounter >= 6 && imgChangeCounter < 8) {
				g2d.drawImage(imgArtillery[2], bulletPositionX, bulletPositionY, bulletWidth, bulletHeight, null);
			}else {
				g2d.drawImage(imgArtillery[2], bulletPositionX, bulletPositionY, bulletWidth, bulletHeight, null);
				imgChangeCounter = 0;
			}
		}else {
			if(isDead <= 3) {
				g2d.drawImage(imgDead[0], bulletPositionX, bulletPositionY, bulletWidth, bulletHeight, null);
			}else if(isDead > 3 && isDead <= 6) {
				g2d.drawImage(imgDead[1], bulletPositionX, bulletPositionY, bulletWidth, bulletHeight, null);
			}else if(isDead > 6 && isDead <= 9) {
				g2d.drawImage(imgDead[2], bulletPositionX, bulletPositionY, bulletWidth, bulletHeight, null);
			}
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
		
		if(isDead > 9) {
			return true;
		}
		
		return false;
	}
	
	
//	可以取得速度值。
	public int getMoveSpeed() {
		return this.moveSpeed;
	}


}
