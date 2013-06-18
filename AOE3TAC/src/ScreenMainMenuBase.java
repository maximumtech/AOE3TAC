public class ScreenMainMenuBase extends Screen { // the background of the main menu, to minimize code replication
	public ScreenMainMenuBase() {
		register(new CompButton(this, 0.05F, 0.01F, 0.1F, 0.1F, "test", 128));
	}
	
	public void render(boolean is2D) {
		if (is2D) {
			ImageRenderer.ins.render(0F, 0F, 1F, 1F, "ui/mainmenu/wallpaper");
			ImageRenderer.ins.renderAspect(0.03F, 0.03F, "ui/mainmenu/mainmenu");
		}else {
			
		}
	}
}
