package org.vclient.v.modules;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.vclient.v.Command;
import org.vclient.v.IRCListener;
import org.vclient.v.V;
import org.vclient.v.annotations.EventHandler;
import org.vclient.v.events.ChatSentEvent;

import net.minecraft.client.Minecraft;


public class ModuleIRC extends ModuleBase
{
	
	Thread client;
	public boolean connected;
	final String filename = "default_irc.vpd";
	boolean flagToMakeYouMad;
	public String lastWhisper;
	
	public ModuleIRC(V V, Minecraft mc) 
	{
		super(V, mc);
		// TODO Auto-generated constructor stub
		
		this.isToggleable = true;
		this.needsTick = true;
		this.flagToMakeYouMad = false;
		this.name = "IRC";
		this.lastWhisper = "";
		
		this.command = new Command(this.V, this, aliases, "IRC");
		this.command.registerArg("connect", new Class[] { String.class, String.class, String.class }, "Server, channel, password");
		this.command.registerArg("default", new Class[] { String.class, String.class, String.class }, "Server, channel, password; starts on login");

		this.defaultArg = "connect";
		this.connected = false;
	}
	
	public String server, nick, channel, login, password;
	Socket socket;
	BufferedWriter writer;
	BufferedReader reader;
	Thread listener;
	
	@Override
	public void onTick()
	{
		if(!flagToMakeYouMad)
		{
			File f = new File(filename);
			if(f.exists())
			{
				String str[] = V.read(filename).split(" ");
				this.setup(str[0], str[1], str[2]);
			}
			flagToMakeYouMad = true;
		}
	}
	
	public void setup(String server, String channel, String password)
	{
		this.server = server;
		int port = 6667;
		
		String[] parts = server.split(":");
		if(parts.length > 1)
			port = Integer.parseInt(parts[1]);
		
		this.nick = mc.thePlayer.getCommandSenderName();
		this.channel = channel;
		this.login = nick;
		this.password = password;
		
		try 
		{
			this.socket = new Socket(server, port);
			this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));	
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	    listener = new Thread(new IRCListener(V, server, nick, channel, writer, reader, password));
	    listener.start();
	    //this.connected = true;
	    
	}
 
	@EventHandler
	public boolean onMessageSent(ChatSentEvent e)
	{
		String m = e.getMessage();

		
		if(!this.connected && ( (m.startsWith("/msg ") || m.startsWith("/re ") || (m.startsWith("/s ") || (this.isEnabled && !m.startsWith("/"))) || m.startsWith("/c ") || m.startsWith("/names"))))
		{
			V.errorMessage("You have to connect to a channel first. -irc server #channel");
			return false;
		}
		

		if(m.startsWith("/nick "))
		{
			if(m.substring(6).equals(this.nick))
			{
				V.errorMessage("That's already your nick");
				return false;
			}
			
			this.command("NICK " + m.substring(6));	
			return false;
		}
		else if(m.startsWith("/names"))
		{
			this.command("NAMES " + channel);	
			return false;
		} 
		else if(m.startsWith("/msg "))
		{
			String[] parts = m.split(" ", 3);
			if(parts.length < 3)
			{
				V.errorMessage("/msg to_user your_message");
				return false;
			}
			
			String to = parts[1];
			String msg = parts[2];
			
			
			this.command("PRIVMSG " + to + " :" + msg);	
			V.message("\247e\247lto " + to + "\247r\247e: " + msg);
			return false;
				
		}
		else if(m.startsWith("/re "))
		{
			if(this.lastWhisper.equals(""))
			{
				V.errorMessage("Nobody to reply to!");
				return false;
			}
			
			String to = this.lastWhisper;
			String msg = m.substring(4);
			
			this.command("PRIVMSG " + to + " :" + msg);	
			V.message("\247e\247lto " + to + "\247r\247e: " + msg);
			return false;
		}
		else if(m.startsWith("/s ") || (this.isEnabled && !m.startsWith("/")))
		{
			this.send(this.isEnabled ? m : m.substring(3));
			V.message("\247e\247l<" + this.nick + ">\247r\247e " + (this.isEnabled ? m : m.substring(3)));
			return false;

		}
		
		return true;
	}
	
	@Override
	public void processArguments(String name, String argv[])
	{
		if(name.equals("connect"))
		{
			if(this.connected)
			{
				this.command("QUIT");
				listener.stop();
				this.connected = false;
			}
			
			this.setup(argv[0], argv[1], argv[2]);
		}
		else
		if(name.equals("quit"))
		{
			this.command("QUIT");
			listener.stop();
			this.connected = false;
		}
		else
		if(name.equals("default"))
		{
			V.write(filename, argv[0] + " " + argv[1] + " " + argv[2]);
			V.confirmMessage("Default server set to: " + argv[0] + " " + argv[1] + " " + argv[2]);
			
			if(this.connected)
			{
				this.command("QUIT");
				listener.stop();
				this.connected = false;
			}
			
			this.setup(argv[0], argv[1], argv[2]);
		}
		
	}
	
	public void send(String msg)
	{
		try {

	       writer.write("PRIVMSG " + channel + " :" + msg + "\r\n");
	       writer.flush( );

		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void command(String msg)
	{
		try {

	       writer.write(msg + "\r\n");
	       writer.flush( );

		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
