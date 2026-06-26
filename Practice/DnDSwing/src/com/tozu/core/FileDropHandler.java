package com.tozu.core;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class FileDropHandler {
	
	public static void handle(DropTargetDropEvent dtde, JPanel panel) {
		try {
			Transferable t = dtde.getTransferable();
			
			if (!t.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
				return;
			
			List<File> files = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
			
			for (File f : files) {
				panel.add(new JLabel("File: " + f.getName()));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
