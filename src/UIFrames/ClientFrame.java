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

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

import Buffers.ClientToMainBuff;
import Buffers.FrameCommandBuff;
import Buffers.MyWaitNotify;
import Buffers.StatusClientBuff;
import Client.Client;
import ImgChooser.ImgChooserFrame;

public class ClientFrame extends JFrame {

	private int port;
	private String ip;


	private Clip clip;

	private Client       client;
	private MyWaitNotify monitor;
	private ClientToMainBuff clientBuff;
	private FrameCommandBuff frameComm;
	private StatusClientBuff statusBuff;



	private Container pane;

	private GameDemoPanel demoPanel;
	private JOptionPane error;
	private JButton soundBtn;
	private JButton endBtn;

	// first part
	private JPanel	firstPanel;

	private JButton registerBtn;
	private JButton loginBtn;
	private JButton guestBtn;


	/// register part
	private JPanel registerPanel;
	private JScrollPane registerPanelScroll;

	private JLabel     userRLbl;
	private JTextField userRTexF;
	private JLabel     passR1Lbl;
	private JTextField passR1TexF;
	private JLabel     passR2Lbl;
	private JTextField passR2TexF;
	private JLabel     nameRLbl;
	private JTextField nameRTexF;

	private Color  borderColorR = Color.RED;
	private Color  fillColorR = Color.RED;

	private JLabel  borderColorRLbl;
	private JButton borderColorRBtn;
	private JLabel  fillColorRLbl;
	private JButton fillColorRBtn;

	private JLabel  imgRLbl;
	private JButton imgRButton;

	private ImgChooserFrame imgRChooser;

	private JButton cancelReg;
	private JButton okReg;
	
	
	// Login Part
	private JPanel loginPanel;
	
	private JLabel     userLLbl;
	private JTextField userLTexF;
	private JLabel     passLLbl;
	private JTextField passLTexF;
	
	private JButton cancelLog;
	private JButton okLog;
	
	
	/// guest Part 
	private JPanel guestPanel;
	
	private JLabel     nameGLbl;
	private JTextField nameGTexF;

	private Color  borderColorG = Color.RED;
	private Color  fillColorG = Color.RED;

	private JLabel  borderColorGLbl;
	private JButton borderColorGBtn;
	private JLabel  fillColorGLbl;
	private JButton fillColorGBtn;

	private JLabel  imgGLbl;
	private JButton imgGButton;

	private ImgChooserFrame imgGChooser;

	private JButton cancelGuest;
	private JButton okGuests;


	
	// Last Part
	private JPanel       lastPanel;
	private JRadioButton keyboradLaRBtn;
	private JRadioButton mouseLaRBtn;
	private JButton      changeLaBtn;
	private JButton      okLast;
	
	
	
	
	//  Change Part 
	private JPanel   changePanel;
	
	
	private JLabel     nameCLbl;
	private JTextField nameCTexF;

	private Color  borderColorC = Color.RED;
	private Color  fillColorC = Color.RED;

	private JLabel  borderColorCLbl;
	private JButton borderColorCBtn;
	private JLabel  fillColorCLbl;
	private JButton fillColorCBtn;

	private JLabel  imgCLbl;
	private JButton imgCButton;

	private ImgChooserFrame imgCChooser;

	private JButton cancelChange;
	private JButton okChange;
	
	private boolean playmusic = true;
	
	
	
	
	////// 
	private String  username = null;
	private String  password = null;
	private String  name     = null;
	private String  borderColorS = null;
	private String  fillColorS   = null;
	private String  imgName      = null;
	private boolean isKeyboard   = false;




