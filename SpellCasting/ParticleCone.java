package jeffersondev.SpellCasting;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;


public class ParticleCone {

    private Location ORIGIN;
    private String PARTICLE;
    private Double SIZEFACTOR;
    private Vector DIRECTION;
    ParticleCone(Location origin, String particle, Vector direction, double sizeFactor){
        this.ORIGIN = origin;
        this.PARTICLE = particle;
        this.SIZEFACTOR = sizeFactor;
        this.DIRECTION = direction;

    }
    public ArrayList<Vector> getCircle(Vector origin, Vector direction, double distance, double radius, int numPoints) {
        ArrayList<Vector> points = new ArrayList<Vector>();

        Vector circleCenter = origin.clone().add(direction.clone().multiply(distance));
        Vector perp = direction.clone().setY(0).normalize().rotateAroundY(Math.PI / 2);

        for (int i = 0; i < numPoints; i++) {
            points.add(circleCenter.clone().add(perp.clone().multiply(radius).rotateAroundNonUnitAxis(direction, i * 2 * Math.PI / numPoints)));
        }
        return points;
    }
    public void draw(){
        Particle importParticle = Particle.valueOf(PARTICLE.toUpperCase());
        ORIGIN.getWorld().spawnParticle(importParticle, ORIGIN, 0, 0, 0, 0, 0.05);
        for(Integer i=1;i<11;i++){
            double loopFactor = SIZEFACTOR/10 * i;
            ArrayList<Vector> points = getCircle(ORIGIN.toVector(), DIRECTION,loopFactor,loopFactor/2,16);
            for(Vector point : points){
                ORIGIN.getWorld().spawnParticle(importParticle, point.toLocation(ORIGIN.getWorld()), 0, 0, 0, 0, 0.05);
            }
    }
    }
}