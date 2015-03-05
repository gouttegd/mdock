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

import java.util.List;
import java.util.Arrays;


/**
 * Author: Matthew Horridge
 * The University Of Manchester
 * Medical Informatics Group
 * Date: 23-Sep-2006
 *
 * matthew.horridge@cs.man.ac.uk
 * www.cs.man.ac.uk/~horridgm
 *
 * Developed as part of the CO-ODE project (http://www.co-ode.org)
 *
 * A nod which contains child nodes which are split with vertical splitters
 */
public class VerticalSplitterNode extends SplitterNode {


    public VerticalSplitterNode(List<? extends Node> children, List<Double> splits) {
        super(children, splits);
    }

    protected double getGloballyNormalisedWidth(Node child) {
        // We affect the width of our children.  The globally normalised
        // width of one of our children, is our globally normalised width
        // multiplied by the normalised split of the child
        return getGloballyNormalisedWidth() * getNormalisedSplit(child);
    }

    public double getGloballyNormalisedXLocation(Node child) {
        // We affect the X location of our child nodes
        // Calculate the child positions relative to ours
        double xLoc = getGloballyNormalisedXLocation();
        for(Node curChild : getVisibleChildren()) {
            if (curChild == child) {
                return xLoc;
            }
            else {
                xLoc += getGloballyNormalisedWidth(curChild);
            }
        }
        return 0.0;
    }

    public boolean isSplitterDirection(int direction) {
        return VERTICAL_SPLITTER == direction;
    }

    public Splitter createSplitter(Node child0, Node child1) {
        return new VerticalSplitter(this, child0, child1);
    }

    protected SplitterNode createPerpendicularSplitterNode(List<Node> children, List<Double> splits) {
        return new HorizontalSplitterNode(children, splits);
    }

    public SplitterNode pushDown(Node existingChild, List<Node> children) {
        HorizontalSplitterNode vsn = new HorizontalSplitterNode(children, Arrays.asList(0.5, 0.5));
        replaceChild(existingChild, vsn);
        return vsn;
    }

    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
