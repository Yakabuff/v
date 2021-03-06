package org.vclient.v;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ScreenShotHelper;
import net.minecraft.util.Util.EnumOS;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.vclient.v.modules.ModuleAura;
import org.vclient.v.modules.ModuleAutoarmor;
import org.vclient.v.modules.ModuleAutobridge;
import org.vclient.v.modules.ModuleAutomine;
import org.vclient.v.modules.ModuleAutorespawn;
import org.vclient.v.modules.ModuleAutowalk;
import org.vclient.v.modules.ModuleBase;
import org.vclient.v.modules.ModuleBindEditor;
import org.vclient.v.modules.ModuleBot;
import org.vclient.v.modules.ModuleBreakMask;
import org.vclient.v.modules.ModuleBrightness;
import org.vclient.v.modules.ModuleDump;
import org.vclient.v.modules.ModuleESP;
import org.vclient.v.modules.ModuleEncryption;
import org.vclient.v.modules.ModuleFastBreak;
import org.vclient.v.modules.ModuleFastPlace;
import org.vclient.v.modules.ModuleFastbow;
import org.vclient.v.modules.ModuleFastbowTristan;
import org.vclient.v.modules.ModuleFly;
import org.vclient.v.modules.ModuleFreecam;
import org.vclient.v.modules.ModuleFriends;
import org.vclient.v.modules.ModuleGlide;
import org.vclient.v.modules.ModuleGreet;
import org.vclient.v.modules.ModuleGui;
import org.vclient.v.modules.ModuleHelp;
import org.vclient.v.modules.ModuleHit;
import org.vclient.v.modules.ModuleIRC;
import org.vclient.v.modules.ModuleImp;
import org.vclient.v.modules.ModuleInfo;
import org.vclient.v.modules.ModuleIntervalThrow;
import org.vclient.v.modules.ModuleMarkers;
import org.vclient.v.modules.ModuleMarkov;
import org.vclient.v.modules.ModuleNightVision;
import org.vclient.v.modules.ModuleNoFall;
import org.vclient.v.modules.ModuleNoKnockback;
import org.vclient.v.modules.ModuleNoSlow;
import org.vclient.v.modules.ModuleNotifications;
import org.vclient.v.modules.ModuleRegexIgnore;
import org.vclient.v.modules.ModuleSounder;
import org.vclient.v.modules.ModuleSpam;
import org.vclient.v.modules.ModuleSpawn;
import org.vclient.v.modules.ModuleSprint;
import org.vclient.v.modules.ModuleStep;
import org.vclient.v.modules.ModuleTeleport;
import org.vclient.v.modules.ModuleTextWidth;
import org.vclient.v.modules.ModuleTimer;
import org.vclient.v.modules.ModuleTracers;
import org.vclient.v.modules.ModuleTrajectories;
import org.vclient.v.modules.ModuleUtil;
import org.vclient.v.modules.ModuleWaypoints;
import org.vclient.v.modules.ModuleXray;
import org.vclient.v.modules.ModuleYaw;

public class V {

	Minecraft mc;
	public ArrayList<ModuleBase> modules;
	public Events events;
	public Map<String, ModuleBase> moduleCache;
	public Map<String, String> moduleNameCache;
	boolean hudNotifications;
	public final String delimeter;
	
	/**
	 * VClient file directory ".minecraft/V/"
	 */
	public static File vDir = new File(getAppDir("minecraft") + File.separator + "V");
	
