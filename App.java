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
import jeffersondev.Utilities.LaserPointer;
import jeffersondev.Utilities.Ruler;
import jeffersondev.Utilities.dmNotes;
import jeffersondev.Utilities.Discord_Bridge.DicerollListener;
import jeffersondev.Utilities.Initiative.InitiativeCore;
import jeffersondev.Utilities.Item_Management.ToolBox;
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
        getLogger().info("DND Tools Loaded");
        dmNotes notery = new dmNotes(this);
        this.getCommand("dnote").setExecutor(notery);
        getServer().getPluginManager().registerEvents(notery, this);
        
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

        Core systemCore = new Core(this);
        this.getCommand("game").setExecutor(systemCore);
        getServer().getPluginManager().registerEvents(systemCore, this); 

        InitiativeCore initCore = new InitiativeCore(this);
        this.getCommand("combat").setExecutor(initCore);
        getServer().getPluginManager().registerEvents(initCore, this); 

        ToolBox toolCore = new ToolBox(this);
        this.getCommand("tools").setExecutor(toolCore);
        getServer().getPluginManager().registerEvents(toolCore, this); 

    }
    @Override
    public void onDisable() {
        DiscordSRV.api.unsubscribe(dicerollListener);
        getLogger().info("Unloading DND Tools");
    }
}