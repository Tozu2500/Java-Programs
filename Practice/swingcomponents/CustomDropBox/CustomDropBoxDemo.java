package com.tozu.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class CustomDropBoxDemo {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new CustomDropBoxDemo().createUI();
		});
	}
	
	private void createUI() {
		JFrame frame = new JFrame("Custom dropbox demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		frame.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 40));
		
		String[] items = {"Tozu", "Finland", "Nordic region", "Custom UI", "Swing love"};
		
		JComboBox<String> combo = new JComboBox<>(items);
		
		// Custom renderer
		combo.setRenderer(new CustomRenderer());
		
		combo.setUI(new CustomComboUI());
		
		combo.setPreferredSize(new Dimension(200, 35));
		
		frame.add(combo);
		frame.setVisible(true);
		
	}

	static class CustomRenderer extends JLabel implements ListCellRenderer<String> {
		
		public CustomRenderer() {
			setOpaque(true);
			setBorder(new EmptyBorder(5, 10, 5, 10));
		}
		
		@Override
		public Component getListCellRendererComponent(
				JList<? extends String> list,
				String value,
				int index,
				boolean isSelected,
				boolean hasCellFocus) {
			
			setText(value);
			
			if (isSelected) {
				setBackground(new Color(70, 130, 180));
				setForeground(Color.WHITE);
			} else {
				setBackground(Color.WHITE);
				setForeground(Color.BLACK);
			}
			
		return this;
	}
}

	static class CustomComboUI extends BasicComboBoxUI {
		
		@Override
		protected JButton createArrowButton() {
			JButton arrow = new JButton("▼");
			arrow.setBorder(null);
			arrow.setFocusable(false);
			arrow.setBackground(new Color(230, 230, 230));
			arrow.setForeground(Color.black);
			
			return arrow;
		}
		
		@Override
		public void installUI(JComponent c) {
			super.installUI(c);
			comboBox.setBorder(BorderFactory.createLineBorder(new Color(120, 120, 120)));
			comboBox.setBackground(Color.WHITE);
		}
	}
}

























