package jeffersondev;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.bukkit.scheduler.BukkitRunnable;
import jeffersondev.App;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Grimoire implements CommandExecutor,Listener {
    private App app;
    public Grimoire(App app){
        this.app = app;
    }
    Map<Player, Inventory> playerView = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        Player p = (Player) sender;
        
        // String callKind = args[0];
        // String fileName = args[1];
        // String Filecontents = new String();
        // if(args.length > 2){
        //     Filecontents = args[2];
        // }
        // Plugin plugin = Bukkit.getPluginManager().getPlugin("DMTools");
        // if (args[0].equals("save")){
        //     save("plugins/DMTools/" + fileName +".spell", Filecontents);
        //     // plugin.getDataFolder() +
        // }
        // else if(args[0].equals("load")){
        //     File dir = new File("plugins/DMTools");
        //     File[] directoryListing = dir.listFiles();
        //     if (directoryListing != null) {
        //       for (File child : directoryListing) {
        //           String fileIter = child.getAbsolutePath();
        //           String tester = load(fileIter);
        //         //   String tester = load("plugins/DMTools/" + fileName +".spell");
        //         p.sendMessage(fileIter);
        //         p.sendMessage(tester);
        //       }
        //     }
            
        // }
        //three args, first will be file name, 2nd will be string within, call command will access the file and print the string with in,
        //so save testfile testconents vs load testfile
        return true;
    }
    public void openInventory(final HumanEntity ent, Inventory inv) {
        ent.openInventory(inv);
    }
    public Inventory spellConstructor(Player input, String spellName){
        //not actual code just temporary return
        Inventory inv = Bukkit.createInventory(null, 18, "Spell Construction"); 
        return inv;
    }
    public ItemStack spellComponent(Player sender, String[] info){
        ItemStack item = new ItemStack(Material.PRISMARINE_SHARD, 1);
        return item;
    }
    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e){
        //this will make sense
        if (e.getInventory() != playerView.get(e.getPlayer())) return;
        else{
            String fileName = e.getInventory().getItem(0).getItemMeta().getDisplayName();
            Map<String, String> spellStorage = new HashMap<>();
            //9 lines of spellStorage configuration based on inventoryslots 1,2,3
            save("plugins/DMTools/" + fileName +".spell", spellStorage);
            return;
        }
    }
    //code to save spell data to storage
    public static <T extends Serializable> boolean save(String filePath, T object) {
        try {
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(new GZIPOutputStream(new FileOutputStream(filePath)));
            out.writeObject(object);
            out.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    //code to import spell data from storage
    public static <T extends Serializable> T load(String filePath){
        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(new GZIPInputStream(new FileInputStream(filePath)));
            T object = (T) in.readObject();
            in.close();
            return object;
        } catch (ClassNotFoundException | IOException e) {
            return null;
        }
    }
    // @EventHandler
    // public void onRightClick(PlayerInteractEntityEvent e) {

    // }
    // Objects you are saving and loading must implement/extend Serializable or a Compilation Error will occur
    // FlatFile.save(plugin.getDataFolder() + "/fileName.extension", objectToSave);
    // You must ensure that the data stored in the file being loaded corresponds to the object being loaded back or a Runtime Error will occur
    // objectToLoad = FlatFile.load(plugin.getDataFolder() + "/fileName.extension");
}

