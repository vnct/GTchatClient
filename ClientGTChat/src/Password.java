import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JButton;

import com.irc.socket.SocketCommunication;
import com.irc.socket.SocketInformation;
import com.irc.socket.SocketMessage;
import com.irc.socket.SocketMessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Password extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private JPanel contentPane = new JPanel();
	private JLabel lblAdminpassword = new JLabel("AdminPassWord ");
	private JPasswordField passwordField = new JPasswordField();
	private JButton btnOk = new JButton("OK");
	private SocketInformation socketInformation = null;

	/**
	 * Create the frame.
	 * @param socketInformation 
	 */
	public Password(final LoginFrame loginframe, SocketInformation _socketInformation) {

		
		this.setTitle("Password");
		this.setSize(300, 150);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		socketInformation = _socketInformation;
		
		contentPane.setLayout(null);

		JPanel toptitle = new JPanel();
		toptitle.setBounds(10, 5, 264, 30);
		toptitle.setLayout(null);
		lblAdminpassword.setBounds(82, 11, 110, 14);
		toptitle.add(lblAdminpassword);

		contentPane.add(toptitle);

		JPanel toppass = new JPanel();
		toppass.setBounds(10, 40, 264, 30);
		toppass.setLayout(null);
		passwordField.setBounds(89, 5, 86, 20);
		passwordField.setColumns(10);
		toppass.add(passwordField);

		contentPane.add(toppass);

		JPanel topval = new JPanel();
		topval.setBounds(10, 75, 264, 30);
		topval.setLayout(null);

		
		this.getRootPane().setDefaultButton(btnOk);
		
		btnOk.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) {

				Boolean connexionStatus = false;
				SocketCommunication socketCommunication = new SocketCommunication();
				try {
					socketCommunication.sendMessage(new SocketMessage("Serveur", String.copyValueOf(passwordField.getPassword()), loginframe.getNameUser(), SocketMessageType.INFO_ADMIN), socketInformation.getStreamOut());
					SocketMessage message = socketCommunication.convertStringtoSocketMessage(socketCommunication.receiveMessage(socketInformation.getStreamIn()));
					System.out.println(message.getMessageContent());
					switch (message.getMessageType()) {
					case INFO_SUCCESS:
						System.out.println("Success");
						connexionStatus= true;
						break;
					case INFO_ADMIN_ERROR:
						System.out.println("Mdp Error");
						break;


					default:
						break;
					}
					if(connexionStatus)
					{
						ChatPrFrame chatprframe = new ChatPrFrame(loginframe,socketInformation);
						chatprframe.setVisible(true);
						setVisible(false);
					}
					else
					{
						setVisible(false);
						loginframe.setVisible(true);
					}
					
					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (Throwable e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		btnOk.setBounds(102, 0, 63, 23);
		topval.add(btnOk);

		contentPane.add(topval);

		this.setContentPane(contentPane);
	}


}
