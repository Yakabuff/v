package org.vclient.v;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;

import org.vclient.v.modules.ModuleIRC;

public class IRCListener implements Runnable {

	
	String server, nick, channel, login, password;
	Socket socket;
	BufferedWriter writer;
	BufferedReader reader;
	V V;
	
	public IRCListener(V V, String server, String nick, String channel, BufferedWriter writer, BufferedReader reader, String password)
	{
		this.V = V;
		this.server = server;
		this.nick = nick;
		this.channel = channel;
		this.login = nick;
		this.writer = writer;
		this.reader = reader;
		this.password = password;
	}
	
	void send(String str)
	{
		try 
		{
			writer.write(str + "\r\n");
			writer.flush();
		} catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	void join(String user)
	{
		try
		{
			
		this.send("NICK " + user);
		this.send("USER " + user + " 8 * : V Client");
	       
		String line = null;
				
		while ((line = reader.readLine()) != null) 
		{
			if (line.indexOf("004") >= 0) 
			{
				V.italicMessage("You are now logged in.");
				this.nick = user;
				V.getModule(ModuleIRC.class).nick = nick;
				break;
			}
			else if (line.indexOf("433") >= 0) 
			{
				V.italicMessage("Username is already in use! Trying with a new name...");
				join(user + "|V");
				return;
			}
		}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		
        
		try {
			   
			V.italicMessage("Logging in with password " + password);
			
			String line = null;
			
			join(this.nick);
			
			this.send("JOIN " + channel + " " + password);
			V.getModule(ModuleIRC.class).connected = true;
			
			V.italicMessage("Joined channel " + channel);

			while((line = reader.readLine()) != null) 
			{
				if(line.indexOf("475") >= 0 && line.contains(":Cannot join channel (+k)"))
				{
					V.italicMessage("Never mind, invalid password! Try again.");
					return;
				}
				else
				if (line.startsWith("PING ")) 
				{
					this.send("PONG " + line.substring(5));
				}
				else 
				{
					String message = line;
					System.out.print(line + "\n");
					if(line.split(" ")[1].equals("PRIVMSG"))
					{
						int index = message.indexOf("!");
						String username = line.substring(1, index);
						String recv = line.split(" ")[2];
						String msg = line.split(" ", 4)[3].substring(1);
						if(recv.equals(this.channel))	
							V.message("\247e\247l<" + username + ">\247r\247e " + msg);
						else
						{
							V.message("\247e\247l" + username + " whispers\247r\247e: " + msg);
							V.getModule(ModuleIRC.class).lastWhisper = username;
						}
					}
					else if(line.split(" ")[1].equals("353"))
					{
						V.message("\247lOnline:\247r " + line.split(":")[2]);
					}
					else if(line.split(" ")[1].equals("332"))
					{
						V.message(line.split(":")[2].replaceAll("1", "\247"));	
					}
					else if(line.split(" ")[1].equals("432"))
					{
						V.errorMessage("Nick reserved for services");

					}
					else if(line.split(" ")[1].equals("433"))
					{
						V.errorMessage("Nick already in use");
					}
					else if((line.split(" ")[1]).equals("JOIN"))
					{
						
						String message2 = line;
						int index = message2.indexOf("!");
						String username = line.substring(1, index);
						V.message("\247e\247o" + username + " joined the channel");
					}
					else if((line.split(" ")[1]).equals("PART"))
					{
						String message2 = line;
						int index = message2.indexOf("!");
						String username = line.substring(1, index);
						V.message("\247e\247o" + username + " left the channel");
					}
					else if((line.split(" ")[1]).equals("NICK"))
					{
						
						String message2 = line;
						int index = message2.indexOf("!");
						String username = line.substring(1, index);
						String new_name = line.split(" ", 3)[2].substring(1);
						
						if(username.equals(this.nick))
						{
							this.nick = new_name;
							V.getModule(ModuleIRC.class).nick = nick;
							V.message("\247eYOU are now known as " + new_name);
						}
						else
							V.message("\247e" + username + " is now known as " + new_name);
					}
					else if((line.split(" ")[1]).equals("NOTICE"))
					{
						V.message("*** " + line.split(" ", 4)[3].substring(1));
					}

				}
	
			}
			
		   } catch (Exception e)
		   {
			   e.printStackTrace();
		   }
	}
	

}
