public class AspectManager { // converts from hardware coordinate(translated to -0.5, -0.5) to pixels & aspected hardware coordinates
	public static float ToAspectX(float hardwareX) {
		return hardwareX * (1F / ((float) Start.screenWidth / 1024F));
	}
	
	public static float ToAspectY(float hardwareY) {
		return hardwareY * (1F / ((float) Start.screenHeight / 1024F));
	}
	
	public static float ToAspectX(int pixelX) {
		return ((float) pixelX / (float) Start.screenWidth);// * Start.screenWidthRatio;
	}
	
	public static float ToAspectY(int pixelY) {
		return ((float) pixelY / (float) Start.screenHeight);// * Start.screenHeightRatio;
	}
	
	public static float ToHardwareX(int pixelX) {
		return ToHardwareX(ToAspectX(pixelX));
	}
	
	public static float ToHardwareY(int pixelY) {
		return ToHardwareY(ToAspectY(pixelY));
	}
	
	public static float ToHardwareX(float aspectX) {
		return aspectX / (1F / ((float) Start.screenWidth / 1024F));
	}
	
	public static float ToHardwareY(float aspectY) {
		return aspectY / (1F / ((float) Start.screenHeight / 1024F));
	}
}
