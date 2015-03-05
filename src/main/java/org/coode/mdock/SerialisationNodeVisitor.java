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
import java.util.List;
import java.util.Stack;
import java.util.Map;
import java.awt.*;


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
 */
public class SerialisationNodeVisitor extends TraversalNodeVisitor {

    private Document doc;

    private Stack<Element> elementStack;

    private ComponentPropertiesFactory componentPropertiesFactory;

    public SerialisationNodeVisitor(Document doc, ComponentPropertiesFactory factory) {
        this.doc = doc;
        this.componentPropertiesFactory = factory;
        elementStack = new Stack<Element>();
        Element element = doc.createElement("layout");
        doc.appendChild(element);
        elementStack.push(element);
    }

    public void visit(ComponentNode componentNode) {
        super.visit(componentNode);
        Element element = doc.createElement(Vocabulary.COMPONENT_NODE);
        elementStack.peek().appendChild(element);
        for(JComponent component : componentNode.getComponents()) {
            Element componentElement = doc.createElement(Vocabulary.COMPONENT);
            element.appendChild(componentElement);
            componentElement.setAttribute(Vocabulary.LABEL, componentNode.getLabel(component));
            Map<String, String> properties = getProperties(component);
            for(String key : properties.keySet()) {
                Element propertyElement = doc.createElement(Vocabulary.PROPERTY);
                componentElement.appendChild(propertyElement);
                propertyElement.setAttribute(Vocabulary.ID, key);
                propertyElement.setAttribute(Vocabulary.VALUE, properties.get(key));
            }
        }
    }

    public void visit(VerticalSplitterNode verticalSplitterNode) {
        Element element = doc.createElement(Vocabulary.VERTICAL_SPLITTER_NODE);
        element.setAttribute(Vocabulary.SPLITS, getSplitAttributeValue(verticalSplitterNode.getSplits()));
        elementStack.peek().appendChild(element);
        elementStack.push(element);
        super.visit(verticalSplitterNode);
        elementStack.pop();

    }

    public void visit(HorizontalSplitterNode horizontalSplitterNode) {
        Element element = doc.createElement(Vocabulary.HORIZONTAL_SPLITTER_NODE);
        element.setAttribute(Vocabulary.SPLITS, getSplitAttributeValue(horizontalSplitterNode.getSplits()));
        elementStack.peek().appendChild(element);
        elementStack.push(element);
        super.visit(horizontalSplitterNode);
        elementStack.pop();
    }

    protected Map<String, String> getProperties(JComponent component) {
        return componentPropertiesFactory.getProperties(component);
    }

    private String getSplitAttributeValue(List<Double> splits) {
        StringBuilder sb = new StringBuilder();
        for(double d : splits) {
            sb.append(d);
            sb.append(" ");
        }
        return sb.toString().trim();
    }
}
