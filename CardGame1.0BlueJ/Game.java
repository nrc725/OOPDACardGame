import javax.swing.*;
import java.awt.*;

public class Game extends JFrame {

    static JPanel panels;
    static CardLayout cardLayout;
    private JPanel menu;
    private JPanel deckSelection;
    private JPanel credit;
    private static EndScreen endScreen;
    static BattleField battleField = new BattleField();

    public static void main(String[] args){
        new Game();
    }
    public Game(){

        //set the JFrame.
        setTitle("Card Game");
        setUndecorated(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);    //exit the program when we click the X.
        setExtendedState(this.MAXIMIZED_BOTH);

        //new JPanel that has the CardLayout as layout manager.
        panels = new JPanel((new CardLayout()));
        cardLayout = (CardLayout) panels.getLayout();

        //4 classes and each of them are inherited from JPanel.
        menu = new Menu();
        deckSelection = new DeckSelection();
        credit = new Credit();
        endScreen = new EndScreen();

        //adding them to the JPanel that has the CardLayout.
        panels.add(menu, "Menu");
        panels.add(deckSelection, "Deck Selection");
        panels.add(credit, "Credit");
        panels.add(battleField, "Battlefield");
        panels.add(endScreen, "End Screen");
        add(panels);
        //show the menu panel when the program first starts.
        cardLayout.show(panels, "Menu");
        setVisible(true);

    }

    public static BattleField getBattleField(){
        return battleField;
    }

    public static EndScreen getEndScreen(){
        return endScreen;
    }
}

