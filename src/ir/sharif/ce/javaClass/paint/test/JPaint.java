//In The Name Of ALLAH

package ir.sharif.ce.javaClass.paint.test;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EventListener;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.MouseInputListener;

import com.swtdesigner.SwingResourceManager;

/**
 * @author Ruholla Jahande
 */
public class JPaint {

	private enum ToolBarState {
		RECT, OVAL, LINE, PEN
	}

	private boolean mustDraw;
	private final JPanel fillDrawPanel = new JPanel();
	private final JRadioButton fillRadioButton = new JRadioButton("Fill");
	private final JRadioButton drawRadioButton = new JRadioButton("Draw");
	private File currentFile = null;
	private final JPanel resizerPanel = new JPanel();
	private Rectangle dinamicDraw = new Rectangle();
	private Graphics2D imageGra;// = null;
	private JPanel showColorPanel = new JPanel();
	private int paintingColor = 0;
	private BufferedImage board;
	private Icon[][] tbIcons;
	private AbstractButton[] colorButtons = new AbstractButton[16];
	private ToolBarState tbs = ToolBarState.values()[0];
	private AbstractButton[] tbButtons = new JToggleButton[ToolBarState
			.values().length];

	private JFrame frame;

	private final JPanel tbpanel = new JPanel();

	private final JMenuBar menuBar = new JMenuBar();
	private final JMenu fileMenu = new JMenu();
	private final JMenu helpMenu = new JMenu("Help");
	private final JMenuItem aboutMenuItem = new JMenuItem("About JPaint 1.0");
	private final JMenuItem newItemMenuItem = new JMenuItem();
	private DinamicPanel bufferImagePanel = new DinamicPanel();
	private final JPanel colorsPanel = new JPanel();

	private final JMenuItem quickSaveMenuItem = new JMenuItem();

	private final JMenuItem LoadMenuItem = new JMenuItem();

	private final JMenuItem newMenuItem = new JMenuItem();

	private final JMenuItem exitMenuItem = new JMenuItem();

	/**
	 * Launch the application
	 * 
	 * @param args
	 */
	public static void main(String args[]) {

		try {
			JPaint window = new JPaint();
		} catch (Exception e) {
		}
	}

	/**
	 * Create the application
	 */
	public JPaint() {
		tbIcons = new Icon[2][tbButtons.length];
		jbInit();
	}

	/**
	 * Initialize the contents of the frame
	 */
	private void jbInit() {
		createFrame();
		organizeToolBar();

		tbpanel.setBackground(new Color(208, 221, 251));

		menuBar.add(fileMenu);
		fileMenu.setMnemonic(KeyEvent.VK_F);
		fileMenu.setText("File");

		fileMenu.add(newMenuItem);
		newMenuItem.addActionListener(new NewMenuItemActionListener());
		newMenuItem.setText("New");

		fileMenu.add(newItemMenuItem);
		newItemMenuItem.addActionListener(new SaveAsMenuItemActionListener());
		newItemMenuItem.setText("Save as");

		fileMenu.add(quickSaveMenuItem);
		quickSaveMenuItem.setMnemonic(KeyEvent.VK_F4);
		quickSaveMenuItem
				.addActionListener(new QuickSaveMenuItemActionListener());
		quickSaveMenuItem.setText("Save");

		fileMenu.add(LoadMenuItem);
		LoadMenuItem.addActionListener(new LoadMenuItemActionListener());
		LoadMenuItem.setText("Load");

		fileMenu.add(exitMenuItem);
		exitMenuItem.addActionListener(new ExitMenuItemActionListener());
		exitMenuItem.setText("Exit");

		// #################

		organizeHelpMenu();
		organizeColorButtons();
		organizeFillDrawPanel();
		addsToFrame();
		organizeImagePanel();
		getFrame().setVisible(true);
		organizeResizerLabel();
	}

