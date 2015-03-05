package org.coode.mdock;
/*
 * Copyright (C) 2007, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;


/**
 * Author: Matthew Horridge
 * The University Of Manchester
 * Medical Informatics Group
 * Date: 25-Sep-2006
 *
 * matthew.horridge@cs.man.ac.uk
 * www.cs.man.ac.uk/~horridgm
 *
 * Developed as part of the CO-ODE project (http://www.co-ode.org)
 *
 * Provides a way of dynamically adding components to a node panel
 * (via drag and drop like actions).
 */
public class DynamicConfigPanel extends JPanel {

    private JComponent component;

    private NodePanel nodePanel;

    private JComponent currentComponent;

    private String currentLabel;

    private Color lineColor = new Color(139, 178, 212);

    public DynamicConfigPanel(JComponent component) {
        this.component = component;
        this.nodePanel = null;
        setOpaque(false);


        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                updateNodePanel();
                repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                handleMouseClicked(e);
            }
        });
    }

    private void handleMouseClicked(MouseEvent e) {
        if(nodePanel == null) {
            return;
        }
        setVisible(false);
        // Insert the new node
        Point nodePanelPt = SwingUtilities.convertPoint(this, e.getPoint(), nodePanel);
        Node n = Util.getDeepestNode(nodePanel.getRootNode(), nodePanel, nodePanelPt);
        if(n == null) {
            return;
        }
        AddMode mode = getAddMode(n, nodePanelPt);
        mode.doAdd(currentComponent, currentLabel);
        currentComponent = null;
        nodePanel.rebuild();
    }

    public void setCurrentComponent(JComponent currentComponent, String label) {
        this.currentComponent = currentComponent;
        this.currentLabel = label;
    }

    private void updateNodePanel() {
        Container contentPane = getRootPane().getContentPane();
        // Find the deepest node panel
        Point pt = getMousePosition();
        if(pt == null) {
            return;
        }
        pt = SwingUtilities.convertPoint(this, pt, contentPane);
        Component c = SwingUtilities.getDeepestComponentAt(contentPane, pt.x, pt.y);
        if(c instanceof NodePanel) {
            nodePanel = (NodePanel) c;
            return;
        }
        NodePanel np  = (NodePanel) SwingUtilities.getAncestorOfClass(NodePanel.class, c);
        if(np != null) {
            nodePanel = np;
        }
    }


    public void activate() {
        if(currentComponent == null) {
            throw new IllegalStateException("No component is set!");
        }
        if(currentComponent.getParent() != null) {
            throw new IllegalStateException("Current component already has a parent!");
        }
        component.getRootPane().setGlassPane(this);
        setVisible(true);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Point pt = getMousePosition();
        // Paint target - this can be quite useful in a complex UI (or on a slow machine),
        // where repaints are slow.
        if (pt != null) {
            g2.fillOval(pt.x - 4, pt.y - 4, 8, 8);
            g2.drawOval(pt.x - 6, pt.y - 6, 12, 12);
        }
        Point mousePos = getMousePosition();
        if (mousePos != null && nodePanel != null) {
            // Draw a box round the deepest node that the mouse is over
            mousePos = SwingUtilities.convertPoint(this, mousePos, nodePanel);
            Node n = Util.getDeepestNode(nodePanel.getRootNode(), nodePanel, mousePos);
            if (n != null) {
                AddMode addMode = getAddMode(n, mousePos);
                addMode.paintRect(g);
            }
        }
    }

    private AddMode getAddMode(Node node, Point mousePoint) {
        if(node.getParent() == null) {
            // Root node
            return new CentreAddMode(node);
        }
        Rectangle bounds = Util.getBounds(node, nodePanel, false);
//        bounds = SwingUtilities.convertRectangle(nodePanel, bounds, this);
        bounds.grow(-6, -6);
        int xMargin = (int) (bounds.width * 0.3);
        int yMargin = (int) (bounds.height * 0.3);

        if(mousePoint.y < (bounds.y + yMargin)) {
            return new TopAddMode(node);
        }
        else if(mousePoint.y > bounds.y + bounds.height - yMargin) {
            return new BottomAddMode(node);
        }
        else if(mousePoint.x < bounds.x + xMargin) {
            return new LeftAddMode(node);
        }
        else if(mousePoint.x > bounds.x + bounds.width - xMargin) {
            return new RightAddMode(node);
        }
        else {
            return new CentreAddMode(node);
        }
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Split mode stuff


    private abstract class AddMode {

        public final Stroke stroke = new BasicStroke(4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        public final Stroke thinStroke = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

        public static final int ARROW_DIM = 20;

        private Node node;

        private Rectangle bounds;


        public AddMode(Node node) {
            this.node = node;
            bounds = Util.getBounds(node, nodePanel, false);
            bounds = SwingUtilities.convertRectangle(nodePanel, bounds, DynamicConfigPanel.this);
        }

        protected Node getNode() {
            return node;
        }

        public abstract void doAdd(JComponent component, String label);

        public abstract Rectangle getRect();

        public Rectangle getBounds() {
            return bounds;
        }

        public Rectangle getInnerBounds() {
            Rectangle smallBounds = new Rectangle(bounds);
            smallBounds.grow(-6, -6);
            return smallBounds;
        }

        public void paintRect(Graphics g) {
            Stroke oldStroke = ((Graphics2D) g).getStroke();

            Graphics2D g2 = ((Graphics2D) g);
            g2.setStroke(stroke);
            Color oldColor = g.getColor();
            g.setColor(lineColor);
            Rectangle smallBounds = getInnerBounds();
            g.drawRect(smallBounds.x, smallBounds.y, smallBounds.width, smallBounds.height);
            Rectangle posRect = getRect();
            posRect.grow(-6, -6);
            currentComponent.setBounds(posRect);
            currentComponent.validate();
            Composite oldComp = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            SwingUtilities.paintComponent(g, currentComponent, DynamicConfigPanel.this, posRect);
            g2.setComposite(oldComp);
            g2.setStroke(oldStroke);
            g.drawRect(posRect.x, posRect.y, posRect.width, posRect.height);
            g.setColor(oldColor);
            g2.setStroke(oldStroke);
        }

    }

    private class TopAddMode extends AddMode {

        public TopAddMode(Node node) {
            super(node);
        }

        public void doAdd(JComponent component, String label) {
            SplitterNode parentNode = getNode().getParent();
            // Create the new node
            ComponentNode cn = new ComponentNode();
            cn.add(component, label);
            // Now add the new node before the existing node. We require a horizontal
            // splitter!
            parentNode.insertNodeBefore(cn, getNode(), SplitterNode.HORIZONTAL_SPLITTER);
        }

        public Rectangle getRect() {
            Rectangle rect = getInnerBounds();
            rect.height = rect.height / 2;
            return rect;
        }
    }

    private class BottomAddMode extends AddMode {

        public BottomAddMode(Node node) {
            super(node);
        }


        public Rectangle getRect() {
            Rectangle rect = getInnerBounds();
            rect.height = rect.height / 2;
            rect.y = rect.y + rect.height;
            return rect;
        }

        public void doAdd(JComponent component, String label) {
            SplitterNode parentNode = getNode().getParent();
            ComponentNode cn = new ComponentNode();
            cn.add(component, label);
            parentNode.insertNodeAfter(cn, getNode(), SplitterNode.HORIZONTAL_SPLITTER);

        }
    }

    private class LeftAddMode extends AddMode {

        public LeftAddMode(Node node) {
            super(node);
        }

        public Rectangle getRect() {
            Rectangle rect = getInnerBounds();
            rect.width = rect.width / 2;
            return rect;
        }

        public void doAdd(JComponent component, String label) {
            ComponentNode cn = new ComponentNode();
            cn.add(component, label);
            SplitterNode parentNode = getNode().getParent();
            parentNode.insertNodeBefore(cn, getNode(), SplitterNode.VERTICAL_SPLITTER);
        }
    }


    private class RightAddMode extends AddMode {

        public RightAddMode(Node node) {
            super(node);
        }

        public void doAdd(JComponent component, String label) {
            SplitterNode parentNode = getNode().getParent();
            ComponentNode cn = new ComponentNode();
            cn.add(component, label);
            parentNode.insertNodeAfter(cn, getNode(), SplitterNode.VERTICAL_SPLITTER);
        }

        public Rectangle getRect() {
            Rectangle rect = getInnerBounds();
            rect.width = rect.width / 2;
            rect.x = rect.x + rect.width;
            return rect;
        }
    }

    private class CentreAddMode extends AddMode {

        public CentreAddMode(Node node) {
            super(node);
        }

        public void doAdd(JComponent component, String label) {
            if(getNode() instanceof ComponentNode) {
                ((ComponentNode) getNode()).add(component, label);
            }
            else {
                ComponentNode cn = new ComponentNode();
                cn.add(component, label);
                ((SplitterNode) getNode()).addChild(cn, 0, 1.0);
            }
            Util.bringToFront(component);
        }

        public Rectangle getRect() {
            return getInnerBounds();
        }
    }


    
}
