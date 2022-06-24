package jeffersondev.Utilities;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;

import jeffersondev.App;



public class Ruler implements CommandExecutor,Listener {   
    private App app;
    public Ruler(App app){
        this.app = app;
    }
    // ArrayList<Player> activeUsers = new ArrayList<>();
    static HashMap<Player, Location> playerMeasure = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){
            Player p = ((Player) sender);
            if(Core.isGamer(p)){
                ItemStack ruler = matchItem();
                p.getInventory().addItem(ruler);
                return true;
            }
            else{
                p.sendMessage("Please join the game system with /game <Character-Name(OneWord)>");
                return false;
            }
        }
        else{
            System.out.println("Cannot execute this command on the command line");
            return false;
            }
    }
    public static ItemStack matchItem(){
        MultiTool toolBox = new MultiTool();
        ItemStack pointerItem = toolBox.ruler();

        return pointerItem;
    }
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
