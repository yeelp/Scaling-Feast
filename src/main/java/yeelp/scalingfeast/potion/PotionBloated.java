package yeelp.scalingfeast.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.api.ScalingFeastAPI;

public class PotionBloated extends PotionBase
{
	public PotionBloated()
	{
		super(false, 0xffff00, 2, 0, false);
		this.setRegistryName("bloated");
		this.setPotionName("effect.bloated");
		this.setBeneficial();
	}
	
	@Override
	public void removeAttributesModifiersFromEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier)
	{
		if(entityLivingBaseIn instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entityLivingBaseIn;
			ScalingFeastAPI.accessor.getBloatedHunger(player).setBloatedAmount((short) (ScalingFeastAPI.accessor.getBloatedHungerAmount(player) - 4*(amplifier+1)));
		}
		super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
	}
	
	@Override
	public void applyAttributesModifiersToEntity(EntityLivingBase entityLivingBaseIn, AbstractAttributeMap attributeMapIn, int amplifier)
	{
		if(entityLivingBaseIn instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entityLivingBaseIn;
			ScalingFeastAPI.accessor.getBloatedHunger(player).setBloatedAmount((short) (ScalingFeastAPI.accessor.getBloatedHungerAmount(player) + 4*(amplifier+1)));
		}
		super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);
	}
}
