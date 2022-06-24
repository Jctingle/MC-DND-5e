package jeffersondev;
import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import github.scarsz.discordsrv.DiscordSRV;
import jeffersondev.SpellCasting.Grimoire;
import jeffersondev.SpellCasting.Spellcaster;
import jeffersondev.SpellCasting.Spellcomponent;
import jeffersondev.Tokens.Equipmentmanager;
import jeffersondev.Tokens.Interact;
import jeffersondev.Tokens.MobMoverJCT;
import jeffersondev.Tokens.Mobicon;
import jeffersondev.Utilities.Core;
import jeffersondev.Utilities.DicerollListener;
import jeffersondev.Utilities.LaserPointer;
import jeffersondev.Utilities.Ruler;
import jeffersondev.Utilities.dmNotes;
import jeffersondev.Utilities.initiative;
public class App extends JavaPlugin {
    private DicerollListener dicerollListener = new DicerollListener(this);
    
    @Override
    public void onEnable() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("DMTools");
        File f = new File(plugin.getDataFolder() + "/");
        DiscordSRV.api.subscribe(dicerollListener);
        if(!f.exists()){
            f.mkdir();
        }
        LaserPointer pointer = new LaserPointer(this);
        getLogger().info("DND Tools Loaded");
        this.getCommand("lpointer").setExecutor(pointer);
        getServer().getPluginManager().registerEvents(pointer, this); 

        dmNotes notery = new dmNotes(this);
        this.getCommand("dnote").setExecutor(notery);
        getServer().getPluginManager().registerEvents(notery, this);

        initiative innit = new initiative(this);
        this.getCommand("init").setExecutor(innit);
        getServer().getPluginManager().registerEvents(innit, this);
        
        Mobicon mobi = new Mobicon(this);
        this.getCommand("mobi").setExecutor(mobi);
        getServer().getPluginManager().registerEvents(mobi, this); 

        Interact inti = new Interact(this);
        this.getCommand("interact").setExecutor(inti);
        getServer().getPluginManager().registerEvents(inti, this); 

        Equipmentmanager equip = new Equipmentmanager(this);
        this.getCommand("equip").setExecutor(equip);
        getServer().getPluginManager().registerEvents(equip, this); 

        Grimoire grimoire = new Grimoire(this);
        this.getCommand("grimoire").setExecutor(grimoire);
        getServer().getPluginManager().registerEvents(grimoire, this); 

        Spellcomponent spellcomp = new Spellcomponent(this);
        this.getCommand("spellcomponent").setExecutor(spellcomp);
        getServer().getPluginManager().registerEvents(spellcomp, this); 

        Spellcaster spellcast = new Spellcaster(this);
        this.getCommand("spellbook").setExecutor(spellcast);
        getServer().getPluginManager().registerEvents(spellcast, this); 

        Ruler ruler = new Ruler(this);
        this.getCommand("ruler").setExecutor(ruler);
        getServer().getPluginManager().registerEvents(ruler, this); 

        MobMoverJCT mobmover = new MobMoverJCT(this);
        this.getCommand("mobmover").setExecutor(mobmover);
        getServer().getPluginManager().registerEvents(mobmover, this); 

        Core systemCore = new Core(this);
        this.getCommand("game").setExecutor(systemCore);
        getServer().getPluginManager().registerEvents(systemCore, this); 

    }
    @Override
    public void onDisable() {
        DiscordSRV.api.unsubscribe(dicerollListener);
        getLogger().info("Unloading DND Tools");
    }
}