package yeelp.scalingfeast.blocks;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import squeek.applecore.api.hunger.ExhaustionEvent.ExhaustionAddition;
import yeelp.scalingfeast.handlers.Handler;

public abstract class ExhaustionIncreasingBlock extends Block {

	public final static class ExhaustionHandler extends Handler {
		@SuppressWarnings("static-method")
		@SubscribeEvent(priority = EventPriority.LOWEST)
		public void onExhaustion(ExhaustionAddition evt) {
			Block block = evt.player.world.getBlockState(evt.player.getPosition().down()).getBlock();
			if(evt.player.onGround && block instanceof ExhaustionIncreasingBlock) {
				evt.deltaExhaustion *= ((ExhaustionIncreasingBlock) block).getExhaustionIncreaseMultiplier(evt.player);
			}
		}
	}
	
	public ExhaustionIncreasingBlock(Material materialIn) {
		super(materialIn);
	}
	
	
	@Override
	@ParametersAreNonnullByDefault
	public final void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
		player.addExhaustion(this.getAddedExhaustionOnBlockBreak(player));
		super.onBlockHarvested(worldIn, pos, state, player);
	}

	abstract float getExhaustionIncreaseMultiplier(@Nonnull EntityPlayer player);
	
	protected abstract float getAddedExhaustionOnBlockBreak(@Nonnull EntityPlayer player);
}
