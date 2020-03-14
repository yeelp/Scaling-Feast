package yeelp.scalingfeast.potion;

import net.minecraft.potion.Potion;

/**
 * The Iron Stomach effect. This reduces exhaustion by 20% per level
 * @author Andrew
 *
 */
public class PotionIronStomach extends Potion
{

	public PotionIronStomach() 
	{
		super(false, 0xCCCCCC);
		this.setRegistryName("ironstomach");
		this.setPotionName("effect.ironstomach");
		this.setBeneficial();
	}

}
