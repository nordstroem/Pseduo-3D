package graphics;

import game.Game;

import java.awt.image.BufferedImage;

import util.Input;
import util.Vec3;

public class Renderer {

	public int pixels[];
	public double zBuffer[];

	private Vec3 cam;
	private double rot;
	private double rCos;
	private double rSin;

	private int width;
	private int height;
	private double fov; //Not really fov
	private double zClip;

	public Renderer(int screenWidth, int screenHeight) {
		this.width = screenWidth;
		this.height = screenHeight;
		pixels = new int[screenWidth * screenHeight];
		zBuffer = new double[screenWidth * screenHeight];
		cam = new Vec3(7.5, 0, -5.6);
		rot = -56;
		fov = 2;
		zClip = 0.2;
	}

	int t = 0;

	//Remaps target value from one linear interval to another
	private double remap(double value, double low1, double high1, double low2, double high2) {
		return low2 + (value - low1) * (high2 - low2) / (high1 - low1);
	}

	private double clamp(double val, double min, double max) {
		if (val < min) {
			return min;
		} else if (val > max) {
			return max;
		} else {
			return val;
		}
	}

	public void render(Game game, Input input) {
		zBuffer = new double[width * height];
		pollInput(input);
		rCos = Math.cos(Math.toRadians(rot));
		rSin = Math.sin(Math.toRadians(rot));

		t++;

		//Fill screen with black
		for (int i = 0; i < width * height; i++) {
			pixels[i] = 0x000000;
		}

		renderFloor();
		renderWall(-1, 1, 1, 1, -1, 2);
		renderWall(-1, -1, -1, 1, -1, 2);
		renderWall(-1, -1, 1, -1, -1, 2);
		renderWall(1, 1, 1, -1, -1, 2);

		renderSprite(3, -0.5, 0, 1, 1, ImageLoader.sprite);
		postProcess();
	}

	private void pollInput(Input input) {

		//Camera control
		if (input.isKeyDown(input.LEFT)) {
			rot -= 2;
		}
		if (input.isKeyDown(input.RIGHT)) {
			rot += 2;
		}
		if (input.isKeyDown(input.SHIFT)) {
			cam.y += 0.1;
		}
		if (input.isKeyDown(input.CTRL)) {
			cam.y -= 0.1;
		}

		double angle = Math.toRadians(rot);

		if (input.isKeyDown(input.W) || input.isKeyDown(input.UP) ) {
			cam.z += 0.1 * Math.cos(angle);
			cam.x += 0.1 * Math.sin(angle);
		}
		if (input.isKeyDown(input.S) || input.isKeyDown(input.DOWN)) {
			cam.z -= 0.1 * Math.cos(angle);
			cam.x -= 0.1 * Math.sin(angle);
		}
		if (input.isKeyDown(input.D)) {
			cam.z += 0.1 * Math.cos(angle + 1.57);
			cam.x += 0.1 * Math.sin(angle + 1.57);
		}
		if (input.isKeyDown(input.A)) {
			cam.z -= 0.1 * Math.cos(angle + 1.57);
			cam.x -= 0.1 * Math.sin(angle + 1.57);
		}

	}

	private void renderCube(double xc, double yc, double zc, double w, double h) {

	}

	private void renderSprite(double xc, double yc, double zc, double w, double h, BufferedImage tex) {

		//World vector
		double rx = (xc - cam.x) * rCos - (zc - cam.z) * rSin;
		double rz = (zc - cam.z) * rCos + (xc - cam.x) * rSin;
		double ry = (yc - cam.y);

		//Check z-clipping
		if (rz < zClip) {
			return;
		}

		//Perspective transformation
		double xs = rx / rz * fov;
		double ys = ry / rz * fov * width / height;
		double ws = (w / rz * fov * width / 2.0);
		double hs = (h / rz * fov * width / 2.0);
		
		//Remap to screen coordinates
		double sxs = remap(xs, -1, 1, 0, width);
		double sys = remap(ys, -1, 1, height, 0);

		int sWidth = tex.getWidth();
		int sHeight = tex.getHeight();
		
		//Render the sprite
		for(int x = (int) Math.ceil(sxs - ws / 2.0); x < Math.floor(sxs + ws / 2.0); x++) {
			double r1 = (double)(x - sxs + ws / 2.0) / ws;
			int tx = (int)Math.floorMod((int)(r1 * sWidth), sWidth);
			
			for(int y = (int) Math.ceil(sys - hs / 2.0); y < Math.floor(sys + hs / 2.0); y++){
				double r2 = (double)(y - sys + hs / 2.0) / hs;
				int ty = (int)Math.floorMod((int)(r2 * sHeight), sHeight);
				
				if (x >= 0 && x < width && y >= 0 && y < height) {
					int col = tex.getRGB(tx, ty);
					if (rz < zBuffer[x + y * width] && col != 0xFFFF00FF) {
						pixels[x + y * width] = col;
						zBuffer[x + y * width] = rz;
					}
				}
			}
			
		}
		

	}

