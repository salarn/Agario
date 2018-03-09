package Shapes;

public class Point {
	
	private double x;
	private double y;
	
	
	//Constructors
	public Point () {
		x = y = 0;
	}
	
	public Point ( java.awt.Point point ){
		x = point.x;
		y = point.y;
	}
	
	public Point ( double x, double y ){
		this.x = x;
		this.y = y;
	}
	
	public Point ( Point p ){
		x = p.x;
		y = p.y;
	}

	
	/// get and set Methods
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	
	/// toString Method
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
	

	/// other useful Methods
	public double getRad () {
		return Math.sqrt(x*x + y*y);
	}
	
	public double getArg (){
		return Math.atan2(y, x);
	}
	
	// with other Points 
	
	public double getDistance ( Point p ){
		return Math.sqrt( (p.x - x)*(p.x - x) + (p.y - y)*(p.y - y) );
	}
	
	public Point getDifference ( Point p ){
		return new Point( x - p.x, y - p.y );
	}
	
	public Point getAddition ( Point p ){
		return new Point( x + p.x, y + p.y );
	}
	
	public Point getScaller ( double scal ){
		return new Point( x*scal, y*scal );
	}
	
	
}
