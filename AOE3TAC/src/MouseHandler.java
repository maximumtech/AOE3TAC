import java.util.ArrayList;
import org.lwjgl.input.Mouse;

public class MouseHandler implements IMouseHandler {
	public static MouseHandler ins = new MouseHandler();
	
	public ArrayList<IMouseHandler> handlers = new ArrayList<IMouseHandler>();
	
	private boolean[] mouseDown = new boolean[256]; // support up to 256 mouse buttons
	
	public void tick() {
		if (!Mouse.isCreated()) return;
		for (int i = 0; i < Mouse.getButtonCount(); i++) {
			boolean isDown = Mouse.isButtonDown(i);
			if (!mouseDown[i] && isDown) {
				onButtonDown(Mouse.getX(), Mouse.getY(), i);
			}else if (mouseDown[i] && !isDown) {
				onButtonUp(Mouse.getX(), Mouse.getY(), i);
			}else if (mouseDown[i] && isDown) {
				onButtonHeld(Mouse.getX(), Mouse.getY(), i);
			}
			mouseDown[i] = isDown;
		}
	}
	
	@Override
	public void onButtonDown(int x, int y, int button) {
		
	}
	
	@Override
	public void onButtonUp(int x, int y, int button) {
		
	}
	
	@Override
	public void onButtonHeld(int x, int y, int button) {
		
	}
}
