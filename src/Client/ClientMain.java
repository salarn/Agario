package Client;

import java.awt.EventQueue;

import javax.sound.sampled.Clip;

import UIFrames.ClientFindingIpFrame;

public class ClientMain  {

	private static Clip clip;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				
				ClientFindingIpFrame findIp = new ClientFindingIpFrame(clip);
			}
		});
	}

}







	



































//        private JTextField ipTextField;
//        private JTextField portTextField;
//          private JPanel    contentPane;
//
//        private Client client;
//        Thread statusThread = null;
//        private JTextField messageTextField;
//		contentPane = new JPanel();
//		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
//		setContentPane(contentPane);
//		contentPane.setLayout(new BoxLayout(contentPane,  BoxLayout.Y_AXIS));
//
//		JPanel textPanel = new JPanel();
//		textPanel.setPreferredSize(new Dimension(0, 400));
//		contentPane.add(textPanel);
//		textPanel.setLayout(new BorderLayout(0, 0));
//
//		JLabel lblNewLabel = new JLabel("Status");
//		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
//		textPanel.add(lblNewLabel, BorderLayout.NORTH);
//
//		final DefaultListModel <String> statusListModel = new DefaultListModel<>();
//		final JList <String> statusList = new JList <String> ( statusListModel );
//		textPanel.add(new JScrollPane(statusList), BorderLayout.CENTER);
//
//		JPanel commandPanel = new JPanel();
//		commandPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));		
//		commandPanel.setPreferredSize(new Dimension(0, 50));
//		contentPane.add(commandPanel);
//		commandPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
//		
//		JLabel lblIp = new JLabel("Ip");
//		commandPanel.add(lblIp);
//		
//		ipTextField = new JTextField();
//		ipTextField.setText("127.0.0.1");
//		commandPanel.add(ipTextField);
//		ipTextField.setColumns(10);
//		
//		
//		JLabel lblPort = new JLabel("Port");
//		commandPanel.add(lblPort);
//		
////		portTextField = new JPasswordField();
//		portTextField = new JTextField();
//		portTextField.setText("12345");
//		commandPanel.add(portTextField);
////		JPasswordField myPasswordField = new JPasswordField();
////		myPasswordField.setEchoChar('*');
////		myPasswordField.setText("12345");
////		commandPanel.add(myPasswordField);
//		portTextField.setColumns(10);
//		
//		
//		
//		final JButton btnStart = new JButton("Start");
//		commandPanel.add(btnStart);
//		
//		final JPanel messagePanel = new JPanel();
//		messagePanel.setBorder(new LineBorder(new Color(0,0,0)));
//		commandPanel.add(messagePanel);
//		messagePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
//		
//		JLabel lblMessage = new JLabel("Messeage");
//		messagePanel.add(lblMessage);
//		
//		messageTextField = new JTextField();
//		messagePanel.add(messageTextField);
//		messageTextField.setColumns(10);
//		
//		final JButton btnSend = new JButton("Send");
//		btnSend.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				client.send(messageTextField.getText());
//			}
//		});
//		messagePanel.add(btnSend);
//		btnSend.setEnabled(false);
//		
//		
//		final ClientToMainBuff buff = new ClientToMainBuff();
//		
//		btnStart.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				if ( btnStart.getText().compareTo("Start") == 0 ){
//						String ip = ipTextField.getText();
//						int port = 12345;
//						try {
//							port = Integer.parseInt(portTextField.getText());
//						} catch (Exception e) {
//							port = 12345;
//						}
//						
//						client = new Client(ip, port, buff);
//						
//						statusThread = new Thread(new Runnable() {
//							@Override
//							public void run() {
//								while ( true ){
//									String str = buff.pick();
//									statusListModel.addElement(str);
//									revalidate();
//								}
//							}
//						});
//						
//						client.start();
//						statusThread.start();
//						
//						btnSend.setEnabled(true);
//						btnStart.setText("Stop");
//						revalidate();
//						
//				} else {
//					btnSend.setEnabled(false);
//					client.send("Command,end");
//					buff.put("client manually stopped.");
//					statusThread.stop();
//					if ( buff.isEmpty() == false )
//						statusListModel.addElement("client manually stopped!");
//					buff.clear();
//					statusListModel.addElement("client stopped.");
//					btnStart.setText("Start");
//					revalidate();
//				}
//			}
//		});