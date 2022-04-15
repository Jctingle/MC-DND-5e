package jeffersondev.Tokens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
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
    Map<Player, Moveable> movingMob = new HashMap<Player, Moveable>();
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
                Moveable mover = new Moveable(clickedMob, clickedMob.getLocation(), false);
                movingMob.put(p, mover);
                p.sendMessage(clickedMob.getName() + " Selected");
                //create new moveable object, store inside hashmap, reference object and helper methods
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
                    e1.setCancelled(true);
                    p.openInventory(remoteGui(p));
                }
                else{
                    //mode dependant
                    if (movingMob.get(p).isPath() == true){
                        Location eyeLoc = p.getEyeLocation();
                        World world = p.getWorld();
                        RayTraceResult rtxResult = world.rayTraceBlocks(eyeLoc, eyeLoc.getDirection(), 10, FluidCollisionMode.NEVER, true);
                        if (rtxResult != null){
                            movingMob.get(p).movingMobReturn().teleport(rtxResult.getHitPosition().toLocation(world));
                        }
                    }
                    else {
                        Location eyeLoc = p.getEyeLocation();
                        World world = p.getWorld();
                        RayTraceResult rtxResult = world.rayTraceBlocks(eyeLoc, eyeLoc.getDirection(), 10, FluidCollisionMode.NEVER, true);
                        // movingMob.get(p).movingMobReturn().teleport(rtxResult.getHitPosition().toLocation(world));
                        if (rtxResult != null){
                            org.bukkit.util.Vector pathVector = rtxResult.getHitPosition().toLocation(world).toVector().subtract(eyeLoc.toVector()).normalize();
                            movingMob.get(p).movingMobReturn().setVelocity(movingMob.get(p).movingMobReturn().getVelocity().add(pathVector));
                        }

                    }
                }
            } 
            //3rd step
            //RTX between point A and B, ifcanpath.
            //Or just teleport
        }
    }
    public Inventory remoteGui(Player invoker){
        Inventory inv = Bukkit.createInventory(null, InventoryType.DISPENSER, "Mover Remote");
        //button 1
        ItemStack slotOne = new ItemStack(Material.LIME_CONCRETE);
        ItemMeta oneMeta = slotOne.getItemMeta();
        oneMeta.setDisplayName("Move one block up");
        slotOne.setItemMeta(oneMeta); 
        inv.setItem(0, slotOne);
        //button 2
        //button 3
        if (movingMob.get(invoker).isPath()){
            ItemStack metaInsert = new ItemStack(Material.NETHER_STAR);
            ItemMeta threeMeta = metaInsert.getItemMeta();
            ArrayList<String> movementType = new ArrayList<String>();
            movementType.add("Teleport");
            threeMeta.setDisplayName("Toggle Pathing Mode");
            threeMeta.setLore(movementType);
            metaInsert.setItemMeta(threeMeta);
            inv.setItem(2, metaInsert);
        }
        else{
            ItemStack metaInsert = new ItemStack(Material.LEATHER_BOOTS);
            ItemMeta threeMeta = metaInsert.getItemMeta();
            ArrayList<String> movementType = new ArrayList<String>();
            movementType.add("Path");
            threeMeta.setDisplayName("Toggle Pathing Mode");
            threeMeta.setLore(movementType);
            metaInsert.setItemMeta(threeMeta);
            inv.setItem(2, metaInsert);
        }
        //button 4
        ItemStack slotFour = new ItemStack(Material.RED_CONCRETE);
        ItemMeta fourMeta = slotFour.getItemMeta();
        fourMeta.setDisplayName("Move one block down");
        slotFour.setItemMeta(fourMeta); 
        inv.setItem(3, slotFour);
        //button 5
        //button 6
        ItemStack slotSix = new ItemStack(Material.IRON_SWORD);
        ItemMeta sixMeta = slotSix.getItemMeta();
        sixMeta.setDisplayName("Swing Main Arm");
        slotSix.setItemMeta(sixMeta); 
        inv.setItem(5, slotSix);
        //What options do I want in the remote control.
        //Move up
        //move down
        //toggle TP/Path
        //attack animation
        //mount
        return inv;
    }
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getView().getTitle().equals("Mover Remote")) return;
        else{
            int buttonClicked = e.getRawSlot();
            e.setCancelled(true);
            Player p = Bukkit.getPlayer(e.getWhoClicked().getName());
            // p.sendMessage("click registered");
            switch (buttonClicked){
                case 0:
                    movingMob.get(p).moveTo(1);
                case 1:
                case 2:
                    if (movingMob.get(p).isPath()){
                        movingMob.get(p).pathBool();
                        p.openInventory(remoteGui(p));
                    }
                    else{
                        movingMob.get(p).pathBool();
                        p.openInventory(remoteGui(p));
                    }
                case 3:
                    movingMob.get(p).moveToB(1);
                case 4:
                case 5:
                    movingMob.get(p).swingArm();
            }
        }
    }
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (!e.getView().getTitle().equals("Mover Remote")) return;
        else{
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onItemDrop(final PlayerDropItemEvent e) {
        if (e.getItemDrop() != matchItem()) return;
        else{
            // e.setCancelled(true);
        }
    }
    //playerquit event too
    //GUI Code, remote control in a 3x3?
    //new crafting-menu? IDK what else the 3x3 interface is a part of.
    //check the different interface types ore the different inventories
}
