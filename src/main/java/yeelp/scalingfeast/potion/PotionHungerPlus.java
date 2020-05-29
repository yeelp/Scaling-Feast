package yeelp.scalingfeast.potion;

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
}
