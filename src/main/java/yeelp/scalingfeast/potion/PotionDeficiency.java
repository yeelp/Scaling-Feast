package yeelp.scalingfeast.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.api.ScalingFeastAPI;

public class PotionDeficiency extends PotionBase
{
	public PotionDeficiency()
	{
		super(true, 0x570d0d, -1, -1, true);
		this.setRegistryName("deficiency");
		this.setPotionName("effect.deficiency");
	}
	
	@Override
	public boolean isReady(int duration, int amplifier)
	{
		return duration >= 1;
	}
	
	@Override
	public void performEffect(EntityLivingBase entityLivingBase, int amplifier)
	{
		if(entityLivingBase instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entityLivingBase;
			ScalingFeastAPI.mutator.deductFoodStats(player, amplifier+1);
		}
	}
}
