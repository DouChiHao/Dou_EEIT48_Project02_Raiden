package tw.com.interactive;

import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.CopyOnWriteArrayList;

import tw.com.battle.BulletEntity;
import tw.com.battle.BulletManager;
import tw.com.raiden.MainPanel;
import tw.com.scenes.TilemapManager;

public class EnemyManager {

	private MainPanel mainPanel;
	private TilemapManager tilemapManager;
	private Fighter fighter;
	private BulletManager bulletManager;
	
//	關卡邏輯。
	private boolean creatEnemyCoolDown;
	private int creatEnemyCoolDownCounter;
	private int enemyScore;  // 10,50,100(分數)
	
//	創造的敵機編號。
	private int enemyNum;
	
//	蒐集畫面中所有敵人的資料結構。
	private LinkedList<EnemyEntity> allEnemyList;
	
	public EnemyManager(TilemapManager tilemapManager,MainPanel mainPanel,Fighter fighter,BulletManager bulletManager) {
		
		this.tilemapManager = tilemapManager;
		this.mainPanel = mainPanel;
		this.fighter = fighter;
		this.bulletManager = bulletManager;
		
		allEnemyList = new LinkedList<>();
		enemyNum = 0;
		
		creatEnemyCoolDown = true;
		creatEnemyCoolDownCounter = 0;
		enemyScore = 10;
	}	
	
	public void update() {

		if(mainPanel.startEventScript) {
//			要開始進行關卡邏輯判斷。
			eventScript();
		}
		
		destroyEnemy();
		
	}
	
	public void doInit() {
		creatEnemyCoolDown = true;
		creatEnemyCoolDownCounter = 0;
		enemyScore = 10;
		enemyNum = 0;
		
		for(Iterator<EnemyEntity> iterEnemy = allEnemyList.iterator(); iterEnemy.hasNext();) {
			EnemyEntity enemy = iterEnemy.next();
			enemy.cancel();
			iterEnemy.remove();
		}		
		allEnemyList.clear();
		
	}
	
	public void eventScript() {
		
		if(!creatEnemyCoolDown) {
			creatEnemyCoolDownCounter--;
			if(creatEnemyCoolDownCounter<=0) {
				creatEnemyCoolDownCounter = 0;
				creatEnemyCoolDown = true;
			}
		}
		
//		可以開始生成敵人時機點。
		if(tilemapManager.getTilemapPositionY() >= (-410)) {
//			不在生成敵人，準備結束。
			if(allEnemyList.size() == 0) {
//				玩家任務完成，關卡結束，所以是2。
				mainPanel.endEventScript = 2;
			}
			
			creatEnemyCoolDown = false;
		}else if(tilemapManager.getTilemapPositionY() > (-3000) && creatEnemyCoolDown && allEnemyList.size() < 5) {
			
			enemyScore = 100;
			createEnemy();
			
			creatEnemyCoolDown = false;
			creatEnemyCoolDownCounter = (int)((Math.random()*91)+30);
		}else if(tilemapManager.getTilemapPositionY() > (-6000) && creatEnemyCoolDown && allEnemyList.size() < 5) {
			
			enemyScore = 50;
			createEnemy();
			
			creatEnemyCoolDown = false;
			creatEnemyCoolDownCounter = (int)((Math.random()*91)+30);
		}else if(tilemapManager.getTilemapPositionY() > (-9000) && creatEnemyCoolDown && allEnemyList.size() < 5) {
			
			enemyScore = 10;
			createEnemy();
			
			creatEnemyCoolDown = false;
			creatEnemyCoolDownCounter = (int)((Math.random()*91)+30);
		}
		
	}
	
	public void createEnemy() {		
		EnemyLightshooter enemyLightshooter = new EnemyLightshooter(mainPanel,tilemapManager,fighter,bulletManager,enemyNum,enemyScore);
		mainPanel.getFpsTimer().schedule(enemyLightshooter, 0, (int)(1/mainPanel.getFps()*1000));
		allEnemyList.add(enemyLightshooter);
		enemyNum++;
	}
	
	public void draw(Graphics2D g2d) {		
		
		for(int i = 0; i < allEnemyList.size(); i++) {
			EnemyEntity enemy = allEnemyList.get(i);
			enemy.draw(g2d);
		}
			
	}
	
	
	public void destroyEnemy() {
//		檢查敵機，血量歸零時銷毀。
		for(Iterator<EnemyEntity> iterEnemy = allEnemyList.iterator(); iterEnemy.hasNext();) {
			EnemyEntity enemy = iterEnemy.next();
			if(enemy.destroyEnemy()) {
				enemy.cancel();
				iterEnemy.remove();
//--------------------------bgm
				mainPanel.playSE(5);
			}
			if(mainPanel.endEventScript != 0) {
				enemy.cancel();
			}
		}
	}	
	
//	可以取得所有敵機。
	public LinkedList<EnemyEntity> getAllEnemyList() {
		return allEnemyList;
	}
	
}
