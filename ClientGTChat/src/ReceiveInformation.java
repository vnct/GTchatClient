import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.component.jms.JMSMessageConsumer;
import com.irc.socket.SocketCommunication;
import com.irc.socket.SocketInformation;
import com.irc.socket.SocketMessage;
import com.irc.socket.SocketMessageType;

public class ReceiveInformation extends Thread {
	
	private JTextArea chatBox;
	private JMSMessageConsumer messageConsumer;
	private JList jlist;
	private String nickname;


	public ReceiveInformation(JList _jlist, JTextArea textAll,String brokerUrl,String _nickname) throws JMSException {
		String jmsAddress = "tcp://"+brokerUrl+":61616";
		this.jlist = _jlist;
		messageConsumer = new JMSMessageConsumer(jmsAddress, "TOPIC.TOPIC");
		this.chatBox = textAll;
		this.nickname = _nickname;
	}




	@Override
	public void run() {

		while(true)
		{
			Boolean done=false;
			while(!done)
			{
				try {
				
					TextMessage string = messageConsumer.receiveMessage();
					if(string!=null)
					{
						SocketCommunication socketCommunication = new SocketCommunication();
						System.out.println("Message reçu --> " + string.getText());
						SocketMessage socketMessage = socketCommunication.convertStringtoSocketMessage(string.getText());
						switch (socketMessage.getMessageType()) {
						case USER_CONNECT:
							addStringToChatBox(socketMessage.getMessageContent() + " connected" + "\n");
							break;
						case USER_DISCONNECT:
							addStringToChatBox(socketMessage.getMessageContent() + " disconneted" + "\n");
							break;
						case USER_KICK:
							addStringToChatBox(socketMessage.getMessageContent() + " kicked by admin" + "\n");
							break;
						case USER_LIST:
							System.out.println(socketMessage.getMessageContent());
							String[] strings = socketMessage.getMessageContent().split(";");
							List<String> temp_list = new ArrayList<String>();
							
							for(String string2 : strings)
							{
								if(!string2.equals(nickname))
								{
									temp_list.add(string2);
								}
							}
							String[] temp_list_tab = new String[temp_list.size()];
							temp_list_tab = temp_list.toArray(temp_list_tab);
							System.out.println(temp_list_tab.length);
							updateList(temp_list_tab);
							break;
						default:
							break;
						}
						Thread.sleep(100);
					}
					
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// vincent stp ici tu met le message recu et tu met le string recu en argument de addStringToChatBox
	}
	
	private void updateList( final String[] userlist) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {

				jlist.setListData(userlist);
				
			}
		});
	}

	private void addStringToChatBox( final String message) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {

				chatBox.append(message);
			}
		});
	}

}