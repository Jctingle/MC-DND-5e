package jeffersondev.Utilities;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
    HashMap<Player, Location> playerMeasure = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){
            Player p = ((Player) sender);
            ItemStack ruler = matchItem();
            p.getInventory().addItem(ruler);
            return true;
        }
        else{
            System.out.println("Cannot execute this command on the command line");
            return false;
            }
    }
    public ItemStack matchItem(){
        ItemStack pointerItem = new ItemStack(Material.NAME_TAG);
        ItemMeta pointerMeta = pointerItem.getItemMeta();
        pointerMeta.setDisplayName("Ruler");
        pointerItem.setItemMeta(pointerMeta); 

        return pointerItem;
    }
    //right click a block :)
    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack pointerItem = matchItem();
            if(e.getHand() == EquipmentSlot.HAND && e.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null && e.getPlayer().getInventory().getItemInMainHand().getItemMeta().equals(pointerItem.getItemMeta())) {
                Player p = (Player) e.getPlayer();
                e.setCancelled(true);
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
        }   
    }
    //right click an entity
    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent e) {
        ItemStack pointerItem = matchItem();
        if(e.getHand() == EquipmentSlot.HAND && e.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null && e.getPlayer().getInventory().getItemInMainHand().getItemMeta().equals(pointerItem.getItemMeta())) {           
                Player p = (Player) e.getPlayer();
                e.setCancelled(true);
                Location pSpot = e.getRightClicked().getLocation();
                pSpot.add(0.0, e.getRightClicked().getHeight()/2, 0.0);
                if(playerMeasure.containsKey(p)){
                    Double distance = playerMeasure.get(p).distance(e.getRightClicked().getLocation()) * 3.0;
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
    }

}
