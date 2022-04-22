package jeffersondev.SpellCasting;

import java.util.ArrayList;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import jeffersondev.App;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Spellcomponent implements CommandExecutor,Listener {
    private App app;
    public Spellcomponent(App app){
        this.app = app;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        Player p = (Player) sender;
        //args will be token, travel, onsite
        //SpellConstructor will be a GUI based class and will go step by step through the other two pieces.
        //travel only needs three, type, particle, travelsize.
        //oh I see a problem I guess lmao.
        String tokentype = args[0];
        switch (tokentype){
            case "token":
            //error checking here for args length and contents
                String tokenItem = args[1].toUpperCase();
                String spellName = args[2];
                ItemStack token = new ItemStack(Material.valueOf(tokenItem), 1);
                ItemMeta meta = token.getItemMeta();
                meta.setDisplayName(spellName);
                token.setItemMeta(meta);
                p.getInventory().addItem(token);
                break;
            case "travel":
            //error checking here for args length and contents
                String travelType = args[1];
                String particle = args[2];
                String travelSize = args[3];
                //build fields from args for variable readability
                ItemStack travelToken = new ItemStack(Material.PRISMARINE_SHARD, 1);
                ItemMeta travelmeta = travelToken.getItemMeta();
                travelmeta.setDisplayName(travelType);
                //build the lore components
                ArrayList<String> travelLore = new ArrayList<String>();
                travelLore.add(particle);
                travelLore.add(travelSize);
                travelmeta.setLore(travelLore);
                travelToken.setItemMeta(travelmeta);
                p.getInventory().addItem(travelToken);
                break;
            case "onsite":
            //lots of error checking here
                String onsiteEffect = args[1];
                String onsiteParticle = args[2];
                String onsiteShape = args[3];
                String onsiteSize = args[4];
                String onsiteHeight = args[5];
                String onsitePersist = args[6];                
                ItemStack onsiteToken = new ItemStack(Material.PRISMARINE_SHARD, 1);
                ItemMeta onsitemeta = onsiteToken.getItemMeta();
                onsitemeta.setDisplayName(onsiteEffect);
                ArrayList<String> onsiteLore = new ArrayList<String>();
                if (NumberUtils.isDigits(onsiteShape)){
                    onsiteLore.add(onsiteParticle);
                    onsiteLore.add(onsiteShape);
                    onsiteLore.add(onsiteSize);
                    onsiteLore.add(onsiteHeight);
                    onsiteLore.add(onsitePersist);
                    onsitemeta.setLore(onsiteLore);
                    onsiteToken.setItemMeta(onsitemeta);
                    p.getInventory().addItem(onsiteToken);
                }
                break;
        }



        return true;
    }
}