	private void organizeFillDrawPanel() {
		Color bgc = this.tbpanel.getBackground();

		this.fillDrawPanel.setBounds(this.tbpanel.getX(), this.tbpanel.getY()
				+ this.tbpanel.getHeight() + 10, 62, 45);
		this.fillDrawPanel.setLayout(null);
		this.fillRadioButton.setBounds(1, 5, 60, 15);
		this.drawRadioButton.setBounds(1, 25, 60, 15);
		this.drawRadioButton.setBackground(bgc);
		this.fillDrawPanel.setBackground(bgc);
		this.fillRadioButton.setBackground(bgc);
		this.fillDrawPanel.add(this.fillRadioButton);
		this.fillDrawPanel.add(this.drawRadioButton);
		this.fillDrawPanel.setBorder(this.tbpanel.getBorder());
		this.fillRadioButton.setSelected(true);
		FillDrawActionListenner fdal = new FillDrawActionListenner();
		this.fillRadioButton.addActionListener(fdal);
		this.drawRadioButton.addActionListener(fdal);

	}

	private class FillDrawActionListenner implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == JPaint.this.fillRadioButton) {
				mustDraw = false;
			} else {
				mustDraw = true;
			}
			fillRadioButton.setSelected(!mustDraw);
			drawRadioButton.setSelected(mustDraw);
		}

	}

	private void organizeHelpMenu() {

		menuBar.add(this.helpMenu);
		this.helpMenu.setMnemonic(KeyEvent.VK_H);

		this.helpMenu.add(aboutMenuItem);
		this.aboutMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JTextPane t = new JTextPane();
				t
						.setText("In The Name Of ALLAH\n\n\nJPaint 1.0 with Swing\n\nBy:\nRouholla Jahande\n\nSharif University Of Technology\nComputer Engineering Department\n\n10-4-1389");
				JOptionPane.showMessageDialog(getFrame(), t,
						"About JPaint 1.0", JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}

	private JPanel getResizerPanel() {
		return resizerPanel;
	}

	private void organizeResizerLabel() {
		JLabel t = new JLabel("Drag to resize");
		getResizerPanel().setLayout(null);
		getResizerPanel()
				.setBounds(
						getBufferImagePanel().getX()
								+ getBufferImagePanel().getWidth(),
						getBufferImagePanel().getY()
								+ getBufferImagePanel().getHeight(), 80, 20);

		t.setBounds(0, 0, 80, 20);
		getResizerPanel().add(t);
		getResizerPanel().setBackground(Color.GREEN);

		getFrame().getContentPane().add(getResizerPanel());

	}

	private void addsToFrame() {
		this.getFrame().getContentPane().add(this.colorsPanel);
		frame.getContentPane().add(bufferImagePanel);

		this.frame.getContentPane().add(tbpanel);

		frame.setJMenuBar(menuBar);
		getFrame().add(this.fillDrawPanel);
		getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

	}

	private void createFrame() {
		frame = new JFrame();
		DrawingMouseListenner r = new DrawingMouseListenner();
		frame.getContentPane().addMouseListener(r);

		frame.getContentPane().addMouseMotionListener(r);
		frame.getContentPane().setLayout(null);
		frame.setBounds(10, 10, 791, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getFrame().getContentPane().setBackground(new Color(201, 211, 244));

		getFrame().addWindowListener(new AskWindowActionListenner());
		getFrame().addComponentListener(new ResizerListenner());
		getFrame().setTitle("JPaint 1.0 - Untitled");

	}

	private class AskWindowActionListenner implements WindowListener {
		public void windowActivated(WindowEvent e) {
		}

		public void windowClosed(WindowEvent e) {
		}

		public void windowClosing(WindowEvent e) {
			exitProgram();

		}

		public void windowDeactivated(WindowEvent e) {
		}

		public void windowDeiconified(WindowEvent e) {
		}

		public void windowIconified(WindowEvent e) {
		}

		public void windowOpened(WindowEvent e) {
		}
	}

	private AbstractButton getColorButtons(int index) {
		return colorButtons[index];
	}

	private void exitProgram() {
		JLabel l = new JLabel("Are you sure to exit?");
		l.setBounds(0, 0, 40, 20);
		int res = JOptionPane.showConfirmDialog(getFrame(), l, "Quiet Paint",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (res == JOptionPane.YES_OPTION) {
			System.exit(0);
		}
	}

	private void setColorButtons(AbstractButton colorButtons, int index) {
		this.colorButtons[index] = colorButtons;
	}

	private class ResizerListenner implements ComponentListener {

		public void componentHidden(ComponentEvent e) {
		}

		public void componentMoved(ComponentEvent e) {
		}

		public void componentResized(ComponentEvent e) {
			refreshPositions();
		}

		public void componentShown(ComponentEvent e) {
		}
	}

	private void organizeColorButtons() {

		JPopupMenu popUpTemp;
		JMenuItem menuItemTemp;
		for (int i = 0; i < this.colorButtons.length; i++) {
			setColorButtons(new JButton(""), i);
			getColorButtons(i).setBounds(25 * i + 70, 20, 20, 20);
			getColorButtons(i).setBackground(Utils.getRandomColor());
			getColorButtons(i).setSelected(false);
			getColorButtons(i).addActionListener(
					new ColorButtonsActionListenner(i));
			this.colorsPanel.add(getColorButtons(i));
			// adding pop up menu
			popUpTemp = new JPopupMenu();
			menuItemTemp = new JMenuItem("Set color");
			menuItemTemp.addActionListener(new SetColorActionListenner(
					getColorButtons(i)));
			popUpTemp.add(menuItemTemp);
			addPopup(getColorButtons(i), popUpTemp);

		}

		this.colorsPanel.setBorder(new TitledBorder("Select Color"));
		colorsPanel.setLayout(null);
		this.getColorButtons(0).setSelected(true);

		this.showColorPanel.setBounds(10, 30, 50, 50);
		this.showColorPanel.setBackground(this.getColorButtons(0)
				.getBackground());
		// this.colorsPanel.setBounds(10, this.getFrame().getHeight() ,
		// this.colorButtons.length * 25 + this.showColorPanel.getWidth()
		// + 50, 100);
		this.colorsPanel.add(this.showColorPanel);
		this.colorsPanel.setBackground(new Color(220, 221, 248));
	}

	private void refreshPositions() {
		JPaint.this.colorsPanel.setBounds(10, getFrame().getHeight() - 170,
				JPaint.this.colorButtons.length * 25
						+ JPaint.this.showColorPanel.getWidth() + 50, 100);
		getResizerPanel()
				.setLocation(
						getBufferImagePanel().getX()
								+ getBufferImagePanel().getWidth(),
						getBufferImagePanel().getY()
								+ getBufferImagePanel().getHeight());
	}

	private class SetColorActionListenner implements ActionListener {

		private AbstractButton b;

		public SetColorActionListenner(AbstractButton in) {
			this.b = in;
		}

		public void actionPerformed(ActionEvent e) {
			this.b.setBackground(JColorChooser.showDialog(this.b, "Set color",
					b.getBackground()));
		}

	}

	private void organizeToolBar() {
		setTbIcons();
		organizeTBPanel();

		tbButtons[0] = new JToggleButton();
		tbButtons[0].setIcon(tbIcons[1][0]);
		tbButtons[0].setSelected(true);
		tbButtons[0].setBounds(10, 10, 26, 26);
		tbButtons[0].setText("");
		this.tbpanel.add(tbButtons[0]);
		tbButtons[0].addActionListener(new ToolBarActionListenner(0));
		tbButtons[0].setBackground(Color.WHITE);

		for (int i = 1; i < tbButtons.length; i++) {
			this.tbpanel.add(tbButtons[0]);
			tbButtons[i] = new JToggleButton();
			tbButtons[i].setIcon(tbIcons[0][i]);
			tbButtons[i].setSelected(false);
			tbButtons[i].setBounds(10, 10 + i * 50, 26, 26);
			tbButtons[i].setText("");
			this.tbpanel.add(tbButtons[i]);
			tbButtons[i].addActionListener(new ToolBarActionListenner(i));
			tbButtons[i].setBackground(new Color(222, 184, 135));
		}
	}

	private void organizeTBPanel() {

		tbpanel.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLUE));
		tbpanel.setBackground(new Color(208, 221, 253));
		tbpanel.setLayout(null);
		tbpanel.setBounds(10, 42, 49, 10 + 50 * tbButtons.length);
	}

	private JFrame getFrame() {
		return this.frame;
	}

	private void setTbIcons() {

		this.tbIcons[0][0] = SwingResourceManager.getIcon(JPaint.class,
				"/resources/toolBarsShapes/rectFill_OFF.gif");
		this.tbIcons[1][0] = SwingResourceManager.getIcon(JPaint.class,
				"/resources/toolBarsShapes/rectFill_ON.gif");

		this.tbIcons[0][1] = SwingResourceManager.getIcon(JPaint.class,
				"/resources/toolBarsShapes/ovalFill_OFF.gif");
		this.tbIcons[1][1] = SwingResourceManager.getIcon(JPaint.class,
				"/resources/toolBarsShapes/ovalFill_ON.gif");

		this.tbIcons[0][2] = SwingResourceManager.getIcon(JPaint.class,
				"/resources/toolBarsShapes/line_OFF.gif");
		this.tbIcons[1][2] = SwingResourceManager.getIcon(JPaint.class,
				"/resources/toolBarsShapes/line_ON.gif");

		this.tbIcons[0][3] = SwingResourceManager.getIcon(JPaint.class,
				"/resources/toolBarsShapes/pen_OFF.gif");
		this.tbIcons[1][3] = SwingResourceManager.getIcon(JPaint.class,
				"/resources/toolBarsShapes/pen_ON.gif");

	}

	private void organizeImagePanel() {

		bufferImagePanel.setBackground(Color.WHITE);
		bufferImagePanel.setLayout(null);
		bufferImagePanel.setBounds(100, 10, 500, 400);

		this.board = new BufferedImage(this.bufferImagePanel.getWidth(),
				this.bufferImagePanel.getHeight(), BufferedImage.TYPE_INT_RGB);

		this.imageGra = this.board.createGraphics();
		getImageGra().setColor(getBufferImagePanel().getBackground());
		getImageGra().fillRect(0, 0, getFrame().getWidth(),
				getFrame().getHeight());
	}

	private class SaveAsMenuItemActionListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			SaveAsItemMenuItemActionPerformed();
		}

	}

	private File getCurrentFile() {
		return this.currentFile;
	}

	private void setCurrentFile(File currentFile) {
		this.currentFile = currentFile;
	}

	private void SaveAsItemMenuItemActionPerformed() {
		JFileChooser f;
		if (getCurrentFile() == null) {
			f = new JFileChooser();
		} else {
			f = new JFileChooser(getCurrentFile());
		}
		for (JPaintFileFilter i : JPaintFileFilter.getImageInstances()) {
			f.setFileFilter(i);
		}
		if (JFileChooser.APPROVE_OPTION == f.showSaveDialog(getFrame())) {

			String path = f.getSelectedFile().getAbsolutePath();
			String format = Utils.getFormat(path);
			if (format.equals(path)) {
				format = f.getFileFilter().toString();
				path = path + '.' + format;
			}
			getImageGra().dispose();
			setCurrentFile(new File(path));

			try {
				ImageIO.write(getBoard(), format, getCurrentFile());
			} catch (IOException e) {
				JOptionPane.showMessageDialog(getFrame(), new JLabel(
						"Error occured during saving file: " + path
								+ " => reason: " + e.getMessage()),
						"Can Not Save", JOptionPane.ERROR_MESSAGE);
			} catch (Exception e) {

			} finally {

			}

			setImageGra(getBoard().createGraphics());
			getFrame().setTitle("JPaint 1.0 - " + path);
		}
	}

	/**
	 * WindowBuilder generated method.<br>
	 * Please don't remove this method or its invocations.<br>
	 * It used by WindowBuilder to associate the {@link javax.swing.JPopupMenu}
	 * with parent.
	 */

	private class ToolBarActionListenner implements ActionListener {

		private int index;

		public ToolBarActionListenner(int button) {
			super();
			this.index = button;
		}

		public void actionPerformed(ActionEvent e) {
			if (tbs.ordinal() == this.index) {
				tbButtons[index].setSelected(true);
			} else {
				tbButtons[JPaint.this.tbs.ordinal()].setSelected(false);
				tbButtons[JPaint.this.tbs.ordinal()]
						.setIcon(tbIcons[0][JPaint.this.tbs.ordinal()]);
				tbButtons[JPaint.this.tbs.ordinal()].setBackground(new Color(
						222, 184, 135));

				JPaint.this.tbs = ToolBarState.values()[index];

				tbButtons[index].setSelected(true);
				tbButtons[index].setIcon(tbIcons[1][index]);
				tbButtons[index].setBackground(Color.WHITE);
			}
			getImageGra().setColor(getPaintingColor());
		}
	}

	private class DrawingMouseListenner implements MouseWheelListener,
			EventListener, MouseInputListener {

		private boolean dragging = false;
		private boolean resizingImage = false;
		private MouseEvent lastEvent;

		public void mouseClicked(MouseEvent e) {
		}

		public void mouseEntered(MouseEvent e) {
		}

		public void mouseExited(MouseEvent e) {
		}

		public void mousePressed(MouseEvent e) {
			if (isInComponent(getBufferImagePanel(), e)) {
				this.dragging = true;
				this.lastEvent = e;
			} else if (isInComponent(getResizerPanel(), e)) {
				this.resizingImage = true;
				this.dragging = false;
				this.lastEvent = e;
			}
		}

		public void mouseReleased(MouseEvent e) {

			if (dragging) {

				int x = this.lastEvent.getX()
						- JPaint.this.bufferImagePanel.getX();
				int y = this.lastEvent.getY()
						- JPaint.this.bufferImagePanel.getY();
				int width = e.getX() - this.lastEvent.getX();
				int height = e.getY() - this.lastEvent.getY();
				setDinamicDraw(x, y, width, height);

				getImageGra().setColor(getPaintingColor());
				finalDraw(getImageGra());

				this.dragging = false;
			} else if (this.resizingImage) {
				setDinamicDraw(0, 0, 0, 0);
				this.resizingImage = false;
				int width = e.getX() - getLastEvent().getX()
						+ getBufferImagePanel().getWidth();
				int height = e.getY() - getLastEvent().getY()
						+ getBufferImagePanel().getHeight();

				getImageGra().dispose();

				getBufferImagePanel().setSize(width, height);
				refreshPositions();

				getBufferImagePanel().repaint();

				// creating a new image buffer
				BufferedImage bufTemp = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_RGB);
				Graphics2D gTemp = bufTemp.createGraphics();
				gTemp.setBackground(Color.WHITE);
				gTemp.setColor(Color.WHITE);
				gTemp.fillRect(0, 0, width, height);

				if (width > getBoard().getWidth()
						|| height > getBoard().getHeight()) {

					if (width < getBoard().getWidth()) {
						gTemp.drawImage(getBoard(), 0, 0, width, getBoard()
								.getHeight(), 0, 0, width, getBoard()
								.getHeight(), null);
					} else if (height < getBoard().getHeight()) {
						gTemp.drawImage(getBoard(), 0, 0,
								getBoard().getWidth(), height, 0, 0, getBoard()
										.getWidth(), height, null);
					} else {
						gTemp.drawImage(getBoard(), 0, 0,
								getBoard().getWidth(), getBoard().getHeight(),
								0, 0, getBoard().getWidth(), getBoard()
										.getHeight(), null);
					}

				} else {
					gTemp.drawImage(getBoard(), 0, 0, width, height, 0, 0,
							width, height, null);
				}
				setBoard(bufTemp);
				setImageGra(gTemp);

				getBufferImagePanel().repaint();

			}
		}

		private MouseEvent getLastEvent() {
			return lastEvent;
		}

		public void mouseDragged(MouseEvent e) {
			if (dragging) {

				int x = this.lastEvent.getX() - getBufferImagePanel().getX();
				int y = this.lastEvent.getY()
						- JPaint.this.bufferImagePanel.getY();
				int width = e.getX() - this.lastEvent.getX();
				int height = e.getY() - this.lastEvent.getY();

				setDinamicDraw(x, y, width, height);
				if (getDrawingShape() == ToolBarState.PEN) {
					this.lastEvent = e;
					finalDraw(getImageGra());
				}
				getBufferImagePanel().repaint();
			}
		}

		// look at its doc
		public void mouseMoved(MouseEvent e) {
		}

		public void mouseWheelMoved(MouseWheelEvent e) {
		}
	}

	private class ColorButtonsActionListenner implements ActionListener {

		private int index = 0;

		public ColorButtonsActionListenner(int index) {
			super();
			this.index = index;
		}

		public void actionPerformed(ActionEvent e) {
			if (JPaint.this.paintingColor == this.index) {
				JPaint.this.colorButtons[index].setSelected(true);
			} else {
				JPaint.this.colorButtons[JPaint.this.paintingColor]
						.setSelected(false);

				JPaint.this.paintingColor = index;

				JPaint.this.colorButtons[index].setSelected(true);
				JPaint.this.showColorPanel
						.setBackground(JPaint.this.colorButtons[index]
								.getBackground());
			}
			getImageGra().setColor(getPaintingColor());
		}
	}

	private class QuickSaveMenuItemActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			quickSaveMenuItemActionPerformed();
		}
	}

	private class LoadMenuItemActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			loadMenuItemActionPerformed();
		}
	}

	private class NewMenuItemActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			newMenuItemActionPerformed();
		}
	}

	private class ExitMenuItemActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			exitProgram();
		}
	}

	private Color getPaintingColor() {
		return JPaint.this.colorButtons[paintingColor].getBackground();
	}

	private Graphics2D getImageGra() {
		return imageGra;
	}

	private ToolBarState getDrawingShape() {
		return this.tbs;
	}

	@SuppressWarnings("serial")
	private class DinamicPanel extends JPanel {
		public DinamicPanel() {
		}

		@Override
		public void paint(Graphics g) {
			super.paint(g);
			g.setColor(getPaintingColor());
			g.drawImage(getBoard(), 0, 0, null);

			finalDraw(g);
		}
	}

	private void finalDraw(Graphics g) {
		int[] p = { getDinamicDraw().x, getDinamicDraw().y,
				getDinamicDraw().width, getDinamicDraw().height };

		switch (getDrawingShape()) {
		case RECT:

			if (p[2] < 0) {
				p[2] = -p[2];
				p[0] -= p[2];
			}
			if (p[3] < 0) {
				p[3] = -p[3];
				p[1] -= p[3];
			}

			if (mustDraw) {
				g.drawRect(p[0], p[1], p[2], p[3]);
			} else {
				g.fillRect(p[0], p[1], p[2], p[3]);
			}
			break;
		case OVAL:
			if (p[2] < 0) {
				p[2] = -p[2];
				p[0] -= p[2];
			}
			if (p[3] < 0) {
				p[3] = -p[3];
				p[1] -= p[3];
			}
			if (mustDraw) {
				g.drawOval(p[0], p[1], p[2], p[3]);

			} else {
				g.fillOval(p[0], p[1], p[2], p[3]);
			}
			break;
		case LINE:
			g.drawLine(p[0], p[1], p[0] + p[2], p[1] + p[3]);
			break;
		case PEN:
			g.drawLine(p[0], p[1], p[0] + p[2], p[1] + p[3]);
			break;
		default:
			break;
		}
	}

	private Rectangle getDinamicDraw() {
		return dinamicDraw;
	}

	private void setDinamicDraw(int x, int y, int width, int height) {
		this.dinamicDraw.setRect(x, y, width, height);
	}

	private DinamicPanel getBufferImagePanel() {
		return bufferImagePanel;
	}

	private BufferedImage getBoard() {
		return board;
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger())
					showMenu(e);
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger())
					showMenu(e);
			}

			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	private void setImageGra(Graphics2D imageGra) {
		this.imageGra = imageGra;
	}

	private void quickSaveMenuItemActionPerformed() {
		if (getCurrentFile() == null) {
			SaveAsItemMenuItemActionPerformed();
			return;
		}
		getImageGra().dispose();

		String path = getCurrentFile().getAbsolutePath();

		try {
			ImageIO
					.write(getBoard(), path
							.substring(path.lastIndexOf('.') + 1),
							getCurrentFile());
		} catch (IOException exc) {
			JOptionPane.showMessageDialog(getFrame(), new JLabel(
					"Error occured during saving file: " + path
							+ " => reason: " + exc.getMessage()),
					"Can Not Save", JOptionPane.ERROR_MESSAGE);
		} finally {

		}

		setImageGra(getBoard().createGraphics());
	}

	private void setBoard(BufferedImage board) {
		this.board = board;
	}

	private void loadMenuItemActionPerformed() {
		JFileChooser j;
		try {
			j = new JFileChooser(getCurrentFile());
		} catch (Exception exc) {
			j = new JFileChooser();
		}
		for (JPaintFileFilter i : JPaintFileFilter.getImageInstances()) {
			j.setFileFilter(i);
		}
		if (JFileChooser.APPROVE_OPTION == j.showOpenDialog(getFrame())) {
			File tempFile = j.getSelectedFile();
			BufferedImage bufTemp;
			try {
				bufTemp = ImageIO.read(tempFile);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(getFrame(), new JLabel(
						"Error occured during loading file: "
								+ tempFile.getAbsolutePath() + " => reason: "
								+ e1.getMessage()), "Can Not Load",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			getImageGra().dispose();
			getBufferImagePanel().setSize(bufTemp.getWidth(),
					bufTemp.getHeight());
			refreshPositions();
			setBoard(bufTemp);
			setCurrentFile(tempFile);
			setImageGra(getBoard().createGraphics());

			setDinamicDraw(0, 0, 0, 0);

			getFrame().repaint();
			getBufferImagePanel().repaint();
			getFrame().setTitle("JPaint 1.0 - " + tempFile.getAbsolutePath());
		}
	}

	private static boolean isInComponent(Component c, MouseEvent e) {
		if (c.getX() < e.getX() && c.getY() < e.getY()
				&& c.getX() + c.getWidth() > e.getX()
				&& c.getY() + c.getHeight() > e.getY()) {
			return true;
		} else {
			return false;
		}
	}

	private void newMenuItemActionPerformed() {
		int x = JOptionPane.showConfirmDialog(getFrame(), new JLabel(
				"Do you want to save before that?"), "New",
				JOptionPane.YES_NO_CANCEL_OPTION);
		if (x != JOptionPane.CANCEL_OPTION) {

			if (x == JOptionPane.OK_OPTION) {
				SaveAsItemMenuItemActionPerformed();
			}

			setDinamicDraw(0, 0, 0, 0);
			BufferedImage bTemp = new BufferedImage(getBoard().getWidth(),
					getBoard().getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D gTemp = bTemp.createGraphics();
			gTemp.setColor(Color.WHITE);
			gTemp.fillRect(0, 0, bTemp.getWidth(), bTemp.getHeight());
			setBoard(bTemp);
			setImageGra(gTemp);
			getBufferImagePanel().repaint();
		}
	}
}
