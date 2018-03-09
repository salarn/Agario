package Shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class PaintCircle {

	private Point         center;
	private double        radius;
	private Color         borderColor = Color.GRAY;
	private float         stroke      = 3;
	private BufferedImage img = null;
	private boolean       isSelected = false;
	private String        name = null;;
	
	
	public PaintCircle ( Point center, double radius, Color borderColor, BufferedImage img, String name ){
		this.img         = img;
		this.name        = name;
		this.center      = center;
		this.radius      = radius;
		this.borderColor = borderColor;
	}
	
	public PaintCircle ( Point center, double radius, Color borderColor, float stroke, BufferedImage img, String name ){
		this.img         = img;
		this.name        = name;
		this.center      = center;
		this.radius      = radius;
		this.stroke      = stroke;
		this.borderColor = borderColor;
	}
	
	
	//// setter and getter Methods
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isIn ( Point pnt ) {
		if ( this.center.getDistance(pnt) <= radius )
			return true;
		return false;
	}


	public void Render( Graphics G ){
		Graphics2D g = (Graphics2D) G;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Ellipse2D.Double circle;
		double rad = radius + stroke;
		circle = new Ellipse2D.Double(center.getX() - rad, center.getY() - rad, 2 * rad, 2 * rad );
		
		g.setColor( borderColor );
		g.fill(circle);
		
		if ( isSelected ) {
			Stroke oldStroke = g.getStroke();
			g.setStroke(new BasicStroke(10) );
			g.setColor(Color.BLUE);
			g.draw(circle);
			g.setStroke(oldStroke);
		}
		
		
		rad = radius;
		g.setClip(new Ellipse2D.Double((int) (center.getX() - rad), (int) (center.getY() - rad), (int) (2 * rad), (int) (2 * rad)));
		g.drawImage(img, (int) (center.getX() - rad), (int) (center.getY() - rad), (int) (2 * rad), (int) (2 * rad), null);
		g.setClip(null);
		
		
	}
	
}
