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
 */
public class NodeLayout implements LayoutManager2 {

    private Node rootNode;

    private int preferredWidth = 800;

    private int preferredHeight = 600;

    public NodeLayout(Node rootNode) {
        this.rootNode = rootNode;
    }

    public void addLayoutComponent(Component comp, Object constraints) {
    }

    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    public void invalidateLayout(Container target) {
    }

    public void addLayoutComponent(String name, Component comp) {
    }

    public void removeLayoutComponent(Component comp) {
    }

    public Dimension preferredLayoutSize(Container parent) {
        return new Dimension(preferredWidth, preferredHeight);
    }

    public Dimension minimumLayoutSize(Container parent) {
        return new Dimension(10, 10);
    }

    public void layoutContainer(Container parent) {
        layoutNode(rootNode, parent);

    }


    private void layoutNode(Node node, Container parent) {
        if(node instanceof ComponentNode) {
            ComponentNode componentNode = (ComponentNode) node;
            JComponent comp = componentNode.getComponent();
            comp.setBounds(Util.getBounds(node, parent, true));
        }
        else {
            SplitterNode splitterNode = (SplitterNode) node;
            for(Splitter splitter : splitterNode.getSplitters()) {
                splitter.resetBounds();
            }
            for(Node curChild : splitterNode.getVisibleChildren()) {
                layoutNode(curChild, parent);
            }
        }
    }


}
