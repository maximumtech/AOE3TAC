import org.lwjgl.input.Mouse;

public class CompButton extends SizableComponent implements IMouseHandler {
	
	public String displayText = "";
	public int imgw = 0;
	public boolean isDisabled = false;
	
	public CompButton(Screen parent, float x, float y, float width, float height, String displayText, int w) {
		super(parent, x, y, width, height);
		this.displayText = displayText;
		imgw = w;
		MouseHandler.ins.register(this);
	}
	
	public CompButton(Screen parent, float x, float y, float width, float height, String displayText) {
		this(parent, x, y, width, height, displayText, 128);
	}
	
	public void render() {
		float x = getAspectX();
		float y = getAspectY();
		float width = getWidth();
		float height = getHeight();
		if (isDisabled) {
			ImageRenderer.ins.render(x, y, width, height, "ui/button/button" + imgw + "d");
		}else if (isInside(Mouse.getX(), Mouse.getY())) {
			if (Mouse.isButtonDown(0)) {
				ImageRenderer.ins.render(x, y, width, height, "ui/button/button" + imgw + "c");
			}else {
				ImageRenderer.ins.render(x, y, width, height, "ui/button/button" + imgw + "r");
			}
		}else {
			ImageRenderer.ins.render(x, y, width, height, "ui/button/button" + imgw + "n");
		}
	}
	
	public boolean isInside(int x, int y) {
		float x2 = AspectManager.ToAspectX(x);
		float y2 = AspectManager.ToAspectY(y);
		float xp = getAspectX();
		float yp = getAspectY();
		float width = getWidth();
		float height = getHeight();
		return x2 > xp && x2 < (xp + width) && y2 > yp && y2 < (yp + height);
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
		}
	}
	
	@Override
	public void onButtonHeld(int x, int y, int button) {
		
	}
	
}
