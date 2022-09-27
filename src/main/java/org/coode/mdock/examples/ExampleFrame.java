package org.coode.mdock.examples;
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

import org.coode.mdock.*;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import java.util.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.StringWriter;
import java.io.StringReader;


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
 * Provides a very simple example of how to use a node panel
 * and a dynamic config panel to create and arrange components
 * in the node panel.
 */
public class ExampleFrame extends JFrame {

    private JMenu menu;

    private NodePanel nodePanel;

    private SplitterNode rootNode;

    /**
     * The panel which is used to configure components in the
     * node panel.
     */
    private DynamicConfigPanel dynamicConfigPanel;


    public ExampleFrame() {
        createMenuBar();
        // The node which will be the root of the node panel.  In this case, we
        // simply want an empty node.
        rootNode = new VerticalSplitterNode(new ArrayList<Node>(), new ArrayList<Double>());

        // The component that contains the "tree map" layout of the various
        // components
        nodePanel = new NodePanel(rootNode);

        setContentPane(nodePanel);

        // The panel which will handle the "drag and drop" of new nodes.
        dynamicConfigPanel = new DynamicConfigPanel(nodePanel);


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    private void createMenuBar() {
        menu = new JMenu("Nodes");
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);
        setJMenuBar(menuBar);

        menu.add(new AbstractAction("Add...") {
            public void actionPerformed(ActionEvent e) {
                addNewComponent();
            }
        });

        menu.add(new AbstractAction("Dump") {
            public void actionPerformed(ActionEvent e) {
                dumpAndReanimate();
            }
        });
    }


    private void dumpAndReanimate() {
        try {

            StringWriter sw = new StringWriter();
            NodeSerialiser serialiser = new NodeSerialiser(rootNode, new ComponentPropertiesFactory() {
                public Map<String, String> getProperties(JComponent component) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("class", component.getClass().getName());
                    return map;
                }
            }, sw);
            serialiser.serialise();


            System.out.println(sw.getBuffer().toString());

            StringReader sr = new StringReader(sw.getBuffer().toString());
            NodeReanimator reanimator = new NodeReanimator(sr, new ComponentFactory() {
                public JComponent createComponent(Map<String, String> properties) {
                    String cls = properties.get("class");
                    try {
                        return (JComponent) Class.forName(cls).newInstance();
                    }
                    catch (InstantiationException e1) {
                        e1.printStackTrace();
                    }
                    catch (IllegalAccessException e1) {
                        e1.printStackTrace();
                    }
                    catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                    return new JButton("Error");
                }
            });

            SplitterNode sn = reanimator.getRootNode();

            NodePanel np = new NodePanel(sn);
            JFrame f = new JFrame();
            f.setContentPane(np);
            f.setVisible(true);
        }
        catch (ParserConfigurationException e1) {
            e1.printStackTrace();
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        catch (TransformerFactoryConfigurationError e1) {
            e1.printStackTrace();
        }
        catch (TransformerException e1) {
            e1.printStackTrace();
        }
    }


    private void addNewComponent() {
        final JComponent comp = createDummyComponent();
        final JMenuItem item = new JMenuItem(new AbstractAction("Remove " + comp.hashCode()) {
            public void actionPerformed(ActionEvent e) {
                comp.getParent().remove(comp);
                nodePanel.rebuild();
                setEnabled(false);
            }
        });
        menu.add(item);
        dynamicConfigPanel.setCurrentComponent(comp, "Text Area");
        dynamicConfigPanel.activate();
    }


    private static JComponent createDummyComponent() {
        final JTextArea textArea = new JTextArea();
        final JScrollPane sp = new JScrollPane(textArea);
        for (int i = 0; i < 30; i++) {
            textArea.append("wfukhwe w erlfjh jwhegr fhyyg werr\n");
        }
        return sp;
    }


    public static void main(String[] args) {

        JFrame f = new ExampleFrame();
        f.setSize(800, 600);
        f.setVisible(true);
    }
}
