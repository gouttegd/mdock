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
 * A node which contains child nodes that are split by horizontal splitters.
 */
public class HorizontalSplitterNode extends SplitterNode {

    public HorizontalSplitterNode(List<? extends Node> children, List<Double> splits) {
        super(children, splits);
    }

    protected double getGloballyNormalisedHeight(Node child) {
        // We affect the height of our children
        return getGloballyNormalisedHeight() * getNormalisedSplit(child);
    }

    public double getGloballyNormalisedYLocation(Node child) {
        // We affect the y location of our children
        double yLoc = getGloballyNormalisedYLocation();
        for(Node curChild : getVisibleChildren()) {
            if(curChild == child) {
                return yLoc;
            }
            else {
                yLoc += getGloballyNormalisedHeight(curChild);
            }
        }
        return 0.0;
    }

    public boolean isSplitterDirection(int direction) {
        return direction == HORIZONTAL_SPLITTER;
    }

    public Splitter createSplitter(Node child0, Node child1) {
        return new HorizontalSplitter(this, child0, child1);
    }

    public SplitterNode pushDown(Node existingChild, List<Node> children) {
        VerticalSplitterNode vsn = new VerticalSplitterNode(children, Arrays.asList(0.5, 0.5));
        replaceChild(existingChild, vsn);
        return vsn;
    }

    protected SplitterNode createPerpendicularSplitterNode(List<Node> children, List<Double> splits) {
        return new VerticalSplitterNode(children, splits);
    }

    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

}
