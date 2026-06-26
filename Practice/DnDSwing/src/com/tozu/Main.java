package com.tozu;

import javax.swing.SwingUtilities;

import com.tozu.ui.DnDWindow;

public class Main {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			new DnDWindow().setVisible(true);
		});
	}

}
