package jeffersondev.SpellCasting;

import org.bukkit.Location;
import org.bukkit.Particle;


public class ParticleSphere {

    private Location ORIGIN;
    private String PARTICLE;
    private Double SIZEFACTOR;
    ParticleSphere(Location origin, String particle, double sizeFactor){
        this.ORIGIN = origin;
        this.PARTICLE = particle;
        this.SIZEFACTOR = sizeFactor;

    }
    public void draw(){
        Particle importParticle = Particle.valueOf(PARTICLE.toUpperCase());
        for (double i = 0; i <= Math.PI; i += Math.PI / 10) {
            //I will need a converter her to take Feet and represent them in game as 1/3 of a block
            //something like x/3, default is radius = 1 block so as long as i'm basing it off of radius
            double radius = Math.sin(i) * SIZEFACTOR;
            double y = Math.cos(i) * SIZEFACTOR;
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