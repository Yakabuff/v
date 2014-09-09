package org.vclient.v.modules;

import org.vclient.v.Command;
import org.vclient.v.V;

import net.minecraft.client.Minecraft;

public class ModuleAutorespawn extends ModuleBase 
{
	
	public ModuleAutorespawn(V V, Minecraft mc) 
	{
		super(V, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = true;
		this.aliases.add("respawn");
		this.name = "AutoRespawn";
		this.command = new Command(this.V, this, aliases, "Automatically respawns upon death");

	}

   
	@Override
	public void onTick()
	{
		if(this.isEnabled && mc.thePlayer.getHealth() <= 0)
			mc.thePlayer.respawnPlayer();
	}

}
