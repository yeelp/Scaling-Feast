package yeelp.scalingfeast.command;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.handlers.CapabilityHandler;
import yeelp.scalingfeast.util.FoodCapProvider;
import yeelp.scalingfeast.util.IFoodCap;

public class SFCommand extends CommandBase {

	private static String[] commandList = new String[] {"setExtendedMax"};
	private static HashSet<String> commands = new HashSet<String>(Arrays.asList(commandList));
	@Override
	public String getName() 
	{
		return "scalingfeast";
	}

	@Override
	public List getAliases()
	{
		return Lists.newArrayList("sf");
	}
	
	@Override
	public String getUsage(ICommandSender sender) 
	{
		return "commands.scalingfeast.usage";
	}
	
	@Override 
	public int getRequiredPermissionLevel()
	{
		return 2;
	}
	

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException 
	{
		if(!(args.length >= 1 && commands.contains(args[0])))
		{
			throw new WrongUsageException(getUsage(sender));
		}
		else
		{
			//check for invalid args, throw appropriate message
			for(String command : commandList)
			{
				if(command.equals(args[0]) && args.length != 3)
				{
					sender.sendMessage(new TextComponentTranslation("commands.scalingfeast.command.usage", command));
					return;
				}
			}
			editFoodCap(server, sender, args);
		}

	}
	
	private void editFoodCap(MinecraftServer server, ICommandSender sender, String[] args)
	{
		List<EntityPlayerMP> players = server.getPlayerList().getPlayers();
		for(EntityPlayerMP player : players)
		{
			if(player.getName().equals(args[1]))
			{
				IFoodCap fs = player.getCapability(FoodCapProvider.capFoodStat, null);
				try
				{
					if(0 <= Float.parseFloat(args[2]) && Float.parseFloat(args[2]) < Short.MAX_VALUE)
					{
						float satVal = Float.parseFloat(args[2]);
						short val = (short) satVal;
						switch(args[0])
						{
							case "setExtendedMax":
								fs.setMax(val);
								CapabilityHandler.syncCap(player);
								break;
							default:
								break; //This will never occur because of the predicates we've checked before hand
						}
						sender.sendMessage(new TextComponentTranslation("commands.scalingfeast.command.success"));
					}
					else
					{
						sender.sendMessage(new TextComponentTranslation("commands.scalingfeast.command.numerr", String.valueOf(Short.MAX_VALUE)));
					}
					return;
				}
				catch(NumberFormatException e)
				{
					sender.sendMessage(new TextComponentTranslation("commands.scalingfeast.command.numerr", String.valueOf(Short.MAX_VALUE)));
					return;
				}
			}
		}
		sender.sendMessage(new TextComponentTranslation("commands.scalingfeast.command.existence", args[1]));
	}
	@Override
	public List<String> getTabCompletions(MinecraftServer serv, ICommandSender sender, String[] args, BlockPos blockPos)
	{
		if(args.length == 1)
		{
			return getListOfStringsMatchingLastWord(args,  commands);
		}
		return null;
	}

}
