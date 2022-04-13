package jeffersondev.Tokens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;

import jeffersondev.App;


public class MobMoverJCT implements CommandExecutor,Listener {
    private App app;
    public MobMoverJCT(App app){
        this.app = app;
    }
    ArrayList<Player> activeMovers = new ArrayList<>();
    Map<Player, LivingEntity> movingMob = new HashMap<Player, LivingEntity>();
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = ((Player) sender);
        if(!activeMovers.contains(p)){
            activeMovers.add(p);
            ItemStack remote = matchItem();
            p.getInventory().addItem(remote);
        }
        else{
            activeMovers.remove(p);
            ItemStack remote = matchItem();
            p.getInventory().removeItem(remote);
        }
        return true;
    }
    public ItemStack matchItem(){
        ItemStack pointerItem = new ItemStack(Material.TOTEM_OF_UNDYING);
        ItemMeta pointerMeta = pointerItem.getItemMeta();
        pointerMeta.setDisplayName("Mover");
        pointerItem.setItemMeta(pointerMeta); 

        return pointerItem;
    }
    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent e1) {
        Player p = e1.getPlayer();
        if(activeMovers.contains(p) && p.getInventory().getItemInMainHand().equals(matchItem())){
            LivingEntity clickedMob = (LivingEntity) e1.getRightClicked();
            if(clickedMob.getScoreboardTags().contains("token")){
                movingMob.put(p, clickedMob);
                p.sendMessage(clickedMob.getName() + " Selected");
            }
            //if sneaking open menu to do shit
            //menu options
            //up 1 block
            //down one block
            //activate Pathing mode vs activating whatever mode, the method that returns an Inventory will have conditionals for the construction, maybe make a pre-defined remote object?
        }
    }
    @EventHandler
    public void onRightClick(PlayerInteractEvent e1) {
        if (e1.getAction() == Action.RIGHT_CLICK_AIR || e1.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = e1.getPlayer();
            if(movingMob.containsKey(p) && p.getInventory().getItemInMainHand().equals(matchItem())) {
                if(p.isSneaking()){
                    //open GUI
                }
                else{
                    //mode dependant
                    Location eyeLoc = p.getEyeLocation();
                    World world = p.getWorld();
                    RayTraceResult rtxResult = world.rayTraceBlocks(eyeLoc, eyeLoc.getDirection(), 10, FluidCollisionMode.NEVER, true);
                    movingMob.get(p).teleport(rtxResult.getHitPosition().toLocation(world));
                }
            } 
            //3rd step
            //RTX between point A and B, ifcanpath.
            //Or just teleport
        }
    }
    //eventhandler for itemdrop if item is matchitem then remove player from immediate activelist
    //GUI Code, remote control in a 3x3?
    //new crafting-menu? IDK what else the 3x3 interface is a part of.
    //check the different interface types ore the different inventories
}
