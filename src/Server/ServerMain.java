package Server;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import Buffers.ServerToFrameBuff;

public class ServerMain extends JFrame {

	private JPanel contentPane;
	private Server server;
	private Thread statusThread;
	private int port;
	private String name;
	private double width;
	private double height;



	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					ServerMain mainServer = new ServerMain( "AliGame", 12345, 1000, 1000 );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	public ServerMain ( String name, int port, double width, double height ) {
		this.port = port;
		this.name = name;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 550);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		JPanel textPanel = new JPanel();
		contentPane.add(textPanel);
		textPanel.setPreferredSize(new Dimension(0, 400));
		textPanel.setLayout(new BorderLayout(0, 0));

		final DefaultListModel <String> statusListModel = new DefaultListModel<>();
		final JList <String> statusList = new JList<String>(statusListModel);
		textPanel.add(new JScrollPane(statusList), BorderLayout.CENTER );

		JLabel lblStatus = new JLabel("Status");
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		textPanel.add(lblStatus, BorderLayout.NORTH);

		JPanel commandPanel = new JPanel();
		commandPanel.setPreferredSize(new Dimension(0, 40));
		contentPane.add(commandPanel);

		final JButton btnStart = new JButton("Start");
		commandPanel.add(btnStart);
		
		final JButton addBtn = new JButton("add");
		commandPanel.add(addBtn);
		addBtn.setEnabled(false);
		
		final JTextField pathField = new JTextField(20);
		final JLabel pathLbl = new JLabel("path: ");
		commandPanel.add(pathLbl);
		commandPanel.add(pathField);
		
		final JTextField nameField = new JTextField(20);
		final JLabel nameLbl = new JLabel("name: ");
		commandPanel.add(nameLbl);
		commandPanel.add(nameField);

		final ServerToFrameBuff buff = new ServerToFrameBuff();

		btnStart.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if ( btnStart.getText().compareTo("Start") == 0 ){
					server = new Server(name, port, buff, width, height);
					statusThread = new Thread(new Runnable() {
						@Override
						public void run() {
							while( true ){
								String str = buff.pick();
								statusListModel.addElement(str);
								revalidate();
							}
						}
					});
					server.listen();
					statusThread.start();

					btnStart.setText("Stop");
					addBtn.setEnabled(true);
					revalidate();

				} else {
					server.close();
					buff.put("server manually stopped.");
					statusThread.stop();
					if ( buff.isEmpty() == false ){
						statusListModel.addElement("server manually stopped!");
					}
					buff.clear();

					statusListModel.addElement("server stopped.");
					btnStart.setText("Start");
					addBtn.setEnabled(false);
					revalidate();
				}
			}
		});
		
		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					File root=new File(pathField.getText());
					URLClassLoader classLoader = new URLClassLoader(new URL[]{root.toURI().toURL()});
					Class cls = Class.forName(nameField.getText(), true, classLoader);
					server.addClass(cls);
				} catch (Exception e) {
					e.printStackTrace();
					System.err.println("add class error , !!!!!!!!!!!!!!!!!!!!!!!!");
				}
			}
		});
		
		
		setVisible(true);
	}


}























//Writer writer = null;
//try {
//	File statText = new File("statssTest.txt");
//	FileOutputStream outStream = new FileOutputStream(statText);
//	OutputStreamWriter out = new OutputStreamWriter(outStream);
//	writer = new BufferedWriter(out);
//	writer.write("Ali is a very good boy!\n");
//} catch (Exception e) {
//	e.printStackTrace();
//}
//
//try {
//	File statText = new File("statssTest.txt");
//	FileOutputStream outStream = new FileOutputStream(statText);
//	OutputStreamWriter out = new OutputStreamWriter(outStream);
//	writer.write("Ali2 is a very good boy!\n");
//	writer.close();
//} catch (Exception e) {
//	e.printStackTrace();
//}
//
//
//try {
//	File file = new File("statssTest.txt");
//	FileReader fileReader = new FileReader(file);
//	BufferedReader bufferedReader = new BufferedReader(fileReader);
//	String line;
//	while ((line = bufferedReader.readLine()) != null) {
//		System.out.println(line);
//	}
//	fileReader.close();
//} catch (IOException e) {
//	e.printStackTrace();
//}
//
//System.out.println("\n****************************************************\n");
//try {
//	File statText = new File("statssTest.txt");
//	FileOutputStream outStream = new FileOutputStream(statText);
//	OutputStreamWriter out = new OutputStreamWriter(outStream);
//	writer = new BufferedWriter(out);
//	writer.write("Ali3 is a very good boy!\n");
//	writer.close();
//} catch (Exception e) {
//	e.printStackTrace();
//}
//
//
//try {
//	File file = new File("statssTest.txt");
//	FileReader fileReader = new FileReader(file);
//	BufferedReader bufferedReader = new BufferedReader(fileReader);
//	String line;
//	while ((line = bufferedReader.readLine()) != null) {
//		System.out.println(line);
//	}
//	fileReader.close();
//} catch (IOException e) {
//	e.printStackTrace();
//}


//Writer writer = null;
//try {
//	File statText = new File("statssTest.txt");
//	FileOutputStream outStream = new FileOutputStream(statText);
//	OutputStreamWriter out = new OutputStreamWriter(outStream);
//	writer = new BufferedWriter(out);
//	writer.write("Ali is a very good boy!\n");
//} catch (Exception e) {
//	e.printStackTrace();
//}
//
//try {
//	File statText = new File("statssTest.txt");
//	FileOutputStream outStream = new FileOutputStream(statText);
//	OutputStreamWriter out = new OutputStreamWriter(outStream);
//	writer.write("Ali2 is a very good boy!\n");
//	writer.close();
//} catch (Exception e) {
//	e.printStackTrace();
//}
//
//
//try {
//	File file = new File("statssTest.txt");
//	FileReader fileReader = new FileReader(file);
//	BufferedReader bufferedReader = new BufferedReader(fileReader);
//	String line;
//	while ((line = bufferedReader.readLine()) != null) {
//		System.out.println(line);
//	}
//	fileReader.close();
//} catch (IOException e) {
//	e.printStackTrace();
//}

