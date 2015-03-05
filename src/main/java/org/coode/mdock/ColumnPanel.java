package org.coode.mdock;

import javax.swing.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
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
 * Author: Matthew Horridge
 * The University Of Manchester
 * Bio-Health Informatics Group
 * Date: 09-Aug-2007
 */
public class ColumnPanel extends JPanel {

    private NodePanel columns;

    private VerticalSplitterNode node;

    private int defaultColumnWidth = 180;


    private JPanel holderPanel;

    private JScrollPane sp;

    private LinkedHashMap<JComponent, Node> componentNodeMap;



    public ColumnPanel(JComponent initialComponent) {
        componentNodeMap = new LinkedHashMap<JComponent, Node>();
        ComponentNode firstComponentNode = new ComponentNode();
        firstComponentNode.add(initialComponent, "Component");

        ArrayList<Node> nodes = new ArrayList<Node>();
        nodes.add(firstComponentNode);

        ArrayList<Double> splits = new ArrayList<Double>();
        splits.add(1.0);

        componentNodeMap.put(initialComponent, firstComponentNode);
        
        node = new VerticalSplitterNode(nodes, splits);
        columns = new NodePanel(node) {

            public Dimension getPreferredSize() {
                return new Dimension(node.getVisibleChildren().size() * defaultColumnWidth, 10);
            }
        };
        setLayout(new BorderLayout());
        holderPanel = new JPanel(new BorderLayout());
        holderPanel.add(columns, BorderLayout.WEST);
        add(sp = new JScrollPane(holderPanel));

    }

    public void ensureVisible(JComponent component) {
        component.scrollRectToVisible(new Rectangle(component.getSize().width, 10));
    }

//
//    public void addColumn(JComponent content) {
//
//
//
//    }


    public void removeColumnsAfter(JComponent columnContent) {
        boolean remove = false;
        for (Iterator<JComponent> it = componentNodeMap.keySet().iterator(); it.hasNext();) {
            JComponent comp = it.next();
            Node curNode = componentNodeMap.get(comp);
            if (comp.equals(columnContent)) {
                remove = true;
            }
            else if (remove) {
                it.remove();
                node.removeChild(curNode);
            }
        }
        columns.rebuild();
        columns.validate();

    }


    public void addColumn(JComponent content, JComponent after) {
        JComponent focusedComponent = null;
        for(JComponent c : componentNodeMap.keySet()) {
            if(c.isFocusOwner()) {
                focusedComponent = c;
                break;
            }
        }
        List<Node> visibleChildren = node.getVisibleChildren();
        List<Integer> widths = new ArrayList<Integer>();
        int total = 0;
        for (Node childNode : visibleChildren) {
            int childWidth = Util.getWidth(childNode, columns, false);
            widths.add(childWidth);
            total += childWidth;
        }
        widths.add(defaultColumnWidth);

        removeColumnsAfter(after);
        visibleChildren = node.getVisibleChildren();

        ComponentNode cn = new ComponentNode();
        cn.add(content, "");
        componentNodeMap.put(content, cn);
        if(!visibleChildren.isEmpty()) {
            node.insertNodeAfter(cn, visibleChildren.get(visibleChildren.size() - 1), SplitterNode.VERTICAL_SPLITTER);
        }
        else {
            node.addChild(cn, 0, 1.0);
        }

        visibleChildren = node.getVisibleChildren();
        int currentIndex = 0;
        for (Node nn : visibleChildren) {
            node.setSplit(nn, widths.get(currentIndex) * 1.0 / total);
            currentIndex++;
        }
        columns.rebuild();
        holderPanel.setPreferredSize(new Dimension(node.getVisibleChildren().size() * defaultColumnWidth, 10));
        sp.validate();
        sp.getHorizontalScrollBar().setValue(Integer.MAX_VALUE);
        if(focusedComponent != null) {
            System.out.println("Refocusing");
            focusedComponent.requestFocus();
        }
        else {
            content.requestFocus();
        }
//        addColumn(content);
    }


    public static void main(String[] args) {
        JButton button = new JButton("Test");
        ColumnPanel pan = new ColumnPanel(button);
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(new Dimension(300, 400));
        f.setContentPane(pan);
        f.setVisible(true);
    }
}