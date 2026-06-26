package com.tozu.core;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.TransferHandler;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class DraggableLabel extends JLabel {
	
	public DraggableLabel(String text) {
		super(text);
		setOpaque(true);
		setBackground(new Color(180, 200, 255));
		setBorder(new LineBorder(Color.BLACK));
		
		setTransferHandler(new TransferHandler("text"));
		
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				TransferHandler handler = getTransferHandler();
				handler.exportAsDrag(DraggableLabel.this, evt, TransferHandler.COPY);
			}
		});
	}

}
