package com.nemo.suite.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(net.minecraft.client.Minecraft.class)
public interface MinecraftFpsAccessor {
  @Accessor("fps")
  int getFps();
}
