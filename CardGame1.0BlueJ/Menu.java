import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Menu extends JPanel implements ActionListener{

    private JButton playButton;
    private JButton creditButton;
    private JButton exitButton;
    private Image background;

    public Menu(){
        //set the Panel up.
        setLayout(null);
        setBackground("Backgrounds\\MenuBackgroundNotree.jpg");
        playButton = new JButton("PLAY");
        initializeButton(playButton,825,275);

        creditButton = new JButton("CREDITS");
        initializeButton(creditButton,825,475);

        exitButton = new JButton("EXIT");
        initializeButton(exitButton,825,675);
    }

    //Method for the 3 buttons in Menu.
    private void initializeButton(JButton button,int x,int y){
        Dimension buttonSize = new Dimension(300,100);
        button.setSize(buttonSize);
        button.setLocation(x,y);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFont(new Font("menuFont",Font.BOLD,50));
        button.setForeground(Color.CYAN);
        button.addActionListener(this);
        add(button);
    }

    //Keep track with the button user click, and switch to the right panel, or exit.
    @Override
    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();      //get the source of the action event.

        if(source == playButton){
            Game.cardLayout.show(Game.panels,"Deck Selection");      //witch to the deck selection panel.
        }
        else if(source == creditButton){
            Game.cardLayout.show(Game.panels,"Credit");      //switch to the Credit panel.
        }
        else if(source == exitButton){
            System.exit(0);     //exit the program.
        }

    }

    //Method for setting the background with our own picture.
    private void setBackground(String url){
        try{
            background = ImageIO.read(new File(url));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    //paint the background.
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.drawImage(background,0,0,null);
    }


}
