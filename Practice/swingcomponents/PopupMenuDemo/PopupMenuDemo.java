package swingcomponents.PopupMenuDemo;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class PopupMenuDemo implements ActionListener, ItemListener {
	
	JTextArea output;
	JScrollPane scrollPane;
	String newline = "\n";
	
	public JMenuBar createMenuBar() {
		JMenuBar menuBar;
		JMenu menu, submenu;
		JMenuItem menuItem;
		JRadioButtonMenuItem rbMenuItem;
		JCheckBoxMenuItem cbMenuItem;
		
		menuBar = new JMenuBar();
		
		menu = new JMenu("A menu");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription(
				"The only menu in this program that has menu items");
		menuBar.add(menu);
		
		menuItem = new JMenuItem("A text-only menu item", KeyEvent.VK_T);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_1, ActionEvent.ALT_MASK));
		menuItem.getAccessibleContext().setAccessibleDescription(
				"This doesn't really do anything");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		menu.addSeparator();
		ButtonGroup group = new ButtonGroup();
		
		rbMenuItem = new JRadioButtonMenuItem("A radio button menu item");
		rbMenuItem.setSelected(true);
		rbMenuItem.setMnemonic(KeyEvent.VK_R);
		group.add(rbMenuItem);
		rbMenuItem.addActionListener(this);
		menu.add(rbMenuItem);
		
		rbMenuItem = new JRadioButtonMenuItem("Another one");
		rbMenuItem.setMnemonic(KeyEvent.VK_O);
		group.add(rbMenuItem);
		rbMenuItem.addActionListener(this);
		menu.add(rbMenuItem);
		
		menu.addSeparator();
		cbMenuItem = new JCheckBoxMenuItem("A check box menu item");
		cbMenuItem.setMnemonic(KeyEvent.VK_C);
		cbMenuItem.addItemListener(this);
		menu.add(cbMenuItem);
		
		cbMenuItem = new JCheckBoxMenuItem("Another one");
		cbMenuItem.setMnemonic(KeyEvent.VK_C);
		cbMenuItem.addItemListener(this);
		menu.add(cbMenuItem);
		
		menu.addSeparator();
		submenu = new JMenu("A submenu");
		submenu.setMnemonic(KeyEvent.VK_H);
		
		menuItem = new JMenuItem("An item in the submenu");
		menuItem.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_2, ActionEvent.ALT_MASK));
		menuItem.addActionListener(this);
		submenu.add(menuItem);
		
		menuItem = new JMenuItem("Another item");
		menuItem.addActionListener(this);
		submenu.add(menuItem);
		menu.add(submenu);
		
		menu = new JMenu("Another Menu");
		menu.setMnemonic(KeyEvent.VK_N);
		menu.getAccessibleContext().setAccessibleDescription(
				"This menu does nothing");
		menuBar.add(menu);
		
		return menuBar;
	}
	
	public Container createContentPane() {
		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.setOpaque(true);
		
		output = new JTextArea(5, 30);
		output.setEditable(false);
		scrollPane = new JScrollPane(output);
	
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		return contentPane;
	}
	
	public void createPopupMenu() {
		JMenuItem menuItem;
		
		JPopupMenu popup = new JPopupMenu();
		menuItem = new JMenuItem("A popup menu item");
		menuItem.addActionListener(this);
		popup.add(menuItem);
		menuItem = new JMenuItem("Another popup menu item");
		menuItem.addActionListener(this);
		popup.add(menuItem);
		
		MouseListener popupListener = new PopupListener(popup);
		output.addMouseListener(popupListener);
	}
	
	public void actionPerformed1(ActionEvent e) {
		JMenuItem source = (JMenuItem)(e.getSource());
		String s = "Action event detected." + newline + "    Event source: " + source.getText() + " (an instance of " + getClassName(source) + ")";
		output.append(s + newline);
		output.setCaretPosition(output.getDocument().getLength());
	}

	public static void main(String[] args) {

	
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}
	
	class PopupListener extends MouseAdapter {
		JPopupMenu popup;
		
		PopupListener(JPopupMenu popupMenu) {
			popup = popupMenu;
		}
		
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}
		
		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}
		
		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				popup.show(e.getComponent(),
						e.getX(), e.getY());
			}
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		JMenuItem source = (JMenuItem)(e.getSource());
		String s = "Item event detected."
				+ newline
				+ "    Event source: " + source.getText()
				+ "  (an instance of " + getClassName(source) + ")"
				+ newline
				+ "    New state: "
				+ ((e.getStateChange() == ItemEvent.SELECTED) ? "selected":"unselected" );
		output.append(s + newline);
		output.setCaretPosition(output.getDocument().getLength());
	}
	
	protected String getClassName(Object o) {
		String classString = o.getClass().getName();
		int dotIndex = classString.lastIndexOf(".");
		return classString.substring(dotIndex++);
	}
	
	private static void createAndShowGUI() {
		JFrame frame = new JFrame("PopupMenuDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		PopupMenuDemo demo = new PopupMenuDemo();
		frame.setJMenuBar(demo.createMenuBar());
		frame.setContentPane(demo.createContentPane());
		
		demo.createPopupMenu();
		
		frame.setSize(450, 260);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}

}
