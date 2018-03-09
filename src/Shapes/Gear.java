package Shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Gear extends Circle {

	
	private BufferedImage img = null;
	private String imgName    = "gear.png";
	
	
	/// Constructors 
	
	public Gear () {
		this( new Point(100,100), 100, Color.GREEN );
	}
	
	public Gear ( Point center, double radius, Color borderColor) {
		super( center, radius, borderColor );
		File in = new File(imgName);
		try {
			img = ImageIO.read(in);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
//	public Gear ( Point center, double radius, Color borderColor, BufferedImage img) {
//		super( center, radius, borderColor );
//		this.img = img;
//		
//	}
	
	
	// getter and setter methods
	 
	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	
	
	
	/// abstract Methods
	
	@Override
	public void Render(Graphics G) {
		Graphics2D g = (Graphics2D) G;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Ellipse2D.Double circle;
		double rad = this.getRadius() + borderLen;
		circle = new Ellipse2D.Double(this.getCenter().getX() - rad, this.getCenter().getY() - rad, 2 * rad, 2 * rad );

//		g.setColor(getBorderColor());
//		g.fill(circle);


		if ( img == null ){
			rad = this.getRadius();
			circle = new Ellipse2D.Double(this.getCenter().getX() - rad, this.getCenter().getY() - rad, 2 * rad, 2 * rad);

			g.setColor(this.getBorderColor());
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
	}

	

	@Override
	public int getType() {
		return 2;
	}

}
