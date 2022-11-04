package jeffersondev.Utilities;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class InitiativeRound {
    private TreeMap<Integer, ArrayList<TurnHolder>> initBlock;
    public Boolean joinable = false;
    public Integer currentTurn;
    MultiTool toolbox = new MultiTool();
    ItemStack endTurnItem = toolbox.endTurn();
    HashMap<Player, Integer> multiTurnHandler = new HashMap<>();

    //Pass in and store the scoreboard manager
     InitiativeRound(){
      this.initBlock = new TreeMap<Integer, ArrayList<TurnHolder>>();
     }
     public void startJoinableRound(){
      joinable = true;
     }
     public void joinRound(TurnHolder joiner){
      final Objective objecteye = InitiativeCore.board.getObjective("initiative");
      if (joinable == true){
         //some stuff here to handle late joiners
         if(this.initBlock.keySet().contains(joiner.getRoll())){
            ArrayList<TurnHolder> cacheArray = this.initBlock.get(joiner.getRoll());
            cacheArray.add(joiner);
            this.initBlock.replace(joiner.getRoll(), cacheArray);
            Score score = objecteye.getScore(joiner.getName());
            score.setScore(joiner.getRoll());

         }
         else{
            ArrayList<TurnHolder> cacheArray = new ArrayList<TurnHolder>();
            cacheArray.add(joiner);
            this.initBlock.put(joiner.getRoll(), cacheArray);
            Score score = objecteye.getScore(joiner.getName());
            score.setScore(joiner.getRoll());
         }
      }

        //this will handle both timely and late joiners
        //just an if that can check the overall Core for an active game
     }
   public void cycleRound(){
      //sets the initblock to the "first value"
      currentTurn = this.initBlock.lastEntry().getKey();
   }
   public void nextKey(){
      if (this.initBlock.lowerEntry(currentTurn) == null){
         cycleRound();
      }
      else{
         currentTurn = this.initBlock.lowerEntry(currentTurn).getKey();
      }
      //do some null checking here, if null then try to pass cycleRound
   }
   public void purgeIndex(Integer Key){
      this.initBlock.remove(Key);
   }
   public void purgeIndexMember(Integer Key, TurnHolder killer){
      ArrayList<TurnHolder> cleaner = this.initBlock.get(Key);
      cleaner.remove(killer);
      if(cleaner.size() > 0){
         this.initBlock.replace(Key, cleaner);
      }
      else{
         purgeIndex(Key);
      }

   }
   public void currentTurnItemDelivery(){
      //this goes through every TurnHolder object within the same initiative Block;
      for(TurnHolder EntryMember: this.initBlock.get(currentTurn)){
         // if(!EntryMember.hasTakenTurn()){
            Player current = Bukkit.getPlayer(EntryMember.getOwner());
            if(!current.getInventory().contains(endTurnItem)){
               current.getInventory().addItem(endTurnItem);
            }
         // }
      }
   }
   public void endOfTurnCleanup(){
      multiTurnHandler.clear();
      for(TurnHolder EntryMember: this.initBlock.get(currentTurn)){
         EntryMember.setNormal();
      }
   }
   public void startOfTurnSetup(){
      if(this.initBlock.get(currentTurn).size() > 1){
         for(TurnHolder EntryMember: this.initBlock.get(currentTurn)){
            Player current = Bukkit.getPlayer(EntryMember.getOwner());
            current.sendTitle("Your Turn", "Please make your move.", 1, 20, 1);
            EntryMember.setActive();
            if (multiTurnHandler.containsKey(EntryMember.getOwner())){
               multiTurnHandler.replace(current, multiTurnHandler.get(EntryMember.getOwner()) + 1);
            }
            else{
               multiTurnHandler.put(current, 1);
            }
         }
      }
      //just a simple turn
      else{
         
         TurnHolder EntryMember = this.initBlock.get(currentTurn).get(0);
         Player current = Bukkit.getPlayer(EntryMember.getOwner());
         EntryMember.setActive();
         current.sendTitle("Your Turn", "Please make your move.", 1, 20, 1);
      }
      currentTurnItemDelivery();
   }
   public void tryKill(String name){
      
      for(TurnHolder pickMe : allCurrentTurns()){
         if(pickMe.getName().equals(name)){
            TurnHolder EntryMember = pickMe;
            Player current = Bukkit.getPlayer(EntryMember.getOwner());
            if(EntryMember.isActive() && this.initBlock.get(EntryMember.getRoll()).size() > 1){
               EntryMember.setNormal();
               EntryMember.setDead();
               purgeIndexMember(EntryMember.getRoll(),EntryMember);
               //no item collection here, what do I need to do:
               if(multiTurnHandler.get(EntryMember.getOwner()) > 1){
                  multiTurnHandler.put(current, multiTurnHandler.get(EntryMember.getOwner()) - 1);
                  //do nothing
               }
               else{
                  multiTurnHandler.remove(EntryMember.getOwner());
                  current.getInventory().remove(endTurnItem);
                  if(multiTurnHandler.size() == 0){
                     endOfTurnCleanup();
                     nextKey();
                     startOfTurnSetup();
                  }
               }
            }
            //if name is just currently active
            else if(EntryMember.isActive() && this.initBlock.get(EntryMember.getRoll()).size() == 1){
               current.getInventory().remove(endTurnItem);
               purgeIndexMember(EntryMember.getRoll(),EntryMember);
               EntryMember.setNormal();
               EntryMember.setDead();
               nextKey();
               startOfTurnSetup();
            }
            //if name is just some a normal guy
            else{
               purgeIndexMember(EntryMember.getRoll(),EntryMember);
               EntryMember.setDead();
            }
         }
      }
      //if name is currently active and in a multi-turn

   }
   public void startFirstRound(){
      cycleRound();
      startOfTurnSetup();
   }
   //THIS IS THE BIG BOY LOGIC HANDLER
   public void rightClickBridge(Player tagger){
      //check if the multiple turn flag has been activated for this current round
      if(multiTurnHandler.size() > 0){
         multiTurnHandler.put(tagger, multiTurnHandler.get(tagger) - 1);
         if(multiTurnHandler.values().contains(0) && multiTurnHandler.get(tagger) == 0){
            multiTurnHandler.remove(tagger);
            tagger.getInventory().remove(endTurnItem);
            if(multiTurnHandler.size() > 0){
               //do nothing
            }
            else{
               endOfTurnCleanup();
               nextKey();
               startOfTurnSetup();
            }
         }
      }
      else{
         tagger.getInventory().remove(endTurnItem);
         endOfTurnCleanup();
         nextKey();
         startOfTurnSetup();
      }
   }
   public ArrayList<TurnHolder> allCurrentTurns(){
      ArrayList<TurnHolder> everyOne = new ArrayList<TurnHolder>();
      for(ArrayList<TurnHolder> values : this.initBlock.values()){
         everyOne.addAll(values);
      }
      return everyOne;
   }
   public ArrayList<String> returnAllNames(){
      ArrayList<String> names = new ArrayList<String>();
      for(TurnHolder turnA : allCurrentTurns()){
         names.add(turnA.getName());
      }
      return names;
   }
}
