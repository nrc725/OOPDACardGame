import java.util.ArrayList;

public class Spell extends Card{
    //Attributes of spells, and fields that help us get more information of the spell.
    private int changeAmount;
    private String effect;
    private String effectType;
    private String description;
    private String targetingType;
    private boolean instant;
    private String log;
    private String simpleEffect;
    private String simpleEffected;

    //Constructor.
    public Spell(String name, int changeAmount,String effect,String effectType,String targetingType,boolean instant,String url,boolean player,String description){
        super(name,true,url,player);
        this.changeAmount = changeAmount;
        this.effect = effect;
        this.effectType = effectType;
        this.targetingType = targetingType;
        this.instant = instant;
        this.description = description;
        log = "";
        setText(getName());
    }

    //Gets the changeAmount for spells that has one.
    public int getChangeAmount(){
        return changeAmount;
    }

    public String getEffect(){
        return effect;
    }

    public String getEffectType(){
        return effectType;
    }

    public String getDescription(){
        return description;
    }

    public String getTargetingType(){
        return targetingType;
    }

    //Very important methods that gets the "effect" field of the spell, which every field has one, and it tells the
    // sort of effect it has.
    public void useEffect(Monster x){

        //Let the spell determine which effect it has, and should be using.
        //By adding more spells in the future, just have to add a new method for that spell, and add a case here.
        switch(effect){
            case "heal":
                heal(x);
                break;
            case "changeHealth":
                changeHealth(x);
                break;
            case "damage":
                damage(x);
                break;
            case "buffAttack":
                buffAttack(x);
                break;
            case "debuffAttack":
                debuffAttack(x);
                break;
            case "changeAttack":
                changeAttack(x);
                break;
            case "freeze":
                freeze(x);
                break;
            case "destroy":
                destroy(x);
                break;
            case "moreAttackChance":
                moreAttackChance(x);
                break;
            case"flipFlop":
                flipFlop(x);
                break;
            case"both":
                increaseBoth(x);
                break;
        }
    }

    //This is an overload method for AOE spells that cast on one side of the field.
    public void useEffect(ArrayList<Monster> xs){
        switch(effect){
            case "heal":
                healAOE(xs);
                break;
            case "changeHealth":
                changeHealthAOE(xs);
                break;
            case "damage":
                damageAOE(xs);
                break;
            case "buffAttack":
                buffAttackAOE(xs);
                break;
            case "debuffAttack":
                debuffAttackAOE(xs);
                break;
            case "changeAttack":
                changeAttackAOE(xs);
                break;
            case "freeze":
                freezeAOE(xs);
                break;
            case "destroy":
                destoryAOE(xs);
                break;
        }
    }

    //Another overload method for spells that will effect both sides of the field.
    public void useEffect(ArrayList<Monster> xs,ArrayList<Monster> ys){
        if(effect.equalsIgnoreCase("destroyAll")){
            destoryAll(xs,ys);
        }
    }


    //Methods that actually do the effect jobs.

    public void heal(Monster x){
        //simpleEffected is for later setDetails() method, so it decide which String it will add to the log.
        simpleEffected = "hp";
        //simpleEffect is just for the log to have the right information.
        simpleEffect = "+" + changeAmount + " hp";
        //this is where we call the method in Monster's class.
        x.increaseHealth(changeAmount);
        //set the log, so we can display the string in our battleLog JLabel during the battle.
        setDetails(x);
    }

    public void changeHealth(Monster x){
        simpleEffected = "hp";
        simpleEffect = "hp = " + changeAmount;
        x.changeHealth(changeAmount);
        setDetails(x);
    }

    public void increaseBoth(Monster x){
        simpleEffected = "both";
        simpleEffect = "hp+" + changeAmount + ",atk+" + changeAmount;
        x.increaseHealth(changeAmount);
        x.increaseAttack(changeAmount);
        setDetails(x);
    }

    public void damage(Monster x){
        simpleEffected = "hp";
        simpleEffect = "-" + changeAmount + " hp";
        x.takeDamge(changeAmount);
        setDetails(x);
    }

    public void buffAttack(Monster x){
        simpleEffected = "atk";
        simpleEffect = "+" + changeAmount + " atk";
        x.increaseAttack(changeAmount);
        setDetails(x);
    }

    public void debuffAttack(Monster x){
        simpleEffected = "atk";
        simpleEffect = "-" + changeAmount + " atk";
        x.decreaseAttack(changeAmount);
        setDetails(x);
    }

