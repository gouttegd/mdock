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

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import java.io.Writer;
import java.io.StringWriter;
import java.io.IOException;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


/**
 * Author: Matthew Horridge
 * The University Of Manchester
 * Medical Informatics Group
 * Date: 26-Sep-2006
 *
 * matthew.horridge@cs.man.ac.uk
 * www.cs.man.ac.uk/~horridgm
 *
 * Developed as part of the CO-ODE project (http://www.co-ode.org)
 *
 * Given a node tree, a <code>NodeSerialiser</code> saves the node tree to
 * an node descriptor XML format.
 */
public class NodeSerialiser {

    private Node node;

    private ComponentPropertiesFactory factory;

    private Writer writer;


    /**
     * Serialises the specified node to a node descriptor using the specified writet.
     * @param node The node to be serialised.
     * @param factory The <code>ComponentPropertiesFactory</code> which should be used to
     * generate a set of properties for a given component which can be used to reanimate the
     * component as the descriptor is read from a <code>Reader</code>.
     * @param writer The <code>Writer</code> which the node descriptor should be written to.
     */
    public NodeSerialiser(Node node, ComponentPropertiesFactory factory, Writer writer) {
        this.node = node;
        this.factory = factory;
        this.writer = writer;
    }

    public void serialise() throws ParserConfigurationException, IOException {
        OutputFormat of = new OutputFormat();
        of.setIndent(4);
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        SerialisationNodeVisitor visitor = new SerialisationNodeVisitor(doc, factory);
        node.accept(visitor);
        XMLSerializer serializer = new XMLSerializer(writer, of);
        serializer.serialize(doc);
    }


}
