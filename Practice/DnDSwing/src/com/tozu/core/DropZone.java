package com.tozu.core;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class DropZone extends JPanel {
	
	public DropZone(String title) {
		setBorder(new LineBorder(Color.GRAY, 2));
		setLayout(new FlowLayout());
		setBackground(Color.WHITE);
		
		add(new JLabel(title));
		
		new DropTarget(this, new DropTargetAdapter() {
			
			@Override
			public void dragEnter(DropTargetDragEvent dtde) {
				setBackground(new Color(200, 255, 200));
			}
			
			@SuppressWarnings("unused")
			public void dragExit(DropTargetDragEvent dte) {
				setBackground(Color.WHITE);
			}
			
			@Override
			public void drop(DropTargetDropEvent dtde) {
				setBackground(Color.WHITE);
				dtde.acceptDrop(DnDConstants.ACTION_COPY);
				
				FileDropHandler.handle(dtde, DropZone.this);
				LabelDropHandler.handle(dtde, DropZone.this);
				
				revalidate();
			}
		});
	}

}
