package yeelp.scalingfeast.potion;

public class PotionBloated extends PotionBase
{
	public PotionBloated()
	{
		super(false, 0xffff00, 2, 0, false);
		this.setRegistryName("bloated");
		this.setPotionName("effect.bloated");
		this.setBeneficial();
	}
}
