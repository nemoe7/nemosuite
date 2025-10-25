package com.nemo.suite.util.forge;

import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.common.ForgeMod;

public class ReachHelperImpl {
  public static double getEntityReach(LocalPlayer player) {
    return player.getAttributeValue(ForgeMod.ATTACK_RANGE.get());
  }

  public static double getBlockReach(LocalPlayer player) {
    return player.getAttributeValue(ForgeMod.REACH_DISTANCE.get());
  }
}
