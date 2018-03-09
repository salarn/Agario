package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Timer;

import Shapes.Bubble;
import Shapes.Bubbles;
import Shapes.Circle;
import Shapes.Point;

public class PlayerGame {	



	private String  userName;
	private String  name;
	private Bubbles bubs;
	private String  imgName;

	private String powerStat = "";

	private Object mnOb = new Object(); 
	
	protected static double speedConst  = 500.0; // :)) // TODO fix
	protected static double speedyCosnt = 1000.0;  //  TODO fix

	protected static double startRad    = Bubble.getMinRadConst(); // :)) TODO fix


	/// Power Up-Down--s
	private int speedyP    = 0;
	private int godMoP     = 0;
	private int fastJoinP  = 0;


	// actions about power Up-Down--s
	private boolean speedy = false;
	private boolean godMo  = false;  /// TODO fix it for bubble and rendering them when it's true --> solution can be this adding isGodmo to bubble class



	// Constructor-s
	public PlayerGame( String userName, String name, Color borderColor, Color fillColor, String imgName, Point start) {
		this.userName = userName;
		this.name     = name;
		this.imgName  = imgName;

		BufferedImage img = null;
		if ( imgName != null && imgName.compareTo("null") == 0 ){

			File in = new File(imgName);
			try {
				img = ImageIO.read(in);

			} catch (IOException e) {
				//e.printStackTrace();
			}
		}

		this.bubs = new Bubbles( new Bubble(start, startRad, borderColor, name, fillColor, img) );
	}

	//	public PlayerGame( String userName, String name, Bubbles bubs ) {
	//		this.userName = userName;
	//		this.name     = name;
	//		this.bubs     = bubs;
	//	}





	// getter and setter Methods

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Bubbles getBubs() {
		return bubs;
	}

	public void setBubs(Bubbles bubs) {
		this.bubs = bubs;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public static double getSpeedConst() {
		return speedConst;
	}

	public static void setSpeedConst(double speedConst) {
		PlayerGame.speedConst = speedConst;
	}

	public static double getSpeedyCosnt() {
		return speedyCosnt;
	}

	public static void setSpeedyCosnt(double speedyCosnt) {
		PlayerGame.speedyCosnt = speedyCosnt;
	}

	public static double getStartRad() {
		return startRad;
	}

	public static void setStartRad(double startRad) {
		PlayerGame.startRad = startRad;
	}

	public int getSpeedyP() {
		return speedyP;
	}

	public void setSpeedyP(int speedyP) {
		this.speedyP = speedyP;
	}

	public int getGodMoP() {
		return godMoP;
	}

	public void setGodMoP(int godMoP) {
		this.godMoP = godMoP;
	}

	public int getFastJoinP() {
		return fastJoinP;
	}

	public void setFastJoinP(int fastJoinP) {
		this.fastJoinP = fastJoinP;
	}

	public boolean isSpeedy() {
		return speedy;
	}

	public void setSpeedy(boolean speedy) {
		this.speedy = speedy;
	}

	public boolean isGodMo() {
		return godMo;
	}

	public void setGodMo(boolean godMo) {
		this.godMo = godMo;
	}

	public String getImgName() {
		return imgName;
	}

	public void setImgName(String imgName) {
		this.imgName = imgName;
	}

	public String getPowerStat() {
		return powerStat;
	}
	
	public void setPowerStat(String powerStat) {
		this.powerStat = powerStat;
	}
	
	public Object getMnOb() {
		return mnOb;
	}
	
	public void setMnOb(Object mnOb) {
		this.mnOb = mnOb;
	}
	


	//// other Methods



	public void move ( Point dir, Point limits ) {
		Bubble bub = null;
		for ( Bubble b : bubs.getBubLst() ){
			if ( bub == null )
				bub = b;
			else if ( bub.getRadius() < b.getRadius() )
				bub = b;
		}
		double area  = bub.getRadius();
		double speed = ( speedy ? speedyCosnt : speedConst ) / area;
		bubs.move(dir, speed, limits);
	}

	public void joinBubble () {
		bubs.join();
	}

	public void splitBubble () {
		bubs.split();
	}

	public void speedyPowerUp () {
		if ( speedyP == 0 )
			return;
		--speedyP;
		speedy = true;

		Timer timer = new Timer(30*1000, null);
		timer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				speedy = false;
				timer.stop();
			}
		});
		timer.start();
	}

	public void godMoPowerUp () {
		if ( godMoP == 0 )
			return;
		--godMoP;
		godMo = true;

		Timer timer = new Timer(30*1000, null);
		timer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				godMo = false;
				timer.stop();
			}
		});
		timer.start();
	}

	public void fastJoinPowerUp () {
		if ( fastJoinP == 0 )
			return;
		--fastJoinP;
		bubs.fastJoin();
	}

	public void destroyerPowerDown () {
		this.speedyP   = 0;
		this.godMoP    = 0;
		this.fastJoinP = 0;
	}

	public void halfererPowerDown () {
		bubs.halfingAll();
	}

	public void eat ( Bubble bub, Circle c ){
		if ( (bubs.getBubLst()).contains(bub) == false)
			return;
		bub.eat(c);
		bubs.normalize();
		int type = c.getType();
		switch (type)  {
		case 0:
		case 1:
		case 2:
			break;
		case 3:
			speedyP++;
			break;
		case 4:
			godMoP++;
			break;
		case 5:
			fastJoinP++;
			break;
		case 6:
			destroyerPowerDown();
			break;
		case 7:
			halfererPowerDown();
			break;
		case 8:
			// TODO this = c.changePlayer( this );
			break;
		}
	}

	public void bubbleDie ( Bubble bub ) {
		if ( (bubs.getBubLst()).contains(bub) == false )
			return ;
		bub.setDead(true);
	}

	public void gearPassingBubble ( Bubble bub ){
		if ( bubs.getBubLst().contains(bub) == false )
			return;
		bubs.gearPassing(bub);
	}

	public void Render ( Graphics G ){
		//		if ( godMo )  // TODO
		bubs.Render(G);
	}



}
