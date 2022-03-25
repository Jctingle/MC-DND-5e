package jeffersondev.SpellCasting;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;


public class ParticleCone {

    private Location ORIGIN;
    private String PARTICLE;
    private Double SIZEFACTOR;
    ParticleCone(Location origin, String particle, Vector direction, double sizeFactor){
        this.ORIGIN = origin;
        this.PARTICLE = particle;
        this.SIZEFACTOR = sizeFactor;

    }
    public ArrayList<Vector> getCircle(Vector origin, Vector direction, float distance, float radius, int numPoints) {
        ArrayList<Vector> points = new ArrayList();

        Vector circleCenter = origin.clone().add(direction.clone().multiply(distance));
        Vector perp = direction.clone().setY(0).normalize().rotateAroundY(Math.PI / 2);

        for (int i = 0; i < numPoints; i++) {
            points.add(circleCenter.clone().add(perp.clone().multiply(radius).rotateAroundNonUnitAxis(direction, i * 2 * Math.PI / numPoints)));
        }
        return points;
    }
    public void draw(){
        Particle importParticle = Particle.valueOf(PARTICLE.toUpperCase());
        // int scaleX = (1 * SIZEFACTOR.intValue());  // use these to tune the size of your circle
        // //I'm kind of nervous that this is for a vertical circle, but i'm dedicated to learning this one myself so I can start memeing
        //     int scaleZ = (1 * SIZEFACTOR.intValue());
        //     double density = 0.1;  // smaller numbers make the particles denser
        //     for (double i=0; i < 2 * Math.PI ; i +=density) {
        //         double x = Math.cos(i) * scaleX;
        //         double y = Math.sin(i) * scaleZ;
        //         ORIGIN.add(x, y, 0);
        //         ORIGIN.getWorld().spawnParticle(importParticle, ORIGIN, 0, 0, 0, 0, 0.05);
        //         ORIGIN.subtract(x, y, 0);
        //         // spawn your particle here
        //     }
        
    }

}