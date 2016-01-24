/**
	* This is used to listen to the character moves typed in by the user
	*/
package zombieMaze;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author rohan
 */
public class Controller implements KeyListener{
	@Override
	/**
		* does nothing when key typed instead key pressed is what we are using
		*/
	public void keyTyped(KeyEvent e) {
	}

	@Override
	/**
		* @param KeyEvent the event when a key gets pressed
		* moves the main character using w a s and d keys space to drop a grenade
		* q to shoot
		*/
	public void keyPressed(KeyEvent e) {

if(e.getKeyCode()==KeyEvent.VK_UP){
    Map.p.move('a');
}  else if(e.getKeyCode()==KeyEvent.VK_DOWN){
    Map.p.move('d');
}else if(e.getKeyCode()==KeyEvent.VK_LEFT){
    Map.p.move('s');
}else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
    Map.p.move('w');
}
if(e.getKeyChar()!='m') {
			Map.p.move(e.getKeyChar());
		}else{
			MouseListener.mouseControlEnabled=!MouseListener.mouseControlEnabled;
		}

	}
                
        

	@Override
	/**
		* Key released does nothing
		*/
	public void keyReleased(KeyEvent e) {
	}
	
}
