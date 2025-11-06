package com.nemo.suite.util;

import java.util.List;
import java.util.function.Function;

import com.nemo.suite.mixin.MinecraftFpsAccessor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class Wrapper {
  public static Minecraft client;

  public static void init(Minecraft client) {
    Wrapper.client = client;
  }

  /**
   * @return true if the player and level are not null
   */
  public static boolean isPlayerPlaying() {
    return client != null && client.player != null && client.level != null;
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
   * @return true if the player attacked the Entity under their crosshair
   */
  public static boolean hasAttackedCrosshairEntity() {
    LocalPlayer player = client.player;
    Entity entity = getCrosshairEntity();
    if (player == null || entity == null)
      return false;
    return !isAttackReady() && player.swinging && !(entity.isAttackable() && entity.isAlive());
  }

  /**
   * @return attack cooldown duration in ticks
   */
  public static int getAttackCooldown() {
    double attackSpeed = client.player.getAttributeValue(Attributes.ATTACK_SPEED); // includes weapon modifiers
    return (int) Math.ceil(20.0 / attackSpeed);
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
   * @return the item in the player's main hand
   */
  public static ItemStack getMainHand() {
    return client.player.getMainHandItem();
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

  /**
   * @param entityClass class of entity to look for
   * @return all entities of the specified class within the player's attack range
   */
  @SuppressWarnings("unchecked")
  public static List<Entity> getNearbyEntities(Class<? extends Entity> entityClass) {
    LocalPlayer player = client.player;
    if (player == null)
      return List.of();

    double range = getEntityReach();
    AABB area = new AABB(
        player.getX() - range, player.getY() - range, player.getZ() - range,
        player.getX() + range, player.getY() + range, player.getZ() + range);

    return client.level.getEntitiesOfClass((Class<Entity>) entityClass, area);
  }

  /**
   * Original code by lilgallon (https://github.com/lilgallon)
   *
   * @param entities list of entities to scan
   * @return the closest entity from the list from the player's crosshair
   */
  public static Entity getClosestEntityToCrosshair(List<Entity> entities) {
    float minDist = Float.MAX_VALUE;
    Entity closest = null;

    for (Entity entity : entities) {
      if (entity == null || entity.isRemoved() || entity.isInvulnerable()
          || (entity instanceof LivingEntity living && living.isDeadOrDying()))
        continue;

      // Get distance between the two entities (rotations)
      float[] yawPitch = getClosestYawPitchBetween(client.player, entity);

      // Compute the distance from the player's crosshair
      float distYaw = Mth.abs(Mth.wrapDegrees(yawPitch[0] - client.player.getRotationVector().y));
      float distPitch = Mth.abs(Mth.wrapDegrees(yawPitch[1] - client.player.getRotationVector().x));
      float dist = Mth.sqrt(distYaw * distYaw + distPitch * distPitch);

      if (dist < minDist) {
        closest = entity;
        minDist = dist;
      }
    }

    return closest;
  }

  /**
   * Original code by lilgallon (https://github.com/lilgallon)
   *
   * @param source the source entity
   * @param target the target entity
   * @return the yaw and pitch to the closest visible scaled corner of the target
   */
  public static float[] getClosestYawPitchBetween(Entity source, Entity target) {
    Vec3 eyePos = source.position().add(0, source.getEyeHeight(), 0);
    AABB box = target.getBoundingBox();
    Vec3 center = box.getCenter();

    float srcYaw = source.getYRot();
    float srcPitch = source.getXRot();
    double reach = getEntityReach();

    // Helper lambda to test if a rotation hits the target
    Function<float[], Entity> rayHit = (rot) -> {
      Vec3 dir = Vec3.directionFromRotation(rot[1], rot[0]);
      Vec3 end = eyePos.add(dir.scale(reach));

      // Check for blocks first
      BlockHitResult blockHit = source.level.clip(new ClipContext(
          eyePos, end,
          ClipContext.Block.COLLIDER,
          ClipContext.Fluid.NONE,
          source));

      // Shorten ray if it hits a block
      Vec3 blockEnd = blockHit.getType() == HitResult.Type.MISS ? end : blockHit.getLocation();

      // Then check for entities up to that point
      EntityHitResult entityHit = ProjectileUtil.getEntityHitResult(
          source,
          eyePos,
          blockEnd,
          source.getBoundingBox().expandTowards(dir.scale(reach)).inflate(1.0),
          e -> e != source && e.isPickable(),
          reach * reach);

      return entityHit != null ? entityHit.getEntity() : null;
    };

    // === 1. Try yaw-only first ===
    float[] yawPitch = Wrapper.getYawPitchBetween(
        eyePos.x, eyePos.y, eyePos.z,
        center.x, center.y, center.z);
    yawPitch[1] = srcPitch;

    Entity hit = rayHit.apply(yawPitch);
    if (hit == target)
      return yawPitch;

    // === 2. Scan up/down the entity’s height ===
    float[] best = { srcYaw, srcPitch };
    float bestDelta = Float.MAX_VALUE;

    Vec3 bottom = new Vec3(center.x, box.minY, center.z);
    Vec3 top = new Vec3(center.x, box.maxY, center.z);

    for (float f : new float[] { 0.1f, 0.3f, 0.5f, 0.7f, 0.9f }) {
      Vec3 sample = bottom.add(top.subtract(bottom).scale(f));
      float[] test = Wrapper.getYawPitchBetween(
          eyePos.x, eyePos.y, eyePos.z,
          sample.x, sample.y, sample.z);

      hit = rayHit.apply(test);
      if (hit == target) {
        float delta = Mth.abs(test[0] - srcYaw) + Mth.abs(test[1] - srcPitch);
        if (delta < bestDelta) {
          best = test;
          bestDelta = delta;
        }
      }
    }

    return best;
  }

  /**
   * @param target the target entity
   * @return the yaw and pitch to the closest visible scaled corner of the target
   */
  public static float[] getClosestYawPitchToEntity(Entity target) {
    return getClosestYawPitchBetween(client.player, target);
  }

  /**
   * Original code by lilgallon (https://github.com/lilgallon)
   *
   * @param sourceX x position for source
   * @param sourceY y position for source
   * @param sourceZ z position for source
   * @param targetX x position for target
   * @param targetY y position for target
   * @param targetZ z position for target
   * @return the [yaw, pitch] difference between the source and the target
   */
  public static float[] getYawPitchBetween(
      double sourceX, double sourceY, double sourceZ,
      double targetX, double targetY, double targetZ) {

    double diffX = targetX - sourceX;
    double diffY = targetY - sourceY;
    double diffZ = targetZ - sourceZ;

    double dist = Mth.sqrt((float) (diffX * diffX + diffZ * diffZ));

    float yaw = (float) ((Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F);
    float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);

    return new float[] { yaw, pitch };
  }

  /**
   * Smoothly rotates the player toward the target yaw/pitch using mouse-like
   * input.
   *
   * @param targetYaw   target yaw
   * @param targetPitch target pitch
   * @param yawScale    max yaw change per tick
   * @param pitchScale  max pitch change per tick
   */
  public static void rotatePlayer(float targetYaw, float targetPitch, float yawScale, float pitchScale) {
    LocalPlayer player = client.player;
    if (player == null)
      return;

    float srcYaw = player.getYRot();
    float srcPitch = player.getXRot();

    float yawDiff = Mth.wrapDegrees(targetYaw - srcYaw);
    float pitchDiff = Mth.wrapDegrees(targetPitch - srcPitch);

    float fps = ((MinecraftFpsAccessor) client).getFps();
    float dt = 1f / fps;

    // nonlinear scaling: 50 = 1s, 100 = ~instant
    float t = yawScale / 100f;
    float duration = (float) Math.pow(2.0, (1.0 - t) * 7.0); // steeper curve
    duration /= 128f; // normalize so 50 ≈ 1s

    float progress = dt / duration;
    progress = Mth.clamp(progress, 0f, 1f);

    float yawStep = yawDiff * progress;
    float pitchStep = pitchDiff * progress;

    float newYaw = srcYaw + yawStep;
    float newPitch = Mth.clamp(srcPitch + pitchStep, -90f, 90f);

    player.setYRot(newYaw);
    player.setXRot(newPitch);
  }
}
