package ImgChooser;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ImgChooserFrame extends JFrame {

	private Container pane;
	
	private JButton OkayButton;
	private ImgChooserPanel imgChooserPanel;
	
	private JScrollPane scrollPane;
	
	
	public ImgChooserFrame () {
		super( "Image Chooser");
		
		setSize ( new Dimension(600, 400) );
		
		pane = getContentPane();
		pane.setLayout(new BorderLayout());
		
		/// adding imgChooserPanel
		imgChooserPanel = new ImgChooserPanel();
		scrollPane = new JScrollPane(imgChooserPanel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		//scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		pane.add(scrollPane, BorderLayout.CENTER);
		
		
		JPanel OkayPanel = new JPanel();
		OkayButton = new JButton("   Ok  ");
		
		OkayPanel.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
	
		gc.fill = GridBagConstraints.NONE;
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.gridx = 0;
		gc.gridy = 0;
		gc.weightx = 0;
		gc.weighty = 0;
		gc.insets = new Insets(0, 0, 20, 10);
		OkayPanel.add(OkayButton, gc);
	
		add(OkayPanel, BorderLayout.SOUTH);
		
		
		OkayButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		
		
	}
	
	
	
	
	
	
	
	
	
	///// getter and setter methods 
	public ImgChooserPanel getImgChooserPanel() {
		return imgChooserPanel;
	}
	public void setImgChooserPanel(ImgChooserPanel imgChooserPanel) {
		this.imgChooserPanel = imgChooserPanel;
	}
	
	
	
	
}