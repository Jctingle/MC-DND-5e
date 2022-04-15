package jeffersondev.Tokens;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class Moveable {
    private LivingEntity TOKEN;
    private Location STARTLOCATION;
    private Boolean ISPATH;
    // private Boolean ISMOUNTED;
     Moveable(LivingEntity token, Location currentLoc, Boolean isPath/*, Boolean isMounted*/){
        this.TOKEN = token;
        this.STARTLOCATION = currentLoc;
        this.ISPATH = isPath;
        // this.ISMOUNTED = isMounted;
     }

     public void moveTo(int x){
        Location tokenDestination = TOKEN.getLocation();
        tokenDestination.setY(TOKEN.getLocation().getY() + 1);
        TOKEN.teleport(tokenDestination);
     }
     public void moveToB(int x){
        Location tokenDestination = TOKEN.getLocation();
        tokenDestination.setY(TOKEN.getLocation().getY() - 1);
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
     //moveUp
     //moveDown
     //Attack Animation   swingMainHand()
     //rotate 90
     //mountOn/dismount
     //call equipment manager
}
