public class AspectManager { // converts from hardware coordinate(translated to -0.5, -0.5) to pixels & aspected hardware coordinates
	public static float ToAspectX(float hardwareX) {
		return hardwareX * (1F / ((float) Start.screenWidth / 1024F));
	}
	
	public static float ToAspectY(float hardwareY) {
		return hardwareY * (1F / ((float) Start.screenHeight / 1024F));
	}
	
	public static float ToAspectX(int pixelX) {
		return ToAspectX(ToHardwareX(pixelX));
	}
	
	public static float ToAspectY(int pixelY) {
		return ToAspectY(ToHardwareY(pixelY));
	}
	
	public static float ToHardwareX(int pixelX) {
		return ((float) pixelX / (float) Start.screenWidth) * Start.screenWidthRatio;
	}
	
	public static float ToHardwareY(int pixelY) {
		return ((float) pixelY / (float) Start.screenHeight) * Start.screenHeightRatio;
	}
}
