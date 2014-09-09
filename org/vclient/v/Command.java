package org.vclient.v;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.vclient.v.modules.ModuleBase;

public class Command {

	public ArrayList<String> aliases;
	V V;
	ModuleBase module;
	public Map<String, Argument> args;
	String desc;
	
	public Command(V V, ModuleBase module, ArrayList<String> aliases) 
	{
		this.aliases = aliases;
		this.V = V;
		this.module = module;
		this.args = new HashMap<String, Argument>();
		this.desc = "No description available";
	}
	
	public Command(V V, ModuleBase module, ArrayList<String> aliases, String desc) 
	{
		this.aliases = aliases;
		this.V = V;
		this.module = module;
		this.args = new HashMap<String, Argument>();
		this.desc = desc;
	}
	
	public void registerArg(String arg, Class sets[])
	{
		
		args.put(arg, new Argument(this, arg, sets));
		
	}
	
	public void registerArg(String arg, Class sets[], String usage)
	{
		
		args.put(arg, new Argument(this, arg, sets, usage));
		
	}
	
	public boolean parseArgs(String argv[]) 
	{
		Argument arg;
		String subarray[];
		boolean foundFlag = false;
		
		if(args.size() == 0 && argv.length > 0)
		{
			V.errorMessage("This command has no arguments (clich\247 sad face)");
			return false;
		}
		
		for(int i = 0; i < argv.length; i++) {
			
			if(args.containsKey(argv[i])) {
				foundFlag = true;
				arg = (Argument)args.get(argv[i]);
				
				if(arg.getArgc() > argv.length - (i + 1)) {
					V.errorMessage("Not enough arguments for flag: " + argv[i] + "; calls for " + arg.getArgc() + " arguments");
					return false;
				}
				
				subarray = Arrays.copyOfRange(argv, i + 1, i + 1 + arg.getArgc());
				
				if(!arg.matches(subarray)) {
					return false;
				}
				
				this.module.processArguments(argv[i], subarray);
				i += arg.getArgc();
				
			} else if(argv[i].equals("set")) {
				if(argv.length > i) {
					foundFlag = true;
					// TODO: Set field in module to this value if it matches the type.
					
				}
			}
			
		}
		
		if(!foundFlag && argv.length > 0) 
		{
			// If no flag was found, use the default flag. This is absolutely bloat.
			
			if(this.module.defaultArg.equals(""))
			{
				V.errorMessage("Invalid argument");
				return false;
			}
			
			arg = (Argument)args.get(this.module.defaultArg);
			
			if(arg.getArgc() > argv.length) 
			{
				V.errorMessage("Not enough arguments for flag; calls for " + arg.getArgc() + " arguments");
				return false;
			}
			
			subarray = Arrays.copyOfRange(argv, 0, arg.getArgc());
			
			if(!arg.matches(subarray)) 
			{
				return false;
			}
			
			this.module.processArguments(this.module.defaultArg, subarray);
			
		} else if(!foundFlag) {
			if(this.module.isToggleable)
			{
				this.module.toggleState();
				V.saveHacks();
			} else {
				V.errorMessage("You have to provide arguments for this command");
				return false;
			}
		}
		
		return true;
	}
	
	public String getDescription()
	{
		return this.desc;
		
	}
}
