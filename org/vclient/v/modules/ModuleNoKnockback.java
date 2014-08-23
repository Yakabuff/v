package org.vclient.v.modules;

import org.vclient.v.Command;
import org.vclient.v.Vapid;

import net.minecraft.client.Minecraft;


public class ModuleNoKnockback extends ModuleBase 
{	
	public ModuleNoKnockback(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
		this.aliases.add("nk");	
		this.aliases.add("knock");		

		this.name = "No Knockback";
		
		this.command = new Command(this.vapid, this, aliases, "Prevents knockback from swords, bows, etc.");
	}
}
