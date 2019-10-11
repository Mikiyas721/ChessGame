package chessEngine.player;

import chessEngine.Board;
import chessEngine.*;
import chessEngine.pieces.*;


import java.util.Comparator;
import java.util.List;

public class ComputerPlayer extends Player implements Runnable {
    private Board tempBoard;
    private ComputerListner computerListner;

    public int getDepth() {
        return depth;
    }

    private int depth;

    public ComputerPlayer(ChessmanType chessmanType,int depth) {
        super(chessmanType);
        this.depth = depth;
    }
    public void addTextListner(ComputerListner computerListner){
        this.computerListner = computerListner;
    }
    @Override
    public void canMove(Board board) {
        tempBoard = board;
        new Thread(this).start();


    }


    @Override
    public void run() {
        computerListner.setText();
        List<Move> moves = tempBoard.getAllAvailableMoveFor(chessmanType);
        Move move = moves.stream().max(Comparator.comparingInt(move1 -> min(tempBoard.copyAndMove(move1), Integer.MIN_VALUE, Integer.MAX_VALUE, depth))).get();
        computerListner.clearText();
        tempBoard.getChessmanAt(move.getInitial()).move(move.getTerminal(), tempBoard);
        fireMoveEvent();
        tempBoard = null;

    }

    private int min(Board board, int alpha, int beta, int depth) {
        //System.out.println("Min");
        if (depth == 0) return evaluateBoard(board);
        else {
            int value = Integer.MAX_VALUE;
            List<Move> available = board.getAllAvailableMoveFor(chessmanType.getOpponent());
            //System.out.println(available.size() + " available moves for minimizing player");
            if (available.isEmpty()) return value;
            int c=0;
            for (Move move : available) {
                //System.out.println("calulating Move "+ ++c+" of minimizing player");
                value = Math.min(value, max(board.copyAndMove(move), alpha, beta, depth - 1));
                beta = Math.min(value, beta);
                if (beta <= alpha) break;
            }
            return value;
        }
    }

    private int max(Board board, int alpha, int beta, int depth) {
        //System.out.println("Max");
        if (depth == 0) return evaluateBoard(board);
        else {
            int value = Integer.MIN_VALUE;
            List<Move> available = board.getAllAvailableMoveFor(chessmanType);
           // System.out.println(available.size() + " available moves for maximizing player");
            if (available.isEmpty()) return value;
            int c = 0;
            for (Move move : available) {
               // System.out.println("calulating Move "+ ++c+" of maximizing player");
                value = Math.max(value, min(board.copyAndMove(move), alpha, beta, depth - 1));
                alpha = Math.max(value, alpha);
                if (alpha >= beta) break;
            }
            return value;
        }
    }

    private int evaluateBoard(Board copyBoard) {
        int computer;
        int human;
        //Material
        computer = 3 * copyBoard.getFriends(chessmanType).stream().mapToInt(Chessman::getValue).sum();
        human = 3 * copyBoard.getOpponents(chessmanType).stream().mapToInt(Chessman::getValue).sum();
        //Development
        computer += copyBoard.getAvailableMoveCount(chessmanType);
        human += copyBoard.getAvailableMoveCount(chessmanType.getOpponent());
        //center control
        computer += evaluateBoardByCenterPosition(chessmanType, copyBoard);
        human += evaluateBoardByCenterPosition(chessmanType.getOpponent(), copyBoard);
        //King safety
        computer += evaluateByKingProtection(chessmanType, copyBoard);
        human += evaluateByKingProtection(chessmanType.getOpponent(), copyBoard);
        //pawn Structure
        computer -= copyBoard.getFaultyPawn(chessmanType);
        human -= copyBoard.getFaultyPawn(chessmanType.getOpponent());
        return computer - human;
    }

    private int evaluateBoardByCenterPosition(ChessmanType chessmanType, Board board) {
        return board.getFriends(chessmanType).stream().mapToInt(chessman ->
                calcDistance(chessman.getRow(), chessman.getColumn(), 3, 3)
        ).sum();
    }

    private int calcDistance(int i1, int j1, int i2, int j2) {
        return (int) Math.sqrt(Math.pow(i1 - i2, 2) + Math.pow(j1 - j2, 2));
    }

    private int evaluateByKingProtection(ChessmanType chessmanType, Board board) {
        King k = board.getKing(chessmanType);
        return board.getFriends(chessmanType).stream()
                .filter(it -> !(it instanceof King))
                .mapToInt(it -> calcDistance(it.getRow(), it.getColumn(), k.getRow(), k.getColumn()))
                .filter(it -> it == 1)
                .sum();
    }


}
