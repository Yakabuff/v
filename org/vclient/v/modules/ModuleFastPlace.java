package org.vclient.v.modules;

import org.vclient.v.Command;
import org.vclient.v.V;

import net.minecraft.client.Minecraft;


public class ModuleFastPlace extends ModuleBase 
{
	
	public int tolerance;
	
	public ModuleFastPlace(V V, Minecraft mc) 
	{
		super(V, mc);
		// TODO Auto-generated constructor stub
		this.name = "FastPlace";	
		this.command = new Command(this.V, this, aliases, "Speeds up block placing. It can slow it down too, if you're so inclined.");
		this.command.registerArg("tolerance", new Class[] { Integer.class }, "Ticks to wait between placing blocks");
		this.defaultArg = "tolerance";
		tolerance = 0;
		
	}
	
	@Override
	public void processArguments(String name, String argv[])
	{
		if(name.equals("tolerance"))
		{
			this.tolerance = Integer.parseInt(argv[0]);
		}
	}

	@Override
	public String getMetadata()
	{
		return this.tolerance == 0F ? "" : ("(" + Integer.toString(this.tolerance) + ")");
	}
}
