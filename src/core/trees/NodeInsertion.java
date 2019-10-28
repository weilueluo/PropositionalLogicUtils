package core.trees;

public interface NodeInsertion {
    Node insert(LitNode node);
    Node insert(BoxNode node);
    Node insert(ConnNode node);
    Node insert(NegNode node);
}
