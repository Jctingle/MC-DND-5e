package jeffersondev;
import org.bukkit.plugin.java.JavaPlugin;
public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        Borpa cum = new Borpa(this);
        getLogger().info("DND Tools Loaded");
        this.getCommand("lpointer").setExecutor(cum);
        getServer().getPluginManager().registerEvents(cum, this); 

        dmNotes notery = new dmNotes(this);
        this.getCommand("dnote").setExecutor(notery);
        getServer().getPluginManager().registerEvents(notery, this);

        initiative innit = new initiative(this);
        this.getCommand("init").setExecutor(innit);
        getServer().getPluginManager().registerEvents(innit, this); 

    }
    @Override
    public void onDisable() {
        getLogger().info("Unloading DND Tools");
    }
}