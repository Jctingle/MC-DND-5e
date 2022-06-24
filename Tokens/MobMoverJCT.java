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
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;

import jeffersondev.App;
import jeffersondev.Utilities.MultiTool;


public class MobMoverJCT implements CommandExecutor,Listener {
    private App app;
    public MobMoverJCT(App app){
        this.app = app;
    }
    MultiTool toolBox = new MultiTool();
    static ArrayList<Player> activeMovers = new ArrayList<>();
    static Map<Player, Moveable> movingMob = new HashMap<Player, Moveable>();
    public static void deRegister(Player player){
        movingMob.remove(player);
        activeMovers.remove(player);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = ((Player) sender);
        if(!activeMovers.contains(p)){
            activeMovers.add(p);
            p.getInventory().addItem(toolBox.mobMover());
            p.sendMessage("Please Shift click a token to select");
        }
        else{
            activeMovers.remove(p);
            ItemStack remote = matchItem();
            p.getInventory().removeItem(remote);
        }
        return true;
    }
    public ItemStack matchItem(){
        ItemStack pointerItem = toolBox.mobMover();
        return pointerItem;
    }
    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent e1) {
        Player p = e1.getPlayer();
            if(activeMovers.contains(p) && p.getInventory().getItemInMainHand().equals(matchItem()) && e1.getHand().equals(EquipmentSlot.HAND)){
                if(p.isSneaking()){
                    e1.setCancelled(true);
                    LivingEntity clickedMob = (LivingEntity) e1.getRightClicked();
                    if(clickedMob.getScoreboardTags().contains("token")){
                        Moveable mover = new Moveable(clickedMob, clickedMob.getLocation(), false);
                        movingMob.put(p, mover);
                        p.sendMessage(clickedMob.getName() + " Selected, you can now right click a destination, or shift left click to open the menu");
                        //create new moveable object, store inside hashmap, reference object and helper methods
                    }
                //if sneaking open menu to do shit
                //menu options
                //up 1 block
                //down one block
                //activate Pathing mode vs activating whatever mode, the method that returns an Inventory will have conditionals for the construction, maybe make a pre-defined remote object?
            }
        }
    }
    public static void fireMovement(Player e1) {
            Player p = e1;
            if(movingMob.containsKey(p)) {
                if(p.isSneaking()){

                }
                else{
                    //mode dependant
                    if (movingMob.get(p).isCursor() == true){
                        Location eyeLoc = p.getEyeLocation();
                        World world = p.getWorld();
                        RayTraceResult rtxResult = world.rayTraceBlocks(eyeLoc, eyeLoc.getDirection(), 3, FluidCollisionMode.NEVER, true);
                        if (rtxResult != null){
                            movingMob.get(p).movingMobReturn().teleport(rtxResult.getHitPosition().toLocation(world));
                        }
                        else{
                            Location rangedDistance = eyeLoc.add(eyeLoc.getDirection().multiply(3));
                            movingMob.get(p).movingMobReturn().teleport(rangedDistance);
                        }
                    }
                    else if (movingMob.get(p).isPath() == false){
                        Location eyeLoc = p.getEyeLocation();
                        World world = p.getWorld();
                        RayTraceResult rtxResult = world.rayTraceBlocks(eyeLoc, eyeLoc.getDirection(), 35, FluidCollisionMode.NEVER, true);
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
                            //I see
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
        public void menu(Player p){
            if(movingMob.containsKey(p)) {
                if(p.isSneaking()){
                    p.openInventory(remoteGui(p));
                }
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
        ItemStack slotTwo = new ItemStack(Material.COMPASS);
        ItemMeta twoMeta = slotTwo.getItemMeta();
        ArrayList<String> lookMe = new ArrayList<String>();
        lookMe.add("Rotates the mob to face the player");
        twoMeta.setDisplayName("Change Rotation");
        slotTwo.setItemMeta(twoMeta); 
        inv.setItem(1, slotTwo);
        //button 3
        //button 4
        ItemStack slotFour = new ItemStack(Material.RED_CONCRETE);
        ItemMeta fourMeta = slotFour.getItemMeta();
        fourMeta.setDisplayName("Move one block down");
        slotFour.setItemMeta(fourMeta); 
        inv.setItem(3, slotFour);
        //button 5
        if (!movingMob.get(invoker).isCursor()){
            ItemStack slotFive = new ItemStack(Material.CHORUS_FRUIT);
            ItemMeta fiveMeta = slotFive.getItemMeta();
            ArrayList<String> movementType = new ArrayList<String>();
            movementType.add("Current: Long Range");
            fiveMeta.setDisplayName("Toggle Target Mode");
            fiveMeta.setLore(movementType);
            slotFive.setItemMeta(fiveMeta);
            inv.setItem(4, slotFive);
        }
        else{
            ItemStack slotFive = new ItemStack(Material.CHORUS_FLOWER);
            ItemMeta fiveMeta = slotFive.getItemMeta();
            ArrayList<String> movementType = new ArrayList<String>();
            movementType.add("Current: Short Range");
            fiveMeta.setDisplayName("Toggle Target Mode");
            fiveMeta.setLore(movementType);
            slotFive.setItemMeta(fiveMeta);
            inv.setItem(4, slotFive);
        }
        //button 6
        ItemStack slotSix = new ItemStack(Material.IRON_SWORD);
        ItemMeta sixMeta = slotSix.getItemMeta();
        sixMeta.setDisplayName("Swing Main Arm");
        slotSix.setItemMeta(sixMeta); 
        inv.setItem(5, slotSix);
        //button 7
        if(movingMob.get(invoker).isSlime()){
            ItemStack slotSeven = new ItemStack(Material.LEATHER);
            ItemMeta sevenMeta = slotSeven.getItemMeta();
            sevenMeta.setDisplayName("Increase size by 1");
            slotSeven.setItemMeta(sevenMeta); 
            inv.setItem(6, slotSeven);
        }
        //button 8
        if(movingMob.get(invoker).isSlime()){
            ItemStack slotEight = new ItemStack(Material.RABBIT_HIDE);
            ItemMeta eightMeta = slotEight.getItemMeta();
            eightMeta.setDisplayName("Decrease size by 1");
            slotEight.setItemMeta(eightMeta); 
            inv.setItem(7, slotEight);
            
        }
        //button 9
        //button 9 will be option to do group move, will have it's own collection
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
            Integer buttonClicked = e.getRawSlot();
            e.setCancelled(true);
            Player p = Bukkit.getPlayer(e.getWhoClicked().getName());
            // p.sendMessage("click registered");
            // p.sendMessage(buttonClicked.toString());
            switch (buttonClicked){
                case 0:
                    movingMob.get(p).moveTo();
                    break;
                case 1:
                    movingMob.get(p).faceMe(p);
                    p.sendMessage("It should have rotated");
                    break;
                case 2:
                    break;
                case 3:
                    movingMob.get(p).moveToB();
                    break;

                case 4:
                if (movingMob.get(p).isCursor()){
                    movingMob.get(p).cursorBool();
                    p.openInventory(remoteGui(p));
                    break;

                }
                else{
                    movingMob.get(p).cursorBool();
                    p.openInventory(remoteGui(p));
                    break;
                }
                case 5:
                    movingMob.get(p).swingArm();
                    break;
                case 6:
                        movingMob.get(p).sizeUp();
                    break;
                case 7:
                    movingMob.get(p).sizeDown();
                    break;
                case 8:
                    break;
                default:
                    p.sendMessage("Don't even worry, nothing dangerous happened");
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
            activeMovers.remove(e.getPlayer());
            e.setCancelled(true);
        }
    }
    //playerquit event too
    //GUI Code, remote control in a 3x3?
    //new crafting-menu? IDK what else the 3x3 interface is a part of.
    //check the different interface types ore the different inventories
}
