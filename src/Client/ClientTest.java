package Client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import Buffers.ClientToMainBuff;
import Buffers.MyWaitNotify;

public class ClientTest extends Thread {

	private Socket socket = null;
	private String serverIp = "127.0.0.1";
	private int port = 0;
	Scanner scanner = null;
	PrintStream printer = null;
	ClientToMainBuff clientBuff;
	MyWaitNotify monitor;
	
	// Constructor
	public ClientTest ( String serverIp, int port, ClientToMainBuff clientBuff, MyWaitNotify monitor ){
		this.serverIp = serverIp;
		this.port = port;
		this.clientBuff = clientBuff;
		this.monitor = monitor;
	}
	
	
	// other Methods
	
	public boolean initialize() {
		boolean temp = true;
		try {
			socket = new Socket(serverIp, port);
			scanner = new Scanner(socket.getInputStream());
			printer = new PrintStream(socket.getOutputStream());
		} catch (IOException e) {
			clientBuff.put("no");
			temp  = false;
		}
		monitor.Notify();
		return temp;
	}

	@Override
	public void run() {
		super.run();
		if ( initialize() == false ){
			clientBuff.put("no");
			return;
		}
		if ( socket == null ){
			clientBuff.put("no");
			return;
		}
		while ( socket.isClosed() == false ) {
			try {
				String str = scanner.nextLine();
				if ( str.compareTo("dead123456") == 0 ){
					try {
						send("Command,end");
						socket.close();
						socket = null;
						clientBuff.put("yes");
						return;
					} catch (IOException e) {
						clientBuff.put("no");
					}
				} else {
					try {
						socket.close();
						socket = null;
						clientBuff.put("no");
						return;
					} catch (IOException e) {
						clientBuff.put("no");
					}
				}
				
				
			} catch (Exception e) {
				clientBuff.put("no");
				break;
			}
		}
	}
	
	
	public void send ( String str ){
		if ( socket == null )
			return;
		printer.println(str);
	}
	
}
