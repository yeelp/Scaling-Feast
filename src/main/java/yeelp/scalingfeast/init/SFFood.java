package yeelp.scalingfeast.init;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.scalingfeast.ModConfig;
import yeelp.scalingfeast.items.EnchantedIronAppleItem;
import yeelp.scalingfeast.items.HeartyFeastItem;
import yeelp.scalingfeast.items.HeartyShankItem;
import yeelp.scalingfeast.items.IronAppleItem;

public class SFFood {
	public static ItemFood heartyshank;
	public static ItemFood ironapple;
	public static ItemFood enchantedironapple;
	public static Item heartyfeastitem;

	public static void init() {
		heartyshank = new HeartyShankItem(ModConfig.items.shank.heartyShankFoodLevel, (float) ModConfig.items.shank.heartyShankSatLevel);
		ironapple = new IronAppleItem();
		enchantedironapple = new EnchantedIronAppleItem();
		heartyfeastitem = new HeartyFeastItem(SFBlocks.heartyfeast);
		ForgeRegistries.ITEMS.register(heartyshank);
		ForgeRegistries.ITEMS.register(ironapple);
		ForgeRegistries.ITEMS.register(enchantedironapple);
		ForgeRegistries.ITEMS.register(heartyfeastitem);
	}

	public static void registerRenders() {
		registerRender(heartyshank);
		registerRender(ironapple);
		registerRender(enchantedironapple);
		registerRender(heartyfeastitem);
	}

	private static void registerRender(Item i) {
		ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), "inventory"));
	}
}
