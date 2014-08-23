package org.vclient.v.altmanager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.util.Session;
import net.minecraft.util.StringTranslate;

public class GuiAltList extends GuiScreen {
	public String dispErrorString = "";
	public boolean deleteMenuOpen = false;
	
	public GuiAltList() {
		Manager.loadAlts();
	}
	
	public FontRenderer getLocalFontRenderer() {
		return this.fontRendererObj;
	}
	
	public void onGuiClosed() {
		Manager.saveAlts();
		super.onGuiClosed();
	}
	
	private SlotAlt tSlot;
	
	public void initGui() {
		buttonList.clear();
		buttonList.add(new GuiButton(1, width / 2 - 100, height - 47, 100, 20, "Add"));
		buttonList.add(new GuiButton(2, width / 2, height - 47, 100, 20, "Login"));
		buttonList.add(new GuiButton(3, width / 2 - 100, height - 26, 66, 20, "Remove"));
		buttonList.add(new GuiButton(4, width / 2 - 33, height - 26, 64, 20, "Back"));
		buttonList.add(new GuiButton(5, width / 2 + 32, height - 26, 69, 20, "Direct Login"));
		
		tSlot = new SlotAlt(Minecraft.getMinecraft(), this);
		tSlot.func_148134_d(7, 8);
	}
	
	@Override
	public void confirmClicked(boolean flag, int i1) {
		super.confirmClicked(flag, i1);
		if(deleteMenuOpen) {
			deleteMenuOpen = false;
			
			if(flag) {
				Manager.altList.remove(i1);
				Manager.saveAlts();
			}
			
			this.mc.displayGuiScreen(this);
		}
	}
	
	public void actionPerformed(GuiButton button) {
		super.actionPerformed(button);
		if(button.id == 1) {
			GuiAltAdd gaa = new GuiAltAdd(this);
			this.mc.displayGuiScreen(gaa);
		}
		if(button.id == 2) {
			try {
				Alt a1 = Manager.altList.get(tSlot.getSelected());
				if(a1.isPremium()) {
					try {
			        	YggdrasilAuthenticator auth = new YggdrasilAuthenticator(a1.getUsername(), a1.getPassword());
			            if (auth.login()) {
			            	Minecraft.getMinecraft().session = auth.getSession();
			            }
						Manager.altScreen.dispErrorString = "";
					} catch(Exception error) {
						dispErrorString = "".concat("\247cBad Login \2477(").concat(a1.getUsername()).concat(")");
					}
				} else {
					Minecraft.getMinecraft().session = new Session(a1.getUsername(), "-", "-");
					Manager.altScreen.dispErrorString = "";
				}
			}catch(Exception e) {}
		}
		if(button.id == 3)
		{
			try
			{
				StringTranslate stringtranslate = StringTranslate.getInstance();
				String s1 = "Are you sure you want to delete the alt: " + "\"" + Manager.altList.get(tSlot.getSelected()).getUsername() + "\"" + "?";
				String s3 = "Delete";
				String s4 = "Cancel";
				GuiYesNo guiyesno = new GuiYesNo(this, s1, "", s3, s4, tSlot.getSelected());
				deleteMenuOpen = true;
				this.mc.displayGuiScreen(guiyesno);
			}catch(Exception e) {}
		}
		if(button.id == 4)
		{
			this.mc.displayGuiScreen(new GuiMultiplayer(this));
		}
		if(button.id == 5)
		{
			GuiDirectLogin gdl = new GuiDirectLogin(this);
			this.mc.displayGuiScreen(new GuiDirectLogin(this));
		}
	}
	
	public void updateScreen()
	{
		super.updateScreen();
	}
	
	public void drawScreen(int i, int j, float f)
	{
		tSlot.func_148128_a(i, j, f);
		drawCenteredString(this.fontRendererObj, "Alts: \2477" + Manager.altList.size(), width / 2, 13, 0xFFFFFF);
		this.fontRendererObj.drawStringWithShadow("Username: \2477" + Minecraft.getMinecraft().session.username, 3, 3, 0xFFFFFF);
		this.fontRendererObj.drawStringWithShadow(dispErrorString, 3, 13, 0xFFFFFF);
		super.drawScreen(i, j, f);
	}
}
