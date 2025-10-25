package com.nemo.suite.fabric;

import net.fabricmc.api.ModInitializer;

import com.nemo.suite.NemoSuiteMod;

public final class NemoSuiteModFabric implements ModInitializer {
  @Override
  public void onInitialize() {
    // This code runs as soon as Minecraft is in a mod-load-ready state.
    // However, some things (like resources) may still be uninitialized.
    // Proceed with mild caution.

    // Run our common setup.
    NemoSuiteMod.init();
  }
}
