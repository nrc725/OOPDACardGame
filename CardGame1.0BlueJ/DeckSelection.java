import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

//Panel where the player select a Deck.
public class DeckSelection extends JPanel implements ActionListener {
    private JButton deckAButton;
    private JButton deckBButton;
    private JButton mainMenu;

    private ArrayList<Monster> deckAMonster;
    private ArrayList<Spell> deckASpell;

    private ArrayList<Monster> deckBMonster;
    private ArrayList<Spell> deckBSpell;

    private Image background;

    //Constructor that set up the panel.
    public DeckSelection(){
        setLayout(null);
        setBackground("Backgrounds\\MenuBackgroundNotree.jpg");

        deckAButton = new JButton("DECK A");
        initiateButtons(deckAButton,450,450,300,150);

        deckBButton = new JButton("DECK B");
        initiateButtons(deckBButton,1200,450,300,150);

        mainMenu = new JButton("Main Menu");
        initiateButtons(mainMenu,1820,1025,100,50);
    }

    //Method for initiating the Buttons in the panel.
    private void initiateButtons(JButton button,int x,int y,int height,int length){
        button.setBounds(x,y,height,length);
        button.addActionListener(this);
        add(button);
    }

    //If the player click A, then it will make both decks, and give the player the right deck they picked.
    //This cause a small delay after clicking one button because it use some time to read and get all the pictures,
    //also make all the cards up again.
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();

