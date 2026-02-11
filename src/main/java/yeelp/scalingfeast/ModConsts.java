package yeelp.scalingfeast;

public interface ModConsts {
	String MOD_ID = "scalingfeast";
	String MOD_NAME = "Scaling Feast";

	String MOD_VERSION = "@version@";
	@SuppressWarnings("unused")
	String MC_VERSION = "1.12.2";
	int VANILLA_MAX_HUNGER = 20;
	float VANILLA_MAX_SAT = 20.0f;

	String CLIENT_PROXY = "yeelp.scalingfeast.proxy.ClientProxy";
	String SERVER_PROXY = "yeelp.scalingfeast.proxy.Proxy";

	interface IntegrationIds {
		String APPLESKIN_ID = "appleskin";
		String LEMONSKIN_ID = "lemonskin";
		String SPICEOFLIFE_ID = "spiceoflife";
		String TCONSTRUCT_ID = "tconstruct";
		String CONARM_ID = "conarm";
		String SOLCARROT_ID = "solcarrot";
		String FERMIUM_ID = "fermiumbooter";
	}
}
