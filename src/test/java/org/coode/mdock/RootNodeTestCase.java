package org.coode.mdock;

import org.junit.Test;

import javax.swing.*;
import java.util.Arrays;

import static junit.framework.Assert.assertEquals;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 23-Sep-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class RootNodeTestCase {

    @Test
    public void testComponentNode() {
        ComponentNode node = new ComponentNode();
        assertEquals(node.getGloballyNormalisedWidth(), 1.0);
        assertEquals(node.getGloballyNormalisedHeight(), 1.0);

    }

    @Test
    public void testVerticslSplitter() {
        ComponentNode child0 = new ComponentNode();
        child0.add(new JButton("Child0"), "Child0");
        ComponentNode child1 = new ComponentNode();
        child1.add(new JButton("Child1"), "Child1");
        ComponentNode child2 = new ComponentNode();
        child2.add(new JButton("Child2"), "Child2");

        VerticalSplitterNode verticalSplitterNode = new VerticalSplitterNode(
                Arrays.asList(child0, child1, child2),
                Arrays.asList(0.5, 1.5, 2.0));
        assertEquals(verticalSplitterNode.getGloballyNormalisedWidth(), 1.0);
        assertEquals(verticalSplitterNode.getGloballyNormalisedHeight(), 1.0);

        assertEquals(verticalSplitterNode.getSplit(child0), 0.5);
        assertEquals(verticalSplitterNode.getSplit(child1), 1.5);
        assertEquals(verticalSplitterNode.getSplit(child2), 2.0);

        assertEquals(verticalSplitterNode.getChildSpan(), 4.0);


        assertEquals(verticalSplitterNode.getNormalisedSplit(child0), (0.5 / 4.0));
        assertEquals(verticalSplitterNode.getNormalisedSplit(child1), (1.5 / 4.0));
        assertEquals(verticalSplitterNode.getNormalisedSplit(child2), (2.0 / 4.0));



    }

    @Test
    public void testHorizontalSplitter() {
        ComponentNode child0 = new ComponentNode();
        child0.add(new JButton("Child0"), "Child0");
        ComponentNode child1 = new ComponentNode();
        child1.add(new JButton("Child1"), "Child1");
        ComponentNode child2 = new ComponentNode();
        child2.add(new JButton("Child2"), "Child2");

        HorizontalSplitterNode horizontalSplitterNode = new HorizontalSplitterNode(
                Arrays.asList(child0, child1, child2),
                Arrays.asList(0.5, 1.5, 2.0));
        assertEquals(horizontalSplitterNode.getGloballyNormalisedWidth(), 1.0);
        assertEquals(horizontalSplitterNode.getGloballyNormalisedHeight(), 1.0);

        assertEquals(horizontalSplitterNode.getSplit(child0), 0.5);
        assertEquals(horizontalSplitterNode.getSplit(child1), 1.5);
        assertEquals(horizontalSplitterNode.getSplit(child2), 2.0);

        assertEquals(horizontalSplitterNode.getChildSpan(), 4.0);


        assertEquals(horizontalSplitterNode.getNormalisedSplit(child0), (0.5 / 4.0));
        assertEquals(horizontalSplitterNode.getNormalisedSplit(child1), (1.5 / 4.0));
        assertEquals(horizontalSplitterNode.getNormalisedSplit(child2), (2.0 / 4.0));
    }

    @Test
    public void testNestedNodes() {
        ComponentNode child0 = new ComponentNode();
        child0.add(new JButton("Child0"), "Child0");
        ComponentNode child1 = new ComponentNode();
        child1.add(new JButton("Child1"), "Child1");
        ComponentNode child2 = new ComponentNode();
        child2.add(new JButton("Child2"), "Child2");
        
        VerticalSplitterNode vNode0 = new VerticalSplitterNode(Arrays.asList(child0, child1), Arrays.asList(0.4, 1.6));

        assertEquals(vNode0.getGloballyNormalisedWidth(), 1.0);
        assertEquals(vNode0.getGloballyNormalisedHeight(), 1.0);

        assertEquals(child0.getGloballyNormalisedWidth(), 0.2);
        assertEquals(child0.getGloballyNormalisedHeight(), 1.0);
        assertEquals(child1.getGloballyNormalisedWidth(), 0.8);
        assertEquals(child1.getGloballyNormalisedHeight(), 1.0);

        HorizontalSplitterNode hNode0 = new HorizontalSplitterNode(Arrays.asList(vNode0, child2), Arrays.asList(0.5, 1.0));


        assertEquals(hNode0.getGloballyNormalisedWidth(), 1.0);
        assertEquals(hNode0.getGloballyNormalisedHeight(), 1.0);

        assertEquals(vNode0.getGloballyNormalisedWidth(), 1.0);
        assertEquals(vNode0.getGloballyNormalisedHeight(), 1.0 / 3.0);

        assertEquals(child0.getGloballyNormalisedWidth(), 0.2);
        assertEquals(child0.getGloballyNormalisedHeight(), 1.0 / 3.0);
        assertEquals(child1.getGloballyNormalisedWidth(), 0.8);
        assertEquals(child1.getGloballyNormalisedHeight(), 1.0 / 3.0);

        assertEquals(child2.getGloballyNormalisedWidth(), 1.0);
        assertEquals(child2.getGloballyNormalisedHeight(), 2.0 / 3.0);



    }

}
