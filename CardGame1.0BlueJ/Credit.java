import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Just a panel that paints our names.
public class Credit extends JPanel {
    private String[] credits = {"      BENNY","        JOE","        ROB","       NICK","      JASON"};
    private JButton mainMenu;

    public Credit(){
        setLayout(null);
        setBackground(Color.black);
        mainMenu = new JButton("Main Menu");
        mainMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game.cardLayout.show(Game.panels,"Menu");
            }
        });
        mainMenu.setBounds(1820,1025,100,50);
        add(mainMenu);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for(int i = 0; i < credits.length; i++){
            g.setColor(Color.CYAN);
            g.setFont( new Font("creditFont", Font.BOLD, 200));
            g.drawString(credits[i],350,(i*200)+200);
        }
    }
}
