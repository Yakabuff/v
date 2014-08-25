package org.vclient.v.modules;

import org.vclient.v.Command;
import org.vclient.v.Vapid;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;

public class ModuleRecover extends ModuleBase 
{
	
	public ModuleRecover(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = true;
		this.aliases.add("recover");
		
		this.command = new Command(this.vapid, this, aliases, "Puts out fire on your player and takes away all negative potion effects.");
	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled) {
			if(canPlayerRecover()) {
				for (short s = 0; s <= 30; s = (short)(s + 1))
			    {
			      mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(mc.thePlayer.onGround));
			    }
			}
		}
		
	}
	
	public boolean canPlayerRecover() {
		return ((mc.thePlayer.isBurning()
				&& !mc.thePlayer.isPotionActive(Potion.fireResistance)
				&& !mc.thePlayer.isImmuneToFire() && mc.thePlayer.onGround
				&& !mc.thePlayer.isInWater()
				&& !mc.thePlayer.isInsideOfMaterial(Material.lava) && !mc.thePlayer
				.isInsideOfMaterial(Material.fire)) || (mc.thePlayer.isPotionActive(Potion.poison)) || mc.thePlayer.isPotionActive(Potion.weakness)) || mc.thePlayer.isPotionActive(Potion.moveSlowdown);
	}

}
