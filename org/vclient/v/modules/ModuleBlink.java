package org.vclient.v.modules;

import java.util.ArrayList;
import java.util.Iterator;

import org.vclient.v.Command;
import org.vclient.v.V;
import org.vclient.v.annotations.EventHandler;
import org.vclient.v.events.PacketSendEvent;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

public class ModuleBlink extends ModuleBase 
{
	
    private ArrayList delayedPackets;
    private double startZ;
    private double startY;
    private double startX;
    private EntityOtherPlayerMP blinkEntity;
    private double timer;
	
	public ModuleBlink(V V, Minecraft mc) 
	{
		super(V, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = true;
		this.command = new Command(this.V, this, aliases, "Synthetic lagswitch.");
		this.delayedPackets = new ArrayList();
	}

	@Override
	public void onDisable()
	{
		this.isEnabled = false;
		if (this.blinkEntity != null) {
			mc.theWorld.removeEntityFromWorld(-1);
			this.blinkEntity = null;
		}
        final Iterator<Packet> iterator = this.delayedPackets.iterator();
        while (iterator.hasNext()) {
            final Packet a;
            if ((a = iterator.next()) != null) {
                mc.thePlayer.sendQueue.addToSendQueue(a);
            }
        }
        this.delayedPackets.clear();
	}
	
	@Override
	public void onEnable()
	{
		this.isEnabled = true;
		if (mc.theWorld != null) {
			final double timer = 0;
			this.blinkEntity = new EntityOtherPlayerMP(mc.theWorld, mc.session.func_148256_e());
	        this.startX = mc.thePlayer.posX;
	        this.startY = mc.thePlayer.posY;
	        this.startZ = mc.thePlayer.posZ;
			this.blinkEntity.inventory = mc.thePlayer.inventory;
			this.blinkEntity.setPosition(this.startX, mc.thePlayer.boundingBox.minY, this.startZ);
			mc.theWorld.addEntityToWorld(-1, this.blinkEntity);
			this.timer = timer;
		}
	}
	
    @EventHandler
    public void onPacketSend(PacketSendEvent e) {
    	if(this.isEnabled)
		{
    		++timer;
    		if ((mc.thePlayer.motionY >= -0.2D) && (mc.thePlayer.motionZ == 0.0D) && (mc.thePlayer.motionX == 0.0D)) {
    			e.cancel();
    		}
	        if (e.getPacket() instanceof C03PacketPlayer || e.getPacket() instanceof C08PacketPlayerBlockPlacement || e.getPacket() instanceof C07PacketPlayerDigging) {
	            this.delayedPackets.add(e.getPacket());
	            e.cancel();
	        }
		}
    }
    
    private String getTimeElapsed()
    {
        final int n = (int)this.timer / 20;
        String displayName = "Time(s): " + n;
        return displayName;
    }
    
    @Override
	public String getMetadata()
	{
    	String ret = "(" + getTimeElapsed() + ")";
    	return ret;
	}
}
