package jeffersondev.SpellCasting;

import java.io.Serializable;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public class Spell implements Serializable{
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
     Spell(String item, String travelType, ArrayList<String> travelLore, String onSiteEffect, ArrayList<String> onSiteLore){
          this.ITEM = item;
          this.TRAVELTYPE = travelType;
          this.TRAVELPARTICLE = travelLore.get(0);
          this.TRAVELSIZE = Double.parseDouble(travelLore.get(1));

          this.ONSITEEFFECT = onSiteEffect;

          this.ONSITEPARTICLE = onSiteLore.get(0);

          this.ONSITESHAPE = onSiteLore.get(1);

          this.ONSITESIZE = Double.parseDouble(onSiteLore.get(2));

          this.ONSITEHEIGHT = Double.parseDouble(onSiteLore.get(3));

          this.PERSISTENT = Boolean.parseBoolean(onSiteLore.get(4));
     }
     public ArrayList<String> ReturnTravelLore(){
          ArrayList<String> travelLore = new ArrayList<String>();
          travelLore.add(TRAVELPARTICLE);
          travelLore.add(TRAVELSIZE.toString());
          //build lore thing

          return travelLore;
     }
     public ArrayList<String> ReturnOnSiteLore(){
          ArrayList<String> onsiteLore = new ArrayList<String>();
          onsiteLore.add(ONSITEPARTICLE); //particle
          onsiteLore.add(ONSITESHAPE); //shape
          onsiteLore.add(ONSITESIZE.toString()); //size
          onsiteLore.add(ONSITEHEIGHT.toString()); //height
          onsiteLore.add(PERSISTENT.toString()); //persistant

          //build lore thing

          return onsiteLore;
     }
     public String ITEM(){
          return ITEM;
     }
     public String TRAVELTYPE(){
          return TRAVELTYPE;
     }
     public String TRAVELPARTICLE(){
          return TRAVELPARTICLE.toUpperCase();
     }
     public Double TRAVELSIZE(){
          return TRAVELSIZE;
     }
     public String ONSITEEFFECT(){
          return ONSITEEFFECT;
     }
     public String ONSITEPARTICLE(){
          return ONSITEPARTICLE.toUpperCase();
     }
     public String ONSITESHAPE(){
          return ONSITESHAPE;
     }
     public Double ONSITESIZE(){
          return ONSITESIZE;
     }
     public Double ONSITEHEIGHT(){
          return ONSITEHEIGHT;
     }
     public Boolean PERSISTENT(){
          return PERSISTENT;
     }
     //moveUp
     //moveDown
     //Attack Animation   swingMainHand()
     //rotate 90
     //mountOn/dismount
     //call equipment manager
}
