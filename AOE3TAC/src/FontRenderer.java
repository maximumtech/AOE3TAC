import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import org.lwjgl.opengl.GL11;

public class FontRenderer {
	public static FontRenderer ins = new FontRenderer();
	
	private HashMap<String, FontStorage> fonts = new HashMap<>();
	
	public void loadFont(BufferedImage fnt, String name, int width, int height) {
		FontStorage storage = new FontStorage();
		storage.size = width / 16;
		int xp = width / 16;
		int yp = height / 16;
		int xt = width / xp;
		int yt = height / yp;
		for (int x = 0; x < xt; x++) {
			for (int y = 0; y < yt; y++) {
				BufferedImage img = fnt.getSubimage(x * xp, y * yp, xp, yp);
				int mX = img.getWidth();
				int mY = img.getHeight();
				for (int px = img.getWidth() - 1; px >= 0; px--) {
					int pt = 0;
					for (int py = 0; py < img.getHeight(); py++) {
						Color color = new Color(img.getRGB(px, py), true);
						if (color.getAlpha() == 0) {
							pt++;
						}
					}
					if (pt >= img.getHeight()) {
						mX--;
					}
				}
				for (int py = img.getHeight() - 1; py >= 0; py--) {
					int pt = 0;
					for (int px = 0; px < img.getWidth(); px++) {
						Color color = new Color(img.getRGB(px, py), true);
						if (color.getAlpha() == 0) {
							pt++;
						}
					}
					if (pt >= img.getWidth()) {
						mY--;
					}
				}
				img = img.getSubimage(0, 0, mX == 0 ? img.getWidth() : mX, mY == 0 ? img.getHeight() : mY);
				storage.glyph.put(y * xt + x, ImageRenderer.ins.loadTexture(img));
			}
		}
		fonts.put(name, storage);
	}
	
	private static class FontStorage {
		
		public int size = 0;
		public HashMap<Integer, Integer> glyph = new HashMap<Integer, Integer>();
		
		public void draw(float x, float y, float width, float height, int index) {
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, glyph.get(index));
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0F, 1F);
			GL11.glVertex3f(x, y, 0F);
			GL11.glTexCoord2f(1F, 1F);
			GL11.glVertex3f(x + width, y, 0F);
			GL11.glTexCoord2f(1F, 0F);
			GL11.glVertex3f(x + width, y + height, 0F);
			GL11.glTexCoord2f(0F, 0F);
			GL11.glVertex3f(x, y + height, 0F);
			GL11.glEnd();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
	}
	
	public void draw(float x, float y, String text, String font, int size) {
		FontStorage fnt = fonts.get(font);
		float wid = AspectManager.ToAspectX(size);
		float hei = AspectManager.ToAspectY(size);
		for (int i = 0; i < text.length(); i++) {
			char chr = text.charAt(i);
			fnt.draw(AspectManager.ToAspectX(x + (wid * i)), AspectManager.ToAspectY(y), wid, hei, chr);
		}
	}
	
	public float getStringWidth(float x, float y, String text, String font, int size) {
		float wid = AspectManager.ToAspectX(size);
		float width = 0F;
		for (int i = 0; i < text.length(); i++) {
			width += wid * i;
		}
		return width;
	}
}
