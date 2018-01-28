package model;
import java.util.ArrayList;

public class Node<T> {
    /**
     * Directory Object stored in this Node.
     */
    private Directory dir;
    /**
     * ArrayList of this children.
     */
    private ArrayList<Node<T>> children = new ArrayList<>();
    /**
     * Parent Node.
     */
    private Node<T> parent;

    /**
     * Creates a Node object containing directory.
     *
     * @param directory The directory to be stored in this Node.
     */
    protected Node(Directory directory){
        this.dir = directory;
    }

    /**
     * Returns the children of this node. In other words, return ArrayList of Nodes that has this Node as their parent.
     * @return ArrayList of Nodes.
     */
    public ArrayList<Node<T>> getChildren(){
        return this.children;
    }

    /**
     * Adds a new Node containing dir to this node's children.
     * @param dir Directory to be added to this children.
     */
    protected void addChildren(Directory dir){
        Node<T> child = new Node<>(dir);
        this.children.add(child);
        child.setParent(this);
    }

    /**
     * Sets the parent of this Node to parent.
     * @param parent Node to set parent to.
     */
    protected void setParent(Node<T> parent){
        this.parent = parent;
    }

    /**
     * Returns the Directory object contained in this Node.
     * @return This Directory.
     */
    public Directory getDir(){
        return this.dir;
    }
}
