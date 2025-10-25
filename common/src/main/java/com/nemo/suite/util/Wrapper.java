package com.nemo.suite.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;

public class Wrapper {
  public static Minecraft client;

  public static void init(Minecraft client) {
    Wrapper.client = client;
  }

  /**
   * @return true if the player and level are not null
   */
  public static boolean isPlayerPlaying() {
    return client.player != null && client.level != null;
  }

  /**
   * @return true if the player's attack key is down/pressed
   */
  public static boolean isAttackKeyDown() {
    return client.player != null
        && client.options.keyAttack.isDown();
  }

  /**
   * @return true if the player's attack is ready/off cooldown
   */
  public static boolean isAttackReady() {
    LocalPlayer player = client.player;
    if (player == null)
      return false;
    return player.getAttackStrengthScale(0.0f) >= 1.0f;
  }

  /**
   * @return true if the player has an Entity under their crosshair
   */
  public static boolean hasCrosshairEntity() {
    return client.crosshairPickEntity != null;
  }

  /**
   * @return the entity under the player's crosshair, or null if there is none
   */
  public static Entity getCrosshairEntity() {
    return client.crosshairPickEntity;
  }

  /**
   * Attempts to attack the specified entity
   */
  private static void playerAttackEntity(LocalPlayer player, Entity target) {
    MultiPlayerGameMode gameMode = client.gameMode;
    if (gameMode != null && player != null && target != null) {
      gameMode.attack(player, target);
      player.swing(InteractionHand.MAIN_HAND);
    }
  }

  /**
   * Attempts to attack the Entity under the player's crosshair
   */
  public static void attackCrosshairEntity() {
    LocalPlayer player = client.player;
    Entity target = getCrosshairEntity();

    if (player == null || target == null)
      return;

    playerAttackEntity(player, target);
  }

  /**
   * @return the attack range of the player (implemented in ReachHelper)
   */
  public static double getEntityReach() {
    return ReachHelper.getEntityReach(client.player);
  }

  /**
   * @return the reach of the player (implemented in ReachHelper)
   */
  public static double getBlockReach() {
    return ReachHelper.getBlockReach(client.player);
  }
}
