package org.vclient.v.events;

public abstract class EventCancellable
{
    private boolean isCancelled;
    
    public EventCancellable() {
        final boolean isCancelled = false;
        this.isCancelled = isCancelled;
    }
    
    public void cancel() {
        this.isCancelled = true;
    }
    
    public boolean getCancelled() {
        return this.isCancelled;
    }
}