    public void changeAttack(Monster x){
        simpleEffected = "atk";
        simpleEffect = "atk= " + changeAmount;
        x.changeAttack(changeAmount);
        setDetails(x);
    }

    public void flipFlop(Monster x){
        simpleEffected = "flip";
        simpleEffect = "hp<-->atk";
        int newAttack = x.getHealth();
        x.changeHealth(x.getAttack());
        x.changeAttack(newAttack);
        setDetails(x);
    }

    public void freeze(Monster x){
        simpleEffected = "freeze";
        simpleEffect = "freeze 1T";
        x.freeze();
        setDetails(x);
    }

    public void destroy(Monster x){
        simpleEffected = "destroy";
        simpleEffect = "DESTROY";
        x.changeHealth(0);
        setDetails(x);
    }

    public void moreAttackChance(Monster x){
        simpleEffected = "moreChance";
        simpleEffect = "atkChance +" + changeAmount;
        x.setMaxAttackTime(changeAmount);
        setDetails(x);
    }


    //AOE methods basically call the same method from the single target ones to every element that the parameter Arraylist got.
    public void healAOE(ArrayList<Monster> xs){
        for(Monster x : xs){
            heal(x);
        }
    }

    public void changeHealthAOE(ArrayList<Monster> xs){
        for(Monster x : xs){
            changeHealth(x);
        }
    }

    public void damageAOE(ArrayList<Monster> xs){
        for(Monster x : xs){
            damage(x);
        }
    }

    public void buffAttackAOE(ArrayList<Monster> xs){
        for(Monster x : xs){
            buffAttack(x);
        }
    }

    public void debuffAttackAOE(ArrayList<Monster> xs){
        for(Monster x : xs){
            debuffAttack(x);
        }
    }

    public void changeAttackAOE(ArrayList<Monster> xs){
        for(Monster x : xs){
            changeAttack(x);
        }
    }

    public void freezeAOE(ArrayList<Monster> xs){
        for(Monster x : xs){
            freeze(x);
        }
    }

    public void destoryAOE(ArrayList<Monster> xs){
        for(Monster x : xs){
            destroy(x);
        }
    }

    public void destoryAll(ArrayList<Monster> xs,ArrayList<Monster> ys){
        for(Monster x : xs){
            destroy(x);
        }
        for(Monster y : ys){
            destroy(y);
        }
    }

    //Give the information of the Spell.
    public String getStatus(){
        return "<html>Name: " + getName() + "<br>" + getEffectType() + "<br>Effect: " + getDescription() + "<html>";
    }

    //So we know if the spell cast right away when we click "cast", or it will need more action for the spell to process.
    public boolean getInstant(){
        return instant;
    }

    //method to set the log, so we get to know what spell was used and what it did to the target.
    public void setDetails(Monster target){
        switch(simpleEffected){
            case"hp":
                log += " used " + getName() + "(" + simpleEffect + ") on " + target.getName() + "<br>" + target.getName() + " now have " + target.getHealth() + " hp<br>";
                break;
            case"atk":
                log += " used " + getName() + "(" + simpleEffect + ") on " + target.getName() + "<br>" + target.getName() + " now have " + target.getHealth() + " atk<br>";
                break;
            case"destroy":
                log += " used " + getName() + "(" + simpleEffect + ") on " + target.getName() + "<br>" + target.getName() + " got destroyed<br>";
                break;
            case"freeze":
                log += " used " + getName() + "(" + simpleEffect + ") on " + target.getName() + "<br>" + target.getName() + " can't attack for one turn<br>";
                break;
            case"flip":
                log += " used " + getName() + "(" + simpleEffect + ") on " + target.getName() + "<br>" + target.getName() + " hp: " + target.getHealth() + " atk: " + target.getAttack() + "<br>";
                break;
            case"atkChance":
                log += " used " + getName() + "(" + simpleEffect + ") on " + target.getName() + "<br>" + target.getName() + " can attack " + changeAmount + " times per Turn<br>";
                break;
            case"both":
                log += " used " + getName() + "(" + simpleEffect + ") on " + target.getName() + "<br>" + target.getName() + " now have " + target.getHealth() + " hp, " + target.getAttack() + " atk<br>";
                break;
        }
    }

    public String getDetails(){
        return log;
    }

}
