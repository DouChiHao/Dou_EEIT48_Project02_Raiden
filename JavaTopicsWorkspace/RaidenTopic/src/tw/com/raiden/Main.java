package tw.com.raiden;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		
		JFrame mainWindow = new JFrame("Raiden");
		
		MainPanel mainPanel = new MainPanel();
		
		mainWindow.add(mainPanel);  // 直接把設定的Panel加入視窗中。
		mainWindow.pack();  // 視窗會自動調整為內部元件尺寸的大小(mainPanel的大小)。
		
		mainWindow.setResizable(false);  // 暫定先不能更改大小。
		mainWindow.setLocationRelativeTo(null);  // JFrame視窗在螢幕居中。
		mainWindow.setVisible(true);
		
//		如果非繼承JFrame的方式設計視窗，EXIT_ON_CLOSE要用JFrame的static方法呼叫。
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainPanel.setupGame();
		mainPanel.startGameThread();  // 啟動mainPanel的執行序run方法。
		
		
	}

}
