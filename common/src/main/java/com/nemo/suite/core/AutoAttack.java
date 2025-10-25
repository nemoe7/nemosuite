package com.nemo.suite.core;

import static com.nemo.suite.util.Wrapper.attackCrosshairEntity;
import static com.nemo.suite.util.Wrapper.getCrosshairEntity;
import static com.nemo.suite.util.Wrapper.isAttackKeyDown;
import static com.nemo.suite.util.Wrapper.isAttackReady;

import com.nemo.suite.NemoSuiteMod;
import com.nemo.suite.config.ClientConfig;
import com.nemo.suite.util.Timer;

import net.minecraft.world.entity.Entity;

public class AutoAttack {
  private static final Timer timer = new Timer();
  private static ClientConfig.AutoAttack config;

  public static void init() {
    config = NemoSuiteMod.config.autoAttack;
  }

  public static void tick() {
    if (!config.enabled)
      return;

    if (!isAttackReady())
      timer.reset();
    else
      timer.tick();

    if (isAttackKeyDown() && isAttackReady() && timer.reached(config.delayTicks)) {
      Entity target = getCrosshairEntity();
      if (target != null && !target.isRemoved()) {
        attackCrosshairEntity();
        timer.reset();
      }
    }
  }
}
