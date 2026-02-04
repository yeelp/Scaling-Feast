package yeelp.scalingfeast.blocks;

import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockCake;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import squeek.applecore.api.AppleCoreAPI;
import squeek.applecore.api.food.FoodValues;
import squeek.applecore.api.food.IEdible;
import squeek.applecore.api.food.IEdibleBlock;
import squeek.applecore.api.food.ItemFoodProxy;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.config.ModConfig;
import yeelp.scalingfeast.init.SFItems;
import yeelp.scalingfeast.init.SFPotion;

/**
 * The Hearty Feast Block. The hunger restored scales to a player's max hunger
 * 
 * @author Yeelp
 *
 */
@MethodsReturnNonnullByDefault
public class HeartyFeastBlock extends BlockCake implements IEdibleBlock {

	/**
	 * Proxy food item when eating feast
	 * 
	 * @author Yeelp
	 *
	 */
	private static final class HeartyFeastSlice extends ItemFoodProxy {
		private final FoodValues fv;

		public HeartyFeastSlice(IEdible proxyEdible, FoodValues fv) {
			super(proxyEdible);
			this.fv = fv;
		}

		@Override
		public int getHealAmount(@Nonnull ItemStack stack) {
			return this.fv.hunger;
		}

		@Override
		public float getSaturationModifier(@Nonnull ItemStack stack) {
			return this.fv.saturationModifier;
		}
	}

	public static final PropertyInteger BITES = PropertyInteger.create("bites", 0, 6);
	private static final HashMap<UUID, FoodValues> USERS = Maps.newHashMap();
	private boolean alwaysEdible = false;
	private int food = 1;
	private static final float SAT = 0.25f;

	public HeartyFeastBlock() {
		this.blockSoundType = SoundType.CLOTH;
		this.setRegistryName("heartyfeast");
		this.setTranslationKey(ModConsts.MOD_ID + ".heartyfeast");
		this.setHardness(0.5f);
	}

	@Override
	public ItemStack getItem(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
		return new ItemStack(SFItems.heartyfeastitem);
	}

	@Override
	public FoodValues getFoodValues(@Nonnull ItemStack itemStack) {
		return new FoodValues(this.food, SAT);
	}

	@Override
	public void setEdibleAtMaxHunger(boolean value) {
		this.alwaysEdible = value;
	}

	@SuppressWarnings("unused")
	public boolean isEdibleAtMaxHunger() {
		return this.alwaysEdible;
	}

	@Override
	public boolean onBlockActivated(@Nullable World world, @Nullable BlockPos pos, @Nullable IBlockState state, EntityPlayer player, @Nullable EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
		USERS.put(player.getUniqueID(), this.setFoodValuesForPlayer(player));
		return this.eat(world, pos, state, player);
	}

	private boolean eat(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		if(!player.canEat(this.alwaysEdible)) {
			return false;
		}
		onEatenCompatibility(new ItemStack(this), player);
		int bites = state.getValue(BITES);
		if(bites < 6) {
			world.setBlockState(pos, state.withProperty(BITES, bites + 1), 3);
		}
		else {
			world.setBlockToAir(pos);
		}
		return true;
	}

	private void onEatenCompatibility(ItemStack itemStack, EntityPlayer player) {
		player.getFoodStats().addStats(new HeartyFeastSlice(this, USERS.get(player.getUniqueID())), itemStack);
		int dur = ModConfig.items.feast.heartyFeastEffectDuration;
		if(dur > 0) {
			player.addPotionEffect(new PotionEffect(SFPotion.ironstomach, dur));
		}
		USERS.remove(player.getUniqueID());
	}
	
	public FoodValues setFoodValuesForPlayer(EntityPlayer player) {
		FoodValues fv = getFoodValuesFor(player);
		this.food = fv.hunger;
		return fv;
	}

	public static int getCap() {
		return ModConfig.items.feast.heartyFeastCap < 0 ? Integer.MAX_VALUE : ModConfig.items.feast.heartyFeastCap;
	}
	
	public static FoodValues getFoodValuesFor(EntityPlayer player) {
		return new FoodValues(MathHelper.clamp(AppleCoreAPI.accessor.getMaxHunger(player) / 7, 1, getCap()), SAT);
	}
}
