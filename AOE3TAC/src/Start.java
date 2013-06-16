import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

/**
 * Major credits to: mini-me from StackOverflow - answered a question similar to mine, enabling me to create the audio engine. My friends, MatthiasM, ra4king, and one guy I forgot(sorry), for being so instrumental in helping me get the 2D engine working. (LWJGL irc) Jeremy Adams(elias4444) for giving me answers to my .OBJ model loader.
 */
public class Start {
	
	public static Start ins;
	
	private static Timer timer;
	
	public static boolean forceClose = false;
	
	public static Random rand = new Random();
	
	public static String path = "";
	
	public static void main(String[] args) {
		ins = new Start(args);
	}
	
	public static int screenWidth = 0;
	public static int screenHeight = 0;
	public static float screenWidthRatio = 0F;
	public static float screenHeightRatio = 0F;
	public static final int initialWidth = 1280;
	public static final int initialHeight = 720;
	
	private static void loadPath() {
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			Process p = Runtime.getRuntime().exec("reg query \"HKLM\\SOFTWARE\\Microsoft\\Microsoft Games\\Age of Empires 3\\1.0\"");
			inputStream = p.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			String tline = "";
			String res = tline;
			while ((tline = bufferedReader.readLine()) != null) {
				res += tline + System.getProperty("line.separator");
			}
			if (!res.contains("ERROR") && res.contains("SetupPath")) {
				int ind1 = res.indexOf("SetupPath");
				int ind2 = res.indexOf("C:\\", ind1);
				int ind3 = res.indexOf("Empires III", ind2);
				String path = res.substring(ind2, ind3 + 11);
				Start.path = path;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (path == null || path.equals("")) {
			try {
				Process p = Runtime.getRuntime().exec("reg query \"HKLM\\SOFTWARE\\Wow6432Node\\Microsoft\\Microsoft Games\\Age of Empires 3\\1.0\"");
				inputStream = p.getInputStream();
				inputStreamReader = new InputStreamReader(inputStream);
				bufferedReader = new BufferedReader(inputStreamReader);
				String tline = "";
				String res = tline;
				while ((tline = bufferedReader.readLine()) != null) {
					res += tline + System.getProperty("line.separator");
				}
				if (!res.contains("ERROR") && res.contains("SetupPath")) {
					int ind1 = res.indexOf("SetupPath");
					int ind2 = res.indexOf(":\\", ind1);
					int ind3 = res.indexOf("Empires III", ind2);
					String path = res.substring(ind2 - 1, ind3 + 11);
					Start.path = path;
				}
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if (bufferedReader != null) {
						bufferedReader.close();
					}
					if (inputStreamReader != null) {
						inputStreamReader.close();
					}
					if (inputStream != null) {
						inputStream.close();
					}
				}catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		path += "\\AOE3TAC\\";
	}
	
	public Start(String[] args) {
		try {
			// setup displays
			Display.setDisplayMode(new DisplayMode(initialWidth, initialHeight));
			Display.setTitle("Age of Empires III: The African Conquest");
			Display.setResizable(true);
			Display.create(); // opens window
			System.out.println("Display Created!");
			System.out.println("Starting OpenGL");
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			set3D();
			screenWidth = initialWidth;
			screenHeight = initialHeight;
			screenWidthRatio = 1F;
			screenHeightRatio = 1F;
			GL11.glDepthFunc(GL11.GL_LEQUAL);
			GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glCullFace(GL11.GL_BACK); // prevents rendering of 2D surfaces on the back
			
			// load stuff
			timer = new Timer(20F);
			loadPath();
			GuiRenderer.ins.loadScreen(new ScreenMainLoading());
			
			// render loop
			while (!Display.isCloseRequested() && !forceClose) {
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // clear colors, and depth from last render
				screenWidth = Display.getWidth();
				screenHeight = Display.getHeight();
				screenWidthRatio = (float) screenWidth / (float) initialWidth;
				screenHeightRatio = (float) screenHeight / (float) initialHeight;
				if (Display.wasResized()) {
					GL11.glViewport(0, 0, screenWidth, screenHeight);
				}
				GuiRenderer.ins.renderGui(); // render guis
				timer.updateTimer();
				for (int i = 0; i < timer.elapsedTicks; i++) { // runs this many ticks
					tick();
				}
				timer.elapsedTicks = 0;
				Display.update();
				Display.sync(30); // try to get 30 fps - just above human eyesight fps, and won't lag
				Thread.sleep(15L); // loops run out of control without this
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		MusicPlayer.ins.stop();
		// exit program, save files, etc
	}
	
	public static boolean is2D = false;
	
	public static void set3D() {
		is2D = false;
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		Project.gluPerspective(30F, 1280F / 720F, 0.001F, 100F); // the creme de la crop of 3D awesomeness
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public static void set2D() {
		is2D = true;
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 1, 0, 1, 0.001F, 100F); // set mode to 2d
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public void tick() {
		// tick
		GuiRenderer.ins.tick();
	}
}
