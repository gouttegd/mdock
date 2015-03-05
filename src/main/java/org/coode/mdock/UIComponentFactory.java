package org.coode.mdock;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
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
 * Bio-Health Informatics Group<br>
 * Date: 01-May-2007<br><br>
 */
public class UIComponentFactory {

    private static UIComponentFactory instance;

    public static synchronized UIComponentFactory getInstance() {
        if(instance == null) {
            instance = new UIComponentFactory();
        }
        return instance;
    }


    /**
     * Creates the tabbed pane which is used to stack multiple components
     * within a single node.  The default implementation returns an instance
     * of <code>ComponentNodeTabbedPane</code>.
     */
    public JTabbedPane createComponentNodeTabbedPane() {
        return new ComponentNodeTabbedPane();
    }


    /**
     * Creates the border which surrounds component nodes.  By default,
     * this is a light gray line border.
     * @return The border, or <code>null</code> if there shouldn't be a
     * border around component nodes.
     */
    public Border createComponentNodeBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(3, 3, 3, 3));
    }

    public static synchronized void setInstance(UIComponentFactory factory) {
        instance = factory;
    }


}
