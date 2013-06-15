public class CompMetalBox extends Component {
	
	private static final String metal = "ui/metalbox/metal";
	private static final String metaltop = "ui/metalbox/metaltop";
	private static final String metalbottom = "ui/metalbox/metalbottom";
	private static final String metalright = "ui/metalbox/metalright";
	private static final String metalleft = "ui/metalbox/metalleft";
	private static final String metaltopright = "ui/metalbox/metaltopright";
	private static final String metaltopleft = "ui/metalbox/metaltopleft";
	private static final String metalbottomleft = "ui/metalbox/metalbottomleft";
	private static final String metalbottomright = "ui/metalbox/metalbottomright";
	
	public float width;
	public float height;
	
	public CompMetalBox(Screen parent, float x, float y, float width, float height) {
		super(parent, x, y);
		this.width = width;
		this.height = height;
	}
	
	public void render() {
		float rx = getRelX();
		float ry = getRelY();
		ImageRenderer.ins.render(rx, ry, width, height, metal);
		ImageRenderer.ins.render(rx, ry, width / 32, height, metalright);
		ImageRenderer.ins.render(rx + width - (width / 32), ry, width / 32, height, metalleft);
		ImageRenderer.ins.render(rx, ry + height - (height / 32), width, height / 32, metaltop);
		ImageRenderer.ins.render(rx, ry, width, height / 32, metalbottom);
	}
	
}
