package Game;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.Timer;

import Buffers.GameCommandBuffer;
import Buffers.GameStatusInBuffer;
import Buffers.GameStatusOutBuffer;
import Shapes.Bubble;
import Shapes.Circle;
import Shapes.Dot;
import Shapes.Gear;
import Shapes.Point;
import Shapes.PowerUD;

public class Game {

	private double width;
	private double height;

	private List <Dot>               dots          = new ArrayList<>();
	private List <Gear>              gears         = new ArrayList<>();
	private List <PowerUD>           powers        = new ArrayList<>(); 
	private List <PlayerGame>        players       = new ArrayList<>();
	private List <PlayerThreadGame>  playersThread = new ArrayList<>();
	 
	
	// newly power up and down added from server
	private List <Class>  powerClass = new ArrayList<>();
	private List <Object> powerObj   = new ArrayList<>();

	
	
	private List <String> statusList = new ArrayList<>();
	private boolean change = false;


	private  int maxDotCnt  ;
	private  int maxPowerCnt;
	private  int maxGearCnt ;

	private  double gearRad ; 


	// Constructor
	public Game() {
		this ( 1000, 1000 );
	}

	public Game ( double width, double height ) {
		
		this(width, height, 20, 4, 6, 50);
		
	}
	
	public Game ( double width, double height, int mxDot, int mxPower, int mxGear, double gearRad ){
		
		
		this.width  = width;
		this.height = height;
		
		this.maxDotCnt   = mxDot;
		this.maxPowerCnt = mxPower;
		this.maxGearCnt  = mxGear;
		this.gearRad     = gearRad;

		addDot();
		addPower();
		setGears();
	}


