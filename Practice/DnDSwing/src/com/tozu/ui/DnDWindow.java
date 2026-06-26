package com.tozu.ui;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.tozu.core.DraggableLabel;
import com.tozu.core.DropZone;

public class DnDWindow extends JFrame {
	
	public DnDWindow() {
		setTitle("Drag and drop in Java Swing");
		setSize(700, 450);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		JPanel main = new JPanel(new GridLayout(1, 2, 10, 10));
		main.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		DropZone zoneA = new DropZone("Drop Zone 1");
		DropZone zoneB = new DropZone("Drop Zone 2");
		
		DraggableLabel label = new DraggableLabel("Drag me!");
		
		zoneA.add(label);
		
		main.add(zoneA);
		main.add(zoneB);
		
		add(main);
		
	}

}
