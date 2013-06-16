import org.lwjgl.input.Mouse;

public class CompButton extends Component implements IMouseHandler {
	
	public String displayText = "";
	public float width = 0;
	public float height = 0;
	public int imgw = 0;
	public boolean isDisabled = false;
	
	public CompButton(Screen parent, float x, float y, float width, float height, String displayText, int w) {
		super(parent, x, y);
		this.displayText = displayText;
		this.width = width;
		this.height = height;
		imgw = w;
	}
	
	public CompButton(Screen parent, float x, float y, float width, float height, String displayText) {
		this(parent, x, y, width, height, displayText, 128);
	}
	
	public void render() {
		if (isDisabled) {
			ImageRenderer.ins.render(getRelX(), getRelY(), "ui/button/button" + imgw + "d");
		}else if (isInside(Mouse.getX(), Mouse.getY())) {
			if (Mouse.isButtonDown(0)) {
				ImageRenderer.ins.render(getRelX(), getRelY(), "ui/button/button" + imgw + "c");
			}else {
				ImageRenderer.ins.render(getRelX(), getRelY(), "ui/button/button" + imgw + "r");
			}
		}else {
			ImageRenderer.ins.render(getRelX(), getRelY(), "ui/button/button" + imgw + "n");
		}
	}
	
	public boolean isInside(int x, int y) {
		float x2 = x / Start.screenWidth;
		float y2 = y / Start.screenHeight;
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
