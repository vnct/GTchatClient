package com.irc.socket;
public class SocketMessage {

	private Boolean privateMsg;
	private String nicknameDestinataire;
	private String messageContent;
	private String nicknameExpediteur;
	private SocketMessageType messageType;
	public SocketMessage(Boolean privateMsg, String nicknameDestinataire, String messageContent, String nicknameExpediteur,SocketMessageType messageType) {
		super();
		this.privateMsg = privateMsg;
		this.nicknameDestinataire = nicknameDestinataire;
		this.nicknameExpediteur = nicknameExpediteur;
		this.messageContent = messageContent;
		this.messageType = messageType;
	}
	
	
	
	
	




	public Boolean getPrivateMsg() {
		return privateMsg;
	}









	public void setPrivateMsg(Boolean privateMsg) {
		this.privateMsg = privateMsg;
	}









	public SocketMessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(SocketMessageType messageType) {
		this.messageType = messageType;
	}

	public String getNicknameDestinataire() {
		return nicknameDestinataire;
	}
	public void setNicknameDestinataire(String nicknameDestinataire) {
		this.nicknameDestinataire = nicknameDestinataire;
	}
	public String getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}
	public String getNicknameExpediteur() {
		return nicknameExpediteur;
	}
	public void setNicknameExpediteur(String nicknameExpediteur) {
		this.nicknameExpediteur = nicknameExpediteur;
	}
	

}
