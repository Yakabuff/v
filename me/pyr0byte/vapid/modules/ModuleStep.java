package me.pyr0byte.vapid.modules;

import org.lwjgl.input.Keyboard;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
import net.minecraft.client.Minecraft;

public class ModuleStep extends ModuleBase 
{
	
	public ModuleStep(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub		
		
		this.needsTick = true;
		this.command = new Command(this.vapid, this, aliases, "Automatically steps up blocks.");
	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled)
		{
			if(mc.theWorld != null && mc.thePlayer != null) {
				mc.thePlayer.stepHeight = 0.6F;
				boolean check = !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater()
						&& mc.thePlayer.isCollidedHorizontally && this.isAir() 
						&& mc.thePlayer.onGround && !Keyboard.isKeyDown(Keyboard.KEY_SPACE);
				if(check) {
					mc.thePlayer.boundingBox.offset(0.0D, 1.0628, 0.0D);
					mc.thePlayer.motionY = -420;
					mc.thePlayer.isCollidedHorizontally = false;
				}
			}
		}
	}

	private boolean isAir() {
        return mc.theWorld.isAirBlock((int) mc.thePlayer.posX, (int) mc.thePlayer.posY, (int) mc.thePlayer.posZ);
    }
}
