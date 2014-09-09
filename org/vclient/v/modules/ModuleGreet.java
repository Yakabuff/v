package org.vclient.v.modules;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import org.vclient.v.Command;
import org.vclient.v.V;
import org.vclient.v.annotations.EventHandler;
import org.vclient.v.events.ChatReceivedEvent;
import org.vclient.v.events.PlayerLogOffEvent;
import org.vclient.v.events.PlayerLogOnEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerInfo;

public class ModuleGreet extends ModuleBase 
{
	
	boolean onJoin;
	boolean onLeave;
	ArrayList<String> ignoredPlayers;
	String syncWith;
	boolean syncing;
	
	String defaultGreetings[] = {"Hey", "Welcome", "Hello", "Hi", "Greetings", "Salutations", "Good to see you", "{time}"};
	String defaultGoodbyes[] = {"Later", "So long", "See you later", "Good bye", "Bye", "Farewell"};
	
	ArrayList<String> greetings;
	ArrayList<String> goodbyes;
	
	String defaultGreetingFormat = "> !!! {msg}, {player}{.}";
	String defaultGoodbyeFormat = "> ... {msg}, {player}{.}";
	
	String greetingFormat;
	String goodbyeFormat;
	
	public ModuleGreet(V V, Minecraft mc) 
	{
		super(V, mc);
		// TODO Auto-generated constructor stub
							
		this.command = new Command(this.V, this, aliases, "Greets");
		this.command.registerArg("join", new Class[] {}, "Welcome");
		this.command.registerArg("leave", new Class[] {}, "Good bye");
		this.command.registerArg("ignore", new Class[] {String.class}, "Adds a player to the ignore list");
		this.command.registerArg("unignore", new Class[] {String.class}, "Removes a player from the ignore list");
		this.command.registerArg("ignored", new Class[] {}, "Lists ignored players");
		this.command.registerArg("unignoreall", new Class[] {}, "Removes all ignored players");
	//	this.command.registerArg("syncing", new Class[] {}, "Experimental; Synchronize messages with another player");
	//	this.command.registerArg("syncwith", new Class[] {String.class}, "Experimental; Synchronize messages with another player");
		this.command.registerArg("greetadd", new Class[] {String.class}, "Add a welcome");
		this.command.registerArg("greetdel", new Class[] {String.class}, "Delete a welcome");
		this.command.registerArg("byeadd", new Class[] {String.class}, "Add a good bye");
		this.command.registerArg("byedel", new Class[] {String.class}, "Delete a good bye");
		this.command.registerArg("greetlist", new Class[] {}, "Lists greetings");
		this.command.registerArg("byelist", new Class[] {}, "Lists goodbyes");
		this.command.registerArg("byeformat", new Class[] {String.class}, "Change greeting format; {player} {msg} {.}");
		this.command.registerArg("greetformat", new Class[] {String.class}, "Change goodbye format; {player} {msg} {.}");

		this.ignoredPlayers = new ArrayList<String>();
		
		File ignored = new File("greeter_ignored.vpd");
		
		if(ignored.exists())
		{
			this.ignoredPlayers = this.V.readLines("greeter_ignored.vpd");
		}
		
		this.syncWith = null;
		
		this.syncing = false;
		this.onJoin = true;
		this.onLeave = true;
		
		
		File greetingsFile = new File("greeter_greetings.vpd");
		
		if(greetingsFile.exists())
		{
			this.greetings = this.V.readLines("greeter_greetings.vpd");
		} else {
			this.greetings = new ArrayList<String>(Arrays.asList(this.defaultGreetings));
		}

		File goodbyesFile = new File("greeter_goodbyes.vpd");
		
		if(goodbyesFile.exists())
		{
			this.goodbyes = this.V.readLines("greeter_goodbyes.vpd");
		} else {
			this.goodbyes = new ArrayList<String>(Arrays.asList(this.defaultGoodbyes));
		}
		
		this.goodbyeFormat = this.V.readOrFallback("greet_format_goodbye.vpd", defaultGoodbyeFormat);
		this.greetingFormat = this.V.readOrFallback("greet_format_greeting.vpd", defaultGreetingFormat);

	}
	
	
	public void writeIgnoredPlayers()
	{
		String filename = "greeter_ignored.vpd";
		File ignored = new File(filename);
		
		this.V.writeLines(filename, this.ignoredPlayers);
	}
	

