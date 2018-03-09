package UIFrames;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Shapes.PaintCircle;
import Shapes.Point;

public class GoodByePanel extends JPanel {

	private PaintCircle [] lst = new PaintCircle [10];
	private BufferedImage img;
	
	public GoodByePanel() {
		
		File in = new File("2wzkcbt.png");
		try {
			img = ImageIO.read(in);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		lst[0] = new PaintCircle(new Point(200, 200), 50, Color.YELLOW, img, "2wzkcbt.png");
		
		in = new File("iran.png");
		try {
			img = ImageIO.read(in);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		lst[1] = new PaintCircle(new Point(600, 200), 120, Color.RED, img, "iran.png");
		
		in = new File("earth.png");
		try {
			img = ImageIO.read(in);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		lst[2] = new PaintCircle(new Point(500, 450), 90, Color.BLUE, img, "earth.png");
		
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.BLACK);
		for ( int i = 0 ; i <= getWidth(); i += 100 )
			g.fillRect(i, 0, 1, getHeight());
		for ( int i = 0 ; i <= getHeight(); i += 100 )
			g.fillRect(0, i, getWidth(), 1);
		
		for ( int i = 0 ; i < lst.length; ++i )
			if ( lst[i] != null )
				lst[i].Render(g);
		
		Graphics2D gg = (Graphics2D) g;
		double scale = 5 ;
		gg.setColor(Color.BLACK);
		gg.scale(scale, scale);
		gg.drawString("Thanks 4 Playing!", (int) (150.0/scale),(int) (300.0/scale));
		gg.drawString("Developed by ali.gtw", (int) (150.0/scale),(int) (420.0/scale));
		gg.scale(1.0/scale, 1.0/scale);
		
	}
	
	
	
}
