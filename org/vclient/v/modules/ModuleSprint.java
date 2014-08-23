package org.vclient.v.modules;

import org.vclient.v.Command;
import org.vclient.v.Vapid;

import net.minecraft.client.Minecraft;

public class ModuleSprint extends ModuleBase 
{
	
	public ModuleSprint(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = true;
		
		this.command = new Command(this.vapid, this, aliases, "you're always sprinting");
		
	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled) 
		{
			if(mc.thePlayer.moveForward > 0 && !mc.thePlayer.isSneaking())
				mc.thePlayer.setSprinting(true);

		}
		
	}

}


