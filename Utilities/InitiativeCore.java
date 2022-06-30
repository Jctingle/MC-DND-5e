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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import jeffersondev.App;

public class InitiativeCore implements CommandExecutor,Listener {   
    private App app;
    public InitiativeCore(App app){
        this.app = app;
    }
    static Boolean activeRound = false;
    static ArrayList<InitiativeRound> activeRounds = new ArrayList<InitiativeRound>();
    MultiTool toolbox = new MultiTool();
    ItemStack endTurnItem = toolbox.endTurn();
    static ScoreboardManager manager = Bukkit.getScoreboardManager();
    final static Scoreboard board = manager.getNewScoreboard();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){
            //player joins a commencing round, or an existing round
            if (args[0].equals("join")){
                if(activeRound  == true){
                    Player p = ((Player) sender);
                    //do join code, make it same action regardless of ifstarted
                    //collect args[1,2]
                    if (!activeRounds.get(0).returnAllNames().contains(args[1])){                   
                        TurnHolder joiner = new TurnHolder(p, Integer.parseInt(args[2]), args[1]);
                        activeRounds.get(0).joinRound(joiner);
                        p.sendMessage("Thank you for joining a round: " + args[1] + " with an initiative of: " + args[2]);
                    }
                    else{
                        p.sendMessage("Error, duplicate entry!");
                    }
                    return true;
                }
                else{
                    Player p = ((Player) sender);
                    p.sendMessage("Please wait for a round to be initialized");
                    return true;
                }
            }
            //new round creation
            else if(args[0].equals("new")){
                if(activeRound  == true){
                    Player p = ((Player) sender);
                    p.sendMessage("There is currently another round in progress");
                    
                    return true;
                }
                else{
                    //create a new round object;
                    initiateScoreBoard();
                    InitiativeRound newRound = new InitiativeRound();
                    activeRounds.add(newRound);
                    activeRound = true;
                    activeRounds.get(0).startJoinableRound();
                    Bukkit.broadcastMessage(ChatColor.GOLD + "New combat round initiated, please roll to join!");
                    return true;
                }
            }
            //kills a player in an active round, checking their current turn condition
            else if(args[0].equals("kill")){
                //set a character status to dead, if current turn & hasnottaken turn, set dead and run end-turn loop cycle
                activeRounds.get(0).tryKill(args[1]);
                return true;
            }
            //purge the overall round, should be nice and just clear the one core
            else if(args[0].equals("end")){
                //run other end code here
                clearScoreBoard();
                //aka PURGE END TURN ITEM FROM EVERYONE;
                for(Player p : Core.getCharacters().keySet()){
                    toolbox.purgePlayerOfSingleTools(p, endTurnItem);
                    p.setScoreboard(manager.getNewScoreboard());
                }
                //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@scoreboard stuff here@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
                activeRounds.remove(0);
                activeRound = false;
                return true;
            }
            //starts a round
            else if(args[0].equals("commence")){
                for(Player p : Core.getCharacters().keySet()){
                    p.setScoreboard(board);
                    activeRounds.get(0).startFirstRound();
                }
                return true;
            }
            //commence
            //end
            //kill
            else{
                return true;
                }
        }
        else{
            System.out.println("Cannot execute this command on the command line");
                return false;
            }
    }
    public static void HandleRightClick(Player flagger){
        activeRounds.get(0).rightClickBridge(flagger);
    }
    public void initiateScoreBoard(){
        final Objective objecteye = board.registerNewObjective("initiative", "dummy", "initiative", RenderType.INTEGER);
        objecteye.setDisplaySlot(DisplaySlot.SIDEBAR);

        Team activeUnit = board.registerNewTeam("active");
        activeUnit.setColor(ChatColor.GREEN);
        Team deadUnit = board.registerNewTeam("dead");
        deadUnit.setColor(ChatColor.BLACK);
    }
    public void clearScoreBoard(){
        final Objective objecteye = board.getObjective("initiative");
        objecteye.unregister();
        Team activeUnit = board.getTeam("active");
        Team deadUnit = board.getTeam("dead");
        activeUnit.unregister();
        deadUnit.unregister();
    }
}   
