package jeffersondev.SpellCasting;

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
import org.bukkit.entity.WitherSkull;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.bukkit.scheduler.BukkitRunnable;
import jeffersondev.App;
import jeffersondev.SpellCasting.Grimoire;

import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
        //add new option here for cuboid call, give option to right click one spot and another? Additional class tbh
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
    public Void castSpell(Location start, Location end, Player caster, LivingEntity tokenOrigin){
        //9 cache variables? traveltype, travelparticle, onsiteeffect, onsiteshape
        //                   onsitesize, onsiteparticle, persistant
        //travel logic
        String travelType = playerSpellData.get(caster).get("traveltype");
        switch(travelType){
            //travel motion thoughts: runnable that tracks an invisible arrow moving slower? 
            //or some kind of controllable flying particle with it's location pinged with particles every x ticks
            //then canceled upon end of animation/onhit
        //formula for intervals of travel particles will be distance*5? I think that makes the most sense
            case "cone":
            //formula for triangle area of blocks in front if possible, not a lot of spaces, then do a 
            //will have no onsite
            //start at origin and expand to onsite-size while maintaining 5e Cone shape rules
            //what if I just make this into like a series amount of lines depending on onsite size :[]
            break;
            case "beam":
            //line formula between origin and destination
            //This is borrowed code from someone online in a spigot forum
                Particle importParticle = Particle.valueOf((playerSpellData.get(caster).get("travelparticle")).toUpperCase());
                double pointsPerLine = (start.distance(end)) * 4.0;
                double d = start.distance(end) / pointsPerLine;
                for (int i = 0; i < pointsPerLine; i++) {
                    Location l = start.clone();
                    Vector direction = end.toVector().subtract(start.toVector()).normalize();
                    Vector v = direction.multiply(i * d);
                    l.add(v.getX(), v.getY(), v.getZ());
                    start.getWorld().spawnParticle(importParticle, l, 1);
                    //for secondary travel particle, put other one here
                }
                //do-onSiteMethod
            break;
            case "skull":
            //testing with witherskulls for now
            //origin - destination
                WitherSkull wskull = tokenOrigin.launchProjectile(WitherSkull.class);
                Vector skullvelocity = end.toVector().subtract(wskull.getLocation().toVector()).normalize();
                wskull.setVelocity(wskull.getVelocity().add(skullvelocity));
            break;
            case "ball":

            break;
            //expand arrow case/duplicate and make spectral arrow, or other visible travel arrow varieties
            case "arrow":
                Arrow arrow = tokenOrigin.launchProjectile(Arrow.class);
                Vector velocity = end.toVector().subtract(arrow.getLocation().toVector()).normalize();
                arrow.setVelocity(arrow.getVelocity().add(velocity));
            //do-onSiteMethod
            break;
            case "instant":
            //will have no particle in travel
            //do-onSiteMethod
            break;
            //I want a case where it takes two different types and corkscrews it towards them
        }
        //then on-site logic it makes sense to parse one, and then parse the other, as it's almost two different things :)
        //persistance logic for Sam to deal with 
        return null;
    }
    //borrowed Code
    public void onSiteEffect(){

        //Special onsite LOCATION, can be calculated fast, only calculate special-special cuboid origin for certain effects.

        //instance variables called into existence here
        //switch
        //explosion
        //shapefill This is gonna be hard for a couple of reasons
        //justTarget fun little flitter of particles over the target if it's a single target spell
        //none
        return;
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
        PotionEffect targetGlow = new PotionEffect(PotionEffectType.GLOWING, 50, 1);
        glower.addPotionEffect(targetGlow);
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
                // ArrayList<String> loreArray = new ArrayList();
                // loreArray.add((String) playerSpellData.get(e.getWhoClicked()).get(e.getCurrentItem().getItemMeta().getDisplayName()));
                // SpellBookMeta.setLore(loreArray);
                SpellBook.setItemMeta(SpellBookMeta);
                //check inventory for existing spell focus, if nothing then proceed, if existing then swap out.
                e.getWhoClicked().getInventory().addItem(SpellBook);
                playerView.remove(e.getWhoClicked());
                e.getView().close();
                return;
                //check for current spell item, change if exists, give new if not
    }
    //eventhandler for right click when a spell is loaded into the player inventory
    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(e.getPlayer().getInventory().getItemInMainHand() == (null) || e.getPlayer().getInventory().getItemInMainHand().getItemMeta() == null){
                return;
            }
            else if(playerSpellData.containsKey(e.getPlayer()) && e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("Grimoire")) {
                Player p = (Player) e.getPlayer();
                RayTraceResult rtx = p.getWorld().rayTraceBlocks(p.getEyeLocation(), p.getEyeLocation().getDirection(), 100);
                if (rtx != null){
                    Location pEnd = rtx.getHitPosition().toLocation(p.getWorld());
                    Location pStart = p.getLocation();
                    //castspell
                    castSpell(pStart, pEnd, e.getPlayer(), e.getPlayer());
                    
                    }
                else{
                }
            }
        }   
    }
    //handel if the player right clicks an entity or shift right clicks
    // @EventHandler
    // public void onRightClick(PlayerInteractEntityEvent e) {
    //     ItemStack pointerItem = matchItem();
    //     if(activeUsers.containsKey(e.getPlayer()) && e.getPlayer().getInventory().getItemInMainHand().getItemMeta().equals(pointerItem.getItemMeta())) {           
    //             Player p = (Player) e.getPlayer();
    //             LivingEntity glower = (LivingEntity) e.getRightClicked();
    //             PotionEffect targetGlow = new PotionEffect(PotionEffectType.GLOWING, 50, 1);
    //             glower.addPotionEffect(targetGlow);
    //         }  
    // }
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
