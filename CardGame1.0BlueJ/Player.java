public class Player {
    private final static int MAX_HEALTH = 25;
    private int health = 25;
    private final static int MAX_MANA = 10;
    private int totalMana;
    private int mana;
    private boolean alive = true;
    private int outDrawDamage = 1;

    public Player(){
        totalMana = 0;
        mana = totalMana;
    }

    public int getMaxHealth(){return MAX_HEALTH;}

    public int getHealth()
    {
        return health;
    }

    public int getMaxMana(){return MAX_MANA;}

    public int getMana()
    {
        return mana;
    }

    public boolean useMana(int cost){
        if(mana - cost >= 0){
            mana -= cost;
            return true;
        }
        return false;
    }

    public void resetMana(){mana = totalMana;}

    public void setMana(int amount)
    {
        mana += amount;
    }

    public int getTotalMana()
    {
        return totalMana;
    }

    public void increaseTotalMana(){
        if(totalMana < MAX_MANA){
            totalMana += 1;
        }
    }

    public void takeDamage(int incomingAttack)
    {
        health -= incomingAttack;
    }

    public int getOutDrawDamage(){
        return outDrawDamage;
    }

    public void increaseOutDrawDamage(){
        outDrawDamage++;
    }

    public void heal(int incomingHeal){
        for(int i = incomingHeal; ((i > 0) && (health < MAX_HEALTH)); i--)
        {
            health++;
        }
    }

    public boolean isDead(){
        if(health <= 0) {
            alive = false;
            return true;
        }
        return false;
    }

    public void changeMana(int manaCost)
    {
        mana -= manaCost;
    }

    public void reset(){
        health = MAX_HEALTH;
        totalMana = 0;
        mana = 0;
        outDrawDamage = 1;
    }
}
