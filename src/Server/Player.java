package Server;

import java.io.PrintStream;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

import Buffers.GameCommandBuffer;
import Buffers.GameStatusInBuffer;
import Buffers.GameStatusOutBuffer;

public class Player extends Thread {

	private Server server;
	
	private Socket socket = null;
	private Scanner scanner = null;
	private PrintStream printer = null;
	
	private String userName;
	
	private GameCommandBuffer command = null;
	private GameStatusInBuffer statIn = null;
	private GameStatusOutBuffer statOut = null;
	
	
	//constructor
	public Player( Server server, Socket socket, Scanner scanner, PrintStream printer, String userName,
			GameCommandBuffer command, GameStatusInBuffer statIn, GameStatusOutBuffer statOut) {
		this.server = server;
		this.socket = socket;
		this.scanner = scanner;
		this.printer = printer;
		this.userName = userName;
		this.command = command;
		this.statIn = statIn;
		this.statOut = statOut;
	}
	
	// getter and setter methods
	public String getUserName() {
		return userName;
	}


	@Override
	public void run() {
		super.run();
		while ( socket.isClosed() == false ){
			StringBuilder str = new StringBuilder(scanner.nextLine());
//			server.getServerBuff().put("recieved from client at (" +
//					socket.getInetAddress() +
//					"): " + str);
			stringProcessor( new String(str) );
		}
		server.killPlayer( this );
	}
	
	
	public void stringProcessor ( String str ){
		String [] quer = str.split(",");
		
		
		if ( quer[0].compareTo("Command") == 0 ){
			String comm = new String ("");
			for ( int i = 1; i < quer.length; ++i ){
				if ( i > 1 )
					comm = new String ( comm + "," );
				comm = new String( comm + quer[i] );
			}
			command.put(comm);
			
			/// dead query
			if ( comm.compareTo("end") == 0 ){
				List <String> lst = null;
				while ( lst == null ){
					lst = statOut.pick();
//					System.out.println("Ali");
//					if ( lst != null )
//						System.out.println(lst.size() + " " + lst.get(0));
				}
				sendToClient(lst);
			}
			
			
		} else if ( quer[0].compareTo("Status") == 0 ) {
				statIn.put();
				List <String> lst = null;
				while ( lst == null ){
					lst = statOut.pick();
				}
				sendToClient( lst );
		}
	}
	
	public void sendToClient( List<String> lst ){
		for( String s : lst ){
			if ( socket.isClosed() == false ){
				printer.println(s);

//				server.getServerBuff().put("Sent to Client at ("
//						+ socket.getInetAddress()
//						+ "): " + s);
			}
		}
		// dead Part
		if ( lst.size() == 1 && lst.get(0).compareTo("dead") == 0 ){
			server.killPlayer( this );
		}
	}
	
}
