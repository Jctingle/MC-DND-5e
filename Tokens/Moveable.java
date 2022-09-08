package jeffersondev.Tokens;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Wolf;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

public class Moveable {
    private LivingEntity TOKEN;
    private Entity ASTOKEN;
    private Boolean ISPATH;
    private boolean CURSORMOVEMENT = false;
    // private Boolean ISMOUNTED;
     Moveable(LivingEntity token, Location currentLoc, Boolean isPath/*, Boolean isMounted*/){
        this.TOKEN = token;
        this.ISPATH = isPath;
        // this.ISMOUNTED = isMounted;
     }
     Moveable(Entity AStoken, Location currentLoc, Boolean isPath/*, Boolean isMounted*/){
      this.ASTOKEN = AStoken;
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
      TOKEN.teleport(faceOppositeLocation(TOKEN,facing));
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
   public boolean isAgeable(){
      if(TOKEN instanceof Ageable) {
         return true;
      }
      else{
         return false;
      }
   }
   public boolean isDog(){
      if(TOKEN instanceof Wolf) {
         return true;
      }
      else{
         return false;
      }
   }
   public void toggleDogAngry(){
      if(TOKEN instanceof Wolf) {
         Wolf wolf = (Wolf) TOKEN;
         boolean angry = wolf.isAngry();
         if(angry){
            wolf.setAngry(false);
         }
         else{
            wolf.setAngry(true);
         }
      }
   }
   public void toggleGrowth(){
      if(TOKEN instanceof Ageable) {
         Ageable age = (Ageable) TOKEN;
         boolean baby = !age.isAdult();
         if(baby){
            age.setAdult();
         }
         else{
            age.setBaby();
         }
      }
   }
   public void toggleInvis(){
      if(TOKEN.isInvisible()) {
         TOKEN.setInvisible(false);
      }
      else{
         TOKEN.setInvisible(true);
      }
   }
   // public Location getRTXLookAt(Player p){
   //    Location eyeLoc = p.getEyeLocation();
   //    World world = p.getWorld();
   //    RayTraceResult rtxResult = world.rayTraceBlocks(eyeLoc, eyeLoc.getDirection(), 100, FluidCollisionMode.NEVER, true);
   //    if (rtxResult != null){
   //        return rtxResult.getHitPosition().toLocation(world);
   //    }
   //    else{
   //        p.sendMessage("Please be facing a solid surface when activating this option");
   //        return p.getLocation();
   //    }
   // }
   public Location faceLocation(LivingEntity entity, Location to) {
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
  public Location faceOppositeLocation(LivingEntity entity, Player to) {
   if (entity.getWorld() != to.getWorld()) {
       return null;
   }
   // LivingEntity LivingEnt = entity;

   Player target = to;

   Vector targetDirection = target.getLocation().getDirection(); // Direction target is facing
                                                  
   // Vector newFacingTargetDirection = (targetDirection.multiply(-1)); // opposite direction

   Location location = entity.getLocation();

   location.setDirection(targetDirection);  //set direction to opposite direction
   location.setPitch(0); //if they are on flat ground set this to 0 so they are looking straight ahead (optional)

   Location loc = location;
   return loc;
}
     //moveUp
     //moveDown
     //Attack Animation   swingMainHand()
     //rotate 90
     //mountOn/dismount
     //call equipment manager
}
