package com.nemo.suite.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.nemo.suite.NemoSuiteMod;
import com.nemo.suite.config.ClientConfig;

import net.minecraft.SharedConstants;

@Mixin(SharedConstants.class)
public class SharedConstantsMixin {

  @Inject(method = "isAllowedChatCharacter", at = @At("HEAD"), cancellable = true)
  private static void allowSectionSign(char c, CallbackInfoReturnable<Boolean> cir) {
    ClientConfig config = NemoSuiteMod.config;
    if (config.allowSectionSign) {
      if (c >= ' ' && c != 127) {
        cir.setReturnValue(true);
        cir.cancel();
      }
    }
  }
}
