package Client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import Buffers.ClientToMainBuff;
import Buffers.FrameCommandBuff;
import Buffers.MyWaitNotify;
import Buffers.StatusClientBuff;

public class Client extends Thread {

	private String serverIp = "127.0.0.1";
	private int port = 0;
	private MyWaitNotify monitor;
	
	private Socket socket = null;
	private Scanner scanner = null;
	private PrintStream printer = null;
	
	private ClientToMainBuff clientBuff;
	private FrameCommandBuff frameComm;
	private StatusClientBuff statusBuff;
	private boolean stautsIsOn = false;
	
	// Constructor
	public Client ( String serverIp, int port, ClientToMainBuff clientBuff, FrameCommandBuff frameComm, StatusClientBuff statusBuff,
			MyWaitNotify monitor){
		this.serverIp = serverIp;
		this.port = port;
		this.clientBuff = clientBuff;
		this.frameComm = frameComm;
		this.statusBuff = statusBuff;
		this.monitor = monitor;
		
		
	}
	
	
	// other Methods
	
	public void initialize() {
		try {
			socket = new Socket(serverIp, port);
			scanner = new Scanner(socket.getInputStream());
			printer = new PrintStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		monitor.Notify();
	}

	@Override
	public void run() {
		super.run();
		initialize();
		while ( socket.isClosed() == false ) {
			try {
				StringBuilder stri = new StringBuilder(scanner.nextLine());
				String str = new String(stri); //scanner.nextLine();
				if ( stautsIsOn ){
					if ( str.compareTo("GameStatus") == 0 ){
						statusBuff.clear();
					} else {
						statusBuff.put(str);
					}
				} else {
					clientBuff.put(str);
				}
				
				
				if ( str.compareTo("GameStarted") == 0 ){
					stautsIsOn = true;
				}
				
				if ( str.compareTo("dead") == 0 ){
					try {
						socket.close();
						clientBuff.put("clientClosed");
						socket = null;
						return;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				
			} catch (Exception e) {
				break;
			}
		}
	}
	
	
	public  void send ( String str ){
		if ( socket == null )
			return;
		printer.println(str);
		printer.flush();
	}
	
	public  void close (){
		if ( socket != null )
			send("Command,end");
		try {
			if ( socket != null ){
				socket.close();
				socket = null;
			}
		} catch (IOException e ){
			e.printStackTrace();
		}
		clientBuff.put("clientClosed");
	}
	
}
