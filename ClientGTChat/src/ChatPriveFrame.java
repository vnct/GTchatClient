
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

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
	
	
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenu fichier = new JMenu("Fichier");
	private JMenu edition = new JMenu("Edition");

	private JMenuItem item1 = new JMenuItem("Fermer");
	private JMenuItem item2 = new JMenuItem("A propos...");
	
	private JPanel contentPane = new JPanel();
	private JScrollPane scrollPane = new JScrollPane();
	private JButton btnSend = new JButton("Send");
	private JScrollPane scrollPane_1 = new JScrollPane();
	private JTextArea textClient = new JTextArea();
    private JTextArea textPrive = new JTextArea();
    private String dest;
    private SocketInformation socketInformation = null;
    private CSVAction csvAction = null;
    private ChatPrFrame chatPrFrame = null;


	/**
	 * Create the frame.
	 */
	
	public ChatPriveFrame(ChatPrFrame _chatPrFrame,SocketInformation _socketInformation,String _dest,final CSVAction action) {
		
		socketInformation = _socketInformation;
		dest = _dest;
		csvAction= action;
		chatPrFrame = _chatPrFrame;
		this.setTitle(_dest);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setBounds(100, 100, 450, 315);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		

		this.fichier.add(item1);
		this.edition.add(item2);
		
		item1.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent arg0) {
            	  dispose(); 
		        }        
		      });
		

		this.menuBar.add(fichier);
		this.menuBar.add(edition);
		

	    this.setJMenuBar(menuBar);
		/*
		 * si on quitte l'application
		 * 
		 */
		
		this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                  int reponse = JOptionPane.showConfirmDialog(null,
                                       "Voulez-vous fermer cette conversation ?",
                                       "Confirmation",
                                       JOptionPane.YES_NO_OPTION,
                                       JOptionPane.QUESTION_MESSAGE);
                  if (reponse==JOptionPane.YES_OPTION){
                	  for(Entry<String, ChatPriveFrame> entry : chatPrFrame.getHashMap().entrySet()) {
                		    String cle = entry.getKey();
                	
                		    // traitements
                		}
                	  chatPrFrame.getHashMap().remove(dest);
                	  System.out.println("Je remove");
                	  for(Entry<String, ChatPriveFrame> entry : chatPrFrame.getHashMap().entrySet()) {
              		    String cle = entry.getKey();
              	
              		    // traitements
              		}
                	  dispose();   
                	  
                	  
                  }
            }
		});
		
		this.getRootPane().setDefaultButton(btnSend);
		
		contentPane.setLayout(null);
		
		this.getRootPane().setDefaultButton(btnSend);
		
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendMessage();
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
					sendMessage();
				
			}
		});
		
		textClient.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER){
					
					sendMessage();
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
				
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
		List<String[]> strings1 = new ArrayList<String[]>();
		if(strings.size()>100)
		{
			strings1 = strings.subList(strings.size()-100, strings.size());
		}
		else
		{
			strings1 = strings;
		}
		Integer i=0;
		for(String[] my_line : strings1)
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
	
	private void sendMessage()
	{
		String msgToSend = send().replace(">", "").trim();
		SocketCommunication socketCommunication = new SocketCommunication();
		SocketMessage socketMessage = null;
		SocketMessageType type = SocketMessageType.MESSAGE_TEXT;
		if(!msgToSend.equals(""))
			textPrive.append(socketInformation.getNickname() + " > " + msgToSend+ "\n");
		
		try {
			socketMessage = new SocketMessage(true,dest, msgToSend, socketInformation.getNickname(), type);
			csvAction.appendfile(socketCommunication.convertSocketMessagetoStringTab(socketMessage));
			socketCommunication.sendMessage(socketMessage, socketInformation.getStreamOut());
			//System.out.println(socketMessage.getMessageType());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		textClient.setText("");
	}
	
	

}
