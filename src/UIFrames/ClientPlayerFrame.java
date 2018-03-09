package UIFrames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import Buffers.ClientToMainBuff;
import Buffers.FrameCommandBuff;
import Buffers.MyWaitNotify;
import Buffers.StatusClientBuff;
import Client.Client;

public class ClientPlayerFrame extends JFrame {


	private String name;
	private Color borderColor;
	private Color fillColor;
	private String imgName;
	private boolean isKeyboard;


	private Container pane;

	private JButton endBtn;

	public ClientPlayerFrame( String name, Color borderColor, Color fillColor, String imgName, boolean isKeyboard) {

		this.name = name;
		this.borderColor = borderColor;
		this.fillColor = fillColor;
		this.imgName = imgName;
		this.isKeyboard = isKeyboard;



		ClientToMainBuff buff = new ClientToMainBuff();
		FrameCommandBuff frameComm = new FrameCommandBuff();
		StatusClientBuff statusBuff = new StatusClientBuff();
		MyWaitNotify monitor = new MyWaitNotify(this);
		Client client;
		client = new Client("127.0.0.1", 12344, buff, frameComm, statusBuff, monitor);
		client.initialize();
		monitor.Wait();
		client.start();
		
		
		pane = getContentPane();
		pane.setLayout(new BorderLayout());
		
		endBtn = new JButton("Close");
		endBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				client.send("Command,end");
				pane.setVisible(false);
				ClientPlayerFrame.this.setVisible(false);
				//new GoodByeFrame();
			}
		});

		Timer t = new Timer(3000, null);
		t.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				client.send("Guest," + name + "," + colorToString(borderColor) + "," + colorToString(fillColor) + "," +
						imgName);
				String s = buff.pick();
				String [] quer = s.split(",");
				if ( quer[0].compareTo("LoggedIn") == 0 ){
					client.send("StartGame");
					s = buff.pick();

					if ( isKeyboard )
						ClientPlayerFrame.this.setBounds(50, 20, 500, 700);
					else
						ClientPlayerFrame.this.setBounds(650, 20, 500, 700);
					pane.removeAll();
					pane.revalidate();

					PaintPanel gamePanel = new PaintPanel(ClientPlayerFrame.this, client, frameComm, statusBuff, isKeyboard);
					pane.add(gamePanel, BorderLayout.CENTER);

					JPanel closePanel = new JPanel();
					closePanel.setLayout(new GridBagLayout());
					GridBagConstraints gc = new GridBagConstraints();

					gc.fill = GridBagConstraints.NONE;
					//gc.anchor = GridBagConstraints.FIRST_LINE_END;
					gc.anchor = GridBagConstraints.FIRST_LINE_START;
					gc.gridx = 0;
					gc.gridy = 0;
					gc.weightx = 0;
					gc.weighty = 0;
					gc.insets = new Insets(3, 0, 3, 20);
					closePanel.add(endBtn, gc);
					pane.add(closePanel, BorderLayout.SOUTH);
				}
				t.stop();
			}
		});
		t.start();





		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
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
