package yeelp.scalingfeast.integration;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.integration.module.IIntegratable;
import yeelp.scalingfeast.integration.module.solcarrot.SOLCarrotModule;
import yeelp.scalingfeast.integration.module.spiceoflife.SpiceOfLifeModule;
import yeelp.scalingfeast.integration.tinkers.SFTinkerIntegration;

public final class ModIntegrationKernel {
	private static final Map<String, Supplier<IIntegratable>> integratableMods = new HashMap<String, Supplier<IIntegratable>>();
	private static final List<IIntegratable> loadedIntegrations = new LinkedList<IIntegratable>();

	static {
		integratableMods.put(ModConsts.SOLCARROT_ID, () -> new SOLCarrotModule());
		integratableMods.put(ModConsts.SPICEOFLIFE_ID, () -> new SpiceOfLifeModule());
		integratableMods.put(ModConsts.TCONSTRUCT_ID, () -> new SFTinkerIntegration());
	}

	public static void load() {
		integratableMods.forEach((mod, sup) -> {
			if(Loader.isModLoaded(mod)) {
				ScalingFeast.info("Scaling Feast found " + mod + "!");
				loadedIntegrations.add(sup.get());
			}
		});
	}
	
	public static void preInit(FMLPreInitializationEvent evt) {
		process((i) -> i.preIntegrate(evt));
	}
	
	public static void init(FMLInitializationEvent evt) {
		process((i) -> i.integrate(evt));
	}
	
	public static void postInit(FMLPostInitializationEvent evt) {
		process((i) -> i.postIntegrate(evt));
	}
	
	private static void process(Predicate<IIntegratable> p) {
		loadedIntegrations.removeIf(p.negate());
	}
}
