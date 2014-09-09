package org.vclient.v.modules;

import org.vclient.v.Command;
import org.vclient.v.V;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovingObjectPosition;

public class ModuleBot extends ModuleBase 
{
	
	boolean hasKilled;
	
	public ModuleBot(V V, Minecraft mc) 
	{
		super(V, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = true;
		
		this.command = new Command(this.V, this, aliases, "A minimal bot for botting things");

	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled) {
			
			if(mc.thePlayer.isDead) {
				mc.thePlayer.respawnPlayer();
			} else if(!hasKilled) {
				mc.thePlayer.rotationPitch = 90;
				try
				{
					mc.botController.onPlayerDamageBlock(mc.objectMouseOver.blockX, mc.objectMouseOver.blockY, mc.objectMouseOver.blockZ, mc.objectMouseOver.sideHit);
				}catch(Exception ex){}
				
				if(mc.thePlayer.posY <= 6) {
					mc.thePlayer.sendChatMessage("/kill");
					mc.thePlayer.setDead();
					
					hasKilled = true;
				}
				
			} else if(hasKilled && !mc.thePlayer.isDead) {
				hasKilled = false;
			}
			
		}
		
	}

}
