public class SizableComponent extends Component {
	private float width;
	private float height;
	
	public SizableComponent(Screen parent, float x, float y, float width, float height) {
		super(parent, x, y);
		this.width = width;
		this.height = height;
	}
	
	public float getWidth() {
		return AspectManager.ToAspectX(width);
	}
	
	public float getHeight() {
		return AspectManager.ToAspectY(height);
	}
	
}
