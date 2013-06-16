import javax.sound.sampled.Clip;

public class MusicPlayer {
	public static MusicPlayer ins = new MusicPlayer();
	
	public String cpath = "";
	public boolean playing = false;
	private Thread play;
	
	public void start(String path) {
		cpath = path;
		playing = true;
		play = new Thread("play" + System.currentTimeMillis()) {
			public void run() {
				Clip clip = AudioHandler.ins.getClip("music/title");
				while (MusicPlayer.ins.playing) {
					if (!clip.isRunning()) {
						clip.setFramePosition(0);
						clip.start();
					}
					try {
						Thread.sleep(1000L);
					}catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				clip.stop();
			}
		};
		play.start();
	}
	
	public void stop() {
		cpath = "";
		playing = false;
	}
}
