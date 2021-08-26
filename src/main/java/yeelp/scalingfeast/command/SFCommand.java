package yeelp.scalingfeast.command;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.command.SelectorHandlerManager;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.util.SFAttributes;

public class SFCommand extends CommandBase {

	private static final String[] commandList = new String[] {
			"setMax",
			"setSaturation"};
	private static final HashSet<String> commands = new HashSet<String>(Arrays.asList(commandList));
	
	private static final Pattern numRegex = Pattern.compile("^\\d+(\\.\\d+)?$");

	@Override
	public String getName() {
		return "scalingfeast";
	}

	@Override
	public List<String> getAliases() {
		return Lists.newArrayList("sf");
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.scalingfeast.usage";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(!(args.length >= 1 && commands.contains(args[0]))) {
			throw new WrongUsageException(getUsage(sender));
		}
		// check for invalid args, throw appropriate message
		for(String command : commandList) {
			if(command.equals(args[0]) && args.length != 3) {
				sendErrorMessage(sender, "commands.scalingfeast.command.usage", command);
				return;
			}
		}
		if(args[1].startsWith("@")) {
			List<EntityPlayerMP> targets = SelectorHandlerManager.matchEntities(sender, args[1], EntityPlayerMP.class);
			if(targets.size() == 0) {
				sendErrorMessage(sender, "commands.scalingfeast.command.emptytarget", args[1]);
			}
			for(EntityPlayerMP player : targets) {
				editStats(sender, args, player);
			}
		}
		else {
			for(EntityPlayerMP player : server.getPlayerList().getPlayers()) {
				if(player.getName().equals(args[1])) {
					editStats(sender, args, player);
					return;
				}
			}
			sendErrorMessage(sender, "commands.scalingfeast.command.existence", args[1]);
		}
	}

	private static boolean editStats(ICommandSender sender, String[] args, EntityPlayerMP player) {
		if(numRegex.matcher(args[2]).matches()) {
			if(0 < Float.parseFloat(args[2]) && Float.parseFloat(args[2]) <= Short.MAX_VALUE) {
				float satVal = Float.parseFloat(args[2]);
				short val = (short) satVal;
				String type = "";
				String result = "";
				FoodStats fs = player.getFoodStats();
				switch(args[0]) {
					case "setMax":
						type = "max hunger base value";
						int maxSet = Math.min(val, ScalingFeastAPI.accessor.getHungerHardCap());
						result = String.valueOf(maxSet);
						player.getEntityAttribute(SFAttributes.MAX_HUNGER_MOD).setBaseValue(maxSet - ModConsts.VANILLA_MAX_HUNGER);
						break;
					case "setSaturation":
						type = "saturation";
						float satSet = Math.min(Math.min(fs.getFoodLevel(), ScalingFeastAPI.accessor.getPlayerSaturationCap(player)), satVal);
						result = String.valueOf(satSet);
						AppleCoreAPI.mutator.setSaturation(player, satSet);
						ScalingFeastAPI.mutator.capPlayerSaturation(player);
						break;
					default:
						break; // This will never occur because of the predicates we've checked beforehand
				}
				sender.sendMessage(new TextComponentTranslation("commands.scalingfeast.command.success", type, player.getName(), result));
				return true;
			}
		}
		sendErrorMessage(sender, "commands.scalingfeast.command.numerr", String.valueOf(Short.MAX_VALUE));
		return false;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer serv, ICommandSender sender, String[] args, BlockPos blockPos) {
		if(args.length == 1) {
			return getListOfStringsMatchingLastWord(args, commands);
		}
		else if(args.length == 2) {
			return getListOfStringsMatchingLastWord(args, serv.getOnlinePlayerNames());
		}
		return null;
	}

	private static void sendErrorMessage(ICommandSender sender, String msg, Object... args) {
		sender.sendMessage(new TextComponentTranslation(msg, args).setStyle(new Style().setColor(TextFormatting.RED)));
	}
}
