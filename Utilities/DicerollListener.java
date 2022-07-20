package jeffersondev.Utilities;

import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.*;
import jeffersondev.App;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;

public class DicerollListener {

    private final App plugin;

    public DicerollListener(App plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onBroadCastDiceRoll(DiscordGuildMessagePostProcessEvent e){
        String output = e.getProcessedMessage();
        if (output.contains("Initiative")){
            //string that represents the roll
            //string that represents the character name, which will reference back to the player via CORE
            int totalIndex = output.indexOf("**Total**:");
            String totalStringed = output.substring(totalIndex + 11);
            // String character = output.substring(totalIndex + 11);
            e.setCancelled(true);
            // Bukkit.broadcastMessage(ChatColor.DARK_RED + totalStringed);
            int initiativeRoll = Integer.valueOf(totalStringed);
            TextComponent hoverable = new TextComponent("Initiative Roll of: [" + totalStringed + "]");
            hoverable.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click here to use this roll in an active initiative round")));

            //future clickable event;
            
            // Bukkit.broadcastMessage(new BaseComponent[]{hoverable});
            for (Player p : Core.getCharacters().keySet()){
                if (output.contains(Core.getCharacters().get(p))){
                    hoverable.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/combat join " + Core.getCharacters().get(p) + " " + totalStringed));
                    p.spigot().sendMessage(hoverable);
                }
            }
        }
        else{
            int totalIndex = output.indexOf("beyond::");
            String ShortenStringed = output.substring(totalIndex + 8);
            e.setCancelled(true);
            Bukkit.broadcastMessage(ChatColor.DARK_RED + ShortenStringed);
            if (ShortenStringed.contains("**20**")){
                for (Player p : Core.getCharacters().keySet()){
                    if (output.contains(Core.getCharacters().get(p))){
                        Firework firework = p.getWorld().spawn(p.getLocation(), Firework.class);
                        FireworkMeta meta = (FireworkMeta) firework.getFireworkMeta();
                        meta.addEffect(FireworkEffect.builder().withColor(Color.RED).withColor(Color.WHITE).with(Type.BALL_LARGE).withFlicker().build());
                        meta.setPower(1);
                        firework.setFireworkMeta(meta);
                    }
                }
            }

        }

    }    

}
