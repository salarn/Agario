package Shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Bubble extends Circle {

	private String         name;
	private Color          fillColor;
	private BufferedImage  img = null;
	
	private boolean        isDead = false;
	
	private static double minRadConst = 25.0;
	

	/// Constructors

	public Bubble () {
		this ( new Point(100, 100), 20, Color.RED, "",  Color.RED, null );
	}

	public Bubble ( Bubble bub ){
		this( bub.getCenter(), bub.getRadius(), bub.getBorderColor(), bub.name, bub.fillColor, bub.img );
	}

	public Bubble ( Point center, double radius, Bubble bub ) {
		this( center, radius , bub.getBorderColor(), bub.name, bub.fillColor, bub.img );
	}
	
	public Bubble ( Point center, double radius, Color borderColor, String name, Color fillColor, BufferedImage img ){
		super( center, radius, borderColor );
		this.name      = name;
		this.fillColor = fillColor;
		this.img       = img;
	}



	/// getter and setter Methods

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}
	
	public boolean isDead() {
		return isDead;
	}
	
	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public static double getMinRadConst() {
		return minRadConst;
	}
	
	public static void setMinRadConst(double minRadConst) {
		Bubble.minRadConst = minRadConst;
	}
	

	/// other methods
	


	public void eat ( Circle cir ){
		this.areaAddition(cir.getArea());
	}
	
	public void move ( Point pnt, double speed ){
		this.setCenter( this.getCenter().getAddition(pnt.getScaller(speed)));
	}
	
	public List<Bubble> gearDisparting () {
		List<Bubble> lst = new ArrayList<>();
		double area = getArea();
		int cnt = (int) ( area / ( minRadConst*minRadConst*Math.PI) );
		double newRad = Math.sqrt( (area/cnt)/Math.PI );
		
		Point pnt = new Point( newRad+2*Circle.borderLen, 0 );
		Bubble b = null;
		for ( int i = 0 ; i < cnt; ++i ){
			b = new Bubble(getCenter().getAddition(pnt.getScaller(i)), newRad, this);
			lst.add(b);
		}
		return lst;
	}

	//// abstract overriding methods
	@Override
	public void Render(Graphics G) {
		if ( isDead )
			return;
		Graphics2D g = (Graphics2D) G;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Ellipse2D.Double circle;
		double rad = this.getRadius() + borderLen;
		circle = new Ellipse2D.Double(this.getCenter().getX() - rad, this.getCenter().getY() - rad, 2 * rad, 2 * rad );

		g.setColor(getBorderColor());
		g.fill(circle);


		if ( img == null ){
			rad = this.getRadius();
			circle = new Ellipse2D.Double(this.getCenter().getX() - rad, this.getCenter().getY() - rad, 2 * rad, 2 * rad);

			g.setColor(this.fillColor);
			g.fill(circle);
		}
		else {
			rad = this.getRadius();
			g.setClip(new Ellipse2D.Double((int) (this.getCenter().getX() - rad), (int) (this.getCenter().getY() - rad),
					(int) (2 * rad), (int) (2 * rad)));

			g.drawImage(img, (int) (this.getCenter().getX() - rad), (int) (this.getCenter().getY() - rad),
					(int) (2 * rad), (int) (2 * rad), null);

			g.setClip(null);
		}


		{ // display name
			//TODO
			double scale = (this.getRadius() / (20.0) ) ;
			g.setColor(Color.BLACK);
			g.scale(scale, scale);
			g.drawString(this.name, (int) (this.getCenter().getX()/scale - this.getRadius()/scale ), 
					(int) (this.getCenter().getY()/scale));
			g.scale(1.0/scale, 1.0/scale);
		}
	}

	@Override
	public int getType() {
		return 0;
	}


}

