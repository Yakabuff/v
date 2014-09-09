package org.vclient.v.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;

import org.lwjgl.opengl.GL11;
import org.vclient.v.Command;
import org.vclient.v.V;

public class ModuleTracers extends ModuleBase {

	Map<Class, Integer> traced;
	Map<String, Class> validEntities;
	Map<Class, String> validReverseLookup;
	Map<String, Integer> color;
	Map<Integer, String> colorReverseLookup;
	boolean lineMode;
	
	public ModuleTracers(V V, Minecraft mc)
	{
		super(V, mc);
		
		this.lineMode = false;
		this.command = new Command(this.V, this, aliases, "Draws a line from your player to an entity");
		this.command.registerArg("add", new Class[] { String.class }, "Entity class to add");
		this.command.registerArg("valid", new Class[] { }, "Lists valid entities");
		this.command.registerArg("list", new Class[] { }, "Lists traced entities");
		this.command.registerArg("del", new Class[] { String.class }, "Removes a traced entity");
		this.command.registerArg("line", new Class[] {}, "Draws a line through the entity rather than to it");

		this.command.registerArg("color", new Class[] { String.class, String.class }, "Change the color of a tracer with a color name");
		this.command.registerArg("rgb", new Class[] { String.class, Integer.class, Integer.class, Integer.class }, "Change the color of a tracer");

		this.defaultArg = "add";
		
		this.needsRendererTick = true;
		traced = new HashMap<Class, Integer>();
		validEntities = new HashMap<String, Class>();
		validReverseLookup = new HashMap<Class, String>();

		color = new HashMap<String, Integer>();
		colorReverseLookup = new HashMap<Integer, String>();

		validEntities.put("dragon", EntityDragon.class);
		validEntities.put("wither", EntityWither.class);
		validEntities.put("item", EntityItem.class);
		validEntities.put("blaze", EntityBlaze.class);
		validEntities.put("cavespider", EntityCaveSpider.class);
		validEntities.put("creeper", EntityCreeper.class);
		
		validEntities.put("enderman", EntityEnderman.class);
		validEntities.put("ghast", EntityGhast.class);
		validEntities.put("giant", EntityGiantZombie.class);
		validEntities.put("irongolem", EntityIronGolem.class);
		validEntities.put("magmacube", EntityMagmaCube.class);
		validEntities.put("pigman", EntityPigZombie.class);
		
		validEntities.put("silverfish", EntitySilverfish.class);
		validEntities.put("skeleton", EntitySkeleton.class);
		validEntities.put("slime", EntitySlime.class);
		validEntities.put("snowman", EntitySnowman.class);
		validEntities.put("spider", EntitySpider.class);
		validEntities.put("witch", EntityWitch.class);
		validEntities.put("zombie", EntityZombie.class);

		validEntities.put("bat", EntityBat.class);
		validEntities.put("chicken", EntityChicken.class);
		validEntities.put("cow", EntityCow.class);
		validEntities.put("horse", EntityHorse.class);
		validEntities.put("mooshroom", EntityMooshroom.class);
		validEntities.put("ocelot", EntityOcelot.class);
		validEntities.put("pig", EntityPig.class);
		
		validEntities.put("sheep", EntitySheep.class);
		validEntities.put("squid", EntitySquid.class);
		validEntities.put("villager", EntityVillager.class);
		validEntities.put("wolf", EntityWolf.class);
		validEntities.put("player", EntityOtherPlayerMP.class);

		validEntities.put("itemframe", EntityItemFrame.class);

		for(String s : validEntities.keySet())
		{
			this.validReverseLookup.put(validEntities.get(s), s);
		}
		
		color.put("white", this.parseRGB(255, 255, 255));
		color.put("black", this.parseRGB(0, 0, 0));
		color.put("red", this.parseRGB(255, 0, 0));
		
		color.put("lime", this.parseRGB(0, 255, 0));
		color.put("blue", this.parseRGB(0, 0, 255));
		color.put("yellow", this.parseRGB(255, 255, 0));
		color.put("cyan", this.parseRGB(0, 255, 255));
		color.put("magenta", this.parseRGB(255, 0, 255));
		color.put("silver", this.parseRGB(192, 192, 192));
		color.put("gray", this.parseRGB(128, 128, 128));
		color.put("maroon", this.parseRGB(128, 0, 0));

		color.put("olive", this.parseRGB(128, 128, 0));
		color.put("green", this.parseRGB(0, 128, 0));
		color.put("purple", this.parseRGB(128, 0, 128));
		color.put("teal", this.parseRGB(0, 128, 128));
		color.put("navy", this.parseRGB(0, 0, 128));

		for(String s : color.keySet())
		{
			this.colorReverseLookup.put(color.get(s), s);
		}

		this.readTraced();

	}
	
	
	public void readTraced()
	{
		this.readTraced("tracers_default.vpd");
	}
	
	public void readTraced(String filename)
	{
		ArrayList<String> in = this.V.readLines(filename);
		String tmp[];
		for(String s : in)
		{
			tmp = s.split(":");
			this.traced.put(this.validEntities.get(tmp[0]), Integer.parseInt(tmp[1]));
		}
	}

	public void writeTraced()
	{
		this.writeTraced("tracers_default.vpd");
	}
	
	public void writeTraced(String filename)
	{
		ArrayList<String> in = new ArrayList<String>();
		for(Class unlikePopbob : this.traced.keySet())
		{
			in.add(this.validReverseLookup.get(unlikePopbob) + ":" + Integer.toString(this.traced.get(unlikePopbob)));
		}
		
		this.V.writeLines(filename, in);
	}
	
