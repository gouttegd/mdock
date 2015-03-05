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

import java.util.*;


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
 * A splitter node contains zero or more child nodes.  The child nodes
 * are split in either a vertical or horizontal direction by splitters
 * (splitters are the components that can be dragged about in the UI).
 * Each child node has a particular split value, which is used to determine
 * the proportion of the parent node which the child node takes up.  The
 * split value is not a percentage of the parent, rather it is a weighting and the percentage
 * is determined from the total child weighting. For example if a node had three children
 * with splits of 2.0, 2.0 and 4.0 then the child nodes would occupy 0.25, 0.25 and 0.5
 * of the parent space respectively.
 */
public abstract class SplitterNode extends Node {

    private List<Node> children;

    private Map<Node, Double> nodeSplits;

    private List<Splitter> splitters;

    public static final int HORIZONTAL_SPLITTER = 0;

    public static final int VERTICAL_SPLITTER = 1;


    /**
     * Creates a splitter node that contains the specified children.
     * @param children The children.
     * @param splits The splits of the children.  The number of splits must
     * be equal to the number of children.  It is not required that the sum of the splits
     * is a particular value.
     * @throws IllegalArgumentException if the number of splits is not equal to
     * the number of children.
     */
    public SplitterNode(List<? extends Node> children, List<Double> splits) {
        if (children.size() != splits.size()) {
            throw new IllegalArgumentException("The number of splits must correspond to the number of children");
        }
        this.children = new ArrayList<Node>(children);
        nodeSplits = new IdentityHashMap<Node, Double>();
        int index = 0;
        for (Node node : children) {
            node.setParent(this);
            nodeSplits.put(node, splits.get(index));
            index++;
        }
        createSplitters();
    }


    /**
     * Gets the split of the specified node.  This node
     * must be a child node.
     * @param node The Node.
     * @return The split.
     *
     */
    public double getSplit(Node node) {
        return nodeSplits.get(node);
    }


    /**
     * Gets the child node splits.
     * @return A list of child node splits
     */
    public List<Double> getSplits() {
        List<Double> splits = new ArrayList<Double>();
        for (Node curNode : getVisibleChildren()) {
            splits.add(nodeSplits.get(curNode));
        }
        return splits;
    }


    /**
     * Sets the split of a particular child node.
     * @param node The child node whose split should be set.
     * @param split The value of the split.
     */
    public void setSplit(Node node, double split) {
        nodeSplits.put(node, split);
    }


    /**
     * Gets the split of the specified node, normalised
     * against the span of the visible children.
     * @param node The node whose normalised split it
     *             to be obtained.
     * @return The normalised split of the specified node
     */
    public double getNormalisedSplit(Node node) {
        double split = nodeSplits.get(node);
        return split / getChildSpan();
    }


    /**
     * Gets the sum of the visible child node splits.
     * @return The sum of the splits
     */
    public double getChildSpan() {
        double span = 0.0;
        for (Node node : getVisibleChildren()) {
            span += nodeSplits.get(node);
        }
        return span;
    }


    /**
     * Gets a list of visible child nodes.
     * @return The list of children.
     */
    public List<Node> getVisibleChildren() {
        List<Node> visibleChildren = new ArrayList<Node>();
        for (Node curChild : children) {
            if (curChild.isVisible()) {
                visibleChildren.add(curChild);
            }
        }
        return visibleChildren;
    }


    /**
     * Determines if this node is visible.  A splitter node is
     * deemed to be visible if at least one child is visible.
     * @return <code>true</code> if this splitter node is visible
     * or <code>false</code> if it is not visible.
     */
    public boolean isVisible() {
        // We are visible if at least one of our children is visible
        for (Node node : children) {
            if (node.isVisible()) {
                return true;
            }
        }
        return false;
    }


    private void createSplitters() {
        splitters = new ArrayList<Splitter>();
        splitters.clear();
        List<Node> visibleChildren = getVisibleChildren();
        for (int i = 0; i < visibleChildren.size() - 1; i++) {
            Node child0 = visibleChildren.get(i);
            Node child1 = visibleChildren.get(i + 1);
            splitters.add(createSplitter(child0, child1));
        }
    }


