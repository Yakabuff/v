package org.vclient.v.modules;

import org.vclient.v.Command;
import org.vclient.v.V;

import net.minecraft.client.Minecraft;

public class ModuleYaw extends ModuleBase 
{
	
	public float yaw;
	
	public ModuleYaw(V V, Minecraft mc) 
	{
		super(V, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = true;
		this.aliases.add("yaw");
		this.yaw = 0F;
		
		this.command = new Command(this.V, this, aliases, "Broken, but not functionally. Locks your yaw to the closest 45 degrees.");
		this.command.registerArg("deg", new Class[] {Float.class}, "This is the part that doesn't work. Ignore it.");
		
		this.defaultArg = "deg";

	}
	
	@Override
	public void onEnable()
	{
		this.isEnabled = true;
		this.yaw = Math.round((mc.thePlayer.rotationYaw + 1F) / 45F) * 45F;
	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled) {
			mc.thePlayer.rotationYaw = this.yaw;
		}
		
	}
	
	@Override
	public void processArguments(String name, String[] argv)
	{
		if(name.equals("deg"))
		{
			this.yaw = Float.parseFloat(argv[0]);
		}
	}
	
	@Override
	public String getMetadata()
	{
		return "(" + Float.toString(this.yaw) + ")";
	}

}
