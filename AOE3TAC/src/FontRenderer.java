import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
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
				WritableRaster raster = img.getWritableTile(0, 0);
				int wid = img.getWidth();
				int hei = img.getHeight();
				int minX = 0;
				int maxX = wid;
				int minY = 0;
				int maxY = hei;
				l1: for (int xx = 0; xx < wid; xx++) {
					int[] pix = raster.getPixels(xx, 0, 1, hei, (int[]) null);
					for (int rgb : pix) {
						if (rgb > 0) {
							break l1;
						}
					}
					minX++;
				}
				if (minX < maxX) {
					l2: for (int xx = wid - 1; xx >= 0; xx--) {
						int[] pix = raster.getPixels(xx, 0, 1, hei, (int[]) null);
						for (int rgb : pix) {
							if (rgb > 0) {
								break l2;
							}
						}
						maxX--;
					}
				}
				l3: for (int yy = 0; yy < hei; yy++) {
					int[] pix = raster.getPixels(0, yy, wid, 1, (int[]) null);
					for (int rgb : pix) {
						if (rgb > 0) {
							break l3;
						}
					}
					minY++;
				}
				if (minY < maxY) {
					l4: for (int yy = hei - 1; yy >= 0; yy--) {
						int[] pix = raster.getPixels(0, yy, wid, 1, (int[]) null);
						for (int rgb : pix) {
							if (rgb > 0) {
								break l4;
							}
						}
						maxY--;
					}
				}
				if (minX < maxX && minY < maxY) img = img.getSubimage(minX, minY, maxX - minX, maxY - minY);
				/*
				 * File out = new File("C:\\Users\\Max\\Desktop\\temp\\" + ((char) y * xt + x) + ".png"); try { out.mkdirs(); out.delete(); out.createNewFile(); ImageIO.write(img, "png", out); }catch (IOException e) { e.printStackTrace(); }
				 */
				int twid = maxX - minX;
				int thei = maxY - minY;
				storage.glyph.put((y + 1) * xt + x, new ImageRenderer.Texture(ImageRenderer.NPOT(twid), ImageRenderer.NPOT(thei), twid, thei, ImageRenderer.ins.loadTexture(img)));
			}
		}
		fonts.put(name, storage);
	}
	
	private static class FontStorage {
		
		public int size = 0;
		public HashMap<Integer, ImageRenderer.Texture> glyph = new HashMap<Integer, ImageRenderer.Texture>();
		
		public void draw(float x, float y, int sized, int index) {
			ImageRenderer.Texture t = glyph.get(index);
			float tY = (float) t.height / (float) t.pheight;
			float tX = (float) t.width / (float) t.pwidth;
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glPushMatrix();
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, t.id);
			float scale = (float) sized / (float) size;
			float wid = AspectManager.ToAspectX(t.width) * scale;
			float hei = AspectManager.ToAspectY(t.height) * scale;
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0F, tY);
			GL11.glVertex3f(x, y, 0F);
			GL11.glTexCoord2f(tX, tY);
			GL11.glVertex3f(x + wid, y, 0F);
			GL11.glTexCoord2f(tX, 0F);
			GL11.glVertex3f(x + wid, y + hei, 0F);
			GL11.glTexCoord2f(0F, 0F);
			GL11.glVertex3f(x, y + hei, 0F);
			GL11.glEnd();
			GL11.glPopMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
		}
		
		public float getWidth(int sized, int index) {
			float scale = (float) sized / (float) size;
			ImageRenderer.Texture t = glyph.get(index);
			return AspectManager.ToAspectX(t.width) * scale;
		}
	}
	
	private final float padding = 0.005F;
	
	public void draw(float x, float y, String text, String font, int size) {
		FontStorage fnt = fonts.get(font);
		float cwid = 0F;
		float xxp = AspectManager.ToAspectX(padding);
		float yy = AspectManager.ToAspectY(y);
		float xx = AspectManager.ToAspectX(x);
		for (int i = 0; i < text.length(); i++) {
			char chr = text.charAt(i);
			fnt.draw(xx + cwid, yy, size, chr);
			cwid += fnt.getWidth(size, chr) + xxp;
		}
	}
	
	public float getStringWidth(float x, float y, String text, String font, int size) {
		FontStorage fnt = fonts.get(font);
		float cwid = 0F;
		float xxp = AspectManager.ToAspectX(padding);
		for (int i = 0; i < text.length(); i++) {
			char chr = text.charAt(i);
			cwid += fnt.getWidth(size, chr) + xxp;
		}
		return cwid;
	}
}
