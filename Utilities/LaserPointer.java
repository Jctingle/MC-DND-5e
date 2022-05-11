package jeffersondev.Utilities;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.bukkit.scheduler.BukkitRunnable;
import jeffersondev.App;
import net.md_5.bungee.api.ChatColor;

//rename this stupid file lmao, fix errors and make it more stable generally
public class LaserPointer implements CommandExecutor,Listener {   
    private App app;
    public LaserPointer(App app){
        this.app = app;
    }
    // ArrayList<Player> activeUsers = new ArrayList<>();
    HashMap<Player, Color>activeUsers = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){
            Player p = ((Player) sender);
            if (args[0].equals("on") && !activeUsers.containsKey(p)) {
                //run a colour matching sequence
                p.sendMessage("Pointer on");
                if (args.length >= 2){
                    String argString = args[1];
                    if(argString.toUpperCase().contains("BLUE")){
                        activeUsers.put(p, Color.BLUE);
                    }
                    else if(argString.toUpperCase().contains("GREEN")){
                        activeUsers.put(p, Color.GREEN);
                    }
                    else {
                        p.sendMessage("Alternate colors are BLUE or GREEN");
                    }
                }
                else{
                    activeUsers.put(p, Color.RED);
                }
                ItemStack pointerItem = matchItem();
                p.getInventory().addItem(pointerItem);
        } else if (args[0].equals("off")) {
                ItemStack pointerItem = matchItem();
                p.sendMessage("Pointer off");
                activeUsers.remove(p);
                p.getInventory().removeItem(pointerItem);
        }
            return true;
        }
        else{
            System.out.println("Cannot execute this command on the command line");
            return false;
            }
    }
    public ItemStack matchItem(){
        ItemStack pointerItem = new ItemStack(Material.AMETHYST_SHARD);
        ItemMeta pointerMeta = pointerItem.getItemMeta();
        pointerMeta.setDisplayName("Laser Pointer");
        pointerItem.setItemMeta(pointerMeta); 

        return pointerItem;
    }
    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ItemStack pointerItem = matchItem();
            if(activeUsers.containsKey(e.getPlayer()) && e.getPlayer().getInventory().getItemInMainHand().getItemMeta().equals(pointerItem.getItemMeta())) {
                Player p = (Player) e.getPlayer();
                e.setCancelled(true);
                DustOptions dustOptions = new DustOptions(activeUsers.get(p), 1.0F);
                RayTraceResult rtx = p.getWorld().rayTraceBlocks(p.getEyeLocation(), p.getEyeLocation().getDirection(), 100);
                if (rtx != null){
                    Location pSpot = rtx.getHitPosition().toLocation(p.getWorld());
                    p.getWorld().spawnParticle(Particle.REDSTONE, pSpot, 1, dustOptions);
                    
                    }
            }
        }   
    }
    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent e) {
        ItemStack pointerItem = matchItem();
        if(activeUsers.containsKey(e.getPlayer()) && e.getPlayer().getInventory().getItemInMainHand().getItemMeta().equals(pointerItem.getItemMeta())) {           
                Player p = (Player) e.getPlayer();
                e.setCancelled(true);
                LivingEntity glower = (LivingEntity) e.getRightClicked();
                PotionEffect targetGlow = new PotionEffect(PotionEffectType.GLOWING, 50, 1, false, false, false);
                glower.addPotionEffect(targetGlow);
            }  
    }

}
