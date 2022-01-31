package jeffersondev;

import java.util.ArrayList;

import org.bukkit.Location;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;

public class dmNotes implements CommandExecutor,Listener {   
    private App app;
    public dmNotes(App app){
        this.app = app;
    }
    ArrayList<Player> notePlacers = new ArrayList<>();
    //need to privatize this between players
    String noteText = new String(); 
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){
            Player p = ((Player) sender);
            if (!notePlacers.contains(p)) {
                notePlacers.add(p);
                StringBuilder builder = new StringBuilder();
                    for(int i = 0; i < args.length; i++) {
                        builder.append(args[i] + " ");
                    } 
                    noteText = builder.toString();
                    p.sendMessage("Placing Dnote " + builder.toString());
                // noteText = args[0];
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
            if(notePlacers.contains(e.getPlayer())) {
                Player p = (Player) e.getPlayer();
                RayTraceResult rtx = p.getWorld().rayTraceBlocks(p.getEyeLocation(), p.getEyeLocation().getDirection(), 100);
                if (rtx != null){
                    Location pSpot = rtx.getHitPosition().toLocation(p.getWorld());
                    //change this to summon the armor stand
                    Location pSpotCopy = pSpot.clone();
                    pSpotCopy.setY(pSpot.getY() - 2);
                    ArmorStand as = (ArmorStand) pSpot.getWorld().spawn(pSpotCopy, ArmorStand.class);
                    as.setGravity(false);
                    as.setCanPickupItems(false);
                    as.setCustomName(noteText + "");
                    as.setCustomNameVisible(true);
                    as.setVisible(false);
                    as.isMarker();
                    notePlacers.remove(p);
                    }
                else{
                }
            }
        }   
    }

}
