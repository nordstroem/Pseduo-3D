package graphics;

import game.Game;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import util.Input;

@SuppressWarnings("serial")
public class FrameLauncher extends Canvas {

	public static int SCREEN_WIDTH = 200;
	public static int SCREEN_HEIGHT = 150;
	public static int CANVAS_WIDTH = 800;
	public static int CANVAS_HEIGHT = 600;

	private BufferedImage image;
	private JFrame frame;
	private int[] pixels;
	private Renderer renderer;
	private Game game;
	private Input input;

	//Init the frame and the game object
	public void init() {
		frame = new JFrame("Pseudo 3D");
		frame.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.add(this);
		createBufferStrategy(2);
		image = new BufferedImage(SCREEN_WIDTH, SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
		renderer = new Renderer(SCREEN_WIDTH, SCREEN_HEIGHT);
		game = new Game();
		input = new Input(this, true);
	}

	private void loop() {

		//Main loop of the program
		while (true) {
			input.update();

			game.tick();
			renderer.render(game, input);

			//Get pixels
			for (int i = 0; i < SCREEN_WIDTH * SCREEN_HEIGHT; i++) {
				pixels[i] = renderer.pixels[i];
			}

			//Render pixels
			BufferStrategy bs = getBufferStrategy();
			Graphics2D g = (Graphics2D) bs.getDrawGraphics();
			g.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
			g.drawImage(image, 0, 0, CANVAS_WIDTH, CANVAS_HEIGHT, null);
			bs.show();
			g.dispose();

			try {
				Thread.sleep(12);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String args[]) {
		FrameLauncher FL = new FrameLauncher();
		FL.init();
		FL.loop();
	}

}
