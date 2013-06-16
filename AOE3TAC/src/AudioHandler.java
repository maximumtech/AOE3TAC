import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioHandler {
	public static AudioHandler ins = new AudioHandler();
	
	private HashMap<String, Clip> clips = new HashMap<String, Clip>();
	
	public AudioHandler() {
		
	}
	
	private volatile ArrayList<File> toload = new ArrayList<File>();
	private volatile boolean done = false;
	
	public void init() { // load images make recursive? NOT OGL SAFE
		recurDir(new File(Start.path + "audio\\"));
		done = true;
	}
	
	public boolean canpostinit() { // load files in, OGL safe, not threaded
		return done;
	}
	
	public void postinit() {
		for (File d : toload) {
			String name = d.getPath();
			name = name.replace("\\", "/");
			name = name.substring(name.indexOf("AOE3TAC/audio/") + 14, name.length() - 4);
			loadClip(name);
		}
	}
	
	public void recurDir(File dir) {
		for (File d : dir.listFiles()) {
			if (d.isDirectory()) {
				recurDir(d);
			}else {
				if (d.getName().endsWith(".wav")) {
					toload.add(d);
				}
			}
		}
	}
	
	public Clip getClip(String path) {
		Clip clip = clips.get(path);
		clip.setMicrosecondPosition(0L);
		return clip;
	}
	
	public void loadClip(String path) { // credit to mini-me from stackoverflow
		File file = new File(Start.path + "audio\\" + path.replace("/", "\\") + ".wav");
		if (file.exists() && !clips.containsKey(path)) {
			try {
				Clip clip = AudioSystem.getClip();
				clip.open(AudioSystem.getAudioInputStream(file));
				clips.put(path, clip);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void playClip(String path) {
		if (clips.containsKey(path)) {
			Clip clip = clips.get(path);
			clip.setFramePosition(0);
			clip.start();
		}
	}
}
