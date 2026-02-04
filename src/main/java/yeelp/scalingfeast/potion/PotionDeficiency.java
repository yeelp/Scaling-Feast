package yeelp.scalingfeast.potion;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.api.ScalingFeastAPI;

@ParametersAreNonnullByDefault
public class PotionDeficiency extends PotionBase {
	public PotionDeficiency() {
		super(true, 0x570d0d, -1, -1, true);
		this.setRegistryName("deficiency");
		this.setPotionName("effect.deficiency");
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return duration >= 1;
	}

	@Override
	public void performEffect(EntityLivingBase entityLivingBase, int amplifier) {
		if(entityLivingBase instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLivingBase;
			ScalingFeastAPI.mutator.deductFoodStats(player, amplifier + 1);
		}
	}

	@Override
	public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource, EntityLivingBase entityLivingBaseIn, int amplifier, double health) {
		if(entityLivingBaseIn instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLivingBaseIn;
			ScalingFeastAPI.mutator.deductFoodStats(player, amplifier + 1);
		}
	}
}