	public String isPlayerOnline(String username)
	{
		String s;
		
		for(Object o : mc.thePlayer.sendQueue.playerInfoList)
		{
			s = ((GuiPlayerInfo)o).name;
			if(s.equalsIgnoreCase(username))
				return s;
		}
		
		return null;
	}
	
	@EventHandler
	public void onChatReceived(ChatReceivedEvent e)
	{

		// only works on servers without modified chat; not gonna mess with that.
		String player = e.getMessage().substring(0, e.getMessage().indexOf(' ')+1);
		player = player.substring(1, player.length() - 2);
		String message = e.getMessage().substring(e.getMessage().indexOf(' ')+1);
		
		if(this.syncWith != null && this.syncWith.equalsIgnoreCase(player))
		{
			if(message.matches(">\\s(!{3}|\\.{3})\\s.*,\\s.*(!|\\.)"))
			{
								
				String[] aaa = message.split(" ");
				String to = aaa[aaa.length-1];
				to = to.substring(0, to.length()-1);
				
				// 1 is greet 0 is farewell
				boolean type = aaa[1].equals("!!!") ? true : false;
				
				if(type)
					this.greeting(to);
				else
					this.farewell(to);
				
			}
		}

	}
	
	public String toFull(String s)
	{
		String ret = "";
		char[] c = s.toCharArray();
		
		for(char h : c)
		{
			
			if(h != ' ')
				ret += (char)((h | 0x10000) + 0xFEE0);
			else
				ret += ' ';
		}
		
		System.out.print(ret);
		
		return ret;
	}
	
	public void greeting(String user)
	{
		if(!(this.isEnabled && this.onJoin && !this.ignoredPlayers.contains(user.toLowerCase())))
			return;
		
		Random rand = new Random();
		int i = rand.nextInt(greetings.size());
		String period = rand.nextInt(2) > 1 ? "." : "!";
		String greet = greetings.get(i);
		
		if(greet.equals("{time}"))
		{
			Date d = new Date();
			int t = d.getHours();
			
			if(t < 11 && t > 2)
			{
				greet = "Morning";
			}
			else if(t > 11 && t < 18)
			{
				greet = "Afternoon";
			}
			else
			{
				greet = "Evening";
			}
		}
		
		String msg = this.greetingFormat.replaceAll("\\{player\\}", user)
						.replaceAll("\\{msg\\}", greet)
						.replaceAll("\\{.\\}", period);
		
		mc.thePlayer.sendChatMessage(msg);
	}
	
	public void farewell(String user)
	{
		
		if(!(this.isEnabled && this.onLeave && !this.ignoredPlayers.contains(user.toLowerCase())))
			return;
		
		Random rand = new Random();
		int i = rand.nextInt(goodbyes.size());
		String period = rand.nextInt(2) > 1 ? "." : "!";
		
		String msg = this.goodbyeFormat.replaceAll("\\{player\\}", user)
				.replaceAll("\\{msg\\}", goodbyes.get(i))
				.replaceAll("\\{.\\}", period);
		
		mc.thePlayer.sendChatMessage(msg);	
	}
	
	@EventHandler
	public void onPlayerLogIn(PlayerLogOnEvent e)
	{		
		if(e.getUsername().equals("BibleBot")) {
			mc.thePlayer.sendChatMessage("!verse Leviticus 18:32");
		}
		
		this.greeting(e.getUsername());
	}
	
	@EventHandler
	public void onPlayerLogOff(PlayerLogOffEvent e)
	{
		this.farewell(e.getUsername());
	}
	
