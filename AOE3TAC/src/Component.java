public class Component {
	
	public Screen parent;
	private float x;
	private float y;
	
	public static enum RFlag {
		ASPECT, // to aspect
		SCALE, // to scale
		REG; // default
	}
	
	public RFlag flag = RFlag.REG;
	
	public Component setFlag(RFlag flag) {
		this.flag = flag;
		return this;
	}
	
	public float getX() {
		switch (flag) {
			case REG:
				return x;
			case ASPECT:
				return x * (1F / ((float) Start.screenWidth / 1024F));
			case SCALE:
				return x;
		}
		return x;
	}
	
	public float getRenderX() {
		return parent.getRelComponentX() + x;
	}
	
	public float getRenderY() {
		return parent.getRelComponentY() + y;
	}
	
	public float getY() {
		switch (flag) {
			case REG:
				return y;
			case ASPECT:
				return y * (1F / ((float) Start.screenHeight / 1024F));
			case SCALE:
				return y;
		}
		return y;
	}
	
	public Component(Screen parent, float x, float y) {
		this.parent = parent;
		this.x = x;
		this.y = y;
	}
	
	public float getRelX() { // get x relative to parent
		return parent.getRelComponentX() + getX();
	}
	
	public float getRelY() { // get y relative to parent
		return parent.getRelComponentY() + getX();
	}
	
	public void render() { // while parent is open
	
	}
	
	public void tick() { // when game ticks & window is open
	
	}
	
	public void onClose() { // called when closing, prepare to close
	
	}
	
}
