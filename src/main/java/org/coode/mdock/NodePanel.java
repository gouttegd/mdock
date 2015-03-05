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
 * A <code>NodePanel</code> is a <code>JPanel</code> which comprises a tree map layout of
 * <code>Node</code>s.  The leaf nodes contain one or more components.
 */
public class NodePanel extends JPanel {

    private Node rootNode;

    public NodePanel(Node rootNode) {
        this.rootNode = rootNode;
        setLayout(new NodeLayout(rootNode));
        rebuild();
    }
    
    public Node getRootNode() {
        return rootNode;
    }

    public void rebuild() {
        Component focusedComponent = FocusManager.getCurrentManager().getFocusOwner();

        removeAll();
        addNode(rootNode);
        revalidate();
        if (focusedComponent != null) {
            focusedComponent.requestFocus();
        }
    }


    private void addNode(Node node) {
        if (node instanceof ComponentNode) {
            add(((ComponentNode) node).getComponent());
        } else {
            for (Splitter splitter : ((SplitterNode) node).getSplitters()) {
                add(splitter);
            }
            for (Node curChildNode : ((SplitterNode) node).getVisibleChildren()) {
                addNode(curChildNode);
            }
        }
    }


}