	@Override
	public void processArguments(String name, String argv[])
	{
		if(name.equals("join"))
		{
			this.onJoin = !this.onJoin;
		}
		else if(name.equals("leave"))
		{
			this.onLeave = !this.onLeave;
		}
		else if(name.equals("ignore"))
		{
			String player = argv[0].toLowerCase();
			if(!this.ignoredPlayers.contains(player))
			{
				this.ignoredPlayers.add(player);
				this.V.confirmMessage("Ignored " + player);
				this.writeIgnoredPlayers();

			}
			else
				this.V.errorMessage("As much as you hate " + argv[0] + ", you can only ignore him once");

		}
		else if(name.equals("unignore"))
		{
			String player = argv[0].toLowerCase();
			if(this.ignoredPlayers.contains(player))
			{
				this.ignoredPlayers.remove(player);
				this.V.confirmMessage("Unignored " + player);

				this.writeIgnoredPlayers();
			}
			else
				this.V.errorMessage("You never ignored " + argv[0]);

		}
		else if(name.equals("syncing"))
		{
			this.syncing = !this.syncing;
		}
		else if(name.equals("syncwith"))
		{
			if(this.isPlayerOnline(argv[0]) != null)
			{
				this.syncWith = this.isPlayerOnline(argv[0]);
			}
			else
				this.V.errorMessage("That player isn't online :^(");
		}
		else if(name.equals("ignored"))
		{
			if(this.ignoredPlayers.isEmpty())	
			{
				this.V.errorMessage("Nobody ignored");
				return;
			}
			
			String ret = "";
			for(String s : this.ignoredPlayers)
			{
				ret += s + ", ";
			}
			
			ret = ret.substring(0, ret.length() - 2);
			this.V.confirmMessage(ret);
		}
		else if(name.equals("unignoreall"))
		{
			this.ignoredPlayers.clear();
			this.writeIgnoredPlayers();
			this.V.confirmMessage("Deleted all players from ignore list");
		}
		else if(name.equals("greetadd")) {
			this.greetings.add(argv[0]);
			this.V.writeLines("greeter_greetings.vpd", this.greetings);
			this.V.confirmMessage("Added greeting");
		}
		else if(name.equals("greetdel")) {
			if(this.greetings.contains(argv[0])) {
				this.greetings.remove(argv[0]);
				this.V.writeLines("greeter_greetings.vpd", this.greetings);
				this.V.confirmMessage("Removed greeting");
			} else {
				this.V.errorMessage("Could not remove greeting");
			}
		}
		else if(name.equals("byeadd")) {
			this.goodbyes.add(argv[0]);
			this.V.writeLines("greeter_goodbyes.vpd", this.goodbyes);
			this.V.confirmMessage("Added goodbye");
		}
		else if(name.equals("byedel")) {
			if(this.goodbyes.contains(argv[0])) {
				this.goodbyes.remove(argv[0]);
				this.V.writeLines("greeter_goodbyes.vpd", this.goodbyes);
				this.V.confirmMessage("Removed goodbye");
			} else {
				this.V.errorMessage("Could not remove goodbye");
			}
		}
		else if(name.endsWith("list")) {
			if(name.startsWith("bye")) {
				this.V.message(this.V.join(this.goodbyes, ", "));
			} else {
				this.V.message(this.V.join(this.greetings, ", "));
			}
		}
		else if(name.equals("greetformat")) {
			this.V.confirmMessage("Changed greeting format");
			this.greetingFormat = argv[0];
			this.V.write("greeter_format_greeting.vpd", this.greetingFormat);
		}
		else if(name.equals("byeformat")) {
			this.V.confirmMessage("Changed goodbye format");
			this.goodbyeFormat = argv[0];
			this.V.write("greeter_format_goodbye.vpd", this.goodbyeFormat);
		}
	}
	
	@Override
	public String getMetadata()
	{
		String ret = "";
		if(this.onJoin && this.onLeave)
		{
			ret = "(Join, Leave)";
		}
		else if(this.onJoin)
		{
			ret = "(Join)";
		}
		else if(this.onLeave)
		{
			ret = "(Leave)";
		}
		else 
		{
			ret = "(Nothing!)";
		}
		
		if(this.syncing)
		{
			ret += " [Synced: " + this.syncWith + "]";
		}
		
		return ret;
			
	}
}
