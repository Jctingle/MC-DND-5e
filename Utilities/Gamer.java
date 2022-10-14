package jeffersondev.Utilities;


import java.io.Serializable;

import org.bukkit.entity.Player;

public class Gamer implements java.io.Serializable{
    private Player GAMER;
    private String CHARACTERNAME;
    //Pass in and store the scoreboard manager
     Gamer(Player gamer, String characterName){
         this.GAMER = gamer;
         this.CHARACTERNAME = characterName;
     }
     public String getCharacter(){
      return this.CHARACTERNAME;
     }
     public Player getGamer(){
      return this.GAMER;
     }
}
