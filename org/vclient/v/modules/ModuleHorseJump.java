package org.vclient.v.modules;

import net.minecraft.client.Minecraft;

import org.vclient.v.Command;
import org.vclient.v.V;

public class ModuleHorseJump extends ModuleBase 
{
	
	public ModuleHorseJump(V V, Minecraft mc) 
	{
		super(V, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = true;
		this.aliases.add("horse");
		this.aliases.add("hj");
		this.name = "HorseJump";	
		this.command = new Command(this.V, this, aliases, "Max jump height everytime when jumping with a horse.");
	}
	
	@Override
	public void onEnable()
	{
		this.isEnabled = true;
	}

	@Override
	public void onDisable()
	{
		this.isEnabled = false;

	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled) {
			mc.thePlayer.horseJumpPower = 1.0F;
		}
	}
}
