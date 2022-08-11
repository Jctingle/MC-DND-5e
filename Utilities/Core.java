package jeffersondev.Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import jeffersondev.App;
import jeffersondev.Tokens.MobMoverJCT;



public class Core implements CommandExecutor,Listener {   
    private App app;
    public Core(App app){
        this.app = app;
    }
    private MultiTool toolBox = new MultiTool();
    private ScoreboardManager manager = Bukkit.getScoreboardManager();
    private Scoreboard board = manager.getNewScoreboard();
    private static HashMap<Player, String> activePlayers = new HashMap();
    // GENERALIZED CORE, HANDLES EVENT FIRING FOR SAFE EVENTS WITHOUT REPEATING LOGIC
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){
            if (!activePlayers.containsKey(sender)){
                //this only needs to be called once per player ever, assuming no directory restart.
                //calling it again would just overwrite it. though it may not even work at all
                Player p = ((Player) sender);
                String playerName = args[0];
                activePlayers.put(p,playerName);
                Gamer playerGamer = new Gamer(p,playerName);
                save("plugins/DMTools/" + p.getName() +".gamer", playerGamer);
                p.sendMessage("Joined the Game as " + playerName);
                return true;
            }
            else{
                //if statements for potential alternate entry points to the Initiative system?
                return true;
            }
        }
        else{
            System.out.println("Cannot execute this command on the command line");
            return false;
            }
    }
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        toolBox.purgePlayerOfAllTools(e.getPlayer());
        File playerGrimoire = new File("plugins/DMTools/" + e.getPlayer().getName() + ".gamer");
        if (playerGrimoire.exists()){ 
            Gamer memoryLoad = load("plugins/DMTools/" + e.getPlayer().getName() + ".gamer");
            activePlayers.put(e.getPlayer(), memoryLoad.getCharacter());
        }
        //check if they have a gamer profile, if so, load it in.
    }
    @EventHandler
    public void onPlayerLeave(final PlayerQuitEvent e) {
        //deregister from any plugin roster players
        activePlayers.remove(e.getPlayer());
        MobMoverJCT.deRegister(e.getPlayer());
        Ruler.deRegister(e.getPlayer());
        //save cache of the player object which will also contain their spell grimoire.
        //spellcasting will be cleaned up
    }
    public static Boolean isGamer(Player p){
        if(activePlayers.containsKey(p)) return true;
        else                             return false;

    }
    public static HashMap<Player,String> getCharacters(){
        HashMap<Player,String> returnHash = (HashMap<Player, String>) activePlayers.clone();
        return returnHash;
    }
    @EventHandler
    public void onRightClickEntity(PlayerInteractEntityEvent e) {
        if(activePlayers.containsKey(e.getPlayer())){
                if(e.getHand() == EquipmentSlot.HAND && e.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null 
                && e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore() == true 
                && e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().get(0).equals("5ETool")) {
                    //code here
                    e.setCancelled(true);
                    switch(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()){
                    case "Ruler":
                        Ruler.RightClickEntity(e.getPlayer(), e.getRightClicked());
                        break;
                    case "Grimoire":
                        break;
                    case "Laser Pointer":
                        LaserPointer.handleRightClickEntity(e.getPlayer(),(LivingEntity) e.getRightClicked());
                        break;
                    default:
                        break;
                    }
                }
        }
    }
    @EventHandler
    public void onFKey(PlayerSwapHandItemsEvent e) {
        if(activePlayers.containsKey(e.getPlayer())){
                if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null 
                && e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore() == true 
                && e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().get(0).equals("5ETool")) {
                    //code here
                    e.setCancelled(true);
                    switch(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()){
                    case "Mover":
                        MobMoverJCT.RTXEntitySelect(e.getPlayer());
                        break;
                    default:
                        break;
                    }
                }
        }
    }
    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        //This bundle of if statements is to protect bad situations from interacting with each other
        if(activePlayers.containsKey(e.getPlayer())){
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if(e.getHand() == EquipmentSlot.HAND && e.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null 
                && e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore() == true 
                && e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().get(0).equals("5ETool")) {
                    //code here
                    e.setCancelled(true);
                    switch(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()){
                    case "Ruler":
                        Ruler.rightClickRTX(e.getPlayer());
                        break;
                    case "Mover":
                        MobMoverJCT.fireMovement(e.getPlayer());
                        break;
                    case "Grimoire":
                        break;
                    case "Laser Pointer":
                        LaserPointer.handleRightClick(e.getPlayer());
                        break;
                    case "End Turn":
                    //couple of events will happen here, going to fire a method from initiativeCore
                        InitiativeCore.HandleRightClick(e.getPlayer());
                        break;
                    }

                }
            }
            else if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
                if(e.getHand() == EquipmentSlot.HAND && e.getPlayer().getInventory().getItemInMainHand().getItemMeta() != null 
                && e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasLore() == true 
                && e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getLore().get(0).equals("5ETool")) {
                    //code here
                    e.setCancelled(true);
                    switch(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName()){
                    case "Ruler":
                        break;
                    case "Mover":
                        MobMoverJCT.menu(e.getPlayer());
                        break;
                    case "Grimoire":
                        break;
                    case "Laser Pointer":
                        break;
                    case "End Turn":
                        break;
                    }

                }
            }
        }
    }
    //FILE SAVING ABILITIES
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
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getView().getTitle().equals("Mover Remote")) return;
        else{
            Integer buttonClicked = e.getRawSlot();
            e.setCancelled(true);
            Player p = Bukkit.getPlayer(e.getWhoClicked().getName());
            // p.sendMessage("click registered");
            // p.sendMessage(buttonClicked.toString());
            switch (buttonClicked){
                case 0:
                    MobMoverJCT.returnMovingMob(p).moveTo();
                    break;
                case 1:
                    MobMoverJCT.returnMovingMob(p).faceMe(p);
                    break;
                case 2:
                    MobMoverJCT.returnMovingMob(p).faceAway(p);
                    break;
                case 3:
                    MobMoverJCT.returnMovingMob(p).moveToB();
                    break;

                case 4:
                if (MobMoverJCT.returnMovingMob(p).isCursor()){
                    MobMoverJCT.returnMovingMob(p).cursorBool();
                    MobMoverJCT.menu(p);
                    break;

                }
                else{
                    MobMoverJCT.returnMovingMob(p).cursorBool();
                    MobMoverJCT.menu(p);
                    break;
                }
                case 5:
                    MobMoverJCT.returnMovingMob(p).swingArm();
                    break;
                case 6:
                        MobMoverJCT.returnMovingMob(p).sizeUp();
                    break;
                case 7:
                    MobMoverJCT.returnMovingMob(p).sizeDown();
                    break;
                case 8:
                    MobMoverJCT.returnMovingMob(p).toggleGrowth();
                    break;
                case 9:
                    MobMoverJCT.returnMovingMob(p).toggleInvis();
                    break;
                default:
                    p.sendMessage("Don't even worry, nothing dangerous happened :^)");
            }
        }
    }
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (!e.getView().getTitle().equals("Mover Remote")) return;
        else{
            e.setCancelled(true);
        }
    }
}

