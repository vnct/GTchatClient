package com.irc.socket;

public enum SocketMessageType {
	 	USER_CONNECT("USER_CONNECT"),
	 	USER_DISCONNECT("USER_DISCONNECT"),
	 	USER_LIST("USER_LIST"),
	 	USER_KICK("USER_KICK"),
	 	USER_NAME("USER_NAME"),
	 	MESSAGE_TEXT("MESSAGE_TEXT"),
	 	MESSAGE_ERROR("MESSAGE_ERROR"),
	 	MESSAGE_WARNING("MESSAGE_WARNING"),
	 	MESSAGE_QUIT("MESSAGE_QUIT"),
	 	INFO_PSEUDO("INFO_PSEUDO"),
	 	INFO_PSEUDO_EXIST("INFO_PSEUDO_EXIST"),
	 	INFO_ADMIN("INFO_ADMIN"),
	 	INFO_ADMIN_ERROR("INFO_ADMIN_ERROR"),
	 	INFO_SUCCESS("INFO_SUCCESS")
	    ;

	    private final String text;

	    /**
	     * @param text
	     */
	    private SocketMessageType(final String text) {
	        this.text = text;
	    }

	    /* (non-Javadoc)
	     * @see java.lang.Enum#toString()
	     */
	    @Override
	    public String toString() {
	        return text;
	    }
}
