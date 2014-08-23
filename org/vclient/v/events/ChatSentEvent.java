package org.vclient.v.events;


public class ChatSentEvent {

	String message;
	
	public ChatSentEvent(String message)
	{
		this.message = message;
	}
	
	public String getMessage()
	{
		return this.message;
	}
}
