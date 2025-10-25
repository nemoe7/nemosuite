package com.nemo.suite.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.player.LocalPlayer;

public class ReachHelper {
  @ExpectPlatform
  public static double getEntityReach(LocalPlayer player) {
    throw new UnsupportedOperationException("Platform implementation missing");
  }

  @ExpectPlatform
  public static double getBlockReach(LocalPlayer player) {
    throw new UnsupportedOperationException("Platform implementation missing");
  }
}
