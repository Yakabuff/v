package org.vclient.v.modules;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.vclient.v.Command;
import org.vclient.v.V;

import net.minecraft.client.Minecraft;

public class ModuleHit extends ModuleBase 
{
	
	public ModuleHit(V V, Minecraft mc) 
	{
		super(V, mc);
		// TODO Auto-generated constructor stub
							
		this.command = new Command(this.V, this, aliases, "Hits you");
	}
	
	@Override
	public void onEnable() {}
	
	@Override
	public void onDisable() {}
	
	@Override
	public void toggleState()
	{
		
		  mc.playerController.attackEntity(mc.thePlayer, mc.thePlayer);
		
	}

}
