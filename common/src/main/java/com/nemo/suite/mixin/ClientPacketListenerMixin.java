package com.nemo.suite.mixin;

import java.lang.reflect.Field;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.nemo.suite.NemoSuiteMod;
import com.nemo.suite.core.AimAssist;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
  @Inject(method = "send", at = @At("HEAD"))
  private void onSend(Packet<?> packet, CallbackInfo ci) {
    if (NemoSuiteMod.config.aimAssist.enabled && packet instanceof ServerboundInteractPacket interact) {
      try {
        Field actionField = ServerboundInteractPacket.class.getDeclaredField("action");
        actionField.setAccessible(true);
        Object action = actionField.get(interact);

        Field attackField = ServerboundInteractPacket.class.getDeclaredField("ATTACK_ACTION");
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
