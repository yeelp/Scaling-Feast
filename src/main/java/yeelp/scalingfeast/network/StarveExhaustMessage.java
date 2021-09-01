package yeelp.scalingfeast.network;

import net.minecraft.nbt.NBTTagFloat;
import net.minecraftforge.common.capabilities.Capability;
import yeelp.scalingfeast.capability.IStarveExhaustionTracker;
import yeelp.scalingfeast.capability.impl.StarveExhaustionTracker;

public class StarveExhaustMessage extends AbstractCapabilityMessageFloat<IStarveExhaustionTracker> {

	public StarveExhaustMessage() {

	}

	public StarveExhaustMessage(IStarveExhaustionTracker tracker) {
		super(tracker);
	}
	
	public static MessageHandler<NBTTagFloat, IStarveExhaustionTracker, StarveExhaustMessage> getMessageHandler() {
		return new MessageHandler<NBTTagFloat, IStarveExhaustionTracker, StarveExhaustMessage>() {
			
			@Override
			protected Capability<IStarveExhaustionTracker> getCapabilityInstance() {
				return StarveExhaustionTracker.cap;
			}
		};
	}
}
