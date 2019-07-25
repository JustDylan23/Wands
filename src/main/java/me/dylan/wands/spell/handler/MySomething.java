package me.dylan.wands.spell.handler;

import com.destroystokyo.paper.block.TargetBlockInfo;
import com.destroystokyo.paper.entity.TargetEntityInfo;
import me.dylan.wands.Main;
import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.SpellEffectUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;

/**
 * @author jetp250
 * <p>
 * This class was created by jetp250 for testing purposes.
 */

public class MySomething extends Behaviour implements Castable {

    private static final int MAX_DISTANCE = 30;
    private final Map<UUID, SpellDataThing> activePlayers = new HashMap<>();

    public MySomething() {
        super(new AbstractBuilder.BaseMeta());
    }

    @Override
    public String getDisplayName() {
        return "MySpellTest";
    }

    @Override
    public boolean cast(Player player, String wandDisplayName) {
        SpellDataThing existingThing = activePlayers.remove(player.getUniqueId());
        if (existingThing != null) {
            existingThing.whatsExecutingIt.cancel();
            shootProjectileThingies(existingThing.whoOwnsIt, existingThing.whereIsIt);
        } else {
            Location spawnLoc = player.getLocation().add(0.0, 1.0, 0.0);
            BukkitTask task = Bukkit.getScheduler().runTaskTimer(Main.getPlugin(), () -> showParticleThing(player, spawnLoc), 0L, 1L);
            SpellDataThing dataStuffs = new SpellDataThing(spawnLoc, player, task);

            activePlayers.put(player.getUniqueId(), dataStuffs);
        }
        return true;
    }

    private void showParticleRing(Location at) {
        Location[] particleLocations = SpellEffectUtil.getCircleFromPlayerView(at, 0.9, 18, 0.0);
        Particle.DustOptions dustOpt = new Particle.DustOptions(Color.WHITE, 1.0f);
        for (Location point : particleLocations) {
            at.getWorld().spawnParticle(Particle.CLOUD, point, 2, 0.0, 0.0, 0.0, 0.00);
        }
    }

    private void showParticleThing(Player player, Location loc) {
        Location target = SpellEffectUtil.getSpellLocation(MAX_DISTANCE, player, false);
        if (target == null) {
            Location playerLoc = player.getEyeLocation();
            target = playerLoc.add(playerLoc.getDirection().multiply(MAX_DISTANCE));
        }

        Location toTarget = target.clone().subtract(loc);
        loc.setDirection(toTarget.toVector());

        showParticleRing(loc);
    }

