package org.vclient.v.modules;

import org.vclient.v.Command;
import org.vclient.v.V;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;


public class ModuleFreecam extends ModuleBase 
{
	double x, y, z;
	float yaw, pitch;
	
	public ModuleFreecam(V V, Minecraft mc) 
	{
		super(V, mc);
		// TODO Auto-generated constructor stub
		
		this.command = new Command(this.V, this, aliases, "frees your camera");
		this.command.registerArg("noclip", new Class[] {}, "pass through blocks?");
	}

	@Override
	public void onEnable() 
	{
		
		x = mc.thePlayer.posX;
		y = mc.thePlayer.posY;
		z = mc.thePlayer.posZ;
		yaw = mc.thePlayer.rotationYaw;
		pitch = mc.thePlayer.rotationPitch;
		
		// We will have to deal with no marker entities for now...
		
		//EntityOtherPlayerMP cam = new EntityOtherPlayerMP(mc.theWorld, mc.thePlayer.getCommandSenderName());
		//cam.inventory.copyInventory(mc.thePlayer.inventory);
		//cam.setPositionAndRotation(mc.thePlayer.posX, mc.thePlayer.posY - 1.5D, mc.thePlayer.posZ, 180, 0);
		
		//mc.theWorld.addEntityToWorld(13337, cam);
		V.getModule("fly").onEnable();
		
		this.isEnabled = true;
	}
	
	@Override
	public void onDisable() 
	{
		mc.thePlayer.setPosition(x,  y,  z);
		mc.thePlayer.rotationPitch = pitch;
		mc.thePlayer.rotationYaw = yaw;
		
		//mc.theWorld.removeEntityFromWorld(13337);
		V.getModule("fly").onDisable();

		this.isEnabled = false;
	}
	
}
