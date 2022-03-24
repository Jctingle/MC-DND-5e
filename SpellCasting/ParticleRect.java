package jeffersondev.SpellCasting;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.util.Vector;

import java.util.ArrayList;

/**
* Created by Connor on 12/3/2016.
*/
public class ParticleRect {

    ArrayList<ParticlePoint> sides = new ArrayList<ParticlePoint>();
    Location start;
    Particle importParticle;
    ParticleRect(Location start, double width, double length,double height, String particleString){
        this.start = start;
        this.importParticle = Particle.valueOf(particleString.toUpperCase());
        Vector A = new Vector(0,0,0);
        Vector B = new Vector(length,0,0);
        Vector C = new Vector(0,0,width);
        Vector D = new Vector(0,height,0);
        Vector E = new Vector(0,height,width);
        Vector F = new Vector(length,height,0);
        Vector G = new Vector(length,0,width);
        sides.add(new ParticlePoint(A,B));
        sides.add(new ParticlePoint(A,C));
        sides.add(new ParticlePoint(A,D));
        sides.add(new ParticlePoint(C,D));
        sides.add(new ParticlePoint(B,D));
        sides.add(new ParticlePoint(C,B));
        sides.add(new ParticlePoint(B,C));
        sides.add(new ParticlePoint(D,C));
        sides.add(new ParticlePoint(D,B));
        sides.add(new ParticlePoint(E,B));
        sides.add(new ParticlePoint(F,C));
        sides.add(new ParticlePoint(G,D));
    }

    public Vector getPosition(double blocksAway,Vector origin, Vector direction) {
        return origin.clone().add(direction.clone().normalize().multiply(blocksAway));
    }

    public ArrayList<Vector> traverse(Vector origin, Vector direction) {
        ArrayList<Vector> positions = new ArrayList<>();
        for (double d = 0; d <= direction.length(); d += 0.1) {
            positions.add(getPosition(d,origin,direction));
        }
        return positions;
    }

    public void draw(){
            for(ParticlePoint point : sides){
            for(Vector position : traverse(point.origin,point.direction)){
                position = start.toVector().clone().add(position);

                // p.getWorld().spawnParticle(Particle.REDSTONE, pSpot, 1, dustOptions);
                // start.getWorld().playEffect(position.toLocation(start.getWorld()), Effect.ELECTRIC_SPARK, 0);
                start.getWorld().spawnParticle(importParticle, position.toLocation(start.getWorld()), 0, 0, 0, 0, 0.05);
            }
        }
    }

    private class ParticlePoint {
        Vector origin,direction;
        ParticlePoint(Vector origin, Vector direction) {
            this.origin = origin;
            this.direction = direction;
        }
    }

}