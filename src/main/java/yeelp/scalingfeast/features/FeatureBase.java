package yeelp.scalingfeast.features;

import java.util.Arrays;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.handlers.Handler;

public abstract class FeatureBase<Config> {
	
	private static final String IS_NUMBER_REGEX = "-?\\d+";

	private int[] dimensionList;

	@Nonnull
	public abstract Handler getFeatureHandler();
	
	protected abstract Config getConfig();
	
	protected abstract String[] getDimensionListFromConfig();
	
	protected abstract FilterListType getFilterListTypeFromConfig();
	
	protected abstract String getName();
	
	protected final void update() {
		String[] arr = this.getDimensionListFromConfig();
		if(arr != null) {
			this.dimensionList = Arrays.stream(arr).filter((s) -> s.matches(IS_NUMBER_REGEX)).mapToInt(Integer::parseInt).sorted().distinct().toArray();			
			if(arr.length != this.dimensionList.length) {
				ScalingFeast.warn(String.format("%s feature has a dimension list that contains one or more invalid entries!", this.getName()));
			}
		}
	}
	
	protected boolean isInValidDimension(EntityPlayer player) {
		return this.getFilterListTypeFromConfig().doesPassFilterList(this.dimensionList, player.dimension);
	}

}
