package jeffersondev.Tokens;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Phantom;
import org.bukkit.entity.Slime;

public class Moveable {
    private LivingEntity TOKEN;
    private Location STARTLOCATION;
    private Boolean ISPATH;
    private boolean CURSORMOVEMENT = false;
    // private Boolean ISMOUNTED;
     Moveable(LivingEntity token, Location currentLoc, Boolean isPath/*, Boolean isMounted*/){
        this.TOKEN = token;
        this.STARTLOCATION = currentLoc;
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
     public void sizeUp(){
      //   Slime sizer = TOKEN;
        switch(TOKEN.getType()){
         case SLIME:
            Slime sl = (Slime) TOKEN;
            sl.setSize(sl.getSize() + 1);
         break;
         case PHANTOM:
            Phantom ph = (Phantom) TOKEN;
            ph.setSize(ph.getSize() + 1);
         break;
         case MAGMA_CUBE:
            MagmaCube mc = (MagmaCube) TOKEN;
            mc.setSize(mc.getSize() + 1);
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
            break;
            case PHANTOM:
               Phantom ph = (Phantom) TOKEN;
               ph.setSize(ph.getSize() - 1);
            break;
            case MAGMA_CUBE:
               MagmaCube mc = (MagmaCube) TOKEN;
               mc.setSize(mc.getSize() - 1);
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
     //moveUp
     //moveDown
     //Attack Animation   swingMainHand()
     //rotate 90
     //mountOn/dismount
     //call equipment manager
}
