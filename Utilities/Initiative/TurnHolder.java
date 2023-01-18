package jeffersondev.Utilities.Initiative;


import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class TurnHolder {
    private UUID TURNOWNER;
    private int INITIATIVE;
    private String TURNNAME;
    private Boolean isActive = false;
    private Boolean isDead = false;
    private Boolean isTaken = false;
    //Pass in and store the scoreboard manager
     TurnHolder(UUID turnowner, int initiative, String turnname){
         this.TURNOWNER = turnowner;
         this.INITIATIVE = initiative;
         this.TURNNAME = turnname;
     }
     public void setActive(){
        this.isActive = true;
        Team activeUnit = InitiativeCore.board.getTeam("active");
        activeUnit.addEntry(this.TURNNAME);
        //bla bla bla add turnname to active team
     }
     public void setDead(){
         this.isDead = true;
         Team activeUnit = InitiativeCore.board.getTeam("dead");
         activeUnit.addEntry(this.TURNNAME);
        //no need to undo this right?
     }
     public void setNormal(){
        Team activeUnit = InitiativeCore.board.getTeam("active");
         activeUnit.removeEntry(this.TURNNAME);
         this.isActive = false;
        //E.G. End of turn/next turn.
     }
     public void setTakenTurn(Boolean setTaken){
      this.isTaken = setTaken;
     }
     public Boolean hasTakenTurn(){
      return this.isTaken;
     }
     public Boolean isActive(){
      return this.isActive;
     }
     public Boolean isDead(){
        return this.isDead;
       }
     public UUID getOwner(){
      return this.TURNOWNER;
     }
     public int getRoll(){
      return this.INITIATIVE;
     }
     public String getName(){
      return this.TURNNAME;
     }
}
