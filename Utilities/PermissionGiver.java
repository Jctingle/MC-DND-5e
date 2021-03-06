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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
                players.addEntry(p.getName());
                p.setScoreboard(board);
            }
            else if(!p.isOp() && args[0].equals("player") && !activePlayers.contains(p)){
                //add to team with everyone else maybe
                players.addEntry(p.getName());
                p.setGameMode(GameMode.ADVENTURE);
                p.setAllowFlight(true);
                p.setFlying(true);
                PotionEffect invisoBill = new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0, false, false, false);
                p.addPotionEffect(invisoBill);
                p.setScoreboard(board);
            }
            else if(!p.isOp() && args[0].equals("leave") && activePlayers.contains(p)){
                players.removeEntry(p.getName());
                p.removePotionEffect(PotionEffectType.INVISIBILITY);
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
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
                if(activePlayers.contains(e.getPlayer())){
                        //Now use the method Player#removePotionEffect to remove the potion effect. This method accepts a PotionEffectType, so we need to get the type of the effect variable, and then remove it:
                        e.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
                        players.removeEntry(e.getPlayer().getName());
                }
        }
}

