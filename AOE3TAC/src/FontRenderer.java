import java.util.HashMap;
import org.lwjgl.opengl.GL11;

public class FontRenderer {
	public static FontRenderer ins = new FontRenderer();
	
	private HashMap<String, FontStorage> fonts = new HashMap<>();
	
	public void loadFont(int font, String name, int width, int height) {
		FontStorage storage = new FontStorage();
		storage.id = font;
		storage.width = width;
		storage.height = height;
		fonts.put(name, storage);
	}
	
	private static class FontStorage {
		
		public int id = 0;
		public int width = 0;
		public int height = 0;
		
		public void draw(float x, float y, float width, float height, int index) {
			int xPos = index % 16;
			int yPos = index / 16;
			int xPer = this.width / 16;
			int yPer = this.height / 16;
			float yF = (float) yPos * (float) yPer / (float) this.height;
			float xF = (float) xPos * (float) xPer / (float) this.width;
			float yFM = yF + ((float) yPer / (float) this.height);
			float xFM = xF + ((float) xPer / (float) this.width);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(xF, yFM);
			GL11.glVertex3f(x, y, 0F);
			GL11.glTexCoord2f(xFM, yFM);
			GL11.glVertex3f(x + width, y, 0F);
			GL11.glTexCoord2f(xFM, yF);
			GL11.glVertex3f(x + width, y + height, 0F);
			GL11.glTexCoord2f(xF, yF);
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
	
	public void draw(float x, float y, String text, String font, float width, float height) {
		FontStorage fnt = fonts.get(font);
		float wid = width / text.length();
		float hei = height / text.length();
		for (int i = 0; i < text.length(); i++) {
			char chr = text.charAt(i);
			// fnt.bindGlyph(chr);
			ImageRenderer.ins.renderAspect(x + (wid * i), y, wid, hei);
		}
	}
}
