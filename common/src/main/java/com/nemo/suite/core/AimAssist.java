package com.nemo.suite.core;

import static com.nemo.suite.util.Wrapper.getAttackCooldown;
import static com.nemo.suite.util.Wrapper.getClosestEntityToCrosshair;
import static com.nemo.suite.util.Wrapper.getClosestYawPitchBetween;
import static com.nemo.suite.util.Wrapper.getCrosshairEntity;
import static com.nemo.suite.util.Wrapper.getMainHand;
import static com.nemo.suite.util.Wrapper.getNearbyEntities;
import static com.nemo.suite.util.Wrapper.rotatePlayer;

import java.util.List;

import com.nemo.suite.NemoSuiteMod;
import com.nemo.suite.config.ClientConfig;
import com.nemo.suite.util.Timer;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public class AimAssist {
  private static ClientConfig.AimAssist config;
  private static boolean inCombat = false;
  private static Timer combatTimer = new Timer();
  private static ItemStack mainHand = ItemStack.EMPTY;

  public static void init() {
    config = NemoSuiteMod.config.aimAssist;
  }

  public static void tick() {
    mainHand = getMainHand();

    if (inCombat) {
      int combatTime = config.maxCombatTime > 0 ? config.maxCombatTime : getAttackCooldown() * 2;

      if (combatTimer.reached(combatTime) || mainHand.toString() == getMainHand().toString()) {
        combatTimer.reset();
        inCombat = false;
        return;
      }
      combatTimer.tick();

      List<Entity> entities = getNearbyEntities(Mob.class);

      if (entities.isEmpty())
        return;

      Entity closest = getClosestEntityToCrosshair(entities);

      if (closest == null || (config.stopWhenReached && getCrosshairEntity() == closest))
        return;

      float[] yawPitch = getClosestYawPitchBetween(closest);
      rotatePlayer(yawPitch[0], yawPitch[1], config.maxYawStep, config.maxPitchStep);
    }
  }

  public static void setInCombat(boolean inCombat) {
    AimAssist.inCombat = inCombat;
  }

  public static void resetCombatTimer() {
    combatTimer.reset();
  }
}
