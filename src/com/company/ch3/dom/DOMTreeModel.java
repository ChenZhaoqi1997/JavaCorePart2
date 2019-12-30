package com.company.ch3.dom;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.event.TreeModelListener;
import javax.swing.text.Document;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class DOMTreeModel implements TreeModel {
    private Document doc;
    public DOMTreeModel(Document doc) {
        this.doc = doc;
    }
    @Override
    public Object getRoot() {
        return doc.getDefaultRootElement();
    }
    @Override
    public Object getChild(Object parent, int index) {
        Node node = (Node)parent;
        NodeList list = node.getChildNodes();
        return list.item(index);
    }
    @Override
    public int getChildCount(Object parent) {
        Node node = (Node)parent;
        NodeList list = node.getChildNodes();
        return list.getLength();
    }
    @Override
    public boolean isLeaf(Object node) {
        return getChildCount(node) == 0;
    }
    @Override
    public int getIndexOfChild(Object parent, Object child) {
        Node node = (Node)parent;
        NodeList list = node.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            if (getChild(node, i) == child) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {}
    @Override
    public void addTreeModelListener(TreeModelListener l) {}
    @Override
    public void removeTreeModelListener(TreeModelListener l) {}
}