        if(source == deckAButton){
            makeJusticeMonsterDeck(deckAMonster = new ArrayList<>(),true);    //initiate the arrayList and makes the deck.
            makeEvilMonsterDeck(deckBMonster = new ArrayList<>(),false);

            makeJusticeSpellDeck(deckASpell = new ArrayList<>(),true);
            makeEvilSpellDeck(deckBSpell = new ArrayList<>(),false);

            //Calls method from BattleField so it assign the correct deck to the player.
            Game.getBattleField().setMyDecks(deckAMonster,deckASpell);
            Game.getBattleField().setEnemyDecks(deckBMonster,deckBSpell);

            //draw card for player and enemy for the first turn.
            Game.getBattleField().drawCard(Game.getBattleField().getMyHandCards());
            Game.getBattleField().drawCard(Game.getBattleField().getEnemyHandCards());

            //switch the panel to battleField.
            Game.cardLayout.show(Game.panels,"Battlefield");
        }
        else if(source == deckBButton){
            //Similar to the last condition. But it assign a different deck for the player.
            //There might be a better way to do the deck assignment, however we kind of ran out of
            //time, so we just kept it as this way.
            makeJusticeMonsterDeck(deckAMonster = new ArrayList<>(),false);
            makeEvilMonsterDeck(deckBMonster = new ArrayList<>(),true);

            makeJusticeSpellDeck(deckASpell = new ArrayList<>(),false);
            makeEvilSpellDeck(deckBSpell = new ArrayList<>(),true);

            Game.getBattleField().setMyDecks(deckBMonster,deckBSpell);
            Game.getBattleField().setEnemyDecks(deckAMonster,deckASpell);
            Game.getBattleField().drawCard(Game.getBattleField().getEnemyHandCards());
            Game.getBattleField().drawCard(Game.getBattleField().getMyHandCards());
            Game.cardLayout.show(Game.panels,"Battlefield");
        }
        else if(source == mainMenu){
            Game.cardLayout.show(Game.panels,"Menu");    //switch to menu panel.
        }

    }

    //might need a better structure for more decks in option ,this is just for now(two preset decks).
    //make the Justice monsters.
    private void makeJusticeMonsterDeck(ArrayList<Monster> deck,boolean player){
        deck.add(new Monster("Mino",1,1,2,Type.BLUE,"MonsterPictures\\mino.png",player));
        deck.add(new Monster("Fire Dragon",5,3,9,Type.RED,"MonsterPictures\\fireDragon.png",player));
        deck.add(new Monster("shadow" ,3,4,4,Type.RED,"MonsterPictures\\Shadow.png",player));
        deck.add(new Monster("Fire Sword",1,2,1,Type.RED,"MonsterPictures\\fire Sword.png",player));
        deck.add(new Monster("Flame Warrior",3,4,2,Type.RED,"MonsterPictures\\flameWarrior.png",player));
        deck.add(new Monster("Grass Man" ,7,11,9,Type.GREEN,"MonsterPictures\\grassMan.png",player));
        deck.add(new Monster("Shark Man" ,7,8,2,Type.BLUE,"MonsterPictures\\sharkMan.png",player));
        deck.add(new Monster("Flame Warden",6,5,7,Type.RED,"MonsterPictures\\flameWarden.png",player));
        deck.add(new Monster("Rambo(Fox)" ,3,6,2,Type.RED,"MonsterPictures\\Rambo.png",player));
        deck.add(new Monster("MR.HEAT" ,1,1,3,Type.RED,"MonsterPictures\\mrHeat.png",player));
        deck.add(new Monster("MR.COOL" ,2,2,6,Type.BLUE,"MonsterPictures\\mrCool.png",player));
        deck.add(new Monster("The [E]" ,10,20,20,Type.RED,"MonsterPictures\\theE.png",player));
        deck.add(new Monster("Ardito" ,7,15,8,Type.GREEN,"MonsterPictures\\ardito.png",player));
    }

    //make the Justice spells.
    private void makeJusticeSpellDeck(ArrayList<Spell> deck,boolean player){
        deck.add(new Spell("Armageddon",9,"destroyAll","AOE","both",true,"SpellPictures\\Armageddon.png",player,"Kill all"));
        deck.add(new Spell("Ultra Heal",5,"heal","AOE","self",true,"SpellPictures\\heal.png",player,"+5hp"));
        deck.add(new Spell("The Triforce",3,"both","single","self",false,"SpellPictures\\The Triforce.png",player,"+3atk & 3hp"));
        deck.add(new Spell("Flip-Flop ",2,"flipFlop","single","both",false,"SpellPictures\\flipFlop.png",player,"flip atk & hp"));
        deck.add(new Spell("Fireball" ,2,"damage","single","enemy",false,"SpellPictures\\fireball.png",player,"deal 2 dmg"));
        deck.add(new Spell("Invade" ,3,"damage","AOE","enemy",true,"SpellPictures\\invade.png",player,"-3hp to enemy monsters"));
        deck.add(new Spell("Sword of Truth",2,"moreAttackChance","single","self",false,"SpellPictures\\Sword of Truth.png",player,"+1 atk chance"));
        deck.add(new Spell("Grasping Root",0,"freeze","single","enemy",false,"SpellPictures\\Grasping Roots.png",player,"freeze 1T"));
        deck.add(new Spell("Back stab" ,2,"destroy","single","enemy",false,"SpellPictures\\back stab.png",player,"Kill 1 monster"));
        deck.add(new Spell("Paul's Blessing",5,"heal","single","self",false,"SpellPictures\\Paul's Blessing.png",player,"+5 hp"));
        deck.add(new Spell("Redemption Furry",2,"both","single","self",false,"SpellPictures\\Redemption Furry.png",player,"+2 hp & atk"));
        deck.add(new Spell("Limited ",1,"changeAttack","single","enemy",false,"SpellPictures\\limited.png",player,"atk = 1"));
        deck.add(new Spell("Believe",6,"buffAttack","single","self",false,"SpellPictures\\Believe.png",player,"+6atk"));
    }

    //make the Evil Monsters.
    private void makeEvilMonsterDeck(ArrayList<Monster> deck,boolean player){
        deck.add(new Monster("Mino",1,1,2,Type.BLUE,"MonsterPictures\\mino.png",player));
        deck.add(new Monster("Flower",2,1,3,Type.GREEN,"MonsterPictures\\Flower.png",player));
        deck.add(new Monster("shadow" ,3,4,4,Type.RED,"MonsterPictures\\Shadow.png",player));
        deck.add(new Monster("Fire Sword",1,2,1,Type.RED,"MonsterPictures\\fire Sword.png",player));
        deck.add(new Monster("Squid",2,3,3,Type.BLUE,"MonsterPictures\\Squid.png",player));
        deck.add(new Monster("Bubble Buddy",1,1,3,Type.BLUE,"MonsterPictures\\bubbleBuddy.png",player));
        deck.add(new Monster("Organic Coffee",5,7,11,Type.GREEN,"MonsterPictures\\organicCoffee.png",player));
        deck.add(new Monster("Deep Abyss" ,3,6,4,Type.BLUE,"MonsterPictures\\deepAbyss.png",player));
        deck.add(new Monster("Insect" ,2,5,3,Type.GREEN,"MonsterPictures\\insect.png",player));
        deck.add(new Monster("Frozen Warrior" ,6,8,12,Type.BLUE,"MonsterPictures\\frozenWarrior.png",player));
        deck.add(new Monster("Fire Girl",3,5,5,Type.RED,"MonsterPictures\\fireGirl.png",player));
        deck.add(new Monster("Centaur" ,2,6,4,Type.GREEN,"MonsterPictures\\Centaur.png",player));
        deck.add(new Monster("Vincecraft" ,2,5,5,Type.GREEN,"MonsterPictures\\vince.png",player));
    }

    //make the Evil Spells.
    private void makeEvilSpellDeck(ArrayList<Spell> deck,boolean player){
        deck.add(new Spell("Insta-kill" ,2,"destroy","single","enemy",false,"SpellPictures\\Thunder.png",player,"kill 1 monster"));
        deck.add(new Spell("Thunder Storm",2,"destroyAll","AOE","both",true,"SpellPictures\\thunderStorm.png",player,"kill all"));
        deck.add(new Spell("Heal",3,"heal","single","self",false,"SpellPictures\\heal2.png",player,"+3hp"));
        deck.add(new Spell("Squeeze",2,"changeHealth","single","enemy",false,"SpellPictures\\squeeze.png",player,"hp=2"));
        deck.add(new Spell("Bleed" ,3,"damage","single","enemy",false,"SpellPictures\\bleed.png",player,"-3hp"));
        deck.add(new Spell("Overpowered",2,"damage","single","enemy",false,"SpellPictures\\overPowered.png",player,"-2hp"));
        deck.add(new Spell("Bear Glove",2,"moreAttackChance","single","self",false,"SpellPictures\\bearGlove.png",player,"+1 atk chance"));
        deck.add(new Spell("Cleric",2,"heal","AOE","self",true,"SpellPictures\\cleric.png",player,"+2hp to your monsters"));
        deck.add(new Spell("Word: ICE",2,"freeze","single","enemy",false,"SpellPictures\\freeze.png",player,"freeze 1T"));
        deck.add(new Spell("Iron Shield",5,"heal","single","self",false,"SpellPictures\\Iron Shield.png",player,"+5hp"));
        deck.add(new Spell("God's Gift ",6,"changeAttack","single","self",false,"SpellPictures\\godGift.png",player,"atk=6"));
        deck.add(new Spell("Power UP",4,"buffAttack","AOE","self",true,"SpellPictures\\powerUp.png",player,"+4atk to your monsters"));
        deck.add(new Spell("WAR CRY",2,"buffAttack","AOE","self",true,"SpellPictures\\warCry.png",player,"+2atk to your monsters"));
    }

    private void setBackground(String url){
        try{
            background = ImageIO.read(new File(url));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(background,0,0,null);
    }
}
