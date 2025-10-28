package com.nemo.suite.fabric.client;

import com.nemo.suite.NemoSuiteMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public final class NemoSuiteModFabricClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    // This entrypoint is suitable for setting up client-specific logic, such as
    // rendering.
    NemoSuiteMod.initClient();

    WorldRenderEvents.BEFORE_ENTITIES.register(context -> NemoSuiteMod.onRenderTick(context.tickDelta()));
  }
}
