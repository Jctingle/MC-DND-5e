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
    ArrayList<Player> tokenPlacer = new ArrayList<>();
    ArrayList<Player> dungeonMaster = new ArrayList<>();
    String mobType = new String();
    Double health = (double) 10;
    String mobName = new String();
    double AC = (double) 0.0;
    Map<Player, ArrayList<String>> unitSpawner = new HashMap<>();
    boolean hasType = false;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){
            Player p = ((Player) sender).getPlayer();
            if (!tokenPlacer.contains(p)) {
                tokenPlacer.add(p);
                //need if's or situation proofers
                    p.sendMessage("Creating Token" + args[1]);
                    mobType = args[1].toString();
                    mobName = args[2].toString();
                    health = Double.parseDouble(args[3]);
                    AC = Double.parseDouble(args[4]);
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
                    //change this to summon the armor stand
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

                    //find some way to set health and track it, as well as AC, and interact with health
                    }
                else{
                    p.sendMessage("Please select solid ground");
                }
            }
            //right click on the specific mob, and gets their info
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
                    //check if current health is .5 AKA in death saving state
                    //check for maxhealth value
                    tempHealth = tempHealth += modim;
                case 1:
                    //damage
                    //check for tempHP first, and then apply damage split between by first removing all tempHP, then apply damage to base health
                    //check for value, ability to drop to .5 is nice as it will represent death saving state
                    tempHealth = tempHealth -= modim;
                case 2:
                    //tempHP
                    //get absorption level, then check if absorb/thp greater than damage double, if greater than do nothing else set value to new temp HP
            }
    }
    public void deleteMob(LivingEntity deceased, Player owner){
            //delete mob from existence
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
