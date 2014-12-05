import java.io.IOException;
import java.util.HashMap;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import com.component.csv.CSVAction;
import com.irc.socket.SocketCommunication;
import com.irc.socket.SocketInformation;
import com.irc.socket.SocketMessage;
import com.irc.socket.SocketMessageType;

public class ReceiveThread extends Thread {
	
	private JTextArea chatBox;
	private SocketInformation socketInformation ;
	private ChatPrFrame chatPrFrame;
	private CSVAction action ;
	

	public ReceiveThread(JTextArea textAll) {
		
		this.chatBox = textAll;
	}
	

	public ReceiveThread(JTextArea textAll, SocketInformation _socketInformation,CSVAction _CsvAction) {
		socketInformation = _socketInformation;
		
		action = _CsvAction;

		this.chatBox = textAll;
	}

	public ReceiveThread(ChatPrFrame _chatPrFrame, JTextArea textAll,
			SocketInformation _socketInformation,CSVAction _CsvAction) {
		chatPrFrame = _chatPrFrame;
		socketInformation = _socketInformation;
		action = _CsvAction;
		this.chatBox = textAll;
	}

	public void AddPrivateUser(String string,ChatPriveFrame chatPriveFrame)
	{
		chatPrFrame.getHashMap().put(string, chatPriveFrame);
	}
	public void DeletePrivateUser(String string,ChatPriveFrame chatPriveFrame)
	{
		chatPrFrame.getHashMap().remove(string);
	}
	public void ClosePrivateUser()
	{
		chatPrFrame.getHashMap().clear();
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
					System.out.println(socketMessage.getMessageContent() + socketMessage.getMessageType());
					if(!socketMessage.getNicknameExpediteur().equals(socketInformation.getNickname()))
					{
						switch (socketMessage.getMessageType()) {
							case MESSAGE_TEXT:
								action.appendfile(socketCommunication.convertSocketMessagetoStringTab(socketMessage));
								String textToDisplay = socketMessage.getNicknameExpediteur()+">"+socketMessage.getMessageContent();
								if(!socketMessage.getPrivateMsg())
								{
									addStringToChatBox(textToDisplay+ "\n");
								}
								else
								{
									if(chatPrFrame.getHashMap().containsKey(socketMessage.getNicknameExpediteur()))
									{
										addStringToPrivateBox(socketMessage.getNicknameExpediteur(),textToDisplay + "\n");
										
									}
									else
									{
										chatPrFrame.createTabPrivate(socketMessage.getNicknameExpediteur());
										addStringToPrivateBox(socketMessage.getNicknameExpediteur(),textToDisplay + "\n");
										
									}
								}
								
								break;
							case MESSAGE_QUIT:
								System.out.println("MESSAGE_QUIT");
								
								chatPrFrame.closeConnection();
							
								break;
							default:
								break;
							}
						
						
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
	public void addStringToPrivateBox(final String user,final String message) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {

				chatPrFrame.getHashMap().get(user).getTextPrive().append(message);
			}
		});
	}
	
	private void addStringToChatBox( final String message) {
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {

				chatBox.append(message);		}
		});
	}

}