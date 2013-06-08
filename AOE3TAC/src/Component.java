public class Component {
	
	public Screen parent;
	public int x;
	public int y;
	
	public Component(Screen parent, int x, int y) {
		this.parent = parent;
		this.x = x;
		this.y = y;
	}
	
	public int getRelX() { // get x relative to parent
		return parent.getRelComponentX() + x;
	}
	
	public int getRelY() { // get y relative to parent
		return parent.getRelComponentY() + y;
	}
	
	public void render() { // while parent is open
	
	}
	
	public void tick() { // when game ticks & window is open
	
	}
	
	public void onClose() { // called when closing, prepare to close
	
	}
	
}
