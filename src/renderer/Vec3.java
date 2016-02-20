package renderer;

public class Vec3 {
	 
    public double x = 0.0f;
    public double y = 0.0f;
    public double z = 0.0f;
     
    public Vec3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
     
    public Vec3(){ 
    	
    }
    
    public Vec3 rotate(double angle){
		angle = Math.toRadians(angle);
		double x0 = x;
		double z0 = z;
		x = x0*Math.cos(angle) - z0*Math.sin(angle);
		z = z0*Math.cos(angle) + x0*Math.sin(angle);
		return this;
    }
    
    public double dot(Vec3 v){
    	return this.x*v.x + this.y*v.y + this.z*v.z;
    }
    
    public Vec3 normalized(){
    	double f = (double)Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
    	return new Vec3(this.x/f, this.y/f, this.z/f);
    }
    
    public Vec3 cross(Vec3 v){
    	Vec3 vec = new Vec3();
    	vec.x = this.y*v.z - this.z*v.y;
    	vec.y = this.z*v.x - this.x*v.z;
    	vec.z = this.x*v.y - this.y*v.x;
    	return vec;
    	
    }

	public Vec3 subtract(Vec3 v) {
		return new Vec3(this.x - v.x, this.y - v.y, this.z - v.z);
	}

	public Vec3 multiply(double i) {
		return new Vec3(i*this.x, i*this.y, i*this.z);
	}
	
	public double distanceTo(Vec3 v) {
		double dx = x - v.x;
		double dy = y - v.y;
		double dz = z - v.z;
		return (double)Math.sqrt(dx*dx + dy*dy + dz*dz);
	}

	public Vec3 add(Vec3 v) {
		return new Vec3(this.x + v.x, this.y + v.y, this.z + v.z);
	}

	public double length() {
		return (double)Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
	}
     
}