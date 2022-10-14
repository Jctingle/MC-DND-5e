package jeffersondev.Utilities;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.RayTraceResult;
import jeffersondev.App;

//rename this stupid file lmao, fix errors and make it more stable generally
public class LaserPointer{   
    private App app;
    public LaserPointer(App app){
        this.app = app;
    }
    // ArrayList<Player> activeUsers = new ArrayList<>();
    MultiTool toolbox = new MultiTool();
    public static void handleRightClick(Player p) {
                DustOptions dustOptions = new DustOptions(Color.RED, 1.0F);
                RayTraceResult rtx = p.getWorld().rayTraceBlocks(p.getEyeLocation(), p.getEyeLocation().getDirection(), 100);
                if (rtx != null){
                    Location pSpot = rtx.getHitPosition().toLocation(p.getWorld());
                    p.getWorld().spawnParticle(Particle.REDSTONE, pSpot, 1, dustOptions); 
                }
            
        
    }
    public static void handleRightClickEntity(Player p, LivingEntity entity) {
                LivingEntity glower = entity;
                PotionEffect targetGlow = new PotionEffect(PotionEffectType.GLOWING, 50, 1, false, false, false);
                glower.addPotionEffect(targetGlow);
            
    }

}
