package yeelp.scalingfeast.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import yeelp.scalingfeast.ModConsts;

/**
 * The Iron Stomach effect. This reduces exhaustion by 20% per level
 * @author Yeelp
 *
 */
public class PotionIronStomach extends PotionBase
{
	public PotionIronStomach() 
	{
		super(false, 0xCCCCCC, 0, 0);
		this.setRegistryName("ironstomach");
		this.setPotionName("effect.ironstomach");
		this.setBeneficial();
		this.setIconIndex(0, 0);
	}
}
