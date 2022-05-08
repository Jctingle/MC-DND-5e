package jeffersondev.SpellCasting;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;


public class ParticleOrb extends BukkitRunnable{

    private Location ORIGIN;
    private String PARTICLE;
    private Location TARGET;
    private double COUNTER;
    private Long DELAY;
    ParticleOrb(Location origin, String particle, Location target){
        this.ORIGIN = origin;
        this.PARTICLE = particle;
        this.TARGET = target;
        this.COUNTER = 1.0;
        Double tempDelay = ((ORIGIN.distance(TARGET)) * 2.0) * 20;
        this.DELAY = (tempDelay).longValue();
    }
    public void draw(){
        Particle importParticle = Particle.valueOf(PARTICLE);
        double pointsPerLine = (ORIGIN.distance(TARGET)) * 2.0;
        double d = ORIGIN.distance(TARGET) / pointsPerLine;
        Location l = ORIGIN.clone();
        Vector direction = TARGET.toVector().subtract(ORIGIN.toVector()).normalize();
        if (pointsPerLine == COUNTER){
            this.cancel();
        }
        Vector v = direction.multiply(COUNTER * d);
        l.add(v);
        ORIGIN.getWorld().spawnParticle(importParticle, l, 0, 0, 0, 0, 0.05);
        COUNTER++;
    }
    public long delayCalculator(){
        return DELAY;
    }
    @Override
    public void run() {
        draw();
    }
    // }




































































































}