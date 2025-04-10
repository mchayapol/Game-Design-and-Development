package edu.au.gdd.sprite;

import javax.swing.ImageIcon;
import edu.au.gdd.Global;

public class Shot extends Sprite {

    public Shot() {
    }

    public Shot(int x, int y) {

        initShot(x, y);
    }

    private void initShot(int x, int y) {

        var shotImg = "src/images/shot.png";
        var ii = new ImageIcon(shotImg);

        // Scale the image to use the global scaling factor
        var scaledImage = ii.getImage().getScaledInstance(ii.getIconWidth() * Global.SCALE_FACTOR, ii.getIconHeight() * Global.SCALE_FACTOR, java.awt.Image.SCALE_SMOOTH);
        setImage(scaledImage);

        int H_SPACE = 6;
        setX(x + H_SPACE);

        int V_SPACE = 1;
        setY(y - V_SPACE);
    }
}