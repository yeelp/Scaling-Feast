package yeelp.scalingfeast.integration.tic.tinkers;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.fluid.FluidMolten;
import slimeknights.tconstruct.smeltery.block.BlockMolten;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.handlers.Handler;
import yeelp.scalingfeast.init.SFPotion;
import yeelp.scalingfeast.integration.tic.TiCConsts;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public final class MoltenExhaustium extends FluidMolten {
	private static final Map<Potion, Integer> POTION_EFFECTS = Maps.newHashMap();
	private static final int POTION_DURATION = 200;
	private static final ITextComponent DESC = new TextComponentTranslation("tooltips.scalingfeast.exhaustion_fluid.desc").setStyle(new Style().setColor(TextFormatting.RED));
	static final Set<UUID> AFFECTED_PLAYERS = Sets.newHashSet();
	
	static void init() {
		POTION_EFFECTS.put(SFPotion.hungerminus, 20);
		POTION_EFFECTS.put(MobEffects.HUNGER, 30);
		POTION_EFFECTS.put(SFPotion.softstomach, 10);
		POTION_EFFECTS.put(null, 40);
	}
	
	public MoltenExhaustium() {
		super("exhaustion_fluid", TiCConsts.EXHAUSTING_COLOUR, FluidMolten.ICON_MetalStill, FluidMolten.ICON_MetalFlowing);
		this.setUnlocalizedName(Util.prefix(this.fluidName));
		this.setTemperature(500);
		this.setLuminosity(3);
		this.setViscosity(50000);
		this.setDensity(8000);
	}

	BlockMolten getBlockMolten() {
		return this.new BlockMoltenExhaustium();
	}
	
	static BucketTooltipHandler getTooltipHandler() {
		return new BucketTooltipHandler();
	}
	
	private static Optional<Potion> getPotionEffect(World world) {
		Map.Entry<Potion, Integer> selected = null;
		Iterator<Map.Entry<Potion, Integer>> it = POTION_EFFECTS.entrySet().iterator();
		for(int i = world.rand.nextInt(100); i >= 0 && it.hasNext(); i -= (selected = it.next()).getValue());
		return selected != null ? Optional.ofNullable(selected.getKey()) : Optional.empty();
	}
	
	static final class BucketTooltipHandler extends Handler {
		@SuppressWarnings("static-method")
		@SubscribeEvent
		public void onTooltip(ItemTooltipEvent evt) {
			ItemStack stack = evt.getItemStack();
			if(!(stack.getItem() instanceof UniversalBucket)) {
				return;
			}
			FluidStack fluidStack = ((UniversalBucket) stack.getItem()).getFluid(stack);
			if(fluidStack == null || !(fluidStack.getFluid() instanceof MoltenExhaustium)) {
				return;
			}
			addTooltipInfo(evt.getToolTip(), 1);
		}
	}
	
	private static void addTooltipInfo(List<String> tooltip, int index) {
		String text = DESC.getFormattedText();
		if(index >= tooltip.size()) {
			tooltip.add(text);
		}
		else {
			tooltip.add(index, DESC.getFormattedText());			
		}
	}
	
	private static void addTooltipInfo(List<String> tooltip) {
		addTooltipInfo(tooltip, tooltip.size());
	}
	
	public final class BlockMoltenExhaustium extends BlockMolten {
		
		public BlockMoltenExhaustium() {
			super(MoltenExhaustium.this);
			String name = "molten_" + MoltenExhaustium.this.getName();
			this.setTranslationKey(name);
			this.setRegistryName(ModConsts.MOD_ID, name);
		}
		
		@Override
		public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
			super.onEntityCollision(worldIn, pos, state, entityIn);
			if(!(entityIn instanceof EntityPlayer)) {
				return;
			}
			EntityPlayer player = (EntityPlayer) entityIn;
			UUID uuid = player.getUniqueID();
			if(player.ticksExisted % 20 != 0) {
				MoltenExhaustium.AFFECTED_PLAYERS.remove(uuid);
				return;
			}
			if(MoltenExhaustium.AFFECTED_PLAYERS.contains(uuid)) {
				return;
			}
			MoltenExhaustium.getPotionEffect(worldIn).ifPresent((potion) -> {
				int amplifier = Math.min(Optional.ofNullable(player.getActivePotionEffect(potion)).map(PotionEffect::getAmplifier).orElse(-1) + 1, 5);
				player.addPotionEffect(new PotionEffect(potion, MoltenExhaustium.POTION_DURATION, amplifier));
				ScalingFeastAPI.mutator.damageFoodStats(player, 2);
				MoltenExhaustium.AFFECTED_PLAYERS.add(player.getUniqueID());
			});
		}
		
		@Override
		public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
			MoltenExhaustium.addTooltipInfo(tooltip);
			super.addInformation(stack, worldIn, tooltip, flagIn);
		}
	}
}