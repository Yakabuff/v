package org.vclient.v.events;

import net.minecraft.entity.player.EntityPlayer;

public class PlayerEnterVisualRangeEvent {

	EntityPlayer player;
	
	public PlayerEnterVisualRangeEvent(EntityPlayer player)
	{
		this.player = player;
	}
	
	public EntityPlayer getPlayer()
	{
		return this.player;
	}
}
