package jeffersondev.SpellCasting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import jeffersondev.App;



public class SpellTester implements CommandExecutor,Listener {  
    //this code was always just a placeholder to help test the structure of shapes. 
    private App app;
    public SpellTester(App app){
        this.app = app;
    }
    Map<Player, ArrayList<Double>> activeUsers = new HashMap<Player, ArrayList<Double>>();
    // Map<Player, ConcentrationSpell> activefocus = new HashMap<Player, ConcentrationSpell>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){
            Player p = ((Player) sender);
            ArrayList<Double> mapAdd = new ArrayList<Double>();
            //args0 is radius, args1 is height
            mapAdd.add(Double.parseDouble(args[0]));
            activeUsers.put(p, mapAdd);
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
        ItemStack pointerItem = new ItemStack(Material.BLAZE_ROD);
        ItemMeta pointerMeta = pointerItem.getItemMeta();
        pointerMeta.setDisplayName("Test_Wand");
        pointerItem.setItemMeta(pointerMeta); 

        return pointerItem;
    }
    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        ItemStack wando = matchItem();
        if(activeUsers.containsKey(e.getPlayer()) && e.getPlayer().getInventory().getItemInMainHand().equals(wando)){
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Player p = (Player) e.getPlayer();
                    e.setCancelled(true);
                        Double parameters = activeUsers.get(p).get(0);
                        ParticleCone testCone = new ParticleCone(p.getEyeLocation(), "flame", p.getEyeLocation().getDirection(), parameters);
                        testCone.draw();
                        activeUsers.remove(p);
                        p.getInventory().removeItem(wando);


            } 
        }
    }
}
