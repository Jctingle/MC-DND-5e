package jeffersondev.SpellCasting;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.bukkit.util.io.BukkitObjectInputStream;
import jeffersondev.App;
import org.bukkit.inventory.Inventory;
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
    Map<Player, LivingEntity> playerToken = new HashMap<>();
    ArrayList<Player> spellCache = new ArrayList<>();
    Map<Player, ConcentrationSpell> activeFocus = new HashMap<Player, ConcentrationSpell>();
    Map<Player, Location> tokenPerspective = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        // Grimoire grimoire = new Grimoire(app);
        Player p = (Player) sender;
        File playerGrimoire = new File("plugins/DMTools/" + p.getName() + ".grim");
        //add new option here for cuboid call, give option to right click one spot and another? Additional class tbh
        if (args.length > 0 && args[0].equals("stopall")){
            stopAllSpells();
        }
        else if (playerGrimoire.exists()){ 
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
    public void stopAllSpells(){
        for(ConcentrationSpell cSpell : activeFocus.values()){
            cSpell.cancel();
        }
        activeFocus.clear();
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
        Double travelSize = Double.parseDouble(playerSpellData.get(caster).get("travelsize"));
        switch(travelType){
            case "cone":
                //somehow lock player to token perspective and blast
                if(tokenPerspective.containsKey(caster)){
                    String coneParticle = (playerSpellData.get(caster).get("travelparticle")).toUpperCase();
                    //some kind of check for colourized particles as well as error inducing particles
                    ParticleCone testCone = new ParticleCone(caster.getEyeLocation(), coneParticle, caster.getEyeLocation().getDirection(), travelSize);
                    tokenOrigin.teleport(caster.getLocation());
                    caster.teleport(tokenPerspective.get(caster));
                    caster.setWalkSpeed(.2f);
                    caster.setFlySpeed(.1f);
                    tokenPerspective.remove(caster);
                    testCone.draw();
                }
                else if(!tokenPerspective.containsKey(caster) || !tokenOrigin.getType().equals(EntityType.PLAYER)){
                    tokenPerspective.put(caster, caster.getLocation());
                    caster.teleport(tokenOrigin.getLocation());
                    caster.setWalkSpeed(.0001f);
                    caster.setFlySpeed(.00001f);
                    tokenOrigin.teleport(tokenPerspective.get(caster));
                }
                else{
                    caster.sendMessage("Sorry, please select a spell origin token for Cone type spells");
                }
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
                    start.getWorld().spawnParticle(importParticle, l, 0, 0, 0, 0, 0.05);
                    //for secondary travel particle, put other one here
                }
                onSiteEffect(start,end,caster);
                //do-onSiteMethod
            break;
            case "skull":
            //testing with witherskulls for now
            //origin - destination
                WitherSkull wskull = tokenOrigin.launchProjectile(WitherSkull.class);
                Vector skullvelocity = end.toVector().subtract(wskull.getLocation().toVector()).normalize();
                wskull.setVelocity(wskull.getVelocity().add(skullvelocity));
                onSiteEffect(start,end,caster);
                //do onsite
            break;
            case "ball":
                onSiteEffect(start,end,caster);
            break;
            //expand arrow case/duplicate and make spectral arrow, or other visible travel arrow varieties
            case "arrow":
                Arrow arrow = tokenOrigin.launchProjectile(Arrow.class);
                Vector velocity = end.toVector().subtract(arrow.getLocation().toVector()).normalize();
                arrow.setVelocity(arrow.getVelocity().add(velocity));
                onSiteEffect(start,end,caster);
            //do-onSiteMethod
            break;
            case "instant":
            //will have no particle in travel
            //do-onSiteMethod
                onSiteEffect(start,end,caster);
            break;
            case "lightning":
                onSiteEffect(start,end,caster);
            break;
            //I want a case where it takes two different types and corkscrews it towards them
        }
        //then on-site logic it makes sense to parse one, and then parse the other, as it's almost two different things :)
        //persistance logic for Sam to deal with 
        return null;
    }
    //borrowed Code
    public void onSiteEffect(Location start, Location end, Player caster){

        //Special onsite LOCATION, can be calculated fast, only calculate special-special cuboid origin for certain effects.
        String onsiteType = playerSpellData.get(caster).get("onsiteeffect");
        String onsiteShape = playerSpellData.get(caster).get("onsiteshape");
        Double onsiteSize = Double.parseDouble(playerSpellData.get(caster).get("onsitesize"));
        String onsiteParticle = playerSpellData.get(caster).get("onsiteparticle");
        String onsitePersist = playerSpellData.get(caster).get("persistant");
        Double onsiteHeight = Double.parseDouble(playerSpellData.get(caster).get("onsiteheight"));

        

        //auto cancel any pre-existing concentration area, can tweak this if necessary;
        //!!!!!!!!!!REMINDER TO MAKE A STOP ALL COMMAND!!!!!!!
        //instance variables called into existence here
        if(onsiteType.equals("shape")){
            switch(onsiteShape){
            case "sphere":
                if(onsitePersist.equals("true")){
                    if(activeFocus.containsKey(caster)){
                        activeFocus.get(caster).cancel();
                    }
                    Location pSpot = end;
                    pSpot.subtract(onsiteSize/2, 0, onsiteSize/2);
                    ConcentrationSpell concentrateSphere = new ConcentrationSpell(pSpot,onsiteParticle,onsiteSize, "sphere");
                    activeFocus.put(caster, concentrateSphere);
                    concentrateSphere.runTaskTimer(app, 0, 40);
                }
                else{
                    Location pSpot = end;
                    pSpot.subtract(onsiteSize/2, 0, onsiteSize/2);
                    ParticleRect cube = new ParticleRect(pSpot, onsiteSize,onsiteSize,onsiteSize, onsiteParticle);
                    cube.draw();
                }
                break;
            case "cube":
                if(onsitePersist.equals("true")){
                    if(activeFocus.containsKey(caster)){
                        activeFocus.get(caster).cancel();
                    }
                    Location pSpot = end;
                    pSpot.subtract(onsiteSize/2, 0, onsiteSize/2);
                    ConcentrationSpell concentrateCube = new ConcentrationSpell(pSpot,onsiteParticle,onsiteSize/3, "cube");
                    activeFocus.put(caster, concentrateCube);
                   concentrateCube.runTaskTimer(app, 0, 40);
                }
                else{
                    Location pSpot = end;
                    pSpot.subtract(onsiteSize/2, 0, onsiteSize/2);
                    ParticleRect cube = new ParticleRect(pSpot, onsiteSize,onsiteSize,onsiteSize, onsiteParticle);
                    cube.draw();
                }
                break;
            case "cylinder":
                if(onsitePersist.equals("true")){
                    if(activeFocus.containsKey(caster)){
                        activeFocus.get(caster).cancel();
                    }
                    Location pSpot = end;
                    ConcentrationSpell concentrateCyl = new ConcentrationSpell(pSpot,onsiteParticle,onsiteSize/3, "cylinder", onsiteHeight);
                    activeFocus.put(caster, concentrateCyl);
                   concentrateCyl.runTaskTimer(app, 0, 40);
                }
                else{
                    Location pSpot = end;
                    //THIS WILL NEED AN UPDATE ONCE HEIGHT IS TACKED ON
                    ParticleCyl cyl = new ParticleCyl(pSpot, onsiteParticle, onsiteSize, onsiteHeight);
                    cyl.draw();
                }
                break;
            }
        }
        else if(onsiteType.equals("explosion")){
        }
        else if(onsiteType.equals("target")){
        }
        else { 
        }
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
                ItemStack SpellBook = matchItem();
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
            ItemStack SpellBook = matchItem();
            if(e.getPlayer().getInventory().getItemInMainHand() == (null) || e.getPlayer().getInventory().getItemInMainHand().getItemMeta() == null){
                return;
            }
            else{
                //if item in their main hand is spellbook
                if(e.getPlayer().getInventory().getItemInMainHand().equals(SpellBook)){
                    //if they have a reference token and a spell Queued
                    if(playerSpellData.containsKey(e.getPlayer()) && e.getPlayer().isSneaking()){
                        playerToken.remove(e.getPlayer());
                    }
                    else if(playerSpellData.containsKey(e.getPlayer()) && playerToken.containsKey(e.getPlayer())) {
                        Player p = (Player) e.getPlayer();
                        if(tokenPerspective.containsKey(p)){
                            castSpell(playerToken.get(p).getLocation().add(0,1,0), p.getLocation(), e.getPlayer(), playerToken.get(p)); 
                        }
                        else{
                            RayTraceResult rtx = p.getWorld().rayTraceBlocks(p.getEyeLocation(), p.getEyeLocation().getDirection(), 100);
                            if (rtx != null){
                                Location pEnd = rtx.getHitPosition().toLocation(p.getWorld());
                                castSpell(playerToken.get(p).getLocation().add(0,1,0), pEnd, e.getPlayer(), playerToken.get(p));                 
                            }
                        }
                    }
                    //no token but still a spell queued
                    else if(playerSpellData.containsKey(e.getPlayer())) {
                        Player p = (Player) e.getPlayer();
                        RayTraceResult rtx = p.getWorld().rayTraceBlocks(p.getEyeLocation(), p.getEyeLocation().getDirection(), 100);
                        if (rtx != null){
                            Location pEnd = rtx.getHitPosition().toLocation(p.getWorld());
                            Location pStart = p.getEyeLocation();
                            //castspell
                            castSpell(pStart, pEnd, e.getPlayer(), e.getPlayer());                 
                            }
                    }
                    else{
                        e.getPlayer().sendMessage("Please select a spell from your spellbook");
                    }
                }
            }
            
        }   
    }
    // handel if the player right clicks an entity or shift right clicks
    public ItemStack matchItem(){
        ItemStack pointerItem = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta pointerMeta = pointerItem.getItemMeta();
        pointerMeta.setDisplayName("Grimoire");
        pointerItem.setItemMeta(pointerMeta); 

        return pointerItem;
    }
    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent e) {
        ItemStack SpellBook = matchItem();
        if(e.getPlayer().getInventory().getItemInMainHand() == (null) || e.getPlayer().getInventory().getItemInMainHand().getItemMeta() == null){
            return;
        }
        else if(playerSpellData.containsKey(e.getPlayer()) && e.getPlayer().getInventory().getItemInMainHand().equals(SpellBook)) {
            if(!playerToken.containsKey(e.getPlayer()) && e.getPlayer().isSneaking() && e.getRightClicked().getScoreboardTags().contains("token")){
                LivingEntity spellSource = (LivingEntity) e.getRightClicked();
                playerToken.put(e.getPlayer(), spellSource);
            }else if(playerToken.containsKey(e.getPlayer())){
                Player p = (Player) e.getPlayer();
                Location targetCenter = e.getRightClicked().getLocation().add(0,1,0);
                castSpell(p.getEyeLocation(), targetCenter, e.getPlayer(), playerToken.get(p));
            }
            else{
                Player p = (Player) e.getPlayer();
                Location targetCenter = e.getRightClicked().getLocation().add(0,1,0);
                castSpell(p.getEyeLocation(), targetCenter, e.getPlayer(), e.getPlayer());
            }
        }
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