	public ClientFrame( String ip, int port, Clip clip ) {
		this.ip  = ip;
		this.port = port;
		this.clip = clip;

		monitor    = new MyWaitNotify();
		clientBuff = new ClientToMainBuff();
		frameComm  = new FrameCommandBuff();
		statusBuff = new StatusClientBuff();

		client = new Client(ip, port, clientBuff, frameComm, statusBuff, monitor);
		client.start();
		monitor.Wait();


		setBounds(100, 100, 400, 600);
		pane = getContentPane();
		pane.setLayout(new BorderLayout());

		demoPanel = new GameDemoPanel();
		pane.add(demoPanel, BorderLayout.NORTH);

		error = new JOptionPane();
		
		Icon soundOn_ic = new ImageIcon("speaker.png");
	    Icon soundOff_ic = new ImageIcon("mute.png");
	    if(clip.isActive()){
	    	playmusic = true;
	    	soundBtn = new JButton(soundOn_ic);
	    }
	    else{
	    	playmusic = false;
	    	soundBtn = new JButton(soundOff_ic);
	    }
	    
	    soundBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ( playmusic == true ){
					playmusic = false;
					ClientFrame.this.clip.stop();
					soundBtn.setIcon(soundOff_ic);
					soundBtn.revalidate();
					pane.revalidate();
				} else {
					playmusic = true;
					playSound();
					soundBtn.setIcon(soundOn_ic);
					soundBtn.revalidate();
					pane.revalidate();
				}
			}
		});

		endBtn = new JButton("Close");
		endBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				client.send("Command,end");
				pane.setVisible(false);
				ClientFrame.this.setVisible(false);
				//new GoodByeFrame();
			}
		});

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

			System.out.println("Error with playing sound.");
			ex.printStackTrace();

		}
	}


	
	
	
	

	public void firstPartInitialize () {
		firstPanel = new JPanel();
		firstPanel.setBackground(new Color(163, 254, 254));
		Border inner = BorderFactory.createTitledBorder("Agar.IO");
		Border outer = BorderFactory.createEmptyBorder();
		firstPanel.setBorder(BorderFactory.createCompoundBorder(inner, outer));

		registerBtn = new JButton(" Rgister ");
		registerBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pane.remove(firstPanel);
				registerInitialize();
			}
		});

		loginBtn = new JButton("  LogIn  ");
		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pane.remove(firstPanel);
				loginInitialize();
			}
		});

		guestBtn = new JButton("Play as Guest");
		guestBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pane.remove(firstPanel);
				guestInitialize();			
			}
		});

		firstPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		gc.fill = GridBagConstraints.NONE;
		//gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 20, 20, 10);
		firstPanel.add(registerBtn, gc);

		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 20, 20, 10);
		firstPanel.add(loginBtn, gc);

		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 20, 80, 10);
		firstPanel.add(guestBtn, gc);

		gc.gridy++;

