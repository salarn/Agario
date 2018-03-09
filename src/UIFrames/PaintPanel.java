package UIFrames;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import Buffers.FrameCommandBuff;
import Buffers.StatusClientBuff;
import Client.Client;
import Shapes.Bubble;
import Shapes.Circle;
import Shapes.CirclePower;
import Shapes.Dot;
import Shapes.Gear;
import Shapes.Point;
import Shapes.PowerUD;

public class PaintPanel extends JPanel {

	private ClientFrame mainFrame = null;
	private ClientPlayerFrame mainFrame2 = null;
	private Client client;
	private FrameCommandBuff frameComm;
	private StatusClientBuff statusBuff;
	private List <Circle> statusLst = new ArrayList<>();
	private Thread builder;

	private boolean isKeyBoard = false;

	private static int powerConstSec = 8 * 1000;

	private int     speedyCnt   = 0;
	private boolean isSpeedy    = false;
	private Timer   speedyTimer ;

	private int     godMoCnt   = 0;
	private boolean isgodMo    = false;
	private Timer   godMoTimer ;

	private int     fastJoinCnt   = 0;
	private boolean isFastJoin    = false;
	private Timer   fastJoinTimer ;

	private static int splitJoinConstSec = 4 * 1000;

	private boolean isSplit = false;
	private Timer   splitTimer ;

	private boolean isJoin    = false;
	private Timer   joinTimer;

	private Point center;
	private double gameW;
	private double gameH;
	private String username;


	PlayerMouseListener    playerMouse    = null;
	PlayerKeyBoardListener playerKeyBoard = null;

	public PaintPanel( ClientFrame frame, Client client, FrameCommandBuff frameComm, StatusClientBuff statusBuff, boolean isKeyboard) {

		this.client     = client;
		this.frameComm  = frameComm;
		this.statusBuff = statusBuff;
		this.isKeyBoard = isKeyboard;
		this.mainFrame  = frame;

		//		setFocusable(true);
		//		requestFocusInWindow();
		//		addKeyListener(new listner());
		//		
		addListeners();
		initialize();
	}

	public PaintPanel( ClientPlayerFrame frame, Client client, FrameCommandBuff frameComm, StatusClientBuff statusBuff, boolean isKeyboard) {

		this.client     = client;
		this.frameComm  = frameComm;
		this.statusBuff = statusBuff;
		this.isKeyBoard = isKeyboard;
		this.mainFrame2  = frame;

		//		setFocusable(true);
		//		requestFocusInWindow();
		//		addKeyListener(new listner());
		//		
		addListeners();
		initialize();
	}



