package yeelp.scalingfeast.integration.conarm;

import c4.conarm.lib.traits.AbstractArmorTrait;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import squeek.applecore.api.AppleCoreAPI;
import yeelp.scalingfeast.api.ScalingFeastAPI;

public final class TraitTorporic extends AbstractArmorTrait {

	private static final String KEY = "scalingfeast.conarm.torporic.activated";
	
	public TraitTorporic() {
		super("torporic", 0xC69174);
	}

	@Override
	public void onAbilityTick(int level, World world, EntityPlayer player) {
		int food = player.getFoodStats().getFoodLevel();
		boolean triggered = getTraitTriggerState(player);
		if(food == 0 && !triggered) {
			getPersistedTag(player).setBoolean(KEY, true);
			if(Math.random() < 0.20) {
				ScalingFeastAPI.accessor.getSFFoodStats(player).addBloatedAmount((short) Math.ceil(AppleCoreAPI.accessor.getMaxHunger(player) * 0.25));
			}
		}
		else if (food > 0 && triggered) {
			getPersistedTag(player).setBoolean(KEY, false);
		}
	}
	
	private static final boolean getTraitTriggerState(EntityPlayer player) {
		return getPersistedTag(player).getBoolean(KEY);
	}
	
	private static final NBTTagCompound getPersistedTag(EntityPlayer player) {
		NBTTagCompound tag = player.getEntityData();
		NBTTagCompound persistTag;
		if(tag.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
			persistTag = tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		}
		else {
			tag.setTag(EntityPlayer.PERSISTED_NBT_TAG, persistTag = new NBTTagCompound());
		}
		return persistTag;
	}
}
