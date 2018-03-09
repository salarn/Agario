package UIFrames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.w3c.dom.css.RGBColor;

import Buffers.ClientToMainBuff;
import Buffers.MyWaitNotify;
import Client.ClientTest;

public class ClientFindingIpFrame extends JFrame {
	
	private String ip;
	private boolean playmusic = true;
	
	private MyWaitNotify monitor;
	private Clip clip;
	
	
	private int PORT = 12345;
	private Container pane;

	private GameDemoPanel demoPanel;
	private JOptionPane error;



	////// first Part 
	private JPanel  firstPanel;
	private JButton firstClipButton;
	private JButton firstNextButton;



	/// second Part
	private JPanel secondPanel;
	
	private JButton secondClipButton;

	private JLabel      fromLabel;
	private JTextField  fromTextField;

	private JLabel      toLabel;
	private JTextField  toTextField;

	private JButton     secondNextButton;

	private List <String> avIpLst = new ArrayList<>();

	// third part
	private JPanel          thirdPanel;
	private JRadioButton [] ipLstRadioButton;
	private JButton         thirdNextButton;
	private JButton         thirdClipButton;
	
	
	public ClientFindingIpFrame( Clip clip) {
		this.monitor = monitor;
		this.clip = clip;
		
		setBounds(100, 100, 400, 600);
		pane = getContentPane();
		pane.setLayout(new BorderLayout());

		demoPanel = new GameDemoPanel();
		pane.add(demoPanel, BorderLayout.NORTH);
		playSound();

		error = new JOptionPane();

		firstPartInitialize();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
	}
	
	

