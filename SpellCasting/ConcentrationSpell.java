package jeffersondev.SpellCasting;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitRunnable;
public final class ConcentrationSpell extends BukkitRunnable{


    private ParticleSphere PARTICLESPHERE;
    private ParticleRect PARTICLERECT;
    private ParticleCyl PARTICLECYL;
    private String SHAPE;

    public ConcentrationSpell(Location center, String particle, Double parameters, String shape) {
        this.SHAPE = shape;
        switch(SHAPE){
            case "sphere":
                this.PARTICLESPHERE = new ParticleSphere(center,particle,parameters);
            break;
            case "cube":
                this.PARTICLERECT = new ParticleRect(center, parameters, parameters, parameters, particle);
            break;
        }
    }
    public ConcentrationSpell(Location center, String particle, Double parameters, String shape, Double cylHeight) {
        this.SHAPE = shape;
        //Probably change this to an IF for security;
        switch(SHAPE){
            case "cylinder":
                this.PARTICLECYL = new ParticleCyl(center, particle, parameters, cylHeight);
            break;
        }
    }
    @Override
    public void run() {
        switch(SHAPE){
            case "sphere":
                PARTICLESPHERE.draw();
            break;
            case "cube":
                PARTICLERECT.draw();
            break;
            case "cylinder":
                PARTICLECYL.draw();
            break;
        }
        
    }

    
}
