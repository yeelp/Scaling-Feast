package yeelp.scalingfeast.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import yeelp.scalingfeast.ModConsts;

public final class ExhaustingNugget extends Item {
	public ExhaustingNugget() {
		this.setRegistryName("exhaustingnugget");
		this.setUnlocalizedName(ModConsts.MOD_ID + ".exhaustingnugget");
		this.setCreativeTab(CreativeTabs.MISC);
	}
}
