package core.trees;

public interface NodeInsertion {
    Node insert(LitNode node);
    Node insert(BracketNode node);
    Node insert(ConnNode node);
    Node insert(NegNode node);
}
