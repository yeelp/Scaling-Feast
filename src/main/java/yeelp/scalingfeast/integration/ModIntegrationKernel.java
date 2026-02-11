package yeelp.scalingfeast.integration;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import yeelp.scalingfeast.ScalingFeast;
import yeelp.scalingfeast.integration.module.solcarrot.SOLCarrotModule;
import yeelp.scalingfeast.integration.module.spiceoflife.SpiceOfLifeModule;
import yeelp.scalingfeast.integration.skins.AbstractFruitSkinIntegration;
import yeelp.scalingfeast.integration.skins.AppleSkinIntegration;
import yeelp.scalingfeast.integration.skins.LemonSkinIntegration;
import yeelp.scalingfeast.integration.tic.conarm.SFConarmIntegration;
import yeelp.scalingfeast.integration.tic.tinkers.SFTinkerIntegration;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static yeelp.scalingfeast.ModConsts.IntegrationIds.*;

@SuppressWarnings("Convert2MethodRef")
public final class ModIntegrationKernel {
	private static final Map<String, Supplier<IIntegratable>> integratableMods = Maps.newHashMap();
	private static final List<IIntegratable> loadedIntegrations = Lists.newLinkedList();
	static {
		//can't use method reference since that will cause a Bootstrap something or other error. Use lambdas.
		integratableMods.put(SOLCARROT_ID, () -> new SOLCarrotModule());
		integratableMods.put(SPICEOFLIFE_ID, () -> new SpiceOfLifeModule());
		integratableMods.put(TCONSTRUCT_ID, () -> new SFTinkerIntegration());
		integratableMods.put(CONARM_ID, () -> new SFConarmIntegration());
		integratableMods.put(APPLESKIN_ID, () -> AppleSkinIntegration.getInstance());
		integratableMods.put(LEMONSKIN_ID, () -> LemonSkinIntegration.getInstance());
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

	public static Optional<AbstractFruitSkinIntegration> getCurrentSkinIntegration() {
		for(AbstractFruitSkinIntegration integration : ImmutableList.of(LemonSkinIntegration.getInstance(), AppleSkinIntegration.getInstance())) {
			if(loadedIntegrations.contains(integration)) {
				return Optional.of(integration);
			}
		}
		return Optional.empty();
	}
}
