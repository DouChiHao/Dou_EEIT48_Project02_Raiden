package tw.com.interactive;

import tw.com.battle.BulletManager;

//宣告互動物件實體，包含玩家、敵方等......接繼承此類別。
public class Entity {
	
	public BulletManager bulletManager;
	
//	設定在畫面的位置、速度。
	public int entityPositionX;  
	public int entityPositionY;	
	public int entitySpeed;
	
	public int fighterBlood;	
	public int isHitCounter;
	
	public void beHit(int bulletAttack) {
		
		fighterBlood-=bulletAttack;
		isHitCounter = 1;
		if(fighterBlood <= 0) {
			fighterBlood = 0;
		}	
		
	}
	
	public int getIsHitCounter() {
		return isHitCounter;
	}
}
