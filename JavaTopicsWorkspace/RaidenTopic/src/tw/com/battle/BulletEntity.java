package tw.com.battle;

import java.awt.Graphics2D;
import java.util.TimerTask;

import tw.com.raiden.MainPanel;
import tw.com.scenes.TilemapManager;

public class BulletEntity extends TimerTask{
	
	public int bulletPositionX,bulletPositionY;  // 位置。	
	public int fighterPositionX,fighterPositionY;

	public int bulletImageNum;
	public int bulletWidth,bulletHeight;	// 尺寸。
	public int startOffsetX,startOffsetY,endOffsetX;  // 偏差值。
	public int moveSpeed;
	public int isDead;
	
//	設定子彈攻擊數值。
	public int bulletAttack;
	
	public MainPanel mainPanel;
	public TilemapManager tilemapManager;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public void bulletHit() {
		
	}
	
	public void draw(Graphics2D g2d) {
		
	}
	
	public boolean destroyBullet() {
		
		return false;
	}
	
//	取得X軸位置。
	public int getBulletCenterPositionX() {
		return bulletPositionX + (bulletWidth/2);
	}
//	取得Y軸位置。
	public int getBulletCenterPositionY() {
		return bulletPositionY + (bulletHeight/2);
	}
//	取得半徑長度。
	public double getRadius() {
		return (Math.sqrt(Math.pow((bulletWidth/2), 2)  + Math.pow((bulletHeight/2), 2)));
	}
	
//	取得子彈攻擊數值。
	public int getBulletAttack() {
		return bulletAttack;
	}
	
//	設定及取得死亡狀態。
	public int getIsDead() {
		return isDead;
	}
	public void setIsDead(int isDead) {
		this.isDead = isDead;
	}
	
	
	
}
