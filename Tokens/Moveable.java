package jeffersondev.Tokens;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;

public class Moveable {
    private LivingEntity TOKEN;
    private Boolean ISPATH;
    private boolean CURSORMOVEMENT = false;
    // private Boolean ISMOUNTED;
     Moveable(LivingEntity token, Location currentLoc, Boolean isPath/*, Boolean isMounted*/){
        this.TOKEN = token;
        this.ISPATH = isPath;
        // this.ISMOUNTED = isMounted;
     }

     public void moveTo(){
        Location tokenDestination = TOKEN.getLocation();
        Double newY = TOKEN.getLocation().getY();
        newY = newY + 1.0;
        tokenDestination.setY(newY);
        TOKEN.teleport(tokenDestination);
     }
     public void moveToB(){
        Location tokenDestination = TOKEN.getLocation();
        Double newY = TOKEN.getLocation().getY();
        newY = newY - 1.0;
        tokenDestination.setY(newY);
        TOKEN.teleport(tokenDestination);
     }
     public void pathBool(){
        this.ISPATH = !ISPATH;
     }
     public void faceMe(Player facing){
         TOKEN.teleport(faceLocation(TOKEN,facing.getEyeLocation()));
     }
     public void faceAway(Player facing){
      TOKEN.teleport(faceLocation(TOKEN,facing.getEyeLocation()));
  }
     public void sizeUp(){
      //   Slime sizer = TOKEN;
        switch(TOKEN.getType()){
         case SLIME:
            Slime sl = (Slime) TOKEN;
            sl.setSize(sl.getSize() + 1);
            sl.setHealth(sl.getHealth() - 5);
         break;
         case PHANTOM:
            Phantom ph = (Phantom) TOKEN;
            ph.setSize(ph.getSize() + 1);
            ph.setHealth(ph.getHealth() - 5);
         break;
         case MAGMA_CUBE:
            MagmaCube mc = (MagmaCube) TOKEN;
            mc.setSize(mc.getSize() + 1);
            mc.setHealth(mc.getHealth() - 5);
         break;
         default:
            break;
        }
      // this.TOKEN.setSize(TOKEN.getSize() + 1);
      }
      public void sizeDown(){
         switch(TOKEN.getType()){
            case SLIME:
               Slime sl = (Slime) TOKEN;
               sl.setSize(sl.getSize() - 1);
               sl.setHealth(sl.getHealth() + 5);
            break;
            case PHANTOM:
               Phantom ph = (Phantom) TOKEN;
               ph.setSize(ph.getSize() - 1);
               ph.setHealth(ph.getHealth() + 5);
            break;
            case MAGMA_CUBE:
               MagmaCube mc = (MagmaCube) TOKEN;
               mc.setSize(mc.getSize() - 1);
               mc.setHealth(mc.getHealth() + 5);
            break;
            default:
               break;
           }
      }
     public Boolean isPath(){
        return ISPATH;
     }
     public LivingEntity movingMobReturn(){
         return this.TOKEN;
     }
     public void swingArm(){
         TOKEN.swingMainHand();
     }
     public boolean isCursor(){
        return this.CURSORMOVEMENT;
     }
     public void cursorBool(){
      this.CURSORMOVEMENT = !CURSORMOVEMENT;
   }
   public boolean isSlime(){
      if(TOKEN.getType().equals(EntityType.SLIME) || TOKEN.getType().equals(EntityType.MAGMA_CUBE) || TOKEN.getType().equals(EntityType.PHANTOM)){
         return true;
      }
      else return false;
   }
   public static Location faceLocation(LivingEntity entity, Location to) {
      if (entity.getWorld() != to.getWorld()) {
          return null;
      }
      Location fromLocation = entity.getEyeLocation();

      double xDiff = to.getX() - fromLocation.getX();
      double yDiff = to.getY() - fromLocation.getY();
      double zDiff = to.getZ() - fromLocation.getZ();

      double distanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
      double distanceY = Math.sqrt(distanceXZ * distanceXZ + yDiff * yDiff);

      double yaw = Math.toDegrees(Math.acos(xDiff / distanceXZ));
      double pitch = Math.toDegrees(Math.acos(yDiff / distanceY)) - 90.0D;
      if (zDiff < 0.0D) {
          yaw += Math.abs(180.0D - yaw) * 2.0D;
      }
      Location loc = entity.getLocation();
      loc.setYaw((float) (yaw - 90.0F));
      loc.setPitch((float) (pitch));
      return loc;
  }
     //moveUp
     //moveDown
     //Attack Animation   swingMainHand()
     //rotate 90
     //mountOn/dismount
     //call equipment manager
}
