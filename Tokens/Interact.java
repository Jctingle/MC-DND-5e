package jeffersondev.Tokens;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import jeffersondev.App;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Interact implements CommandExecutor,Listener {
    private App app;
    public Interact(App app){
        this.app = app;
    }
    // private Mobicon mobicon;
    // public Interact(Mobicon mobicon){
    //     this.mobicon = mobicon;
    // }
    ArrayList<Player> deleteMob = new ArrayList<>();
    HashMap<Player, ArrayList<String>> actionVal = new HashMap<>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        //Could build a version of this that you do /interact x and then it prompts with a GUI -> I really like this

        Player p = ((Player) sender);
        if (args[0].toString().equals("delete")){
            deleteMob.add(p);
            return true;
        }
        else if (args[0].toString().equals("heal")){
            ArrayList<String> builder = new ArrayList<String>();
            builder.add(args[1]);
            builder.add("0");
            actionVal.put(p, builder);
            p.sendMessage("Right click to Heal for " + args[1]);
            return true;
        }
        else if (args[0].toString().equals("damage")){
            ArrayList<String> builder = new ArrayList<String>();
            builder.add(args[1]);
            builder.add("1");
            actionVal.put(p, builder);
            p.sendMessage("Right click to Damage for " + args[1]);
            return true;
        }
        else if (args[0].toString().equals("temphp")){
            ArrayList<String> builder = new ArrayList<String>();
            builder.add(args[1]);
            builder.add("2");
            actionVal.put(p, builder);
            p.sendMessage("Right click to change TempHP to " + args[1]);
            return true;
        }
        else {
            return false;
        }
    }
    public void adjustMob(LivingEntity victim, double modim, Integer modType){
        //check for bool, if true then damage, if false then heal, absorption hearts for tempHP? that actually works SO WELL
        double tempHealth = 0.0 + victim.getHealth();
        double maxHealth = victim.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        double adjValue = 0.0 + modim;
        double currentHealth = victim.getHealth();
        //if healing
        switch (modType){
            case 0:
                //healing
                //check for scoreboardtag Deathsaving, then remove because healing
                if(victim.getScoreboardTags().contains("deathSaving")){
                    tempHealth = 0.0 + modim;
                    victim.setHealth(tempHealth);
                    victim.removeScoreboardTag("deathSaving");
                }
                else{
                    tempHealth += modim;
                    if (tempHealth > maxHealth){
                        victim.setHealth(maxHealth);
                    }
                    else {
                        victim.setHealth(tempHealth);
                    }
                }
                break;
            case 1:
                //damage
                //check for tempHP first, and then apply damage split between by first removing all tempHP, then apply damage to base health
                //check for value, ability to drop to .5 is nice as it will represent death saving state
                if (victim.getAbsorptionAmount() > 0.0) {
                    adjValue =  adjValue - victim.getAbsorptionAmount();
                    if (adjValue > 0.0){
                        if (!checkForDeath(adjValue, currentHealth)){
                            tempHealth = tempHealth - adjValue;
                            victim.setAbsorptionAmount(0.0);
                            victim.setHealth(tempHealth);
                        }
                    }
                    else{
                        double holdValue = victim.getAbsorptionAmount();
                        victim.setAbsorptionAmount(holdValue-modim);
                    }
                }
                else if(!checkForDeath(adjValue, currentHealth)){
                    tempHealth -= adjValue;
                    victim.setHealth(tempHealth);
                }
                else if(checkForDeath(adjValue, currentHealth) && victim.getScoreboardTags().contains("PlayerCharacter")){
                    setDeathSave(victim);
                    victim.setHealth(1.0);
                }
                else if(checkForDeath(adjValue, currentHealth)){
                    deleteMob(victim);
                }
                break;
            case 2:
                if (victim.getAbsorptionAmount() < modim) {
                    victim.setAbsorptionAmount(modim);
                }
                break;
                //tempHP
        }
    }
    public boolean checkForDeath(double damage, double currentHp){
            if (currentHp < damage){
                return true;
            }
            else{
                return false;
            }
    }
    public void setDeathSave(LivingEntity playerToken){
            //going to addscoreboardtag, have to determine a way of marking it as a player tag
            playerToken.addScoreboardTag("deathSaving");
            playerToken.setHealth(1.0);
            //send global message that entity is in trouble!
            //command for fake killing a player token AKA putting in death saving mode
    }
    public void deleteMob(LivingEntity deceased){
        deceased.remove();
    }
    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent e1) {
        //contents can be checked for permission if the tags contain the players name, EASY
        if(deleteMob.contains(e1.getPlayer())){
            LivingEntity killMob = (LivingEntity) e1.getRightClicked();
            deleteMob(killMob);
            deleteMob.remove(e1.getPlayer());
        }
        else if(actionVal.containsKey(e1.getPlayer())){
            ArrayList<String> tempRead = new ArrayList<String>();
            tempRead = actionVal.get(e1.getPlayer());
            LivingEntity touched = (LivingEntity) e1.getRightClicked();

            adjustMob(touched, Double.parseDouble(tempRead.get(0)), Integer.parseInt(tempRead.get(1)));
            actionVal.remove(e1.getPlayer());
        }
        else{
            //need some way to control this, probably will use permissions
            Player p1 = (Player) e1.getPlayer();
            //will also need a case here to handle DM pulling info
                LivingEntity tempEnt = (LivingEntity) e1.getRightClicked();
                if (tempEnt.getScoreboardTags().contains("token")){
                    e1.setCancelled(true);
                    String tempName = tempEnt.getCustomName();
                    //have to change this to custom nbt or scoreboard tag
                    //scoreboard tags are easiest to check for YES or NO, slash basically checkboxes
                    // String tempAC = Double.toString(tempEnt.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).getValue());
                    String tempHealth =  "" + tempEnt.getHealth();
                    String tempTempHP = "" + tempEnt.getAbsorptionAmount();
                    String tempAC = new String();
                    // startsWith("ac:")
                    // Set<String> tempThing = tempEnt.getScoreboardTags();
                    HashMap<String, String> tempMap = new HashMap<String, String>();
                    for (String rip: tempEnt.getScoreboardTags()){
                        String[] ripped = rip.split(":");
                        if(ripped.length > 1){
                            tempMap.put(ripped[0], ripped[1]);
                        }
                    }
                    tempAC = tempMap.get("ac");
                    // for(String tag : tempEnt.getScoreboardTags()){
                    //     if (tag.startsWith("ac:")){
                    //         tempAC = tag.replace("ac:", "");
                    p1.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("" + tempName + " AC: " + tempAC + " CurrentHealth: " + tempHealth + " tempHP: " + tempTempHP));
                }
            }
        }
}
