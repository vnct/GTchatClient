
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.TrayIcon.MessageType;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.imageio.ImageIO;
import javax.jms.JMSException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ChatPrFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JMenuBar menuBar = new JMenuBar();
	private JMenu fichier = new JMenu("File");
	private JMenu edition = new JMenu("Edit");

	private JMenuItem item1 = new JMenuItem("Close");
	private JMenuItem item2 = new JMenuItem("About");
	
	
	private JPanel contentPane = new JPanel();
	private JScrollPane scrollPane = new JScrollPane();
	private JScrollPane scrollPane_1 = new JScrollPane();
	private JScrollPane scrollPane_2 = new JScrollPane();
	private JButton btnSend = new JButton("SEND");

	private JTextArea textListPers = new JTextArea();
	private JTextArea textClient = new JTextArea();
	private MyTextArea textAll = new MyTextArea();
	private SocketInformation socketInformation = null;
	private ReceiveThread tSocket = null;
	private ReceiveInformation tTopic = null;
	private CSVAction actionHistorique ;

	public String[] userlist = new String[]{};

	private JList list = null;
	
	private HashMap<String, ChatPriveFrame> hashMap;

	private LoginFrame loginframe;
	/**
	 * Create the frame.
	 */
	
	
	
	
	
	public ChatPrFrame(final LoginFrame _loginframe, SocketInformation _socketInformation) {
		socketInformation = _socketInformation;
		actionHistorique = new CSVAction();
		actionHistorique.setFilename("historiqueFile");
		actionHistorique.setStringsTitleCSV(new String[]{"Private","Type","Destinataire","Expediteur","Text"});
		actionHistorique.createFile();
	
		hashMap = new HashMap<String, ChatPriveFrame>();
		loginframe = _loginframe;
		this.setTitle("GTChat - " + _socketInformation.getNickname());
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setBounds(100, 100, 450, 315);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		
		this.fichier.add(item1);
		this.edition.add(item2);
		item1.addActionListener(new ActionListener(){
		      public void actionPerformed(ActionEvent arg0) {
	          	  closeConnection();
	          	  dispose();
	          	  loginframe.disable();  
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
            	dialogClose();
            }
		});
		
		ImageIcon icone = new ImageIcon("./Imag/Logo_2.png");
		this.setIconImage(icone.getImage());
	
		
		// Organiser la JFRAME
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		
		contentPane.setLayout(null);
				
		this.getRootPane().setDefaultButton(btnSend);
		
		
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				send_msg();
			}
		});
		textClient.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				
				
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER)
					send_msg();
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			

		});
		reloadHistorique("***");
		
		btnSend.setBounds(263, 205, 65, 40);
		contentPane.add(btnSend);
		
	
		
		scrollPane.setBounds(5, 5, 298, 178);
		textAll.setEditable(false);
		File img=new File("./Imag/Logo.png");
		
		//textAll.setOpaque(false);

		
				//textAll.setOpaque(false);
		//scrollPane.setBackground(Color.black);
		//textAll.setBackground();
		
		
		
        try {
            Image image = ImageIO.read(img);
            if (image != null)
                textAll.setBackgroundImage(image);
            textAll.repaint();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
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
					ChatPriveFrame chatprive = new ChatPriveFrame(getFrame(),socketInformation,selected,actionHistorique);
					tSocket.AddPrivateUser(selected, chatprive);
					chatprive.setVisible(true);
					System.out.println(selected);
				}else{
				//	System.out.println("essaye encore !!!!");
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
			tTopic = new ReceiveInformation(this,list,textAll, socketInformation.getUrl(),socketInformation.getNickname());
			tTopic.start();
		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
	}
	
	
	public void reloadHistorique(String user)
	{
		List<String[]> strings = actionHistorique.getCSV();
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
				// TODO : CLOSE
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
		ChatPriveFrame chatprive = new ChatPriveFrame(this,socketInformation,expediteur,actionHistorique);
		tSocket.AddPrivateUser(expediteur, chatprive);
		chatprive.setVisible(true);
	}


	public HashMap<String, ChatPriveFrame> getHashMap() {
		return hashMap;
	}


	public void setHashMap(HashMap<String, ChatPriveFrame> hashMap) {
		this.hashMap = hashMap;
	}
	private void send_msg( )
	{
		String msgToSend = send().replace(">", "").trim();
		SocketCommunication socketCommunication = new SocketCommunication();
		SocketMessage socketMessage = null;
		SocketMessageType type = SocketMessageType.MESSAGE_TEXT;
		if(msgToSend.contains("!kick"))
		{
			//System.out.println("Changememnt du type");
			type = SocketMessageType.USER_KICK;
		}
		else
		{
		//	textAll.append(socketInformation.getNickname() + ">" + msgToSend+ "\n");
			if(!msgToSend.equals(""))
				textAll.append(socketInformation.getNickname() + ">" + msgToSend+ "\n");
		}
		try {
			socketMessage = new SocketMessage(false,"***", msgToSend, socketInformation.getNickname(), type);
			actionHistorique.appendfile(socketCommunication.convertSocketMessagetoStringTab(socketMessage));
			socketCommunication.sendMessage(socketMessage, socketInformation.getStreamOut());
			//System.out.println(socketMessage.getMessageType());
		} catch (IOException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		}
		
		textClient.setText("");
		
	}
	
	public void dialogClose()
	{
		int reponse = JOptionPane.showConfirmDialog(null,
                "Disconnect ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
		if (reponse==JOptionPane.YES_OPTION){
			closeAll();
		}
	}
	
	public void dialogServerQuit(String message,String title)
	{
		
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
		closeAll();
	//	dispose();
		//loginframe.disable();  
		
		//this.disable();
		//System.exit(0);
		
	}
	public void closeAll()
	{
		closeConnection();
		dispose();
		loginframe.disable();  
		
		this.disable();
		
	}
	public ChatPrFrame getFrame()
	{
		return this;
	}
	
	/**************************************************************************************/
	static class MyTextArea extends JTextArea {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Image backgroundImage;
 
        public MyTextArea() {
            super();
            setOpaque(false);
        }
 
        public void setBackgroundImage(Image image) {
            this.backgroundImage = image;
            this.repaint();
        }
 
        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(getBackground());
            g.fillRect(0, 0, getWidth(), getHeight());
 
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, this);
            }
 
            super.paintComponent(g);
        }
    }
	/**************************************************************************************/
	
	

	
}
