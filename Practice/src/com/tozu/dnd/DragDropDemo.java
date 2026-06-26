package com.tozu.dnd;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class DragDropDemo extends JFrame {

    private final JPanel dropPanel;

    public DragDropDemo() {

        setTitle("✨ Fancy Drag & Drop Demo");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 20));
        mainPanel.setBackground(new Color(25, 25, 35));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // LEFT PANEL
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(30, 30, 45));
        leftPanel.setLayout(new GridLayout(3, 1, 15, 15));
        leftPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 255)),
                "Drag These"));

        leftPanel.add(createDraggableLabel("🚀 Rocket"));
        leftPanel.add(createDraggableLabel("🎮 Gaming"));
        leftPanel.add(createDraggableLabel("⭐ Premium"));

        // RIGHT PANEL
        dropPanel = new JPanel(new FlowLayout());
        dropPanel.setBackground(new Color(40, 40, 55));
        dropPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 255, 255)),
                "Drop Here"));

        dropPanel.setTransferHandler(new TransferHandler() {

            @Override
            public boolean canImport(TransferSupport support) {
                boolean canImport =
                        support.isDataFlavorSupported(
                                java.awt.datatransfer.DataFlavor.stringFlavor);

                if (canImport) {
                    dropPanel.setBackground(new Color(60, 90, 140));
                }

                return canImport;
            }

            @Override
            public boolean importData(TransferSupport support) {

                if (!canImport(support)) {
                    return false;
                }

                try {

                    String text = (String) support.getTransferable()
                            .getTransferData(
                                    java.awt.datatransfer.DataFlavor.stringFlavor);

                    JLabel dropped = new JLabel("✓ " + text);
                    dropped.setForeground(new Color(100, 255, 150));
                    dropped.setFont(new Font("Segoe UI", Font.BOLD, 16));

                    dropPanel.add(dropped);

                    dropPanel.revalidate();
                    dropPanel.repaint();

                    dropPanel.setBackground(new Color(40, 40, 55));

                    return true;

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });

        mainPanel.add(leftPanel);
        mainPanel.add(dropPanel);

        add(mainPanel);
        setVisible(true);
    }

    private JLabel createDraggableLabel(String text) {

        JLabel label = new JLabel(text, SwingConstants.CENTER) {

            private boolean hovered = false;

            {
                setOpaque(false);
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI", Font.BOLD, 18));

                addMouseListener(new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        hovered = true;
                        repaint();
                    }

                    @Override
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        hovered = false;
                        repaint();
                    }
                });

                addMouseMotionListener(
                        new java.awt.event.MouseMotionAdapter() {

                            @Override
                            public void mouseDragged(
                                    java.awt.event.MouseEvent e) {

                                JComponent c =
                                        (JComponent) e.getSource();

                                c.getTransferHandler()
                                        .exportAsDrag(
                                                c,
                                                e,
                                                TransferHandler.COPY);
                            }
                        });
            }

            @Override
            protected void paintComponent(Graphics g) {

                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth();
                int h = getHeight();

                // Shadow
                g2.setColor(new Color(0, 0, 0, 70));
                g2.fillRoundRect(5, 5, w - 8, h - 8, 30, 30);

                // Glow
                if (hovered) {
                    g2.setColor(new Color(0, 255, 255, 80));
                    g2.fillRoundRect(0, 0, w, h, 30, 30);
                }

                GradientPaint gradient;

                if (hovered) {
                    gradient = new GradientPaint(
                            0, 0,
                            new Color(0, 200, 255),
                            w, h,
                            new Color(0, 120, 255));
                } else {
                    gradient = new GradientPaint(
                            0, 0,
                            new Color(130, 80, 255),
                            w, h,
                            new Color(70, 40, 180));
                }

                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, w - 5, h - 5, 30, 30);

                g2.dispose();

                super.paintComponent(g);
            }
        };

        label.setPreferredSize(new Dimension(200, 70));

        // DRAG SUPPORT
        label.setTransferHandler(new TransferHandler() {

            @Override
            protected java.awt.datatransfer.Transferable
            createTransferable(JComponent c) {

                JLabel lbl = (JLabel) c;
                return new StringSelection(lbl.getText());
            }

            @Override
            public int getSourceActions(JComponent c) {
                return COPY;
            }
        });

        return label;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                        UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            new DragDropDemo();
        });
    }
}