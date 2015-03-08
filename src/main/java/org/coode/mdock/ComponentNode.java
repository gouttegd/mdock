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
import javax.swing.plaf.TabbedPaneUI;
import java.awt.*;
import java.util.*;
import java.util.List;

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
 * Represents a node that contains one or more user components.  If there
 * are multiple user components then they appear to be stacked via the use
 * of a tabbed pane.
 */
public class ComponentNode extends Node {

    private final JTabbedPane tabbedPane;

    private final List<JComponent> components;

    private final Map<JComponent, String> component2LabelMap;

    private final JComponent baseComponent;


    public ComponentNode() {
        baseComponent = new JPanel() {


            public void remove(Component comp) {
                ComponentNode.this.remove((JComponent) comp);
            }


            public void remove(int index) {
                super.remove(index);

            }


            public void removeAll() {
                super.removeAll();
            }
        };
        components = new ArrayList<>();
        component2LabelMap = new HashMap<>();
        TabbedPaneUI tabbedPaneUI = UIComponentFactory.getInstance().createComponentNodeTabbedPaneUI();
        tabbedPane = new JTabbedPane() {
            @Override
            public void remove(Component component) {
                ComponentNode.this.remove((JComponent) component);
            }
        };
        tabbedPane.setUI(tabbedPaneUI);
        baseComponent.setBorder(UIComponentFactory.getInstance().createComponentNodeBorder());
    }


    /**
     * Adds a component to this node.  If there are multiple components
     * they will be "stacked" in a tabbed pane.
     *
     * @param component The component to be added
     * @param label     The label for the selection tab which brings the component
     *                  to the front if there are multiple components.
     */
    public void add(JComponent component, String label) {
        components.add(component);
        component2LabelMap.put(component, label);
        if (components.size() == 1) {
            baseComponent.add(component);
        } else if (components.size() > 1) {
            baseComponent.removeAll();
            for (JComponent c : components) {
                tabbedPane.add(component2LabelMap.get(c), c);
            }
            baseComponent.add(tabbedPane);
        }


//        tabbedPane.add(label, component);

        if (component instanceof NodeComponent) {
            // Notify the component that it was added to us
            ((NodeComponent) component).addedToNode(this);
        }
    }


    /**
     * Gets the label for the specified component.
     *
     * @param component The component whose label is to be
     *                  retrieved
     * @return The label of the specified component, or an
     * empty string if the component isn't held by this component node.
     */
    public String getLabel(JComponent component) {
        String label = component2LabelMap.get(component);
        if (label != null) {
            return label;
        } else {
            return "";
        }
    }

    public void remove(JComponent component) {
        component2LabelMap.remove(component);
        components.remove(component);

        baseComponent.removeAll();
        tabbedPane.removeAll();
        if (components.size() == 1) {
            baseComponent.add(components.get(0));
        } else if (components.size() > 1) {
            for (JComponent c : components) {
                tabbedPane.add(component2LabelMap.get(c), c);
            }
            baseComponent.add(tabbedPane);
        }

        removeFromParentIfEmpty();
        notifyStateChange();
    }


    private void removeFromParentIfEmpty() {
        if (getComponentCount() == 0) {
            baseComponent.getParent().remove(baseComponent);
            remove();
        }
    }


    /**
     * Gets the root component (most likely a tabbed pane)
     *
     * @return The root component
     */
    public JComponent getComponent() {
        return baseComponent;
    }


    /**
     * Gets the number of user components held by this component node.
     *
     * @return The number of components
     */
    public int getComponentCount() {
        return components.size();
    }


    /**
     * Determines if this node is visible.  A component node is defined to
     * be visible if it holds at least one component, otherwise, it is
     * not visible.
     *
     * @return The visibility
     */
    public boolean isVisible() {
        return getComponentCount() != 0;
    }

    public double getGloballyNormalisedXLocation(Node child) {
        if (getParent() == null) {
            return 1.0;
        } else {
            // Ask our parent
            return getParent().getGloballyNormalisedYLocation();
        }
    }

    public double getGloballyNormalisedYLocation(Node child) {
        return 0;
    }

    protected void stateChanged() {
    }

    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }


    /**
     * Gets a list of the component that are held by this component node.
     *
     * @return The components.
     */
    public List<JComponent> getComponents() {
        return Collections.unmodifiableList(components);
    }
}
