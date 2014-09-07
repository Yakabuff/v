package org.vclient.v.modules;

import org.vclient.v.Command;
import org.vclient.v.Vapid;

import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;

public class ModuleNoSlow extends ModuleBase 
{
	public double ladderSpeed = 2.89D;
	
	public ModuleNoSlow(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
		aliases.add("ns");

		this.needsTick = true;
		this.name = "NoSlow";
		
		this.command = new Command(this.vapid, this, aliases, "Prevents you from slowing down while eating, shooting a bow, walking in soul sand, etc.");
	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled)
		{
			fastLadder();
			fastIce();
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
		normalIce();
	}
	
	private void fastLadder() {
		if (mc.thePlayer.isOnLadder()) {
			if (mc.gameSettings.keyBindForward.pressed) {
				if (!mc.thePlayer.capabilities.isFlying) {
					mc.thePlayer.motionY = 0.4F;
					mc.thePlayer.motionY = 0.1D * ladderSpeed;
				}else{
					mc.thePlayer.motionZ = 0.15D * ladderSpeed;
				}
			}
		}
	}

	private void fastIce() {
		Blocks.ice.slipperiness = 0.39F;
		Blocks.packed_ice.slipperiness = 0.39F;
	}

	private void normalIce() {
		Blocks.ice.slipperiness = 0.98F;
		Blocks.packed_ice.slipperiness = 0.98F;
	}

	private boolean canSpeed() {
		return ((!(mc.thePlayer.moveForward == 0.0F)
				&& (!mc.thePlayer.isSneaking()) && mc.thePlayer.getFoodStats()
				.getFoodLevel() > 6) && (!mc.thePlayer.isCollidedHorizontally));
	}

}
