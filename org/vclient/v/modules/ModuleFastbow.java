package org.vclient.v.modules;

import org.vclient.v.Command;
import org.vclient.v.V;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class ModuleFastbow extends ModuleBase 
{
	
	public ModuleFastbow(V V, Minecraft mc) 
	{
		super(V, mc);
		// TODO Auto-generated constructor stub		
		this.needsTick = true;		
		this.command = new Command(this.V, this, aliases, "No delay on shooting bow");
	}
	
	  public void onTick()
	  {
	    if (this.isEnabled)
	    {
	      if ((this.mc.thePlayer.isUsingItem()) && 
	        ((this.mc.thePlayer.getHeldItem().getItem() instanceof ItemBow))) {
	        shootBow();
	        this.mc.thePlayer.stopUsingItem();
	      }
	    }
	  }

	  public void shootBow()
	  {
	    int item = this.mc.thePlayer.inventory.currentItem;
	    this.mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(-1, -1, -1, 255, this.mc.thePlayer.inventory.getCurrentItem(), 0.0F, 0.0F, 0.0F));
	    this.mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(item));
	    for (int i = 0; i < 20; i++)
	    {
	      this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
	    }
	    this.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(5, 0, 0, 0, -1));
	  }
    
}
