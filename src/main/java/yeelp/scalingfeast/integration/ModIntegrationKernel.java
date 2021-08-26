package yeelp.scalingfeast.integration;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraftforge.fml.common.Loader;
import yeelp.scalingfeast.ModConsts;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.integration.module.IIntegratable;
import yeelp.scalingfeast.integration.module.SOLCarrotModule;
import yeelp.scalingfeast.integration.module.SpiceOfLifeModule;

public final class ModIntegrationKernel {
	private static final Map<String, Supplier<IIntegratable>> integratableMods = new HashMap<String, Supplier<IIntegratable>>();
	private static final List<IIntegratable> loadedIntegrations = new LinkedList<IIntegratable>();

	static {
		integratableMods.put(ModConsts.SOLCARROT_ID, () -> new SOLCarrotModule());
		integratableMods.put(ModConsts.SPICEOFLIFE_ID, () -> new SpiceOfLifeModule());
	}

	public static void load() {
		integratableMods.forEach((mod, sup) -> {
			if(Loader.isModLoaded(mod)) {
				ScalingFeast.info("Scaling Feast found " + mod + "!");
				IIntegratable integration = sup.get();
				if(integration.integrate()) {
					loadedIntegrations.add(integration);
				}
				else {
					ScalingFeast.err("Encountered problems trying to load integration for mod " + mod);
				}
			}
		});
	}
}
