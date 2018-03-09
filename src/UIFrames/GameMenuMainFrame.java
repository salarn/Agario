package UIFrames;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;


public class GameMenuMainFrame extends JFrame {
	
	private Container pane;
	
	
	
	private GameDemoPanel gameDemoPanel;
	
	private GameInfoPanel gameInfoPanel; 
	
	
	
	
	public GameMenuMainFrame () {
		super( "Game Menu");
		
		setSize ( new Dimension(400, 600) );
		
		pane = getContentPane();
		pane.setLayout(new BorderLayout());
		
		
		
		gameDemoPanel = new GameDemoPanel();
		gameInfoPanel = new GameInfoPanel( this );
		
		
		pane.add(gameDemoPanel, BorderLayout.NORTH);
		pane.add(gameInfoPanel, BorderLayout.CENTER);
		
		
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}



	/// getter and setter Methods
	public GameInfoPanel getGameInfoPanel() {
		return gameInfoPanel;
	}

	public void setGameInfoPanel(GameInfoPanel gameInfoPanel) {
		this.gameInfoPanel = gameInfoPanel;
	}
	
	
	
	
	
	
}
