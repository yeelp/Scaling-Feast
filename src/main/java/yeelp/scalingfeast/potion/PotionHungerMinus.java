package yeelp.scalingfeast.potion;

import yeelp.scalingfeast.util.SFAttributes;

public class PotionHungerMinus extends PotionBase {
	public static double modifier = -1;

	public PotionHungerMinus() {
		super(true, 0xff0000, 4, 0, false);
		this.setRegistryName("hungerminus");
		this.setPotionName("effect.hungerminus");
		this.registerPotionAttributeModifier(SFAttributes.MAX_HUNGER_MOD, "1fcbb016-35c7-442d-be6c-f5fcca90e389", modifier, 0);
	}
}
