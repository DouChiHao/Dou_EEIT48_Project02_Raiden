package tw.com.battle;

import java.util.Iterator;

import tw.com.interactive.EnemyEntity;
import tw.com.interactive.EnemyManager;
import tw.com.interactive.Fighter;
import tw.com.raiden.MainUI;

public class CheckHit {
	
	private Fighter fighter;
	private EnemyManager enemyManager;
	private BulletManager bulletManager;
	private MainUI mainUI;
	
	public CheckHit(Fighter fighter,EnemyManager enemyManager,BulletManager bulletManager,MainUI mainUI) {
		
		this.fighter = fighter;
		this.enemyManager = enemyManager;
		this.bulletManager = bulletManager;
		this.mainUI = mainUI;
		
	}
	
	public void fighterBulletHit() {		
		
//		迭代所有子彈。
		for(Iterator<BulletEntity> iterBullet = bulletManager.getAllBulletList().iterator(); iterBullet.hasNext();) {
			BulletEntity bullet = iterBullet.next();
			
//			迭代所有敵機。
			for(Iterator<EnemyEntity> iterEnemy = enemyManager.getAllEnemyList().iterator(); iterEnemy.hasNext();) {
				EnemyEntity enemy = iterEnemy.next();
				
				if(
				isHit(bullet.getBulletCenterPositionX(), bullet.getBulletCenterPositionY(), bullet.getRadius(),
						enemy.getEnemyCenterPositionX(), enemy.getEnemyCenterPositionY(), enemy.getRadius())
				) {
					if(bullet.getIsDead() == 0 && enemy.getEnemyblood() > 0) {
						bullet.setIsDead(1);
						enemy.beHit(bullet.getBulletAttack());
						if(enemy.getEnemyblood() == 0) {
							
							mainUI.playerScoreAdd(enemy.getEnemyScore());
						}
					}
					
				}
			}			
			
		}
		
	}
	
	
	public void enemyBulletHit() {
		
//		迭代所有子彈。
		for(Iterator<BulletEntity> iterEnemyBullet = bulletManager.getAllEnemyBulletList().iterator(); iterEnemyBullet.hasNext();) {
			BulletEntity enemybullet = iterEnemyBullet.next();
			
			if(
					isHit(enemybullet.getBulletCenterPositionX(), enemybullet.getBulletCenterPositionY(), enemybullet.getRadius()
					, fighter.getEntityCenterPositionX(), fighter.getEntityCenterPositionY(), fighter.getRadius())
				) {
				if(enemybullet.getIsDead() == 0 && fighter.getIsHitCounter() == 0) {
					enemybullet.setIsDead(1);
					fighter.beHit(enemybullet.getBulletAttack());
				}
			}			
			
		}
		
	}
	
	
	
	public boolean isHit(int selfX,int selfY,double selfRadius,int targetX,int targetY,double targetRadius) {
		
//		計算兩點間距離。(勾股定理第三邊)
		double xDistance = selfX - targetX;
		double yDistance = selfY - targetY;
		double straightDistance = (Math.sqrt(xDistance*xDistance + yDistance*yDistance));
		
		if(straightDistance <= Math.abs(selfRadius-targetRadius)) {
			return true;
		}else {
			return false;
		}
		
	}
	
	

}
