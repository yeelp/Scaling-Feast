package yeelp.scalingfeast.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.util.SFAttributes;

public class PotionHungerMinus extends PotionBase
{
	public static double modifier = -1;
	public PotionHungerMinus()
	{
		super(true, 0xff0000, 4, 0, false);
		this.setRegistryName("hungerminus");
		this.setPotionName("effect.hungerminus");
		this.registerPotionAttributeModifier(SFAttributes.MAX_HUNGER_MOD, "1fcbb016-35c7-442d-be6c-f5fcca90e389", modifier, 0);
	}
	
	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier)
	{
		if(entityLivingBaseIn instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entityLivingBaseIn;
			FoodStats fs = player.getFoodStats();
			int newCap = ScalingFeastAPI.accessor.getModifiedFoodCap(player) - (amplifier + 1);
			if(fs.getFoodLevel() > newCap)
			{
				AppleCoreAPI.mutator.setHunger(player, newCap);
			}
		}
		super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);
	}
	
	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier)
	{
		if(entityLivingBaseIn instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entityLivingBaseIn;
			FoodStats fs = player.getFoodStats();
			int newCap = ScalingFeastAPI.accessor.getModifiedFoodCap(player) + (amplifier + 1);
			if(fs.getFoodLevel() > newCap)
			{
				AppleCoreAPI.mutator.setHunger(player, newCap);
			}
		}
		super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
	}
}
