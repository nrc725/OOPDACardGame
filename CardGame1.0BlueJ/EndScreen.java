import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//A panel that shows when the battle ends.
public class EndScreen extends JPanel{
    private String youWon = "YOU WON";
    private String youLost = "YOU LOST";
    private String printString;
    private JButton mainMenu;

    public EndScreen(){
        //setting up the panel.
        setLayout(null);
        setBackground(Color.black);
        mainMenu = new JButton("Main Menu");
        mainMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.cardLayout.show(Game.panels,"Menu");
            }
        });
        mainMenu.setBounds(1350,1000,100,50);
        add(mainMenu);
    }

    //Paint the word.
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(printString.equalsIgnoreCase(youWon)){
            g.setColor(Color.ORANGE);
        }
        else{
            g.setColor(Color.RED);
        }
        g.setFont(new Font("endFont",Font.BOLD, 200));
        g.drawString(printString,500,550);
    }

    //In the battleField, when the player or the enemy dies,
    // we will pass in 0 or 1 for this parameter, and it decides which String to print.
    public void setPrintString(int i){
        if(i == 0){
            printString = youWon;
        }
        else{
            printString = youLost;
        }
        repaint();
    }

}
