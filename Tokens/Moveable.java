package jeffersondev.Tokens;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

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
     //moveUp
     //moveDown
     //Attack Animation   swingMainHand()
     //rotate 90
     //mountOn/dismount
     //call equipment manager
}
