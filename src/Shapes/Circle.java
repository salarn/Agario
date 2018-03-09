package Shapes;

import java.awt.Color;
import java.awt.Graphics;

public abstract class Circle implements Comparable  {
	
	private Point  center;
	private double radius;
	private Color  borderColor;
	
	static protected double borderLen = 3; 
	
	
	
	
	// Constructors
	public Circle ()  {
		this( new Point(0,0), 10, Color.RED );
	}
	
	public Circle ( Circle c ){
		this( c.getCenter(), c.getRadius(), c.getBorderColor() );
	}
	
	public Circle ( Point center, double radius, Color borderColor ){
		this.center      = center;
		this.radius      = radius;
		this.borderColor = borderColor;
	}
	public Circle(String json){
		String [] s = json.split(",");
		this.center = new Point(Integer.parseInt(s[0]),Integer.parseInt(s[1]));
		this.radius = Integer.parseInt(s[2]);
	}

	
	
	
	// getter and setter Methods
	
	public Point getCenter() {
		return center;
	}
	
	public void setCenter(Point center) {
		this.center = center;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public void setRadius(double radius) {
		this.radius = radius;
	}
	
	public Color getBorderColor() {
		return borderColor;
	}
	
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}
	
	public static double getBorderLen() {
		return borderLen;
	}
	
	public static void setBorderLen(double borderLen) {
		Circle.borderLen = borderLen;
	}
	
	
	//////
	

	double getArea () {
		return Math.PI * this.radius * this.radius;
	}
	
	void areaAddition( double areaAdd ){
		double area = this.getArea() + areaAdd;
		setRadius( Math.sqrt(area / Math.PI) );
	}
	
	public boolean isIn ( Circle c ){
		if ( this.center.getDistance(c.getCenter()) < this.radius + c.getRadius()/2.0 )
			return true;
		return false;
	}
	
	
	@Override
	public int compareTo(Object cir) {
		double cirRad = ( (Circle) cir ).getRadius();
		return (int) (- this.radius + cirRad); 
		
	}
	
	/// abstract Methods
	

	abstract public void Render  ( Graphics G );
	abstract public int  getType ();
}

