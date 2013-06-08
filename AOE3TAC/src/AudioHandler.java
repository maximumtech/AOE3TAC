import java.io.File;
import java.util.HashMap;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioHandler {
	public static AudioHandler ins = new AudioHandler();
	
	private HashMap<String, Clip> clips = new HashMap<String, Clip>();
	
	public AudioHandler() {
		
	}
	
	public void loadClip(String argpath) { // credit to mini-me from stackoverflow
		String path = argpath.replace("/", "\\");
		File file = new File(Start.path + "audio\\" + path + ".wav");
		if (file.exists() && !clips.containsKey(path)) {
			try {
				Clip clip = AudioSystem.getClip();
				clip.open(AudioSystem.getAudioInputStream(file));
				clips.put(argpath, clip);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void playClip(String argpath) {
		String path = argpath.replace("/", "\\");
		loadClip(path);
		if (clips.containsKey(path)) {
			clips.get(path).start();
		}
	}
}
