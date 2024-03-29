package yeelp.scalingfeast.handlers;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.network.BloatedHungerMessage;
import yeelp.scalingfeast.network.SatSyncMessage;
import yeelp.scalingfeast.network.SoundMessage;
import yeelp.scalingfeast.network.StarvationStatsMessage;
import yeelp.scalingfeast.network.StarveExhaustMessage;

public final class PacketHandler {
	public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(ModConsts.MOD_ID);
	public static int id = 0;

	public static final void init() {
		INSTANCE.registerMessage(StarvationStatsMessage.getMessageHandler(), StarvationStatsMessage.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(SatSyncMessage.Handler.class, SatSyncMessage.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(BloatedHungerMessage.getMessageHandler(), BloatedHungerMessage.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(StarveExhaustMessage.getMessageHandler(), StarveExhaustMessage.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(SoundMessage.class, SoundMessage.class, id++, Side.CLIENT);
	}
}
