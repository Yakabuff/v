package org.vclient.v.modules;

import org.lwjgl.input.Keyboard;
import org.vclient.v.Command;
import org.vclient.v.V;

import net.minecraft.client.Minecraft;

public class ModuleStep extends ModuleBase 
{
	
	public ModuleStep(V V, Minecraft mc) 
	{
		super(V, mc);
		// TODO Auto-generated constructor stub		
		
		this.needsTick = true;
		this.command = new Command(this.V, this, aliases, "Automatically steps up blocks.");
	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled)
		{
			if(mc.theWorld != null && mc.thePlayer != null) {
				if(mc.thePlayer.onGround && mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isInWater()) {
					mc.thePlayer.boundingBox.offset(0.0D, 1.0628, 0.0D);
					mc.thePlayer.motionY = -1.067;
					mc.thePlayer.isCollidedHorizontally = false;
				}
			}
		}
	}

	private boolean isAir() {
        return mc.theWorld.isAirBlock((int) mc.thePlayer.posX, (int) mc.thePlayer.posY, (int) mc.thePlayer.posZ);
    }
}
