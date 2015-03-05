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

import java.awt.*;


/**
 * Author: Matthew Horridge
 * The University Of Manchester
 * Medical Informatics Group
 * Date: 24-Sep-2006
 *
 * matthew.horridge@cs.man.ac.uk
 * www.cs.man.ac.uk/~horridgm
 *
 * Developed as part of the CO-ODE project (http://www.co-ode.org)
 */
public class VerticalSplitter extends Splitter {

    public VerticalSplitter(SplitterNode node, Node child0, Node child1) {
        super(node, child0, child1);
    }

    public void resetBounds() {
        // Set the bounds
        int x = Util.getX(getChild1(), getParent(), false) - SPLITTER_WIDTH / 2 + 1;
        int y = Util.getY(getChild1(), getParent(), false) + SPLITTER_WIDTH / 2 + 1;
        int h = Util.getHeight(getChild1(), getParent(), false) - SPLITTER_WIDTH - 2;
        int w = Splitter.SPLITTER_WIDTH - 2;
        setBounds(x, y, w, h);
    }

    public Point getMaxLocation() {
        int x = Util.getX(getChild1(), getParent(), false) - SPLITTER_WIDTH / 2;
        int y = Util.getY(getChild1(), getParent(), false) + SPLITTER_WIDTH / 2;
        int w = Util.getWidth(getChild1(), getParent(), false);
        return new Point(x + w, y);
    }

    public Point getMinLocation() {
        int x = Util.getX(getChild0(), getParent(), false);
        int y = Util.getY(getChild0(), getParent(), false) + SPLITTER_WIDTH / 2;
        return new Point(x, y);
    }

    protected int convertToLocation(Point pt) {
        return pt.x;
    }

    protected int getSpan(Node child, Component parent, boolean includingSplitter) {
        return Util.getWidth(child, parent, includingSplitter);
    }

    protected void setSplitterCursor() {
        setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
    }
}
