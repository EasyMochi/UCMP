package moe.okaeri.ucmp;

import net.fabricmc.api.ModInitializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UCMP implements ModInitializer {

	public static final String MOD_ID = "ucmp";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("UCMP >> Unofficial Cobblemon Patch loaded!");
	}
}
