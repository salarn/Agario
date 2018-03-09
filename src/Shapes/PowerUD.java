package Shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class PowerUD extends Circle {


	private Color          fillColor;
	private BufferedImage  img = null;
	private int type; // [3,7]
	
	private boolean        isDead = false;

	private static double radConst = 15.0;

	/// Constructors

	public PowerUD() {
		this( new Point(100,100), Color.RED, 0);
	}

	public PowerUD ( Point center, Color borderColor, int type ){
		super( center,  radConst, borderColor.darker() );
		this.type = type;
		this.fillColor = borderColor.brighter();
		setImgType();
	}


	// getter and setter Methods
	
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
	
	public static double getRadConst() {
		return radConst;
	}
	
	public static void setRadConst(double radConst) {
		PowerUD.radConst = radConst;
	}
	
	
	


	/// other methods


	public void setImgType() {
		//TODO
		return;
	}




	///// abstract Methods


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
			
			Color writeColor = Color.BLACK;
			String name = null;
			switch (type) {
			case 3:
				name = new String("Speedy");
				writeColor = Color.GREEN;
				break;
			case 4:
				name = new String("GodMo");
				writeColor = Color.GREEN;
				break;
			case 5:
				name = new String("fastJoin");
				writeColor = Color.GREEN;
				break;
			case 6:
				name = new String("Destroyer");
				writeColor = Color.RED;
				break;
			case 7:
				name = new String("Halferer");
				writeColor = Color.RED;
				break;
			}
			
			
			double scale = (this.getRadius() / (20.0) ) ;
			g.setColor(writeColor);
			g.scale(scale, scale);
			g.drawString(name, (int) (this.getCenter().getX()/scale - this.getRadius()/scale ), 
					(int) (this.getCenter().getY()/scale));
			g.scale(1.0/scale, 1.0/scale);
		}


	}

	@Override
	public int getType() {
		return type;
	}

}
