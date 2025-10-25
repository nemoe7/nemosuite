package com.nemo.suite.fabric.config;

import com.nemo.suite.config.ClientConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import me.shedaniel.autoconfig.AutoConfig;

public class ModMenuIntegration implements ModMenuApi {
  @Override
  public ConfigScreenFactory<?> getModConfigScreenFactory() {
    return parent -> {
      return AutoConfig.getConfigScreen(ClientConfig.class, parent).get();
    };
  }
}
