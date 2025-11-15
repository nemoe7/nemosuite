package com.nemo.suite.config;

import static com.nemo.suite.NemoSuiteMod.MOD_ID;

import org.jetbrains.annotations.NotNull;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry.Translatable;

@Config(name = MOD_ID)
public class ClientConfig implements ConfigData {

  public boolean enabled = true;

  @ConfigEntry.Gui.TransitiveObject
  public AimAssist aimAssist = new AimAssist();

  public boolean allowSectionSign = true;

  @ConfigEntry.Gui.TransitiveObject
  public AutoAttack autoAttack = new AutoAttack();

  public boolean printDebug = false;

  public static class AutoAttack {

    public boolean enabled = true;

    @ConfigEntry.BoundedDiscrete(max = 100)
    public int delayTicks = 0;

    @Override
    public String toString() {
      return "{" +
          "enabled=" + enabled +
          ", delayTicks=" + delayTicks +
          '}';
    }
  }

  public static class AimAssist {
    public boolean enabled = true;

    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int yawScale = 50;

    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int pitchScale = 50;

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public CombatTimeTypeEnum combatTimeType = CombatTimeTypeEnum.ON_READY;

    @ConfigEntry.BoundedDiscrete(max = 600)
    @ConfigEntry.Gui.Tooltip
    public int combatTime = 0;

    @ConfigEntry.BoundedDiscrete(min = 0, max = 20)
    @ConfigEntry.Gui.Tooltip
    public int distanceThreshold = 0;

    public boolean stopWhenReached = false;

    public boolean actionBar = true;

    @ConfigEntry.BoundedDiscrete(min = 20, max = 100)
    public int actionBarWidth = 60;

    public enum CombatTimeTypeEnum implements Translatable{
      ON_SWING("text.autoconfig.nemosuite.option.aimAssist.combatTimeType.ON_SWING"),
      ON_READY("text.autoconfig.nemosuite.option.aimAssist.combatTimeType.ON_READY");

      private final String key;

      CombatTimeTypeEnum(String key) {
        this.key = key;
      }

      @Override
      public @NotNull String getKey() {
        return key;
      }
    }

    @Override
    public String toString() {
      return "{" +
          "enabled=" + enabled +
          ", yawScale=" + yawScale +
          ", pitchScale=" + pitchScale +
          ", combatTimeType=" + combatTimeType +
          ", combatTime=" + combatTime +
          ", stopWhenReached=" + stopWhenReached +
          '}';
    }
  }

  @Override
  public String toString() {
    return "{" +
        "enabled=" + enabled +
        ", aimAssist=" + aimAssist +
        ", allowSectionSign=" + allowSectionSign +
        ", autoAttack=" + autoAttack +
        ", printDebug=" + printDebug +
        '}';
  }
}
