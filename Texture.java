/*

 * To change this license header, choose License Headers in Project Properties.

 * To change this template file, choose Tools | Templates

 * and open the template in the editor.

 */

package zombieMaze;


import java.awt.Color;
import java.awt.Image;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**

 *

 * @author Nick Villano

 */

public class Texture {


    public BufferedImage image;
public static final int TILE_SIZE=16;
    public Color[][] texture=new Color[TILE_SIZE][TILE_SIZE];

    public Texture(BufferedImage inImage) throws IOException {
       texture=new Color[TILE_SIZE][TILE_SIZE];
        image=inImage;
        int w = TILE_SIZE;
        for (int x = 0; x < w; x++) {

            for (int y = 0; y < w; y++) {
try{
                texture[x][y] = new Color(image.getRGB(x, y));
}catch(Exception e){
    System.err.println(x+"|"+y);
}
            }


        }

/*        
        for(Color[] c:texture){

            for(Color d:c){

          System.out.println(d);

        }
    }
*/
}
}

