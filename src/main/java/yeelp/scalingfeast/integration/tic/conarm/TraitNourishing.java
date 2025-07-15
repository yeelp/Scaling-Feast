package yeelp.scalingfeast.integration.tic.conarm;

import java.util.UUID;

import c4.conarm.common.armor.utils.ArmorHelper;
import c4.conarm.lib.traits.AbstractArmorTrait;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import yeelp.scalingfeast.api.ScalingFeastAPI;
import yeelp.scalingfeast.api.impl.SFFoodStats;
import yeelp.scalingfeast.integration.tic.TiCConsts;

public final class TraitNourishing extends AbstractArmorTrait {
	
	private static final UUID MAX_HUNGER_MOD_UUID = UUID.fromString("86dd4de8-c351-4c55-91a2-284e7ceada7d");
	private static final double AMOUNT_PER_LEVEL = 4.0;
	
	public TraitNourishing() {
		super("nourishing", TiCConsts.EXHAUSTING_COLOUR);
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public final void onPlayerTick(PlayerTickEvent evt) {
		if(evt.phase == Phase.END && evt.side == Side.SERVER) {
			EntityPlayer player = evt.player;
			SFFoodStats sfstats = ScalingFeastAPI.accessor.getSFFoodStats(player);
			IAttributeInstance instance = sfstats.getMaxHungerAttribute();
			sfstats.getMaxHungerModifier(MAX_HUNGER_MOD_UUID).ifPresent(instance::removeModifier);
			double level = ArmorHelper.getArmorAbilityLevel(player, this.identifier);
			if(level > 0) {
				sfstats.applyMaxHungerModifier(new AttributeModifier(MAX_HUNGER_MOD_UUID, "Nourishing modifier", level * AMOUNT_PER_LEVEL, 0));
			}
		}
	}
}
