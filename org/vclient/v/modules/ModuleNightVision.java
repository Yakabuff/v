package org.vclient.v.modules;

import org.vclient.v.Command;
import org.vclient.v.Vapid;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.PotionEffect;

public class ModuleNightVision extends ModuleBase 
{
	public ModuleNightVision(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
		
		this.needsTick = true;
		aliases.add("nv");
		this.name = "NightVision";
		this.command = new Command(this.vapid, this, aliases, "(Alternative to Brightness) Activate NightVision potion effect.");
	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled)
		{
			PotionEffect nightVision = new PotionEffect(16, 999999999, 0);
			nightVision.setPotionDurationMax(true);
			mc.thePlayer.addPotionEffect(nightVision);
		}
	}
	
	@Override
	public void onEnable()
	{
		this.isEnabled = true;
	}
	
	@Override
	public void onDisable()
	{
		this.isEnabled = false;
		try {
			mc.thePlayer.removePotionEffect(16);
		}catch(Exception err) {}
	}
}
