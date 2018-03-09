package Shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;

public class CirclePower extends Circle {


	String name;
	Color fillColor;

	double borderLen = 3;

	public CirclePower( String name, Color borderColor, Color fillColor, double radius, Point center) {
		super( center, radius, borderColor );
		this.name = name;
		this.fillColor = fillColor;
	}
	

	
	
	// other Methods

	@Override
	public void Render(Graphics G) {
		Graphics2D g = (Graphics2D) G;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Ellipse2D.Double circle;
		double rad = getRadius() + borderLen;
		circle = new Ellipse2D.Double(getCenter().getX() - rad, getCenter().getY() - rad, 2 * rad, 2 * rad );

		g.setColor(getBorderColor());
		g.fill(circle);


		rad = getRadius();
		circle = new Ellipse2D.Double(getCenter().getX() - rad, getCenter().getY() - rad, 2 * rad, 2 * rad);

		g.setColor(this.fillColor);
		g.fill(circle);


		{ // display name
			//TODO
			double scale = (getRadius() / (20.0) ) ;
			g.setColor(Color.BLACK);
			g.scale(scale, scale);
			g.drawString(this.name, (int) (getCenter().getX()/scale - getRadius()/scale ), 
					(int) (getCenter().getY()/scale));
			g.scale(1.0/scale, 1.0/scale);
		}
	}




	@Override
	public int getType() {
		return -1;
	}
}
