package Shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

public class Dot extends Circle {

	private Color   fillColor;
	private boolean isDead = false;

	private static double radConst = 10.0;



	// Constructors
	public Dot() {
		this(new Point(100,100), Color.RED);
	}

	public Dot ( Point center, Color borderColor ){
		super( center, radConst, borderColor.darker());
		fillColor = borderColor.brighter();
	}

	public Dot ( Dot d ){
		super(d);
		fillColor = d.fillColor;
	}


	// getter and setter methods

	public Color getFillColor() {
		return fillColor;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public static double getRadConst() {
		return radConst;
	}
	
	public static void setRadConst(double radConst) {
		Dot.radConst = radConst;
	}
	


	/// other Methods




	// abstract Methods
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


		rad = this.getRadius();
		circle = new Ellipse2D.Double(this.getCenter().getX() - rad, this.getCenter().getY() - rad, 2 * rad, 2 * rad);

		g.setColor(this.fillColor);
		g.fill(circle);
	}


	@Override
	public int getType() {
		return 1;
	}

}
