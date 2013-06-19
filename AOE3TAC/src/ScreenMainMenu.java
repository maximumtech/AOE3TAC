public class ScreenMainMenu extends Screen {
	public ScreenMainMenu() {
		register(new CompButton(this, 0.045F, 0.045F, 0.1799609375F, 0.03125F, "Exit", 128).setID(0));
	}
	
	public void callComponentAction(Component component, int id) {
		if (id == 0) {
			if (component.id == 0) {// exit
				Start.forceClose = true;
			}
		}
	}
	
	public void render(boolean is2D) {
		if (is2D) {
			ImageRenderer.ins.render(0F, 0F, 1F, 1F, "ui/mainmenu/wallpaper"); // background
			ImageRenderer.ins.renderAspect(0.03F, 0.03F, "ui/mainmenu/mainmenu"); // button background
		}else {
			
		}
	}
}