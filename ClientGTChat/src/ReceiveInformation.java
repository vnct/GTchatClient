import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.TextMessage;
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


	public ReceiveInformation(JTextArea textAll,String brokerUrl) throws JMSException {
		String jmsAddress = "tcp://"+brokerUrl+":61616";
		messageConsumer = new JMSMessageConsumer(jmsAddress, "TOPIC.TOPIC");
		this.chatBox = textAll;
	}




	@Override
	public void run() {

		while(true)
		{
			Boolean done=false;
			while(!done)
			{
				try {
					Thread.sleep(100);
					TextMessage string = messageConsumer.receiveMessage();
					if(string!=null)
					{
						SocketCommunication socketCommunication = new SocketCommunication();
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
							// update list des users
							break;
						default:
							break;
						}
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
	
	private void addStringToChatBox( final String message) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {

				chatBox.append(message);
			}
		});
	}

}