package ImgChooser;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import Shapes.PaintCircle;
import Shapes.Point;

public class ImgChooserPanel extends JPanel {
	
	
	private BufferedImage img = null;
	private String        imgName = null;
	
	private ImgChooserPanelListenner imgChooserPanelListenner;
	
	private List <PaintCircle> picShape  = new ArrayList<>();
	
	private Point prevMouse = null;
	private PaintCircle curShape = null;
	
	
	
	public ImgChooserPanel ( ){ 
		super();
		
		Dimension dim = getPreferredSize();
		dim.width = 12050 ;
		setPreferredSize(dim);

		
		//// setting borders
		Border inner = BorderFactory.createTitledBorder("Image Chooser!");
		Border outer = BorderFactory.createEmptyBorder();
		setBorder(BorderFactory.createCompoundBorder(inner, outer));
		
		
		addPics();
		
		imgChooserPanelListenner = new ImgChooserPanelListenner();
		addMouseListener(imgChooserPanelListenner);
		addMouseMotionListener(imgChooserPanelListenner);
		
	}
	
	
	
	//// getter and setter Methods
	public BufferedImage getImg() {
		return img;
	}

	public void setImg(BufferedImage img) {
		this.img = img;
	}
	
	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	
	public void setSelectedImg ( String name ){
		if ( name == null )
			return;
		for ( PaintCircle cir : picShape )
			if ( name.compareTo(cir.getName()) == 0 ){
				if ( curShape != null ){
					curShape.setSelected(false);
					curShape = null;
				}
				curShape = cir;
				cir.setSelected(true);
				img      = curShape.getImg();
				imgName  = curShape.getName();
			}
	}


	@Override
	protected void paintComponent(Graphics G) {
		super.paintComponent(G);
		Render(G);
	}
	
	



	public void Render ( Graphics G ) {
		for ( PaintCircle shape : picShape )
			shape.Render(G);
	}


	class ImgChooserPanelListenner implements MouseListener, MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			prevMouse = new Point( e.getPoint() );
			
			if ( curShape != null ){
				curShape.setSelected(false);
				img = null;
				imgName = null;
			}
			curShape = null;
			for ( PaintCircle shape : picShape ){
				if ( shape.isIn(prevMouse) ){
					curShape = shape;
				}
			}
			
			if ( curShape != null ){
				curShape.setSelected(true);
				img = curShape.getImg();
				imgName = curShape.getName();
			}
			
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	public void addPics () {
		BufferedImage img = null;
		
		File in = new File("10.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(150,150), 100, Color.WHITE, 0, img, "10.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("12389123.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(400,150), 100, Color.WHITE, 0, img, "12389123.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("2ch.hk.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(650,150), 100, Color.WHITE, 0, img, "2ch.hk.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("2wzkcbt.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(900,150), 100, Color.WHITE, 0, img, "2wzkcbt.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("4chan.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(1150,150), 100, Color.WHITE, 0, img, "4chan.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("8.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(1400,150), 100, Color.WHITE, 0, img, "8.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("8ch.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(1650,150), 100, Color.WHITE, 0, img, "8ch.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("9gag.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(1900,150), 100, Color.WHITE, 0, img, "9gag.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("agariopanther.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(2150,150), 100, Color.WHITE, 0, img, "agariopanther.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("agarleprachun.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(2400,150), 100, Color.WHITE, 0, img, "agarleprachun.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("army.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(2650,150), 100, Color.WHITE, 0, img, "army.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("ayy lmao.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(2900,150), 100, Color.WHITE, 0, img, "ayy lmao.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("az.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(3150,150), 100, Color.WHITE, 0, img, "az.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("ba.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(3400,150), 100, Color.WHITE, 0, img, "ba.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("bait.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(3650,150), 100, Color.WHITE, 0, img, "bait.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("banana2.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(3900,150), 100, Color.WHITE, 0, img, "banana2.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("beats.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(4150,150), 100, Color.WHITE, 0, img, "beats.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		in = new File("blackhole.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(4400,150), 100, Color.WHITE, 0, img, "blackhole.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("blatter.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(4650,150), 100, Color.WHITE, 0, img, "blatter.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("blueberry.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(4900,150), 100, Color.WHITE, 0, img, "blueberry.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("boo.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(5150,150), 100, Color.WHITE, 0, img, "boo.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("canada.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(5400,150), 100, Color.WHITE, 0, img, "canada.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("chaplin.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(5650,150), 100, Color.WHITE, 0, img, "chaplin.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("cia.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(5900,150), 100, Color.WHITE, 0, img, "cia.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("doge.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(6150,150), 100, Color.WHITE, 0, img, "doge.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("ea.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(6400,150), 100, Color.WHITE, 0, img, "ea.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		in = new File("earth.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(6650,150), 100, Color.WHITE, 0, img, "earth.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("facebook.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(6900,150), 100, Color.WHITE, 0, img, "facebook.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("facepunch.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(7150,150), 100, Color.WHITE, 0, img, "facepunch.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("feminism.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(7400,150), 100, Color.WHITE, 0, img, "feminism.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("france.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(7650,150), 100, Color.WHITE, 0, img, "france.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("germany.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(7900,150), 100, Color.WHITE, 0, img, "germany.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("iran.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(8150,150), 100, Color.WHITE, 0, img, "iran.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("italy.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(8400,150), 100, Color.WHITE, 0, img, "italy.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("mars.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(8650,150), 100, Color.WHITE, 0, img, "mars.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		in = new File("moon.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(8900,150), 100, Color.WHITE, 0, img, "moon.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("nasa.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(9150,150), 100, Color.WHITE, 0, img, "nasa.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("origin.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(9400,150), 100, Color.WHITE, 0, img, "origin.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("pokerface.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(9650,150), 100, Color.WHITE, 0, img, "pokerface.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("sanik.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(9900,150), 100, Color.WHITE, 0, img, "sanik.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("sir.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(10150,150), 100, Color.WHITE, 0, img, "sir.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("stalin.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(10400,150), 100, Color.WHITE, 0, img, "stalin.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("steam.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(10650,150), 100, Color.WHITE, 0, img, "steam.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("tumblr.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(10900,150), 100, Color.WHITE, 0, img, "tumblr.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("united kingdom.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(11150,150), 100, Color.WHITE, 0, img, "united kingdom.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("usa.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(11400,150), 100, Color.WHITE, 0, img, "usa.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("wojak.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(11650,150), 100, Color.WHITE, 0, img, "wojak.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		in = new File("yaranaika.png");
		try {
			img = ImageIO.read(in);
			picShape.add( new PaintCircle(new Point(11900,150), 100, Color.WHITE, 0, img, "yaranaika.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		repaint();
	}
}
