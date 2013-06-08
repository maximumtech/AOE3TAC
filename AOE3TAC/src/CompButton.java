import org.lwjgl.input.Mouse;

public class CompButton extends Component implements IMouseHandler {
	
	public String displayText = "";
	public int width = 0;
	public int height = 0;
	
	public CompButton(Screen parent, int x, int y, int width, int height, String displayText) {
		super(parent, x, y);
		this.displayText = displayText;
		this.width = width;
		this.height = height;
	}
	
	public void render() {
		if (isInside(Mouse.getX(), Mouse.getY())) {
			ImageRenderer.ins.render(Start.screenWidth / getRelX(), Start.screenHeight / getRelY(), "ui/highlightedbutton");
		}else {
			ImageRenderer.ins.render(Start.screenWidth / getRelX(), Start.screenHeight / getRelY(), "ui/button");
		}
	}
	
	public boolean isInside(int x2, int y2) {
		return x2 > getRelX() && x2 < getRelX() + width && y2 > getRelY() && y2 < getRelY() + height;
	}
	
	public void tick() {
		
	}
	
	@Override
	public void onButtonDown(int x, int y, int button) {
		if (button == 0 && isInside(x, y)) {
			AudioHandler.ins.playClip("ui/button");
		}
	}
	
	@Override
	public void onButtonUp(int x, int y, int button) {
		if (button == 0 && isInside(x, y)) {
			parent.callComponentAction(this, 0);
			if (button == 0 && isInside(x, y)) {
				AudioHandler.ins.playClip("ui/button");
			}
		}
	}
	
	@Override
	public void onButtonHeld(int x, int y, int button) {
		
	}
	
}
