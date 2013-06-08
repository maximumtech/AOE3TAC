public interface IMouseHandler {
	public void onButtonDown(int x, int y, int button);
	
	public void onButtonUp(int x, int y, int button);
	
	public void onButtonHeld(int x, int y, int button);
}
