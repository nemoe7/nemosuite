package com.nemo.suite.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import static com.nemo.suite.NemoSuiteMod.MOD_ID;


@Config(name = MOD_ID)
public class ClientConfig implements ConfigData {
  public boolean enabled = true;

  @ConfigEntry.Gui.TransitiveObject
  public AutoAttack autoAttack = new AutoAttack();

  public boolean allowSectionSign = true;

  public class AutoAttack {
    public boolean enabled = true;
    public int delayTicks = 0;
  }
}
