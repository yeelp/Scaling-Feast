package yeelp.scalingfeast.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.util.SFAttributes;

public class PotionHungerPlus extends PotionBase
{
	public static double modifier = 1;
	public PotionHungerPlus()
	{
		super(false, 0x00ff00, 3, 0, false);
		this.setRegistryName("hungerplus");
		this.setPotionName("effect.hungerplus");
		this.setBeneficial();
		this.registerPotionAttributeModifier(SFAttributes.MAX_HUNGER_MOD, "8660b357-5fbe-45cd-b6fb-fba4cca2c3d0", modifier, 0);
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
			int newCap = ScalingFeastAPI.accessor.getModifiedFoodCap(player) - (amplifier + 1);
			if(fs.getFoodLevel() > newCap)
			{
				AppleCoreAPI.mutator.setHunger(player, newCap);
			}
		}
		super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
	}
}
