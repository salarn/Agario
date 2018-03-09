package Server;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Buffers.GameCommandBuffer;
import Buffers.GameStatusInBuffer;
import Buffers.GameStatusOutBuffer;
import Buffers.ServerToFrameBuff;
import Game.Game;

public class Server {

	private String serverName;

	private ServerSocket serverSocket = null;
	private int port = 0;
	private ServerToFrameBuff serverBuff = null;


	private Thread listeningThread = null;
	private List <Player> players = null;


	private File registerF = null;
	private Writer regWriter = null;


	private static int guestCnt = 0;


	private Game game;	

	// Constructor
	public Server( String serverName, int port, ServerToFrameBuff serverBuff, double width, double height ) {
		this.port       = port;
		this.serverBuff = serverBuff;
		this.serverName = serverName;
		//this.game = new Game(width, height);
		// TODO change limits
		this.game = new Game(1000, 1000, 100, 20, 1, 60);
		
		players = new ArrayList<>();

		
		try {
			if ( port == 0 ){
				serverSocket = new ServerSocket();
			} else{
				serverSocket = new ServerSocket(port);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		registerWriterInitialize();

	}

	
	// getter and setter method
	public ServerToFrameBuff getServerBuff() {
		return serverBuff;
	}

	public void setServerBuff(ServerToFrameBuff serverBuff) {
		this.serverBuff = serverBuff;
	}


	// other Methods
	public void listen () {

		listeningThread = new Thread( new Runnable() {
			ServerSocket listenSocket = serverSocket;
			@Override
			public void run() {
				while( listenSocket.isClosed() == false ){
					try{
						final Socket soc = listenSocket.accept();
						serverBuff.put("client at (" + soc.getInetAddress()
						+ ") connected");

						final Thread actionThread = new Thread( new Runnable() {
							boolean running = true;

							Socket actionSocket = soc;
							Scanner scanner = null;
							PrintStream printer = null;

							boolean loggedIn   = false;
							
							
							String userName    = null;
							String passWord    = null;
							String name        = null;
							String borderColor = null;
							String fillColor   = null;
							String imgName     = null;

							@Override
							public void run() {
								//getting input Stream
								try {
									scanner = new Scanner(actionSocket.getInputStream());
								} catch (IOException e) {
									e.printStackTrace();
								}
								// getting output stream
								try {
									printer = new PrintStream(actionSocket.getOutputStream());
								} catch (IOException e) {
									e.printStackTrace();
								}

								while ( running && actionSocket.isClosed() == false ){
									StringBuilder str = new StringBuilder(scanner.nextLine());
									serverBuff.put("recieved from client at (" +
											actionSocket.getInetAddress() +
											"): " + str);
									String response = stringProcessor( new String(str) );
									printer.println(response);
									serverBuff.put("sent to client at (" + actionSocket.getInetAddress() +
											") " + response);
									responseQuery(response);
								}

							}

							public String stringProcessor ( String str ){
								if (str.compareTo("CanIPlayInThisServer") == 0 )
									return new String( "dead123456" );
								
								if ( str.compareTo("Command,end") == 0 )
									return new String("dead");

								String quer[] = str.split(",");
								if ( quer.length == 0 )
									return new String("NotValid");


								if ( quer[0].compareTo("Register") == 0 ){
									if ( quer.length != 7 )
										return new String("NotValid");
									
									String addNewPlayerToServer = new String("");
									for ( int i = 1; i < quer.length; ++i ){
										if ( i > 1 )
											addNewPlayerToServer = new String( addNewPlayerToServer + "," );
										addNewPlayerToServer = new String ( addNewPlayerToServer + quer[i] );
									}
									
									
									// response  to query here ***********************************************************************
									boolean temp = addPlayer(addNewPlayerToServer);
									if ( temp )
										return new String( "RegisterCompleted" );
									return new String ("UserNameAlredyBeenTaken");

								}
								
								if ( quer[0].compareTo("Login") == 0 ){
									if ( quer.length != 3 )
										return new String("NotValid");
									
									for ( Player playeri : players )
										if ( playeri.getUserName().compareTo(quer[1]) == 0 )
											return new String ("UserNameAlreadyInServer");
									
									loggedIn = loggingIn( quer[1], quer[2] );
									if ( loggedIn ){
										// response  to query here ***********************************************************************
										fillData( quer[1] );
										return new String ("LoggedIn," + userName + "," + passWord + "," +
												name + "," + borderColor + "," + fillColor + ","
														+ imgName);
									}
									else
										return new String("InCorrectUserNameOrPassWord");
								}

								if ( quer[0].compareTo("Guest") == 0 ){
									if ( quer.length != 5 )
										return new String( "NotValid" );
									// response  to query here ***********************************************************************
									userName    = new String("Guest_" + (guestCnt++));
									passWord    = new String("");
									name        = quer[1];
									borderColor = quer[2];
									fillColor   = quer[3];
									imgName     = quer[4];
									
									loggedIn = true;

									return new String ("LoggedIn," + userName + "," + passWord + "," +
															name + "," + borderColor + "," + fillColor + ","
																	+ imgName);
								}

								if ( quer[0].compareTo("Change") == 0 ){
									if ( quer.length != 5 )
										return new String("NotValid");
									
									if ( loggedIn == false )
										return "NotLoggedIn";
									
									name = quer[1];
									borderColor = quer[2];
									fillColor = quer[3];
									imgName = quer[4];
									
									
									// response  to query here ***********************************************************************
									changeRegisterInfo( userName, passWord, name, borderColor, fillColor, imgName );
									return new String("Changed," + userName + "," + passWord + "," + name + "," + borderColor + ","
														+ fillColor + "," + imgName);
								}

								//TODO
								if ( quer[0].compareTo("StartGame") == 0 ){
									if ( loggedIn )
										return new String("GameStarted");
									else 
										return new String("NotLoggedIn");
								}
								
								
								return new String("NotValid");
							}

							public void responseQuery( String response ) {
								if ( response.compareTo("dead") == 0 ){
									closeSocket();
									return;
								}
								if ( response.compareTo("GameStarted") == 0 ){
									running = false;
									
									GameCommandBuffer command = new GameCommandBuffer();
									GameStatusInBuffer statusIn = new GameStatusInBuffer();
									GameStatusOutBuffer statusOut = new GameStatusOutBuffer();
									Player player = new Player(Server.this, actionSocket, scanner, printer, userName,
											command, statusIn, statusOut);
									
									players.add(player);
									player.start();
									
									game.addPlayer(userName, name, stringToColor(borderColor), stringToColor(fillColor),
											imgName, statusIn, statusOut, command);
									return;
								}

							}

							public void fillData ( String userN ) {
								try {
									FileReader fileReader = new FileReader(registerF);
									BufferedReader bufferedReader = new BufferedReader(fileReader);
									String line;
									while ((line = bufferedReader.readLine()) != null) {
										if ( line.length() == 0 )
											continue;
										String data[] = line.split(",");
										if ( data[0].compareTo(userN) == 0){
											userName    = data[0];
											passWord    = data[1];
											name        = data[2];
											borderColor = data[3];
											fillColor   = data[4];
											imgName     = data[5];
											break;
										} 
									}
									fileReader.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}

							public void closeSocket () {
								try {
									serverBuff.put("client at (" + actionSocket.getInetAddress() +
											") exited.");
									actionSocket.close();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}

						});

						actionThread.start();
						serverBuff.put("starting service to client at: " + 
								soc.getInetAddress() + ".");

					} catch ( IOException e ){
						e.printStackTrace();
					}
				}
			}
		});

		listeningThread.start();
		serverBuff.put("server started.");
	}
	
	public void addClass ( Class cls ){
		game.addPowerClass(cls);
		serverBuff.put( "Class:     " + cls.getName() + "     added to the server!");
	}
	
	public void killPlayer ( Player player ) {
		if ( players.contains(player) == false )
			return ;
		players.remove(player);
		player.stop();
	}

	public void close () {
		serverBuff.put("server closed.");
		listeningThread.stop();

		for ( Player player : players){
			try{
				player.stop();
			} catch ( Exception e ){
				e.printStackTrace();
			}
		}
		try {
			serverSocket.close();
			serverSocket = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean loggingIn ( String userName, String passWord ) {
		try {
			FileReader fileReader = new FileReader(registerF);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				if ( line.length() == 0 )
					continue;
				String data[] = line.split(",");
				if ( data[0].compareTo(userName) == 0 && data[1].compareTo(passWord) == 0 ){
					fileReader.close();
					return true;
				}
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean addPlayer ( String str ){
		boolean temp = true;		
		String [] quer = str.split(",");

		List <String> lst = new ArrayList<>();
		try {
			FileReader fileReader = new FileReader(registerF);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				if ( line.length() == 0 )
					continue;
				String [] qu = line.split(",");
				if ( qu[0].compareTo(quer[0]) == 0 )
						temp = false;
				lst.add(line);
			}
			fileReader.close();
			if ( temp )
				lst.add(str);

			registerWriterInitialize();
			for ( String s : lst ) {
				if ( s.length() > 0){
					regWriter.append(s);
					regWriter.append(System.lineSeparator());
				}
			}
			regWriter.close();
			return temp;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void changeRegisterInfo ( String userName, String passWord, String name, String borderColor, String fillColor, String imgName){
		List <String> lst = new ArrayList<>();
		try {
			FileReader fileReader = new FileReader(registerF);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				if ( line.length() == 0 )
					continue;
				String data[] = line.split(",");
				if ( data[0].compareTo(userName) == 0 ){
					lst.add(new String(userName + "," + passWord + "," + name + "," + borderColor + "," + fillColor + "," + imgName ) );
				}
				else {
					lst.add(line);
				}
			}
			fileReader.close();

			registerWriterInitialize();
			for ( String s : lst ) {
				if ( s.length() > 0){
					regWriter.append(s);
					regWriter.append(System.lineSeparator());
				}
			}
			regWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void registerWriterInitialize () {
		try{
			registerF = new File(serverName + ".txt");
			FileOutputStream outStream = new FileOutputStream(registerF);
			OutputStreamWriter out = new OutputStreamWriter(outStream);
			regWriter = new BufferedWriter(out);
		} catch (Exception e){
			e.printStackTrace();
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
