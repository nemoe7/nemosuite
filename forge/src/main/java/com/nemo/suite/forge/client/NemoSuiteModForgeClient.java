package com.nemo.suite.forge.client;

import com.nemo.suite.NemoSuiteMod;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = NemoSuiteMod.MOD_ID, value = Dist.CLIENT)
public class NemoSuiteModForgeClient {

  public static void init() {
    NemoSuiteMod.initClient();
  }

  @SubscribeEvent
  public static void onRender(TickEvent.RenderTickEvent event) {
    NemoSuiteMod.onRenderTick(event.renderTickTime);
  }
}
