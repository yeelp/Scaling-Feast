package yeelp.scalingfeast.capability;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import yeelp.scalingfeast.handlers.PacketHandler;

/**
 * Base Capability for Scaling Feast
 * @author Yeelp
 *
 * @param <T> The type of NBT tags this capability is stored in.
 */
public interface SFCapabilityBase<T extends NBTBase> extends ICapabilitySerializable<T> {
	
	/**
	 * A skeletal {@link IStorage} implementation. All it needs is the class of the NBT tags being stored.
	 * @author Yeelp
	 *
	 * @param <NBT> The type of NBT tags being stored.
	 * @param <Cap> The capability being stored.
	 */
	final class SFCapStorage<NBT extends NBTBase, Cap extends SFCapabilityBase<NBT>> implements IStorage<Cap> {
		private final Class<NBT> nbtClass;

		/**
		 * Make a new Storage
		 * @param clazz the class of NBT tags stored.
		 */
		public SFCapStorage(Class<NBT> clazz) {
			this.nbtClass = clazz;
		}

		@Override
		public NBTBase writeNBT(Capability<Cap> capability, Cap instance, EnumFacing side) {
			return instance.serializeNBT();
		}

		@Override
		public void readNBT(Capability<Cap> capability, Cap instance, EnumFacing side, NBTBase nbt) {
			if(this.nbtClass.isInstance(nbt)) {
				instance.deserializeNBT(this.nbtClass.cast(nbt));
			}
		}
	}

	/**
	 * A skeletal factory that creates default instances of the capability
	 * @author Yeelp
	 *
	 * @param <NBT> The kind of NBT tags stored
	 * @param <Cap> The kind of Capability stored.
	 */
	final class SFCapFactory<NBT extends NBTBase, Cap extends SFCapabilityBase<NBT>> implements Callable<Cap> {

		private final Supplier<Cap> sup;
		
		/**
		 * Create a new factory
		 * @param sup The Supplier that generates default instances of the capability.
		 */
		public SFCapFactory(Supplier<Cap> sup) {
			this.sup = sup;
		}

		@Override
		public Cap call() {
			return this.sup.get();
		}
	}
	
	/**
	 * Register a Capability
	 * @param capClass The class being registered
	 * @param nbtClass The class of the NBT tags being stored
	 * @param factorySup a Supplier that generates default instances
	 */
	static <NBT extends NBTBase, C extends SFCapabilityBase<NBT>> void register(Class<C> capClass, Class<NBT> nbtClass, Supplier<C> factorySup) {
        //noinspection Convert2Diamond
        CapabilityManager.INSTANCE.register(capClass, new SFCapStorage<NBT, C>(nbtClass), new SFCapFactory<NBT, C>(factorySup));
	}
	
	/**
	 * Get a message for syncing with the contents of this Capability.
	 * @return an IMessage with the contents of this capability.
	 */
	IMessage getIMessage();
	
	/**
	 * Sync this Capability with the server side if needed.
	 * @param player the player to sync for.
	 */
	default void sync(EntityPlayer player) {
		if(!player.world.isRemote) {
			PacketHandler.INSTANCE.sendTo(this.getIMessage(), (EntityPlayerMP) player);
		}
	}
}
