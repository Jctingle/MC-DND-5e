package jeffersondev;
import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("DMTools");
        File f = new File(plugin.getDataFolder() + "/");
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
    }
    @Override
    public void onDisable() {
        getLogger().info("Unloading DND Tools");
    }
}