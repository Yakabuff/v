package org.vclient.v.events;

import org.vclient.v.Location;
import org.vclient.v.SimpleBlock;

public class ChunkUnloadedEvent {

	public int x, z;
	public Location loc;
	
	public ChunkUnloadedEvent(int x, int z)
	{
		this.x = x;
		this.z = z;
		this.loc = new Location((double)x, 0, (double)z);
	}
	
}
