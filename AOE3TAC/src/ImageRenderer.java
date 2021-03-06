import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class ImageRenderer {
	public static ImageRenderer ins = new ImageRenderer();
	
	public static class Texture {
		public int width;
		public int height;
		public int id;
		public int pwidth;
		public int pheight;
		
		public Texture(int pwidth, int pheight, int width, int height, int id) {
			this.width = width;
			this.height = height;
			this.id = id;
			this.pwidth = pwidth;
			this.pheight = pheight;
		}
	}
	
	private HashMap<String, Texture> images = new HashMap<String, Texture>();
	
	private volatile ArrayList<File> toload = new ArrayList<File>();
	private volatile boolean done = false;
	
	public void init() { // load images make recursive? NOT OGL SAFE
		recurDir(new File(Start.path + "art\\"));
		done = true;
	}
	
	public boolean canpostinit() { // load files in, OGL safe, not threaded
		return done;
	}
	
	public void postinit() {
		for (File d : toload) {
			String name = d.getPath();
			name = name.replace("\\", "/");
			name = name.substring(name.indexOf("AOE3TAC/art/") + 12, name.length() - 4);
			loadImage(name);
		}
	}
	
	public void recurDir(File dir) {
		for (File d : dir.listFiles()) {
			if (d.isDirectory()) {
				recurDir(d);
			}else {
				if (d.getName().endsWith(".png")) {
					toload.add(d);
				}
			}
		}
	}
	
	public void loadImage(String argpath) { // credit to flafla2
		String path = argpath.replace("/", "\\");
		if (!images.containsKey(path) && new File(Start.path + "art\\" + path + ".png").exists()) {
			try {
				int id = 0;
				
				BufferedImage image = ImageIO.read(new File(Start.path + "art\\" + path + ".png"));
				int width = image.getWidth();
				int height = image.getHeight();
				if (argpath.startsWith("ui/fonts/")) {
					FontRenderer.ins.loadFont(image, argpath.substring(argpath.lastIndexOf("/") + 1), width, height);
				}else {
					id = loadTexture(image);
					images.put(argpath, new Texture(NPOT(width), NPOT(height), width, height, id));
				}
			}catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static int NPOT(int p) {
		int pow = 2;
		while (pow < p) {
			pow *= 2;
		}
		return pow;
	}
	
	public int loadTexture(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		int nw = NPOT(width);
		int nh = NPOT(height);
		int[] pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
		ByteBuffer bb = ByteBuffer.allocateDirect(nw * nh * 4);
		for (int y = 0; y < nh; y++) {
			for (int x = 0; x < nw; x++) {
				int pixel = y * width + x < pixels.length ? pixels[y * width + x] : 0;
				bb.put((byte) ((pixel >> 16) & 0xFF));
				bb.put((byte) ((pixel >> 8) & 0xFF));
				bb.put((byte) (pixel & 0xFF));
				bb.put((byte) ((pixel >> 24) & 0xFF));
			}
		}
		bb.flip();
		
		int id = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, nw, nh, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bb);
		return id;
	}
	
	public void bind(String path) {
		if (images.containsKey(path)) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, images.get(path).id);
		}
	}
	
	public Texture getTexture(String path) {
		if (images.containsKey(path)) {
			return images.get(path);
		}
		return null;
	}
	
	public static enum RFlag {
		REG, ASPECT, SCALE;
	}
	
	public void render(float x, float y, float width, float height, String path, RFlag flag) {
		switch (flag) {
			case REG:
				render(x, y, width, height, path);
				break;
			case ASPECT:
				renderAspect(x, y, width, height, path);
				break;
			case SCALE:
				renderScale(x, y, width, height, path);
				break;
		}
	}
	
	public void render(float x, float y, String path, RFlag flag) {
		switch (flag) {
			case REG:
				render(x, y, path);
				break;
			case ASPECT:
				renderAspect(x, y, path);
				break;
			case SCALE:
				render(x, y, path);
				break;
		}
	}
	
	public void render(float x, float y, String path) {
		if (images.containsKey(path)) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			bind(path);
			Texture t = images.get(path);
			float x2 = x + ((float) t.width / (float) Start.screenWidth) * Start.screenWidthRatio;
			float y2 = y + ((float) t.height / (float) Start.screenHeight) * Start.screenHeightRatio;
			GL11.glBegin(GL11.GL_QUADS);
			float tY = (float) t.height / (float) t.pheight;
			float tX = (float) t.width / (float) t.pwidth;
			GL11.glTexCoord2f(0F, tY);
			GL11.glVertex3f(x, y, 0F);
			GL11.glTexCoord2f(tX, tY);
			GL11.glVertex3f(x2, y, 0F);
			GL11.glTexCoord2f(tX, 0F);
			GL11.glVertex3f(x2, y2, 0F);
			GL11.glTexCoord2f(0F, 0F);
			GL11.glVertex3f(x, y2, 0F);
			GL11.glEnd();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
	}
	
	public int getWidth(String path) {
		Texture t = images.get(path);
		return t.width;
	}
	
	public int getHeight(String path) {
		Texture t = images.get(path);
		return t.width;
	}
	
	public float getAspectedWidth(String path) {
		return AspectManager.ToAspectX(getWidth(path));
	}
	
	public float getAspectedHeight(String path) {
		return AspectManager.ToAspectY(getHeight(path));
	}
	
	public void renderAspect(float x, float y, String path) { // makes y and x coordinates the same relative distance.
		if (images.containsKey(path)) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			bind(path);
			Texture t = images.get(path);
			float x2 = AspectManager.ToAspectX(x);
			float y2 = AspectManager.ToAspectY(y);
			float width = x2 + AspectManager.ToAspectX(t.width);
			float height = y2 + AspectManager.ToAspectY(t.height);
			float tY = (float) t.height / (float) t.pheight;
			float tX = (float) t.width / (float) t.pwidth;
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0F, tY);
			GL11.glVertex3f(x2, y2, 0F);
			GL11.glTexCoord2f(tX, tY);
			GL11.glVertex3f(width, y2, 0F);
			GL11.glTexCoord2f(tX, 0F);
			GL11.glVertex3f(width, height, 0F);
			GL11.glTexCoord2f(0F, 0F);
			GL11.glVertex3f(x2, height, 0F);
			GL11.glEnd();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
	}
	
	public void renderAspect(float x, float y, float wid, float hei, String path) { // makes y and x coordinates the same relative distance.
		if (images.containsKey(path)) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			bind(path);
			float x2 = AspectManager.ToAspectX(x);
			float y2 = AspectManager.ToAspectY(y);
			float width = x2 + wid;
			float height = y2 + hei;
			Texture t = images.get(path);
			float tY = (float) t.height / (float) t.pheight;
			float tX = (float) t.width / (float) t.pwidth;
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0F, tY);
			GL11.glVertex3f(x2, y2, 0F);
			GL11.glTexCoord2f(tX, tY);
			GL11.glVertex3f(width, y2, 0F);
			GL11.glTexCoord2f(tX, 0F);
			GL11.glVertex3f(width, height, 0F);
			GL11.glTexCoord2f(0F, 0F);
			GL11.glVertex3f(x2, height, 0F);
			GL11.glEnd();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
	}
	
	public void render(float x, float y, float width, float height, String path) {
		if (images.containsKey(path)) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			bind(path);
			GL11.glBegin(GL11.GL_QUADS);
			Texture t = images.get(path);
			float tY = (float) t.height / (float) t.pheight;
			float tX = (float) t.width / (float) t.pwidth;
			GL11.glTexCoord2f(0F, tY);
			GL11.glVertex3f(x, y, 0F);
			GL11.glTexCoord2f(tX, tY);
			GL11.glVertex3f(x + width, y, 0F);
			GL11.glTexCoord2f(tX, 0F);
			GL11.glVertex3f(x + width, y + height, 0F);
			GL11.glTexCoord2f(0F, 0F);
			GL11.glVertex3f(x, y + height, 0F);
			GL11.glEnd();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
	}
	
	public void renderScale(float x, float y, float width, float height, String path) {
		if (images.containsKey(path)) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			bind(path);
			Texture t = images.get(path);
			float newWidth = (float) t.width / (float) Start.screenWidth;
			float newHeight = (float) t.height / (float) Start.screenHeight;
			float w = newWidth;
			float h = newHeight;
			if (w > width) {
				newWidth = width;
				newHeight = (newWidth * h) / w;
			}
			if (h > height) {
				newHeight = height;
				newWidth = (newHeight * w) / h;
			}
			float nx = x;
			float ny = y;
			if (newWidth < width) {
				nx = ((width - newWidth) / 2);
			}
			if (newHeight < height) {
				ny = ((height - newHeight) / 2);
			}
			float tY = (float) t.height / (float) t.pheight;
			float tX = (float) t.width / (float) t.pwidth;
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0F, tY);
			GL11.glVertex3f(nx, ny, 0F);
			GL11.glTexCoord2f(tX, tY);
			GL11.glVertex3f(nx + newWidth, ny, 0F);
			GL11.glTexCoord2f(tX, 0F);
			GL11.glVertex3f(nx + newWidth, ny + newHeight, 0F);
			GL11.glTexCoord2f(0F, 0F);
			GL11.glVertex3f(nx, ny + newHeight, 0F);
			GL11.glEnd();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
	}
}
