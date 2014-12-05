
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.component.csv.CSVAction;
import com.irc.socket.SocketCommunication;
import com.irc.socket.SocketInformation;
import com.irc.socket.SocketMessage;
import com.irc.socket.SocketMessageType;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;



public class LoginFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private JPanel contentPane = new JPanel();
	private JComboBox boxAddr = new JComboBox();
	private JTextField textPort = new JTextField();;
	private JTextField textNickName = new JTextField();
	private JButton btnLogin = new JButton("Login");
	private JLabel lblPort = new JLabel("Port :");
	private JLabel lblAddressIp = new JLabel("IP Address :");
	private JLabel lblNickname = new JLabel("Nickname :");
	private CSVAction actionConfiguration ;
	/**
	 * Create the frame.
	 */
	public LoginFrame() {
		
		// Creation de frame principal
		this.setTitle("Login");
		this.setSize(300, 180);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		//contentPane.setBackground(Color.white);
		
		actionConfiguration = new CSVAction();
		actionConfiguration.setFilename("configurationFile");
		actionConfiguration.setStringsTitleCSV(new String[]{"IP","PORT"});
		actionConfiguration.createFile();
	
		
		 //Quitter l'application
		this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e){
                  int reponse = JOptionPane.showConfirmDialog(null,
                                       "Voulez-vous quitter l'application",
                                       "Confirmation",
                                       JOptionPane.YES_NO_OPTION,
                                       JOptionPane.QUESTION_MESSAGE);
                  if (reponse==JOptionPane.YES_OPTION){
                      dispose();  
                  }
            }
		});
		
		
		
		contentPane.setLayout(null);

		// Creation de la partie addresse IP
		
		JPanel topip = new JPanel();
		topip.setBounds(10, 5, 264, 30);
		topip.setLayout(null);
		
		lblAddressIp.setBounds(10, 8, 79, 14);
		topip.add(lblAddressIp);
		
		boxAddr.setBounds(110, 5, 100, 20);
		boxAddr.setPreferredSize(new Dimension(100, 20));
		List<String> list = loadConfiguration();
		for(String st : list)
		{
			boxAddr.addItem(st);
		}
		/*boxAddr.addItem("127.0.0.1");
		boxAddr.addItem("192.168.160.152");
		boxAddr.addItem("192.168.160.131");*/
		
		
		topip.add(boxAddr);
		
		boxAddr.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				textPort.setText(String.valueOf(loadPort(boxAddr.getSelectedItem().toString())));
				
			}
		});
		
		contentPane.add(topip);
		
		// Creation de la partie PORT de connection 
		JPanel topport = new JPanel();
		topport.setBounds(10, 40, 264, 30);
		topport.setLayout(null);
		lblPort.setBounds(10, 8, 47, 14);
		topport.add(lblPort);
		
		textPort.setBounds(110, 5, 100, 20);
		textPort.setColumns(10);
		//textPort.setText("80");
		topport.add(textPort);
		
		
		contentPane.add(topport);
		
		// Creation de la partie Nickname
		JPanel topnick = new JPanel();
		topnick.setBounds(10, 75, 264, 30);
		topnick.setLayout(null);
		
		lblNickname.setBounds(10, 8, 72, 14);
		topnick.add(lblNickname);
		
		textNickName.setBounds(110, 5, 100, 20);
		textNickName.setColumns(10);
		//textNickName.setText("admin");
		topnick.add(textNickName);
		
		contentPane.add(topnick);
		
		JPanel topval = new JPanel();
		topval.setBounds(10, 110, 264, 30);
		topval.setLayout(null);
		
		
		this.getRootPane().setDefaultButton(btnLogin);
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				//Password password = new Password(LoginFrame.this);
				
				SocketInformation socketInformation = null;
				try {
					socketInformation = new SocketInformation(new Socket(getIp(), getPort()),getIp());
					Boolean pseudo_available = true;
					Boolean done = false;
					Boolean admin_status = false;
					while(!done)
					{
						try{
							String nickname = getNameUser();
							socketInformation.setNickname(nickname);
							SocketCommunication communication = new SocketCommunication();
						
							SocketMessage message = communication.convertStringtoSocketMessage(communication.receiveMessage(socketInformation.getStreamIn()));
							System.out.println(message.getMessageContent());
							switch (message.getMessageType()) {
							case INFO_PSEUDO:
								communication.sendMessage(new SocketMessage(true,"Serveur", nickname, nickname, SocketMessageType.INFO_PSEUDO), socketInformation.getStreamOut());
								break;
							case INFO_PSEUDO_EXIST:
								pseudo_available = false;
								done=true;
								break;
							case INFO_ADMIN:
								admin_status = true;
								done=true;
								System.out.println("I'm in admin status");
								//communication.sendMessage(new SocketMessage("Serveur", "ADMIN", nickname, SocketMessageType.INFO_ADMIN), socketInformation.getStreamOut());
								break;	
							case INFO_SUCCESS:
								
								pseudo_available = true;
								done=true;
								break;	
							default:
								System.out.println("DEFAULT"  + message.getMessageContent());
								break;
							}
						}
						catch(IOException ioException)
						{
							
						}
						
					}
					if(!pseudo_available)
					{
						textNickName.setBackground(Color.red);
					}
					else if(admin_status)
					{
						Password password = new Password(LoginFrame.this, socketInformation);
						System.out.println("ADMIN STATUS");
						password.setVisible(true);
						setVisible(false);
					}
					else
					{
						ChatPrFrame chatprframe = new ChatPrFrame(LoginFrame.this, socketInformation);
						//if (textNickName.getText().equalsIgnoreCase("Admin")){
						//password.setVisible(true);
						//}else{Z
						chatprframe.setVisible(true);
						setVisible(false);
					}
						
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				//}
			}

		
		});
		
		
		btnLogin.setBounds(130, 0, 82, 20);
		topval.add(btnLogin);
		
		
		contentPane.add(topval);
		
		this.setContentPane(contentPane);
		
			
	}
	
	public List<String> loadConfiguration()
	{
		List<String[]> strings = actionConfiguration.getCSV();
		List<String> listAddress = new ArrayList<String>();
		if(strings.size()==0)
		{
			actionConfiguration.appendfile(new String[]{"127.0.0.1","80"});
			strings = actionConfiguration.getCSV();
		}
		for(String[] st : strings)
		{
			try{
				listAddress.add(st[0]);
			}
			catch(Exception exception)
			{
				
			}
		}
		return listAddress;
	}
	public Integer loadPort(String ipaddress)
	{
		List<String[]> strings = actionConfiguration.getCSV();
		for(String[] st : strings)
		{
			try{
				if(st[0].equals(ipaddress))
					return Integer.valueOf(st[1]);
			}
			catch(Exception exception)
			{
				
			}
		}
		return 0;
	}
	
	
		public String getIp(){
			return boxAddr.getSelectedItem().toString();
			
		}
		
		public int getPort(){
			return  Integer.parseInt(textPort.getText());
		}
		
		public String getNameUser(){
			return textNickName.getText();
		}
}
