package UIFrames;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import Shapes.PaintCircle;
import Shapes.Point;

public class GameDemoPanel extends JPanel {
	public GameDemoPanel () {
		
		//// setting preferredSize 
		Dimension dim = getPreferredSize();
		dim.height = 150;
		setPreferredSize(dim);
		
	}

	
	
	/// Painting JPanel
	@Override
	protected void paintComponent(Graphics G) {
		super.paintComponent(G);
		
		Graphics2D g = (Graphics2D) G;
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		File in = new File("agarBack.png");
		BufferedImage image = null;
		try {
			image = ImageIO.read(in);
		} catch (IOException e) {
			//System.out.println("Fuck");
			e.printStackTrace();
		}
		g.drawImage(image, 0, 0, getWidth(),getHeight(),null);
		
		/*
		iranCir.Render(G);
		usaCir.Render(G);
		marsCir.Render(G);
		moonCir.Render(G);
		
		g.setColor( Color.RED );
		g.scale(5, 5);
		g.drawString("Agar.IO!", 15 , 18 );
		g.scale(1.0/5, 1.0/5);
		*/
		
	}
	
	
	
}
