package jeffersondev.SpellCasting;

import org.bukkit.Location;
import org.bukkit.Particle;


public class ParticleCyl {

    private Location ORIGIN;
    private String PARTICLE;
    private Double SIZEFACTOR;
    ParticleCyl(Location origin, String particle, double sizeFactor){
        this.ORIGIN = origin;
        this.PARTICLE = particle;
        this.SIZEFACTOR = sizeFactor;

    }
    public void draw(){
        Particle importParticle = Particle.valueOf(PARTICLE.toUpperCase());
        int scaleX = 1;  // use these to tune the size of your circle
            int scaleY = 1;
            double density = 0.1;  // smaller numbers make the particles denser

            for (double i=0; i < 2 * Math.PI ; i +=density) {
                double x = Math.cos(i) * scaleX;
                double y = Math.sin(i) * scaleY;
                ORIGIN.add(x, y, 0);
                ORIGIN.getWorld().spawnParticle(importParticle, ORIGIN, 0, 0, 0, 0, 0.05);
                ORIGIN.subtract(x, y, 0);
                // spawn your particle here
            }
        
    }

}