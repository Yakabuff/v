package org.vclient.v.events;

import net.minecraft.entity.player.EntityPlayer;

public class PlayerLeaveVisualRangeEvent {

	EntityPlayer player;
	
	public PlayerLeaveVisualRangeEvent(EntityPlayer player)
	{
		this.player = player;
	}
	
	public EntityPlayer getPlayer()
	{
		return this.player;
	}
}
