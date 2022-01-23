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
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.bukkit.scheduler.BukkitRunnable;
import jeffersondev.App;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;

public class Equipmentmanager implements CommandExecutor,Listener {
    ArrayList<Player> equipmentAdjusters = new ArrayList<Player>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        Player p = (Player) sender;
        equipmentAdjusters.add(p);
        return true;
    }
    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent e) {
        if(equipmentAdjusters.contains(e.getPlayer())){
            LivingEntity modifyMob = (LivingEntity) e.getRightClicked();
            Inventory mobInventory = (Inventory) modifyMob.getEquipment();
            //take the 6 inventory slots we like want, create an array list of the? no, probably when I construct the Gui
            //I will be assigning specific slots to pieces of equipment, and if it's empty, then wow! Just leave it empty I guess
            //accept all items types but only display the viable ones of course.
        }
    }
}