	// getter and setter methods

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}




	/// other methods

	public synchronized void addPlayer ( String userName, String name, Color borderColor, Color fillColor, String imgName,
			GameStatusInBuffer statusIn, GameStatusOutBuffer statusOut, GameCommandBuffer command){

		Point start = pointGenerator(PlayerGame.getStartRad() + Circle.getBorderLen());
		PlayerGame       player       = new PlayerGame(userName, name, borderColor, fillColor, imgName, start);
		PlayerThreadGame playerThread = new PlayerThreadGame(userName, this, statusIn, statusOut, command);

		players.add(player);
		playersThread.add(playerThread);

		playerThread.startPlayer();
		change = true;

	}

	public synchronized void removePlayer ( String userName ){
		PlayerGame player = getPlayerUserName(userName);
//		for ( PlayerGame p : players )
//			if ( p.getUserName().compareTo(userName) == 0 ){
//				player = p;
//				break;
//			}
		if ( player == null ) 
			return;
		int ind = players.indexOf(player);
		if ( playersThread.size() < ind )
			return;
		PlayerThreadGame playerThread = playersThread.get(ind);
		if ( player.getUserName().compareTo(playerThread.getUserName()) != 0 )
			return;

		players.remove(ind);
		playersThread.remove(ind);
		playerThread.endPlayer();
		change = true;
	}

	public void addDot () {
		while ( dots.size() < maxDotCnt ){
			Point center = pointGenerator(Dot.getRadConst() + Circle.getBorderLen());
			Color color  = colorGenerator(); 
			Dot dot = new Dot(center, color);
			dots.add(dot);
		}
	}

	public void addPower (){
		if ( powers.size() >= maxPowerCnt )
			return;
		Point center = pointGenerator(PowerUD.getRadConst() + Circle.getBorderLen());
		Color color  = colorGenerator();
		int type = 100;
		while ( type > 7 || type < 3 ){
			type = (int) ( Math.random() * 10 ) - 2;
		}
		PowerUD power = new PowerUD(center, color, type);
		powers.add(power);
	}
	
	public void addPowerClass () {
		if ( powerObj.size() >= maxPowerCnt || powerClass.size() == 0 ) 
			return;
		
		int ty = powerClass.size() - 1;//TODO radom ty az beine powerClass.size
		if ( ty < 0 )
			return;
//		if ( ty == powerClass.size() )
//			return;
		
		try {
			Class cls = powerClass.get(ty);
			Field field = cls.getDeclaredField("radius");
			double rad = (Double) ( field.get(null) );
			Point center = pointGenerator(rad);
			
			Constructor cns = cls.getConstructor(new Class[] { Point.class } );
			Object obj = cns.newInstance(center);
			powerObj.add(obj);
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
	}

	public void setGears (){
//		return; //TODO
		Gear gear;
		Point center;
		
		while (gears.size() < maxGearCnt ){
			center = pointGenerator(gearRad);
			gear = new Gear( center, gearRad, Color.GREEN );
			gears.add(gear);
			
		}
		
		//TODO

//		// TOP LEFT
//		center = new Point( 2*gearRad, 2*gearRad );
//		gear = new Gear(center, gearRad, Color.GREEN );
//		gears.add(gear);
//
//		// TOP RIGHT
//		center = new Point( width-2*gearRad, 2*gearRad );
//		gear = new Gear(center, gearRad, Color.GREEN );
//		gears.add(gear);
//
//		// BOTTOM LEFT
//		center = new Point( 2*gearRad, height-2*gearRad );
//		gear = new Gear(center, gearRad, Color.GREEN );
//		gears.add(gear);
//
//		// BOTTOM RIGHT
//		center = new Point( width-2*gearRad, height-2*gearRad );
//		gear = new Gear(center, gearRad, Color.GREEN );
//		gears.add(gear);
//
//		// CENTER
//		center = new Point( width/2.0, height/2.0 );
//		gear = new Gear(center, gearRad, Color.GREEN );
//		gears.add(gear);

	}

	// get status list 

	public synchronized List<String> getStatusList () {
		if ( !change )
			return statusList;
		change = false;
		statusList.clear();
		
		statusList.add(width + "," + height);
		// first players
		playersFill();
		// second dots
		dotsFill();
		// third powers up and down
		powersFill();
		// fourth gears
		gearsFill();
		// 5th power objects 
		powerObjectFill();
		/// done everything
		return statusList;
	}

	public synchronized  void playersFill () {
		for ( PlayerGame player : players ){
			if ( player.getBubs().getBubLst().size() < 1 )
				continue;
			Bubble bub = player.getBubs().getBubLst().get(0);

			String name = new String ( player.getName() + (player.getPowerStat().length() > 0 ? (player.getPowerStat()) : "") ); 
			String s = new String ( "Player," + player.getUserName() + "," + name + ","
					+ colorToString(bub.getBorderColor()) + "," + colorToString(bub.getFillColor()) + "," + 
					player.getImgName() + "," + player.getSpeedyP() + "," + player.getGodMoP() + "," +
					player.getFastJoinP());
			
			s = new String ( s + "," + bub.getCenter().getY());
			s = new String ( s + "," + player.getBubs().getBubLst().size());
			List <Bubble> lst = player.getBubs().getBubLst();
			for ( Bubble bubi : lst ){
				s = new String( s + "," + new Double(bubi.getCenter().getX()) + "," + new Double(bubi.getRadius()) );
			}
			//TODO
//			Double y = new Double( bub.getCenter().getY() );
//			statusList.add(y + "");
//			s = "";
//			int head = 0;
//			List <Bubble> lst = player.getBubs().getBubLst();
//			for ( Bubble bubi : lst ){
//				s = new String ( s + (head++ == 0 ? "" : "," ) + new Double(bubi.getCenter().getX()));
//			}
//			statusList.add(s);
//
//			s = "";
//			head = 0;
//			for ( Bubble bubi : lst ){
//				s = new String ( s + (head++ == 0 ? "" : "," ) + new Double(bubi.getRadius()));
//			}
			statusList.add(s);
		}
	}

	public synchronized  void dotsFill    (){
		for ( Dot dot : dots ){
			String s = new String( "Dot," + colorToString(dot.getBorderColor()) + "," + colorToString(dot.getFillColor()) + "," +
					Dot.getRadConst() + "," + dot.getCenter().getX() + "," + dot.getCenter().getY());
			statusList.add(s);
		}
	}

	public synchronized  void powersFill  () {
		for ( PowerUD power : powers ){
			String s = new String ( "Power," + colorToString(power.getBorderColor()) + "," + colorToString(power.getFillColor()) + "," +
					power.getType() + "," + PowerUD.getRadConst() + "," +
					power.getCenter().getX() + "," + power.getCenter().getY());
			statusList.add(s);
		}
	}

	public synchronized  void gearsFill   (){
		for ( Gear gear : gears ){
			String s = new String ( "Gear," + colorToString(gear.getBorderColor()) + "," + gear.getImgName() + "," + gearRad + "," +
					gear.getCenter().getX() + "," + gear.getCenter().getY());
			statusList.add(s);
		}
	}

	public synchronized  void powerObjectFill () {
		for ( Object obj : powerObj ){
			try {
				Field field;
				String name;
				Color borderColor;
				Color fillColor;
				double radius;
				Point center;
				
				Class cls = obj.getClass();
				
				// name
				field = cls.getDeclaredField("name");
				name = (String) (field.get(null));
				// borderColor
				field = cls.getDeclaredField("borderColor");
				borderColor = (Color) (field.get(null));
				// fillColor
				field = cls.getDeclaredField("fillColor");
				fillColor = (Color) (field.get(null));
				// radius
				field = cls.getDeclaredField("radius");
				radius = (Double) (field.get(null));
				// center
				field = cls.getDeclaredField("center");
				center = (Point) (field.get(obj));
				
				
				String s = new String ( "PowerObject," + name + "," + colorToString(borderColor) + "," 
										+ colorToString(fillColor) + "," + radius + "," + center.getX() + "," + center.getY() );
				statusList.add(s);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}

	
	
	// updating game sheet 

	public synchronized void update () {
		// take a sorted list of all players bubbles 
		List <Bubble> playerBubLst = getSortedBubble();
		// first taking care of players under the gears
		gearUpdatingPlayer( playerBubLst );
		// second taking care of players eating the powers
		eatingPowerUpdatePlayer( playerBubLst );
		// second and half :) taking care of players eating the powersObject
		eatingPowerObjectUpdatePlayer( playerBubLst );
		// third taking care of players eating the dots
		eatingDotsUpdatePlayer( playerBubLst );
		// 4th taking care of players eating each other
		eatingPlayerUpdatePlayer( playerBubLst );
		// making new Dots and possibly new Powers
		// TODO timer 1s
		addDot();
		addPower();
		addPowerClass();
	}	
	

	public PlayerGame getPlayer ( Bubble bub ) {
		for ( PlayerGame player : players )
			if ( player.getBubs().getBubLst().contains(bub) )
				return player;
		return null;
	}

	public List <Bubble> getSortedBubble () {
		List <Bubble> lst = new ArrayList<>();
		for ( PlayerGame player : players )
			lst.addAll(player.getBubs().getBubLst());
		Collections.sort(lst);
		return lst;
	}

	public synchronized void gearUpdatingPlayer ( List <Bubble> bubLst) {
		for ( Bubble bub : bubLst ) {
			PlayerGame player = getPlayer(bub);
			if ( player == null )
				continue;
			for ( Gear gear : gears ){
				if ( bub.isIn(gear) && bub.getRadius() > gear.getRadius() )
					player.gearPassingBubble(bub);
			}
		}
	}

	public synchronized void eatingPowerUpdatePlayer ( List <Bubble> bubLst ){
		List <PowerUD> deadPowers = new ArrayList<>();
		for ( Bubble bub : bubLst ){
			PlayerGame player = getPlayer(bub);
			if ( player == null )
				continue;
			for ( PowerUD power : powers ){
				if ( power.isDead() )
					continue;
				if ( bub.isIn(power) ){
					player.eat(bub, power);
					power.setDead(true);
					deadPowers.add(power);
				}
			}
		}
		for ( PowerUD power : deadPowers ){
			if ( powers.contains(power) )
				powers.remove(power);
		}
	}

	public synchronized void eatingPowerObjectUpdatePlayer ( List <Bubble> bubLst ){
		//TODO
		List <Object> deadPowersObject = new ArrayList<>();
		for ( Bubble bub : bubLst ){
			//TODO
			PlayerGame player = getPlayer(bub);
			if ( player == null )
				continue;
			for ( Object obj : powerObj ){
				try {
					Class cls = obj.getClass();
					Point center; 
					double radius;
					boolean isDead;
					
					Field field = cls.getDeclaredField("center");
					center = (Point) (field.get(obj));
					field = cls.getDeclaredField("radius");
					radius = (Double) (field.get(null));
					field = cls.getDeclaredField("isDead");
					isDead = (Boolean) (field.get(obj));
					if ( isDead ) 
						continue;
					Bubble bubi = new Bubble(center, radius, bub);
					if ( bub.isIn(bubi) == false && radius < bub.getRadius() )
						continue;
					field = cls.getDeclaredField("isDead");
					field.set(obj, true);
					deadPowersObject.add(obj);
					
					bub.eat(bubi);
					player.getBubs().normalize();
					
					synchronized (player.getMnOb()) {
						Field fieldi = cls.getDeclaredField("name");
						String namei = (String) (fieldi.get(null));
						String [] status = player.getPowerStat().split("-");
						boolean isWorking = false;
						for ( int j = 0; j < status.length; ++j )
							if ( status[j].compareTo(namei) == 0 ){
								isWorking = true;
							}
						if ( isWorking )
							continue;
						
						
						
						Method met = cls.getDeclaredMethod("setPlayer", new Class [] { PlayerGame.class } );
						met.invoke(obj, player);
						
						met = cls.getDeclaredMethod("changePlayer", new Class[] { PlayerGame.class } );
						player = (PlayerGame) (met.invoke(obj, player));
						
						field = cls.getDeclaredField("timeSec");
						int timeSec = (Integer) (field.get(null) );
						if ( timeSec > 0 ) {
							Timer timer = new Timer(timeSec, null);
							timer.addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent arg0) {
									try {
										Method met = cls.getDeclaredMethod("getPlayer");
										PlayerGame player = (PlayerGame) (met.invoke(obj));
										
										met = cls.getDeclaredMethod("normalPlayer", new Class [] { PlayerGame.class } );
										player = (PlayerGame) (met.invoke(obj, player));
										
										Field field = cls.getDeclaredField("name");
										String name = (String) (field.get(null));
										
										String stat = player.getPowerStat();
										String [] stats = stat.split("-");
										stat = new String ("");
										for ( int i = 0 ; i < stat.length(); i += 2 )
											if ( stats[i].compareTo(name) != 0 )
												stat = new String( stat + "-" + stats[i] + "-" + stats[i+1] );
										player.setPowerStat(stat);
										
										timer.stop();
									} catch (Exception e) {
										e.printStackTrace();
									}
									
								}
							});
							timer.start();
						}
						
						field = cls.getDeclaredField("show");
						boolean show  = (Boolean) ( field.get(null) );
						field = cls.getDeclaredField("name");
						String name = (String) ( field.get(null) );
						
						String stat = player.getPowerStat();
						String [] stats = stat.split("-");
						boolean tmp = true;
						for ( int i = 0 ; i < stats.length; ++i )
							if ( stats[i].compareTo(name) == 0 )
								tmp = false;
						if ( tmp ){
							stat = new String( stat + "-" + name + "-" + (show ? 1 : 0) );
							player.setPowerStat(stat);
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		for ( Object obj : deadPowersObject )
			if ( powerObj.contains(obj) ){
				powerObj.remove(obj);
			}
	}
	
	public synchronized void eatingDotsUpdatePlayer ( List <Bubble> bubLst ){
		List <Dot> deadDots = new ArrayList<>();
		for ( Bubble bub : bubLst ){
			PlayerGame player = getPlayer(bub);
			if ( player == null )
				continue;
			for ( Dot dot : dots ){
				if ( dot.isDead() )
					continue;
				if ( bub.isIn(dot) ){
					player.eat(bub, dot);
					dot.setDead(true);
					deadDots.add(dot);
				}
			}
		}
		for ( Dot dot : deadDots ){
			if ( dots.contains(dot) )
				dots.remove(dot);
		}
	}

	public synchronized void eatingPlayerUpdatePlayer ( List <Bubble> bubLst ){
		List <Bubble> deadBubble = new ArrayList<>();
		int head = 0;
		for ( Bubble bub : bubLst ){
			++head;
			if ( bub.isDead() )
				continue;
			PlayerGame player = getPlayer(bub);
			if ( player == null )
				continue;
			int tail = 0;
			for ( Bubble bubEaten : bubLst ){
				if ( (tail++) < head )
					continue;
				if ( bubEaten.isDead() )
					continue;
				PlayerGame playerEaten = getPlayer(bubEaten);
				if ( playerEaten == null )
					continue;

				if ( bub.isIn(bubEaten) && bub.getRadius() > bubEaten.getRadius() && playerEaten.isGodMo() == false ){
					player.eat(bub, bubEaten);
					playerEaten.bubbleDie(bubEaten);
					bubEaten.setDead(true);
					deadBubble.add(bubEaten);
				}
			}
		}
		for ( Bubble bub : deadBubble ){
			PlayerGame player = getPlayer(bub);
			if ( player == null )
				continue;
			player.getBubs().removeBubble(bub);
			if ( player.getBubs().getBubLst().size() < 1 )
				removePlayer(player.getUserName());
		}
	}

	// commands Methods

	public PlayerGame getPlayerUserName( String userName ){
		for ( PlayerGame player : players )
			if ( player.getUserName().compareTo(userName) == 0 )
				return player;
		return null;
	}


	public synchronized void movePlayer ( String userName, Point dir  ) {
		PlayerGame player = getPlayerUserName( userName );
		if ( player == null )
			return;
		player.move(dir, new Point(width,height));
		change = true;
		update();
	}

	public synchronized void splitPlayer ( String userName ){
		PlayerGame player = getPlayerUserName(userName);
		if ( player == null )
			return;
		player.splitBubble();
		change = true;
		update();
	}

	public synchronized void joinPlayer ( String userName ){
		PlayerGame player = getPlayerUserName(userName);
		if ( player == null )
			return;
		player.joinBubble();
		change = true;
		update();

	}

	public synchronized void speedyPlayer ( String userName ){
		PlayerGame player = getPlayerUserName(userName);
		if ( player == null )
			return;
		player.speedyPowerUp();
		//TODO no need for new update!
		change = true;
		update();
	}

	public synchronized void godMoPlayer ( String userName ){
		PlayerGame player = getPlayerUserName(userName);
		if ( player == null )
			return;
		player.godMoPowerUp();
		//TODO no need for update !
		change = true;
		update();
	}

	public synchronized void fastJoinPlayer ( String userName ){
		PlayerGame player = getPlayerUserName(userName);
		if ( player == null )
			return;
		player.fastJoinPowerUp();
		change = true;
		update();
	}

	public synchronized void endPlayer ( String userName ){
		removePlayer(userName);
	}

	
	
	// adding a new Power up and down class
	public synchronized void addPowerClass ( Class cls ) {
		powerClass.add( cls );
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

	
	/// random Point and Color generator
	
	public synchronized Point pointGenerator ( double rad ){
		List <Circle> cir = new ArrayList<>();
		cir.addAll(dots);
		cir.addAll(powers);
		cir.addAll(gears);
		for ( PlayerGame player : players )
			cir.addAll(player.getBubs().getBubLst());
		while ( true ){
			double x = Math.random() * width;
			double y = Math.random() * height;
			Point pnt = new Point(x, y);
			boolean temp = true;
			for ( Circle c : cir )
				if ( pnt.getDistance(c.getCenter()) < rad + c.getRadius() + Circle.getBorderLen() ){
					temp = false;
					break;
				}
			for ( Object obj : powerObj ){
				try {
					Class cls = obj.getClass();
					
					Field field = cls.getDeclaredField("center");
					Point center = (Point) (field.get(obj));
					field = cls.getDeclaredField("radius");
					double radius = (Double) (field.get(null));
					if ( pnt.getDistance(center) < rad + radius + Circle.getBorderLen() ){
						temp = false;
						break;
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if ( pnt.getX() - rad < 0 )
				temp = false;
			if ( pnt.getY() - rad < 0 )
				temp = false;
			if ( pnt.getX() + rad > width )
				temp = false;
			if ( pnt.getY() + rad > height )
				temp = false;
			//TODO
			if (temp)
				return pnt;
		}
	}

	public Color colorGenerator (){
		int x = (int) ( Math.random() * 255 );
		int y = (int) ( Math.random() * 255 );
		int z = (int) ( Math.random() * 255 );
		return new Color(x, y, z);
	}

}
