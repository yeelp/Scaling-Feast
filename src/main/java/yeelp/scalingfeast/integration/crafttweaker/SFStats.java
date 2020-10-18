package yeelp.scalingfeast.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import net.minecraft.entity.player.EntityPlayer;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenGetter;
import stanhebben.zenscript.annotations.ZenMethod;
import stanhebben.zenscript.annotations.ZenSetter;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.util.FoodCapModifier;
import yeelp.scalingfeast.util.IBloatedHunger;
import yeelp.scalingfeast.util.IFoodCap;
import yeelp.scalingfeast.util.IFoodCapModifier;

@ZenClass("mods.scalingfeast.SFStats")
@ZenRegister
public class SFStats
{
	private final IFoodCap cap;
	private final IFoodCapModifier mod;
	private final IBloatedHunger bloat;
	
	public SFStats(IPlayer ctplayer)
	{
		EntityPlayer player = CraftTweakerMC.getPlayer(ctplayer);
		this.cap = ScalingFeastAPI.accessor.getFoodCap(player);
		this.mod = ScalingFeastAPI.accessor.getFoodCapModifier(player);
		this.bloat = ScalingFeastAPI.accessor.getBloatedHunger(player);
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
	}
	
	@ZenGetter("bloatedHungerAmount")
	public short getBloatedHunger()
	{
		return this.bloat.getBloatedAmount();
	}
	
	@ZenSetter("bloatedHungerAmount")
	public void setBloatedHunger(short amount)
	{
		this.bloat.setBloatedAmount(amount);
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
	}
	
	@ZenMethod
	public void setModifier(String id, float amount, byte op)
	{
		if(0 <= op && op < 3)
		{
			this.mod.setModifier(id, amount, FoodCapModifier.Operation.values()[op]);
		}
		else
		{
			throw new RuntimeException("op argument for setModifier must be either 0, 1 or 2!");
		}
	}
}
