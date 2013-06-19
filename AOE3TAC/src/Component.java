public class Component {
	
	public Screen parent;
	private float x;
	private float y;
	public int id = 0;
	
	public Component(Screen parent, float x, float y) {
		this.parent = parent;
		this.x = x;
		this.y = y;
	}
	
	public Component setID(int id) {
		this.id = id;
		return this;
	}
	
	public float getAspectX() { // get x relative to parent
		return parent.getRelComponentX() + AspectManager.ToAspectX(x);
	}
	
	public float getAspectY() { // get y relative to parent
		return parent.getRelComponentY() + AspectManager.ToAspectY(y);
	}
	
	public float getHardwareX() { // get x relative to parent
		return parent.getRelComponentX() + x;
	}
	
	public float getHardwareY() { // get y relative to parent
		return parent.getRelComponentY() + y;
	}
	
	public void render() { // while parent is open
	
	}
	
	public void tick() { // when game ticks & window is open
	
	}
	
	public void onClose() { // called when closing, prepare to close
	
	}
	
}
