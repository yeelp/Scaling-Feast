package yeelp.scalingfeast.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.api.ScalingFeastAPI;

/**
 * The Metabolism effect. This regens hunger over time.
 * 
 * @author Yeelp
 *
 */
public class PotionMetabolism extends PotionBase {
	public PotionMetabolism() {
		super(false, 0xF0B78C, 1, 0, false);
		this.setRegistryName("metabolism");
		this.setPotionName("effect.metabolism");
		this.setBeneficial();
	}

	@Override
	public void performEffect(EntityLivingBase entity, int amplifier) {
		if(entity instanceof EntityPlayer && !entity.world.isRemote) {
			EntityPlayer player = (EntityPlayer) entity;
			FoodStats fs = player.getFoodStats();
			if(fs.getFoodLevel() < AppleCoreAPI.accessor.getMaxHunger(player)) {
				AppleCoreAPI.mutator.setHunger(player, fs.getFoodLevel() + 1);
			}
			else if(fs.getSaturationLevel() < AppleCoreAPI.accessor.getMaxHunger(player)) {
				AppleCoreAPI.mutator.setSaturation(player, fs.getSaturationLevel() + 1);
			}
			ScalingFeastAPI.mutator.capPlayerSaturation(player);
		}
	}

	// This should tick exactly as often as regen of the same level. We borrow the
	// same code.
	@Override
	public boolean isReady(int duration, int amplifier) {
		int k = 50 >> amplifier;

		if(k > 0) {
			return duration % k == 0;
		}
		return true;
	}
}
