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
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;


/**
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 26-Sep-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 *
 * Developed as part of the CO-ODE project (http://www.co-ode.org)
 */
public class ComponentNodeTabbedPaneUI extends BasicTabbedPaneUI {

    private static final int TAB_HEIGHT = 16;

    protected void installDefaults() {
        super.installDefaults();
        tabAreaInsets.left = 0;
        selectedTabPadInsets = new Insets(0, 0, 0, 0);
        tabInsets = selectedTabPadInsets;
        tabPane.setFont(tabPane.getFont().deriveFont(Font.PLAIN, 10.0f));
    }


    /**
     * Paints the tabs in the tab area.
     * Invoked by paint().
     * The graphics parameter must be a valid <code>Graphics</code>
     * object.  Tab placement may be either:
     * <code>JTabbedPane.TOP</code>, <code>JTabbedPane.BOTTOM</code>,
     * <code>JTabbedPane.LEFT</code>, or <code>JTabbedPane.RIGHT</code>.
     * The selected index must be a valid tabbed pane tab index (0 to
     * tab count - 1, inclusive) or -1 if no tab is currently selected.
     * The handling of invalid parameters is unspecified.
     * @param g             the graphics object to use for rendering
     * @param tabPlacement  the placement for the tabs within the JTabbedPane
     * @param selectedIndex the tab index of the selected component
     * @since 1.4
     */
    protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
        super.paintTabArea(g, tabPlacement, selectedIndex);
    }


    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
    }


    protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title,
                             Rectangle textRect, boolean isSelected) {
        if (isSelected) {
            g.setColor(Color.DARK_GRAY.darker());
        }
        else {
            g.setColor(Color.GRAY);
        }
        g.drawString(title, textRect.x, textRect.y + metrics.getAscent());
        //super.paintText(g, tabPlacement, font, metrics, tabIndex, title, textRect, isSelected);
    }


    protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect,
                            Rectangle textRect) {
        super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect);
    }


    protected Insets getContentBorderInsets(int tabPlacement) {
        return new Insets(0, 0, 0, 0);
    }


    protected Insets getTabInsets(int tabPlacement, int tabIndex) {
        return new Insets(0, 0, 0, 0);
    }


    protected Insets getSelectedTabPadInsets(int tabPlacement) {
        return new Insets(0, 0, 0, 0);
    }


    protected Insets getTabAreaInsets(int tabPlacement) {
        return new Insets(1, 1, 1, 1);
    }


    protected int getTabLabelShiftX(int tabPlacement, int tabIndex, boolean isSelected) {
        Rectangle tabRect = rects[tabIndex];//return super.getTabLabelShiftX(tabPlacement, tabIndex, isSelected);
        return tabRect.width % 2;
    }


    protected int getTabLabelShiftY(int tabPlacement, int tabIndex, boolean isSelected) {
        return 0;
    }

//
//    /**
//     * Returns the bounds of the specified tab index.  The bounds are
//     * with respect to the JTabbedPane's coordinate space.
//     */
//    public Rectangle getTabBounds(JTabbedPane pane, int i) {
//        Rectangle r = new Rectangle(TAB_WIDTH * i, 0, TAB_WIDTH, TAB_HEIGHT);
//        return r;
//    }


    /**
     * this function draws the border around each tab
     * note that this function does now draw the background of the tab.
     * that is done elsewhere
     */
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
                                  boolean isSelected) {
        //super.paintTabBorder(g, tabPlacement, tabIndex, x, y, w, h, isSelected);
        if (isSelected) {
            g.setColor(Color.GRAY);
        }
        else {
            g.setColor(Color.LIGHT_GRAY);
        }
        g.drawRect(x + 1, y + 1, w - 2, h - 2);
    }


    protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex,
                                       Rectangle iconRect, Rectangle textRect, boolean isSelected) {
        //super.paintFocusIndicator(g, tabPlacement, rects, tabIndex, iconRect, textRect, isSelected);
    }


    /**
     * Returns the bounds of the specified tab index.  The bounds are
     * with respect to the JTabbedPane's coordinate space.
     */
    public Rectangle getTabBounds(JTabbedPane pane, int i) {
        return super.getTabBounds(pane, i);
    }


    protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
        if (tabPane.getComponentCount() > 1) {
            return TAB_HEIGHT;
        }
        else {
            return 0;
        }
    }


    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        return super.calculateTabWidth(tabPlacement, tabIndex, metrics) + 3;
    }


    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
                                      boolean isSelected) {
//        if(isSelected) {
//            g.setColor(Color.LIGHT_GRAY);
//            g.fillRect(x + 1,  y + 1, w - 2, h - 2);
//        }
        //super.paintTabBackground(g, tabPlacement, tabIndex, x, y, w, h, isSelected);
    }


    protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
        //super.paintContentBorder(g, tabPlacement, selectedIndex);
    }


    protected void paintContentBorderTopEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w,
                                             int h) {
        //super.paintContentBorderTopEdge(g, tabPlacement, selectedIndex, x, y, w, h);
    }


    protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w,
                                              int h) {
        //super.paintContentBorderLeftEdge(g, tabPlacement, selectedIndex, x, y, w, h);
    }


    protected void paintContentBorderBottomEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w,
                                                int h) {
        //super.paintContentBorderBottomEdge(g, tabPlacement, selectedIndex, x, y, w, h);
    }


    protected void paintContentBorderRightEdge(Graphics g, int tabPlacement, int selectedIndex, int x, int y, int w,
                                               int h) {
        //super.paintContentBorderRightEdge(g, tabPlacement, selectedIndex, x, y, w, h);
    }

//    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
//        return TAB_WIDTH;
//    }
}
