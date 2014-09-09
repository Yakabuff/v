package org.vclient.v.modules;

import org.vclient.v.Command;
import org.vclient.v.V;

import net.minecraft.client.Minecraft;


public class ModuleNoKnockback extends ModuleBase 
{	
	public ModuleNoKnockback(V V, Minecraft mc) 
	{
		super(V, mc);
		// TODO Auto-generated constructor stub
		this.aliases.add("nk");	
		this.aliases.add("knock");		

		this.name = "NoKnockback";
		
		this.command = new Command(this.V, this, aliases, "Prevents knockback from swords, bows, etc.");
	}
}
