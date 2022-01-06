package jeffersondev;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.Location;

import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.bukkit.scheduler.BukkitRunnable;
import jeffersondev.App;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class mobicon implements CommandExecutor,Listener,TabCompleter {   
    private App app;
    public mobicon(App app){
        this.app = app;
    }
    //these can be overridden by permissions, well some of them
    ArrayList<Player> tokenPlacer = new ArrayList<>();
    ArrayList<Player> dungeonMaster = new ArrayList<>();
    Map<Player, ArrayList<String>> informationHold = new HashMap<>();
    Map<Player, ArrayList<String>> unitSpawner = new HashMap<>();
    //will need an onmobdeath reaction for when they are murdered by hand or /killed to remove them from the list of units in unitSpawners
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){
            Player p = ((Player) sender).getPlayer();
            if (!tokenPlacer.contains(p)) {
                tokenPlacer.add(p);
                //need if's or situation proofers
                    p.sendMessage("Creating Token" + args[1]);
                    String mobType = args[1].toString();
                    String mobName = args[2].toString();
                    String health = args[3].toString();
                    String AC = args[4].toString();
                    ArrayList<String> temphold = new ArrayList<>();
                    temphold.add(mobType);
                    temphold.add(mobName);
                    temphold.add(health);
                    temphold.add(AC);
                    if (args[5] != null && args[5].equals("pc")){
                        temphold.add("PC");
                        informationHold.put(p, temphold);
                    }
                    else{
                        informationHold.put(p, temphold);
                    }
                // noteText = args[0];
                                        }
            return true;
        }
        else{
            System.out.println("Cannot execute this command on the command line");
            return false;
            }
    }
    @EventHandler
    public void onRightClick(PlayerInteractEvent e, PlayerInteractEntityEvent e1) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(tokenPlacer.contains(e.getPlayer())) {
                Player p = (Player) e.getPlayer();
                RayTraceResult rtx = p.getWorld().rayTraceBlocks(p.getEyeLocation(), p.getEyeLocation().getDirection(), 100);
                if (rtx != null){
                    Location pSpot = rtx.getHitPosition().toLocation(p.getWorld());

                    //deconstructor I guess
                    ArrayList<String> tokenInfo = informationHold.get(p);
                    String mobType = tokenInfo.get(0).toString();
                    String mobName = tokenInfo.get(1).toString();
                    Double health = Double.parseDouble(tokenInfo.get(2).toString());
                    Double AC = Double.parseDouble(tokenInfo.get(3).toString());
                    //end deconstructor

                    Location pSpotCopy = pSpot.clone();
                    pSpotCopy.setY(pSpot.getY() - 2);
                    LivingEntity token = (LivingEntity) p.getLocation().getWorld().spawnEntity(pSpot,EntityType.valueOf(mobType));
                    tokenPlacer.remove(p);
                    token.setSilent(true);
                    token.setAI(false);
                    //check and set max health higher than current
                    token.setHealth(health);
                    token.setCustomName(mobName);
                    token.setCustomNameVisible(true);   
                    token.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(AC);
                    token.setInvulnerable(true);
                    token.addScoreboardTag("token");
                    informationHold.remove(p);
                    //add PC tag for player tokens
                    }
                else{
                    p.sendMessage("Please select solid ground");
                }
            }
            //right click on the specific mob, and gets their info
            //need a check for OP and/or DM player
            else if(unitSpawner.containsKey(e.getPlayer())){
                Player p1 = (Player) e.getPlayer();
                ArrayList tempOwned = unitSpawner.get(p1);
                //will also need a case here to handle DM pulling info
                if (tempOwned.contains(e1.getRightClicked().getCustomName())){
                    LivingEntity tempEnt = (LivingEntity) e1.getRightClicked();
                    String tempName = tempEnt.getCustomName();
                    String tempAC = tempEnt.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).toString();
                    String tempHealth =  "" + tempEnt.getHealth();
                    String tempTempHP = "" + tempEnt.getAbsorptionAmount();
                    //need a switch with cases here; have to provide sitations for when tempHP is completely empty
                    p1.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("" + tempName + " AC: " + tempAC + " CurrentHealth: " + tempHealth + " tempHP: " + tempTempHP));
                }
            }
        }   
    }
    public void adjustMob(LivingEntity victim, double modim, Integer modType){
            //check for bool, if true then damage, if false then heal, absorption hearts for tempHP? that actually works SO WELL
            double tempHealth = victim.getHealth();
            tempHealth = tempHealth += modim;
            //if healing
            switch (modType){
                case 0:
                    //healing
                    //check for scoreboardtag Deathsaving, then remove because healing
                    //check for maxhealth value
                    tempHealth = tempHealth += modim;
                case 1:
                    //damage
                    //check for tempHP first, and then apply damage split between by first removing all tempHP, then apply damage to base health
                    //check for value, ability to drop to .5 is nice as it will represent death saving state
                    tempHealth = tempHealth -= modim;
                case 2:
                    //tempHP
                    //also check for deathsaving
                    //get absorption level, then check if absorb/thp greater than damage double, if greater than do nothing else set value to new temp HP
            }
    }
    public void setDeathSave(LivingEntity playerToken){
        //going to addscoreboardtag, have to determine a way of marking it as a player tag
        playerToken.addScoreboardTag("deathSaving");
        //command for fake killing a player token AKA putting in death saving mode
    }
    public void deleteMob(LivingEntity deceased, Player owner){
            //delete mob from existence
            //run this function/call it from other situations.
            //actual mob token deletion will depend on current state of mob
            //remove value from player key if player has more than one unit, or remove key and value if just the one unit
            if (unitSpawner.get(owner).size() >= 2){
                ArrayList<String> removedList = unitSpawner.get(owner);
                removedList.remove(deceased);
                unitSpawner.replace(owner, removedList);
            }
            else{
                unitSpawner.remove(owner);
            }
    }
    @Override
    public ArrayList<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("mobi")) {
            if (args.length == 1) {
                ArrayList<String> entityTypes = new ArrayList<String>();
                if (!args[0].equals("")) {
                    for (EntityType type : EntityType.values()) {
                        if (type.isAlive() && type.name().toLowerCase().startsWith(args[0].toLowerCase())) {
                            entityTypes.add(type.name());
                        }
                    }
                }
                else {
                    for (EntityType type : EntityType.values()) {
                        if (type.isAlive()) {
                            entityTypes.add(type.name());
                        }
                    }
                }
                return entityTypes;
            }
        }
        return null;
    }
}
