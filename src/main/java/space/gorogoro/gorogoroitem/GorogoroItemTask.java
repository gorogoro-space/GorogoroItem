package space.gorogoro.gorogoroitem;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class GorogoroItemTask extends BukkitRunnable {

  GorogoroItem p;

  public GorogoroItemTask setGorogoroItem(GorogoroItem p){
    this.p = p;
    return this;
  }

  @Override
  public void run() {
    for(Player target: p.getServer().getOnlinePlayers()) {
      boolean isNormal = p.isPhantomGuardNormal(target);
      boolean isRare = p.isPhantomGuardRare(target);
      if( isNormal || isRare ) {
        double rangeGuard = 10;
        if(isRare) {
          rangeGuard = 20;
        }
        for(Entity e:target.getNearbyEntities(rangeGuard, rangeGuard, rangeGuard)) {
           if(!e.getType().equals(EntityType.PHANTOM)) {
             continue;
           }

           LivingEntity attacker = (LivingEntity)e;
           if(attacker.getFireTicks() < 1) {
             Vector attackerVec = attacker.getVelocity();
             double x = attackerVec.getX();
             double y = attackerVec.getY();
             double z = attackerVec.getZ();
             if(target.getLocation().getX() < attacker.getLocation().getX() && attackerVec.getX() < 0) {
               x = x * rangeGuard * -1.0;
             } else if(target.getLocation().getX() > attacker.getLocation().getX() && attackerVec.getX() > 0) {
               x = x * rangeGuard * -1.0;
             }
             if(target.getLocation().getY() < attacker.getLocation().getY() && attackerVec.getY() < 0) {
               y = y * rangeGuard * -1.0;
             } else if(target.getLocation().getY() > attacker.getLocation().getY() && attackerVec.getY() > 0) {
               y = y * rangeGuard * -1.0;
             }
             if(target.getLocation().getZ() < attacker.getLocation().getZ() && attackerVec.getZ() < 0) {
               z = z * rangeGuard * -1.0;
             } else if(target.getLocation().getZ() > attacker.getLocation().getZ() && attackerVec.getZ() > 0) {
               z = z * rangeGuard * -1.0;
             }
             attacker.setVelocity(new Vector(x,y,z));
             attacker.damage(5.0);
             attacker.setFireTicks(20);
           }
        }
      }
    }
  }
}