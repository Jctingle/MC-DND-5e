package jeffersondev.Utilities;


import org.bukkit.entity.Player;

public class TurnHolder {
    private Player TURNOWNER;
    private int INITIATIVE;
    private String TURNNAME;
    private Boolean isActive = false;
    private Boolean isDead = false;
    //Pass in and store the scoreboard manager
     TurnHolder(Player turnowner, int initiative, String turnname){
         
     }
     public void setActive(){
        isActive = true;
        //bla bla bla add turnname to active team
     }
     public void setDead(){
        isDead = true;
     }
     public void setNormal(){
        isActive = false;
        //E.G. End of turn/next turn.
     }
}
