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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ContainerListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.util.*;
import java.util.List;
import java.beans.VetoableChangeListener;


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
 * Represents a node that contains one or more user components.  If there
 * are multiple user components then they appear to be stacked via the use
 * of a tabbed pane.
 */
public class ComponentNode extends Node {

    private JTabbedPane tabbedPane;

    private List<JComponent> components;

    private Map<JComponent, String> component2LabelMap;

    private JComponent baseComponent;


    public ComponentNode() {
        baseComponent = new JPanel(new BorderLayout()) {


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
        components = new ArrayList<JComponent>();
        component2LabelMap = new HashMap<JComponent, String>();
        tabbedPane = new JTabbedPane() {

            public void remove(Component component) {
                ComponentNode.this.remove((JComponent)component);
            }


            public void remove(int index) {
                super.remove(index);
            }


            public void removeAll() {
                super.removeAll();
            }
        };
        tabbedPane.setFont(new Font("sans-serif", Font.PLAIN, 10));
        baseComponent.setBorder(UIComponentFactory.getInstance().createComponentNodeBorder());
    }


    /**
     * Adds a component to this node.  If there are multiple components
     * they will be "stacked" in a tabbed pane.
     * @param component The component to be added
     * @param label The label for the selection tab which brings the component
     * to the front if there are multiple components.
     */
    public void add(JComponent component, String label) {
        components.add(component);
        component2LabelMap.put(component, label);
        if(components.size() == 1) {
            baseComponent.add(component);
        }
        else if(components.size() > 1) {
            baseComponent.removeAll();
            for(JComponent c : components) {
                tabbedPane.add(component2LabelMap.get(c), c);
            }
            baseComponent.add(tabbedPane);
        }


//        tabbedPane.add(label, component);

        if(component instanceof NodeComponent) {
            // Notify the component that it was added to us
            ((NodeComponent) component).addedToNode(this);
        }
    }


    /**
     * Gets the label for the specified component.
     * @param component The component whose label is to be
     * retrieved
     * @return The label of the specified component, or an
     * empty string if the component isn't held by this component node.
     */
    public String getLabel(JComponent component) {
        String label = component2LabelMap.get(component);
        if(label != null) {
            return label;
        }
        else {
            return "";
        }
    }


    public void remove(JComponent component) {

        component2LabelMap.remove(component);
        components.remove(component);

        baseComponent.removeAll();
        tabbedPane.removeAll();
        if(components.size() == 1) {
            baseComponent.add(components.get(0));
        }
        else if(components.size() > 1) {
            for(JComponent c : components) {
                tabbedPane.add(component2LabelMap.get(c), c);
            }
            baseComponent.add(tabbedPane);
        }

        removeFromParentIfEmpty();
        notifyStateChange();
    }


    private void removeFromParentIfEmpty() {
        if(getComponentCount() == 0) {
            baseComponent.getParent().remove(baseComponent);
            remove();
        }
    }


    /**
     * Gets the root component (most likely a tabbed pane)
     */
    public JComponent getComponent() {
        return baseComponent;
    }


    /**
     * Gets the number of user components held by this component node.
     * @return
     */
    public int getComponentCount() {
        return components.size();
    }


    /**
     * Determines if this node is visible.  A component node is defined to
     * be visible if it holds at least one component, otherwise, it is
     * not visible.
     */
    public boolean isVisible() {
        return getComponentCount() != 0;
    }

    public double getGloballyNormalisedXLocation(Node child) {
        if(getParent() == null) {
            return 1.0;
        }
        else {
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
     */
    public List<JComponent> getComponents() {
        return Collections.unmodifiableList(components);
    }
}
