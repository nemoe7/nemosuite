package com.nemo.suite.util.forge;

import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.common.ForgeMod;

public class ReachHelperImpl {
  public static double getEntityReach(LocalPlayer player) {
    return player.getAttributeValue(ForgeMod.ATTACK_RANGE.get()) + (player.isCreative() ? 3.0D : 0.0D);
  }

  public static double getBlockReach(LocalPlayer player) {
    return player.getAttributeValue(ForgeMod.REACH_DISTANCE.get()) + (player.isCreative() ? 0.5D : 0.0D);
  }
}
