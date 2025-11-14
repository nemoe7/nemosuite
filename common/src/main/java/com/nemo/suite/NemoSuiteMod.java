package com.nemo.suite;

import static com.nemo.suite.util.Wrapper.isPlayerPlaying;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.nemo.suite.config.ClientConfig;
import com.nemo.suite.core.AimAssist;
import com.nemo.suite.core.AutoAttack;
import com.nemo.suite.util.Wrapper;

import dev.architectury.event.events.client.ClientTickEvent;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.event.ConfigSerializeEvent;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;

public final class NemoSuiteMod {
  public static final String MOD_ID = "nemosuite";
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
  public static ClientConfig config;

  public static void init() {
    LOGGER.info("Hello from NemoSuite!");
    // Write common init code here.
  }

  public static void initClient() {
    AutoConfig.register(ClientConfig.class, Toml4jConfigSerializer::new);

    AutoConfig.getConfigHolder(ClientConfig.class)
        .registerSaveListener(new ConfigSerializeEvent.Save<ClientConfig>() {
          @Override
          public InteractionResult onSave(ConfigHolder<ClientConfig> holder, ClientConfig config) {
            initConfig();
            LOGGER.info("Updated config.", config);
            return InteractionResult.SUCCESS;
          }
        });
    initConfig();

    // Register client-side logic here.
    ClientTickEvent.CLIENT_PRE.register(Wrapper::init);
    ClientTickEvent.CLIENT_POST.register(NemoSuiteMod::onClientPostTick);
  }

  private static void initConfig() {
    config = AutoConfig.getConfigHolder(ClientConfig.class).getConfig();
    AimAssist.init();
    AutoAttack.init();
    printDebug("Initialized config: {}", config);
  }

  private static void onClientPostTick(Minecraft client) {
    if (!config.enabled) {
      return;
    }

    if (isPlayerPlaying()) {
      if (config.aimAssist.enabled)
        AimAssist.tick();
      if (config.autoAttack.enabled)
        AutoAttack.tick();
    }
  }

  public static void onRenderTick(float tickDelta) {
    if (isPlayerPlaying()) {
      if (config.aimAssist.enabled)
        AimAssist.renderTick();
    }
  }

  public static void printDebug(String key, Object... args) {
    if (config.printDebug) LOGGER.info(key, args);
  }
}
