import java.io.File;

public class ScreenMainLoading extends Screen {
	
	private long load;
	
	public ScreenMainLoading() {
		ImageRenderer.ins.recurDir(new File(Start.path + "art\\ui\\loading"));
		ImageRenderer.ins.postinit();
		AudioHandler.ins.loadClip("music/title");
		MusicPlayer.ins.start("music/title");
		load = System.currentTimeMillis();
		Thread run = new Thread("threadRun" + System.currentTimeMillis() % 10000) { // thread loading
		
			@Override
			public void run() {
				// FontRenderer.ins.preinit(new Font("Arial", 0, 12));
				ImageRenderer.ins.init(); // load images?
				AudioHandler.ins.init();
			}
			
		};
		run.start();
	}
	
	private float rotation = 0;
	private boolean artdone = false;
	private boolean audiodone = false;
	
	public void render(boolean is2D) { // render while open
		if (is2D) {
			ImageRenderer.ins.render(0F, 0F, 1F, 1F, "ui/loading/loading"); // render image... maybe differentiate / animate?
			int coin = Math.min(18, (int) (rotation / 18F)); // calculate coin texture, and make sure it is valid
			ImageRenderer.ins.render(0.45F, 0.04F, ("ui/loading/cs" + (1 + coin))); // draw coin
			rotation += 4; // increase rotation
			if (rotation >= 360F) { // reset
				rotation -= 360F;
			}
			if (!artdone && ImageRenderer.ins.canpostinit()) {
				System.out.println("Art Loaded!");
				artdone = true;
			}
			if (!audiodone && AudioHandler.ins.canpostinit()) {
				System.out.println("Audio Loaded!");
				audiodone = true;
			}
			if (artdone && audiodone && load + 10000L < System.currentTimeMillis()) {
				ImageRenderer.ins.postinit();
				AudioHandler.ins.postinit();
				close();
				GuiRenderer.ins.screens.add(new ScreenMainMenu());
			}
		}else {
			
		}
	}
}
