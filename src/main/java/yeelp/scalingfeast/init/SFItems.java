package yeelp.scalingfeast.init;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.items.EnchantedIronAppleItem;
import yeelp.scalingfeast.items.ExhaustingApple;
import yeelp.scalingfeast.items.ExhaustingBlockItem;
import yeelp.scalingfeast.items.ExhaustingIngot;
import yeelp.scalingfeast.items.ExhaustingNugget;
import yeelp.scalingfeast.items.ExhaustingOreItemBlock;
import yeelp.scalingfeast.items.HeartyFeastItem;
import yeelp.scalingfeast.items.HeartyShankItem;
import yeelp.scalingfeast.items.IronAppleItem;

public class SFItems {
	public static ItemFood heartyshank;
	public static ItemFood ironapple;
	public static ItemFood enchantedironapple;
	public static ItemFood exhaustingapple;
	public static Item heartyfeastitem;
	public static ExhaustingOreItemBlock exhaustingOre;
	public static Item exhaustingBlock;
	public static Item exhaustingIngot;
	public static Item exhaustingNugget;

	public static void init() {
		heartyshank = new HeartyShankItem(ModConfig.items.shank.heartyShankFoodLevel, (float) ModConfig.items.shank.heartyShankSatLevel);
		ironapple = new IronAppleItem();
		enchantedironapple = new EnchantedIronAppleItem();
		heartyfeastitem = new HeartyFeastItem(SFBlocks.heartyfeast);
		exhaustingOre = new ExhaustingOreItemBlock(SFBlocks.exhaustingOre);
		exhaustingBlock = new ExhaustingBlockItem(SFBlocks.exhaustingBlock);
		exhaustingIngot = new ExhaustingIngot();
		exhaustingNugget = new ExhaustingNugget();
		exhaustingapple = new ExhaustingApple();
		
		ForgeRegistries.ITEMS.register(heartyshank);
		ForgeRegistries.ITEMS.register(ironapple);
		ForgeRegistries.ITEMS.register(enchantedironapple);
		ForgeRegistries.ITEMS.register(heartyfeastitem);
		ForgeRegistries.ITEMS.register(exhaustingOre);
		ForgeRegistries.ITEMS.register(exhaustingBlock);
		ForgeRegistries.ITEMS.register(exhaustingIngot);
		ForgeRegistries.ITEMS.register(exhaustingNugget);
		ForgeRegistries.ITEMS.register(exhaustingapple);		
	}

	public static void registerRenders() {
		registerRender(heartyshank, "inventory");
		registerRender(ironapple, "inventory");
		registerRender(enchantedironapple, "inventory");
		registerRender(heartyfeastitem, "inventory");
		registerRender(exhaustingNugget, "inventory");
		registerRender(exhaustingIngot, "inventory");
		registerRender(exhaustingapple, "inventory");
		exhaustingOre.getMetadataModels().forEach((i, m) -> ModelLoader.setCustomModelResourceLocation(exhaustingOre, i, m));
		registerRender(exhaustingBlock, "normal");
	}

	private static void registerRender(Item i, String variant) {
		ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(i.getRegistryName(), variant));
	}
}
