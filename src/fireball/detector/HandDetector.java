package fireball.detector;

import java.awt.Color;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import fireball.entities.Hand;

public class HandDetector {

	double[][] angles;

	public ArrayList<Hand> scan(BufferedImage image) {
		ArrayList<Hand> hands = new ArrayList<Hand>();
		angles = new double[image.getWidth()][image.getHeight()];

		// Convert image to grayscale
		for (int i = 0; i < image.getWidth(); i++) {
			for (int j = 0; j < image.getHeight(); j++) {
				Color c = new Color(image.getRGB(i, j));
				image.setRGB(i, j, grayscale(c));
			}
		}

		// Put all pixels in image in array
		int[][] pixels = new int[image.getWidth()][image.getHeight()];
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 1; y < image.getHeight() - 1; y++) {
				pixels[x][y] = image.getRGB(x, y);
			}
		}

		// blur image
		pixels = gBlur(pixels);

		// sobel operator
		pixels = sobel(pixels);

		// canny algorithm
		pixels = canny(pixels);
		
		//ArrayList<ArrayList<Point>> lines = getLines(pixels);

		// Set image pixels to array values.
		for (int x = 0; x < image.getWidth(); x++) {
			for (int y = 0; y < image.getHeight(); y++) {
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
	
	private boolean contains(ArrayList<ArrayList<Point>> points, int x, int y){
		boolean result = false;
		return result;
	}
	
	private ArrayList<ArrayList<Point>> getLines(int[][] pixels){
		ArrayList<ArrayList<Point>> result = new ArrayList<ArrayList<Point>>();
		for(int x = 0; x < angles.length; x++){
			for(int y = 0; y < angles[0].length; y++){
				double angle = Math.toDegrees(angles[x][y]);
				if(angle < 0){
					angle += 360;
				}
				if (angle >= 270 && angle <= 282.5) {
					angle -= 180;
				} else if (angle >= 282.5 && angle <= 337.5) {
					angle -= 180;
				} else if (angle >= 337.5 && angle <= 360) {
					angle -= 180;
				}
				angles[x][y] = Math.toRadians(angle);
			}
		}
		
		for (int x = 1; x < pixels.length - 1; x++) {
			for (int y = 1; y < pixels[0].length - 1; y++) {
				Color middle = new Color(pixels[x][y]);
				if (middle.getRed() == 255) {
					Color topRight = new Color(pixels[x + 1][y - 1]);
					Color middleRight = new Color(pixels[x + 1][y]);
					Color bottomRight = new Color(pixels[x + 1][y + 1]);
					Color topLeft = new Color(pixels[x - 1][y - 1]);
					Color middleLeft = new Color(pixels[x - 1][y]);
					Color bottomLeft = new Color(pixels[x - 1][y + 1]);
					Color topCenter = new Color(pixels[x][y - 1]);
					Color bottomCenter = new Color(pixels[x][y + 1]);
					if(topRight.getRed() == 255 && angles[x + 1][y - 1] > angles[x][y] - 20 &&
							angles[x + 1][y - 1] < angles[x][y] + 20){
						if(contains(result, x + 1, y - 1)){
							// Add point in existing line.
						} else {
							// Create new line and put point in it.
						}
					} else if(middleRight.getRed() == 255 && angles[x + 1][y] > angles[x][y] - 20 &&
							angles[x + 1][y] < angles[x][y] + 20){
						if(contains(result, x + 1, y)){
							
						}
					} else if(bottomRight.getRed() == 255 && angles[x + 1][y + 1] > angles[x][y] - 20 &&
							angles[x + 1][y + 1] < angles[x][y] + 20){
						if(contains(result, x + 1, y + 1)){
							
						}
					} else if(topLeft.getRed() == 255 && angles[x - 1][y - 1] > angles[x][y] - 20 &&
							angles[x - 1][y - 1] < angles[x][y] + 20){
						if(contains(result, x - 1, y - 1)){
							
						}
					} else if(middleLeft.getRed() == 255 && angles[x - 1][y] > angles[x][y] - 20 &&
							angles[x - 1][y] < angles[x][y] + 20){
						if(contains(result, x - 1, y)){
							
						}
					} else if(bottomLeft.getRed() == 255 && angles[x - 1][y + 1] > angles[x][y] - 20 &&
							angles[x - 1][y + 1] < angles[x][y] + 20){
						if(contains(result, x - 1, y + 1)){
							
						}
					} else if(topCenter.getRed() == 255 && angles[x][y - 1] > angles[x][y] - 20 &&
							angles[x][y - 1] < angles[x][y] + 20){
						if(contains(result, x, y - 1)){
							
						}
					} else if(bottomCenter.getRed() == 255 && angles[x][y + 1] > angles[x][y] - 20 &&
							angles[x][y + 1] < angles[x][y] + 20){
						if(contains(result, x, y + 1)){
							
						}
					}
				}
			}
		}
		return result;
	}

	private int[][] canny(int[][] pixels) {
		int[][] result = new int[pixels.length][pixels[0].length];
		for (int x = 1; x < pixels.length - 1; x++) {
			for (int y = 1; y < pixels[0].length - 1; y++) {
				double angle = Math.toDegrees(angles[x][y]);
				if (angle < 0) {
					angle += 360;
				}
				Color topRight = new Color(pixels[x + 1][y - 1]);
				Color middleRight = new Color(pixels[x + 1][y]);
				Color bottomRight = new Color(pixels[x + 1][y + 1]);
				Color topLeft = new Color(pixels[x - 1][y - 1]);
				Color middleLeft = new Color(pixels[x - 1][y]);
				Color bottomLeft = new Color(pixels[x - 1][y + 1]);
				Color topCenter = new Color(pixels[x][y - 1]);
				Color bottomCenter = new Color(pixels[x][y + 1]);
				Color middle = new Color(pixels[x][y]);
				Color black = new Color(pixels[x][y]);
				if (angle >= 22.5 && angle <= 67.5) {
					if (middle.getRed() > topLeft.getRed() && middle.getRed() > bottomRight.getRed()
							&& middle.getRed() > 70) {
						result[x][y] = new Color(255, 255, 255).getRGB();
					} else {
						result[x][y] = black.getRGB();
					}
				} else if (angle >= 67.5 && angle <= 90 || angle >= 270 && angle <= 282.5) {
					if (middle.getRed() > topCenter.getRed() && middle.getRed() > bottomCenter.getRed()
							&& middle.getRed() > 70) {
						result[x][y] = new Color(255, 255, 255).getRGB();
					} else {
						result[x][y] = black.getRGB();
					}
				} else if (angle >= 282.5 && angle <= 337.5) {
					if (middle.getRed() > topRight.getRed() && middle.getRed() > bottomLeft.getRed()
							&& middle.getRed() > 70) {
						result[x][y] = new Color(255, 255, 255).getRGB();
					} else {
						result[x][y] = black.getRGB();
					}
				} else if (angle < 22.5 || angle >= 337.5 && angle <= 360) {
					if (middle.getRed() > middleLeft.getRed() && middle.getRed() > middleRight.getRed()
							&& middle.getRed() > 70) {
						result[x][y] = new Color(255, 255, 255).getRGB();
					} else {
						result[x][y] = black.getRGB();
					}
				}
			}
		}

		for(int x = 0; x < result.length; x++){
			for(int y = 0; y < result[0].length; y++){
				if(new Color(result[x][y]).getRed() != 255){
					result[x][y] = new Color(0, 0, 0).getRGB();
				}
			}
		}
		return result;
	}
	
	private int[][]mBlur(int[][] pixels){
		int[][] result = new int[pixels.length][pixels[0].length];

		for (int x = 1; x < result.length - 1; x++) {
			for (int y = 1; y < result[0].length - 1; y++) {
				Color topRight = new Color(pixels[x + 1][y - 1]);
				Color middleRight = new Color(pixels[x + 1][y]);
				Color bottomRight = new Color(pixels[x + 1][y + 1]);
				Color topLeft = new Color(pixels[x - 1][y - 1]);
				Color middleLeft = new Color(pixels[x - 1][y]);
				Color bottomLeft = new Color(pixels[x - 1][y + 1]);
				Color topCenter = new Color(pixels[x][y - 1]);
				Color bottomCenter = new Color(pixels[x][y + 1]);
				Color middle = new Color(pixels[x][y]);

				int val = topRight.getRed();
				val += middleRight.getRed();
				val += bottomRight.getRed();
				val += topLeft.getRed();
				val += middleLeft.getRed();
				val += bottomLeft.getRed();
				val += topCenter.getRed();
				val += bottomCenter.getRed();
				val += middle.getRed();
				val /= 9;
				result[x][y] = new Color(val, val, val).getRGB();
			}
		}
		return result;
	}

	private int[][] gBlur(int[][] pixels) {
		int[][] result = new int[pixels.length][pixels[0].length];

		for (int x = 1; x < result.length - 1; x++) {
			for (int y = 1; y < result[0].length - 1; y++) {
				Color topRight = new Color(pixels[x + 1][y - 1]);
				Color middleRight = new Color(pixels[x + 1][y]);
				Color bottomRight = new Color(pixels[x + 1][y + 1]);
				Color topLeft = new Color(pixels[x - 1][y - 1]);
				Color middleLeft = new Color(pixels[x - 1][y]);
				Color bottomLeft = new Color(pixels[x - 1][y + 1]);
				Color topCenter = new Color(pixels[x][y - 1]);
				Color bottomCenter = new Color(pixels[x][y + 1]);
				Color middle = new Color(pixels[x][y]);

				int val = topRight.getRed();
				val += middleRight.getRed() * 2;
				val += bottomRight.getRed();
				val += topLeft.getRed();
				val += middleLeft.getRed() * 2;
				val += bottomLeft.getRed();
				val += topCenter.getRed() * 2;
				val += bottomCenter.getRed() * 2;
				val += middle.getRed() * 4;
				val /= 16;
				result[x][y] = new Color(val, val, val).getRGB();
			}
		}
		return result;
	}

	private int[][] sobel(int[][] pixels) {
		int[][] result = new int[pixels.length][pixels[0].length];

		for (int x = 1; x < result.length - 1; x++) {
			for (int y = 1; y < result[0].length - 1; y++) {
				Color topRight = new Color(pixels[x + 1][y - 1]);
				Color middleRight = new Color(pixels[x + 1][y]);
				Color bottomRight = new Color(pixels[x + 1][y + 1]);
				Color topLeft = new Color(pixels[x - 1][y - 1]);
				Color middleLeft = new Color(pixels[x - 1][y]);
				Color bottomLeft = new Color(pixels[x - 1][y + 1]);
				Color topCenter = new Color(pixels[x][y - 1]);
				Color bottomCenter = new Color(pixels[x][y + 1]);

				double val1 = topLeft.getRed() * -1;
				val1 += middleLeft.getRed() * -2;
				val1 += bottomLeft.getRed() * -1;
				val1 += topRight.getRed();
				val1 += middleRight.getRed() * 2;
				val1 += bottomRight.getRed();

				double val2 = topLeft.getRed() * -1;
				val2 += topCenter.getRed() * -2;
				val2 += topRight.getRed() * -1;
				val2 += bottomLeft.getRed() * 1;
				val2 += bottomCenter.getRed() * 2;
				val2 += bottomRight.getRed() * 1;

				int val = (int) Math.sqrt(val1 * val1 + val2 * val2);
				if (val > 255) {
					val = 255;
				}
				double angle;
				if (val1 != 0) {
					angle = Math.atan(val2 / val1);
				} else {
					angle = Math.PI / 2;
				}
				
				if(angle < 0){
					angle += Math.PI * 2;
				}
				
				angles[x][y] = angle;
				result[x][y] = new Color(val, val, val).getRGB();
			}
		}
		return result;
	}
}
