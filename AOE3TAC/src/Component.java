public class Component {
	
	public Screen parent;
	public float x;
	public float y;
	
	public Component(Screen parent, float x, float y) {
		this.parent = parent;
		this.x = x;
		this.y = y;
	}
	
	public float getRelX() { // get x relative to parent
		return parent.getRelComponentX() + x;
	}
	
	public float getRelY() { // get y relative to parent
		return parent.getRelComponentY() + y;
	}
	
	public void render() { // while parent is open
	
	}
	
	public void tick() { // when game ticks & window is open
	
	}
	
	public void onClose() { // called when closing, prepare to close
	
	}
	
}
