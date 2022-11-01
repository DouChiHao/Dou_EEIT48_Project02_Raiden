package tw.com.interactive;

import java.awt.Graphics2D;
import java.util.TimerTask;

import tw.com.battle.BulletManager;
import tw.com.raiden.MainPanel;
import tw.com.scenes.TilemapManager;

public class EnemyEntity extends TimerTask {

	public double enemyPositionX,enemyPositionY;  // 位置。		
	public int enemyWidth,enemyHeight;	// 尺寸。
	
	public double moveSpeed,moveSpeedX,moveSpeedY;
	public int enemyStateStage;
	public double targetPositionX,targetPositionY;
	
	public int enemyblood;
	public int isHitCounter;
	public int enemyScore;
	
	public int enemyNum;
	
	public MainPanel mainPanel;
	public TilemapManager tilemapManager;
	public Fighter fighter;
	public BulletManager bulletManager;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public void draw(Graphics2D g2d) {		
		
	}
	
	public void beHit(int bulletAttack) {
		
		enemyblood-=bulletAttack;
		isHitCounter = 1;
		if(enemyblood <= 0) {
			enemyblood = 0;
		}	
		
	}
	
	
	public boolean destroyEnemy() {
		
		return false;
	}
	
	

//	取得X軸位置。
	public int getEnemyCenterPositionX() {
		return (int)enemyPositionX + (enemyWidth/2);
	}
//	取得Y軸位置。
	public int getEnemyCenterPositionY() {
		return (int)enemyPositionY + (enemyHeight/2);
	}
//	取得半徑長度。
	public double getRadius() {
		return (Math.sqrt(Math.pow((enemyWidth/2), 2)  + Math.pow((enemyHeight/2), 2)));
	}
	public int getEnemyblood() {
		return enemyblood;
	}
//	取得敵機得分。
	public int getEnemyScore() {
		return enemyScore;
	}
	
}
