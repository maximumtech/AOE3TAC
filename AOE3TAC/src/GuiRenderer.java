import java.util.ArrayList;
import org.lwjgl.opengl.GL11;

public class GuiRenderer {
	
	public static GuiRenderer ins = new GuiRenderer();
	
	public ArrayList<Screen> screens = new ArrayList<Screen>(); // screens to render
	
	public void loadScreen(Screen screen) {
		screens.add(screen);
	}
	
	public void renderGui() {
		GL11.glPushMatrix(); // we do not want gui depth and the world depth combining
		GL11.glTranslatef(0F, 0F, -1F); // fix depth & move 0, 0 to the bottom left
		for (Screen screen : new ArrayList<Screen>(screens)) { // make a new arraylist so it can close itself - ConcurrentModificationException
			GL11.glPushMatrix(); // we do not want guis depth combining
			Start.set2D();
			screen.render(true); // render screen 2D
			GL11.glTranslatef(-0.5F, -0.5F, 0F);
			Start.set3D();
			screen.render(false); // render screen 3D
			screen.renderComponents(); // render button, textboxes, etc
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
	}
	
	public void tick() {
		for (Screen screen : new ArrayList<Screen>(screens)) { // make a new arraylist so it can close itself
			screen.tick(); // tick the screen, if its open
		}
	}
}