    public int parseRGB(int r, int g, int b)
    {
 	   return ( r << 16) | ( g << 8) | b;
    }
    
    @Override
    public void processArguments(String name, String argv[])
    {
    	
    	if(name.equals("add"))
    	{
    		if(this.validEntities.containsKey(argv[0].toLowerCase()))
    		{
    			this.traced.put(this.validEntities.get(argv[0].toLowerCase()), this.parseRGB(255, 255, 255));
    			this.V.confirmMessage("Added " + argv[0] + " to tracers");
    			this.writeTraced();
    		}
    		else
    			this.V.errorMessage("You can't trace that entity; check out -tracers valid");
    	}
    	else if(name.equals("del"))
    	{
    		String entity = argv[0].toLowerCase();

    		if(this.traced.containsKey(this.validEntities.get(entity)))
    		{
    			this.traced.remove(this.validEntities.get(entity));
    			this.V.confirmMessage("Deleted " + entity);
    			this.writeTraced();
    		}
    		else
    			this.V.errorMessage("That entity hasn't been added yet");
    	}
    	else if(name.equals("valid"))
    	{
    		String ret = "";
    		for(String s : this.validEntities.keySet())
    		{
    			ret += s + ", ";
    		}
    		
    		this.V.confirmMessage("You can select:");
    		this.V.message(ret.substring(0, ret.length() - 2));
    	}
    	else if(name.equals("color"))
    	{
    		if(this.traced.containsKey(this.validEntities.get(argv[0])))
    		{
	    		if(this.color.containsKey(argv[1]))
	    		{
	    			this.traced.put(this.validEntities.get(argv[0]), this.color.get(argv[1]));
	    			System.out.print(this.color.get(argv[1]) + "\n");
	    			this.writeTraced();
	    			this.V.confirmMessage("Changed color of " + argv[0] + " to " + argv[1]);
	    		}
	    		else
	    		{
	    			this.V.errorMessage("Not a valid color. Try one of these, or use -tracer rgb:");
	        		String ret = "";
	        		for(String s : this.color.keySet())
	        		{
	        			ret += s + ", ";
	        		}
	
	        		this.V.message(ret.substring(0, ret.length() - 2));
	    		}
    		}
    		else
    		{
    			this.V.errorMessage("Add that entity before setting it's color");
    		}
    	}
    	else if(name.equals("list"))
    	{
    		String type = "";
    		String color = "";
    		for(Class c : this.traced.keySet())
    		{
    			type = this.validReverseLookup.get(c);
    			if(this.colorReverseLookup.containsKey(this.traced.get(c)))
    			{
    				color = this.colorReverseLookup.get(this.traced.get(c));
    			}
    			else
    			{
    				int rgb = this.traced.get(c);
    				int r, g, b;
    				r = (rgb >> 16) & 255;
    				g = (rgb >> 8) & 255;
    				b = (rgb & 255);
    				
    				color = "(" + r + ", " + b + ", " + g + ")";
    			}
    			
    			this.V.message(type + ": " + color);
    				
    		}
    	}
    	else if(name.equals("line"))
    	{
    		this.lineMode = !this.lineMode;
    	}
    	
    }

    
    @Override
    public void onRendererTick()
    {
    	if(this.isEnabled)
    	{
    		this.tracers();
    	}
    }
    
	public void tracers() 
	{
      double size = 0.45;
      double ytSize = 0.35;

      GL11.glPushMatrix();
      
      GL11.glEnable(GL11.GL_LINE_SMOOTH);
      
      GL11.glDisable(GL11.GL_DEPTH_TEST);
      
      GL11.glDisable(GL11.GL_TEXTURE_2D);
      
      GL11.glDepthMask(false);
      
      GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
      
      GL11.glEnable(GL11.GL_BLEND);
      
      GL11.glLineWidth(1.5F);
      
      
	  for(int x = 0; x < mc.theWorld.getLoadedEntityList().size(); x ++) 
	  {
		  Entity entity = (Entity) mc.theWorld.getLoadedEntityList().get(x);
		  
		  if(this.traced.containsKey(entity.getClass()))
		  {
			  int color = this.traced.get(entity.getClass());
			  GL11.glColor3d(((color >> 16) & 255) / 255.0D, ((color >> 8) & 255) / 255.0D, ((color) & 255) / 255.0D);

			  float distance = mc.renderViewEntity.getDistanceToEntity(entity);
			  double posX = ((entity.lastTickPosX + (entity.posX - entity.lastTickPosX) - RenderManager.instance.renderPosX));
			  double posY = ((entity.lastTickPosY + (entity.posY - entity.lastTickPosY) - RenderManager.instance.renderPosY));
			  double posZ = ((entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) - RenderManager.instance.renderPosZ));
			  

			  if(!this.lineMode)
			  {
				  GL11.glBegin(GL11.GL_LINE_LOOP);
				  GL11.glVertex3d(0, 0, 0);
				  GL11.glVertex3d(posX, posY, posZ);
				  GL11.glEnd();
			  }else{}
		  }
       }

      GL11.glDisable(GL11.GL_BLEND);
      
      GL11.glDepthMask(true);
      
      GL11.glEnable(GL11.GL_TEXTURE_2D);
      
      GL11.glEnable(GL11.GL_DEPTH_TEST);
      
      GL11.glDisable(GL11.GL_LINE_SMOOTH);
      
      GL11.glPopMatrix();
    }
}