	public void playSound() {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("musicSalar.wav").getAbsoluteFile());
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);

			clip.start();

		} catch(Exception ex) {

			System.out.println(" Error with playing sound.");
			ex.printStackTrace();

		}
	}


	public boolean isIp ( String s ){
		String quer = new String ("");
		int head = 0;
		for ( int i = 0 ; i < s.length(); ++i ){
			if ( s.charAt(i) != '.' )
				quer = new String( quer + s.charAt(i) );
			else{
				head++;
				int x;
				try {
					x = Integer.parseInt(quer);
					if ( !(x>=0 && x<256) )
						return false;
				} catch (Exception e) {
					return false;
				}
				quer = new String("");
			}
		}
		int x;
		try {
			x = Integer.parseInt(quer);
			if ( !(x>=0 && x<256) )
				return false;
		} catch (Exception e) {
			return false;
		}
		if ( head != 3 )
			return false;
		return true;
	}
	
	public long ipToLong ( String s ) {
		String quer = new String ("");
		int [] lst = new int [4];
		int head = 0;
		for ( int i = 0 ; i < s.length(); ++i ){
			if ( s.charAt(i) != '.' )
				quer = new String( quer + s.charAt(i) );
			else{
				int x;
				try {
					x = Integer.parseInt(quer);
					lst[head++] = x;
				} catch (Exception e) {
					return -1;
				}
				quer = new String("");
			}
		}
		int x;
		try {
			x = Integer.parseInt(quer);
			lst[head++] = x;
		} catch (Exception e) {
			return -1;
		}
		if ( head != 4 )
			return -1;
		
		long res = 0;
		for ( int i = 3 ; i >= 0; --i )
			res = res*256 + lst[i];
		return res;
		
	}
	
	public String ipToString ( long x ){
		String str = new String("");
		for ( int i = 0 ; i < 4 ; ++i ){
			str = new String ( str + (x%256) );
			if( i < 3 )
				str = new String( str + "." );
			x /= 256;
		}
		return str;
	}
	
	public void checkForIPs ( String from, String to ){
		long head = ipToLong(from), tail = ipToLong(to);
		for ( long i = head ; i <= tail; ++ i ){
			String ip = ipToString(i);
			ClientToMainBuff buff = new ClientToMainBuff();
			MyWaitNotify monitor = new MyWaitNotify(ip);
			ClientTest client = new ClientTest(ip, PORT, buff, monitor);
			client.start();
			monitor.Wait();
			client.send("CanIPlayInThisServer");
			String s = buff.pick();
			if ( s.compareTo("yes") == 0 )
				avIpLst.add(ip);
		}
	}

	
	/// UI diff Parts 
	public void firstPartInitialize (){
		firstPanel = new JPanel();
		
		Border inner = BorderFactory.createTitledBorder("Agar.IO");
		Border outer = BorderFactory.createEmptyBorder();
		firstPanel.setBorder(BorderFactory.createCompoundBorder(inner, outer));

	    ////New things
	    firstPanel.setBackground(new Color(163, 254, 254));
	    Icon soundOn_ic = new ImageIcon("speaker.png");
	    Icon soundOff_ic = new ImageIcon("mute.png");
	    firstClipButton = new JButton(soundOn_ic);
	    playmusic = true;
	    
		firstClipButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ( playmusic == true ){
					playmusic = false;
					clip.stop();
					firstClipButton.setIcon(soundOff_ic);
					firstClipButton.revalidate();
					pane.revalidate();
				} else {
					playmusic = true;
					playSound();
					firstClipButton.setIcon(soundOn_ic);
					firstClipButton.revalidate();
					pane.revalidate();
				}
			}
		});
		//////////
		
		
		firstNextButton = new JButton("Clik to Start!");
		firstNextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pane.remove(firstPanel);
				secondPartInitialize();
			}
		});
		firstPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 100, -15);
		firstPanel.add(firstNextButton, gc);

		gc.gridy=0;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(-140, -140, 0, 0);
		firstPanel.add(firstClipButton,gc );
		
		
		pane.add(firstPanel, BorderLayout.CENTER);
		pane.revalidate();
	}

	public void secondPartInitialize(){
		secondPanel = new JPanel();
		
		Border inner = BorderFactory.createTitledBorder("IP-Range");
		Border outer = BorderFactory.createEmptyBorder();
		secondPanel.setBorder(BorderFactory.createCompoundBorder(inner, outer));

		fromLabel     = new JLabel("From: ");
		fromTextField = new JTextField(10);

		toLabel       = new JLabel("To:   ");
		toTextField   = new JTextField(10);
		
		
		secondPanel.setBackground(new Color(163, 254, 254));
	    Icon soundOn_ic = new ImageIcon("speaker.png");
	    Icon soundOff_ic = new ImageIcon("mute.png");
	    secondClipButton = new JButton(soundOn_ic);
	    playmusic = true;
	    
		secondClipButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ( playmusic == true ){
					playmusic = false;
					clip.stop();
					secondClipButton.setIcon(soundOff_ic);
					secondClipButton.revalidate();
					pane.revalidate();
				} else {
					playmusic = true;
					playSound();
					secondClipButton.setIcon(soundOn_ic);
					secondClipButton.revalidate();
					pane.revalidate();
				}
			}
		});

		secondNextButton = new JButton("Click to Check for IPs");
		secondNextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String from = fromTextField.getText();
				String to   = toTextField.getText();
				if ( isIp(from) == false ){
					error.showMessageDialog(ClientFindingIpFrame.this, "Not Valid IP at From", "UnValid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
					fromTextField.setBackground(Color.ORANGE);
					fromTextField.setText("");
					return;
				} else {
					fromTextField.setBackground(Color.WHITE);
				}
				if ( isIp(to) == false ){
					error.showMessageDialog(ClientFindingIpFrame.this, "Not Valid IP at To", "UnValid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
					toTextField.setBackground(Color.ORANGE);
					toTextField.setText("");
					return;
				} else {
					fromTextField.setBackground(Color.WHITE);
				}
				
				checkForIPs (from, to);
				if ( avIpLst.size() > 0){
					pane.remove(secondPanel);
					thirdPartInitialize();
				} else {
					error.showMessageDialog(ClientFindingIpFrame.this, "No Game is Running On this Range", "UnValid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
					toTextField.setText("");
					fromTextField.setText("");
					return;
				}
				
			}
		});
		
		
		secondPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		gc.fill = GridBagConstraints.NONE;
		//gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(4, 50, 20, 0);
		secondPanel.add(fromLabel, gc);
		
		gc.gridx++;
		gc.insets = new Insets(3, -50, 0, 5);
		secondPanel.add(fromTextField, gc);
		
		gc.gridy++;
		
		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(4, 50, 20, 10);
		secondPanel.add(toLabel, gc);
		
		gc.gridx++;
		gc.insets = new Insets(3, -50, 100, 5);
		secondPanel.add(toTextField, gc);

		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 20, 0);
		secondPanel.add(secondNextButton, gc);
		
		gc.gridy=0;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(-100, -60, 0, 0);
		secondPanel.add(secondClipButton, gc);
		
		pane.add(secondPanel, BorderLayout.CENTER);
		pane.revalidate();

	}

	public void thirdPartInitialize() {
		thirdPanel = new JPanel();
		
		Border inner = BorderFactory.createTitledBorder("IP-Choose");
		Border outer = BorderFactory.createEmptyBorder();
		secondPanel.setBorder(BorderFactory.createCompoundBorder(inner, outer));
		
//		for ( int i = 0; i < 50; ++i )
//			avIpLst.add("127.0.0.1");
		ipLstRadioButton = new JRadioButton [ avIpLst.size() ];
		for ( int i = 0 ; i < avIpLst.size() ; ++i )
			ipLstRadioButton[i] = new JRadioButton( avIpLst.get(i) );
		ButtonGroup ipLstRadioButtonGroup = new ButtonGroup();
		for( int i = 0 ; i < ipLstRadioButton.length ; ++i )
			ipLstRadioButtonGroup.add( ipLstRadioButton[i] );
		ipLstRadioButton[0].setSelected(true);
		
		thirdNextButton = new JButton("Click To Connect To The Server");
		thirdNextButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				for ( int i = 0 ; i < ipLstRadioButton.length; ++i )
					if ( ipLstRadioButton[i].isSelected() ){
						ip = ipLstRadioButton[i].getText();
						break;
					}
				ClientFrame clientFrame = new ClientFrame(ip, PORT, clip); ///
				pane.setVisible(false);
				ClientFindingIpFrame.this.setVisible(false);
			}
		});
		
		thirdPanel.setBackground(new Color(163, 254, 254));
	    Icon soundOn_ic = new ImageIcon("speaker.png");
	    Icon soundOff_ic = new ImageIcon("mute.png");
	    thirdClipButton = new JButton(soundOn_ic);
	    playmusic = true;
	    
		thirdClipButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ( playmusic == true ){
					playmusic = false;
					clip.stop();
					thirdClipButton.setIcon(soundOff_ic);
					thirdClipButton.revalidate();
					pane.revalidate();
				} else {
					playmusic = true;
					playSound();
					thirdClipButton.setIcon(soundOn_ic);
					thirdClipButton.revalidate();
					pane.revalidate();
				}
			}
		});
		
		thirdPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		gc.fill = GridBagConstraints.NONE;
		//gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.gridx = 0;
		gc.gridy = 0;
		
		for ( int i = 0 ; i < ipLstRadioButton.length; ++i ){
			gc.weightx = 0;
			gc.weighty = 0;
			gc.insets = new Insets(4, 50, 0, 0);
			thirdPanel.add(ipLstRadioButton[i], gc);
			gc.gridy++;
		}
	
		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 100, 0);
		thirdPanel.add(new JPanel(), gc);
		
		gc.gridy++;
		

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 0, 0);
		thirdPanel.add(thirdNextButton, gc);
		
		gc.gridy++;
		gc.gridy=0;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(-130, -80, 0, 0);
		thirdPanel.add(thirdClipButton, gc);
		
		pane.add(new JScrollPane(thirdPanel), BorderLayout.CENTER);
		pane.revalidate();
		
	}

}
