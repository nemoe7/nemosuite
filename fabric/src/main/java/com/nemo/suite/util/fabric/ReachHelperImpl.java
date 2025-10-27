package com.nemo.suite.util.fabric;

import net.minecraft.client.player.LocalPlayer;

public class ReachHelperImpl {
  private static final double DEFAULT_ENTITY_REACH = 3.0D;
  private static final double DEFAULT_BLOCK_REACH = 4.5D;
  public static double getEntityReach(LocalPlayer player) {
    return player.isCreative() ? DEFAULT_ENTITY_REACH + 2.0D : DEFAULT_ENTITY_REACH;
  }

  public static double getBlockReach(LocalPlayer player) {
    return player.isCreative() ? DEFAULT_BLOCK_REACH + 0.5D : DEFAULT_BLOCK_REACH;
  }
}