    /**
     * Creates a splitter of the type (horizontal or vertical) that
     * is appropriate for this type of splitter node.
     * @param child0 The left/top child
     * @param child1 The right/bottom child
     * @return A splitter node which splits the left/top with the right/bottom
     */
    public abstract Splitter createSplitter(Node child0, Node child1);


    public final List<Splitter> getSplitters() {
        if (splitters == null) {
            createSplitters();
        }
        return splitters;
    }


    /**
     * Adds a child of this node.
     * @param child The child node to be added
     * @param index The position of the child node.
     * @param split The amount/weighting of parent node that the child node should
     *              receive
     */
    protected void addChild(Node child, int index, double split) {
        children.add(index, child);
        nodeSplits.put(child, split);
        child.setParent(this);
    }

    public void addChild(Node child, double split) {
        children.add(child);
        nodeSplits.put(child, split);
        child.setParent(this);
        notifyStateChange();
    }


    public void removeChild(Node child) {
        children.remove(child);
        nodeSplits.remove(child);
        child.setParent(null);
        if (children.isEmpty()) {
            remove();
        }
        notifyStateChange();
    }


    public abstract boolean isSplitterDirection(int direction);


    /**
     * Inserts a node before (left of or top of) a given node by splitting the inserted
     * node with the given node.
     * @param insert    The node to be inserted
     * @param before    The node that the inserted node will be split with.
     * @param direction The direction of the split
     */
    public void insertNodeBefore(Node insert, Node before, int direction) {
        if (isSplitterDirection(direction)) {
            double split = getSplit(before) / 2;
            setSplit(before, split);
            addChild(insert, children.indexOf(before), split);
        }
        else {
            pushDown(before, insert, before);
        }
        notifyStateChange();
    }


    /**
     * Inserts a node after (right of or bottom of) a given node by splitting the inserted
     * node with the given node.
     * @param insert    The node to be inserted
     * @param after     The node that the inserted node will be split with.
     * @param direction The direction of the split
     */
    public void insertNodeAfter(Node insert, Node after, int direction) {
        if (isSplitterDirection(direction)) {
            double split = getSplit(after) / 2;
            setSplit(after, split);
            addChild(insert, children.indexOf(after) + 1, split);
        }
        else {
            pushDown(after, after, insert);
        }
        notifyStateChange();
    }

     /**
     * Inserts a node after (right of or bottom of) a given node by splitting the inserted
     * node with the given node.
     * @param insert    The node to be inserted
     * @param after     The node that the inserted node will be split with.
      *   @param split The weight
     */
    public void insertNodeAfter(Node insert, Node after, double split) {
        addChild(insert, children.indexOf(after) + 1, split);
        notifyStateChange();
    }

    /**
     * Inserts a node after (right of or bottom of) a given node by splitting the inserted
     * node with the given node.
     * @param insert    The node to be inserted.
     *                  @param index The index
     *                               @param split The split
     */
    public void insertNodeAt(Node insert, int index, double split) {
        addChild(insert, index, split);
        notifyStateChange();
    }

    protected void stateChanged() {
        splitters = null;
    }


    /**
     * Replaces a child node with another node
     * @param current The child node which should be replaced
     * @param with    The node that the child node should be replaced with
     */
    public void replaceChild(Node current, Node with) {
        double currentSplit = nodeSplits.get(current);
        int index = children.indexOf(current);
        children.remove(current);
        addChild(with, index, currentSplit);
        notifyStateChange();
    }


    protected abstract SplitterNode createPerpendicularSplitterNode(List<Node> children, List<Double> splits);


    private SplitterNode pushDown(Node existingChild, Node node0, Node node1) {
        double split = getSplit(existingChild) / 2;
        SplitterNode sn = createPerpendicularSplitterNode(Arrays.asList(node0, node1), Arrays.asList(split, split));
        replaceChild(existingChild, sn);
        return sn;
    }
}
