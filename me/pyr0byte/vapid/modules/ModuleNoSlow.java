package me.pyr0byte.vapid.modules;

import me.pyr0byte.vapid.Command;
import me.pyr0byte.vapid.Vapid;
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

		this.name = "No Slow";
		
		this.command = new Command(this.vapid, this, aliases, "Prevents you from slowing down while eating, shooting a bow, walking in soul sand, etc.");
	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled)
			fastLadder();
			skipLadder();
	}
	
	@Override
	public void onEnable()
	{
		this.isEnabled = true;
		fastIce();
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

	private void skipLadder() {
		if (!mc.thePlayer.isOnLadder()
				&& mc.gameSettings.keyBindForward.pressed
				&& ((mc.theWorld.getBlock((int) mc.thePlayer.posX, (int) mc.thePlayer.boundingBox.minY + 1, (int) mc.thePlayer.posZ) == Blocks.ladder || (mc.theWorld.getBlock((int) mc.thePlayer.posX, (int) mc.thePlayer.boundingBox.minY + 1, (int) mc.thePlayer.posZ) == Blocks.vine)))) {
			mc.thePlayer.setSprinting(false);
			mc.thePlayer.motionY = 0.26F;
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
