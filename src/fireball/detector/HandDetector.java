package fireball.detector;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import fireball.entities.Hand;

public class HandDetector {

	public ArrayList<Hand> scan(BufferedImage image){
		ArrayList<Hand> hands = new ArrayList<Hand>();
		for(int i = 0; i < image.getWidth(); i++){
			for(int j = 0; j < image.getHeight(); j++){
				Color c = new Color(image.getRGB(i, j));
				image.setRGB(i, j, grayscale(c));
			}
		}
		int[][] pixels = new int[image.getWidth()][image.getHeight()];
		for(int x = 1; x < image.getWidth() - 1; x++){
			for(int y = 1; y < image.getHeight() - 1; y++){
				Color val = sobel(image, x, y);
				pixels[x][y] = val.getRGB();
			}
		}
		for(int x = 1; x < image.getWidth() - 1; x++){
			for(int y = 1; y < image.getHeight() - 1; y++){
				image.setRGB(x, y, pixels[x][y]);
			}
		}
		return hands;
	}
	
	private int grayscale(Color c) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		int val = (int) (r * 0.21 + g * 0.72 + b * 0.07);
		return new Color(val, val, val).getRGB();
	}
	
	private Color sobel(BufferedImage image, int x, int y){
		// x value
		Color topRight = new Color(image.getRGB(x + 1, y - 1));
		Color middleRight = new Color(image.getRGB(x + 1, y));
		Color bottomRight = new Color(image.getRGB(x + 1, y + 1));
		Color topLeft = new Color(image.getRGB(x - 1, y - 1));
		Color middleLeft = new Color(image.getRGB(x - 1, y));
		Color bottomLeft = new Color(image.getRGB(x - 1, y + 1));
		int val1 = topLeft.getRed() * -1;
		val1 += middleLeft.getRed() * -2;
		val1 += bottomLeft.getRed() * -1;
		val1 += topRight.getRed();
		val1 += middleRight.getRed() * 2;
		val1 += bottomRight.getRed();
		
		// y value
		Color topCenter = new Color(image.getRGB(x, y - 1));
		Color bottomCenter = new Color(image.getRGB(x, y + 1));
		int val2 = topLeft.getRed() * -1;
		val2 += topCenter.getRed() * -2;
		val2 += topRight.getRed() * -1;
		val2 += bottomLeft.getRed() * 1;
		val2 += bottomCenter.getRed() * 2;
		val2 += bottomRight.getRed() * 1;
		
		int val = (int) Math.sqrt(val1 * val1 + val2 * val2);
		if(val > 255){
			val = 255;
		}
		
		return new Color(val, val, val);
	}
}
