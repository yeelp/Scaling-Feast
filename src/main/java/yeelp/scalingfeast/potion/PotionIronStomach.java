package yeelp.scalingfeast.potion;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.util.SFAttributes;

/**
 * The Iron Stomach effect. This reduces exhaustion by 20% per level
 * @author Yeelp
 *
 */
public class PotionIronStomach extends PotionBase
{
	public static double reductionMultiplier = -0.2;
	public PotionIronStomach() 
	{
		super(false, 0xCCCCCC, 0, 0, false);
		this.setRegistryName("ironstomach");
		this.setPotionName("effect.ironstomach");
		this.setBeneficial();
		this.registerPotionAttributeModifier(SFAttributes.EXHAUSTION_RATE, "39e48ce7-2afc-4f80-83b2-11eccd2faabc", reductionMultiplier, 2);
	}
}
