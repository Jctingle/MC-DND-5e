package jeffersondev.Utilities;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;

import jeffersondev.App;



public class Ruler{   
    private App app;
    public Ruler(App app){
        this.app = app;
    }
    // ArrayList<Player> activeUsers = new ArrayList<>();
    static HashMap<Player, Location> playerMeasure = new HashMap<>();
    //right click a block :)
    public static void rightClickRTX(Player p) {
                RayTraceResult rtx = p.getWorld().rayTraceBlocks(p.getEyeLocation(), p.getEyeLocation().getDirection(), 100);
                if (rtx != null){
                    Location pSpot = rtx.getHitPosition().toLocation(p.getWorld());
                    if(playerMeasure.containsKey(p)){
                        Double distance = playerMeasure.get(p).distance(pSpot) * 3.0;
                        DecimalFormat df = new DecimalFormat("#.#"); 
                        String distanceString  = df.format(distance).toString();
                        p.sendMessage("The distance is " + distanceString);
                        playerMeasure.remove(p);
                    }
                    else{
                        playerMeasure.put(p, pSpot);
                        p.sendMessage("Point One Chosen");
                    }
                }
                else{
                } 
    }
    //right click an entity
    public static void RightClickEntity(Player p, Entity rightClicked) {
                Location pSpot = rightClicked.getLocation();      
                pSpot.add(0.0, rightClicked.getHeight()/2, 0.0);
                if(playerMeasure.containsKey(p)){
                    Double distance = playerMeasure.get(p).distance(rightClicked.getLocation()) * 3.0;
                    DecimalFormat df = new DecimalFormat("#.#"); 
                    String distanceString  = df.format(distance).toString();
                    p.sendMessage("The distance is " + distanceString);
                    playerMeasure.remove(p);
                }
                else{
                    playerMeasure.put(p, pSpot);
                    p.sendMessage("Point One Chosen on Entity: " + rightClicked.getCustomName());
                } 
    }

}
