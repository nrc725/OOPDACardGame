import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class BattleField extends JPanel implements ActionListener {

    //player's decks/hand/feild.
    private ArrayList<Monster> myMonsterDeck;
    private ArrayList<Spell> mySpellDeck;
    private ArrayList<Card> myHandCards;
    private ArrayList<Monster> myFieldCards;

    //enemy's decks/hand/field.
    private ArrayList<Monster> enemyMonsterDeck;
    private ArrayList<Spell> enemySpellDeck;
    private ArrayList<Card> enemyHandCards;
    private ArrayList<Monster> enemyFieldCards;

    //Systematic buttons.
    private JButton mainMenu;
    private JButton endTurn;
    private JButton cancel;

    //JPopupMenus for cards.
    private JPopupMenu monsterPopMenu;
    private JPopupMenu spellPopMenu;
    private JPopupMenu fieldPopMenu;

    //JMenuItems for cards.
    private JMenuItem summon;
    private JMenuItem attack;
    private JMenuItem cast;

    //This three field will keep tract with what is the last monster/spell we clicked on, in order to process attacks and casts right.
    private Monster currentMonster;
    private Spell currentSpell; // should be Spell
    private Monster targetMonster;

    //cardStatus is what appear when we put our cursor on the card, and battleLog is the log for battle details.
    private JLabel cardStatus;
    private JLabel battleLog;
    private Type type;

    private Player player;
    private Player aI;
    private String playerNow = "[ You ]";

    //This is a two dimension array, which has 5 elements, each of them contains a x and y for the position.
    private int[][] myFieldPlaces =  {{750,575},{550,575},{950,575},{350,575},{1150,575}};

    //This is an array correspond with the myFieldPlaces, which everytime we summon a monster, we turn the correspond
    //element to true, and turn back to false when it's removed.
    private boolean[] myFieldPlacesTaken = {false,false,false,false,false};

    //positions for enemyFields.
    private int[][] enemyFieldPlaces = {{750,350},{550,350},{950,350},{350,350},{1150,350}};

    private boolean[] enemyFieldPlacesTaken = {false,false,false,false,false};

    //every time we click "cast", it means we are casing a spell, this will help us do the trick.
    private boolean usingSpell = false;

    //ScrollPane for battle log.
    JScrollPane scroller;
    JScrollBar sb;

    public BattleField(){
        setLayout(null);
        setBackground(Color.DARK_GRAY);
        myMonsterDeck = new ArrayList<>();
        mySpellDeck = new ArrayList<>();
        myHandCards = new ArrayList<>();
        myFieldCards = new ArrayList<>();
        enemyMonsterDeck = new ArrayList<>();
        enemySpellDeck = new ArrayList<>();
        enemyHandCards = new ArrayList<>();
        enemyFieldCards = new ArrayList<>();
        mainMenu = new JButton("Main Menu");
        endTurn = new JButton("End Turn");
        cancel = new JButton("Cancel");
        monsterPopMenu = new JPopupMenu();
        spellPopMenu = new JPopupMenu();
        fieldPopMenu = new JPopupMenu();
        summon = new JMenuItem("Summon");
        attack = new JMenuItem("Attack");
        cast = new JMenuItem("Cast");
        cardStatus = new JLabel();
        battleLog = new JLabel();
        player = new Player();
        aI = new Player();

        basicInitiation();
    }

    //This is where we take the decks player picked, and add actionListener/mouseListener to them.
    public void setMyDecks(ArrayList<Monster> monsterDeck, ArrayList<Spell> spellDeck){
        myMonsterDeck = monsterDeck;

        for(final Monster monster : myMonsterDeck){
            monster.addActionListener(this);

            //These mouseListeners help with updating the change of status.
            monster.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    cardStatus.setText(monster.getStatus());
                    cardStatus.setVisible(true);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    cardStatus.setVisible(false);
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    cardStatus.setText(monster.getStatus());
                }
            });
        }

        mySpellDeck = spellDeck;
        for(final Spell spell : mySpellDeck ){
            spell.addActionListener(this);
            spell.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    cardStatus.setText(spell.getStatus());
                    cardStatus.setVisible(true);
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    cardStatus.setVisible(false);
                }
            });
        }
    }

    public void setEnemyDecks(ArrayList<Monster> monsterDeck, ArrayList<Spell> spellDeck){
        enemyMonsterDeck = monsterDeck;
        for(final Monster monster : enemyMonsterDeck){
            monster.addActionListener(this);
        }

        enemySpellDeck = spellDeck;
    }

    private void basicInitiation(){
        mainMenu.setBounds(1820,1055,100,25);
        mainMenu.addActionListener(this);
        add(mainMenu);

        endTurn.setBounds(1450,500,150,50);
        endTurn.addActionListener(this);
        add(endTurn);

        cancel.setBounds(1670,875,150,50);
        cancel.addActionListener(this);
        cancel.setEnabled(false);
        add(cancel);

        summon.addActionListener(this);
        monsterPopMenu.add(summon);

        attack.addActionListener(this);
        fieldPopMenu.add(attack);

        cast.addActionListener(this);
        spellPopMenu.add(cast);

        cardStatus.setBounds(50,250,300,500);
        cardStatus.setForeground(Color.white);
        cardStatus.setFont(new Font("statusFont",Font.BOLD,20));
        cardStatus.setVisible(false);
        add(cardStatus);


        battleLog.setBounds(1600,250,320,600);
        battleLog.setOpaque(true);
        battleLog.setBorder(new EmptyBorder(0,10,0,0));
        battleLog.setBackground(Color.BLACK);
        battleLog.setForeground(Color.YELLOW);
        battleLog.setVerticalAlignment(JLabel.TOP);
        battleLog.setHorizontalAlignment(JLabel.LEFT);
        battleLog.setFont(new Font("logFont",Font.PLAIN,12));
        battleLog.setText("<html>## Your Turn ##<br> ");
        battleLog.setVisible(true);
        add(battleLog);

        scroller = new JScrollPane(battleLog, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroller.setBounds(1600,250,320,600);
        scroller.setVisible(true);
        add(scroller);

        sb = scroller.getVerticalScrollBar();
    }

    //Arrange hand card is used everytime we summon/cast/draw card, so our hand card will be sort in the right way.
    private void arrangeHandCard(ArrayList<Card> hand){
        if(hand == myHandCards){
            for(int i = 0; i < hand.size(); i++){
                if(i == 0){
                    hand.get(i).setLocation(125,825);
                    add(hand.get(i));
                }
                else{
                    hand.get(i).setLocation((i*150)+125,825);
                    add(hand.get(i));
                }
            }
        }
        else{
            for(int i = 0; i < hand.size(); i++){
                if(i == 0){
                    hand.get(i).setLocation(125,100);
                    add(hand.get(i));
                }
                else{
                    hand.get(i).setLocation((i*150)+125,100);
                    add(hand.get(i));
                }
            }
        }

    }

    //a window that pops when the player try to go back to main menu.
    public void warningWindow(Object source){
        if (JOptionPane.showConfirmDialog(null, "This action will END THE BATTLE, ARE YOU SURE ?", "WARNING",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            if(source == mainMenu){
                Game.cardLayout.show(Game.panels,"Menu");
                resetAll();
            }
        }
        else {
        }
    }

    //This is mainly what take control of the game. It's the "brain".
    //switch method will definitely be a more efficient way, however since we
    //have a lot of objects that does not work in the switch statement, we just left it in
    //else if statements. There might be ways to work around it and make the switch work, but
    //we did not find it out just yet. Fortunately this doesn't cause any lag so far.
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();

        if(source == mainMenu){
            warningWindow(source);
        }
        else if(myHandCards.contains(source)){
            Component card = (Component) e.getSource();
            if(checkSource(source)){
                monsterPopMenu.show(card,0,0);
                monsterPopMenu.setLocation(card.getX()+70, card.getY()-25);
                currentMonster = (Monster) source;
            }
            else{
                spellPopMenu.show(card,0,0);
                spellPopMenu.setLocation(card.getX()+70, card.getY()-25);
                currentSpell = (Spell) source;
            }
        }
        else if(myFieldCards.contains(source)){
            cancel.setEnabled(false);
            currentMonster = (Monster)source;
            if(!usingSpell){
                Component card = (Component) e.getSource();
                fieldPopMenu.show(card,0,0);
                fieldPopMenu.setLocation(card.getX()+70, card.getY()-25);
            }
            else{
                currentSpell.useEffect(currentMonster);
                battleLog.setText(battleLog.getText() + playerNow + currentSpell.getDetails());
                sb.setValue(sb.getMaximum());
                usingSpell = false;
                endTurn.setEnabled(true);
                enableField(myFieldCards);
                enableCards(myHandCards);
                disableField(enemyFieldCards);
                myHandCards.remove(currentSpell);
                arrangeHandCard(myHandCards);
                remove(currentSpell);
                revalidate();
                repaint();
            }
        }
        else if(source == summon){
            if(checkEnoughMana(player,currentMonster)){
                summonPlacement(currentMonster);
                arrangeHandCard(myHandCards );
            }
            else{
                JOptionPane.showMessageDialog(new JFrame(), "Not enough Mana!");
            }
        }
        else if(source == attack){
            if(enemyFieldCards.size() > 0){
                attackPhase();
                cancel.setEnabled(true);
            }
            else{
                aI.takeDamage(currentMonster.getAttack());
                battleLog.setText(battleLog.getText() + playerNow + " Enemy took " + currentMonster.getAttack() + " damages<br>");
                sb.setValue(sb.getMaximum());
                currentMonster.attackedOnce();
                enableField(myFieldCards);
                if(aI.isDead()){
                    Game.getEndScreen().setPrintString(0);
                    displayOnEndGame();
                }
                repaint();
            }

        }
        else if(source == cast){
            usingSpell = true;
            String targetingType = currentSpell.getTargetingType();
            if(currentSpell.getEffectType().equals("AOE") && currentSpell.getInstant()){
                switch(targetingType.toLowerCase()){
                    case"self":
                        if(myFieldCards.size() > 0){
                            currentSpell.useEffect(myFieldCards);
                            battleLog.setText(battleLog.getText() + playerNow + currentSpell.getDetails());
                            sb.setValue(sb.getMaximum());
                        }
                        usingSpell = false;
                        break;
                    case"enemy":
                        currentSpell.useEffect(enemyFieldCards);
                        battleLog.setText(battleLog.getText() + playerNow + currentSpell.getDetails());
                        sb.setValue(sb.getMaximum());
                        usingSpell = false;
                        break;
                    case"both":
                        currentSpell.useEffect(myFieldCards,enemyFieldCards);
                        battleLog.setText(battleLog.getText() + playerNow + currentSpell.getDetails());
                        sb.setValue(sb.getMaximum());
                        usingSpell = false;
                        break;
                   }
                myHandCards.remove(currentSpell);
                arrangeHandCard(myHandCards);
                remove(currentSpell);
                repaint();

            }
            else{
                cancel.setEnabled(true);
                switch(targetingType.toLowerCase()){
                case"self":
                    if(myFieldCards.size() > 0){
                        endTurn.setEnabled(false);
                        disableCards(myHandCards);
                        enableFieldAbsolute(myFieldCards);
                    }
                    else{
                        usingSpell = false;
                    }
                    break;
                case"enemy":
                    if(enemyFieldCards.size() > 0){
                        enableFieldAbsolute(enemyFieldCards);
                        disableField(myFieldCards);
                        endTurn.setEnabled(false);
                        disableCards(myHandCards);
                    }
                    else{
                        usingSpell = false;
                    }
                    break;
                case"both":
                    if(myFieldCards.size() > 0 || enemyFieldCards.size() > 0){
                        endTurn.setEnabled(false);
                        disableCards(myHandCards);
                        enableFieldAbsolute(myFieldCards);
                        enableFieldAbsolute(enemyFieldCards);
                    }
                    else{
                        usingSpell = false;
                    }
                    break;
            }

            }
            checkAllDead(myFieldCards);
            checkAllDead(enemyFieldCards);

        }
        else if(enemyFieldCards.contains(source)){          //which means we either attack or used spell on enemy monster.
            cancel.setEnabled(false);
            targetMonster = (Monster) source;

            if(!usingSpell) {
                Monster attackingMonster = currentMonster;
                attackProcess(attackingMonster, targetMonster);
                enableCards(myHandCards);
                enableField(myFieldCards);
                disableField(enemyFieldCards);
            }
            else{
                currentSpell.useEffect(targetMonster);
                battleLog.setText(battleLog.getText() + playerNow + currentSpell.getDetails());
                sb.setValue(sb.getMaximum());
                if(targetMonster.checkDead()){
                    removeFieldCard(targetMonster);
                }
                usingSpell = false;
                enableField(myFieldCards);
                disableField(enemyFieldCards);
                enableCards(myHandCards);
                myHandCards.remove(currentSpell);
                arrangeHandCard(myHandCards);
                remove(currentSpell);
            }
            endTurn.setEnabled(true);
            repaint();
        }
        else if(source == endTurn){
            aITurn();
            if(aI.isDead()){
                Game.getEndScreen().setPrintString(0);
                displayOnEndGame();
            }
        }
        else if(source == cancel){
            usingSpell = false;
            currentSpell = null;
            currentMonster = null;
            enableCards(myHandCards);
            enableField(myFieldCards);
            endTurn.setEnabled(true);
            cancel.setEnabled(false);
            disableField(enemyFieldCards);
        }
    }

    //Summon processes.
    public void summonPlacement(Monster card){
        //Check if player drawing or enemy drawing. Which will have difference in place position and change of icon.
        if(myHandCards.contains(card)){
            boolean havePlace = false;
            for(int i = 0; i < myFieldPlaces.length; i++){
                if(myFieldPlacesTaken[i] == false){
                    card.setLocation(myFieldPlaces[i][0], myFieldPlaces[i][1]);
                    myFieldPlacesTaken[i] = true;
                    card.setTookField(i);
                    i = myFieldPlaces.length;
                    havePlace = true;
                    myFieldCards.add(card);
                    myHandCards.remove(card);
                    player.useMana(card.getCost());
                    battleLog.setText(battleLog.getText() + playerNow + " Summoned " + card.getName() + ", remain Mana: " + player.getMana() + "<br><br>");
                    sb.setValue(sb.getMaximum());
                }
                repaint();
            }
            if(!havePlace){
                JOptionPane.showMessageDialog(new JFrame(), "Summon is up to limit !");
            }
        }
        else if(enemyHandCards.contains(card)){
            for(int i = 0; i < enemyFieldPlaces.length; i++){
                if(enemyFieldPlacesTaken[i] == false){
                    card.setLocation(enemyFieldPlaces[i][0], enemyFieldPlaces[i][1]);
                    enemyFieldPlacesTaken[i] = true;
                    card.setTookField(i);
                    i = enemyFieldPlaces.length;
                    enemyFieldCards.add(card);
                    enemyHandCards.remove(card);
                    aI.useMana(card.getCost());
                    battleLog.setText(battleLog.getText() + playerNow + " Summoned " + card.getName() + ", remain Mana: " + aI.getMana() + "<br><br>");
                    sb.setValue(sb.getMaximum());
                }
            }
        }
    }

    //This take control of clickable buttons after we clicked "attack", so only the enemy monsters are clickable.
    public void attackPhase(){
        disableCards(myHandCards);
        disableField(myFieldCards);
        enableFieldAbsolute(enemyFieldCards);
        endTurn.setEnabled(false);
    }

    //short method to check enable of card and enable the ones that it's fine to be enable, especially for hands.
    public void enableCards(ArrayList<Card> cards){
        for(Card card : cards){
            card.setEnabled(true);
        }
    }

    //This is for field cards, which also check for attacked times and if it was froze.
    public void enableField(ArrayList<Monster> monsters){
        for(Monster monster : monsters){
            if(monster.checkAttackChance() && !monster.getFroze()){
                monster.setEnabled(true);
            }
            else{
                monster.setEnabled(false);
            }
        }
    }

    //Enable no matter what, this is for attacking and casting spell target, we want the targets to be available
    //regardless if they were froze or attacked.
    public void enableFieldAbsolute(ArrayList<Monster> monsters){
        for(Monster monster : monsters){
            monster.setEnabled(true);
        }
    }

    //disable mainly used for handcards.
    public void disableCards(ArrayList<Card> cards){
        for(Card card : cards){
            card.setEnabled(false);
        }
    }

    //disable manly used for field cards.
    public void disableField(ArrayList<Monster> monsters){
        for(Monster monster : monsters){
            monster.setEnabled(false);
        }
    }

    //remove a field card, which usually is used after an attack, or spell cast.
    public void removeFieldCard(Monster targetCard){
        if(myFieldCards.contains(targetCard)){
            myFieldPlacesTaken[targetCard.getTookField()] = false;
            myFieldCards.remove(targetCard);
        }
        else{
            enemyFieldPlacesTaken[targetCard.getTookField()]= false;
            enemyFieldCards.remove(targetCard);
        }
        remove(targetCard);
        revalidate();
        repaint();

    }

    //draw card,resets the field monsters' attack chance,increase mana, basically work as a turn start method.
    public void drawCard(ArrayList<Card> hand){
        if(hand == myHandCards){
            player.increaseTotalMana();
            player.resetMana();
            if(myMonsterDeck.size() > 0 && mySpellDeck.size() > 0){
                if(hand.size() < 9){
                    int randomMonster = (int) (Math.random() * (myMonsterDeck.size() - 1) + 0);
                    int randomSpell = (int) (Math.random() * (mySpellDeck.size() - 1) + 0);
                    hand.add(myMonsterDeck.get(randomMonster));
                    myMonsterDeck.remove(randomMonster);

                    hand.add(mySpellDeck.get(randomSpell));
                    mySpellDeck.remove(randomSpell);
                }
            }
            else{
                player.takeDamage(player.getOutDrawDamage());
                if(player.isDead()){
                    Game.getEndScreen().setPrintString(1);
                    displayOnEndGame();
                }
                player.increaseOutDrawDamage();
                repaint();
            }

            for(Monster monster : myFieldCards){
                monster.resetAttackTimes();
            }
            arrangeHandCard(hand);
            disableCards(hand);
            enableCards(hand);

            checkFieldAfterDrawPlayer(myFieldCards);
            repaint();
        }
        else{
            aI.increaseTotalMana();
            aI.resetMana();
            repaint();
            if(enemyMonsterDeck.size() > 0 && enemySpellDeck.size() > 0){
                if(hand.size() < 9){
                    int randomMonster = (int) (Math.random() * (enemyMonsterDeck.size() - 1) + 0);
                    int randomSpell = (int) (Math.random() * (enemySpellDeck.size() - 1) + 0);
                    hand.add(enemyMonsterDeck.get(randomMonster));
                    enemyMonsterDeck.remove(randomMonster);

                    hand.add(enemySpellDeck.get(randomSpell));
                    enemySpellDeck.remove(randomSpell);
                }
            }
            else{
                aI.takeDamage(aI.getOutDrawDamage());
                aI.increaseOutDrawDamage();
                repaint();
            }
            arrangeHandCard(hand);
            disableCards(hand);
        }
    }

    //Normal attack, takes the monster that attacks and target. Also check the types,deal 2x more dmg to the counter type.
    public void attackProcess(Monster attackingMonster, Monster targetCard){
        int attack = attackingMonster.getAttack();
        Type atkMonsterType = attackingMonster.getType();
        Type targetType = targetCard.getType();
        switch(atkMonsterType){
            case BLUE:
                if(targetType == type.RED){
                    attack *= 2;
                }
                break;
            case RED:
                if(targetType == type.GREEN){
                    attack *= 2;
                }
                break;
            case GREEN:
                if(targetType == type.BLUE){
                    attack *= 2;
                }
                break;
        }
        targetCard.takeDamge(attack);

        attackingMonster.attackedOnce();
        checkAttacked(attackingMonster);
        if(targetCard.checkDead()){
            battleLog.setText(battleLog.getText() + playerNow +" " + attackingMonster.getName() + " attacked " + targetCard.getName() + "<br>" + playerNow + " " + targetCard.getName() + " is dead<br><br>");
            sb.setValue(sb.getMaximum());
            removeFieldCard(targetCard);

        }
        else{
            battleLog.setText(battleLog.getText() + playerNow + " " + attackingMonster.getName() + " attacked " + targetCard.getName() +" (-" + attackingMonster.getAttack() + "hp)"+ "<br>" + playerNow + " " + targetCard.getName() + " now have " + targetCard.getHealth() + "hp left<br><br>");
            sb.setValue(sb.getMaximum());
        }
    }

    //If a monster is attacked, then set it disable.
    public void checkAttacked(Monster monster){
        if(monster.getAttackedTimes() >= monster.getMaxAttackTime()){
            monster.setEnabled(false);
        }
    }

    public void checkAllDead(ArrayList<Monster> monsters){
        if(monsters.size() > 0){
            for(int i = 0; i < monsters.size(); i++){
                if(monsters.get(i).checkDead()){
                    removeFieldCard(monsters.get(i));
                    i--;
                }
            }
        }
    }

    public void checkFieldAfterDrawPlayer(ArrayList<Monster> monsters){
        for(Monster monster : monsters){
            if(!monster.getFroze()){
                monster.setEnabled(true);
            }
            else{
                monster.setEnabled(false);
                if(monster.getFrozeTurn() < 1){
                   monster.increaseFrozeTurn();
                }
                else{
                    monster.unFroze();
                    monster.setEnabled(true);
                }
            }
        }
    }

    private boolean checkSource(Object source){
        if(source.getClass().getName().equalsIgnoreCase("Monster")){
            return true;
        }
        return false;
    }

    private void displayOnEndGame(){
        Game.cardLayout.show(Game.panels,"End Screen");
        resetAll();
    }

    private boolean checkEnoughMana(Player player,Monster monster){
        if(monster.getCost() <= player.getMana()){
            return true;
        }
        return false;
    }

    //This is method that runs after the player endTurn, which does everything the AI needs to do.
    private void aITurn(){
        battleLog.setText(battleLog.getText() + "<br>##Enemy Turn##<br>");
        sb.setValue(sb.getMaximum());
        playerNow = "[ Enemy ]";
        //Enemy draw card.
        drawCard(enemyHandCards);
        disableCards(myHandCards);
        disableField(myFieldCards);
        //check if dead by out draw damage.
        if(aI.isDead()){
            Game.getEndScreen().setPrintString(0);
            displayOnEndGame();
        }

        //for all enemy field monster, check if it was froze,if yes then increase the froze turn, or unfroze.
        for(Monster monster : enemyFieldCards){
            monster.resetAttackTimes();
            if(monster.getFroze()){
                if(monster.getFrozeTurn() < 2){
                    monster.increaseFrozeTurn();
                }
                else{
                    monster.unFroze();
                }
            }

        }


        //Iterate through cards in hand.
        for(int i = 0; i < enemyHandCards.size(); i++){

            //if it's a monster card.
            if(!enemyHandCards.get(i).getIsSpell()){

                //check for enough mana, and summon, also switch the disableIcon from cardBack to the old default oen.
                final Monster monster =(Monster) enemyHandCards.get(i);
                if(checkEnoughMana(aI,monster)){
                    if(enemyFieldCards.size() < 5){
                        summonPlacement(monster);
                        monster.switchDisableIcon();

                        //add the mouseListener after it is summon, so player can see the status on it.
                        monster.addMouseListener(new MouseAdapter() {
                            @Override
                            public void mouseEntered(MouseEvent e) {
                                cardStatus.setText(monster.getStatus());
                                cardStatus.setVisible(true);
                            }

                            @Override
                            public void mouseExited(MouseEvent e) {
                                cardStatus.setVisible(false);
                            }

                            @Override
                            public void mouseClicked(MouseEvent e) {
                                super.mouseClicked(e);
                                cardStatus.setText(monster.getStatus());
                            }
                        });
                        arrangeHandCard(enemyHandCards);
                    }

                }

            }
            //else for the spell.
            else{
                Spell spell = (Spell) enemyHandCards.get(i);

                //get the effectType which either are AOE or single.
                switch (spell.getEffectType()) {

                    //in case of AOE
                    case "AOE":
                        //Check if it should be use on self, enemy or both, so we can have the right condition for it to be use.
                        switch (spell.getTargetingType()) {
                            case "self":
                                if (enemyFieldCards.size() > 1) {
                                    spell.useEffect(enemyFieldCards);
                                    battleLog.setText(battleLog.getText() + playerNow + spell.getDetails());
                                    sb.setValue(sb.getMaximum());
                                    enemyHandCards.remove(spell);
                                    remove(spell);
                                }
                                break;
                            case "enemy":
                                if (myFieldCards.size() > 1) {
                                    spell.useEffect(myFieldCards);
                                    battleLog.setText(battleLog.getText() + playerNow + spell.getDetails());
                                    sb.setValue(sb.getMaximum());
                                    enemyHandCards.remove(spell);
                                    remove(spell);
                                }
                                break;
                            case "both":
                                if (myFieldCards.size() > 0 && enemyFieldCards.size() > 0) {
                                    spell.useEffect(myFieldCards,enemyFieldCards);
                                    battleLog.setText(battleLog.getText() + playerNow + spell.getDetails());
                                    sb.setValue(sb.getMaximum());
                                    enemyHandCards.remove(spell);
                                    remove(spell);
                                }
                                break;
                        }
                        arrangeHandCard(enemyHandCards);
                        repaint();
                        break;
                    //In case of single
                    case "single":
                        switch (spell.getTargetingType()) {
                            //targeting are mostly picked randomly
                            case "self":
                                if (enemyFieldCards.size() > 0) {
                                    spell.useEffect(enemyFieldCards.get(enemyFieldCards.size() - 1));
                                    battleLog.setText(battleLog.getText() + playerNow + spell.getDetails());
                                    sb.setValue(sb.getMaximum());
                                    enemyHandCards.remove(spell);
                                    remove(spell);
                                }
                                break;
                            case "enemy":
                                if (myFieldCards.size() > 0) {
                                    spell.useEffect(myFieldCards.get(myFieldCards.size() - 1));
                                    battleLog.setText(battleLog.getText() + playerNow + spell.getDetails());
                                    sb.setValue(sb.getMaximum());
                                    enemyHandCards.remove(spell);
                                    remove(spell);
                                }
                                break;
                            case "both":
                                if (enemyFieldCards.size() > 0 && myFieldCards.size() > 0) {
                                    ArrayList<Monster> monsters = new ArrayList<>();
                                    for (Monster monster : enemyFieldCards) {
                                        monsters.add(monster);
                                    }
                                    for (Monster monster : myFieldCards) {
                                        monsters.add(monster);
                                    }
                                    int random = (int) (Math.random() * (monsters.size() - 1) + 0);
                                    spell.useEffect(monsters.get(random));
                                    battleLog.setText(battleLog.getText() + playerNow + spell.getDetails());
                                    sb.setValue(sb.getMaximum());
                                    enemyHandCards.remove(spell);
                                    remove(spell);
                                }
                                break;
                        }
                        arrangeHandCard(enemyHandCards);
                        repaint();
                        break;
                }
                //after using the spell, check dead for all monsters.
                checkAllDead(myFieldCards);
                checkAllDead(enemyFieldCards);
            }
        }
        repaint();

        //If there's monster on AI's field, it checks if they can attack, then attack either a random monster on player field.
        //or the player.
        if(enemyFieldCards.size() > 0){
            for(Monster monster : enemyFieldCards){
                if(monster.checkAttackChance() && !monster.getFroze()){
                    for(int i = 0;i < monster.getMaxAttackTime();i++){
                        if(myFieldCards.size() > 0){
                            int random = (int)(Math.random() * (myFieldCards.size() -1) + 0);
                            attackProcess(monster,myFieldCards.get(random));
                        }
                        else{
                            player.takeDamage(monster.getAttack());
                            battleLog.setText(battleLog.getText() + "You took " + monster.getAttack() + "Damages!<br> ");
                            sb.setValue(sb.getMaximum());
                            monster.attackedOnce();
                        }
                    }
                }
            }
        }

        //after attack and spell, check if the player is dead.
        if(player.isDead()){
            Game.getEndScreen().setPrintString(1);
            displayOnEndGame();
        }

        //if player still alive, then start the player turn.
        else{
            playerTurnStart();
        }
        playerNow = "[ You ]";
        battleLog.setText(battleLog.getText() + "<br>##Your Turn##<br>");
        sb.setValue(sb.getMaximum());
    }

    //start of player turn.
    private void playerTurnStart(){
        drawCard(myHandCards);
    }

    //paints the hp & mana status and bars.
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Font healthFont = new Font("health",Font.BOLD, 20);
        Font manaFont = new Font("mana",Font.BOLD,20);

        String playerHealthStatus = player.getHealth() + " / " + player.getMaxHealth();
        g.setColor(Color.RED);
        g.setFont(healthFont);
        g.drawString(playerHealthStatus,150,1050);
        g.drawRect(225,1040,player.getMaxHealth()*50,10);
        if(player.getHealth() < 1){
            g.fillRect(225,1040,0,10);
        }
        else{
            g.fillRect(225,1040,player.getHealth()*50,10);
        }

        String playerManaStatus = player.getMana() + " / " + player.getTotalMana();
        g.setColor(Color.BLUE);
        g.setFont(manaFont);
        g.drawString(playerManaStatus,150,1070);
        g.drawRect(225,1060,player.getTotalMana()*125,10);
        g.fillRect(225,1060,player.getMana()*125,10);


        String aIHealthStatus = aI.getHealth() + " / " + aI.getMaxHealth();
        g.setColor(Color.RED);
        g.setFont(healthFont);
        g.drawString(aIHealthStatus,150,60);
        g.drawRect(225,50,aI.getMaxHealth()*50,10);
        if(aI.getHealth() < 1){
            g.fillRect(225,50,0,10);
        }
        else{
            g.fillRect(225,50,aI.getHealth()*50,10);
        }

        String aIManaStatus = aI.getMana() + " / " + aI.getTotalMana();
        g.setColor(Color.BLUE);
        g.setFont(manaFont);
        g.drawString(aIManaStatus,150,80);
        g.drawRect(225,70,aI.getTotalMana()*125,10);
        g.fillRect(225,70,aI.getMana()*125,10);
    }

    public ArrayList<Card> getMyHandCards(){
        return myHandCards;
    }

    public ArrayList<Card> getEnemyHandCards(){
        return enemyHandCards;
    }

    //reset method when game is ended reset all fields that are necessary, and clear field/hand/deck. Then in the deckSelection it will make a new deck.
    public void resetAll(){
        while(myMonsterDeck.size() > 0){
            myMonsterDeck.remove(0);
        }

        while(mySpellDeck.size() > 0){
            mySpellDeck.remove(0);
        }

        while(myHandCards.size() > 0){
            remove(myHandCards.get(0));
            myHandCards.remove(0);
        }

        while(myFieldCards.size() > 0){
            remove(myFieldCards.get(0));
            myFieldCards.remove(0);
        }

        while(enemyMonsterDeck.size() > 0){
            enemyMonsterDeck.remove(0);
        }

        while(enemySpellDeck.size() > 0){
            enemySpellDeck.remove(0);
        }

        while(enemyHandCards.size() > 0){
            remove(enemyHandCards.get(0));
            enemyHandCards.remove(0);
        }

        while(enemyFieldCards.size() > 0){
            remove(enemyFieldCards.get(0));
            enemyFieldCards.remove(0);
        }

        player.reset();
        aI.reset();

        currentMonster = null;
        currentSpell = null;
        targetMonster = null;
        usingSpell = false;
        for(int i = 0; i < myFieldPlacesTaken.length;i++){
            myFieldPlacesTaken[i] = false;
        }
        for(int i = 0; i < enemyFieldPlacesTaken.length;i++){
            enemyFieldPlacesTaken[i] = false;
        }

        playerNow = "[ You ]";
    }

}