package re.cod.hypnos;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import re.cod.hypnos.config.Config;

public class HypnosMod implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("Hypnos");

	@Override
	public void onInitialize() {
		Config.get();
		LOGGER.info("Loaded");
	}
}
