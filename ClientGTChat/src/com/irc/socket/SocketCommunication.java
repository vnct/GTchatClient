package com.irc.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SocketCommunication {

	// fonction qui envoie la liste des users connectés
		public void sendMessage(SocketMessage message, DataOutputStream streamOut) throws IOException
		{
			String parseur=">";
			streamOut.writeUTF(message.getMessageType()+parseur+message.getNicknameDestinataire()+parseur+message.getNicknameExpediteur()+parseur+message.getMessageContent()); // TODO : A mettre dans un fichier properties
			streamOut.flush();
		}
		public String receiveMessage(DataInputStream streamIn) throws IOException
		{
			return streamIn.readUTF();
		}
		public SocketMessage convertStringtoSocketMessage(String msg)
		{
			try
			{
				String[] strings = msg.split(">");
				SocketMessage message = new SocketMessage(strings[1], strings[3], strings[2],SocketMessageType.valueOf(strings[0])) ;
				return message;
			}
			catch(Exception exception)
			{
				exception.printStackTrace();
			}
			return new SocketMessage("", "", "", SocketMessageType.MESSAGE_ERROR);
		
		}
		public String convertSocketMessagetoString(SocketMessage message)
		{
			String parseur=">";
			return message.getMessageType()+parseur+message.getNicknameDestinataire()+parseur+message.getNicknameExpediteur()+parseur+message.getMessageContent();
		}
}
