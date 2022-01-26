package jeffersondev;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Location;

import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.bukkit.scheduler.BukkitRunnable;
import jeffersondev.App;

//rename this stupid file lmao, fix errors and make it more stable generally
public class Borpa implements CommandExecutor,Listener {   
    private App app;
    public Borpa(App app){
        this.app = app;
    }
    ArrayList<Player> activeUsers = new ArrayList<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){
            Player p = ((Player) sender);
            if (args[0].equals("on") && !activeUsers.contains(p)) {
                p.sendMessage("Pointer on");
                activeUsers.add(p);
        } else if (args[0].equals("off")) {
                p.sendMessage("Pointer off");
                activeUsers.remove(p);
        }
            return true;
        }
        else{
            System.out.println("Cannot execute this command on the command line");
            return false;
            }
    }
    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(activeUsers.contains(e.getPlayer())) {
                Player p = (Player) e.getPlayer();
                DustOptions dustOptions = new DustOptions(Color.fromRGB(255, 0, 0), 1.0F);
                RayTraceResult rtx = p.getWorld().rayTraceBlocks(p.getEyeLocation(), p.getEyeLocation().getDirection(), 100);
                if (rtx != null){
                    Location pSpot = rtx.getHitPosition().toLocation(p.getWorld());
                    p.getWorld().spawnParticle(Particle.REDSTONE, pSpot, 1, dustOptions);
                    
                    }
                else{
                }
            }
        }   
    }

}
