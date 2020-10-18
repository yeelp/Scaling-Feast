package yeelp.scalingfeast.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.player.IPlayer;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenGetter;

@ZenExpansion("crafttweaker.player.IPlayer")
@ZenRegister
public class CTIPlayerExt
{
	@ZenGetter("sfStats")
	public static SFStats getSFStats(IPlayer player)
	{
		return new SFStats(player);
	}
}
