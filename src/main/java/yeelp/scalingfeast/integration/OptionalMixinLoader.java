package yeelp.scalingfeast.integration;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import yeelp.scalingfeast.ModConsts.IntegrationIds;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

@MCVersion("1.12.2")
@Name("ScalingFeast-OptionalMixinLoader")
@SortingIndex(-5000)
@SuppressWarnings("unused")
public class OptionalMixinLoader {

    private static final String MIXIN_FILE = "mixin.scalingfeast.json";
    private static final Map<String, Supplier<IOptionalMixinProvider>> PROVIDERS = Maps.newHashMap();
    private static final Set<String> MIXINS_ENQUEUED = Sets.newHashSet();
    private static boolean loaded = false;

    static {
        PROVIDERS.put(IntegrationIds.APPLESKIN_ID, () -> new IOptionalMixinProvider() {
            @Override
            public String getModID() {
                return IntegrationIds.APPLESKIN_ID;
            }

            @Override
            public boolean enabled() {
                return true;
            }
        });
        PROVIDERS.put(IntegrationIds.LEMONSKIN_ID, () -> new IOptionalMixinProvider() {
            @Override
            public String getModID() {
                return IntegrationIds.LEMONSKIN_ID;
            }

            @Override
            public boolean enabled() {
                return true;
            }
        });
    }

    public OptionalMixinLoader() {
        try {
            //get class to load it first. If class is present, then load mixins, else don't even initialize
            Class<?> registryClass = fermiumbooter.FermiumRegistryAPI.class;
            PROVIDERS.values().forEach((sup) -> {
                IOptionalMixinProvider provider = sup.get();
                String modid = provider.getModID();
                fermiumbooter.FermiumRegistryAPI.enqueueMixin(true, String.format("mixin.scalingfeast.%s.json", modid), () -> provider.enabled() && Loader.instance().getModList().stream().map(ModContainer::getModId).anyMatch(modid::equals));
                MIXINS_ENQUEUED.add(modid);
            });
            loaded = true;
        }
        catch(NoClassDefFoundError e) {
            //no logger ready to log message, just do nothing.
        }
    }

    public static boolean wereMixinsEnqueued(String modid) {
        return MIXINS_ENQUEUED.contains(modid);
    }

    public static boolean wasFermiumBooterPresent() {
        return loaded;
    }
}
