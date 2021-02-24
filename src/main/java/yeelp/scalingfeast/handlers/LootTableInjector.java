package yeelp.scalingfeast.handlers;

import java.util.Arrays;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryTable;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.conditions.RandomChance;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import yeelp.scalingfeast.ModConsts;

public class LootTableInjector extends Handler 
{
	private static final String MC_CHEST_PREFIX = "minecraft:chests/";
	private static final int PREFIX_LENGTH = MC_CHEST_PREFIX.length();
	private static class SFLootTables
	{
		private static final LootCondition CHANCE = new RandomChance(1.00f);
		private static final String[] INJECTED_TABLES = new String[] {"abandoned_mineshaft", "desert_pyramid", "end_city_treasure", "jungle_temple", "nether_bridge", "simple_dungeon", "stronghold_crossing", "woodland_mansion"};
		private static ResourceLocation[] tables = new ResourceLocation[INJECTED_TABLES.length];
		static
		{
			Arrays.sort(INJECTED_TABLES);
			for(int i = 0; i < INJECTED_TABLES.length; i++)
			{
				tables[i] = new ResourceLocation(ModConsts.MOD_ID, "chests/" + INJECTED_TABLES[i]);
			}
		}
		
		static void registerTables()
		{
			for(ResourceLocation loc : tables)
			{
				LootTableList.register(loc);
			}
		}
		
		static LootPool getLootPool(String id)
		{
			return new LootPool(getLootEntries(id), new LootCondition[] {CHANCE}, new RandomValueRange(1), new RandomValueRange(0), "ScalingFeast Injector");
		}
		
		private static LootEntry[] getLootEntries(String id)
		{
			return new LootEntry[] {new LootEntryTable(getResourceLocation(id), 1, 0, new LootCondition[] {CHANCE}, "ScalingFeast Injector")};
		}
		
		private static ResourceLocation getResourceLocation(String id)
		{
			return tables[Arrays.binarySearch(INJECTED_TABLES, id)];
		}
	}
	public LootTableInjector()
	{
		super();
		SFLootTables.registerTables();
	}
	@SubscribeEvent
	public void onLoot(LootTableLoadEvent evt)
	{
		try
		{
			String table = evt.getName().toString().substring(PREFIX_LENGTH);
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
					evt.getTable().addPool(SFLootTables.getLootPool(table));
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
