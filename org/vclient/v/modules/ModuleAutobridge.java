package org.vclient.v.modules;

import org.vclient.v.Command;
import org.vclient.v.V;

import net.minecraft.client.Minecraft;

public class ModuleAutobridge extends ModuleBase 
{
	
	public ModuleAutobridge(V V, Minecraft mc) 
	{
		super(V, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = true;
		this.aliases.add("bridge");
		this.name = "AutoBridge";
		this.command = new Command(this.V, this, aliases, "as if you were holding S, Shift, and Right Click");

	}

   
	@Override
	public void onDisable()
	{
		this.isEnabled = false;
		mc.gameSettings.keyBindBack.pressed = false;
		mc.gameSettings.keyBindUseItem.pressed = false;
		mc.gameSettings.keyBindSneak.pressed = false;
	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled) {
			mc.gameSettings.keyBindBack.pressed = true;
			mc.gameSettings.keyBindUseItem.pressed = true;
			mc.gameSettings.keyBindSneak.pressed = true;

		}
		
	}

}
