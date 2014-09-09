package org.vclient.v.modules;

import org.vclient.v.Command;
import org.vclient.v.V;

import net.minecraft.client.Minecraft;


public class ModuleBreakMask extends ModuleBase 
{
	public int id;
	
	public ModuleBreakMask(V V, Minecraft mc) 
	{
		super(V, mc);
		// TODO Auto-generated constructor stub
		this.name = "BreakMask";
		this.command = new Command(this.V, this, aliases, "Only allows you to break blocks of the given type.");
		this.command.registerArg("id", new Class[] { Integer.class }, "The only ID you want to break.");
		this.defaultArg = "id";
		
		this.id = 4;
	}

	
	@Override
	public void processArguments(String name, String argv[])
	{
		if(name.equals("id"))
		{
			this.id = Integer.parseInt(argv[0]);
			this.V.confirmMessage("Now masking block: " + Integer.toString(id));
		}
	}

	@Override
	public String getMetadata()
	{
		return  ("(" + Integer.toString(this.id) + ")");
	}
}
