package yeelp.scalingfeast.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.command.SelectorHandlerManager;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.init.SFAttributes;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.regex.Pattern;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SFCommand extends CommandBase {

	private static final String[] COMMAND_LIST = new String[] {
			"setMax",
			"setSaturation"};
	private static final Set<String> COMMANDS = Sets.newHashSet(Arrays.asList(COMMAND_LIST));
	
	private static final Pattern NUM_REGEX = Pattern.compile("^\\d+(\\.\\d+)?$");

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
		if(!(args.length >= 1 && COMMANDS.contains(args[0]))) {
			throw new WrongUsageException(getUsage(sender));
		}
		// check for invalid args, throw appropriate message
		for(String command : COMMAND_LIST) {
			if(command.equals(args[0]) && args.length != 3) {
				sendErrorMessage(sender, "commands.scalingfeast.command.usage", command);
				return;
			}
		}
		if(args[1].startsWith("@")) {
			List<EntityPlayerMP> targets = SelectorHandlerManager.matchEntities(sender, args[1], EntityPlayerMP.class);
			if(targets.isEmpty()) {
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

	private static void editStats(ICommandSender sender, String[] args, EntityPlayerMP player) {
		if(!NUM_REGEX.matcher(args[2]).matches()) {
			sendErrorMessage(sender, "commands.scalingfeast.command.notnum", args[2]);
			return;
		}
		CommandType.getCommand(args[0]).ifPresent((command) -> {
			float fVal = Float.parseFloat(args[2]);
			Optional<String> s = command.getErrorMessageBoundsIfNotInBounds(fVal);
			if(s.isPresent()) {
				sendErrorMessage(sender, "commands.scalingfeast.command.numerr", s.get(), String.valueOf(Short.MAX_VALUE));
			}
			else {
				sender.sendMessage(new TextComponentTranslation("commands.scalingfeast.command.success", command.type, player.getName(), command.editStats(player, fVal, (short) fVal)));
			}
		});
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer serv, ICommandSender sender, String[] args, @Nullable BlockPos blockPos) {
		if(args.length == 1) {
			return getListOfStringsMatchingLastWord(args, COMMANDS);
		}
		else if(args.length == 2) {
			return getListOfStringsMatchingLastWord(args, serv.getOnlinePlayerNames());
		}
		return Collections.emptyList();
	}

	private static void sendErrorMessage(ICommandSender sender, String msg, Object... args) {
		sender.sendMessage(new TextComponentTranslation(msg, args).setStyle(new Style().setColor(TextFormatting.RED)));
	}

	private enum CommandType {
		SET_MAX(false, "setMax", "max hunger base value") {
			@Override
			String editStats(EntityPlayerMP player, float fArg, short sArg) {
				int maxSet = Math.min(sArg, ScalingFeastAPI.accessor.getHungerHardCap());
				player.getEntityAttribute(SFAttributes.MAX_HUNGER_MOD).setBaseValue(maxSet - ModConsts.VANILLA_MAX_HUNGER);
				return String.valueOf(maxSet);
			}
		},
		SET_SAT(true, "setSaturation", "saturation") {
			@Override
			String editStats(EntityPlayerMP player, float fArg, short sArg) {
				float satSet = Math.min(Math.min(player.getFoodStats().getFoodLevel(), ScalingFeastAPI.accessor.getPlayerSaturationCap(player)), fArg);
				AppleCoreAPI.mutator.setSaturation(player, satSet);
				ScalingFeastAPI.mutator.capPlayerSaturation(player);
				return String.valueOf(satSet);
			}
		};

		final boolean lenient;
		final String command, type;

		CommandType(boolean lenient, String command, String type) {
			this.lenient = lenient;
			this.command = command;
            this.type = type;
        }

		private Optional<String> getErrorMessageBoundsIfNotInBounds(float arg) {
			boolean result = 0 < arg && arg < Short.MAX_VALUE;
			if(this.lenient ? arg == 0 || result : result) {
				return Optional.empty();
			}
			return Optional.of(this.lenient ? "[" : "(");
		}

		abstract String editStats(EntityPlayerMP player, float fArg, short sArg);

		static Optional<CommandType> getCommand(String arg) {
			for(CommandType type : CommandType.values()) {
				if(arg.equals(type.command)) {
					return Optional.of(type);
				}
			}
			return Optional.empty();
		}
	}
}
