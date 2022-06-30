package jeffersondev.Utilities;

import java.text.DecimalFormat;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;

import jeffersondev.App;



public class ToolBox implements CommandExecutor,Listener {   
    private App app;
    public ToolBox(App app){
        this.app = app;
    }
    // ArrayList<Player> activeUsers = new ArrayList<>();
    static HashMap<Player, Location> playerMeasure = new HashMap<>();
    MultiTool toolbox = new MultiTool();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){
            Player p = ((Player) sender);
            if(Core.isGamer(p)){
                Inventory box = toolKit(p);
                p.openInventory(box);
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
    public void openInventory(final HumanEntity ent, Inventory inv) {
        ent.openInventory(inv);
    }
    public Inventory toolKit(Player p){
        //I really hate this one ngl
        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, "Tool Box"); 
        for(ItemStack Tool : toolbox.returnAllItems()){
            if(!p.getInventory().contains(Tool)){
                inv.addItem(Tool);
            }
        }
        return inv;
    }
}
