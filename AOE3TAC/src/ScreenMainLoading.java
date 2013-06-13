import java.io.File;

public class ScreenMainLoading extends Screen {
	
	public ScreenMainLoading() {
		ImageRenderer.ins.recurDir(new File(Start.path + "art\\ui\\loading"));
		Thread run = new Thread("threadRun" + System.currentTimeMillis() % 10000) { // thread loading
		
			@Override
			public void run() {
				ImageRenderer.ins.init(); // load images?
			}
			
		};
		run.start();
	}
	
	private float rotation = 0;
	private boolean done = false;
	
	public void render(boolean is2D) { // render while open
		if (is2D) {
			ImageRenderer.ins.render(0F, 0F, 1F, 1F, "ui/loading/loading"); // render image... maybe differentiate / animate?
			int coin = Math.min(18, (int) (rotation / 18F)); // calculate coin texture, and make sure it is valid
			ImageRenderer.ins.render(0.45F, 0.04F, ("ui/loading/cs" + (1 + coin))); // draw coin
			rotation += 3; // increase rotation
			if (rotation >= 360F) { // reset
				rotation -= 360F;
			}
		}else {
			/*
			 * Model model = Model.getModel("ui/loading/loadingcoin"); // load coin model model.push(); // push matrix int coin = (int) (rotation / 18F); model.bind("ui/loading/c" + (1 + coin)); model.translate(0.5F, 0.3F, 0F); // translate, must be before rotate model.rotate(0F, rotation, 0F); // rotate, must be after translate model.scale(0.08F, 0.08F, 0.08F); // best to use at end of matrix manipulations, scales to good size model.draw();// draw coin model.pop();// pop matrix rotation++; // increase rotation if (rotation > 360F) { // reset rotation = 0F; }
			 */
			if (!done && ImageRenderer.ins.postinit()) {
				System.out.println("Art Loaded!");
				done = true;
				close();
				GuiRenderer.ins.screens.add(new ScreenMainMenu());
			}
		}
	}
}
