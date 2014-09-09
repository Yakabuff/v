package org.vclient.v.modules;

import org.vclient.v.Command;
import org.vclient.v.V;

import net.minecraft.client.Minecraft;

public class ModuleAutomine extends ModuleBase 
{
	
	public ModuleAutomine(V V, Minecraft mc) 
	{
		super(V, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = true;
		this.aliases.add("mine");
		this.name = "AutoMine";
		this.command = new Command(this.V, this, aliases, "You mine");

	}

    
	@Override
	public void onDisable()
	{
		this.isEnabled = false;
		mc.gameSettings.keyBindAttack.pressed = false;

	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled && mc.leftClickCounter == 0) {
			mc.gameSettings.keyBindAttack.pressed = true;

		}
		
	}

}
