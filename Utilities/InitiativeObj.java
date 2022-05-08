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

import jeffersondev.App;

public class InitiativeObj implements CommandExecutor,Listener {   
    private App app;
    public InitiativeObj(App app){
        this.app = app;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        return true;
    }
    @EventHandler
    public void onRightClick(PlayerInteractEvent event){
    }
}
