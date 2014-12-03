
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.jms.JMSException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JScrollBar;







import com.irc.socket.SocketCommunication;
import com.irc.socket.SocketInformation;
import com.irc.socket.SocketMessage;
import com.irc.socket.SocketMessageType;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;


public class ChatPrFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private JPanel contentPane = new JPanel();
	private JScrollPane scrollPane = new JScrollPane();
	private JScrollPane scrollPane_1 = new JScrollPane();
	private JScrollPane scrollPane_2 = new JScrollPane();
	private JButton btnSend = new JButton("SEND");
	private JScrollBar scrollBar = new JScrollBar();
	private JTextArea textListPers = new JTextArea();
	private JTextArea textClient = new JTextArea();
	private JTextArea textAll = new JTextArea();
	private SocketInformation socketInformation = null;
	private Thread tSocket = null;
	private Thread tTopic = null;

	/**
	 * Create the frame.
	 */
	public ChatPrFrame(final LoginFrame loginframe,SocketInformation _socketInformation) {
		socketInformation = _socketInformation;
		this.setTitle("GTChat");
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setBounds(100, 100, 450, 300);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		/*
		 * si on quitte l'application
		 * 
		 */
		
		this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                  int reponse = JOptionPane.showConfirmDialog(null,
                                       "Voulez-vous vous deconneter ?",
                                       "Confirmation",
                                       JOptionPane.YES_NO_OPTION,
                                       JOptionPane.QUESTION_MESSAGE);
                  if (reponse==JOptionPane.YES_OPTION){
                	  SocketMessage socketMessage = new SocketMessage("SERVEUR", "Disconnect", loginframe.getNameUser(), SocketMessageType.MESSAGE_QUIT);
                	  SocketCommunication socketCommunication = new SocketCommunication();
                	  try {
	      					socketCommunication.sendMessage(socketMessage, socketInformation.getStreamOut());
	      					socketInformation.stop();
	      				} catch (IOException ex) {
	      					// TODO Auto-generated catch block
	      					ex.printStackTrace();
	      				}
                	  
                	  tSocket.stop();
                	  if(tTopic.isAlive())
                		  tTopic.stop();
                	  dispose();
                      loginframe.setVisible(true);    
                  }
            }
		});
		
		// Organiser la JFRAME
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		
		contentPane.setLayout(null);
		
		
		this.getRootPane().setDefaultButton(btnSend);
		
		
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String msgToSend = send().replace(">", "").trim();
				textAll.append(loginframe.getNameUser() + " > " + msgToSend+ "\n");
				
				SocketCommunication socketCommunication = new SocketCommunication();
				SocketMessage socketMessage = new SocketMessage("***", msgToSend, loginframe.getNameUser(), SocketMessageType.MESSAGE_TEXT);
				try {
					socketCommunication.sendMessage(socketMessage, socketInformation.getStreamOut());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				textClient.setText("");
			}
		});

		
		btnSend.setBounds(263, 205, 65, 40);
		contentPane.add(btnSend);
		
		scrollBar.setBounds(309, 5, 17, 178);
		contentPane.add(scrollBar);
		
		scrollPane.setBounds(5, 5, 298, 178);
		textAll.setEnabled(false);
		scrollPane.setViewportView(textAll);
		
		contentPane.add(scrollPane);
		
		scrollPane_1.setBounds(334, 5, 97, 254);
		textListPers.setEditable(false);
		scrollPane_1.setViewportView(textListPers);
		contentPane.add(scrollPane_1);
		
		scrollPane_2.setBounds(6, 189, 247, 68);
		scrollPane_2.setViewportView(textClient);
		scrollPane_2.setBackground(Color.white);
		contentPane.add(scrollPane_2);
		
		
		this.setContentPane(contentPane);

		
		
		
		tSocket = new ReceiveThread(textAll,_socketInformation) ;
		// lancement du thread
		tSocket.start();
		try {
			tTopic = new ReceiveInformation(textAll, socketInformation.getUrl());
			tTopic.start();
		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	
	

	public String send(){
		return  textClient.getText();
	}
	

}