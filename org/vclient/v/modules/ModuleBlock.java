package org.vclient.v.modules;

import org.vclient.v.Command;
import org.vclient.v.V;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;

public class ModuleBlock extends ModuleBase 
{
	
	int tick = 0;
	
	public ModuleBlock(V V, Minecraft mc) 
	{
		super(V, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = true;
		
		this.command = new Command(this.V, this, aliases, "you're always blocking");
		
	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled && tick % 2 == 0) 
		{
			if(mc.thePlayer.getHeldItem() != null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
				
                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
			
			}
		}
		
		tick++;
		
	}

}


