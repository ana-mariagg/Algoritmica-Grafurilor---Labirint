public class Node {
    private int Row;
    private int Column;
    private boolean Marginal;
    public Node predecessor;

    public int getRow() {
        return this.Row;
    }

    public int getColumn() {
        return this.Column;
    }

    public boolean getMarginal(){
        return this.Marginal;
    }

    //constructorul
    public Node(int row,int column,boolean marginal){
        this.Column=column;
        this.Row=row;
        this.Marginal=marginal;
    }
}
