package swingcomponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.Serializable;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

public class CheckBoxDemo extends JPanel implements ItemListener, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	JCheckBox chinButton;
	JCheckBox glassesButton;
	JCheckBox hairButton;
	JCheckBox teethButton;
	
	StringBuffer choices;
	JLabel pictureLabel;
	
	public CheckBoxDemo() {
		super(new BorderLayout());
		
		chinButton = new JCheckBox("Chin");
		chinButton.setMnemonic(KeyEvent.VK_C);
		chinButton.setSelected(true);
		
		glassesButton = new JCheckBox("Glasses");
		glassesButton.setMnemonic(KeyEvent.VK_G);
		glassesButton.setSelected(true);
		
		hairButton = new JCheckBox("Hair");
		hairButton.setMnemonic(KeyEvent.VK_H);
		hairButton.setSelected(true);
		
		teethButton = new JCheckBox("Teeth");
		teethButton.setMnemonic(KeyEvent.VK_T);
		teethButton.setSelected(true);
		teethButton.setFocusable(false);
		teethButton.setBackground(new Color(30, 30, 30));
		teethButton.setForeground(new Color(150, 150, 150));
		teethButton.setPreferredSize(new Dimension(200, 50));
		teethButton.setFont(new Font("Arial", Font.BOLD, 16));
		teethButton.setComponentPopupMenu(new JPopupMenu());
		teethButton.setToolTipText("Test tooltip");
		teethButton.setHorizontalAlignment(SwingConstants.CENTER);

		Border redBorder = BorderFactory.createLineBorder(Color.red);
		
		teethButton.setBorder(redBorder);
		
		chinButton.addItemListener(this);
		glassesButton.addItemListener(this);
		hairButton.addItemListener(this);
		teethButton.addItemListener(this);
		
		choices = new StringBuffer("cght");
		
		pictureLabel = new JLabel();
		pictureLabel.setFont(pictureLabel.getFont().deriveFont(Font.ITALIC));
		//updatePicture();
		
		JPanel checkPanel = new JPanel(new GridLayout(0, 1));
		checkPanel.add(chinButton);
		checkPanel.add(glassesButton);
		checkPanel.add(hairButton);
		checkPanel.add(teethButton);
		
		this.add(checkPanel, BorderLayout.LINE_START);
		this.add(pictureLabel, BorderLayout.CENTER);
		this.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		int index = 0;
		char c = '-';
		Object source = e.getItemSelectable();
		
		if (source == chinButton) {
			index = 0;
			c = 'c';
		} else if (source == glassesButton) {
			index = 1;
			c = 'g';
		} else if (source == hairButton) {
			index = 2;
			c = 'h';
		} else if (source == teethButton) {
			index = 3;
			c = 't';
		}
		
		if (e.getStateChange() == ItemEvent.DESELECTED) {
			c = '-';
		}
		
		choices.setCharAt(index, c);
		
		updatePicture();
	}
	
	protected void updatePicture() {
		ImageIcon icon = createImageIcon(
				"image.jpg"
				+ choices.toString()
				+ ".gif");
		pictureLabel.setIcon(icon);
		pictureLabel.setToolTipText(choices.toString());
		
		if (icon == null) {
			pictureLabel.setText("Missing icon");
		} else {
			pictureLabel.setText(null);
		}
	}
	
	protected static ImageIcon createImageIcon(String path) {
		URL imgURL = CheckBoxDemo.class.getResource(path); {
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find tile: " + path);
			return null;
		}}
	}
	
	private static void createAndShowGUI() {
		JFrame frame = new JFrame("Checkbox demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		JComponent newContentPane = new CheckBoxDemo();
		newContentPane.setOpaque(true);
		frame.setContentPane(newContentPane);
		
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

}
