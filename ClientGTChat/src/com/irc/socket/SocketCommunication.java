package com.irc.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SocketCommunication {

	// fonction qui envoie la liste des users connectés
		public void sendMessage(SocketMessage message, DataOutputStream streamOut) throws IOException
		{
			String parseur=">";
			streamOut.writeUTF(message.getPrivateMsg()+parseur+message.getMessageType()+parseur+message.getNicknameDestinataire()+parseur+message.getNicknameExpediteur()+parseur+message.getMessageContent()); // TODO : A mettre dans un fichier properties
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
				SocketMessage message = new SocketMessage(Boolean.valueOf(strings[0]),strings[2], strings[4], strings[3],SocketMessageType.valueOf(strings[1])) ;
				return message;
			}
			catch(Exception exception)
			{
				exception.printStackTrace();
			}
			return new SocketMessage(false,"", "", "", SocketMessageType.MESSAGE_ERROR);
		
		}
		public String convertSocketMessagetoString(SocketMessage message)
		{
			String parseur=">";
			return message.getPrivateMsg()+parseur+message.getMessageType()+parseur+message.getNicknameDestinataire()+parseur+message.getNicknameExpediteur()+parseur+message.getMessageContent();
		}
		public String getNickName(String message)
		{
			try {
				
				String[] strings  =  message.split(" ");
				if(strings[0].equals("!kick"))
				{
					return strings[1];
				}
			
			} catch (Exception e) {
				// TODO: handle exception
			}
			return "";
		}
		public String[] convertSocketMessagetoStringTab(
				SocketMessage socketMessage) {
			// TODO Auto-generated method stub
		
			return new String[]{socketMessage.getPrivateMsg().toString(),socketMessage.getMessageType().toString(),socketMessage.getNicknameDestinataire(),socketMessage.getNicknameExpediteur(),socketMessage.getMessageContent()};
		}
		public SocketMessage convertStringTabtoSocketMessage(String[] strings)
		{
			try
			{
				SocketMessage message = new SocketMessage(Boolean.valueOf(strings[0]),strings[2], strings[4], strings[3],SocketMessageType.valueOf(strings[1])) ;
				return message;
			}
			catch(Exception exception)
			{
				exception.printStackTrace();
			}
			return new SocketMessage(false,"", "", "", SocketMessageType.MESSAGE_ERROR);
		
		}
}
