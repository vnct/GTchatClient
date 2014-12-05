
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.jms.JMSException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JScrollBar;


















import com.component.csv.CSVAction;
import com.irc.socket.SocketCommunication;
import com.irc.socket.SocketInformation;
import com.irc.socket.SocketMessage;
import com.irc.socket.SocketMessageType;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


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
	private ReceiveThread tSocket = null;
	private ReceiveInformation tTopic = null;
	private CSVAction actionHistorique ;

	public String[] userlist = new String[]{};

	private JList list = null;
	
	private HashMap<String, ChatPriveFrame> hashMap;
	/**
	 * Create the frame.
	 */
	
	
	
	
	
	public ChatPrFrame(final LoginFrame loginframe, SocketInformation _socketInformation) {
		socketInformation = _socketInformation;
		actionHistorique = new CSVAction();
		actionHistorique.setFilename("historiqueFile");
		actionHistorique.setStringsTitleCSV(new String[]{"Private","Type","Destinataire","Expediteur","Text"});
		actionHistorique.createFile();
	
		hashMap = new HashMap<String, ChatPriveFrame>();
		
		this.setTitle("GTChat - " + _socketInformation.getNickname());
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
                	  closeConnection();
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
				SocketCommunication socketCommunication = new SocketCommunication();
				SocketMessage socketMessage = null;
				SocketMessageType type = SocketMessageType.MESSAGE_TEXT;
				if(msgToSend.contains("!kick"))
				{
					System.out.println("Changememnt du type");
					type = SocketMessageType.USER_KICK;
				}
				else
				{
					textAll.append(socketInformation.getNickname() + " > " + msgToSend+ "\n");
					
					
				}
				try {
					socketMessage = new SocketMessage(false,"***", msgToSend, socketInformation.getNickname(), type);
					actionHistorique.appendfile(socketCommunication.convertSocketMessagetoStringTab(socketMessage));
					socketCommunication.sendMessage(socketMessage, socketInformation.getStreamOut());
					System.out.println(socketMessage.getMessageType());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				textClient.setText("");
			}
		});

		reloadHistorique("***");
		
		btnSend.setBounds(263, 205, 65, 40);
		contentPane.add(btnSend);
		
		scrollBar.setBounds(309, 5, 17, 178);
		contentPane.add(scrollBar);
		
		scrollPane.setBounds(5, 5, 298, 178);
		textAll.setEditable(false);
		scrollPane.setViewportView(textAll);
		
		contentPane.add(scrollPane);
		
		
		
		
		list = new JList<String>(userlist);
		scrollPane_1= new JScrollPane(list);
		scrollPane_1.setBounds(334, 5, 97, 254);
		
		list.addMouseListener(new MouseListener() {	
			
			@Override
			public void mouseReleased(MouseEvent e) {}
			
			@Override
			public void mousePressed(MouseEvent e) {}
			
			@Override
			public void mouseExited(MouseEvent e) {}
			
			@Override
			public void mouseEntered(MouseEvent e) {}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					String selected = (String) list.getSelectedValue();
					ChatPriveFrame chatprive = new ChatPriveFrame(socketInformation,selected,actionHistorique);
					tSocket.AddPrivateUser(selected, chatprive);
					chatprive.setVisible(true);
					System.out.println(selected);
				}else{
					System.out.println("essaye encore !!!!");
				}
				
				
			}
		});
		
		
	
		
		contentPane.add(scrollPane_1);
		
		scrollPane_2.setBounds(6, 189, 247, 68);
		scrollPane_2.setViewportView(textClient);
		scrollPane_2.setBackground(Color.white);
		contentPane.add(scrollPane_2);
		
		
		this.setContentPane(contentPane);

	
		
		
		tSocket = new ReceiveThread(this,textAll,_socketInformation,actionHistorique) ;
		// lancement du thread
		tSocket.start();
		try {
			tTopic = new ReceiveInformation(list,textAll, socketInformation.getUrl(),socketInformation.getNickname());
			tTopic.start();
		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	
	
	public void reloadHistorique(String user)
	{
		List<String[]> strings = actionHistorique.getCSV();
		Integer i=0;
		for(String[] my_line : strings)
		{
			SocketCommunication socketCommunication = new SocketCommunication();
			SocketMessage socketMessage = socketCommunication.convertStringTabtoSocketMessage(my_line);
			
			if(socketMessage.getNicknameDestinataire().equals("***"))
			{
				i++;
				String textToDisplay = socketMessage.getNicknameExpediteur()+">"+socketMessage.getMessageContent();
				textAll.append(textToDisplay + "\n");
			}
			if(i==100)
			{
				break;
			}
			
		}
	}
	
	

	public String send(){
		return  textClient.getText();
	}
	public void closeConnection()
	{
		SocketMessage socketMessage = new SocketMessage(true,"SERVEUR", "Disconnect", socketInformation.getNickname(), SocketMessageType.MESSAGE_QUIT);
  	  	SocketCommunication socketCommunication = new SocketCommunication();
  	  	try {
				socketCommunication.sendMessage(socketMessage, socketInformation.getStreamOut());
				socketInformation.stop();
				
			} catch (IOException ex) {
				// TODO Auto-generated catch block
				ex.printStackTrace();
			}
  	  	System.out.println("closeConnection");
  	  	tSocket.stop();
  	  	if(tTopic!=null)
  		  tTopic.stop();
  	 
	}


	public JList getList() {
		return list;
	}


	public void setList(JList list) {
		this.list = list;
	}
	
	public void createTabPrivate(String expediteur)
	{
		ChatPriveFrame chatprive = new ChatPriveFrame(socketInformation,expediteur,actionHistorique);
		tSocket.AddPrivateUser(expediteur, chatprive);
		chatprive.setVisible(true);
	}


	public HashMap<String, ChatPriveFrame> getHashMap() {
		return hashMap;
	}


	public void setHashMap(HashMap<String, ChatPriveFrame> hashMap) {
		this.hashMap = hashMap;
	}
	
	

	
}
