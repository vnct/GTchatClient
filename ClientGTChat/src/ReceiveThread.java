import java.io.IOException;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.irc.socket.SocketCommunication;
import com.irc.socket.SocketInformation;
import com.irc.socket.SocketMessage;
import com.irc.socket.SocketMessageType;

public class ReceiveThread extends Thread {
	
	private JTextArea chatBox;
	private SocketInformation socketInformation ;
	
	public ReceiveThread(JTextArea textAll) {
		
		this.chatBox = textAll;
	}
	

	public ReceiveThread(JTextArea textAll, SocketInformation _socketInformation) {
		socketInformation = _socketInformation;
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
					SocketCommunication socketCommunication = new SocketCommunication();
					SocketMessage socketMessage = socketCommunication.convertStringtoSocketMessage(socketInformation.getStreamIn().readUTF());
					if(!socketMessage.getNicknameExpediteur().equals(socketInformation.getNickname()))
					{
						String textToDisplay = socketMessage.getNicknameExpediteur()+">"+socketMessage.getMessageContent();
						addStringToChatBox(textToDisplay+ "\n");
					}
						
							
					// TODO open & close connection to sent a message
				} catch (IOException e) {
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