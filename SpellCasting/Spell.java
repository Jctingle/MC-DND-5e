package jeffersondev.SpellCasting;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class Spell {
     private String ITEM;
     private String TRAVELTYPE;
     private String TRAVELPARTICLE;
     private Double TRAVELSIZE;
     private String ONSITEEFFECT;
     private String ONSITEPARTICLE ;
     private String ONSITESHAPE;
     private Double ONSITESIZE;
     private Double ONSITEHEIGHT;
     private Boolean PERSISTENT;

     Spell(String item, String travelType, String travelParticle, Double travelSize, String onSiteEffect, String onSiteParticle, String onSiteShape, Double onSiteSize, Double onSiteHeight, Boolean persistent){
          this.ITEM = item;
          this.TRAVELTYPE = travelType;
          this.TRAVELPARTICLE = travelParticle;
          this.TRAVELSIZE = travelSize;

          this.ONSITEEFFECT = onSiteEffect;

          this.ONSITEPARTICLE = onSiteParticle;

          this.ONSITESHAPE = onSiteShape;

          this.ONSITESIZE = onSiteSize;

          this.ONSITEHEIGHT = onSiteHeight;

          this.PERSISTENT = persistent;
     }
     public ArrayList<String> ReturnLore(){
          ArrayList<String> onsiteLore = new ArrayList<String>();

          //build lore thing

          return onsiteLore;
     }

     //moveUp
     //moveDown
     //Attack Animation   swingMainHand()
     //rotate 90
     //mountOn/dismount
     //call equipment manager
}
