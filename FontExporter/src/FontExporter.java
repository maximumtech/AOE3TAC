import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FontExporter {
	public static final Font font = new Font("Arial", 0, 64); // font to export
	public static final File save = new File("C:\\Users\\Max\\Desktop\\font.png"); // save image location
	
	public static void main(String[] args) {
		BufferedImage img = new BufferedImage(1024, 1024, BufferedImage.TYPE_4BYTE_ABGR); // new image, 64*16 = 1024 16*16 = 256 = ASCII
		Graphics2D gfx = (Graphics2D) img.getGraphics();
		gfx.setFont(font); // sets gfx font
		for (int i = 0; i < 255; i++) { // loops through 256 characters
			char chr = (char) i;
			int xma = i % 16; // major, 0-15
			int yma = i / 16; // major, 0-15
			int ymi = yma * font.getSize(); // minor, pixel coordinate
			int xmi = xma * font.getSize();// ^^^
			gfx.drawString(chr + "", xmi, ymi); // draw character in cell
		}
		try {
			ImageIO.write(img, "png", save); // save image
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
