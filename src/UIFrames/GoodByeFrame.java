package UIFrames;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;

public class GoodByeFrame extends JFrame {

	private GoodByePanel goodPanel;
	
	
	public GoodByeFrame() {
		super("gameOver");
		
		setBounds(100, 100, 800, 600);
		
		setLayout(new BorderLayout());
		
		goodPanel = new GoodByePanel();
		add(goodPanel, BorderLayout.CENTER);
		goodPanel.repaint();
		this.revalidate();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
