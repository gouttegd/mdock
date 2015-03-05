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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 23-Sep-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 *
 * Developed as part of the CO-ODE project (http://www.co-ode.org)
 *
 * The base class of horizontal and vertical splitters
 */
public abstract class Splitter extends JPanel {

    public static final int SPLITTER_WIDTH = 6;

    private SplitterNode node;

    private Node child0;

    private Node child1;

    Point startPoint;

    Point endPoint;

    private boolean mouseDown;

    private Cursor defaultCursor;

    protected Splitter(SplitterNode node, Node child0, Node child1) {
        this.node = node;
        this.child0 = child0;
        this.child1 = child1;
        setOpaque(true);

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                setBackground(Color.GRAY);
                mouseDown = true;
                startDragging(e.getPoint());
            }

            public void mouseReleased(MouseEvent e) {
                setBackground(null);
                mouseDown = false;
                endDragging(e.getPoint());
                if(getMousePosition() == null) {
                    restoreDefaultCursor();
                }
            }

            public void mouseEntered(MouseEvent e) {
                defaultCursor = getCursor();
                setSplitterCursor();
            }

            public void mouseExited(MouseEvent e) {
                if (!mouseDown) {
                    restoreDefaultCursor();
                }
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point p = SwingUtilities.convertPoint(Splitter.this, e.getPoint(), getParent());
                setLocation(p);
            }
        });
    }

    protected abstract void setSplitterCursor();

    protected void restoreDefaultCursor() {
        setCursor(defaultCursor);
    }

    public void setLocation(int x, int y) {
            Point minLoc = getMinLocation();
            Point maxLoc = getMaxLocation();
            if(x < minLoc.x) {
                x = minLoc.x;
            }
            if(y < minLoc.y) {
                y = minLoc.y;
            }
            if(x > maxLoc.x) {
                x = maxLoc.x;
            }
            if(y > maxLoc.y) {
                y = maxLoc.y;
            }
            super.setLocation(x, y);
    }

    public SplitterNode getNode() {
        return node;
    }

    public Node getChild0() {
        return child0;
    }

    public Node getChild1() {
        return child1;
    }

    public abstract void resetBounds();

    public abstract Point getMaxLocation();

    public abstract Point getMinLocation();

    public void startDragging(Point pt) {
        startPoint = getLocation();
    }

    protected abstract int convertToLocation(Point pt);

    protected abstract int getSpan(Node child, Component parent, boolean includingSplitter);

    public void endDragging(Point pt) {
        endPoint = getLocation();
        // Work out the new child splits
        int delta = convertToLocation(endPoint) - convertToLocation(startPoint);
        // Normalise the delta against the total height out the children
        int totalChildSpan = getSpan(getChild0(), getParent(), false) + getSpan(getChild1(), getParent(), false);

        double span = getNode().getSplit(getChild0()) + getNode().getSplit(getChild1());

        double splitterDelta = (span * delta) / (totalChildSpan);

        double split0 = (getNode().getSplit(getChild0()) + splitterDelta);
        double split1 = (getNode().getSplit(getChild1()) - splitterDelta);
        getNode().setSplit(getChild0(), split0);
        getNode().setSplit(getChild1(), split1);
        getParent().validate();
    }


}
