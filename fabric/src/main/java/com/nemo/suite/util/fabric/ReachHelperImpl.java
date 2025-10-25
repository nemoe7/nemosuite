package com.nemo.suite.util.fabric;

import net.minecraft.client.player.LocalPlayer;

public class ReachHelperImpl {
  public static double getEntityReach(LocalPlayer player) {
    return player.isCreative() ? 6.0D : 3.0D;
  }

  public static double getBlockReach(LocalPlayer player) {
    return player.isCreative() ? 5.0D : 4.5D;
  }
}
