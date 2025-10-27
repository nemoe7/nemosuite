package com.nemo.suite.mixin;

import java.lang.reflect.Field;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nemo.suite.NemoSuiteMod;
import com.nemo.suite.core.AimAssist;
import com.nemo.suite.util.ReflectionUtils;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
  @Inject(method = "send", at = @At("HEAD"))
  private void onSend(Packet<?> packet, CallbackInfo ci) {
    if (NemoSuiteMod.config.aimAssist.enabled && packet instanceof ServerboundInteractPacket interact) {
      try {
        Field actionField = ReflectionUtils.findField(
            ServerboundInteractPacket.class,
            "action",
            "f_134031_");
        actionField.setAccessible(true);
        Object action = actionField.get(interact);

        Field attackField = ReflectionUtils.findField(
            ServerboundInteractPacket.class,
            "ATTACK_ACTION",
            "f_179595_");
        attackField.setAccessible(true);
        Object attackAction = attackField.get(null);

        if (action == attackAction) {
          AimAssist.setInCombat(true);
          AimAssist.resetCombatTimer();
        }
      } catch (ReflectiveOperationException e) {
        e.printStackTrace();
      }
    }
  }
}