    private void shootProjectileThingies(Player player, Location loc) {
        showParticleRing(loc);

        final Vector baseDir = loc.getDirection();

        TargetEntityInfo targetInfo = player.getTargetEntityInfo(MAX_DISTANCE, false);
        Location targetLocation;
        if (targetInfo != null) {
            targetLocation = targetInfo.getHitVector().toLocation(player.getWorld());
        } else {
            TargetBlockInfo targetBlockInfo = player.getTargetBlockInfo(MAX_DISTANCE);
            if (targetBlockInfo != null) {
                targetLocation = targetBlockInfo.getBlock().getLocation().toCenterLocation();
            } else {
                Location playerLoc = player.getEyeLocation();
                targetLocation = playerLoc.add(playerLoc.getDirection().multiply(MAX_DISTANCE));
            }
        }

        MyProjectile[] projectiles = new MyProjectile[30];

        new BukkitRunnable() {
            ThreadLocalRandom RNG = ThreadLocalRandom.current();
            float TWO_PI = (float) (2 * Math.PI);
            float MIN_ADD = 1 / 4.0f * TWO_PI; // 90 degrees
            float MAX_ADD = 3 / 4.0f * TWO_PI; // 270 degrees

            int count = 0;
            float angle = RNG.nextFloat() * TWO_PI;

            @Override
            public void run() {
                Vector dir = baseDir.clone();
                float yaw = 100.0f * (float) Math.sin(angle);
                float pitch = 100.0f * (float) Math.cos(angle);

                Vector randomization = new Location(null, 0, 0, 0, yaw, pitch).getDirection();
                dir.add(randomization.multiply(1.0f));//RNG.nextGaussian() * (1.2 - 0.25) + 0.25));

                angle += RNG.nextGaussian() * (MAX_ADD - MIN_ADD) + MIN_ADD;

                MyProjectile projectile = new MyProjectile(player, loc, targetLocation, dir);
                projectiles[count++] = projectile;

                if (count == projectiles.length)
                    cancel();
            }
        }.runTaskTimer(Main.getPlugin(), 0L, 4L);

        new BukkitRunnable() {
            int numDead = 0;

            @Override
            public void run() {
                if (targetInfo != null) {
                    targetInfo.getEntity().getLocation(targetLocation);
                }
                showParticleRing(loc);
                for (int i = 0; i < projectiles.length; ++i) {
                    MyProjectile proj = projectiles[i];
                    if (proj == null)
                        continue;

                    if (!proj.tryLiveOneMoreTick()) {
                        numDead++;
                        // Swap with the last one, which will be ignored by the loop from now on
                        projectiles[i] = null;
                    }
                }
                if (numDead == projectiles.length) {
                    cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0L, 1L);
    }

    @Override
    public Behaviour getBehaviour() {
        return this;
    }

    private static final class SpellDataThing {
        Location whereIsIt;
        Player whoOwnsIt;
        BukkitTask whatsExecutingIt;

        SpellDataThing(Location where, Player whose, BukkitTask runner) {
            this.whereIsIt = where;
            this.whoOwnsIt = whose;
            this.whatsExecutingIt = runner;
        }
    }

    private static final class MyProjectile {

        private static final double INERTIA = 6;
        private static final double INERTIA_MULTIPLIER = 0.9; // Makes '6' become '1' in 10 ticks
        private static final double SPEED = 1.0;
        private static final double HITBOX_SIZE = 0.4;
        private static final double DAMAGE = 2.0;

        private static final Particle.DustOptions DUST_OPT = new Particle.DustOptions(Color.WHITE, 2.5f);

        private static final Predicate<Entity> GLOBAL_TARGET_PREDICATE = e -> {
            return e instanceof Damageable;
        };
        private final Location victimLocation;
        private final Player owner;
        private final Predicate<Entity> raytraceEntityPredicate;
        private Location pos;
        private Vector dir;
        private double inertia;

        private boolean missed = false;

        public MyProjectile(Player owner, Location whereami, Location whatDoIKill, Vector direction) {
            this.pos = whereami.clone();
            this.owner = owner;
            this.victimLocation = whatDoIKill;
            this.dir = direction;//.clone().subtract(whereami).toVector().normalize();


/*            ThreadLocalRandom rng = ThreadLocalRandom.current();

            Location temp = whereami.clone();
            temp.setDirection(dir);
            temp.setYaw(whereami.getYaw() + (float)rng.nextDouble(45.0,90.0) * (rng.nextBoolean() ? 1 : -1));
            temp.setPitch(whereami.getPitch() + (float) ThreadLocalRandom.current().nextDouble(40.0, 80.0) * (rng.nextBoolean() ? 1 : -1));

            dir = temp.getDirection();*/

            this.raytraceEntityPredicate = Main.getPlugin().getConfigurableData().isSelfHarmAllowed() ? e -> true : e -> e != owner;
        }

        private static Vector reflect(Vector v, Vector normal) {
            double length = v.length(); // 1 sqrt...
            double f = v.dot(normal) / length;
            return normal.clone().multiply(f * 2 * length).add(v);
        }

        public boolean tryLiveOneMoreTick() {
            if (!missed) {
                Vector toTarget = victimLocation.clone().subtract(pos).toVector();
                if (toTarget.lengthSquared() < 2.5 * 2.5 * HITBOX_SIZE * HITBOX_SIZE) {
                    missed = true;
                } else {
                    inertia *= INERTIA_MULTIPLIER;
                    dir.multiply(INERTIA).add(toTarget.normalize()).normalize();
                }
            }

            RayTraceResult result = pos.getWorld().rayTrace(pos, dir, SPEED, FluidCollisionMode.NEVER, false, HITBOX_SIZE, raytraceEntityPredicate);

            if (result == null || (result.getHitBlock() != null && result.getHitBlock().isPassable())) {
                pos.add(dir.clone().multiply(SPEED));
                pos.getWorld().spawnParticle(Particle.REDSTONE, pos, 4, 0.0, 0.0, 0.0, 0.08, DUST_OPT);
                //pos.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, pos, 6, 0.05, 0.05, 0.05, 0.05);
                return true; // happy days, still got time to live
            }

            Block hitBlock = result.getHitBlock();
            if (hitBlock != null) {
                double distanceSqr = pos.distanceSquared(victimLocation);
                double idk = SPEED * 1.25;
                if (distanceSqr > idk * idk) { // About to collide nowhere near the target, try to prevent
                    BlockFace hitFace = result.getHitBlockFace();
                    Vector normal = hitFace.getDirection();

                    Vector reflected = reflect(dir, normal);
                    dir.add(reflected.add(reflected)).normalize();
                } // Otherwise hits the target (hopefully), all g
            }

            Entity hitEntity = result.getHitEntity();
            if (hitEntity == owner) {
                Location feet = hitEntity.getLocation();
                // Mission: try not to hit shooter if possible, that's just silly
                if (feet.distanceSquared(pos) > 0.5 * 0.5 * SPEED * SPEED) {
                    Vector fakeNormal = pos.clone().subtract(feet).toVector().normalize();

                    Vector reflected = reflect(dir, fakeNormal);
                    dir.add(reflected).normalize();
                    return true; // Try an another tick
                }
                // It's so close let's just call it a hit for fairness...
            }
            onHit(result);
            return false;
        }

        private void onHit(RayTraceResult hitInfo) {
            Location hitPos = pos;
            Entity hitEntity = hitInfo.getHitEntity();
            if (hitEntity != null) {
                hitPos = hitEntity.getLocation();
                if (hitEntity instanceof Damageable) {
                    Damageable damageable = (Damageable) hitEntity;
                    damageable.damage(DAMAGE);
                }
            } else if (hitInfo.getHitPosition() != null) {
                hitPos = hitInfo.getHitPosition().toLocation(pos.getWorld());
            } else if (hitInfo.getHitBlock() != null) {
                hitPos = hitInfo.getHitBlock().getLocation().toCenterLocation();
            }
            pos.getWorld().createExplosion(hitPos, 0.5f, false, false);
        }

    }


}
