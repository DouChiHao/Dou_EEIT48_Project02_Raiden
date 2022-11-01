package tw.com.battle;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Timer;

import javax.imageio.ImageIO;

import tw.com.raiden.MainPanel;

public class BulletManager {
	
//	取得主畫面。
	private MainPanel mainPanel;
	
//	放置全部子彈的資料結構。
	private LinkedList<BulletEntity> allBulletList;
//	放置全部的敵方子彈資料結構。
	private LinkedList<BulletEntity> allEnemyBulletList;
	
	public BulletManager(MainPanel mainPanel) {		
		this.mainPanel = mainPanel;
		
		allBulletList = new LinkedList<>();
		allEnemyBulletList = new LinkedList<>();
		
	}	

	public void doInit() {
		for(Iterator<BulletEntity> iterBullet = allBulletList.iterator(); iterBullet.hasNext();) {
			BulletEntity bullet = iterBullet.next();
			bullet.cancel();
			iterBullet.remove();
		}
		allBulletList.clear();
		for(Iterator<BulletEntity> iterEnemyBullet = allEnemyBulletList.iterator(); iterEnemyBullet.hasNext();) {
			BulletEntity enemybullet = iterEnemyBullet.next();
			enemybullet.cancel();
			iterEnemyBullet.remove();
		}
		allEnemyBulletList.clear();		
	}
	
	public void createBullet(int fighterPositionX,int fighterPositionY,int fighterMainBulletlevel) {

//		建立子彈物件並將飛機位置傳入。
		BulletFiregod bulletFiregod = new BulletFiregod(fighterPositionX, fighterPositionY, fighterMainBulletlevel, mainPanel,mainPanel.getTilemapManager());
		mainPanel.getFpsTimer().schedule(bulletFiregod, 0, (int)(1/mainPanel.getFps()*1000));
		allBulletList.add(bulletFiregod);
		
	}
	
	public void creatEnemyBullet(int[] bulletPosition) {
		
//		建立敵方子彈物件。
		BulletArtillery bulletArtillery = new BulletArtillery(bulletPosition, mainPanel, mainPanel.getTilemapManager());
		mainPanel.getFpsTimer().schedule(bulletArtillery, 0, (int)(1/mainPanel.getFps()*1000));
		allEnemyBulletList.add(bulletArtillery);
		
	}
	
	public void draw(Graphics2D g2d) {
		

		for(int i = 0; i < allEnemyBulletList.size(); i++) {
			BulletEntity enemybullet = allEnemyBulletList.get(i);
			enemybullet.draw(g2d);
		}
		
		for(int i = 0; i < allBulletList.size(); i++) {
			BulletEntity bullet = allBulletList.get(i);
			bullet.draw(g2d);
		}
			
	}
	
	public void destroyBullet() {
		
//		當linkedlist在進行尋訪時，不能進行新增或移除，如果要進行新增或移除，要使用跌代器。
		for(Iterator<BulletEntity> iterBullet = allBulletList.iterator(); iterBullet.hasNext();) {
			BulletEntity bullet = iterBullet.next();
			if(bullet.destroyBullet()) {
				bullet.cancel();
				iterBullet.remove();
			}
		}
		
//		檢查敵機子彈，超出畫面銷毀。
		for(Iterator<BulletEntity> iterEnemyBullet = allEnemyBulletList.iterator(); iterEnemyBullet.hasNext();) {
			BulletEntity enemybullet = iterEnemyBullet.next();
			if(enemybullet.destroyBullet()) {
				enemybullet.cancel();
				iterEnemyBullet.remove();
			}
		}
		
	}


//	可以取得所有射出子彈。
	public LinkedList<BulletEntity> getAllBulletList() {
		return allBulletList;
	}
	public LinkedList<BulletEntity> getAllEnemyBulletList(){
		return allEnemyBulletList;
	}

	
	
	
	
	
	
}
