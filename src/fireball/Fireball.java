package fireball;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import com.github.sarxos.webcam.Webcam;

import fireball.detector.HandDetector;
import fireball.entities.Hand;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Fireball extends Application{

	public static void main(String[] args) throws IOException {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Pane root = new Pane();
		ImageView iv = new ImageView();
		root.getChildren().add(iv);
		root.setPrefSize(640, 480);
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
		Webcam webcam = Webcam.getDefault();
		webcam.setViewSize(new Dimension(640, 480));
		webcam.open();
		HandDetector detector = new HandDetector();
		new AnimationTimer() {
			@Override
			public void handle(long arg0) {
				BufferedImage image = flipImage(webcam.getImage());
				ArrayList<Hand> hands = detector.scan(image);
				Image img = SwingFXUtils.toFXImage(image, null);
				iv.setImage(img);
			}
		}.start();
	}
	
	private BufferedImage flipImage(BufferedImage image){
		BufferedImage after = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for(int x = 0; x < image.getWidth(); x++){
			for(int y = 0; y < image.getHeight(); y++){
				int temp = image.getRGB(x, y);
				Color c = new Color(temp);
				after.setRGB(after.getWidth() - x - 1, y, c.getRGB());
			}
		}
		return after;
	}
	
	private int grayscale(Color c) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		int val = (int) (r * 0.21 + g * 0.72 + b * 0.07);
		return new Color(val, val, val).getRGB();
	}
	
	private int negative(Color c) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		return new Color(255 - r, 255 - g, 255 - b).getRGB();
	}
}
