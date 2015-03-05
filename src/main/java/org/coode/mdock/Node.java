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

import org.w3c.dom.Element;
import org.w3c.dom.Document;

import java.io.Writer;
import java.io.IOException;
import java.io.PrintWriter;


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
 */
public abstract class Node {

    private SplitterNode parent;
    private boolean visible = true;

    protected void setParent(SplitterNode node) {
        this.parent = node;
    }

    /**
     * Gets the parent node of this node.
     * @return The parent node, or <code>null</code>
     * if there is no parent node
     */
    public SplitterNode getParent() {
        return parent;
    }

    protected double getGloballyNormalisedHeight(Node child) {
        if(parent != null) {
            // Ask out parent
            return parent.getGloballyNormalisedHeight(this);
        }
        else {
            // We are a root, so, by definition our normalised
            // height is 1.0
            return 1.0;
        }
    }

    protected double getGloballyNormalisedWidth(Node child) {
        if(parent != null) {
            // Ask our parent
            return parent.getGloballyNormalisedWidth(this);
        }
        else {
            return 1.0;
        }
    }

    public double getGloballyNormalisedXLocation() {
        if(parent == null) {
            return 0.0;
        }
        else {
            return parent.getGloballyNormalisedXLocation(this);
        }
    }

    public double getGloballyNormalisedXLocation(Node child) {
        if(parent == null) {
            return 0.0;
        }
        else {
            return parent.getGloballyNormalisedXLocation(this);
        }
    }

    public double getGloballyNormalisedYLocation() {
        if(parent == null) {
            return 0.0;
        }
        else {
            return parent.getGloballyNormalisedYLocation(this);
        }
    }

    public double getGloballyNormalisedYLocation(Node child) {
        if(parent == null) {
            return 0.0;
        }
        else {
            return parent.getGloballyNormalisedYLocation(this);
        }
    }


    public double getGloballyNormalisedHeight() {
        if(parent == null) {
            // Root node, so our global height is 1.0
            return 1.0;
        }
        else {
            // We need to ask our parent
            return parent.getGloballyNormalisedHeight(this);
        }
    }

    public double getGloballyNormalisedWidth() {
        if(parent == null) {
            // Root node, so our global width is 1.0
            return 1.0;
        }
        else {
            // We need to ask out parent
            return parent.getGloballyNormalisedWidth(this);
        }

    }

    final public void setVisible(boolean visible) {
        this.visible = visible;
        notifyStateChange();
    }

    public boolean isVisible() {
        return visible;
    }

    protected void notifyStateChange() {
        stateChanged();
        if(parent != null) {
            parent.stateChanged();
            // Apply recursively
            parent.notifyStateChange();
        }
    }

    public void remove() {
        SplitterNode parent = getParent();
        if(parent != null) {
            parent.removeChild(this);
            notifyStateChange();
        }
    }

    protected abstract void stateChanged();

    public abstract void accept(NodeVisitor visitor);
}
