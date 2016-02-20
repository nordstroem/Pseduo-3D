package renderer;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Usage:
 * Create input object with JFrame as argument
 * In main loop, call input.update()
 * 
 * keyDown() 		- if the key is pressed
 * keyPressed()		- if the key is pressed current frame but not the previous
 * keyReleased()	- if the key is released current frame but pressed previous
 * 
 * @author Johan with modifications by Oskar
 *
 */
public class Input implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	public final int left = KeyEvent.VK_LEFT;
	public final int right = KeyEvent.VK_RIGHT;
	public final int up = KeyEvent.VK_UP;
	public final int down = KeyEvent.VK_DOWN;
	public final int space = KeyEvent.VK_SPACE;
	public final int escape = KeyEvent.VK_ESCAPE;

	public final int LEFT = KeyEvent.VK_LEFT;
	public final int RIGHT = KeyEvent.VK_RIGHT;
	public final int UP = KeyEvent.VK_UP;
	public final int DOWN = KeyEvent.VK_DOWN;
	public final int SPACE = KeyEvent.VK_SPACE;
	public final int ENTER = KeyEvent.VK_ENTER;
	public final int BACKSPACE = KeyEvent.VK_BACK_SPACE;
	public final int ESCAPE = KeyEvent.VK_ESCAPE;
	public final int SHIFT = KeyEvent.VK_SHIFT;
	public final int CTRL = KeyEvent.VK_CONTROL;

	public final int W = KeyEvent.VK_W;
	public final int A = KeyEvent.VK_A;
	public final int S = KeyEvent.VK_S;
	public final int D = KeyEvent.VK_D;
	public final int I = KeyEvent.VK_I;
	public final int Q = KeyEvent.VK_Q;
	public final int M = KeyEvent.VK_M;
	public final int C = KeyEvent.VK_C;
	public final int R = KeyEvent.VK_R;
	public final int T = KeyEvent.VK_T;
	public final int N = KeyEvent.VK_N;
	public final int J = KeyEvent.VK_J;
	public final int H = KeyEvent.VK_H;
	public final int E = KeyEvent.VK_E;
	public final int Z = KeyEvent.VK_Z;
	public final int X = KeyEvent.VK_X;
	
	
	private boolean[] keys = new boolean[1000];
	private boolean[] previousKeys = new boolean[1000];

	private int keysDown = 0;
	private int mousePositionX;
	private int mousePositionY;

	private boolean mouseLeftDown;
	private boolean mouseRightDown;
	private boolean mouseCenterDown;

	private boolean previousMouseLeftDown;
	private boolean previousMouseRightDown;
	private boolean previousMouseCenterDown;

	private boolean collectMouseLeftDown;
	private boolean collectMouseRightDown;
	private boolean collectMouseCenterDown;

	private boolean mouseWheelMoved;
	private int mouseWheelNotches;

	private boolean decorated;
	private int topDecorationSize = 29;
	private int sideAndBotDecorationSize = 7;

	private char latestTyped;

	public Input(Component c, boolean decorated) {
		c.addMouseListener(this);
		c.addMouseMotionListener(this);
		c.addKeyListener(this);
		c.addMouseWheelListener(this);
		c.setFocusable(true);
		this.decorated = decorated;

	}

	public void changeFocus(Component c) {
		c.requestFocus();
	}

	boolean[] collectKeys = new boolean[1000];

	public synchronized void update() {

		previousKeys = keys.clone();
		keys = collectKeys.clone();

		previousMouseLeftDown = mouseLeftDown;
		previousMouseRightDown = mouseRightDown;
		previousMouseCenterDown = mouseCenterDown;

		mouseLeftDown = collectMouseLeftDown;
		mouseRightDown = collectMouseRightDown;
		mouseCenterDown = collectMouseCenterDown;

	}

	public synchronized void keyPressed(KeyEvent e) {
		collectKeys[e.getKeyCode()] = true;
		keysDown++;

	}

	public synchronized void keyReleased(KeyEvent e) {
		collectKeys[e.getKeyCode()] = false;
		keysDown--;
	}

	public void keyTyped(KeyEvent e) {
		latestTyped = e.getKeyChar();
	}

	public boolean hasLatestTyped() {
		return latestTyped != '\0' && latestTyped >= 32 && latestTyped <= 255;
	}

	public char getLatestTyped() {
		char c = latestTyped;
		latestTyped = '\0';
		return c;
	}

	//TODO Should implement this function
	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public synchronized void mousePressed(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			collectMouseLeftDown = true;
		} else if (e.getButton() == MouseEvent.BUTTON2) {
			collectMouseCenterDown = true;
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			collectMouseRightDown = true;
		}

	}

	public synchronized void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			collectMouseLeftDown = false;
		} else if (e.getButton() == MouseEvent.BUTTON2) {
			collectMouseCenterDown = false;
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			collectMouseRightDown = false;
		}

	}

	public void mouseDragged(MouseEvent e) {
		mousePositionX = e.getX();
		mousePositionY = e.getY();
	}

	public void mouseMoved(MouseEvent e) {
		mousePositionX = e.getX();
		mousePositionY = e.getY();
	}

	public boolean isKeyDown(int k) {
		return keys[k] || previousKeys[k];
	}

	public boolean isKeyPressed(int k) {
		return keys[k] && !previousKeys[k];
	}

	public boolean isKeyReleased(int k) {
		return !keys[k] && previousKeys[k];
	}
	
	public int getMouseX() {
		return mousePositionX - (decorated ? sideAndBotDecorationSize : 0);
	}

	public int getMouseY() {
		return mousePositionY - (decorated ? topDecorationSize : 0);
	}

	public Point getMousePoint() {
		return new Point(getMouseX(), getMouseY());
	}

	public boolean isMouseDown() {
		return mouseLeftDown || mouseRightDown || mouseCenterDown;
	}

	public boolean isMouseLeftDown() {
		return mouseLeftDown;
	}

	public boolean isMouseRightDown() {
		return mouseRightDown;
	}

	public boolean isMouseCenterDown() {
		return mouseCenterDown;
	}

	public boolean isMouseLeftPressed() {
		return mouseLeftDown && !previousMouseLeftDown;
	}

	public boolean isMouseRightPressed() {
		return mouseRightDown && !previousMouseRightDown;
	}

	public boolean isMouseCenterPressed() {
		return mouseCenterDown && !previousMouseCenterDown;
	}

	public boolean isMouseLeftReleased() {
		return !mouseLeftDown && previousMouseLeftDown;
	}

	public boolean isMouseRightReleased() {
		return !mouseRightDown && previousMouseRightDown;
	}

	public boolean isMouseCenterReleased() {
		return !mouseCenterDown && mouseCenterDown;
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		mouseWheelMoved = true;
		mouseWheelNotches = e.getWheelRotation();

	}

	public boolean isMouseWheelMoved() {
		if (mouseWheelMoved) {
			mouseWheelMoved = false;
			mouseWheelNotches = 0;
			return true;
		}
		return false;
	}

	public int getMouseWheelNotches() {
		mouseWheelMoved = false;
		int temp = mouseWheelNotches;
		mouseWheelNotches = 0;
		return temp;

	}

	public boolean isAnyKeyDown() {
		return keysDown > 0;
	}

}
