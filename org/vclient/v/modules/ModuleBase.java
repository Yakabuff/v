package org.vclient.v.modules;

import java.util.ArrayList;

import org.vclient.v.Command;
import org.vclient.v.IModule;
import org.vclient.v.V;

import net.minecraft.client.Minecraft;

public class ModuleBase implements IModule {

	V V;
	public ArrayList<String> aliases;
	public String name;
	public String defaultArg;
	public Command command;
	public boolean isEnabled;
	public boolean needsTick;
	public boolean needsRendererTick;
	public boolean needsBotTick;
	public boolean showEnabled;
	
	// If false, this will essentially just act as a command.
	public boolean isToggleable;
	
	Minecraft mc;
	
	public ModuleBase(V V, Minecraft mc) 
	{
		this.V = V;
		this.isEnabled = false;
		this.mc = mc;
		this.isToggleable = true;
		this.needsRendererTick = false;
		this.needsBotTick = false;
		this.defaultArg = "";
		
		aliases = new ArrayList<String>();
		
		this.command = new Command(this.V, this, aliases);
		needsTick = false;
		showEnabled = true;
		
		this.name = this.getClass().getSimpleName().replaceFirst("Module", "").toLowerCase();
		aliases.add(name);
		
		this.V.moduleCache.put(this.name, this);
	}
	
	public void onEnable()
	{
		this.isEnabled = true;
	}
	
	public void onDisable()
	{
		this.isEnabled = false;
	}
	
	public void toggleState() {
		if(this.isEnabled)
			this.onDisable();
		else
			this.onEnable();
	}
	
	
	public void onTick()
	{
		
	}
	
	public void onRendererTick()
	{
		
	}
	
	public void onBotTick()
	{
		
	}
	
	public void processArguments(String name, String argv[])
	{
	}
	
	public String getMetadata()
	{
		return "";
	}
	
	public String getName()
	{
		return this.name;
	}

}
