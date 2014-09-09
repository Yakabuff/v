package org.vclient.v.modules;

import org.vclient.v.Command;
import org.vclient.v.MarkovBot;
import org.vclient.v.V;

import net.minecraft.client.Minecraft;

public class ModuleMarkov extends ModuleBase 
{	
	MarkovBot bot;
	
	public ModuleMarkov(V V, Minecraft mc) 
	{
		super(V, mc);
		// TODO Auto-generated constructor stub
			
		bot = new MarkovBot(mc);
		
		this.command = new Command(this.V, this, aliases, "Markov chain mad bot");
		this.command.registerArg("load", new Class[] {String.class}, "Load a file");
		this.command.registerArg("rand", new Class[] {}, "Rand");
		this.command.registerArg("say", new Class[] {String.class}, "Rand");
		
		this.defaultArg = "say";

	}

	@Override
	public void processArguments(String name, String argv[]) {
		if(name.equals("load")) {

			bot.addStringsFromFile(argv[0]);

		} else if(name.equals("say")) {
			
			mc.thePlayer.sendChatMessage(bot.getSentence(argv[0]));
			
		} else if(name.equals("rand")) {
			mc.thePlayer.sendChatMessage(bot.getSentence());
			
		}
	}

}
