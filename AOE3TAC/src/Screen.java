import java.util.ArrayList;

public class Screen {
	
	public ArrayList<Component> components = new ArrayList<Component>();
	
	public Screen() { // on open(usually), not yet rendering.
	
	}
	
	public void render(boolean is2D) { // render while open
	
	}
	
	public void callComponentAction(Component component, int id) {
		
	}
	
	public void renderComponents() { // render components (buttons, textboxes, etc) while open - should not be overridden, call super
		for (Component component : new ArrayList<Component>(components)) {
			component.render();
		}
	}
	
	public void tick() { // tick while open call super!
		for (Component component : new ArrayList<Component>(components)) {
			component.tick();
		}
	}
	
	public final void close() { // closes window, stops rendering. do not call from initializer
		onClose(); // alert ones self
		GuiRenderer.ins.screens.remove(this); // ends rendering
		for (Component component : new ArrayList<Component>(components)) {
			component.onClose();
			components.remove(component);
		}
	}
	
	public void onClose() { // called when closing, prepare to close
	
	}
	
	public int getRelComponentX() {
		return 0;
	}
	
	public int getRelComponentY() {
		return 0;
	}
	
}
