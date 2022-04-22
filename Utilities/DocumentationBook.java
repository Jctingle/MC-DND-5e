package jeffersondev.Utilities;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class DocumentationBook {
    private LivingEntity TOKEN;
    private Location STARTLOCATION;
    private Boolean ISPATH;
    // private Boolean ISMOUNTED;
     DocumentationBook(LivingEntity token, Location currentLoc, Boolean isPath){
        this.TOKEN = token;
        this.STARTLOCATION = currentLoc;
        this.ISPATH = isPath;
        // this.ISMOUNTED = isMounted;
     }
     //moveUp
     //moveDown
     //Attack Animation   swingMainHand()
     //rotate 90
     //mountOn/dismount
     //call equipment manager
}
