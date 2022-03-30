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
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import jeffersondev.App;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Grimoire implements CommandExecutor,Listener {
    private App app;
    public Grimoire(App app){
        this.app = app;
    }
    Map<Player, Inventory> playerView = new HashMap<>();
    Map<Player, Inventory> playerEdit = new HashMap<>();
    HashMap<Player, ArrayList<String>> grimcallers = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        Player p = (Player) sender;
        if (args.length == 0){
            Inventory inv = spellViewer(p);
            openInventory(p, inv);
            playerView.put(p, inv);
        }
        else if (args.length == 1 && args[0].equals("new")){
            Inventory inv = spellConstructor();
            playerView.put(p, inv);
            openInventory(p, inv);
        }
        else if (args.length == 2 && args[0].equals("edit")){
            String spellGrab = args[1];
            Inventory inv = spellEditor(spellGrab);
            playerView.put(p, inv);
            openInventory(p, inv);
        }
        //do edit code

        return true;
    }
    public void openInventory(final HumanEntity ent, Inventory inv) {
        ent.openInventory(inv);
    }
    public Inventory spellViewer(Player viewer){
        //will need special code now to compare to player's personal spell list
        File dir = new File("plugins/DMTools");
        File[] directoryListing = dir.listFiles();
        Inventory inv = Bukkit.createInventory(null, 18, "Spell Library"); 
        //do some math here and find the factor of 9, then do multiple pages, blah blah blah
        if (directoryListing != null) {
            Integer itercount = 0;
            for (File child : directoryListing) {
                String fileIter = child.getAbsolutePath();
                //this now imposes a problem
                if (child.getName().endsWith(".spell")){
                    HashMap<String, String> spellContents = load(fileIter);
                    ItemStack referenceItem = new ItemStack(Material.valueOf(spellContents.get("item").toUpperCase()), 1);
                    ItemMeta referenceMeta= referenceItem.getItemMeta();
                    referenceMeta.setDisplayName(child.getName().replace(".spell", ""));
                    if(grimcallers.get(viewer) != null && grimcallers.get(viewer).contains(child.getName().replace(".spell", ""))){
                        referenceMeta.addEnchant(Enchantment.LURE, 1, true);
                        referenceMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    }
                    referenceItem.setItemMeta(referenceMeta);
                    inv.setItem(itercount, referenceItem);
                    itercount++;
                }
            }
        }
        //construct labels here
        return inv;
    }
    public Inventory spellConstructor(){
        Inventory inv = Bukkit.createInventory(null, 18, "Spell Construction"); 
        ItemStack labelone = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
        ItemStack labeltwo = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
        ItemStack labelthree = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
        ItemStack unusedSlot = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta metaOne = labelone.getItemMeta();
        ItemMeta metaTwo = labeltwo.getItemMeta();
        ItemMeta metaThree = labelthree.getItemMeta();
        ItemMeta metaUnused = unusedSlot.getItemMeta();
        metaOne.setDisplayName("^^^ This is the Name & item Token slot ^^^");
        labelone.setItemMeta(metaOne);
        metaTwo.setDisplayName("^^^ This is Travel spell component slot ^^^");
        labeltwo.setItemMeta(metaTwo);
        metaThree.setDisplayName("^^^ This is the On-Site spell component slot ^^^");
        labelthree.setItemMeta(metaThree);
        metaUnused.setDisplayName("This Slot is currently unused");
        unusedSlot.setItemMeta(metaUnused);
        inv.setItem(9, labelone);
        inv.setItem(10, labeltwo);
        inv.setItem(11, labelthree);
        for(Integer i=3; i<9; i++){
            inv.setItem(i, unusedSlot);
        }
        for(Integer i=12; i<18; i++){
            inv.setItem(i, unusedSlot);
        }
        //construct labels here
        return inv;
    }
    public Inventory spellEditor(String fileName){
        //I really hate this one ngl
        HashMap<String, String> spellEdit = load("plugins/DMTools/" + fileName +".spell");
        Inventory inv = Bukkit.createInventory(null, 18, "Spell Editing"); 
        ItemStack labelone = new ItemStack(Material.RED_STAINED_GLASS_PANE, 1);
        ItemStack labeltwo = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
        ItemStack labelthree = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
        ItemStack unusedSlot = new ItemStack(Material.BLACK_STAINED_GLASS_PANE, 1);
        ItemMeta metaOne = labelone.getItemMeta();
        ItemMeta metaTwo = labeltwo.getItemMeta();
        ItemMeta metaThree = labelthree.getItemMeta();
        ItemMeta metaUnused = unusedSlot.getItemMeta();
        metaOne.setDisplayName("^^^ This Slot is Uneditable ^^^");
        labelone.setItemMeta(metaOne);
        metaTwo.setDisplayName("^^^ This is Travel spell component slot ^^^");
        labeltwo.setItemMeta(metaTwo);
        metaThree.setDisplayName("^^^ This is the On-Site spell component slot ^^^");
        labelthree.setItemMeta(metaThree);
        metaUnused.setDisplayName("This Slot is currently unused");
        unusedSlot.setItemMeta(metaUnused);
        inv.setItem(9, labelone);
        inv.setItem(10, labeltwo);
        inv.setItem(11, labelthree);
        for(Integer i=3; i<9; i++){
            inv.setItem(i, unusedSlot);
        }
        for(Integer i=12; i<18; i++){
            inv.setItem(i, unusedSlot);
        }
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~LABEL SLOT CODE~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        ItemStack referenceItem = new ItemStack(Material.valueOf(spellEdit.get("item").toUpperCase()), 1);
        ItemMeta referenceMeta= referenceItem.getItemMeta();
        referenceMeta.setDisplayName(fileName);
        referenceItem.setItemMeta(referenceMeta);
        inv.setItem(0, referenceItem);
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~^ Slot 1 Filler ^~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        String travelType = spellEdit.get("traveltype");
        String particle = spellEdit.get("travelparticle");
        //travelsize go here
        //build fields from args for variable readability
        ItemStack travelToken = new ItemStack(Material.PRISMARINE_SHARD, 1);
        ItemMeta travelmeta = travelToken.getItemMeta();
        travelmeta.setDisplayName(travelType);
        //build the lore components
        ArrayList<String> travelLore = new ArrayList<String>();
        travelLore.add(particle);
        travelmeta.setLore(travelLore);
        travelToken.setItemMeta(travelmeta);
        inv.setItem(1, travelToken);
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~^ Slot 2 Filler ^~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        String onsiteType = spellEdit.get("onsiteeffect");
        String onsiteParticle = spellEdit.get("onsiteparticle");
        String onsiteShape = spellEdit.get("onsiteshape");
        String onsiteSize = spellEdit.get("onsitesize");
        String onsitePersist = spellEdit.get("persistant");              
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
        inv.setItem(2, onsiteToken);
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~^ Slot 3 Filler ^~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        return inv;
    }
    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e){
        //this will make sense
        if (e.getInventory() != playerView.get(e.getPlayer())) return;
        //logic to error catch cases where there are no items
        else if (!e.getView().getTitle().equals("Spell Library")){
            String fileName = e.getView().getItem(0).getItemMeta().getDisplayName();
            ItemStack tokenSlot = e.getView().getItem(0);
            ItemStack travelSlot = e.getView().getItem(1);
            ItemStack onsiteSlot = e.getView().getItem(2);
            ItemMeta travelMeta = travelSlot.getItemMeta();
            ItemMeta onsiteMeta = onsiteSlot.getItemMeta();
            HashMap<String, String> spellStorage = new HashMap<>();
            spellStorage.put("item",tokenSlot.getType().toString());
            spellStorage.put("traveltype",travelMeta.getDisplayName());
            spellStorage.put("travelparticle",travelMeta.getLore().get(0));
            //travelsize go here
            spellStorage.put("onsiteeffect",onsiteMeta.getDisplayName());
            spellStorage.put("onsiteshape",onsiteMeta.getLore().get(0));
            spellStorage.put("onsitesize",onsiteMeta.getLore().get(1));
            spellStorage.put("onsiteparticle",onsiteMeta.getLore().get(2));
            spellStorage.put("persistant",onsiteMeta.getLore().get(3));
            save("plugins/DMTools/" + fileName +".spell", spellStorage);
            playerView.remove(e.getPlayer());
            return;
        }
        else{
            return;
        }
    }
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory() != playerView.get(e.getWhoClicked())) return;
        else{
            ArrayList<Integer> badSlots = new ArrayList<>(Arrays.asList(3,4,5,6,7,8,9,10,11,12,13,14,15,16,17));
            if (badSlots.contains(e.getRawSlot()) && e.getView().getTitle().equals("Spell Construction")){
                // e.getWhoClicked().sendMessage(Integer.toString(e.getSlot()));
                e.setCancelled(true);
            }
            badSlots.add(0);
            if (badSlots.contains(e.getRawSlot()) && e.getView().getTitle().equals("Spell Editing")){
                e.setCancelled(true);
            }
            //code for selecting the spells for personal grimoire and spell construction and saving it to file, because it's onclick I don't need to worry about save on inventory close
            if(e.getView().getTitle().equals("Spell Library")){
                if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR){
                    //do nothing :)
                }
                else { 
                    ItemStack enchantify = e.getCurrentItem();
                    if (!grimcallers.containsKey(e.getWhoClicked()) || !grimcallers.get(e.getWhoClicked()).contains(enchantify.getItemMeta().getDisplayName())){
                        Inventory quickAdd = e.getWhoClicked().getInventory();
                        // quickAdd.addItem(enchantify);
                        ItemMeta quickAddMeta = enchantify.getItemMeta();
                        quickAddMeta.addEnchant(Enchantment.LURE, 1, true);
                        quickAddMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                        enchantify.setItemMeta(quickAddMeta);
                        // e.setCurrentItem(enchantify);
                        e.getView().setItem(e.getRawSlot(),enchantify);
                        //now saving to the player's collection, which is just about to come into existence
                        ArrayList<String> playerGrim = new ArrayList<String>();
                        if (grimcallers.containsKey(e.getWhoClicked())){
                            playerGrim = grimcallers.get(e.getWhoClicked());
                        }
                        playerGrim.add(enchantify.getItemMeta().getDisplayName());
                        grimcallers.put((Player) e.getWhoClicked(), playerGrim);
                        save("plugins/DMTools/" + e.getWhoClicked().getName() +".grim", grimcallers.get((Player) e.getWhoClicked()));
                        //will load instanced version on playerjoin,
                        //big save command will go here
                        // save(, grimcallers);

                    }
                    else if (grimcallers.get(e.getWhoClicked()).contains(enchantify.getItemMeta().getDisplayName())){
                        Inventory quickAdd = e.getWhoClicked().getInventory();
                        ItemMeta quickAddMeta = enchantify.getItemMeta();
                        quickAddMeta.removeEnchant(Enchantment.LURE);
                        enchantify.setItemMeta(quickAddMeta);
                        e.getView().setItem(e.getRawSlot(), enchantify);
                        ArrayList<String> playerGrim = grimcallers.get(e.getWhoClicked());
                        playerGrim.remove(enchantify.getItemMeta().getDisplayName());
                        grimcallers.put((Player) e.getWhoClicked(), playerGrim);
                        save("plugins/DMTools/" + e.getWhoClicked().getName() +".grim", grimcallers.get((Player) e.getWhoClicked()));
                    }
                }
                e.setCancelled(true);
            }
        }

    }
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory() != playerView.get(e.getWhoClicked())) {
            return;
        }
        else{
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        //will need special code now to compare to player's personal spell list
        File playerGrimoire = new File("plugins/DMTools/" + e.getPlayer().getName() + ".grim");
        if (playerGrimoire.exists()){ 
            ArrayList<String> memoryLoad = load("plugins/DMTools/" + e.getPlayer().getName() + ".grim");
            grimcallers.put(e.getPlayer(), memoryLoad);
        }
    }
    @EventHandler
    public void onPlayerLeave(final PlayerQuitEvent e) {
        //will need special code now to compare to player's personal spell list
        if (grimcallers.containsKey(e.getPlayer())){ 
            grimcallers.remove(e.getPlayer());
        }
    }
    //need an onplayerleave function to unload the player information
    //code to save spell data to storage
    //inventory click, and possibly drag, don't really understand that one will be cancelled if it's certain slots
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
}