//		gc.gridx = 0;
//		gc.weightx = 0;
//		gc.weighty = 5;
//		gc.insets = new Insets(0, 0, 0, 0);
//		firstPanel.add(new JPanel(), gc);

		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 10, 0);
		firstPanel.add(endBtn, gc);

		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(-320, -120, 0, 0);
		firstPanel.add(soundBtn, gc);

		pane.add(firstPanel, BorderLayout.CENTER);
		pane.revalidate();
	}

	
	
	public void registerInitialize(){
		registerPanel = new JPanel();
		registerPanel.setBackground(new Color(163, 254, 254));

		Border inner = BorderFactory.createTitledBorder("Register Form");
		Border outer = BorderFactory.createEmptyBorder();
		registerPanel.setBorder(BorderFactory.createCompoundBorder(inner, outer));

		userRLbl   = new JLabel("username: ");
		userRTexF  = new JTextField(10);
		passR1Lbl  = new JLabel("password: ");
		passR1TexF = new JPasswordField(10);
		passR2Lbl  = new JLabel("confrim password: ");
		passR2TexF = new JPasswordField(10);
		nameRLbl   = new JLabel("name: ");
		nameRTexF  = new JTextField(10);

		borderColorRLbl = new JLabel("borderColor: ");
		borderColorRBtn = new JButton("  ");
		borderColorRBtn.setBackground(borderColorR);
		borderColorRBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				borderColorR = JColorChooser.showDialog(borderColorRBtn, "Choose Color ...", borderColorR);
				borderColorRBtn.setBackground(borderColorR);
			}
		});

		fillColorRLbl = new JLabel("fillColor: ");
		fillColorRBtn = new JButton("  ");
		fillColorRBtn.setBackground(fillColorR);
		fillColorRBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fillColorR = JColorChooser.showDialog(fillColorRBtn, "Choose Color ...", fillColorR);
				fillColorRBtn.setBackground(fillColorR);
			}
		});

		imgRLbl     = new JLabel("picture :");
		imgRButton  = new JButton("  ") ;
		imgRChooser = new ImgChooserFrame();
		imgRButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				imgRChooser.setVisible(true);
			}
		});
		cancelReg = new JButton("Cancel");
		cancelReg.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pane.remove(registerPanelScroll);
				firstPartInitialize();
				return;
			}
		});
		
		okReg = new JButton("SignUp!");
		okReg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String quer = new String ("Register");
				
				String s = userRTexF.getText();
				if ( s.length() == 0 || s.indexOf(',') != -1 ){
					userRTexF.setBackground(Color.ORANGE);
					error.showMessageDialog(ClientFrame.this, "UnValid userName!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
					clearReg();
					return;
				}
				quer = new String( quer + "," + s );
				
				s = passR1TexF.getText();
				String s1 = passR2TexF.getText();
				if ( s.compareTo(s1) != 0 ){
					passR1TexF.setBackground(Color.ORANGE);
					passR2TexF.setBackground(Color.ORANGE);
					error.showMessageDialog(ClientFrame.this, "PassWords Must be The Same!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
					clearReg();
					return;
				}
				if ( s.indexOf(',') != -1 || s.length() == 0 ){
					passR1TexF.setBackground(Color.ORANGE);
					passR2TexF.setBackground(Color.ORANGE);
					error.showMessageDialog(ClientFrame.this, "UnValid PassWord", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
					clearReg();
					return;
				}
				quer = new String( quer + "," + s );
				
				s = nameRTexF.getText();
				if ( s.length() == 0 || s.indexOf(',') != -1 ) {
					nameRTexF.setBackground(Color.ORANGE);
					error.showMessageDialog(ClientFrame.this, "UnValid Name", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
					clearReg();
					return;
				}
				quer = new String( quer + "," + s + "," + colorToString(borderColorR) + "," + colorToString(fillColorR) + 
									"," + imgRChooser.getImgChooserPanel().getImgName());
				client.send(quer);
				s = clientBuff.pick();
				
				if ( s.compareTo("UserNameAlredyBeenTaken") == 0 ){
					error.showMessageDialog(ClientFrame.this, "This UserName has Already been Taken!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
					clearReg();
					return;
				} else if ( s.compareTo("RegisterCompleted") == 0 ){
					error.showMessageDialog(ClientFrame.this, "Register Completed", "Task Done", JOptionPane.OK_OPTION | JOptionPane.INFORMATION_MESSAGE);
					pane.remove(registerPanelScroll);
					firstPartInitialize();
					return;
				}
				error.showMessageDialog(ClientFrame.this, "UnvalidTask", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
				clearReg();
				return;
				
				
			}
		});


		registerPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		gc.fill = GridBagConstraints.NONE;
		//gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(3, 0, -20, 20);
		registerPanel.add(userRLbl, gc);

		gc.gridx++;
		gc.insets = new Insets(3, -20, 0, 5);
		registerPanel.add(userRTexF, gc);

		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(3, 0, -20, 20);
		registerPanel.add(passR1Lbl, gc);

		gc.gridx++;
		gc.insets = new Insets(3, -20, 0, 5);
		registerPanel.add(passR1TexF, gc);

		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(3, 0, -20, 20);
		registerPanel.add(passR2Lbl, gc);

		gc.gridx++;
		gc.insets = new Insets(3, -20, 0, 5);
		registerPanel.add(passR2TexF, gc);

		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(3, 0, -20, 20);
		registerPanel.add(nameRLbl, gc);

		gc.gridx++;
		gc.insets = new Insets(3, -20, 0, 5);
		registerPanel.add(nameRTexF, gc);


		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(3, 0, -20, 20);
		registerPanel.add(borderColorRLbl, gc);

		gc.gridx++;
		gc.insets = new Insets(3, -20, 0, 5);
		registerPanel.add(borderColorRBtn, gc);

		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(3, 0, -20, 20);
		registerPanel.add(fillColorRLbl, gc);

		gc.gridx++;
		gc.insets = new Insets(3, -20, 0, 5);
		registerPanel.add(fillColorRBtn, gc);

		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(3, 0, -20, 20);
		registerPanel.add(imgRLbl, gc);

		gc.gridx++;
		gc.insets = new Insets(3, -20, 30, 5);
		registerPanel.add(imgRButton, gc);

//		gc.gridy++;
//
//		gc.gridx = 0;
//		gc.weightx = 0;
//		gc.weighty = 5;
//		gc.insets = new Insets(0, 0, 0, 0);
//		registerPanel.add(new JPanel(), gc);

		
		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 6, 0);
		registerPanel.add(okReg, gc);
		
		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 20, 0);
		registerPanel.add(cancelReg, gc);
		
		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 5, 0);
		registerPanel.add(endBtn, gc);

		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, -10, 0);
		registerPanel.add(soundBtn, gc);

		registerPanelScroll = new JScrollPane(registerPanel);
		pane.add(registerPanelScroll, BorderLayout.CENTER);
		pane.revalidate();

	}

	public void clearReg () {
		userRTexF.setText("");
		passR1TexF.setText("");
		passR2TexF.setText("");
		nameRTexF.setText("");
	}


	
	public void loginInitialize() {
		loginPanel = new JPanel();
		loginPanel.setBackground(new Color(163, 254, 254));

		Border inner = BorderFactory.createTitledBorder("Login Form");
		Border outer = BorderFactory.createEmptyBorder();
		loginPanel.setBorder(BorderFactory.createCompoundBorder(inner, outer));
		
		userLLbl  = new JLabel("username: ");
		userLTexF = new JTextField(10);
		passLLbl  = new JLabel("password: ");
		passLTexF = new JPasswordField(10);
		
		cancelLog = new JButton("cancel");
		cancelLog.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pane.remove(loginPanel);
				firstPartInitialize();
			}
		});
		
		okLog = new JButton("Sign In");
		okLog.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				String quer = new String("Login");
				
				String s = userLTexF.getText();
				if ( s.length() == 0 || s.indexOf(',') != -1 ){
					userLTexF.setBackground(Color.ORANGE);
					error.showMessageDialog(ClientFrame.this, "UnValid userName!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
					clearLogin();
					return;
				}
				quer = new String ( quer + "," + s );
				
				s = passLTexF.getText();
				if ( s.length() == 0 || s.indexOf(',') != -1 ){
					passLTexF.setBackground(Color.ORANGE);
					error.showMessageDialog(ClientFrame.this, "UnValid passWord!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
					clearLogin();
					return;
				}
				quer = new String( quer + "," + s );
				client.send(quer);
				s = clientBuff.pick();
				
				if ( s.compareTo("InCorrectUserNameOrPassWord") == 0 ){
					error.showMessageDialog(ClientFrame.this, "InCorrect username or password!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
					clearLogin();
					return;
				}
				
				if ( s.compareTo("UserNameAlreadyInServer") == 0 ){
					error.showMessageDialog(ClientFrame.this, "This User is Already in Server!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
					clearLogin();
					return;
				}
				
				if ( s.compareTo("NotValid") == 0 ){
					error.showMessageDialog(ClientFrame.this, "UnValid Task!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
					clearLogin();
					return;
				}
				
				String [] q = s.split(",");
				if ( q[0].compareTo("LoggedIn") != 0 || q.length != 7  ){
					error.showMessageDialog(ClientFrame.this, "UnValid Task!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
					clearLogin();
					return;
				}
				
				username     = q[1];
				password     = q[2];
				name         = q[3];
				borderColorS = q[4];
				fillColorS   = q[5];
				imgName      = q[6];
				
				pane.remove(loginPanel);
				lastInitialize();
//				// TODO last frame update
//				System.out.println(username + " " + password + " " + name + " " + borderColorS + " " +
//										fillColorS + " " + imgName);
				
				
			}
		});
		
		
		loginPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		gc.fill = GridBagConstraints.NONE;
		//gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(3, 0, -20, 20);
		loginPanel.add(userLLbl, gc);

		gc.gridx++;
		gc.insets = new Insets(3, -20, 12, 5);
		loginPanel.add(userLTexF, gc);

		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(3, 0, -20, 20);
		loginPanel.add(passLLbl, gc);

		gc.gridx++;
		gc.insets = new Insets(3, -20, 0, 5);
		loginPanel.add(passLTexF, gc);
		
		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 100, 0);
		loginPanel.add(new JPanel(), gc);
		
		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 6, 0);
		loginPanel.add(okLog, gc);
		
		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 30, 0);
		loginPanel.add(cancelLog, gc);
		
		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 10, 0);
		loginPanel.add(endBtn, gc);

		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, -0, 0);
		loginPanel.add(soundBtn, gc);
		
		
		
		
		pane.add(loginPanel, BorderLayout.CENTER);
		pane.revalidate();
	}
	
	public void clearLogin () {
		userLTexF.setText("");
		passLTexF.setText("");
	}

	
	
	public void guestInitialize () {
		guestPanel = new JPanel();
		guestPanel.setBackground(new Color(163, 254, 254));

		Border inner = BorderFactory.createTitledBorder("Guest Form");
		Border outer = BorderFactory.createEmptyBorder();
		guestPanel.setBorder(BorderFactory.createCompoundBorder(inner, outer));
		
		nameGLbl   = new JLabel("name: ");
		nameGTexF  = new JTextField(10);

		borderColorGLbl = new JLabel("borderColor: ");
		borderColorGBtn = new JButton("  ");
		borderColorGBtn.setBackground(borderColorG);
		borderColorGBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				borderColorG = JColorChooser.showDialog(borderColorGBtn, "Choose Color ...", borderColorG);
				borderColorGBtn.setBackground(borderColorG);
			}
		});

		fillColorGLbl = new JLabel("fillColor: ");
		fillColorGBtn = new JButton("  ");
		fillColorGBtn.setBackground(fillColorG);
		fillColorGBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fillColorG = JColorChooser.showDialog(fillColorGBtn, "Choose Color ...", fillColorG);
				fillColorGBtn.setBackground(fillColorG);
			}
		});

		imgGLbl     = new JLabel("picture :");
		imgGButton  = new JButton("  ") ;
		imgGChooser = new ImgChooserFrame();
		imgGButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				imgGChooser.setVisible(true);
			}
		});

		cancelGuest = new JButton("cancel");
		cancelGuest.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pane.remove(guestPanel);
				firstPartInitialize();
			}
		});
		
		okGuests = new JButton("Next!");
		okGuests.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String quer = new String ("Guest" );
				
				String s = nameGTexF.getText();
				if ( s.length() == 0 || s.indexOf(',') != -1 ){
					nameGTexF.setBackground(Color.ORANGE);
					error.showMessageDialog(ClientFrame.this, "UnValid name!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
					nameGTexF.setText("");
					return;
				}
				quer = new String ( quer + ","  + s + "," + colorToString(borderColorG) + "," + colorToString(fillColorG) + "," +
											imgGChooser.getImgChooserPanel().getImgName());
				client.send(quer);
				s = clientBuff.pick();
				
				if ( s.compareTo("NotValid") == 0 ){
					error.showMessageDialog(ClientFrame.this, "UnValid Task!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				String [] q = s.split(",");
				
				username     = q[1];
				password     = q[2];
				name         = q[3];
				borderColorS = q[4];
				fillColorS   = q[5];
				imgName      = q[6];
				
				
//				//TODO lastFrame
//				System.out.println(s);
//				System.out.println(username + " " + password + " " + name + " " + borderColorS + " " +
//						fillColorS + " " + imgName);
				
				pane.remove(guestPanel);
				lastInitialize();
				return;
			}
		});
		
		
		
		guestPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		gc.fill = GridBagConstraints.NONE;
		//gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(3, 0, -20, 20);
		guestPanel.add(nameGLbl, gc);

		gc.gridx++;
		gc.insets = new Insets(3, -20, 0, 5);
		guestPanel.add(nameGTexF, gc);


		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(3, 0, -20, 20);
		guestPanel.add(borderColorGLbl, gc);

		gc.gridx++;
		gc.insets = new Insets(3, -20, 0, 5);
		guestPanel.add(borderColorGBtn, gc);

		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(3, 0, -20, 20);
		guestPanel.add(fillColorGLbl, gc);

		gc.gridx++;
		gc.insets = new Insets(3, -20, 0, 5);
		guestPanel.add(fillColorGBtn, gc);

		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(3, 0, -20, 20);
		guestPanel.add(imgGLbl, gc);

		gc.gridx++;
		gc.insets = new Insets(3, -20, 50, 5);
		guestPanel.add(imgGButton, gc);

