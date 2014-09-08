package org.vclient.v.events;

import net.minecraft.network.Packet;

public class PacketSendEvent extends EventCancellable
{
    private Packet packet;
    
    public PacketSendEvent(final Packet a) {
        this.packet = a;
    }
    
    public void setPacket(final Packet a) {
        this.packet = a;
    }
    
    public Packet getPacket() {
        return this.packet;
    }
}
