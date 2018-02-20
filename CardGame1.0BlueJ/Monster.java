

public class Monster extends Card {
    private int attack;
    private int health;
    private int cost;
    private Type type;
    private int tookField;  //for the positions in battleField, so we can set the right position back to available after the monster is removed.
    private int attackedTimes = 0;
    private int maxAttackTime;
    private boolean froze = false;
    private int frozeTurn = 0;  //turns that it been froze, so we can unfroze in the right turn.

    //Constructor. String url is the url for the card Picture, and boolean player is to determine if it's a player card or enemy card.
    public Monster(String name,int cost,int attack,int health,Type type,String url,boolean player){
        super(name,false,url,player);
        this.cost = cost;
        this.attack = attack;
        this.health = health;
        this.type = type;
        maxAttackTime = 1;
        setText(getName());
    }

    public int getAttack(){
        return attack;
    }

    public int getHealth(){
        return health;
    }

    public int getCost(){
        return cost;
    }

    public Type getType(){
        return type;
    }

    public int getTookField(){
        return tookField;
    }

    public int getAttackedTimes(){return attackedTimes;}

    public int getMaxAttackTime(){return maxAttackTime;}

    public boolean getFroze(){
        return froze;
    }

    public int getFrozeTurn(){return frozeTurn;}

    //take damage for attackProcess and damage spells.
    public void takeDamge(int damage){
        health -= damage;
    }

    //check if the monster is dead.
    public boolean checkDead(){
        if(health < 1){
            return true;
        }
        return false;
    }

    //To make sure it only attack in a limited times.
    public boolean checkAttackChance(){
        if(attackedTimes < maxAttackTime){
            return true;
        }
        else{
            return false;
        }
    }

    //set it when it's summon, so we have control with the position it took.
    public void setTookField(int fieldNumber){
        tookField = fieldNumber;
    }

    public void changeAttack(int newAttack){
        attack = newAttack;
    }

    public void increaseAttack(int increaseAmount){
        attack += increaseAmount;
    }

    public void decreaseAttack(int decreaseAmount){
        if(attack - decreaseAmount >= 0){
            attack -= decreaseAmount;
        }

    }

    public void changeHealth(int amount){
        health = amount;
    }

    public void increaseHealth(int amount){
        health += amount;
    }

    //increase when it attacked
    public void attackedOnce(){
        attackedTimes++;
    }

    //use when the turn starts
    public void resetAttackTimes(){
        attackedTimes = 0;
    }

    public void freeze(){
        froze = true;
    }

    public void increaseFrozeTurn(){
        frozeTurn++;
    }

    public void unFroze(){
        froze = false;
        frozeTurn = 0;
    }

    public void setMaxAttackTime(int chances){
        maxAttackTime = chances;
    }

    //get the status for the display in battleField.
    public String getStatus(){
        return "<html>Name: " + getName() + "<br>ATK: " + getAttack() + "<br>HP: " + getHealth() + "<br>Cost: " + getCost() + "<br>Type: " + getType();
    }

    //This is useful when the enemy summon the monster, we no longer want it to have the card back as disabledIcon.
    public void switchDisableIcon(){
        setDisabledIcon(getOldDisabledIcon());
    }

}
