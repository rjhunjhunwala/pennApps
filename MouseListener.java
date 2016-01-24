/**
	* This is used to listen to the character moves typed in by the user
	*/
package zombieMaze;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.event.MouseInputListener;

/**
 *
 * @author rohan
 */
public class MouseListener implements MouseInputListener,MouseMotionListener{
	public static boolean mouseControlEnabled=true;
	@Override
	public void mouseClicked(MouseEvent e) {
		if(mouseControlEnabled) {
			Map.p.shoot();
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
			if(mouseControlEnabled) {
				Map.p.angle+=Math.atan(((e.getX()-450)/5000.0)/.2);
			}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}
}
