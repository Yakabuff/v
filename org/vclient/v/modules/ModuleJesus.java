package org.vclient.v.modules;

import org.vclient.v.Command;
import org.vclient.v.Vapid;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class ModuleJesus extends ModuleBase 
{
	
	private long lastMS = -1L;
	
	public ModuleJesus(Vapid vapid, Minecraft mc) 
	{
		super(vapid, mc);
		// TODO Auto-generated constructor stub
					
		this.needsTick = true;
		this.aliases.add("jesus");
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

	}
	
	@Override
	public void onTick()
	{
		if(this.isEnabled) {
			if (mc.gameSettings.keyBindJump.pressed) {
				mc.thePlayer.onGround = false;
				return;
			}
			
			if (isInLiquid()) {
				
				if (mc.thePlayer.isInsideOfMaterial(Material.air)) 
				{
					mc.thePlayer.motionY = 0.085;
				}
			}else{
				if ((isOnLiquid(mc.thePlayer.boundingBox)) && (!mc.thePlayer.isInWater())) {
					mc.thePlayer.onGround = true;
					mc.thePlayer.motionY = 0.0D;
					long currentMS = System.nanoTime() / 1000000L;
					if ((currentMS - this.lastMS >= 180L) || (this.lastMS == -1L)) {
						mc.thePlayer.sendQueue
								.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
										mc.thePlayer.posX, mc.thePlayer.posY - 1.73D,
										mc.thePlayer.boundingBox.minY, mc.thePlayer.posZ,
										true));
						this.lastMS = currentMS;
					}
					mc.thePlayer.onGround = true;
				}
			}
		}
	}

	public static boolean isOnLiquid(AxisAlignedBB boundingBox) {
		boolean onLiquid = false;
		int y = (int) boundingBox.copy().offset(0.0D, -0.01D, 0.0D).minY;
		for (int x = MathHelper.floor_double(boundingBox.minX); x < MathHelper
				.floor_double(boundingBox.maxX + 1.0D); x++) {
			for (int z = MathHelper.floor_double(boundingBox.minZ); z < MathHelper
					.floor_double(boundingBox.maxZ + 1.0D); z++) {
				Block block = Minecraft.getMinecraft().theWorld.getBlock(x, y, z);
				if (block != Blocks.air) {
					if (!(block instanceof BlockLiquid)) {
						return false;
					}

					onLiquid = true;
				}
			}
		}
		return onLiquid;
	}
	
	private boolean isInLiquid() {
		AxisAlignedBB par1AxisAlignedBB = Minecraft.getMinecraft().thePlayer.boundingBox.contract(0.001D, 0.001D, 0.001D);
		int var4 = MathHelper.floor_double(par1AxisAlignedBB.minX);
		int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0D);
		int var6 = MathHelper.floor_double(par1AxisAlignedBB.minY);
		int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0D);
		int var8 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
		int var9 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0D);
		
		if (!Minecraft.getMinecraft().theWorld.checkChunksExist(var4, var6, var8, var5, var7, var9)) {
			return false;
		} else {
			Vec3 var11 = Minecraft.getMinecraft().theWorld.getWorldVec3Pool().getVecFromPool(0.0D, 0.0D, 0.0D);
			for (int var12 = var4; var12 < var5; ++var12) {
				for (int var13 = var6; var13 < var7; ++var13) {
					for (int var14 = var8; var14 < var9; ++var14) {
						Block var15 = Minecraft.getMinecraft().theWorld.getBlock(var12, var13, var14);
						if (var15 instanceof BlockLiquid) {
							return true;
						}
					}
				}
			}
			
			return false;
		}
	}
}
