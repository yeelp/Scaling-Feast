package yeelp.scalingfeast.integration.tic.tinkers;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.google.common.base.Functions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.fluid.FluidMolten;
import slimeknights.tconstruct.smeltery.block.BlockMolten;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.init.SFPotion;
import yeelp.scalingfeast.integration.tic.TiCConsts;

public final class MoltenExhaustium extends FluidMolten {
	private static final Map<Potion, Integer> POTION_EFFECTS = Maps.newHashMap();
	private static final int POTION_DURATION = 200;
	static final Set<UUID> AFFECTED_PLAYERS = Sets.newHashSet();
	
	static void init() {
		POTION_EFFECTS.put(SFPotion.hungerminus, 20);
		POTION_EFFECTS.put(MobEffects.HUNGER, 30);
		POTION_EFFECTS.put(SFPotion.softstomach, 10);
		POTION_EFFECTS.put(null, 40);
		
		POTION_EFFECTS.entrySet().stream().map(Functions.compose((k) -> k == null ? "null" : k.getRegistryName().toString(), Map.Entry::getKey)).forEach(ScalingFeast::debug);
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
	
	protected static final Optional<Potion> getPotionEffect(World world) {
		Map.Entry<Potion, Integer> selected = null;
		Iterator<Map.Entry<Potion, Integer>> it = POTION_EFFECTS.entrySet().iterator();
		for(int i = world.rand.nextInt(100); i >= 0 && it.hasNext(); i -= (selected = it.next()).getValue());
		return selected != null ? Optional.ofNullable(selected.getKey()) : Optional.empty();
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
	}
}