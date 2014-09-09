package org.vclient.v.modules;

import org.vclient.v.Command;
import org.vclient.v.V;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class ModuleFastbowTristan extends ModuleBase 
{
	
	public ModuleFastbowTristan(V V, Minecraft mc) 
	{
		super(V, mc);
		// TODO Auto-generated constructor stub		
		this.needsTick = true;
		this.name = "Fastbow (v.t)";
		
		this.command = new Command(this.V, this, aliases, "Turns your bow into a chain gun!");
	}
	
	@Override
	public void onTick() {
			if (this.isEnabled && (mc.thePlayer.getCurrentEquippedItem() != null)) {
				if ((mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow) && mc.thePlayer.onGround) {
					mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
					for (int x = 0; x <40; x++) {
						mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
					}
					mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(5, 0, 0, 0, 255));
				}
			}
	}
    
}
