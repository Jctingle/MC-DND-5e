package jeffersondev.SpellCasting;

import org.bukkit.Location;
import org.bukkit.Particle;


public class ParticleCyl {

    private Location ORIGIN;
    private String PARTICLE;
    private Double SIZEFACTOR;
    private Double HEIGHTFACTOR;
    ParticleCyl(Location origin, String particle, double sizeFactor, double heightFactor){
        this.ORIGIN = origin;
        this.PARTICLE = particle;
        this.SIZEFACTOR = sizeFactor;
        this.HEIGHTFACTOR = heightFactor;

    }
    public void draw(){
        Particle importParticle = Particle.valueOf(PARTICLE.toUpperCase());
        int scaleX = (1 * SIZEFACTOR.intValue());  // use these to tune the size of your circle
        //I'm kind of nervous that this is for a vertical circle, but i'm dedicated to learning this one myself so I can start memeing
            int scaleZ = (1 * SIZEFACTOR.intValue());
            double density = 0.1;  // smaller numbers make the particles denser
            double densityY = HEIGHTFACTOR/10;
        for(Double y=0.0; y < HEIGHTFACTOR; y += densityY){
            for (double i=0; i < 2 * Math.PI ; i +=density) {
                double x = Math.cos(i) * scaleX;
                double z = Math.sin(i) * scaleZ;
                ORIGIN.add(x, y, z);
                ORIGIN.getWorld().spawnParticle(importParticle, ORIGIN, 0, 0, 0, 0, 0.05);
                ORIGIN.subtract(x, y, z);
                // spawn your particle here
            }
        }
    }

}