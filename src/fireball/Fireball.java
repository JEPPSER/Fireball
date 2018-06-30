package fireball;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.github.sarxos.webcam.Webcam;

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
		new AnimationTimer() {
			@Override
			public void handle(long arg0) {
				BufferedImage image = flipImage(webcam.getImage());
				// Do image recognition stuff pls....
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
				after.setRGB(after.getWidth() - x - 1, y, temp);
			}
		}
		return after;
	}
}
