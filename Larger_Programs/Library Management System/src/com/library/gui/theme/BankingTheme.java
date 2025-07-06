package com.library.gui.theme;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/*
 * Modern banking application theme system
 * Provides consisten styling across custom Swing components
 * 
 * @author Tozu
 * @version 1.0.0
 * */
public class BankingTheme {
	
	// This class needs to work together with (almost) completely custom-made Swing components
	
	// Color Palette
	public static class Colors {
		// Primary colors
		public static final Color PRIMARY_BLUE = new Color(0, 102, 204);
		public static final Color PRIMARY_DARK = new Color(0, 76, 153);
		public static final Color PRIMARY_LIGHT = new Color(51, 153, 255);
		
		// Secondary colors
		public static final Color SECONDARY_GREEN = new Color(34, 139, 34);
		public static final Color SECONDARY_ORANGE = new Color(255, 140, 0);
		public static final Color SECONDARY_RED = new Color(220, 53, 69);
		
		// Neutral colors
		public static final Color BACKGROUND = new Color(248, 249, 250);
		public static final Color SURFACE = Color.white;
		public static final Color SURFACE_ELEVATED = new Color(255, 255, 255);
		public static final Color BORDER = new Color(224, 224, 224);
		public static final Color DIVIDER = new Color(238, 238, 238);
		
		// Text colors
		public static final Color TEXT_PRIMARY = new Color(33, 37, 41);
		public static final Color TEXT_SECONDARY = new Color(188, 117, 125);
		public static final Color TEXT_MUTED = new Color(173, 181, 189);
		public static final Color TEXT_ON_PRIMARY = Color.white;
		
		// Status colors
		public static final Color SUCCESS = new Color(40, 167, 69);
		public static final Color WARNING = new Color(255, 193, 7);
		public static final Color ERROR = new Color(220, 53, 69);
		public static  final Color INFO = new Color(23, 162, 184);
		
		// Hover states
		public static final Color HOVER_LIGHT = new Color(0, 0, 0, 10);
		public static final Color HOVER_DARK = new Color(0, 0, 0, 20);
	}
	
	// Typography
	public static class Typography {
		public static final Font HEADER_LARGE = new Font("Segoe UI", Font.BOLD, 24);
		public static final Font HEADER_MEDIUM = new Font("Segoe UI", Font.BOLD, 20);
		public static final Font HEADER_SMALL = new Font("Segoe UI", Font.BOLD, 16);
		public static final Font BODY_LARGE = new Font("Segoe UI", Font.PLAIN, 16);
		public static final Font BODY_MEDIUM = new Font("Segoe UI", Font.PLAIN, 14);
		public static final Font BODY_SMALL = new Font("Segoe UI", Font.PLAIN, 12);
		public static final Font CAPTION = new Font("Segoe UI", Font.PLAIN, 11);
		public static final Font BUTTON = new Font("Segoe UI", Font.BOLD, 14);
		public static final Font MONOSPACE = new Font("Consolas", Font.PLAIN, 14);
	}

	// Spacing
	public static class Spacing {
		
	}
	
	// Border radius
	public static class BorderRadius {
		
	}
	
	// Shadows
	public static class Shadows {
		public static final Color SHADOW_COLOR = new Color(0, 0, 0, 15);
		public static final Color SHADOW_DARK = new Color(0, 0, 0, 25);
		
		public static void drawShadow(Graphics2D g2d, Shape shape, int offsetX, int offsetY, int blur) {
			Graphics2D shadow = (Graphics2D) g2d.create();
			shadow.setColor(SHADOW_COLOR);
			shadow.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			
			// Simple shadow effect
			shadow.translate(offsetX, offsetY);
			shadow.fill(shape);
			shadow.dispose();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private JPanel createFooter() {
		JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
		footer.setBackground(BankingTheme.Colors.SURFACE);
		footer.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(1, 0, 0, 0, BankingTheme.Colors.BORDER),
				BorderFactory.createEmptyBorder(BankingTheme.Spacing.SM, 0, BankingTheme.Spacing.SM, 0)
		));
		
		JLabel footerText = new JLabel("Author: Tozu - @Tozu2500 on Github");
		footerText.setFont(BankingTheme.Typography.CAPTION);
		footerText.setForeground(BankingTheme.Colors.TEXT_MUTED);
		footer.add(footerText);
		
		return footer;
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			new BankingApplicationDemo().setVisible(true);
		});
	}
	
	
}
