package yeelp.scalingfeast.potion;

/**
 * The Iron Stomach effect. This reduces exhaustion by 20% per level
 * 
 * @author Yeelp
 *
 */
public class PotionIronStomach extends PotionExhaustion {

	public PotionIronStomach() {
		super(0xCCCCCC, 0, 0, -0.2f);
		this.setRegistryName("ironstomach");
		this.setPotionName("effect.ironstomach");
	}
}
