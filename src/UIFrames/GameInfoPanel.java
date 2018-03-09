package UIFrames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;

import Buffers.MyWaitNotify;
import Client.Client;
import ImgChooser.ImgChooserFrame;
import Server.Server;

public class GameInfoPanel extends JPanel {

	
	private JOptionPane error;
	
	private GameMenuMainFrame gameMenuMainFrame;
	
	// first part
	private JButton firstClickToPlay;
	
	
	/// second part Game Panel Information
	
	private int gameWidth;
	private int gameHeight;
	
	private int cntDots;
	
	private double wheelsRadiusConst;
	
	private double mxBubbleSpeed;
	
	
	private JLabel     widthLabel;
	private JTextField widthTextField;
	
	private JLabel     heightLabel;
	private JTextField heightTextField;
	
	private JLabel     cntDotsLabel;
	private JTextField cntDotsTextField;
	
	private JLabel     wheelsRadiusConstLabel;
	private JTextField wheelsRadiusConstTextField;
	
	private JLabel     mxBubbleSpeedLabel;
	private JTextField mxBubbleSpeedTextField;
	
	private JButton next2ClickToPlay;
	
	//// third part player 1 info
	
	private String        namePlayerOne;
	
	private Color         colorPlayerOne = Color.RED;
	
	private BufferedImage imgPlayerOne = null;
	
	private int           typePlayerOne  = 0;
	
	
	
	private JLabel     namePlayerOneLabel;
	private JTextField namePlayerOneTextField;
	
	private JLabel  ColorPlayerOneLabel;
	private JButton ColorPlayerOneButton;
	
	
	private JLabel  imgPlayerOneLabel;
	private JButton imgPlayerOneButton;
	
	private ImgChooserFrame imgChooserFramePlayerOne;
	
	private JButton next3ClickToPlay;
	
	
	private JRadioButton [] keyboardMousePalyerOne;
	private ButtonGroup  keyboardMousePlayerOneGroup;
	
	
	//// 4th part player 2 info
	
	private String        namePlayerTwo;
	
	private Color         colorPlayerTwo = Color.BLUE;
	
	private BufferedImage imgPlayerTwo = null;
	
	private int           typePlayerTwo = 1;
	
	
	
	private JLabel     namePlayerTwoLabel;
	private JTextField namePlayerTwoTextField;
	
	private JLabel  ColorPlayerTwoLabel;
	private JButton ColorPlayerTwoButton;
	
	
	private JLabel  imgPlayerTwoLabel;
	private JButton imgPlayerTwoButton;
	
	private ImgChooserFrame imgChooserFramePlayerTwo;
	
	private JButton next4ClickToPlay;
	
	private JRadioButton [] keyboardMousePalyerTwo;
	private ButtonGroup  keyboardMousePlayerTwoGroup;
	
	
	
	
	private JPanel firstPart;
	private JPanel secondPart;
	private JPanel thirdPart;
	private JPanel fourthPart;
	
