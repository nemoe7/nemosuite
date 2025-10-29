package com.nemo.suite.forge;

import com.nemo.suite.NemoSuiteMod;
import com.nemo.suite.config.ClientConfig;
import com.nemo.suite.forge.client.NemoSuiteModForgeClient;

import dev.architectury.platform.forge.EventBuses;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigGuiHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(NemoSuiteMod.MOD_ID)
public final class NemoSuiteModForge {
  public NemoSuiteModForge() {
    // Submit our event bus to let Architectury API register our content on the
    // right time.
    EventBuses.registerModEventBus(NemoSuiteMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

    // Run our common setup.
    NemoSuiteMod.init();

    ModLoadingContext.get().registerExtensionPoint(
        ConfigGuiHandler.ConfigGuiFactory.class,
        () -> new ConfigGuiHandler.ConfigGuiFactory(
            (mc, parent) -> AutoConfig.getConfigScreen(ClientConfig.class, parent).get()));

    if (FMLEnvironment.dist == Dist.CLIENT)
      NemoSuiteModForgeClient.init();
  }
}
