package Game;

import java.util.ArrayList;
import java.util.List;

import Buffers.GameCommandBuffer;
import Buffers.GameStatusInBuffer;
import Buffers.GameStatusOutBuffer;
import Shapes.Point;

public class PlayerThreadGame {
		
	private String userName; // unique for every player
	
	private GameStatusInBuffer  statusIn;
	private GameStatusOutBuffer statusOut;
	private GameCommandBuffer   command;
	
	private Thread statusInT;
	private Thread commandT;
	
	
	private Game game;
	
	
	/// Constructors
	
	public PlayerThreadGame( String userName, Game game, GameStatusInBuffer statusIn, GameStatusOutBuffer statusOut, GameCommandBuffer command ) {
		this.game      = game;
		this.command   = command;
		this.userName  = userName;
		this.statusIn  = statusIn;
		this.statusOut = statusOut;
		
		
		// status in thread work
		statusInT = new Thread(new Runnable() {
			@Override
			public void run() {
				while( true ){
					boolean statIn = statusIn.pick();
					if ( statIn ){
						List <String> lst = new ArrayList<>();
						lst.add(new String("GameStatus"));
						lst.add(userName);
						lst.addAll(game.getStatusList());
						lst.add(new String("done"));
						statusOut.put(lst);
					}
				}
			}
		});
		
		// commands thread work
		commandT = new Thread(new Runnable() {
			@Override
			public void run() {
				while ( true ){
					String query = command.pick();
					if ( query == null )
						continue;
					stringProcessor(query);
				}
			}
		});
		
	}
	
	
	// getter and setter Methods
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	
	// other Methods
	
	public void startPlayer () {
		statusInT.start();
		commandT.start();
	}
	
	public void endPlayer (){
		List <String> lst = new ArrayList<>();
		lst.add("dead");
		statusOut.put(lst);
		
 		statusInT.stop();
		commandT.stop();
		
	}
	
	public void stringProcessor ( String s ){
		String [] query = s.split(",");
		if ( query.length == 0 )
			return;
		
		// moving
		if ( query[0].compareTo("move") == 0 ){
			if ( query.length != 3 )
				return;
			
			double x = 0, y = 0;
			try {
				x = Double.parseDouble(query[1]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				y = Double.parseDouble(query[2]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			game.movePlayer( userName, new Point(x,y) );
		}
		
		// splitting 
		if ( query[0].compareTo("split") == 0 ){
			game.splitPlayer( userName );
		}
		
		// joining
		if ( query[0].compareTo("join") == 0 ) {
			game.joinPlayer( userName );
		}
		
		// speedy power up
		if ( query[0].compareTo("speedy") == 0 ){
			game.speedyPlayer( userName );
		}
		
		// godMo power up
		if ( query[0].compareTo("godmo") == 0 ){
			game.godMoPlayer( userName );
		}
		
		// fast join power up
		if ( query[0].compareTo("fastjoin") == 0 ){
			game.fastJoinPlayer( userName );
		}
		
		// end game for player 
		if ( query[0].compareTo("end") == 0 ){
			game.endPlayer( userName );
		}
	}
	

}
