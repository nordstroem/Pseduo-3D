package util;

public class Vec2 {

	public double x, y;

	public Vec2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vec2 subtract(Vec2 v2) {
		return new Vec2(this.x - v2.x, this.y - v2.y);
	}
	
	public Vec2 rotate(double angle){
		angle = Math.toRadians(angle);
		double x0 = x;
		double y0 = y;
		x = x0*Math.cos(angle) - y0*Math.sin(angle);
		y = y0*Math.cos(angle) + x0*Math.sin(angle);
		return this;
	}
}
