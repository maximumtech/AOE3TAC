public class Component {
	
	public Screen parent;
	private float x;
	private float y;
	private float hx;
	private float hy;
	
	public Component(Screen parent, float x, float y) {
		this.parent = parent;
		this.x = AspectManager.ToAspectX(x);
		this.y = AspectManager.ToAspectY(y);
		this.hx = x;
		this.hy = y;
	}
	
	public float getAspectX() { // get x relative to parent
		return parent.getRelComponentX() + x;
	}
	
	public float getAspectY() { // get y relative to parent
		return parent.getRelComponentY() + y;
	}
	
	public float getHardwareX() { // get x relative to parent
		return parent.getRelComponentX() + hx;
	}
	
	public float getHardwareY() { // get y relative to parent
		return parent.getRelComponentY() + hy;
	}
	
	public void render() { // while parent is open
	
	}
	
	public void tick() { // when game ticks & window is open
	
	}
	
	public void onClose() { // called when closing, prepare to close
	
	}
	
}
