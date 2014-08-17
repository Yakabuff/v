package me.pyr0byte.vapid.altmanager;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiAltAdd extends GuiScreen {
	public GuiScreen parent;
	public GuiTextField usernameBox;
	public GuiPasswordField passwordBox;
	
	public GuiAltAdd(GuiScreen paramScreen) {
		this.parent = paramScreen;
	}
	
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 96 + 12, "Add"));
		buttonList.add(new GuiButton(2, width / 2 - 100, height / 4 + 96 + 36, "Back"));
		usernameBox = new GuiTextField(this.fontRendererObj, width / 2 - 100, 76, 200, 20);
		passwordBox = new GuiPasswordField(this.fontRendererObj, width / 2 - 100, 116, 200, 20);
		usernameBox.func_146203_f(200);
		passwordBox.func_146203_f(128);
	}
	
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}
	
	public void updateScreen() {
		usernameBox.updateCursorCounter();
		passwordBox.updateCursorCounter();
	}
	
	public void mouseClicked(int x, int y, int b) {
		usernameBox.mouseClicked(x, y, b);
		passwordBox.mouseClicked(x, y, b);
		super.mouseClicked(x, y, b);
	}
	
	public void actionPerformed(GuiButton button) {
		if(button.id == 1) {
			if(!usernameBox.getText().trim().isEmpty()) {
				if(passwordBox.getText().trim().isEmpty()) {
					Alt theAlt = new Alt(usernameBox.getText().trim());
					if(!Manager.altList.contains(theAlt)) {
						Manager.altList.add(theAlt);
						Manager.saveAlts();
					}
				} else {
					Alt theAlt = new Alt(usernameBox.getText().trim(), passwordBox.getText().trim());
					if(!Manager.altList.contains(theAlt)) {
						Manager.altList.add(theAlt);
						Manager.saveAlts();
					}
				}
			}
			Minecraft.getMinecraft().displayGuiScreen(parent);
		} else if(button.id == 2) {
			Minecraft.getMinecraft().displayGuiScreen(parent);
		}
	}
	
	protected void keyTyped(char c, int i) {
		usernameBox.textboxKeyTyped(c, i);
		passwordBox.textboxKeyTyped(c, i);
		if(c == '\t') {
			if(usernameBox.isFocused()) {
				usernameBox.setFocused(false);
				passwordBox.setFocused(true);
			} else {
				usernameBox.setFocused(true);
				passwordBox.setFocused(false);
			}
		}
		if(c == '\r') {
			actionPerformed((GuiButton) buttonList.get(0));
		}
	}
	
	public void drawScreen(int x, int y, float f) {
		drawDefaultBackground();
		drawString(this.fontRendererObj, "Username", width / 2 - 100, 63, 0xa0a0a0);
		drawString(this.fontRendererObj, "Password", width / 2 - 100, 104, 0xa0a0a0);
		try{
			usernameBox.drawTextBox();
			passwordBox.drawTextBox();
		} catch(Exception err) {
			err.printStackTrace();
		}
		super.drawScreen(x, y, f);
	}
}
