package jeffersondev.Utilities;

import java.util.ArrayList;

import org.bukkit.Location;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;

import jeffersondev.App;

public class AnimationRail implements CommandExecutor,Listener {   
    private App app;
    public AnimationRail(App app){
        this.app = app;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(sender instanceof Player){
            Player p = ((Player) sender);
            return true;
        }
        else{
            System.out.println("Cannot execute this command on the command line");
            return false;
            }
    }
    //begin cuboid selection
    //save cuboid/cuboid contents
    //run fallingblock animation, at same time apply similair vector (IFFY on this)
    //paste in saved Cuboid
}
