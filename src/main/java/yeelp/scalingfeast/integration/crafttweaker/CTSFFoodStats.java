package yeelp.scalingfeast.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import net.minecraft.entity.player.EntityPlayer;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenSetter;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.api.impl.SFFoodStats;

@ZenClass("mods.scalingfeast.SFStats")
@ZenRegister
public class CTSFFoodStats {
	private final EntityPlayer player;
	private final SFFoodStats sfstats;

	public CTSFFoodStats(IPlayer ctplayer) {
		this.player = CraftTweakerMC.getPlayer(ctplayer);
		this.sfstats = ScalingFeastAPI.accessor.getSFFoodStats(this.player);
	}

	@ZenGetter("bloatedHungerAmount")
	public short getBloatedHunger() {
		return this.sfstats.getBloatedHungerAmount();
	}

	@ZenSetter("bloatedHungerAmount")
	public void setBloatedHunger(short amount) {
		this.sfstats.setBloatedHungerAmount(amount);
	}

	@ZenGetter("starvationTrackerCount")
	public short getStarvationTrackerCount() {
		return this.sfstats.getStarvationTrackerCount();
	}
	
	@ZenGetter("consecutiveStarvations")
	public short getStarvationCountAllTime() {
		return this.sfstats.getStarvationCountAllTime();
	}
	
	@ZenGetter("bonusStarvationDamage")
	public int getBonusStarvationDamage() {
		return this.sfstats.getTotalBonusDynamicStarvationDamage();
	}
}
