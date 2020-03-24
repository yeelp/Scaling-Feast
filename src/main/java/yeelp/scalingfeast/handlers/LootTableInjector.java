package yeelp.scalingfeast.handlers;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class LootTableInjector extends Handler 
{
	@SubscribeEvent
	public void onLoot(LootTableLoadEvent evt)
	{
		LootCondition chance = new RandomChance(1.00f);
		try
		{
			String table = evt.getName().toString().substring(new String("minecraft:chests/").length());
			switch(table)
			{
				case "abandoned_mineshaft":
				case "desert_pyramid":
				case "end_city_treasure":
				case "jungle_temple":
				case "nether_bridge":
				case "simple_dungeon":
				case "stronghold_crossing":
				case "woodland_mansion":
					LootEntry entry = new LootEntryTable(new ResourceLocation("scalingfeast:chests/"+table), 1, 0, null, "ScalingFeast Injector");
					LootPool pool = new LootPool(new LootEntry[] {entry}, new LootCondition[] {chance}, new RandomValueRange(1), new RandomValueRange(0), "ScalingFeast Injector");
					evt.getTable().addPool(pool);
				default:
					return;
			}
		}
		//If this happens, then clearly the loot table name didn't begin with "minecraft:chests/", so it's not a loot table we care about. Ignore.
		catch(IndexOutOfBoundsException e)
		{
			return;
		}
	}
}
