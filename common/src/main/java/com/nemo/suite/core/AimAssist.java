package com.nemo.suite.core;

import static com.nemo.suite.NemoSuiteMod.printDebug;
import static com.nemo.suite.util.Wrapper.getAttackCooldown;
import static com.nemo.suite.util.Wrapper.getClosestEntityToCrosshair;
import static com.nemo.suite.util.Wrapper.getClosestYawPitchToEntity;
import static com.nemo.suite.util.Wrapper.getCrosshairEntity;
import static com.nemo.suite.util.Wrapper.getMainHand;
import static com.nemo.suite.util.Wrapper.getNearbyEntities;
import static com.nemo.suite.util.Wrapper.isAttackReady;
import static com.nemo.suite.util.Wrapper.rotatePlayer;

import java.util.List;

import com.nemo.suite.NemoSuiteMod;
import com.nemo.suite.config.ClientConfig;
import com.nemo.suite.config.ClientConfig.AimAssist.CombatTimeTypeEnum;
import com.nemo.suite.util.Timer;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public class AimAssist {
  private static ClientConfig.AimAssist config;
  private static boolean inCombat = false;
  private static Timer combatTimer = new Timer();
  private static ItemStack mainHand = ItemStack.EMPTY;
  private static Entity target = null;

  public static void init() {
    config = NemoSuiteMod.config.aimAssist;
  }

  public static void tick() {
    target = null;

    if (inCombat) {
      int combatTime = config.combatTime > 0 ? config.combatTime : 20 + getAttackCooldown();
      boolean isAttackReady = isAttackReady();
      if (config.combatTimeType == CombatTimeTypeEnum.ON_SWING ||
          (config.combatTimeType == CombatTimeTypeEnum.ON_READY && isAttackReady)) {
        combatTimer.tick();
        printDebug("in combat, timer: {}/{}", combatTimer.getTicks(), combatTime);
      }

      if (combatTimer.reached(combatTime) ||
          !mainHand.getDisplayName().equals(getMainHand().getDisplayName())) {
        combatTimer.reset();
        inCombat = false;
        printDebug("exiting combat");
        return;
      }

      List<Entity> entities = getNearbyEntities(Mob.class);
      if (entities.isEmpty()) {
        printDebug("no nearby entities");
        return;
      }

      Entity closest = getClosestEntityToCrosshair(entities);
      if (closest == null || (config.stopWhenReached && getCrosshairEntity() == closest)) {
        printDebug("no valid target");
        return;
      }

      target = closest;
    }

    mainHand = getMainHand();
  }

  public static void renderTick() {
    if (inCombat && target != null) {
      float[] yawPitch = getClosestYawPitchToEntity(target);
      rotatePlayer(yawPitch[0], yawPitch[1], config.yawScale, config.pitchScale);
    }
  }

  public static void setInCombat(boolean inCombat) {
    AimAssist.inCombat = inCombat;
  }

  public static void resetCombatTimer() {
    combatTimer.reset();
  }
}
