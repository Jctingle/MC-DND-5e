package jeffersondev.SpellCasting;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import java.util.ArrayList;


public class ParticleSphere {

    private Location ORIGIN;
    private String PARTICLE;
    ParticleSphere(Location origin, String particle){
        this.ORIGIN = origin;
        this.PARTICLE = particle;
    }
    public void draw(){
        Particle importParticle = Particle.valueOf(PARTICLE.toUpperCase());
        for (double i = 0; i <= Math.PI; i += Math.PI / 10) {
            double radius = Math.sin(i);
            double y = Math.cos(i);
            for (double a = 0; a < Math.PI * 2; a+= Math.PI / 10) {
            double x = Math.cos(a) * radius;
            double z = Math.sin(a) * radius;
            ORIGIN.add(x, y, z);
            // display particle at 'location'.
            ORIGIN.getWorld().spawnParticle(importParticle, ORIGIN, 0, 0, 0, 0, 0.05);

            ORIGIN.subtract(x, y, z);
            }
        }
    }

}