	private void renderWall(double x0, double z0, double x1, double z1, double y0, double y1) {

		//Get the rotated relative vector
		double rxl = (x0 - cam.x) * rCos - (z0 - cam.z) * rSin;
		double rzl = (z0 - cam.z) * rCos + (x0 - cam.x) * rSin;
		double ryl = (y0 - cam.y);
		double rxr = (x1 - cam.x) * rCos - (z1 - cam.z) * rSin;
		double rzr = (z1 - cam.z) * rCos + (x1 - cam.x) * rSin;
		double ryr = (y0 - cam.y);

		//Check z-clipping
		if (rzl < zClip && rzr < zClip)
			return;

		if (rzl < zClip) {
			double p = (zClip - rzl) / (rzr - rzl);
			ryl = ryl + (ryr - ryl) * p;
			rxl = rxl + (rxr - rxl) * p;
			rzl = zClip;
		}

		if (rzr < zClip) {
			double p = (zClip - rzl) / (rzr - rzl);
			ryr = ryl + (ryr - ryl) * p;
			rxr = rxl + (rxr - rxl) * p;
			rzr = zClip;
		}

		//Perspective
		double xl = rxl / rzl * fov;
		double xr = rxr / rzr * fov;
		double ylb = ryl / rzl * fov * width / height;
		double yla = (ryl + y1) / rzl * fov * width / height;
		double yrb = ryr / rzr * fov * width / height;
		double yra = (ryr + y1) / rzr * fov * width / height;

		//Project from -1, 1 to screen coordinates
		int sxl = (int) ((xl + 1) * width / 2);
		int sxr = (int) ((xr + 1) * width / 2);
		int syla = (int) (height + (yla + 1) * -height / 2);
		int sylb = (int) (height + (ylb + 1) * -height / 2);
		int syra = (int) (height + (yra + 1) * -height / 2);
		int syrb = (int) (height + (yrb + 1) * -height / 2);

		//Swap left and right screen cords, if needed
		if (sxr < sxl) {
			int temp = sxr;
			sxr = sxl;
			sxl = temp;

			temp = syla;
			syla = syra;
			syra = temp;

			temp = sylb;
			sylb = syrb;
			syrb = temp;

			double tempd = rzl;
			rzl = rzr;
			rzr = tempd;
		}

		//Paint the wall
		for (int x = sxl; x < sxr; x++) {
			double r = (double) (x - sxl) / (sxr - sxl);
			double sy1 = syla + r * (syra - syla);
			double sy2 = sylb + r * (syrb - sylb);

			for (int y = (int) sy1; y < (int) sy2; y++) {
				double r2 = (double) (y - sy1) / (sy2 - sy1);
				if (x >= 0 && x < width && y >= 0 && y < height) {
					int tx = Math.floorMod((int) (ImageLoader.tile.getWidth() * r), 16);
					int ty = Math.floorMod((int) (ImageLoader.tile.getHeight() * r2), 16);

					double z = rzl + r * (rzr - rzl);
					if (z < zBuffer[x + y * width]) {
						zBuffer[x + y * width] = z;
						pixels[x + y * width] = ImageLoader.tile.getRGB(tx, ty);
					}

				}
			}
		}

	}

	private void renderFloor() {
		double y = -1;

		for (int syr = 0; syr < height; syr++) {
			double sy = remap(syr, height, 0, -1, 1);
			double z;

			if (sy > 0) {
				z = (y + cam.y) * fov / sy * width / height * -1;
			} else {
				z = (y - cam.y) * fov / sy * width / height;
			}
			for (int sxr = 0; sxr < width; sxr++) {
				double sx = remap(sxr, 0, width, -1, 1);

				if (sy == 0) {
					zBuffer[sxr + syr * width] = Double.MAX_VALUE;
					continue;
				}

				double x = sx * z / fov;

				double rx = x * rCos + z * rSin + cam.x;
				double rz = z * rCos - x * rSin + cam.z;

				int tx = Math.floorMod((int) (rx * 10), 16);
				int ty = Math.floorMod((int) (rz * 10), 16);

				zBuffer[sxr + syr * width] = z;

				if (sy < 0) {
					pixels[sxr + syr * width] = ImageLoader.grassTile.getRGB(tx, ty);
				} else {
					pixels[sxr + syr * width] = ImageLoader.ceilTile.getRGB(tx, ty);
				}

			}
		}
	}

	private void postProcess() {

		for (int i = 0; i < width * height; i++) {
			double z = zBuffer[i];
			int cur = pixels[i];
			double brightness = 1.0 / (1.0 + 0.5 * z);
			int r = (cur >> 16) & 0xFF;
			int g = (cur >> 8) & 0xFF;
			int b = (cur) & 0xFF;
			r = (int) clamp(r * brightness, 0, 255);
			g = (int) clamp(g * brightness, 0, 255);
			b = (int) clamp(b * brightness, 0, 255);
			pixels[i] = r << 16 | g << 8 | b;
		}

	}

}
