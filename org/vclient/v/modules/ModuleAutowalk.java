package org.vclient.v.modules;

import org.vclient.v.Command;
import org.vclient.v.V;

import net.minecraft.client.Minecraft;

public class ModuleAutowalk extends ModuleBase 
{
	
	public ModuleAutowalk(V V, Minecraft mc) 
	{
		super(V, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = true;
		this.aliases.add("walk");
		this.name = "AutoWalk";
		this.command = new Command(this.V, this, aliases, "as if you were holding \"W\"");

	}

	@Override
	public void onDisable()
	{
		this.isEnabled = false;
		mc.gameSettings.keyBindForward.pressed = false;

	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled) {
			mc.gameSettings.keyBindForward.pressed = true;
		}
		
	}

}
