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


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 25-Sep-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 *
 * Developed as part of the CO-ODE project (http://www.co-ode.org)
 *
 * Vocabulary used for serialising descriptions of node panels
 */
public interface Vocabulary {

    /**
     * Represents an element containing information about a component node
     */
    public static final String COMPONENT_NODE = "CNode";

    /**
     * Represents an element containing information about a vertical splitter node
     */
    public static final String VERTICAL_SPLITTER_NODE = "VSNode";


    /**
     * Represents an element containing information about a horrizontal splitter node
     */
    public static final String HORIZONTAL_SPLITTER_NODE = "HSNode";


    /**
     * Represents an element containing information about a component that resides in a component node
     */
    public static final String COMPONENT = "Component";


    /**
     * Represents a list of splits
     */
    public static final String SPLITS = "splits";


    /**
     * An attribute that hold information about the label for a component in a component node.
     */
    public static final String LABEL = "label";

    public static final String PROPERTY = "Property";

    public static final String VALUE = "value";

    public static final String ID = "id";
}
