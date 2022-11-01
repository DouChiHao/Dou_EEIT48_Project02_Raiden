package tw.com.raiden;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
	
//	透過boolean檢測使用者是處於按下去還是放開的狀態。
	public boolean upPressed,downPressed,leftPressed,rightPressed;
	public boolean spacePressed;
	public boolean enterPressed;

	@Override
	public void keyTyped(KeyEvent e) {		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		int keycode = e.getKeyCode();  // 取得使用者按下去的按鍵。
		
		if(keycode == KeyEvent.VK_ENTER) {
//			按下ENTER。
			enterPressed = true;
		}
		
		
		if(keycode == KeyEvent.VK_W) {
			upPressed = true;
		}
		if(keycode == KeyEvent.VK_S) {
			downPressed = true;
		}
		if(keycode == KeyEvent.VK_A) {
			leftPressed = true;
		}
		if(keycode == KeyEvent.VK_D) {
			rightPressed = true;
		}
		
		
		if(keycode == KeyEvent.VK_SPACE) {
			spacePressed = true;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		int keycode = e.getKeyCode(); 
		
		if(keycode == KeyEvent.VK_ENTER) {
//			按下ENTER。
			enterPressed = false;
		}
		
		if(keycode == KeyEvent.VK_W) {
			upPressed = false;
		}
		if(keycode == KeyEvent.VK_S) {
			downPressed = false;
		}
		if(keycode == KeyEvent.VK_A) {
			leftPressed = false;
		}
		if(keycode == KeyEvent.VK_D) {
			rightPressed = false;
		}
		
		if(keycode == KeyEvent.VK_SPACE) {
			spacePressed = false;
		}
	}

}