	private Clip clip;
	
	
	public GameInfoPanel ( GameMenuMainFrame gameMenuMainFrame ) {
		
		
		//// setting borders
		Border inner = BorderFactory.createTitledBorder("Game Information Menu");
		Border outer = BorderFactory.createEmptyBorder();
		setBorder(BorderFactory.createCompoundBorder(inner, outer));
		
		////
		
		setLayout(new BorderLayout());
		

		{ /// initializing first part 
			firstPart = new JPanel();
			firstPart.setBackground(new Color(163, 254, 254));
			firstClickToPlay = new JButton("Click to Play!");
		}
		
		{ /// initializing second part
			secondPart = new JPanel();
			secondPart.setBackground(new Color(163, 254, 254));
			
			widthLabel = new JLabel("Game Width  :");
			widthTextField = new JTextField(5);
			
			
			heightLabel = new JLabel("Game height  :");
			heightTextField = new JTextField(5);
			
			cntDotsLabel = new JLabel("Maximum Dots on Game  :");
			cntDotsTextField = new JTextField(5);
			
			wheelsRadiusConstLabel = new JLabel("Wheels Radius Const  :");
			wheelsRadiusConstTextField = new JTextField(5);
			
			mxBubbleSpeedLabel = new JLabel("Maximum Bubble Speed  :");
			mxBubbleSpeedTextField = new JTextField(5);
			
			next2ClickToPlay = new JButton("  Next  ");
			
		}
		
		{ /// initializing third part
			thirdPart = new JPanel();
			thirdPart.setBackground(new Color(163, 254, 254));
			
			namePlayerOneLabel = new JLabel("Player One Name  :");
			namePlayerOneTextField = new JTextField(10);
			
			ColorPlayerOneLabel = new JLabel("Player One Color  :");
			ColorPlayerOneButton = new JButton();
			ColorPlayerOneButton.setBackground(colorPlayerOne);
			ColorPlayerOneButton.setPreferredSize(new Dimension(20, 20));
			ColorPlayerOneButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					colorPlayerOne =JColorChooser.showDialog(ColorPlayerOneButton, "Choose Color ...", colorPlayerOne);
					ColorPlayerOneButton.setBackground(colorPlayerOne);
				}
			});
			
			imgChooserFramePlayerOne = new ImgChooserFrame();
			imgPlayerOneLabel = new JLabel("Player One Picture  :");
			imgPlayerOneButton = new JButton();
			imgPlayerOneButton.setPreferredSize(new Dimension(20, 20));
			imgPlayerOneButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					imgChooserFramePlayerOne.setVisible(true);
				}
			});
			
			keyboardMousePalyerOne = new JRadioButton [2];
			keyboardMousePalyerOne[0] = new JRadioButton("  Mouse ");
			keyboardMousePalyerOne[1] = new JRadioButton("KeyBoard");
			keyboardMousePlayerOneGroup = new ButtonGroup();
			for ( int i = 0 ; i < 2 ; ++i )
				keyboardMousePlayerOneGroup.add(keyboardMousePalyerOne[i]);
			keyboardMousePalyerOne[0].setSelected(true);
			
			next3ClickToPlay = new JButton("  Next  ");
		}

	
		{ /// initializing fourth part
			fourthPart = new JPanel();
			fourthPart.setBackground(new Color(163, 254, 254));
			
			namePlayerTwoLabel = new JLabel("Player Two Name  :");
			namePlayerTwoTextField = new JTextField(10);
			
			ColorPlayerTwoLabel = new JLabel("Player Two Color  :");
			ColorPlayerTwoButton = new JButton();
			ColorPlayerTwoButton.setBackground(colorPlayerTwo);
			ColorPlayerTwoButton.setPreferredSize(new Dimension(20, 20));
			ColorPlayerTwoButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					colorPlayerTwo =JColorChooser.showDialog(ColorPlayerTwoButton, "Choose Color ...", colorPlayerTwo);
					ColorPlayerTwoButton.setBackground(colorPlayerTwo);
				}
			});
			
			imgChooserFramePlayerTwo = new ImgChooserFrame();
			imgPlayerTwoLabel = new JLabel("Player Two Picture  :");
			imgPlayerTwoButton = new JButton();
			imgPlayerTwoButton.setPreferredSize(new Dimension(20, 20));
			imgPlayerTwoButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					imgChooserFramePlayerTwo.setVisible(true);
				}
			});
			
			keyboardMousePalyerTwo = new JRadioButton [2];
			keyboardMousePalyerTwo[0] = new JRadioButton("  Mouse ");
			keyboardMousePalyerTwo[1] = new JRadioButton("KeyBoard");
			keyboardMousePlayerTwoGroup = new ButtonGroup();
			for ( int i = 0 ; i < 2 ; ++i )
				keyboardMousePlayerTwoGroup.add(keyboardMousePalyerTwo[i]);
			keyboardMousePalyerTwo[1].setSelected(true);
			
			
			next4ClickToPlay = new JButton(" Start The Game ");
		}
		error = new JOptionPane();
		
		
		
		
		
		showingFristPart();
		
		firstClickToPlay.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				firstPart.setVisible(false);
				showingSecondPart();
			}
		});
		
		
		next2ClickToPlay.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ( secondPartReceive() ){
					secondPart.setVisible(false);
					showingThirdPart();
				}
			}
		});
		
		
		next3ClickToPlay.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ( thirdPartReceive() ){
					imgPlayerOne = imgChooserFramePlayerOne.getImgChooserPanel().getImg();
					thirdPart.setVisible(false);
					showingFourthPart();
				}
			}
		});
		
		next4ClickToPlay.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ( fourthPartReceive() ){
					imgPlayerTwo = imgChooserFramePlayerTwo.getImgChooserPanel().getImg();
					clip.stop();
					gameMenuMainFrame.setVisible(false);
					
					MyWaitNotify monitorServer = new MyWaitNotify(this);
					Server server = new Server("Agar.IO", 12344, null, gameWidth, gameHeight);
					server.listen();
					monitorServer.Wait();
					
					new ClientPlayerFrame(namePlayerOne, colorPlayerOne.brighter(), colorPlayerOne.darker(), 
							imgChooserFramePlayerOne.getImgChooserPanel().getImgName(), (typePlayerOne == 0 ? false : true) );
					
					new ClientPlayerFrame(namePlayerTwo, colorPlayerTwo.brighter(), colorPlayerTwo.darker(), 
							imgChooserFramePlayerTwo.getImgChooserPanel().getImgName(), (typePlayerTwo == 0 ? false : true) );
					
				}
			}
		});
		
		
		
		
		playSound();
	}
	
	
	
	
	
	/// getter and setter Methods
	public int getGameWidth() {
		return gameWidth;
	}


	public void setGameWidth(int gameWidth) {
		this.gameWidth = gameWidth;
	}


	public int getGameHeight() {
		return gameHeight;
	}


	public void setGameHeight(int gameHeight) {
		this.gameHeight = gameHeight;
	}


	public int getCntDots() {
		return cntDots;
	}


	public void setCntDots(int cntDots) {
		this.cntDots = cntDots;
	}


	public double getWheelsRadiusConst() {
		return wheelsRadiusConst;
	}


	public void setWheelsRadiusConst(int wheelsRadiusConst) {
		this.wheelsRadiusConst = wheelsRadiusConst;
	}

	
	public double getMxBubbleSpeed() {
		return mxBubbleSpeed;
	}


	public void setMxBubbleSpeed(int mxBubbleSpeed) {
		this.mxBubbleSpeed = mxBubbleSpeed;
	}


	public String getNamePlayerOne() {
		return namePlayerOne;
	}


	public void setNamePlayerOne(String namePlayerOne) {
		this.namePlayerOne = namePlayerOne;
	}

	
	public Color getColorPlayerOne() {
		return colorPlayerOne;
	}


	public void setColorPlayerOne(Color colorPlayerOne) {
		this.colorPlayerOne = colorPlayerOne;
	}


	public BufferedImage getImgPlayerOne() {
		return imgPlayerOne;
	}


	public void setImgPlayerOne(BufferedImage imgPlayerOne) {
		this.imgPlayerOne = imgPlayerOne;
	}


	public String getNamePlayerTwo() {
		return namePlayerTwo;
	}


	public void setNamePlayerTwo(String namePlayerTwo) {
		this.namePlayerTwo = namePlayerTwo;
	}


	public Color getColorPlayerTwo() {
		return colorPlayerTwo;
	}


	public void setColorPlayerTwo(Color colorPlayerTwo) {
		this.colorPlayerTwo = colorPlayerTwo;
	}


	public BufferedImage getImgPlayerTwo() {
		return imgPlayerTwo;
	}


	public void setImgPlayerTwo(BufferedImage imgPlayerTwo) {
		this.imgPlayerTwo = imgPlayerTwo;
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
	
	
	
	
	
	
	public void showingFristPart () {
		firstPart.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
	
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 20, 10);
		firstPart.add(firstClickToPlay, gc);
	
		add(firstPart, BorderLayout.CENTER);
	}
	
	public void showingSecondPart () {
		secondPart.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 20, 10);
		secondPart.add(widthLabel, gc);
		
		gc.gridx++;
		gc.insets = new Insets(0, 0, 0, 5);
		secondPart.add(widthTextField, gc);
		
		gc.gridy++;
		
		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 20, 10);
		secondPart.add(heightLabel, gc);
		
		gc.gridx++;
		gc.insets = new Insets(0, 0, 0, 5);
		secondPart.add(heightTextField, gc);
		
		
		gc.gridy++;
		
		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 20, 10);
		secondPart.add(cntDotsLabel, gc);
		
		gc.gridx++;
		gc.insets = new Insets(0, 0, 0, 5);
		secondPart.add(cntDotsTextField, gc);
		
		
		gc.gridy++;
		
		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 20, 10);
		secondPart.add(wheelsRadiusConstLabel, gc);
		
		gc.gridx++;
		gc.insets = new Insets(0, 0, 0, 5);
		secondPart.add(wheelsRadiusConstTextField, gc);
		
		
		gc.gridy++;
		
		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 20, 10);
		secondPart.add(mxBubbleSpeedLabel, gc);
		
		gc.gridx++;
		gc.insets = new Insets(0, 0, 0, 5);
		secondPart.add(mxBubbleSpeedTextField, gc);

		gc.gridy++;
		
		{
			JPanel fakePanel = new JPanel();
			gc.gridx = 0;
			gc.weightx = 0;
			gc.weighty = 1;
			gc.insets = new Insets(0, 0, 20, 10);
			secondPart.add(fakePanel, gc);

			gc.gridy++;
		}
		
		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 20, 10);
		secondPart.add(next2ClickToPlay, gc);
		


		gc.gridy++;
		
		
		add(secondPart, BorderLayout.CENTER);
	}
	
	public void showingThirdPart () {
		thirdPart.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 20, 10);
		thirdPart.add(namePlayerOneLabel, gc);
		
		gc.gridx++;
		gc.insets = new Insets(0, 0, 0, 5);
		thirdPart.add(namePlayerOneTextField, gc);
		
		gc.gridy++;
		
		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 20, 10);
		thirdPart.add(ColorPlayerOneLabel, gc);
		
		gc.gridx++;
		gc.insets = new Insets(0, 0, 0, 5);
		thirdPart.add(ColorPlayerOneButton, gc);
		
		
		gc.gridy++;
		
		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 20, 10);
		thirdPart.add(imgPlayerOneLabel, gc);
		
		gc.gridx++;
		gc.insets = new Insets(0, 0, 0, 5);
		thirdPart.add(imgPlayerOneButton, gc);
		
		
		gc.gridy++;
		
		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 20, 10);
		thirdPart.add(keyboardMousePalyerOne[0], gc);
		
		gc.gridy++;
		
		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 10, 10, 10);
		thirdPart.add(keyboardMousePalyerOne[1], gc);
		
		gc.gridy++;
		
		{
			JPanel fakePanel = new JPanel();
			gc.gridx = 0;
			gc.weightx = 0;
			gc.weighty = 1;
			gc.insets = new Insets(0, 0, 20, 10);
			thirdPart.add(fakePanel, gc);

			gc.gridy++;
		}
		
		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 20, 10);
		thirdPart.add(next3ClickToPlay, gc);
		


		gc.gridy++;
		
		
		add(thirdPart, BorderLayout.CENTER);
	}
	
	public void showingFourthPart () {
		fourthPart.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 20, 10);
		fourthPart.add(namePlayerTwoLabel, gc);
		
		gc.gridx++;
		gc.insets = new Insets(0, 0, 0, 5);
		fourthPart.add(namePlayerTwoTextField, gc);
		
		gc.gridy++;
		
		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 20, 10);
		fourthPart.add(ColorPlayerTwoLabel, gc);
		
		gc.gridx++;
		gc.insets = new Insets(0, 0, 0, 5);
		fourthPart.add(ColorPlayerTwoButton, gc);
		
		
		gc.gridy++;
		
		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 20, 10);
		fourthPart.add(imgPlayerTwoLabel, gc);
		
		gc.gridx++;
		gc.insets = new Insets(0, 0, 0, 5);
		fourthPart.add(imgPlayerTwoButton, gc);
		
		
		gc.gridy++;
		
		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 20, 10);
		fourthPart.add(keyboardMousePalyerTwo[0], gc);
		
		gc.gridy++;
		
		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 10, 10, 10);
		fourthPart.add(keyboardMousePalyerTwo[1], gc);
		
		gc.gridy++;
		
		
		{
			JPanel fakePanel = new JPanel();
			gc.gridx = 0;
			gc.weightx = 0;
			gc.weighty = 1;
			gc.insets = new Insets(0, 0, 20, 10);
			fourthPart.add(fakePanel, gc);

			gc.gridy++;
		}
		
		gc.gridx = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 20, 10);
		fourthPart.add(next4ClickToPlay, gc);
		


		gc.gridy++;
		
		
		add(fourthPart, BorderLayout.CENTER);
	}
	
	
	public boolean secondPartReceive () {
		int errorCount = 0;
		
		String widthString = widthTextField.getText();
		if ( widthString.length() == 0 ){
			widthTextField.setBackground(Color.ORANGE);
			error.showMessageDialog(GameInfoPanel.this, "Fill the Game Width!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
			errorCount++;
		}
		try {
			gameWidth = Integer.parseInt(widthString);
		} catch (Exception e) {
			//e.printStackTrace();
			widthTextField.setBackground(Color.ORANGE);
			error.showMessageDialog(GameInfoPanel.this, "Fill the Game Width with Integer!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
			errorCount++;
		}
		
		String heightString = heightTextField.getText();
		if ( heightString.length() == 0 ){
			heightTextField.setBackground(Color.ORANGE);
			error.showMessageDialog(GameInfoPanel.this, "Fill the Game Height!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
			errorCount++;
		}
		try {
			gameHeight = Integer.parseInt(heightString);
		} catch (Exception e) {
			//e.printStackTrace();
			heightTextField.setBackground(Color.ORANGE);
			error.showMessageDialog(GameInfoPanel.this, "Fill the Game Height with Integer!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
			errorCount++;
		}
		
		String cntDotsString = cntDotsTextField.getText();
		if ( cntDotsString.length() == 0 ){
			cntDotsTextField.setBackground(Color.ORANGE);
			error.showMessageDialog(GameInfoPanel.this, "Fill the Maximum Dots on Game!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
			errorCount++;
		}
		try {
			cntDots = Integer.parseInt(cntDotsString);
		} catch (Exception e) {
			//e.printStackTrace();
			cntDotsTextField.setBackground(Color.ORANGE);
			error.showMessageDialog(GameInfoPanel.this, "Fill the Maximum Dots on Game with Integer!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
			errorCount++;
		}
		
		String wheelRadiusConstString = wheelsRadiusConstTextField.getText();
		if ( wheelRadiusConstString.length() == 0 ){
			wheelsRadiusConstTextField.setBackground(Color.ORANGE);
			error.showMessageDialog(GameInfoPanel.this, "Fill the Wheels Radius Const on Game!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
			errorCount++;
		}
		try {
			wheelsRadiusConst = Double.parseDouble(wheelRadiusConstString);
		} catch (Exception e) {
			//e.printStackTrace();
			cntDotsTextField.setBackground(Color.ORANGE);
			error.showMessageDialog(GameInfoPanel.this, "Fill the Wheels Radius Const with Number!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
			errorCount++;
		}
		
		String mxBubbleSpeedString = mxBubbleSpeedTextField.getText();
		if ( mxBubbleSpeedString.length() == 0 ){
			mxBubbleSpeedTextField.setBackground(Color.ORANGE);
			error.showMessageDialog(GameInfoPanel.this, "Fill the Maximum Bubble Speed on Game!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
			errorCount++;
		}
		try {
			mxBubbleSpeed = Double.parseDouble(mxBubbleSpeedString);
		} catch (Exception e) {
			//e.printStackTrace();
			mxBubbleSpeedTextField.setBackground(Color.ORANGE);
			error.showMessageDialog(GameInfoPanel.this, "Fill the Maximum Bubble Speed with Number!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
			errorCount++;
		}
		
		return ( errorCount == 0 ? true : false );
	}
	
	public boolean thirdPartReceive () {
		int errorCount = 0;
		
		namePlayerOne = namePlayerOneTextField.getText();
		if ( namePlayerOne.length() == 0 ){
			namePlayerOneTextField.setBackground(Color.ORANGE);
			error.showMessageDialog(GameInfoPanel.this, "Fill the Player One with String!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
			errorCount++;
		}
		
		typePlayerOne = ( keyboardMousePalyerOne[0].isSelected() ? 0 : 1 );
		
		return ( errorCount == 0 ? true : false );
	}
	
	public boolean fourthPartReceive () {
		int errorCount = 0;
		
		namePlayerTwo = namePlayerTwoTextField.getText();
		if ( namePlayerOne.length() == 0 ){
			namePlayerTwoTextField.setBackground(Color.ORANGE);
			error.showMessageDialog(GameInfoPanel.this, "Fill the Player One with String!", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
			errorCount++;
		}
		
		if ( namePlayerOne.compareTo(namePlayerTwo) == 0 ){
			namePlayerTwoTextField.setBackground(Color.ORANGE);
			error.showMessageDialog(GameInfoPanel.this, "Player Two can't have the same name as Player One !", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
			errorCount++;
		}
		
		typePlayerTwo = ( keyboardMousePalyerTwo[0].isSelected() ? 0 : 1 );
		if ( typePlayerOne == typePlayerTwo ) {
			error.showMessageDialog(GameInfoPanel.this, "Player Two can't have the same key listenner as Player One !", "Unvalid Task", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
			errorCount++;
		}
		
		return ( errorCount == 0 ? true : false );
	}
	
}
