package jeffersondev.Tokens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;

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

public class Equipmentmanager implements CommandExecutor,Listener {
    private App app;
    public Equipmentmanager(App app){
        this.app = app;
    }
    ArrayList<Player> equipmentAdjusters = new ArrayList<Player>();
    Map<Player, Inventory> playerView = new HashMap<>();
    Map<Player, LivingEntity> playerClick = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        Player p = (Player) sender;
        equipmentAdjusters.add(p);
        return true;
    }
    // private final Inventory inv = Bukkit.createInventory(null, 6, "Equipment");
    public Inventory whatWearing(LivingEntity billy){
        Inventory inv = Bukkit.createInventory(null, 9, "Equipment");
        EntityEquipment billyCloset = billy.getEquipment();
        // ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        ItemStack helmet = billyCloset.getHelmet();
        ItemStack chest = billyCloset.getChestplate();
        ItemStack legs = billyCloset.getLeggings();
        ItemStack boots = billyCloset.getBoots();
        ItemStack mainHand = billyCloset.getItemInMainHand();
        ItemStack offHand = billyCloset.getItemInOffHand();
        inv.setItem(0, helmet);
        inv.setItem(1, chest);
        inv.setItem(2, legs);
        inv.setItem(3, boots);
        inv.setItem(4, mainHand);
        inv.setItem(5, offHand);
        return inv;
    }
    public void changeEquip(LivingEntity billy, Inventory inv){
        EntityEquipment billyCloset = billy.getEquipment();
        billyCloset.setHelmet(inv.getItem(0));
        billyCloset.setChestplate(inv.getItem(1));
        billyCloset.setLeggings(inv.getItem(2));
        billyCloset.setBoots(inv.getItem(3));
        billyCloset.setItemInMainHand(inv.getItem(4));
        billyCloset.setItemInOffHand(inv.getItem(5));
    }
    public void openInventory(final HumanEntity ent, Inventory inv) {
        ent.openInventory(inv);
    }
    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent e) {
        if(equipmentAdjusters.contains(e.getPlayer())){
            e.setCancelled(true);
            LivingEntity modifyMob = (LivingEntity) e.getRightClicked();
            Inventory mobInventory = whatWearing(modifyMob);
            playerView.put(e.getPlayer(), mobInventory);
            playerClick.put(e.getPlayer(), modifyMob);
            //take the 6 inventory slots we like want, create an array list of the? no, probably when I construct the Gui
            //I will be assigning specific slots to pieces of equipment, and if it's empty, then wow! Just leave it empty I guess
            //accept all items types but only display the viable ones of course.
            //head, chest, legs, boots, main-hand, off-hand
            openInventory(e.getPlayer(), mobInventory);
        }
    }
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory() != playerView.get(e.getWhoClicked())) return;

    }
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory() != playerView.get(e.getWhoClicked())) {
            return;
        }
    }
    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e){
        if (e.getInventory() != playerView.get(e.getPlayer())) return;
        else{
            changeEquip(playerClick.get(e.getPlayer()), e.getInventory());
            playerView.remove(e.getPlayer());
            equipmentAdjusters.remove(e.getPlayer());
            playerClick.remove(e.getPlayer());
            return;
        }
    }
}