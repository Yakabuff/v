package org.vclient.v.modules;

import org.vclient.v.Command;
import org.vclient.v.V;

import net.minecraft.client.Minecraft;

public class ModuleTimer extends ModuleBase 
{
	
	float speed;
	
	public ModuleTimer(V V, Minecraft mc) 
	{
		super(V, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = false;
		
		this.command = new Command(this.V, this, aliases, "Broken by update; changes tick speed");
		this.command.registerArg("interval", new Class[] { Float.class }, "Tick speed");
		this.defaultArg = "interval";
		this.isToggleable = false;
		this.speed = 1.0F;
	}
	
	
	@Override
	public void processArguments(String name, String argv[])
	{
		try
		{
			this.speed = Float.parseFloat(argv[0]);
		}
		catch (NumberFormatException e)
		{
			V.errorMessage("That isn't a number, you imbecile.");
		}

		mc.timer.timerSpeed = speed;
	}
	
	@Override
	public String getMetadata()
	{
		return this.speed == 1.0F ? "" :  "(" + Float.toString(this.speed) + ")";
	}

}
