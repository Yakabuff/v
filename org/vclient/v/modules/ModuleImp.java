package org.vclient.v.modules;

import org.vclient.v.Command;
import org.vclient.v.V;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S18PacketEntityTeleport;


public class ModuleImp extends ModuleBase 
{	
	public ModuleImp(V V, Minecraft mc) 
	{
		super(V, mc);
		// TODO Auto-generated constructor stub
		aliases.add("tp");
		
		this.command = new Command(this.V, this, aliases, "Teleports you to a coordinate set");
		this.command.registerArg("do", new Class[] { String.class, String.class }, "Coordinates x, y, z respectively");
		this.defaultArg = "do";
	}

	@Override
	public void onEnable() {}
	
	@Override
	public void onDisable() {}
	
	@Override
	public void processArguments(String name, String argv[])
	{
		if(name.equals("do"))
		{
			String username = argv[0];
			String msg = argv[1];
			
			String p = "";
			
			p += this.V.getModule(ModuleGreet.class).toFull("(((((((mmmmmmmmmmmmmmmmmmmmmmmmmmmm");
			p += "<" + username + "> " + msg;
			
			mc.thePlayer.sendChatMessage(p);
		}
	}
}
