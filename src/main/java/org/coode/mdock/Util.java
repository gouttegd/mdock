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
 * Date: 24-Sep-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 *
 * Developed as part of the CO-ODE project (http://www.co-ode.org)
 */
public class Util {

    public static int getX(Node node, Component c, boolean includeSplitter) {
        int x = (int) Math.round((node.getGloballyNormalisedXLocation() * c.getWidth()));
        if(includeSplitter) {
            x = x + Splitter.SPLITTER_WIDTH / 2;
        }
        return x;
    }

    public static int getY(Node node, Component c, boolean includeSplitter) {
        int y = (int) Math.round((node.getGloballyNormalisedYLocation() * c.getHeight()));
        if(includeSplitter) {
            y = y + Splitter.SPLITTER_WIDTH / 2;
        }
        return y;
    }

    public static int getWidth(Node node, Component c, boolean includeSplitter) {
        int w = (int) Math.round((node.getGloballyNormalisedWidth() * c.getWidth()));
        if(includeSplitter) {
            w = w - Splitter.SPLITTER_WIDTH;
        }
        return w;
    }

    public static int getHeight(Node node, Component c,  boolean includeSplitter) {
        int h = (int) Math.round((node.getGloballyNormalisedHeight() * c.getHeight()));
        if(includeSplitter) {
            h = h - Splitter.SPLITTER_WIDTH;
        }
        return h;
    }

    public static Rectangle getBounds(Node node, Component c,  boolean includeSplitter) {
        return new Rectangle(getX(node, c, includeSplitter),
                             getY(node, c, includeSplitter),
                             getWidth(node, c, includeSplitter),
                             getHeight(node, c, includeSplitter));
    }

    public static SplitterNode getDeepestSplitterNode(SplitterNode rootNode, Component c, Point pt) {
        SplitterNode deepestNode = null;
        Rectangle bounds = getBounds(rootNode, c, false);
        if(bounds.contains(pt)) {
            deepestNode = rootNode;
            for(Node curChild : rootNode.getVisibleChildren()) {
                if (curChild instanceof SplitterNode) {
                    SplitterNode n = getDeepestSplitterNode((SplitterNode) curChild, c, pt);
                    if(n != null) {
                        return n;
                    }
                }
            }
        }
        else {
            return null;
        }
        return deepestNode;
    }

    public static Node getDeepestNode(Node rootNode, Component c, Point pt) {
        Node deepestNode = null;
        Rectangle bounds = getBounds(rootNode, c, false);
            if(bounds.contains(pt)) {
                deepestNode = rootNode;
                if (rootNode instanceof SplitterNode) {
                    for(Node curChild : ((SplitterNode) rootNode).getVisibleChildren()) {
                            Node n = getDeepestNode(curChild, c, pt);
                            if(n != null) {
                                return n;
                            }
                    }
                }
            }
        return deepestNode;
    }

    public static void bringToFront(JComponent component) {
        Component parent = component.getParent();
        Component child = component;
        while(parent != null) {
            if(parent instanceof JTabbedPane) {
                ((JTabbedPane) parent).setSelectedComponent(child);
            }
            child = parent;
            parent = parent.getParent();
        }
    }

}
