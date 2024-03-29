import java.io.IOException;

public class SubstractImage {

	public static void main(String[] args) throws IOException {
		RGBImage subtract = new RGBImage(args[0]);
		RGBImage subtractFrom = new RGBImage(args[1]);
		
		int height = Math.min(subtract.getHeight(), subtractFrom.getHeight());
		int width = Math.min(subtract.getWidth(), subtractFrom.getWidth());
		
		RGBImage difference = new RGBImage(subtract.getHeight(), subtract.getWidth());

		RGBPixel subtractPixel;
		RGBPixel subtractFromPixel;
		
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				subtractFromPixel = subtractFrom.getPixel(r, c);
				subtractPixel = subtract.getPixel(r, c);
				difference.setPixel(r, c, subtractFromPixel.getRed() - subtractPixel.getRed(),
						subtractFromPixel.getGreen() - subtractPixel.getGreen(),
						subtractFromPixel.getBlue() - subtractPixel.getBlue());
			}
		}

		difference.writeImage(args[1]+"_-_"+args[0]+".jpg");
	}

}
