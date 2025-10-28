package com.nemo.suite.config;

import static com.nemo.suite.NemoSuiteMod.MOD_ID;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.network.chat.TranslatableComponent;

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

    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int yawScale = 50;

    @ConfigEntry.BoundedDiscrete(min = 1, max = 100)
    public int pitchScale = 50;

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public CombatTimeTypeEnum combatTimeType = CombatTimeTypeEnum.ON_READY;

    @ConfigEntry.BoundedDiscrete(max = 600)
    @ConfigEntry.Gui.Tooltip
    public int combatTime = 0;

    public boolean stopWhenReached = false;

    public enum CombatTimeTypeEnum {
      ON_SWING("text.autoconfig.nemosuite.option.aimAssist.combatTimeType.ON_SWING"),
      ON_READY("text.autoconfig.nemosuite.option.aimAssist.combatTimeType.ON_READY");

      private final String translation;

      CombatTimeTypeEnum(String i18nKey) {
        translation = new TranslatableComponent(i18nKey).getString();
      }

      @Override
      public String toString() {
        return translation;
      }
    }
  }
}
