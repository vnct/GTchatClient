
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JScrollBar;
import javax.swing.JButton;
import javax.swing.JTextArea;

import com.component.csv.CSVAction;
import com.irc.socket.SocketCommunication;
import com.irc.socket.SocketInformation;
import com.irc.socket.SocketMessage;
import com.irc.socket.SocketMessageType;


public class ChatPriveFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private JPanel contentPane = new JPanel();
	private JScrollPane scrollPane = new JScrollPane();
	private JButton btnSend = new JButton("Send");
	private JScrollPane scrollPane_1 = new JScrollPane();
	private JTextArea textClient = new JTextArea();
    private JTextArea textPrive = new JTextArea();
    private String dest;
    private SocketInformation socketInformation = null;
    private CSVAction csvAction = null;


	/**
	 * Create the frame.
	 */
	
	public ChatPriveFrame(SocketInformation _socketInformation,String _dest,final CSVAction action) {
		
		socketInformation = _socketInformation;
		dest = _dest;
		csvAction= action;
		
		this.setTitle(_dest);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
                	  dispose();   
                  }
            }
		});
		
		this.getRootPane().setDefaultButton(btnSend);
		
		contentPane.setLayout(null);
		
		this.getRootPane().setDefaultButton(btnSend);
		
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// recuperer la string pour ce user
			}
		});
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		
		
		scrollPane.setBounds(5, 5, 429, 176);
		scrollPane.setEnabled(false);
		textPrive.setEditable(false);
		scrollPane.setViewportView(textPrive);
		contentPane.add(scrollPane);
		
		
		
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String msgToSend = send().replace(">", "").trim();
				SocketCommunication socketCommunication = new SocketCommunication();
				SocketMessage socketMessage = null;
				SocketMessageType type = SocketMessageType.MESSAGE_TEXT;
				textPrive.append(socketInformation.getNickname() + " > " + msgToSend+ "\n");
				try {
					socketMessage = new SocketMessage(true,dest, msgToSend, socketInformation.getNickname(), type);
					action.appendfile(socketCommunication.convertSocketMessagetoStringTab(socketMessage));
					socketCommunication.sendMessage(socketMessage, socketInformation.getStreamOut());
					//System.out.println(socketMessage.getMessageType());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				textClient.setText("");
			}



		});
		
		reloadHistorique(dest);
		
		btnSend.setBounds(363, 209, 71, 46);
		contentPane.add(btnSend);
		
		scrollPane_1.setBounds(5, 188, 344, 67);
		scrollPane_1.setViewportView(textClient);
		contentPane.add(scrollPane_1);
		
		
		
	}
	
	public void reloadHistorique(String user)
	{
		List<String[]> strings = csvAction.getCSV();
		Integer i=0;
		for(String[] my_line : strings)
		{
			SocketCommunication socketCommunication = new SocketCommunication();
			SocketMessage socketMessage = socketCommunication.convertStringTabtoSocketMessage(my_line);
			
			if(socketMessage.getNicknameDestinataire().equals(user))
			{
				i++;
				String textToDisplay = socketMessage.getNicknameExpediteur()+">"+socketMessage.getMessageContent();
				textPrive.append(textToDisplay + "\n");
			}
			if(i==100)
			{
				break;
			}
			
		}
	}
	
	
	
	private String send() {
		// TODO Auto-generated method stub
		return  textClient.getText();
	}
	public JTextArea getTextPrive() {
		return textPrive;
	}
	public void setTextPrive(JTextArea textPrive) {
		this.textPrive = textPrive;
	}
	
	

}
