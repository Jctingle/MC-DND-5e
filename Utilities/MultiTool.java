package jeffersondev.Utilities;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MultiTool {
  private ArrayList<ItemStack> allItems = new ArrayList<ItemStack>();
  public List<String> loreTag = new ArrayList<String>(){{add("5ETool");}};
     public MultiTool(){
      allItems.add(this.laserPointer()); 
      allItems.add(this.mobMover()); 
      allItems.add(this.grimoire()); 
      allItems.add(this.ruler()); 
      allItems.add(this.endTurn());
     }
     public ItemStack laserPointer(){
      ItemStack pointerItem = new ItemStack(Material.AMETHYST_SHARD);
      ItemMeta pointerMeta = pointerItem.getItemMeta();
      pointerMeta.setDisplayName("Laser Pointer");
      pointerMeta.setLore(loreTag);
      pointerItem.setItemMeta(pointerMeta); 

      return pointerItem;
     }
     public ItemStack mobMover(){
      ItemStack pointerItem = new ItemStack(Material.TOTEM_OF_UNDYING);
      ItemMeta pointerMeta = pointerItem.getItemMeta();
      pointerMeta.setDisplayName("Mover");
      pointerMeta.setLore(loreTag);
      pointerItem.setItemMeta(pointerMeta); 

      return pointerItem;
     }
     public ItemStack grimoire(){
      ItemStack pointerItem = new ItemStack(Material.ENCHANTED_BOOK);
      ItemMeta pointerMeta = pointerItem.getItemMeta();
      pointerMeta.setDisplayName("Grimoire");
      pointerMeta.setLore(loreTag);
      pointerItem.setItemMeta(pointerMeta); 

      return pointerItem;
     }
     public ItemStack ruler(){
      ItemStack pointerItem = new ItemStack(Material.NAME_TAG);
      ItemMeta pointerMeta = pointerItem.getItemMeta();
      pointerMeta.setDisplayName("Ruler");
      pointerMeta.setLore(loreTag);
      pointerItem.setItemMeta(pointerMeta); 
      return pointerItem;
     }
     public ItemStack endTurn(){
      ItemStack item = new ItemStack(Material.EMERALD, 1);
      ItemMeta meta = item.getItemMeta();
      meta.setDisplayName("End Turn");
      meta.setLore(loreTag);
      item.setItemMeta(meta);
      return item;
     }
    public void purgePlayerOfAllTools(Player player){
        for (ItemStack plucker : allItems){
              player.getInventory().remove(plucker);

        }
      }
     public void purgePlayerOfSingleTools(Player player, ItemStack plucker){
      if (player.getInventory().contains(plucker)){
        player.getInventory().remove(plucker);
      }
   }
   //example use MultiTool.purgePlayerOfSingleTools(Player, MultiTool.ruler());
}
