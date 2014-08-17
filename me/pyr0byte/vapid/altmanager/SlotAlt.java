package me.pyr0byte.vapid.altmanager;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.StringUtils;

public class SlotAlt extends GuiSlot
{
	private GuiAltList aList;
	private int selected;
	
	public SlotAlt(Minecraft theMinecraft, GuiAltList aList)
	{
		super(theMinecraft, aList.width, aList.height, 32, aList.height - 59, Manager.slotHeight);
		this.aList = aList;
		this.selected = 0;
	}
	
	@Override
	protected int func_148138_e()
	{
		return this.getSize() * Manager.slotHeight;
	}
	
	@Override
	protected int getSize()
	{
		return Manager.altList.size();
	}

	@Override
	protected void elementClicked(int var1, boolean var2, int var3, int var4)
	{
		this.selected = var1;
	}

	@Override
	protected boolean isSelected(int var1)
	{
		return this.selected == var1;
	}
	
	protected int getSelected()
	{
		return this.selected;
	}

	@Override
	protected void drawBackground()
	{
		aList.drawDefaultBackground();
	}

	@Override
	protected void drawSlot(int selectedIndex, int x, int y, int var4, Tessellator var5, int var6, int var7)
	{
		//try
		//{
			Alt theAlt = Manager.altList.get(selectedIndex);
			aList.drawString(aList.getLocalFontRenderer(), theAlt.getUsername(), x, y + 1, 0xFFFFFF);
			if(theAlt.isPremium())
			{
				aList.drawString(aList.getLocalFontRenderer(), "Premium", x, y + 12, 0x00FF00);
				//aList.drawString(aList.getLocalFontRenderer(), Manager.makePassChar(theAlt.getPassword()), x, y + 12, 0x808080);
			}else
			{
				aList.drawString(aList.getLocalFontRenderer(), "\247mPremium", x, y + 12, 0x990000);
			}
		/*}catch(AccountManagementException error)
		{
			error.printStackTrace();
		}*/
	}

    public void drawTexturedModalRectZ(final float i, final float y, final int k, final int i1, final int j1, final int k1, final float zLevel) {
		float f = 0.015625F;
		float f1 = 0.03125F;
		Tessellator tesselator = Tessellator.instance;
		tesselator.startDrawingQuads();
		tesselator.addVertexWithUV(i + 0, y + k1, zLevel, (k + 0) * f, (i1 + k1) * f1);
		tesselator.addVertexWithUV(i + j1, y + k1, zLevel, (k + j1) * f, (i1 + k1) * f1);
		tesselator.addVertexWithUV(i + j1, y + 0, zLevel, (k + j1) * f, (i1 + 0) * f1);
		tesselator.addVertexWithUV(i + 0, y + 0, zLevel, (k + 0) * f, (i1 + 0) * f1);
		tesselator.draw();
    }
}
