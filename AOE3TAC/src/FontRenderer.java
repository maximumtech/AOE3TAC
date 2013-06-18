import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class FontRenderer {
	public static FontRenderer ins = new FontRenderer();
	
	private HashMap<String, FontStorage> fonts = new HashMap<>();
	
	private static class FontStorage {
		
		private HashMap<Integer, Integer> glyphs = new HashMap<Integer, Integer>();
		
		public void addGlyph(int index, BufferedImage chr) {
			int id = 0;
			int width = chr.getWidth();
			int height = chr.getHeight();
			int[] pixels = new int[width * height];
			chr.getRGB(0, 0, width, height, pixels, 0, width);
			ByteBuffer bb = ByteBuffer.allocateDirect(width * height * 4);
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int pixel = pixels[y * width + x];
					bb.put((byte) ((pixel >> 16) & 0xFF));
					bb.put((byte) ((pixel >> 8) & 0xFF));
					bb.put((byte) (pixel & 0xFF));
					bb.put((byte) ((pixel >> 24) & 0xFF));
				}
			}
			bb.flip();
			id = GL11.glGenTextures();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, bb);
			glyphs.put(index, id);
		}
		
		public void bindGlyph(int index) {
			if (glyphs.containsKey(index)) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, glyphs.get(0));
			}
		}
	}
	
	public void load(Font font) {
		FontStorage store = new FontStorage();
		for (int i = 0; i < font.getNumGlyphs(); i++) {
			BufferedImage img = new BufferedImage(font.getSize(), font.getSize(), BufferedImage.TYPE_4BYTE_ABGR);
			Graphics2D gfx = (Graphics2D) img.getGraphics();
			gfx.setFont(font);
			gfx.drawString(Character.toString((char) i), 0, 0);
			store.addGlyph(i, img);
		}
		fonts.put(font.getName(), store);
	}
	
	public void draw(float x, float y, String text, String font, int size) {
		FontStorage fnt = fonts.get(font);
		for (int i = 0; i < text.length(); i++) {
			char chr = text.charAt(i);
			fnt.bindGlyph(chr);
			float wid = (float) size / (float) Start.screenWidth;
			float hei = (float) size / (float) Start.screenHeight;
			ImageRenderer.ins.renderAspect(x + (wid * i), y, wid, hei);
		}
	}
	
	public void draw(float x, float y, String text, String font, float width, float height) {
		FontStorage fnt = fonts.get(font);
		float wid = width / text.length();
		float hei = height / text.length();
		for (int i = 0; i < text.length(); i++) {
			char chr = text.charAt(i);
			fnt.bindGlyph(chr);
			ImageRenderer.ins.renderAspect(x + (wid * i), y, wid, hei);
		}
	}
}
