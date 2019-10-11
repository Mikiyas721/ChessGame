package chessEngine;

public class Move {
    private Position initial;
    private Position terminal;
    public Move(Position initial,Position terminal){
        this.initial = initial;
        this.terminal = terminal;
    }
    public Position getInitial() {
        return initial;
    }

    public Position getTerminal() {
        return terminal;
    }

    public void setInitial(Position initial) {
        this.initial = initial;
    }

    public void setTerminal(Position terminal) {
        this.terminal = terminal;
    }



}
