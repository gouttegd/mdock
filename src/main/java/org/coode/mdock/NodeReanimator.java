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

import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.swing.*;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.List;
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
 *
 * Creates a tree of <code>Node</code>s from a given node descriptor (node XML file).
 */
public class NodeReanimator extends DefaultHandler {

    private ComponentFactory componentFactory;

    private Stack<NodeElement> elementStack;

    private ComponentElement currentComponentElement;

    private SplitterNode rootNode;


    /**
     * Creates a <code>NodeReanimator</code> which will read an XML node
     * descriptor from a given reader.
     * @param is The reader which a node descriptor will be read from
     * @param componentFactory The component factory which should be used
     * to create the components that are contained in component nodes.  This
     * factory is application specific.
     */
    public NodeReanimator(Reader is, ComponentFactory componentFactory) {
        this.componentFactory = componentFactory;
        elementStack = new Stack<NodeElement>();
        try {
            SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
            saxParser.parse(new InputSource(is), this);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SplitterNode getRootNode() {
        return rootNode;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if(qName.equals(Vocabulary.COMPONENT_NODE)) {
            NodeElement element = new ComponentNodeElement();
            elementStack.push(element);
        }
        else if(qName.equals(Vocabulary.HORIZONTAL_SPLITTER_NODE)) {
            NodeElement element = new HorizontalSplitterElement(getSplits(attributes));
            elementStack.push(element);
        }
        else if(qName.equals(Vocabulary.VERTICAL_SPLITTER_NODE)) {
            NodeElement element = new VerticalSplitterElement(getSplits(attributes));
            elementStack.push(element);
        }
        else if(qName.equals(Vocabulary.COMPONENT)) {
            currentComponentElement = new ComponentElement(attributes.getValue(Vocabulary.LABEL));
        }
        else if(qName.equals(Vocabulary.PROPERTY)) {
            currentComponentElement.putProperty(attributes.getValue(Vocabulary.ID), attributes.getValue(Vocabulary.VALUE));
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(qName.equals(Vocabulary.COMPONENT_NODE)) {
            NodeElement element = elementStack.pop();
            elementStack.peek().addChildNodeElement(element);
        }
        else if(qName.equals(Vocabulary.HORIZONTAL_SPLITTER_NODE)) {
            NodeElement element = elementStack.pop();
            if(elementStack.isEmpty()) {
                // Root
                rootNode = (SplitterNode) element.createNode();
            }
            else {
                elementStack.peek().addChildNodeElement(element);
            }
        }
        else if(qName.equals(Vocabulary.VERTICAL_SPLITTER_NODE)) {
            NodeElement element = elementStack.pop();
            if(elementStack.isEmpty()) {
                // Root
                rootNode = (SplitterNode) element.createNode();
            }
            else {
                elementStack.peek().addChildNodeElement(element);
            }
        }
        else if(qName.equals(Vocabulary.COMPONENT)) {
            ComponentNodeElement element = (ComponentNodeElement) elementStack.peek();
            element.addComponentElement(currentComponentElement);
        }
    }


    private static List<Double> getSplits(Attributes attributes) {
        String splits = attributes.getValue(Vocabulary.SPLITS);
        if(splits != null) {
            return parseSplits(splits);
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }

    private static List<Double> parseSplits(String splits) {
        List<Double> list = new ArrayList<Double>();
        StringTokenizer tokenizer = new StringTokenizer(splits.trim(), " ");
        while(tokenizer.hasMoreTokens()) {
            String curTok = tokenizer.nextToken();
            list.add(Double.parseDouble(curTok));
        }
        return list;
    }




    private abstract class NodeElement {

        private List<NodeElement> children;

        public NodeElement() {
            this.children = new ArrayList<NodeElement>();
        }

        public void addChildNodeElement(NodeElement child) {
            children.add(child);
        }

        public abstract Node createNode();

        public List<Node> createChildNodes() {
            List<Node> childNodes = new ArrayList<Node>();
            for(NodeElement element : children) {
                childNodes.add(element.createNode());
            }
            return childNodes;
        }
    }

    private class ComponentNodeElement extends NodeElement {

        private List<ComponentElement> componentElements;

        public ComponentNodeElement() {
            componentElements = new ArrayList<ComponentElement>();
        }

        public void addComponentElement(ComponentElement componentElement) {
            componentElements.add(componentElement);
        }

        public Node createNode() {
            ComponentNode cn = new ComponentNode();
            for(ComponentElement element : componentElements) {
                cn.add(element.createComponent(), element.getLabel());
            }
            return cn;
        }
    }

    private abstract class SplitterElement extends NodeElement {

        private List<Double> splits;

        protected SplitterElement(List<Double> splits) {
            this.splits = splits;
        }

        public List<Double> getSplits() {
            return splits;
        }
    }

    private class HorizontalSplitterElement extends SplitterElement {

        public HorizontalSplitterElement(List<Double> splits) {
            super(splits);
        }

        public Node createNode() {
            return new HorizontalSplitterNode(createChildNodes(), getSplits());
        }
    }

    private class VerticalSplitterElement extends SplitterElement {

        public VerticalSplitterElement(List<Double> splits) {
            super(splits);
        }

        public Node createNode() {
            return new VerticalSplitterNode(createChildNodes(), getSplits());
        }
    }


    private class ComponentElement {

        private String label;

        private Map<String, String> properties;

        public ComponentElement(String label) {
            this.label = label;
            properties = new HashMap<String, String>();
        }

        public String getLabel() {
            return label;
        }

        public Map<String, String> getProperties() {
            return properties;
        }

        public void putProperty(String id, String val) {
            properties.put(id, val);
        }

        public JComponent createComponent() {
            return componentFactory.createComponent(properties);
        }
    }
}
