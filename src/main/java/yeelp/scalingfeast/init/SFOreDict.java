package yeelp.scalingfeast.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public final class SFOreDict {

	public static final String SUFFIX = "Exhausting";

	public static final void init() {
		oredict(SFItems.exhaustingNugget, "nugget");
		oredict(SFItems.exhaustingIngot, "ingot");
		oredict(SFItems.exhaustingBlock, "block");
		OreDictionary.registerOre("ore" + SUFFIX, new ItemStack(SFItems.exhaustingOre, 1, OreDictionary.WILDCARD_VALUE));
	}

	private static final void oredict(Item i, String type) {
		OreDictionary.registerOre(type + SUFFIX, i);
	}
}