//		gc.gridy++;
//
//		gc.gridx = 0;
//		gc.weightx = 0;
//		gc.weighty = 5;
//		gc.insets = new Insets(0, 0, 0, 0);
//		registerPanel.add(new JPanel(), gc);

		
		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 6, 0);
		guestPanel.add(okGuests, gc);
		
		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 20, 0);
		guestPanel.add(cancelGuest, gc);
		
		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 5, 0);
		guestPanel.add(endBtn, gc);

		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, -10, 0);
		guestPanel.add(soundBtn, gc);

		pane.add(guestPanel, BorderLayout.CENTER);
		pane.revalidate();
		
	}


	
	public void lastInitialize () {
		lastPanel = new JPanel();
		lastPanel.setBackground(new Color(163, 254, 254));

		Border inner = BorderFactory.createTitledBorder("Agar.IO");
		Border outer = BorderFactory.createEmptyBorder();
		lastPanel.setBorder(BorderFactory.createCompoundBorder(inner, outer));
		
		
		keyboradLaRBtn = new JRadioButton("keyboard");
		mouseLaRBtn    = new JRadioButton("mouse");
		ButtonGroup keyListenerGp = new ButtonGroup();
		keyListenerGp.add(keyboradLaRBtn);
		keyListenerGp.add(mouseLaRBtn);
		mouseLaRBtn.setSelected(true);
		
		changeLaBtn = new JButton("edit");
		changeLaBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pane.remove(lastPanel);
				changeInitialize();
			}
		});
		
		okLast = new JButton("go for it! :)");
		okLast.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				client.send("StartGame");
				String s = clientBuff.pick();
				if ( s.compareTo("NotValid") == 0 ){
					error.showMessageDialog(ClientFrame.this, "UnValid Task!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if ( keyboradLaRBtn.isSelected() )
					isKeyboard = true;
				
				ClientFrame.this.setBounds(300, 20, 700, 700);
				pane.removeAll();
				pane.revalidate();
				
				PaintPanel gamePanel = new PaintPanel(ClientFrame.this, client, frameComm, statusBuff, isKeyboard);
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
				
				clip.stop();
			}
		});
		
		
		lastPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		gc.fill = GridBagConstraints.NONE;
		//gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(3, 0, 3, 20);
		lastPanel.add(mouseLaRBtn, gc);

		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(3, 0, 5, 20);
		lastPanel.add(keyboradLaRBtn, gc);

		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(3, 0, 50, 20);
		lastPanel.add(changeLaBtn, gc);
		
		
		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(3, 0, 50, 20);
		lastPanel.add(okLast, gc);
		
		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 6, 0);
		lastPanel.add(endBtn, gc);

		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 0, 0);
		lastPanel.add(soundBtn, gc);

		pane.add(lastPanel, BorderLayout.CENTER);
		pane.revalidate();
		
	}

	
	
	public void changeInitialize () {
		changePanel = new JPanel();
		changePanel.setBackground(new Color(163, 254, 254));

		Border inner = BorderFactory.createTitledBorder("Change Form");
		Border outer = BorderFactory.createEmptyBorder();
		changePanel.setBorder(BorderFactory.createCompoundBorder(inner, outer));
		
		nameCLbl   = new JLabel("name: ");
		nameCTexF  = new JTextField(10);
		nameCTexF.setText(name);

		borderColorCLbl = new JLabel("borderColor: ");
		borderColorCBtn = new JButton("  ");
		if ( borderColorS != null )
			borderColorC = stringToColor(borderColorS);
		borderColorCBtn.setBackground(borderColorC);
		borderColorCBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				borderColorC = JColorChooser.showDialog(borderColorCBtn, "Choose Color ...", borderColorC);
				borderColorCBtn.setBackground(borderColorC);
			}
		});

		fillColorCLbl = new JLabel("fillColor: ");
		fillColorCBtn = new JButton("  ");
		if ( fillColorS != null )
			fillColorC = stringToColor(fillColorS);
		fillColorCBtn.setBackground(fillColorC);
		fillColorCBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				fillColorC = JColorChooser.showDialog(fillColorCBtn, "Choose Color ...", fillColorC);
				fillColorCBtn.setBackground(fillColorC);
			}
		});

		imgCLbl     = new JLabel("picture :");
		imgCButton  = new JButton("  ") ;
		imgCChooser = new ImgChooserFrame();
		if ( imgName != null )
			imgCChooser.getImgChooserPanel().setSelectedImg(imgName);
		imgCButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				imgCChooser.setVisible(true);
			}
		});

		cancelChange = new JButton("cancel");
		cancelChange.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pane.remove(changePanel);
				lastInitialize();
			}
		});
		
		okChange = new JButton("OK");
		okChange.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String quer = new String ("Change");
				
				String s = nameCTexF.getText();
				if ( s.length() == 0 || s.indexOf(',') != -1 ){
					nameCTexF.setBackground(Color.ORANGE);
					error.showMessageDialog(ClientFrame.this, "UnValid name!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
					nameCTexF.setText("");
					return;
				}
				quer = new String( quer + "," + s + "," + colorToString(borderColorC) + "," + colorToString(fillColorC) + 
										"," + imgCChooser.getImgChooserPanel().getImgName());
				client.send(quer);
				s = clientBuff.pick();
				if ( s.compareTo("NotLoggedIn") == 0 ){
					error.showMessageDialog(ClientFrame.this, "No User is LoggedIn!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
					return;
				}
				if ( s.compareTo("NotValid") == 0 ){
					error.showMessageDialog(ClientFrame.this, "UnValid Task!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
					return;
				}
//				System.out.println( "query: " + quer);
//				System.out.println( "response: " + s);
//				
				String [] q = s.split(",");
				
				if ( q.length != 7 ){
					error.showMessageDialog(ClientFrame.this, "Server Response Error!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				username     = q[1];
				password     = q[2];
				name         = q[3];
				borderColorS = q[4];
				fillColorS   = q[5];
				imgName      = q[6];
				
				pane.remove(changePanel);
				lastInitialize();
//				System.out.println(username + " " + password + " " + name + " " + borderColorS + " " + fillColorS + " " + imgName);
				
				
			}
		});
		
		
		
		changePanel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();

		gc.fill = GridBagConstraints.NONE;
		//gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(3, 0, -20, 20);
		changePanel.add(nameCLbl, gc);

		gc.gridx++;
		gc.insets = new Insets(3, -20, 0, 5);
		changePanel.add(nameCTexF, gc);


		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(3, 0, -20, 20);
		changePanel.add(borderColorCLbl, gc);

		gc.gridx++;
		gc.insets = new Insets(3, -20, 0, 5);
		changePanel.add(borderColorCBtn, gc);

		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(3, 0, -20, 20);
		changePanel.add(fillColorCLbl, gc);

		gc.gridx++;
		gc.insets = new Insets(3, -20, 0, 5);
		changePanel.add(fillColorCBtn, gc);

		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(3, 0, -20, 20);
		changePanel.add(imgCLbl, gc);

		gc.gridx++;
		gc.insets = new Insets(3, -20, 50, 5);
		changePanel.add(imgCButton, gc);

//		gc.gridy++;
//
//		gc.gridx = 0;
//		gc.weightx = 0;
//		gc.weighty = 5;
//		gc.insets = new Insets(0, 0, 0, 0);
//		changePanel.add(new JPanel(), gc);

		
		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 6, 0);
		changePanel.add(okChange, gc);
		
		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 20, 0);
		changePanel.add(cancelChange, gc);
		
		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 5, 0);
		changePanel.add(endBtn, gc);

		gc.gridy++;

		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, -10, 0);
		changePanel.add(soundBtn, gc);

		pane.add(changePanel, BorderLayout.CENTER);
		pane.revalidate();
		
		
		
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
