package tw.com.scenes;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import tw.com.raiden.KeyHandler;
import tw.com.raiden.MainPanel;

public class TilemapManager {
	
	private MainPanel mainPanel;
	private KeyHandler keyHandler;
	
	private BufferedImage imgTilemapStage1Bottom,imgTilemapStage1Top;
	private final int tilemapStage1BottomWidth = 1056;
	private final int tilemapStage1BottomHeight = 10200;
	private final int tilemapStage1TopWidth = 1056;
	private final int tilemapStage1TopHeight = 10200;
	
	private int tilemapPositionX;
	private int tilemapPositionY;
	private int moveSpeed;
	
	private int isMoveX;  // (0)不動,(1)向左,(2)向右
	
	public TilemapManager(MainPanel mainPanel, KeyHandler keyHandler) {
		this.mainPanel = mainPanel;
		this.keyHandler = keyHandler;
		
//		初始化位置。
		setBasicValues();
//		讀取圖檔。
		getTilemapImage();
	}
	
//	設定初始值位置、速度及狀態。
	public void setBasicValues() {
		
		tilemapPositionX = (tilemapStage1TopWidth-mainPanel.getWindowWidth())/2*(-1);
		tilemapPositionY = -tilemapStage1TopHeight + mainPanel.getWindowHight() + 10;
		
		moveSpeed = 4;
		isMoveX = 0;
	}
	
	public void getTilemapImage() {
		
		try {
			
			imgTilemapStage1Bottom = ImageIO.read(getClass().getResourceAsStream("/BackGround/TilemapStage1_Bottom.png"));
			imgTilemapStage1Top = ImageIO.read(getClass().getResourceAsStream("/BackGround/TilemapStage1_Top.png"));
			
		}catch (Exception e) {
			System.out.println(e.toString());
		}
	}
		
	public void update() {		
		
//		isMoveX檢測是否在左右移動中，如果是的話同步移動敵機。
		
		if(keyHandler.leftPressed) {
			tilemapPositionX += moveSpeed;  // 按A向右移動。
			isMoveX = 2;
		}
		if(keyHandler.rightPressed) {
			tilemapPositionX -= moveSpeed;  // 按D向左移動。
			isMoveX = 1;
		}
		
		if((!keyHandler.leftPressed && !keyHandler.rightPressed) || (keyHandler.leftPressed && keyHandler.rightPressed)) isMoveX = 0;
		
		if(tilemapPositionX >= -10) {
			tilemapPositionX = -10;
			isMoveX = 0;
		}else if(tilemapPositionX <= ((tilemapStage1TopWidth-mainPanel.getWindowWidth())*-1+10)) {
			tilemapPositionX = (tilemapStage1TopWidth-mainPanel.getWindowWidth())*-1+10;
			isMoveX = 0;
		}
		
		if(tilemapPositionY >= (-410)) {
			tilemapPositionY = -410;
		}else {
			tilemapPositionY += 1;
//			tilemapPositionY += 50;
		}	
		
//		System.out.println(tilemapPositionY);
	}
	
	public void doInit() {
		tilemapPositionX = (tilemapStage1TopWidth-mainPanel.getWindowWidth())/2*(-1);
		tilemapPositionY = -tilemapStage1TopHeight + mainPanel.getWindowHight() + 10;
		isMoveX = 0;
	}
	
	public void draw(Graphics2D g2d) {
		
		g2d.drawImage(imgTilemapStage1Bottom, tilemapPositionX, tilemapPositionY, tilemapStage1TopWidth, tilemapStage1TopHeight, null);
		g2d.drawImage(imgTilemapStage1Top, tilemapPositionX, tilemapPositionY, tilemapStage1TopWidth, tilemapStage1TopHeight, null);

	}
	
	public int getTilemapPositionY() {
		return tilemapPositionY;
	}
	
	public int getIsMoveX() {
		return isMoveX;
	}
	
	public int getMoveSpeed() {
		return moveSpeed;
	}
	
}
