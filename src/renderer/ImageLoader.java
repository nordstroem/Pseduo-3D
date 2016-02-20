package renderer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageLoader {
	
	
	public static BufferedImage tile = load("/res/tile.png");
	public static BufferedImage grassTile = load("/res/grassTile.png");
	public static BufferedImage ceilTile = load("/res/ceilTile.png");
	public static BufferedImage sprite = load("/res/sprite.png");
	
	public static BufferedImage load(String path){
		try {
			BufferedImage loaded = ImageIO.read(ImageLoader.class.getResource(path));
			BufferedImage img  = new BufferedImage(loaded.getWidth(), loaded.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics g = img.getGraphics();
			g.drawImage(loaded,0,0,null,null);
			g.dispose();
			return img;
		} catch (IOException e) {
			System.out.println("Could not load: " + path);
		}
		return null;
	}
	
}
