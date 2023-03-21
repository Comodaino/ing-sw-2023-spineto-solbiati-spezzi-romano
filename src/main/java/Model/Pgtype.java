package Model;
public class Pgtype {
    private int line,col;
    private Color color;
    public Pgtype(int line, int col, Color color) {
        this.line = line;
        this.col = col;
        this.color = color;
    }
    public int getLine(){
        return line;
    }
    public void setLine(int line){
        this.line=line;
    }
    public int getCol() {
        return col;
    }
    public void setCol(int col) {
        this.col = col;
    }
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}

