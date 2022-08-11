package jeffersondev.Tokens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import org.bukkit.Bukkit;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import jeffersondev.App;
import jeffersondev.Utilities.MultiTool;


public class MobMoverJCT{
    private App app;
    public MobMoverJCT(App app){
        this.app = app;
    }
    MultiTool toolBox = new MultiTool();
    static Map<Player, Moveable> movingMob = new HashMap<Player, Moveable>();
    public static void deRegister(Player player){
        movingMob.remove(player);;
    }
    public ItemStack matchItem(){
        ItemStack pointerItem = toolBox.mobMover();
        return pointerItem;
    }
    // public static void onRightClickBody(Player p, LivingEntity clickedMob) {
    //             if(p.isSneaking()){
    //                 if(clickedMob.getScoreboardTags().contains("token")){
    //                     Moveable mover = new Moveable(clickedMob, clickedMob.getLocation(), false);
    //                     movingMob.put(p, mover);
    //                     p.sendMessage(clickedMob.getName() + " Selected, you can now right click a destination, or shift left click to open the menu");
    //                 }
    //             }
    // }
    public static void RTXEntitySelect(Player p) {
        Location eyeLoc = p.getEyeLocation();
        World world = p.getWorld();
        Double maxDistance = 100.0;
        RayTraceResult rtxResult = world.rayTraceEntities(eyeLoc, eyeLoc.getDirection(), maxDistance, 0.1, (entity) -> (entity.getScoreboardTags().contains("token")));
        LivingEntity clickedMob = (LivingEntity) rtxResult.getHitEntity();
        if(rtxResult != null){
            Moveable mover = new Moveable(clickedMob, clickedMob.getLocation(), false);
            movingMob.put(p, mover);
            p.sendMessage(clickedMob.getName() + " Selected, you can now right click a destination, or shift left click to open the menu");
        }
    }
    public static Moveable returnMovingMob(Player p){
        Moveable returner = movingMob.get(p);
        return returner;
    }
    public static void fireMovement(Player e1) {
            Player p = e1;
            if(movingMob.containsKey(p)) {
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
                            Location destination = rtxResult.getHitPosition().toLocation(world);
                            Vector targetDirection = p.getLocation().getDirection();
                            destination.setDirection(targetDirection);
                            destination.setPitch(0);
                            movingMob.get(p).movingMobReturn().teleport(destination);
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
            //3rd step
            //RTX between point A and B, ifcanpath.
            //Or just teleport
        }
    public static void menu(Player p){
            if(movingMob.containsKey(p)) {
                if(p.isSneaking()){
                    p.openInventory(remoteGui(p));
                }
                else if(p.getOpenInventory().getTitle().equals("Mover Remote")){
                    p.openInventory(remoteGui(p));
                }
            }
    }
    public static Inventory remoteGui(Player invoker){
        Inventory inv = Bukkit.createInventory(null, InventoryType.BARREL, "Mover Remote");
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
        twoMeta.setDisplayName("Change Rotation +");
        slotTwo.setItemMeta(twoMeta); 
        inv.setItem(1, slotTwo);
        //button 3
        ItemStack slotThree = new ItemStack(Material.SPYGLASS);
        ItemMeta threeMeta = slotThree.getItemMeta();
        ArrayList<String> lookAway = new ArrayList<String>();
        lookAway.add("Rotates the mob to face away from the player");
        threeMeta.setDisplayName("Change Rotation -");
        slotThree.setItemMeta(threeMeta); 
        inv.setItem(2, slotThree);
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
        if(movingMob.get(invoker).isAgeable()){
            ItemStack slotNine = new ItemStack(Material.EGG);
            ItemMeta nineMeta = slotNine.getItemMeta();
            nineMeta.setDisplayName("Toggle Age State");
            slotNine.setItemMeta(nineMeta); 
            inv.setItem(8, slotNine);
        }
        ItemStack slotTen = new ItemStack(Material.GLASS);
        ItemMeta tenMeta = slotTen.getItemMeta();
        tenMeta.setDisplayName("Toggle Visiblity");
        slotTen.setItemMeta(tenMeta);
        inv.setItem(9, slotTen);
        //What options do I want in the remote control.
        //Move up
        //move down
        //toggle TP/Path
        //attack animation
        //mount
        return inv;
    }
}
