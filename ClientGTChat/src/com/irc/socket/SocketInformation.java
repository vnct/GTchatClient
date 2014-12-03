package com.irc.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class SocketInformation {

	private Socket socket = null;
	private DataInputStream streamIn =null;
	private DataOutputStream streamOut = null;
	private String nickname ;
	private String url;
	
	
	public SocketInformation(Socket _socket,String _url) {
		super();
		this.socket = _socket;
		this.url = _url;
			try {
			start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	 public DataInputStream getStreamIn() {
		return streamIn;
	}
	public void setStreamIn(DataInputStream streamIn) {
		this.streamIn = streamIn;
	}
	public DataOutputStream getStreamOut() {
		return streamOut;
	}
	public void setStreamOut(DataOutputStream streamOut) {
		this.streamOut = streamOut;
	}
	public void stop() throws IOException
	{
		if (socket != null)    socket.close();
		if (streamIn != null)  streamIn.close();
		if (streamOut != null)  streamOut.close();
	}
	public void start() throws IOException
	   {  
		   streamIn   = new DataInputStream(socket.getInputStream());
		  
	      streamOut = new DataOutputStream(socket.getOutputStream());
	   }
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
