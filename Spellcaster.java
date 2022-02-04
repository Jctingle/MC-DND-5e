package jeffersondev;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import jeffersondev.Grimoire;
public class Spellcaster implements CommandExecutor,Listener {
    // dispatchCommand(item.getmeta.getlore)
    private App app;
    public Spellcaster(App app){
        this.app = app;
    }

    HashMap<Player, HashMap<String, String>> playerSpellData = new HashMap<>();
    Map<Player, Inventory> playerView = new HashMap<>();
    ArrayList<Player> spellCache = new ArrayList<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        // Grimoire grimoire = new Grimoire(app);
        Player p = (Player) sender;
        File playerGrimoire = new File("plugins/DMTools/" + p.getName() + ".grim");
        if (playerGrimoire.exists()){ 
            spellCache.add(p);
        }
        if(spellCache.contains(p)){
            //load smol menu of spell inventory
            //oninventory click
            Inventory inv = spellChooser(p);
            openInventory(p, inv);
            playerView.put(p, inv);
        }
        else{
            p.sendMessage("Sorry! You do not currently have any spells known, please browse the catalogue");
        }
        return true;
    }
    public void openInventory(final HumanEntity ent, Inventory inv) {
        ent.openInventory(inv);
    }
    //void method that will have components and multiple pathways/complex logic for particle programflow
    public Void castSpell(Location origin, Location destination, Player caster, LivingEntity tokenOrigin){
        //9 cache variables? traveltype, travelparticle, onsiteeffect, onsiteshape
        //                   onsitesize, onsiteparticle, persistant
        //travel logic
        String travelType = playerSpellData.get(caster).get("traveltype");
        switch(travelType){
            //travel motion thoughts: runnable that tracks an invisible arrow moving slower? 
            //or some kind of controllable flying particle with it's location pinged with particles every x ticks
            //then canceled upon end of animation/onhit
        //cone
        //beam
        //ball
        //lob
        //instant aka do nothing for travel
        //formula for intervals of travel particles will be distance*5? I think that makes the most sense
            case "cone":
            //will have no onsite
            //start at origin and expand to onsite-size while maintaining 5e Cone shape rules
            break;
            case "beam":
            //line formula between origin and destination
            break;
            case "ball":
            //origin - destination
            //make a generic ball function/formula that can be passed on series of points to make it look like it's moving 
            //or bind a runnable to poop particles on it every so often
            break;
            case "projectile":
            //might tweak this so it takes the item Icon and passes it at a dropped entity,
            // fired in a parabola with xyz vector motion using some trig to get the arch between points
            //you know make it unique and stuff
            Arrow arrow = tokenOrigin.launchProjectile(Arrow.class);
            Vector velocity = destination.toVector().subtract(arrow.getLocation().toVector()).normalize();
            arrow.setVelocity(arrow.getVelocity().add(velocity));
            break;
            case "instant":
            //will have no particle in travel
            break;
        }
        //then on-site logic it makes sense to parse one, and then parse the other, as it's almost two different things :)
        //explosion
        //shapefill
        //none
        //persistance logic for Sam to deal with 
        return null;
    }
    public Inventory spellChooser(Player viewer){
        // Grimoire grimoire = new Grimoire(app);
        //will need special code now to compare to player's personal spell list
        // File dir = new File("plugins/DMTools");
        // File[] directoryListing = dir.listFiles();
        Inventory inv = Bukkit.createInventory(null, 18, "Personal Grimoire"); 
        //do some math here and find the factor of 9, then do multiple pages, blah blah blah
        ArrayList<String> learnedSpells = load("plugins/DMTools/" + viewer.getName() + ".grim");
        if (learnedSpells.size() >= 1) {
            Integer itercount = 0;
            for (String spell : learnedSpells) {
                HashMap<String, String> spellContents = load("plugins/DMTools/" + spell + ".spell");
                ItemStack referenceItem = new ItemStack(Material.valueOf(spellContents.get("item").toUpperCase()), 1);
                ItemMeta referenceMeta= referenceItem.getItemMeta();
                referenceMeta.setDisplayName(spell);
                referenceItem.setItemMeta(referenceMeta);
                inv.setItem(itercount, referenceItem);
                itercount++;
            }
        }
        //construct labels here
        return inv;
    }
    public Void collateralGlow(LivingEntity glower){
        //mobs caught within range will glow for period of time
        return null;
    }
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory() != playerView.get(e.getWhoClicked())) return;
        else{
        }
                e.setCancelled(true);
                playerSpellData.put((Player) e.getWhoClicked(), (HashMap<String, String>) load("plugins/DMTools/" + e.getCurrentItem().getItemMeta().getDisplayName() + ".spell"));
                ItemStack SpellBook = new ItemStack(Material.ENCHANTED_BOOK, 1);
                ItemMeta SpellBookMeta = SpellBook.getItemMeta();
                SpellBookMeta.setDisplayName("Grimoire");
                ArrayList<String> loreArray = new ArrayList();
                loreArray.add((String) playerSpellData.get(e.getWhoClicked()).get(e.getCurrentItem().getItemMeta().getDisplayName()));
                SpellBookMeta.setLore(loreArray);
                SpellBook.setItemMeta(SpellBookMeta);
                //check inventory for existing spell focus, if nothing then proceed, if existing then swap out.
                e.getWhoClicked().getInventory().addItem(SpellBook);
                playerView.remove(e.getWhoClicked());
                e.getView().close();
                return;
                //check for current spell item, change if exists, give new if not
    }
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




}