	public V(Minecraft mc) 
	{
		this.mc = mc;
		
		delimeter = "-";
		
		modules = new ArrayList<ModuleBase>();
		
		moduleCache = new HashMap<String, ModuleBase>();
		moduleNameCache = new HashMap<String, String>();

		Reflections reflections = new Reflections("org.vclient.v.modules");

		Set<Class<? extends ModuleBase>> classes = reflections.getSubTypesOf(ModuleBase.class);

		Iterator i = classes.iterator();
		Class clazz;
		
		while(i.hasNext()) {
			clazz = (Class<? extends ModuleBase>)i.next();
			try {
				modules.add((ModuleBase)clazz.getConstructor(V.class, Minecraft.class).newInstance(this, this.mc));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		// Troublesome 
		
		for(ModuleBase m : modules)
		{
			moduleCache.put(m.getName().toLowerCase(), m);
			moduleNameCache.put(m.getClass().getSimpleName(), m.getName().toLowerCase());
		}
		
		events = new Events(this.mc, this);
		
		hudNotifications = true;
		//vb = new VBools(this);
		
		new StaticV(this, this.mc);
	
	}
	

	/*
	public void loadGui()
	{
		vg = new VGui(this.mc, this);	
	}
	*/
	
	public void onTick() 
	{

		if(mc.thePlayer != null) {
			
			for(ModuleBase m : this.modules) {
				if(m.needsTick)
					m.onTick();
			}
		}
	
	}
	
	public void onRendererTick()
	{
		for(ModuleBase m : this.modules) {
			if(m.needsRendererTick)
				m.onRendererTick();
		}
	}
	
	public void onBotTick()
	{
		for(ModuleBase m : this.modules) {
			if(m.needsBotTick)
				m.onBotTick();
		}
	}
	
	public void confirmMessage(String msg)
	{
		this.message("\247l[V]\247r " + msg);
	}
	
	public void notificationMessage(String msg)
	{
		this.notificationMessage(msg, this.hudNotifications);
	}
	
	
	public void notificationMessage(String msg, boolean hud)
	{
		if(!hud)
			this.message("\247l>>\247r " + msg);
		else
			getModule(ModuleGui.class).addToQueue(msg);
	}
	
	public void errorMessage(String msg)
	{
		this.message("\247l\247m[V]\247r " + msg);
	}
	
	public void message(String msg) {
		this.mc.ingameGUI.getChatGUI().func_146227_a(new ChatComponentText(msg));
	}
	
	public void yellowMessage(String msg) {
		this.message("\247e" + msg);
	}
	
	public void italicMessage(String msg) {
		this.message("\247o" + msg);
	}

	public ModuleBase getModule(String name)
	{
		return moduleCache.get(name);
	}
	
	public <T extends ModuleBase> T getModule(String name, Class<T> type) {
	    return type.cast( moduleCache.get(name) );
	}
	
	public <T extends ModuleBase> T getModule(Class<T> type) {
	    return type.cast( moduleCache.get(moduleNameCache.get(type.getSimpleName())));
	}
	
	public int getBlockId(Block b)
	{
		return Block.blockRegistry.getIDForObject(b);
	}
	

	public String read(String filename) 
	{
	    
	    File f = new File(filename);
	    
	    if(!f.exists())
	    {

				try {
					f.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    }
	    
		try
	    {
	     BufferedReader br = new BufferedReader(new FileReader(filename));
	     
	     String line;
	     String res = ""; 
	     while ((line = br.readLine()) != null)
	     {
	        res += line;
	     }
	      
	     	br.close();
	    
	     	return res;
	    } catch (IOException localIOException) 
	    {  
	    	localIOException.printStackTrace();
	    }
		
		return null;
	}
	
	public String readOrFallback(String filename, String fallback) {
		String out = this.read(filename);
		return out == "" ? fallback : out;
	}
	
	public ArrayList<String> readLines(String filename) 
	{
	    
	    File f = new File(filename);
	    
	    if(!f.exists())
	    {

				try {
					f.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    }
	    
		try
	    {
	     BufferedReader br = new BufferedReader(new FileReader(filename));
	     
	     String line;
	     ArrayList<String> lines = new ArrayList<String>(); 
	     while ((line = br.readLine()) != null)
	     {
	        lines.add(line);
	     }
	      
	     	br.close();
	    
	     	return lines;
	     	
	    } catch (IOException localIOException) 
	    {  
	    	localIOException.printStackTrace();
	    }
		
		return null;
	}
	
	public ArrayList<String> readLinesOrFallback(String filename, ArrayList<String> fallback) {
		ArrayList<String> out = this.readLines(filename);
		return out.size() == 0 ? fallback : out;
	}
	
	public ArrayList<String> readUTF8Lines(String filename) 
	{
	    
	    File f = new File(filename);
	    
	    if(!f.exists())
	    {

				try {
					f.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    }
	    
		try
	    {
	     BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "UTF-8"));
	     
	     String line;
	     ArrayList<String> lines = new ArrayList<String>(); 
	     while ((line = br.readLine()) != null)
	     {
	        lines.add(line);
	     }
	      
	     	br.close();
	    
	     	return lines;
	     	
	    } catch (IOException localIOException) 
	    {  
	    	localIOException.printStackTrace();
	    }
		
		return null;
	}
	
	public void write(String filename, String data)
	{
		try 
		  {
		        File f = new File(filename);
		
		        if (f.exists()) 
		        {
		        	f.delete();
		        	f.createNewFile();
		        }
		
				
		        FileWriter fw = new FileWriter(filename);
		        BufferedWriter bw = new BufferedWriter(fw);
		
		        
		        bw.write(data);
		
		        bw.close();
		        
		     
	    }
	    catch (IOException localIOException)
	    {
	    	localIOException.printStackTrace();
	    }
	}
	
	public void writeLines(String filename, ArrayList<String> data)
	{
		try 
		  {
		        File f = new File(filename);
		
		        if (f.exists()) 
		        {
		        	f.delete();
		        	f.createNewFile();
		        }
		
				
		        FileWriter fw = new FileWriter(filename);
		        BufferedWriter bw = new BufferedWriter(fw);
		
		        for(String s : data)
		        {
		        	bw.write(s + "\n");
		        }
		        
		        bw.close();
		        
		     
	    }
	    catch (IOException localIOException)
	    {
	    	localIOException.printStackTrace();
	    }
	}

	public void appendToFile(String filename, String str)
	{
		File f = new File(filename);
		String in = "";
		if(f.exists())
			in = this.read(filename) + str + "\n";
		else
			in = str + "\n";
		
		this.write(filename, in);
	}
	
	public void takeScreenshot()
	{
        mc.ingameGUI.getChatGUI().func_146227_a(ScreenShotHelper.saveScreenshot(mc.mcDataDir, mc.displayWidth, mc.displayHeight, mc.mcFramebuffer));
	}

	public String join(ArrayList<String> strs, String glue) {
		String ret = "";
		for(int i = 0; i < strs.size(); i++) {
			if(i > strs.size() - 2) {
				ret += strs.get(i);
			} else {
				ret += strs.get(i) + glue;
			}
		}
		return ret;
	}
	
    /**
     * gets the working dir (OS specific) for the specific application (which is always minecraft)
     */
    public static File getAppDir(String par0Str)
    {
        String var1 = System.getProperty("user.home", ".");
        File var2;

        switch (EnumOSHelper.field_90049_a[getOs().ordinal()])
        {
            case 1:
            case 2:
                var2 = new File(var1, '.' + par0Str + '/');
                break;

            case 3:
                String var3 = System.getenv("APPDATA");

                if (var3 != null)
                {
                    var2 = new File(var3, "." + par0Str + '/');
                }
                else
                {
                    var2 = new File(var1, '.' + par0Str + '/');
                }

                break;

            case 4:
                var2 = new File(var1, "Library/Application Support/" + par0Str);
                break;

            default:
                var2 = new File(var1, par0Str + '/');
        }

        if (!var2.exists() && !var2.mkdirs())
        {
            throw new RuntimeException("The working directory could not be created: " + var2);
        }
        else
        {
            return var2;
        }
    }

    public static EnumOS getOs()
    {
        String var0 = System.getProperty("os.name").toLowerCase();
        return var0.contains("win") ? EnumOS.WINDOWS : (var0.contains("mac") ? EnumOS.MACOS : (var0.contains("solaris") ? EnumOS.SOLARIS : (var0.contains("sunos") ? EnumOS.SOLARIS : (var0.contains("linux") ? EnumOS.LINUX : (var0.contains("unix") ? EnumOS.LINUX : EnumOS.UNKNOWN)))));
    }
    
    
    public void saveHacks()
	{
		try
		{
			File file = new File(vDir.getAbsolutePath(), "hacks.vpd");
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			for(ModuleBase mod : this.modules)
			{
				if(!mod.getName().equals("Freecam"))
				{
					out.write(mod.getName().toLowerCase().replace(" ", "") + ":" + mod.isEnabled);
					out.write("\r\n");
				}
			}
			out.close();
		}catch(Exception e) {}
	}
    
	public void loadHacks()
	{
		try
		{
			File file = new File(vDir.getAbsolutePath(), "hacks.vpd");
			FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line;
			while((line = br.readLine()) != null)
			{
				String curLine = line.toLowerCase().trim();
				String name = curLine.split(":")[0];
				boolean isOn = Boolean.parseBoolean(curLine.split(":")[1]);
				for(ModuleBase mod : this.modules)
				{
					if(mod.getName().toLowerCase().replace(" ", "").equals(name))
					{
						mod.isEnabled = isOn;
					}
				}
			}
			br.close();
		}catch(Exception e)
		{
			e.printStackTrace();
			saveHacks();
		}
	}
}
