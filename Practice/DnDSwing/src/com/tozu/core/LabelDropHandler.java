package com.tozu.core;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetDropEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class LabelDropHandler {
	
	public static void handle(DropTargetDropEvent dtde, JPanel panel) {
		try {
			Transferable t = dtde.getTransferable();
			
			if (!t.isDataFlavorSupported(DataFlavor.stringFlavor))
				return;
			
			String text = (String) t.getTransferData(DataFlavor.stringFlavor);
			panel.add(new JLabel(text));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
