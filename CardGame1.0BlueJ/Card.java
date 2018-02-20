import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

//Card class, which is the super class for Monster and Spell, and it's inherited from JButton.
public abstract class Card extends JButton{
    //Consist the basic attribute for monster and spell that are in common.
    protected String name;
    protected boolean isSpell;
    protected Image img;
    protected Icon oldDisabledIcon;

    //Constructor that gets the name,if it's a spell, url for picture, and if it's a card for the player or the AI.
    public Card(String name,boolean isSpell,String url,boolean player){
        this.name = name;
        this.isSpell = isSpell;
        //the size for our card is 130x200 in pixels.
        setSize(130,200);
        //no borders for the button.
        setBorder(null);

        try {
            //gets the Image from our files.
            img = ImageIO.read(new File(url));

            //resize the picture to the size of the Button, +15 after the width because it makes it look better.
            Image resizeImg = img.getScaledInstance(this.getWidth() + 15, this.getHeight(), Image.SCALE_AREA_AVERAGING);

            //set the picture on our card.
            setIcon(new ImageIcon(resizeImg));

            //Checks if it's a player card, so we can have the enemy hand cards covered
            //by the Card Back picture,but not the players'.
            if(!player){
                //Store the default disabledIcon because we need it again
                // when the enemy summon the monster on field, and we don't want them to show the card back anymore.
                oldDisabledIcon = getDisabledIcon();

                //get the cardBack for enemyHandCards.
                Icon disabledIcon = new ImageIcon(ImageIO.read(new File("CardBack.png")).getScaledInstance(this.getWidth() + 28, this.getHeight(), Image.SCALE_AREA_AVERAGING));
                //set the picture when the card is disabled.
                setDisabledIcon(disabledIcon);
            }

        }
        //Need to catch the IOException because we used ImageIo to read the files.
        catch (IOException e){
            e.printStackTrace();
            //Notice this would be an exception if we don't type the url correct, just a reminder.
            System.out.println("Check your url for the picture");
        }
    }

    public String getName(){
        return name;
    }

    public boolean getIsSpell(){
        return isSpell;
    }

    public Icon getOldDisabledIcon(){
        return oldDisabledIcon;
    }

}
