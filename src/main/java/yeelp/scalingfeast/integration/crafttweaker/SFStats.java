package yeelp.scalingfeast.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import squeek.applecore.api.AppleCoreAPI;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.handlers.CapabilityHandler;
import yeelp.scalingfeast.util.FoodCapModifier;
import yeelp.scalingfeast.util.IBloatedHunger;
import yeelp.scalingfeast.util.IFoodCap;
import yeelp.scalingfeast.util.IFoodCapModifier;

@ZenClass("mods.scalingfeast.SFStats")
@ZenRegister
public class SFStats
{
	private final EntityPlayer player;
	private final IFoodCap cap;
	private final IFoodCapModifier mod;
	private final IBloatedHunger bloat;
	
	public SFStats(IPlayer ctplayer)
	{
		this.player = CraftTweakerMC.getPlayer(ctplayer);
		this.cap = ScalingFeastAPI.accessor.getFoodCap(this.player);
		this.mod = ScalingFeastAPI.accessor.getFoodCapModifier(this.player);
		this.bloat = ScalingFeastAPI.accessor.getBloatedHunger(this.player);
	}
	
	@ZenGetter("maxHunger")
	public short getMaxHunger()
	{
		return this.cap.getMaxFoodLevel(this.mod);
	}
	
	@ZenGetter("unmodifiedMaxHunger")
	public short getUnmodifiedMaxHunger()
	{
		return this.cap.getUnmodifiedMaxFoodLevel();
	}
	
	@ZenSetter("unmodifiedMaxHunger")
	public void setUnmodifiedMaxHunger(short max)
	{
		this.cap.setMax(max);
		if(!this.player.world.isRemote)
		{
			CapabilityHandler.syncCap(this.player);
		}
	}
	
	@ZenGetter("bloatedHungerAmount")
	public short getBloatedHunger()
	{
		return this.bloat.getBloatedAmount();
	}
	
	@ZenSetter("bloatedHungerAmount")
	public void setBloatedHunger(short amount)
	{
		ScalingFeastAPI.mutator.setBloatedHunger(this.player, amount);
	}
	
	@ZenMethod
	public SFModifier getModifier(String id)
	{
		return new SFModifier(mod.getAllModifiers().get(id));
	}
	
	@ZenMethod 
	public void setModifier(String id, SFModifier mod)
	{
		this.mod.setModifier(id, mod.getValue(), FoodCapModifier.Operation.values()[mod.getOperation()]);
		update();
	}
	
	@ZenMethod
	public void setModifier(String id, float amount, byte op)
	{
		if(0 <= op && op < 3)
		{
			this.mod.setModifier(id, amount, FoodCapModifier.Operation.values()[op]);
			update();
		}
		else
		{
			throw new RuntimeException("op argument for setModifier must be either 0, 1 or 2!");
		}
	}
	
	private void update()
	{
		short currMax = ScalingFeastAPI.accessor.getFoodCap(this.player).getMaxFoodLevel(this.mod);
		FoodStats fs = this.player.getFoodStats();
		if(fs.getFoodLevel() > currMax)
		{
			AppleCoreAPI.mutator.setHunger(this.player, currMax);
			ScalingFeastAPI.mutator.capPlayerSaturation(this.player);
		}
		if(fs.getSaturationLevel() > currMax)
		{
			AppleCoreAPI.mutator.setSaturation(this.player, currMax);
			ScalingFeastAPI.mutator.capPlayerSaturation(this.player);
		}
		CapabilityHandler.sync(this.player);
	}
}
