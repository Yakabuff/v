package org.vclient.v.events;


public class ChatReceivedEvent {

	String message;
	
	public ChatReceivedEvent(String message)
	{
		this.message = message;
	}
	
	public String getMessage()
	{
		return this.message;
	}
}
