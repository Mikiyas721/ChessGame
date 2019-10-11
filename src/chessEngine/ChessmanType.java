package chessEngine;

public enum ChessmanType {
    WHITE,
    BLACK;
    public ChessmanType getOpponent(){
        return this==WHITE?BLACK:WHITE;
    }

}
