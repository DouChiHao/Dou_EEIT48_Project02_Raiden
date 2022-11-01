package tw.com.raiden;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
	
	Clip clip;  // 開啟聲音檔案的接口，使音樂能在播放前先加載好。
	URL soundURL[] = new URL[6];
	
	public Sound() {
		
//		音檔的路徑。
		soundURL[0] = getClass().getResource("/SoundBgm/BGM_Title.wav");  
		soundURL[1] = getClass().getResource("/SoundBgm/BGM_Main.wav"); 
		soundURL[2] = getClass().getResource("/SoundBgm/BGM_Over.wav"); 
		soundURL[3] = getClass().getResource("/SoundBgm/Fx_Player_Shot.wav"); 
		soundURL[4] = getClass().getResource("/SoundBgm/Fx_Enemy_Shot.wav"); 
		soundURL[5] = getClass().getResource("/SoundBgm/Fx_Explosion.wav"); 
	}
	
	
	public void setFile(int i) {
		
		try {
			
//			使用AudioInputStream取得音檔通道。
			AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
			clip = AudioSystem.getClip();  // 取得可以用於播放聲音的數據通道。
			clip.open(ais);  // 打開數據通道。
			
			
		}catch(Exception e) {
			System.out.println("sound:"+e.toString());
		}
		
		
	}
	public void play() {
		
//		播放音樂。
		clip.setFramePosition(0);
		clip.start();
//		重播音樂前，可以再調用clip.setFramePosition(0)，重置播放進度。
		
	}
	public void loop() {
		
//		Loop音樂。
		clip.loop(Clip.LOOP_CONTINUOUSLY);
		
	}
	public void stop() {
		
//		停止音樂。
		clip.stop();
		
	}
	
	
}
