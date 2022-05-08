package jeffersondev.Utilities;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import jeffersondev.App;



public class PermissionGiver implements CommandExecutor,Listener {   
    private App app;
    public PermissionGiver(App app){
        this.app = app;
    }
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Team players = board.registerNewTeam("players");
        ArrayList<Player> activePlayers = new ArrayList<>();
        @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){
            Player p = ((Player) sender);
            if(p.isOp() && args[0].equals("dm") && !activePlayers.contains(p)){
                //well I guess if they are already Opped they don't need it lmao.
                if (!players.canSeeFriendlyInvisibles()){
                    teamFlag();
                }
                players.addEntry(p.getName());
                // p.addScoreboardTag("dm");
            }
            else if(!p.isOp() && args[0].equals("player") && !activePlayers.contains(p)){
                //add to team with everyone else maybe
                if (!players.canSeeFriendlyInvisibles()){
                    teamFlag();
                }
                players.addEntry(p.getName());
                p.setAllowFlight(true);
                p.setFlying(true);
                p.setGameMode(GameMode.ADVENTURE);
                p.setInvisible(true);
            }
            return true;
        }
        else{
            System.out.println("Cannot execute this command on the command line");
            return false;
            }
    }
    public void teamFlag(){
        players.setCanSeeFriendlyInvisibles(true);
        return;
    }
}