	// Rendering
	public void initialize () {
		builder = new Thread( new Runnable() {
			Timer sleepTime = new Timer(60, null);
			boolean deadC = false;
			@Override
			public void run() {
				sleepTime.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						client.send("Status");
						List <String> lst = statusBuff.pick();
						if ( lst.get(0).compareTo("dead") == 0 ){
							sleepTime.stop();
							deadC = true;
							try {
								GlobalScreen.unregisterNativeHook();
							} catch (NativeHookException e) {
								//e.printStackTrace();
							}
						}
						updateStatusLst(lst);
						PaintPanel.this.repaint();
						if ( deadC ){
							//new GoodByeFrame();
							//PaintPanel.this.mainFrame.setVisible(false);
							if ( PaintPanel.this.mainFrame != null )
								PaintPanel.this.mainFrame.setVisible(false);
							else
								PaintPanel.this.mainFrame2.setVisible(false);
						}
					}
				});
				sleepTime.start();
			}
		});
		builder.start();
	}

	public synchronized void updateStatusLst ( List <String> lst) {
		statusLst.clear();
		if ( lst.size() > 1 ){
			username = lst.get(0);

			try {
				String [] s = lst.get(1).split(",");
				gameW = Double.parseDouble(s[0]);
				gameH = Double.parseDouble(s[1]);
			} catch (Exception e) {
				System.err.println("Game Size error!");
			}

			for ( int i = 2 ; i < lst.size(); ++i ){
				String s = lst.get(i);
				String [] quer = s.split(",");
				if ( quer[0].compareTo("Player") == 0 ){
					boolean temp = false;
					if ( quer[1].compareTo(username) == 0 ){
						center = new Point(0, 0);
						temp = true;
					}
					playerProc(s, temp);
				}
				else if ( quer[0].compareTo("Dot") == 0 )
					dotProc(s);
				else if ( quer[0].compareTo("Power") == 0 )
					powerProc(s);
				else if ( quer[0].compareTo("Gear") == 0 )
					gearProc(s);
				else if ( quer[0].compareTo("PowerObject") == 0 )
					powerObjectProc(s);
			}
		} else {
			client.send("Command,end");
		}
	}

	public void playerProc ( String s, boolean tmp ){
		String [] quer = s.split(",");

		String name = quer[2];
		String [] names = name.split("-");
		name = new String(names[0]);
		for ( int i = 1 ; i < names.length; i += 2 ){
			try {
				int temp = Integer.parseInt(names[i+1]);
				if ( temp == 1 || (temp == 0 && username.compareTo(quer[1]) == 0 ) )
					name = new String( name + "-" + names[i] );
			} catch (Exception e) {
				System.err.println("Power Object Player Stat error");
			}
		}
		
		Color borderColor = stringToColor(quer[3]);
		Color fillColor   = stringToColor(quer[4]);
		BufferedImage img = null;
		if ( quer[5] != null && quer[5].compareTo("null") != 0 ){
			File in = new File(quer[5]);
			try {
				img = ImageIO.read(in);

			} catch (IOException e) {
				//e.printStackTrace();
			}
		}
		if ( tmp ) {
			try {
				speedyCnt   = Integer.parseInt(quer[6]);
				godMoCnt    = Integer.parseInt(quer[7]);
				fastJoinCnt = Integer.parseInt(quer[8]);
				center = new Point(0,0);
			} catch (Exception e) {
				System.err.println("Player Exception");
				//e.printStackTrace();
			}
		}
		try {
			double y = Double.parseDouble(quer[9]);
			if ( tmp )
				center.setY(y);
			int size  = Integer.parseInt(quer[10]);
			int head = 11;
			for ( int i = 0 ; i < size; ++i ){
				double x   = Double.parseDouble(quer[head++]);
				double rad = Double.parseDouble(quer[head++]);

				if ( tmp )
					center.setX( center.getX() + x );

				statusLst.add( new Bubble(new Point(x, y), rad, borderColor, name, fillColor, img) );
			}
			if ( tmp )
				center.setX(center.getX() / (double)(size));
		} catch (Exception e) {
			System.err.println("Player Exception");
			///e.printStackTrace();
		}
	}

	public void dotProc ( String s ){
		String [] quer = s.split(",");

		try {
			Color borderColor = stringToColor(quer[1]);
			Color fillColor   = stringToColor(quer[2]);
			double rad = Double.parseDouble(quer[3]);
			double x   = Double.parseDouble(quer[4]);
			double y   = Double.parseDouble(quer[5]);
			statusLst.add(new Dot(new Point(x, y), borderColor.brighter()));
		} catch (Exception e) {
			System.err.println("Dot error");
		}
	}

	public void powerProc ( String s ){
		String [] quer = s.split(",");
		try {
			Color borderColor = stringToColor(quer[1]).brighter();
			int type = Integer.parseInt(quer[3]);
			double x = Double.parseDouble(quer[5]);
			double y = Double.parseDouble(quer[6]);
			statusLst.add(new PowerUD(new Point(x, y), borderColor, type));
		} catch (Exception e) {
			System.err.println("PowerUD error");
		}
	}
	
	public void powerObjectProc ( String s ) {
		String [] quer = s.split(",");
		try {
			
			String name = quer[1];
			Color borderColor = stringToColor(quer[2]);
			Color fillColor   = stringToColor(quer[3]);
			double radius = Double.parseDouble(quer[4]);
			double x = Double.parseDouble(quer[5]);
			double y = Double.parseDouble(quer[6]);
			Point center = new Point(x, y);

			statusLst.add(new CirclePower(name, borderColor, fillColor, radius, center) );
			
		} catch (Exception e) {
			System.err.println("Power Object error");
		}
	}

	public void gearProc ( String s ){
		String [] quer = s.split(",");
		try {
			Color borderColor = stringToColor(quer[1]).brighter();
			double radius = Double.parseDouble(quer[3]);
			double x = Double.parseDouble(quer[4]);
			double y = Double.parseDouble(quer[5]);
			statusLst.add(new Gear(new Point(x, y), radius, borderColor));
		} catch (Exception e) {
			System.err.println("Gear error");
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		synchronized (this){

			Render(g);
		}
	}

	public void Render ( Graphics g ) {
		if (center == null)
			return;
		double width = this.getWidth(), height = this.getHeight();
		g.setColor(Color.BLACK);
		for ( int i = 0 ; i <= gameW; i += 100 ){
			int len = 1;
			if (i == 0 || i == gameW)
				len = 5;
			g.fillRect((int)(width/3.0 - center.getX() + i),(int) (height/2.0 - center.getY() + 0), len, (int)gameH);
		}

		for ( int i = 0 ; i <= gameH; i += 100 ){
			int len = 1;
			if (i == 0 || i == gameW)
				len = 5;
			g.fillRect((int)(width/3.0 - center.getX() + 0), (int)( height/2.0 - center.getY() + i), (int) gameW, len);
		}
		for ( Circle cir : statusLst ){
			cir.setCenter(new Point(width/3.0 - center.getX() + cir.getCenter().getX(),
					height/2.0 - center.getY() + cir.getCenter().getY()));
			cir.Render(g);
		}

		Graphics2D g2 = (Graphics2D) g;
		double scale = 1 ;
		g2.setColor(Color.BLACK);
		g2.scale(scale, scale);
		g2.drawString("speedy: " +speedyCnt, (int) (40.0/scale), 
				(int) (40.0/scale));
		g2.scale(1.0/scale, 1.0/scale);

		g2.setColor(Color.BLACK);
		g2.scale(scale, scale);
		g2.drawString("godMo: " +godMoCnt, (int) (40.0/scale), 
				(int) (60.0/scale));
		g2.scale(1.0/scale, 1.0/scale);

		g2.setColor(Color.BLACK);
		g2.scale(scale, scale);
		g2.drawString("fastJoin: " +fastJoinCnt, (int) (40.0/scale), 
				(int) (80.0/scale));
		g2.scale(1.0/scale, 1.0/scale);

	}









	public void addListeners(){

		if ( !isKeyBoard ){
			setFocusable(true);
			requestFocusInWindow();
			playerMouse = new PlayerMouseListener();
			addMouseListener(playerMouse);
			addMouseMotionListener(playerMouse);
			try {
				GlobalScreen.registerNativeHook();
			}
			catch (NativeHookException ex) {
				//				System.err.println("There was a problem0 registering the native hook.");
				//				System.err.println(ex.getMessage());

				// System.exit(1);
			}

			playerKeyBoard = new PlayerKeyBoardListener();
			GlobalScreen.addNativeKeyListener(playerMouse);
		}
		else {
			try {
				GlobalScreen.registerNativeHook();
			}
			catch (NativeHookException ex) {
				//				System.err.println("There was a problem0 registering the native hook.");
				//				System.err.println(ex.getMessage());

				// System.exit(1);
			}

			playerKeyBoard = new PlayerKeyBoardListener();
			GlobalScreen.addNativeKeyListener(playerKeyBoard);
		}
	}

	class listner implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
			System.out.println("key pressed");
		}

		@Override
		public void keyReleased(KeyEvent arg0) {

		}

		@Override	
		public void keyTyped(KeyEvent arg0) {

		}

	}


	class PlayerMouseListener implements  MouseMotionListener, MouseListener, NativeKeyListener {


		@Override
		public void mouseClicked(MouseEvent arg0) {

		}

		@Override
		public void mouseEntered(MouseEvent arg0) {

		}

		@Override
		public void mouseExited(MouseEvent arg0) {

		}

		@Override
		public void mousePressed(MouseEvent e) {
			if ( center == null )
				return;
			double width = PaintPanel.this.getWidth(), height = PaintPanel.this.getHeight();
			Point pnt = new Point(new Point(width/3.0, height/2.0));
			Point cur = new Point( e.getPoint() );

			cur = cur.getDifference(pnt);
			cur = cur.getScaller(1.0/cur.getRad());

			client.send("Command,move," + cur.getX() + "," + cur.getY());
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {

		}

		@Override
		public void mouseDragged(MouseEvent arg0) {

		}

		@Override
		public void mouseMoved(MouseEvent arg0) {

		}

		@Override
		public void nativeKeyPressed(NativeKeyEvent e) {

			if ( e.getKeyCode() == NativeKeyEvent.VC_KP_1 && !isSplit ){
				client.send("Command,split");

				isSplit = true;
				splitTimer = new Timer(splitJoinConstSec, null );;
				splitTimer.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						isSplit = false;
						splitTimer.stop();
					}
				});
				splitTimer.start();
			}

			if ( e.getKeyCode() == NativeKeyEvent.VC_KP_2 && !isJoin ){
				client.send("Command,join");



				isJoin = true;
				joinTimer = new Timer(splitJoinConstSec, null );;
				joinTimer.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						isJoin = false;
						joinTimer.stop();
					}
				});
				joinTimer.start();
			}

			if ( e.getKeyCode() == NativeKeyEvent.VC_KP_4 && !isSpeedy && speedyCnt > 0 ){
				client.send("Command,speedy");

				--speedyCnt;
				isSpeedy = true;
				speedyTimer = new Timer(powerConstSec, null );;
				speedyTimer.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						isSpeedy = false;
						speedyTimer.stop();
					}
				});
				speedyTimer.start();
			}

			if ( e.getKeyCode() == NativeKeyEvent.VC_KP_5 && !isgodMo && godMoCnt > 0 ){
				client.send("Command,godmo");

				--godMoCnt;
				isgodMo = true;
				godMoTimer = new Timer(powerConstSec, null );;
				godMoTimer.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						isgodMo = false;
						godMoTimer.stop();
					}
				});
				godMoTimer.start();
			}

			if ( e.getKeyCode() == NativeKeyEvent.VC_KP_6 && !isFastJoin && fastJoinCnt > 0 ){
				client.send("Command,fastjoin");

				--fastJoinCnt;
				isFastJoin = true;
				fastJoinTimer = new Timer(powerConstSec, null );;
				fastJoinTimer.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						isFastJoin = false;
						fastJoinTimer.stop();
					}
				});
				fastJoinTimer.start();
			}

		}

		@Override
		public void nativeKeyReleased(NativeKeyEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void nativeKeyTyped(NativeKeyEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

	class PlayerKeyBoardListener implements NativeKeyListener {

		@Override
		public void nativeKeyPressed(NativeKeyEvent e) {
			System.out.println("Key Pressed: " + NativeKeyEvent.getKeyText(e.getKeyCode()));
			if ( e.getKeyCode() == NativeKeyEvent.VC_RIGHT){
				client.send("Command,move,1,0");
			}
			if ( e.getKeyCode() == NativeKeyEvent.VC_LEFT){
				client.send("Command,move,-1,0");
			}
			if ( e.getKeyCode() == NativeKeyEvent.VC_DOWN){
				client.send("Command,move,0,1");
			}
			if ( e.getKeyCode() == NativeKeyEvent.VC_UP){
				client.send("Command,move,0,-1");
			}

			if ( e.getKeyCode() == NativeKeyEvent.VC_S && !isSplit ){
				client.send("Command,split");

				isSplit = true;
				splitTimer = new Timer(splitJoinConstSec, null );;
				splitTimer.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						isSplit = false;
						splitTimer.stop();
					}
				});
				splitTimer.start();
			}

			if ( e.getKeyCode() == NativeKeyEvent.VC_J && !isJoin ){
				client.send("Command,join");

				isJoin = true;
				joinTimer = new Timer(splitJoinConstSec, null );;
				joinTimer.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						isJoin = false;
						joinTimer.stop();
					}
				});
				joinTimer.start();
			}

			if ( e.getKeyCode() == NativeKeyEvent.VC_Q && !isSpeedy && speedyCnt > 0 ){
				client.send("Command,speedy");

				--speedyCnt;
				isSpeedy = true;
				speedyTimer = new Timer(powerConstSec, null );
				speedyTimer.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						isSpeedy = false;
						speedyTimer.stop();
					}
				});
				speedyTimer.start();
			}

			if ( e.getKeyCode() == NativeKeyEvent.VC_W && !isgodMo && godMoCnt > 0 ){
				client.send("Command,godmo");

				--godMoCnt;
				isgodMo = true;
				godMoTimer = new Timer(powerConstSec, null );;
				godMoTimer.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						isgodMo = false;
						godMoTimer.stop();
					}
				});
				godMoTimer.start();
			}

			if ( e.getKeyCode() == NativeKeyEvent.VC_E && !isFastJoin && fastJoinCnt > 0 ){
				client.send("Command,fastjoin");

				--fastJoinCnt;
				isFastJoin = true;
				fastJoinTimer = new Timer(powerConstSec, null );;
				fastJoinTimer.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						isFastJoin = false;
						fastJoinTimer.stop();
					}
				});
				fastJoinTimer.start();
			}
		}

		@Override
		public void nativeKeyReleased(NativeKeyEvent arg0) {

		}

		@Override
		public void nativeKeyTyped(NativeKeyEvent arg0) {

		}

	}





	// Converting Color

	public int hexNumToIntNum ( char x ){
		if ( x >= '0' && x <= '9' )
			return (int) (x - '0');
		else
			return (int) (x - 'a' + 10);
	}

	public char numIntToNumHex ( int x ){
		if ( x >= 0 && x <= 9 )
			return (char) ( x + '0' );
		else
			return (char) ( x - 10 + 'a' );
	}

	public Color stringToColor ( String s ) {
		if ( s.length() != 6 )
			return new Color(255, 255, 255) ;
		int firstVal  = hexNumToIntNum(s.charAt(0)) * 16 + hexNumToIntNum(s.charAt(1));
		int secondVal = hexNumToIntNum(s.charAt(2)) * 16 + hexNumToIntNum(s.charAt(3));
		int thirdVal  = hexNumToIntNum(s.charAt(4)) * 16 + hexNumToIntNum(s.charAt(5));
		return new Color(firstVal, secondVal, thirdVal) ;

	}

	public String colorToString ( Color c ){
		return ( numIntToNumHex(c.getRed() / 16) + "" + numIntToNumHex(c.getRed() % 16 ) + "" +
				numIntToNumHex(c.getGreen() / 16) + "" + numIntToNumHex(c.getGreen() % 16 ) + "" +
				numIntToNumHex(c.getBlue() / 16) + "" + numIntToNumHex(c.getBlue() % 16 ) );
	}



}
