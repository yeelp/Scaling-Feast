package yeelp.scalingfeast.network;

import net.minecraft.nbt.NBTTagShort;
import net.minecraftforge.common.capabilities.Capability;
import yeelp.scalingfeast.capability.IBloatedHunger;
import yeelp.scalingfeast.capability.impl.BloatedHunger;

/**
 * Scaling Feast's message for tracking bloated hunger
 * 
 * @author Yeelp
 *
 */
public class BloatedHungerMessage extends AbstractCapabilityMessageShort<IBloatedHunger> {

	public BloatedHungerMessage() {

	}

	public BloatedHungerMessage(IBloatedHunger cap) {
		super(cap);
	}

	public static MessageHandler<NBTTagShort, IBloatedHunger, BloatedHungerMessage> getMessageHandler() {
		return new MessageHandler<NBTTagShort, IBloatedHunger, BloatedHungerMessage>() {

			@Override
			protected Capability<IBloatedHunger> getCapabilityInstance() {
				return BloatedHunger.cap;
			}
		};
	}
}
