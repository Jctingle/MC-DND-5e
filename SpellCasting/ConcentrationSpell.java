package jeffersondev.SpellCasting;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitRunnable;
public final class ConcentrationSpell extends BukkitRunnable{

    private final Location CENTER;
    private final String PARTICLE;
    private final Double PARAMETERS;
    private ParticleRect PARTICLERECT;

    public ConcentrationSpell(Location center, String particle, Double parameters) {
        this.CENTER = center;
        this.PARTICLE = particle;
        this.PARAMETERS = parameters;
        this.PARTICLERECT = new ParticleRect(center, parameters, parameters, parameters);
    }
    @Override
    public void run() {
        PARTICLERECT.draw(PARTICLE);
    }

    
}
