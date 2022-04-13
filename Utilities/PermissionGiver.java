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



public class PermissionGiver implements CommandExecutor,Listener {   
    private App app;
    public PermissionGiver(App app){
        this.app = app;
    }
    // ArrayList<Player> activeUsers = new ArrayList<>();
    HashMap<Player, Location> playerMeasure = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){
            Player p = ((Player) sender);
            if(p.isOp() && args[1].equals("dm")){
                //well I guess if they are already Opped they don't need it lmao.
            }
            else if(!p.isOp() && args[1].equals("player")){
                
            }
            return true;
        }
        else{
            System.out.println("Cannot execute this command on the command line");
            return false;
            }
    }

}
