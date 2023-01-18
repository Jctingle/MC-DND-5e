package jeffersondev.Utilities;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import jeffersondev.App;



public class Ruler{   
    private App app;
    public Ruler(App app){
        this.app = app;
    }
    // ArrayList<Player> activeUsers = new ArrayList<>();
    static HashMap<Player, Location> playerMeasure = new HashMap<>();
    public static void deRegister(Player p){
        playerMeasure.remove(p);
    }
    //right click a block :)
    public static void rightClickRTX(Player p) {
                RayTraceResult rtx = p.getWorld().rayTraceBlocks(p.getEyeLocation(), p.getEyeLocation().getDirection(), 100);
                if (rtx != null){
                    
                    Location pSpot = rtx.getHitPosition().toLocation(p.getWorld());
                    if(playerMeasure.containsKey(p)){
                        Particle importParticle = Particle.END_ROD;
                        double pointsPerLine = (playerMeasure.get(p).distance(pSpot)) * 4.0;
                        double d = playerMeasure.get(p).distance(pSpot) / pointsPerLine;
                        for (int i = 0; i < pointsPerLine; i++) {
                            Location l = playerMeasure.get(p).clone();
                            Vector direction = pSpot.toVector().subtract(playerMeasure.get(p).toVector()).normalize();
                            Vector v = direction.multiply(i * d);
                            l.add(v.getX(), v.getY(), v.getZ());
                            playerMeasure.get(p).getWorld().spawnParticle(importParticle, l, 0, 0, 0, 0, 0.05);
                            //for secondary travel particle, put other one here
                        }
                        Double distance = playerMeasure.get(p).distance(pSpot) * 3.0;
                        DecimalFormat df = new DecimalFormat("#.#"); 
                        String distanceString  = df.format(distance).toString();
                        p.sendMessage("[" + ChatColor.GREEN + "Ruler" + ChatColor.WHITE + "]: The distance is " + distanceString);
                        playerMeasure.remove(p);
                    }
                    else{
                        playerMeasure.put(p, pSpot);
                        p.sendMessage("[" + ChatColor.GREEN + "Ruler" + ChatColor.WHITE + "]: Point One Chosen, please select a second location");
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
                    Particle importParticle = Particle.TOTEM;
                    double pointsPerLine = (playerMeasure.get(p).distance(pSpot)) * 4.0;
                    double d = playerMeasure.get(p).distance(pSpot) / pointsPerLine;
                    for (int i = 0; i < pointsPerLine; i++) {
                        Location l = playerMeasure.get(p).clone();
                        Vector direction = pSpot.toVector().subtract(playerMeasure.get(p).toVector()).normalize();
                        Vector v = direction.multiply(i * d);
                        l.add(v.getX(), v.getY(), v.getZ());
                        playerMeasure.get(p).getWorld().spawnParticle(importParticle, l, 0, 0, 0, 0, 0.05);
                        //for secondary travel particle, put other one here
                    }
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
