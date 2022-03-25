package jeffersondev.SpellCasting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;

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
            mapAdd.add(Double.parseDouble(args[1]));
            activeUsers.put(p, mapAdd);
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
                        Double parameters = activeUsers.get(p).get(0);
                        Double heightParam = activeUsers.get(p).get(1);
                        Location pSpot = rtx.getHitPosition().toLocation(p.getWorld());
                        ParticleCyl testCyl = new ParticleCyl(pSpot,"flame",parameters, heightParam);
                        testCyl.draw();
                        activeUsers.remove(p);
                    }
            } 
        }
    }
}
