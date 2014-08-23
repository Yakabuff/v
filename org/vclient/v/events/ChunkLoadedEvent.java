package org.vclient.v.events;

import org.vclient.v.Location;
import org.vclient.v.SimpleBlock;

public class ChunkLoadedEvent {

	public int x, z;
	public Location loc;
	
	public ChunkLoadedEvent(int x, int z)
	{
		this.x = x;
		this.z = z;
		this.loc = new Location((double)x, 0, (double)z);
	}
	
}
