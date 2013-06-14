public class ScreenMainMenuBase extends Screen { // the background of the main menu, to minimize code replication
	public void render(boolean is2D) {
		if (is2D) {
			ImageRenderer.ins.render(0F, 0F, 1F, 1F, "ui/mainmenu/wallpaper");
		}else {
			
		}
	}
}
