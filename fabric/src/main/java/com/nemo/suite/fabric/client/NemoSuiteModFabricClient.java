package com.nemo.suite.fabric.client;

import com.nemo.suite.NemoSuiteMod;

import net.fabricmc.api.ClientModInitializer;

public final class NemoSuiteModFabricClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    // This entrypoint is suitable for setting up client-specific logic, such as
    // rendering.
    NemoSuiteMod.initClient();
  }
}
