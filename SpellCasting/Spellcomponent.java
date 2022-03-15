package jeffersondev.SpellCasting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
import org.bukkit.scheduler.BukkitRunnable;
import jeffersondev.App;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Spellcomponent implements CommandExecutor,Listener {
    private App app;
    public Spellcomponent(App app){
        this.app = app;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        Player p = (Player) sender;
        //args will be token, travel, onsite
        String tokentype = args[0];
        switch (tokentype){
            case "token":
            //error checking here for args length and contents
                String tokenItem = args[1].toUpperCase();
                String spellName = args[2];
                ItemStack token = new ItemStack(Material.valueOf(tokenItem), 1);
                ItemMeta meta = token.getItemMeta();
                meta.setDisplayName(spellName);
                token.setItemMeta(meta);
                p.getInventory().addItem(token);
                break;
            case "travel":
            //error checking here for args length and contents
                String travelType = args[1];
                String particle = args[2];
                //build fields from args for variable readability
                ItemStack travelToken = new ItemStack(Material.PRISMARINE_SHARD, 1);
                ItemMeta travelmeta = travelToken.getItemMeta();
                travelmeta.setDisplayName(travelType);
                //build the lore components
                ArrayList<String> travelLore = new ArrayList<String>();
                travelLore.add(particle);
                travelmeta.setLore(travelLore);
                travelToken.setItemMeta(travelmeta);
                p.getInventory().addItem(travelToken);
                break;
            case "onsite":
            //lots of error checking here
                String onsiteType = args[1];
                String onsiteParticle = args[2];
                String onsiteShape = args[3];
                String onsiteSize = args[4];
                String onsitePersist = args[5];                
                ItemStack onsiteToken = new ItemStack(Material.PRISMARINE_SHARD, 1);
                ItemMeta onsitemeta = onsiteToken.getItemMeta();
                onsitemeta.setDisplayName(onsiteType);
                ArrayList<String> onsiteLore = new ArrayList<String>();
                onsiteLore.add(onsiteParticle);
                onsiteLore.add(onsiteShape);
                onsiteLore.add(onsiteSize);
                onsiteLore.add(onsitePersist);
                onsitemeta.setLore(onsiteLore);
                onsiteToken.setItemMeta(onsitemeta);
                p.getInventory().addItem(onsiteToken);
                break;
        }



        return true;
    }
}