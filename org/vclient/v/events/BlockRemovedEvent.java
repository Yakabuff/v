package org.vclient.v.events;

import org.vclient.v.Location;

public class BlockRemovedEvent {

	int id;
	Location location;
	
	public BlockRemovedEvent(int id, Location location)
	{
		this.id = id;
		this.location = location;
	}
	
	public int getBlockId()
	{
		return this.id;
	}
	
	public Location getBlockLocation()
	{
		return this.location;
	}
}
