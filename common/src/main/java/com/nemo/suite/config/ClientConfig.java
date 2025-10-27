package com.nemo.suite.config;

import static com.nemo.suite.NemoSuiteMod.MOD_ID;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = MOD_ID)
public class ClientConfig implements ConfigData {

  public boolean enabled = true;

  @ConfigEntry.Gui.TransitiveObject
  public AimAssist aimAssist = new AimAssist();

  public boolean allowSectionSign = true;

  @ConfigEntry.Gui.TransitiveObject
  public AutoAttack autoAttack = new AutoAttack();

  public static class AutoAttack {

    public boolean enabled = true;

    @ConfigEntry.BoundedDiscrete(max = 100)
    public int delayTicks = 0;
  }

  public static class AimAssist {
    public boolean enabled = true;

    @ConfigEntry.BoundedDiscrete(max = 100)
    public int maxYawStep = 50;

    @ConfigEntry.BoundedDiscrete(max = 100)
    public int maxPitchStep = 50;

    @ConfigEntry.BoundedDiscrete(max = 600)
    @ConfigEntry.Gui.Tooltip
    public int maxCombatTime = 0;

    public boolean stopWhenReached = false;
  }
}
