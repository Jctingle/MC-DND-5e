package jeffersondev.Utilities;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MainHand;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import jeffersondev.App;

public class initiative implements CommandExecutor,Listener {   
    private App app;
    public initiative(App app){
        this.app = app;
    }
    //eventually include something that store combat rounds out of combat;
    //add more comments
    ScoreboardManager manager = Bukkit.getScoreboardManager();
    TreeMap<Integer, ArrayList<String>> initBlock = new TreeMap<>();
    Map<String, Player> unitOwner = new Hashtable<>();
    Integer currentTurn = 0;
    final Scoreboard board = manager.getNewScoreboard();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){
            Player p = ((Player) sender);
            if (args[0].equals("start")) {
                final Objective objecteye = board.registerNewObjective("initiative", "dummy", "initiative", RenderType.INTEGER);
                objecteye.setDisplaySlot(DisplaySlot.SIDEBAR);
                p.sendMessage("Starting New Combat Round");
                Team activeUnit = board.registerNewTeam("active");
                activeUnit.setColor(ChatColor.GREEN);
                Team deadUnit = board.registerNewTeam("dead");
                deadUnit.setColor(ChatColor.BLACK);
                return true;
            } else if (args[0].equals("end")) {
                    final Objective objecteye = board.getObjective("initiative");
                    objecteye.unregister();
                    ItemStack item = new ItemStack(Material.EMERALD, 1);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName("End Turn");
                    item.setItemMeta(meta);
                    for(Player online : unitOwner.values()){
                        online.setScoreboard(manager.getNewScoreboard());
                        online.getInventory().removeItem(item);
                        }
                    p.sendMessage("Combat over, clearing display");
                    Team activeUnit = board.getTeam("active");
                    Team deadUnit = board.getTeam("dead");
                    activeUnit.unregister();
                    deadUnit.unregister();
                    initBlock.clear();
                    unitOwner.clear();
                    currentTurn = 0;

            }
            else if (args[0].equals("join")) {
                final Objective objecteye = board.getObjective("initiative");
                String unitName = args[1];
                String initRoll = args[2];
                unitOwner.put(unitName,p);
                ArrayList<String> tempAL = new ArrayList<String>();
                if(initBlock.isEmpty()){
                    tempAL.add(unitName);
                    initBlock.put(Integer.parseInt(initRoll), tempAL);

                    Score score = objecteye.getScore(unitName);
                    score.setScore(Integer.parseInt(initRoll));
                    p.sendMessage("Successfully joined with " + unitName + " please wait for the round to begin");
                }
                else if (initBlock.get(Integer.parseInt(initRoll)) != null && initBlock.get(Integer.parseInt(initRoll)).size() >= 1){
                    //need to add a duplicate check
                    tempAL = initBlock.get(Integer.parseInt(initRoll));
                    tempAL.add(unitName);
                    initBlock.replace(Integer.parseInt(initRoll), tempAL);
                    Score score = objecteye.getScore(unitName);
                    score.setScore(Integer.parseInt(initRoll));
                    p.sendMessage("Successfully joined with " + unitName + " Please wait for the round to begin");
                }
                else{
                    tempAL.add(unitName);
                    initBlock.put(Integer.parseInt(initRoll), tempAL);

                    Score score = objecteye.getScore(unitName);
                    score.setScore(Integer.parseInt(initRoll));
                    p.sendMessage("Successfully joined with " + unitName + " Please wait for the round to begin");
                    //send a message to the DM about who is joining with what score
                    //this will be combined with writing permissions
                }
            }
            else if (args[0].equals("begin")) {
                Map.Entry<Integer, ArrayList<String>> pull = initBlock.lastEntry();
                ArrayList<String> tempAL = pull.getValue();
                currentTurn = pull.getKey();
                activeTurn(tempAL);
                for(Player player : unitOwner.values()){
                    player.setScoreboard(board);
                }
                }
            else if (args[0].equals("kill")){
                ItemStack item = new ItemStack(Material.EMERALD, 1);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("End Turn");
                item.setItemMeta(meta);
                String toDie = args[1];
                Team deadUnit = board.getTeam("dead");
                Team activeUnit = board.getTeam("active");
                if(initBlock.get(currentTurn).contains(toDie) && initBlock.get(currentTurn).size() == 1){
                    if(unitOwner.get(toDie).getInventory().contains(item)){
                        unitOwner.get(toDie).getInventory().remove(item);
                    }
                    ArrayList<String> value = initBlock.get(currentTurn);           
                    if (value.contains(toDie) && value.size() == 1){
                        initBlock.remove(currentTurn);
                        activeUnit.removeEntry(toDie);
                        deadUnit.addEntry(toDie);
                    }
                    else if (value.contains(toDie) && value.size() > 1){
                        value.remove(toDie);
                        initBlock.replace(currentTurn, value);
                        activeUnit.removeEntry(toDie);
                        deadUnit.addEntry(toDie);
                    }
                    ArrayList tempAL = whoseTurnNext();
                    activeTurn(tempAL);
                }
                else{
                    for(Map.Entry<Integer,ArrayList<String>> entry : initBlock.entrySet()) {
                        Integer key = entry.getKey();
                        ArrayList<String> value = entry.getValue();           
                        if (value.contains(toDie) && value.size() == 1){
                            initBlock.remove(key);
                            deadUnit.addEntry(toDie);
                        }
                        else if (value.contains(toDie) && value.size() > 1){
                            value.remove(toDie);
                            initBlock.replace(key, value);
                            deadUnit.addEntry(toDie);
                        }
                    }
                }

            }
                return true;
        }
        else{
            System.out.println("Cannot execute this command on the command line");
            return false;
            }
    }
    public void activeTurn(ArrayList<String> yourTurn){
        ItemStack item = new ItemStack(Material.EMERALD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("End Turn");
        item.setItemMeta(meta);

        Team activeUnit = board.getTeam("active");

        for (String entry : initBlock.get(currentTurn)) {
            activeUnit.addEntry(entry);
            if (!unitOwner.get(entry).getInventory().contains(item)){
                unitOwner.get(entry).getInventory().addItem(item);
                unitOwner.get(entry).sendTitle("Your Turn", "Please make your move.", 1, 20, 1);
            }
        }

    }
    public ArrayList<String> whoseTurnNext(){
        Map.Entry<Integer, ArrayList<String>> nextEntry = initBlock.lowerEntry(currentTurn);
        if (nextEntry == null){
            nextEntry = initBlock.lastEntry();
            currentTurn = nextEntry.getKey();
            ArrayList<String> returnArray = nextEntry.getValue();
            return returnArray;
        }
        else{
            ArrayList<String> returnArray = nextEntry.getValue();
            currentTurn = nextEntry.getKey();
            return returnArray;
        }
    }
    @EventHandler
    public void onRightClick(PlayerInteractEvent event){
        Player player = event.getPlayer();  
        ItemStack item = new ItemStack(Material.EMERALD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("End Turn");
        item.setItemMeta(meta);

        Team activeUnit = board.getTeam("active");
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if (player.getInventory().getItemInMainHand() == null || player.getInventory().getItemInMainHand().getType() == Material.AIR) {
                // they have nothing in their hand
            }
            else if(player.getInventory().getItemInMainHand().getItemMeta().equals(meta) && event.getHand().equals(EquipmentSlot.HAND)){  
                event.setCancelled(true);
                Bukkit.broadcastMessage(ChatColor.GOLD + player.getName() + ChatColor.DARK_RED + " Has finished their Turn");
                player.getInventory().remove(item);
                if (initBlock.get(currentTurn).size() > 1){
                    Boolean tempBool = false;
                    for (String entry : initBlock.get(currentTurn)) {
                        if (unitOwner.get(entry).getInventory().contains(item)){
                            tempBool = true;
                        }
                        else if(activeUnit.hasEntry(entry)){
                            activeUnit.removeEntry(entry);
                        }
                    }
                    if (tempBool == true){
                    }
                    else{
                        for (String entry : initBlock.get(currentTurn)) {
                            activeUnit.removeEntry(entry);
                        }
                        ArrayList tempAL = whoseTurnNext();
                        activeTurn(tempAL);
                    }
                }
                else{
                    //just pass the next group into active and remove the current guy from active group, ezpz
                    for (String entry : initBlock.get(currentTurn)) {
                            activeUnit.removeEntry(entry);
                        }
                    ArrayList tempAL = whoseTurnNext();
                    activeTurn(tempAL);
                    }
            }
        }
                }
}
