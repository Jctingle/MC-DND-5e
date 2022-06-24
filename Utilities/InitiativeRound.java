package jeffersondev.Utilities;


import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.entity.Player;

public class InitiativeRound {
    private HashMap<TurnHolder, Integer> playersHM = new HashMap<>();
    private Integer currentTurnIndex = Integer.MAX_VALUE;
    private TurnHolder currentTurner;
    //Pass in and store the scoreboard manager
     InitiativeRound(){
         
     }
     public void startJoinableRound(){

     }
     public void joinRound(TurnHolder joiner){
        //this will handle both timely and late joiners
     }
     public void whoseTurnNext(){
        //will be changed from void to a TurnHolder slash Player return value
     }
     public void startRound(){

     }
     public void nextTurn(){

     }
}
