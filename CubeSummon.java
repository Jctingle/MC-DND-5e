package jeffersondev;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
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



public class CubeSummon implements CommandExecutor,Listener {   
    private App app;
    public CubeSummon(App app){
        this.app = app;
    }
    Map<Player, Double> activeUsers = new HashMap<Player, Double>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){
            Player p = ((Player) sender);
            activeUsers.put(p, Double.parseDouble(args[0]));
            return true;
        }
        else{
            System.out.println("Cannot execute this command on the command line");
            return false;
            }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if(activeUsers.containsKey(e.getPlayer())){
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    Player p = (Player) e.getPlayer();
                    e.setCancelled(true);
                    RayTraceResult rtx = p.getWorld().rayTraceBlocks(p.getEyeLocation(), p.getEyeLocation().getDirection(), 100);
                    if (rtx != null){
                        // Double parameters = activeUsers.get(p);
                        Location pSpot = rtx.getHitPosition().toLocation(p.getWorld());
                        // pSpot.subtract(parameters/2, 0, parameters/2);
                        // ParticleRect testCube = new ParticleRect(pSpot, parameters, parameters, parameters);
                        // testCube.draw("flame");
                        // activeUsers.remove(p);
                        p.getWorld().spawnParticle(Particle.FLAME, p.getLocation(), 0, pSpot.getX(), pSpot.getY(), pSpot.getZ(), 0.05);
                    }
            } 
        }
    }
}
