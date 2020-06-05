package yeelp.scalingfeast.potion;

import yeelp.scalingfeast.util.SFAttributes;

public class PotionSoftStomach extends PotionBase
{
	public static double increaseMultiplier = -0.2;
	public PotionSoftStomach()
	{
		super(true, 0xb8db1a, 5, 0, false);
		this.setRegistryName("softstomach");
		this.setPotionName("effect.softstomach");
	}

}
