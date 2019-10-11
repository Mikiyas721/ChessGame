package chessEngine;

import chessEngine.pieces.*;
import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Board {
    private List<Chessman> chessmen = new ArrayList<>();

    public List<Chessman> getRemovedChessman() {
        return removedChessman;
    }

    private List<Chessman> removedChessman = new ArrayList<>();

    public Board() {

    }

    public List<Chessman> getChessmen() {
        return chessmen;
    }
    /*public Board(){
        setInitialPosition();
    }*/

    private static List<Chessman> getStandardChessmenPosition() {
        return new ArrayList<>(Arrays.asList(
                new Rook(ChessmanType.WHITE, 0, 0),
                new Rook(ChessmanType.WHITE, 0, 7),
                new Knight(ChessmanType.WHITE, 0, 1),
                new Knight(ChessmanType.WHITE, 0, 6),
                new Bishop(ChessmanType.WHITE, 0, 2),
                new Bishop(ChessmanType.WHITE, 0, 5),
                new Queen(ChessmanType.WHITE, 0, 3),
                new King(ChessmanType.WHITE, 0, 4),
                new Pawn(ChessmanType.WHITE, 1, 0),
                new Pawn(ChessmanType.WHITE, 1, 1),
                new Pawn(ChessmanType.WHITE, 1, 2),
                new Pawn(ChessmanType.WHITE, 1, 3),
                new Pawn(ChessmanType.WHITE, 1, 4),
                new Pawn(ChessmanType.WHITE, 1, 5),
                new Pawn(ChessmanType.WHITE, 1, 6),
                new Pawn(ChessmanType.WHITE, 1, 7),

                new Rook(ChessmanType.BLACK, 7, 7),
                new Rook(ChessmanType.BLACK, 7, 0),
                new Knight(ChessmanType.BLACK, 7, 6),
                new Knight(ChessmanType.BLACK, 7, 1),
                new Bishop(ChessmanType.BLACK, 7, 5),
                new Bishop(ChessmanType.BLACK, 7, 2),
                new Queen(ChessmanType.BLACK, 7, 3),
                new King(ChessmanType.BLACK, 7, 4),
                new Pawn(ChessmanType.BLACK, 6, 7),
                new Pawn(ChessmanType.BLACK, 6, 6),
                new Pawn(ChessmanType.BLACK, 6, 5),
                new Pawn(ChessmanType.BLACK, 6, 4),
                new Pawn(ChessmanType.BLACK, 6, 3),
                new Pawn(ChessmanType.BLACK, 6, 2),
                new Pawn(ChessmanType.BLACK, 6, 1),
                new Pawn(ChessmanType.BLACK, 6, 0)
        ));
    }

    public void setNormalInitialPosition() {
        chessmen.addAll(getStandardChessmenPosition());
    }

    public void setFlippedInitialPosition() {
        chessmen.addAll(
                getStandardChessmenPosition()
                        .stream()
                        .peek(chessman -> {
                            chessman.setPosition(chessman.getPosition().flipped());
                            chessman.setInitialPosition(chessman.getPosition());
                        }).collect(Collectors.toList()));
    }

    public Board copy() {
        Board copyBoard = new Board();
        List<Chessman> copy = new ArrayList<>();
        for (Chessman chess : chessmen) {
            copy.add(chess.copy());
        }
        copyBoard.chessmen = copy;
        return copyBoard;
    }

    public void removePiece(Chessman chessman) {

        chessmen.remove(chessman);
        removedChessman.add(chessman);
    }

    public void addPiece(Chessman chessman) {

        chessmen.add(chessman);
        removedChessman.remove(chessman);
    }

    public Chessman getChessmanAt(Position position) {
        return getChessmanAt(position.getRow(), position.getColumn());
    }

    public Chessman getChessmanAt(int row, int column) {
        for (Chessman chess : chessmen) {
            if (chess.getRow() == row && chess.getColumn() == column) {
                return chess;
            }
        }
        return null;
    }

    public List<Chessman> getOpponents(ChessmanType chessmanType) {
        List<Chessman> opponent = new ArrayList<>();
        for (Chessman chess : chessmen) {
            if (chess.getChessmanType() != chessmanType) {
                opponent.add(chess);
            }
        }
        return opponent;
    }

    public List<Chessman> getFriends(ChessmanType chessmanType) {
        List<Chessman> friends = new ArrayList<>();
        for (Chessman chess : chessmen) {
            if (chess.getChessmanType() == chessmanType) {
                friends.add(chess);
            }
        }
        return friends;
    }

    public List<Move> getAllAvailableMoveFor(ChessmanType chessmanType) {
        return getFriends(chessmanType).stream()
                .flatMap(chessman ->
                        Chessman.validMoves(chessman, this).stream()
                                .map(position -> new Move(chessman.getPosition(), position)))
                .collect(Collectors.toList());

    }

    public int getAvailableMoveCount(ChessmanType chessmanType) {
        return getFriends(chessmanType).stream().mapToInt(chessman -> Chessman.validMoves(chessman, this).size()).sum();
    }


    public King getKing(ChessmanType chessmanType) {
        King king = chessmen.stream().
                filter(chessman -> chessman instanceof King && chessman.getChessmanType() == chessmanType).
                map(chessman -> (King) chessman).findAny().get();//Error:No such element
        return king;
    }

    public List<Chessman> getRemovedByChessmanType(ChessmanType chessmanType) {
        List<Chessman> removed = getRemovedChessman();
        List<Chessman> chessmen = new ArrayList<>();
        if (removed!=null) {
            for (Chessman chessman : removed) {
                if (chessman.getChessmanType() == chessmanType) {
                    chessmen.add(chessman);
                }
            }
        }
        return chessmen;
    }

    public Rook getRook(ChessmanType chessmanType) {
        Rook king = chessmen.stream().
                filter(chessman -> chessman instanceof Rook && chessman.getChessmanType() == chessmanType).
                map(chessman -> (Rook) chessman).findAny().get();
        return king;
    }

    @Override
    public String toString() {
        return chessmen.stream().
                map(chessman -> chessman.getID() + chessman.getRow() + chessman.getColumn() + chessman.getInitialrow() + chessman.getInitialcolumn())
                .collect(Collectors.joining(","));
    }

    public void loadBoard(String saved) {

        List<Chessman> chessmanList = Arrays.stream(saved.split(","))
                .map(s -> {
                    char a = s.charAt(0);
                    int r = Integer.parseInt(String.valueOf(s.charAt(1)));
                    int c = Integer.parseInt(String.valueOf(s.charAt(2)));
                    int ir = Integer.parseInt(String.valueOf(s.charAt(3)));
                    int ic = Integer.parseInt(String.valueOf(s.charAt(4)));
                    System.out.println(a);
                    if (a == 'k') {
                        return new King(ChessmanType.WHITE, r, c, ir, ic);
                    } else if (a == 'K') {
                        return new King(ChessmanType.BLACK, r, c, ir, ic);
                    } else if (a == 'q') {
                        return new Queen(ChessmanType.WHITE, r, c, ir, ic);
                    } else if (a == 'Q') {
                        return new Queen(ChessmanType.BLACK, r, c, ir, ic);
                    } else if (a == 'b') {
                        return new Bishop(ChessmanType.WHITE, r, c, ir, ic);
                    } else if (a == 'B') {
                        return new Bishop(ChessmanType.BLACK, r, c, ir, ic);
                    } else if (a == 'h') {
                        return new Knight(ChessmanType.WHITE, r, c, ir, ic);
                    } else if (a == 'H') {
                        return new Knight(ChessmanType.BLACK, r, c, ir, ic);
                    } else if (a == 'r') {
                        return new Rook(ChessmanType.WHITE, r, c, ir, ic);
                    } else if (a == 'R') {
                        return new Rook(ChessmanType.BLACK, r, c, ir, ic);
                    } else if (a == 'p') {
                        return new Pawn(ChessmanType.WHITE, r, c, ir, ic);
                    } else if (a == 'P') {
                        return new Pawn(ChessmanType.BLACK, r, c, ir, ic);
                    } else return null;
                }).collect(Collectors.toList());
        chessmen.addAll(chessmanList);

    }

    public boolean valid(ChessmanType chessmanType) {
        boolean valid = false;
        List<Chessman> chessmen = this.getFriends(chessmanType);
        for (Chessman chessman : chessmen) {
            if (!Chessman.validMoves(chessman, this).isEmpty()) {
                valid = true;
                break;
            }
        }
        return valid;

    }

    public int getCenteredPieces(ChessmanType chessmanType) {
        int i = 0;
        List<Chessman> friends = getFriends(chessmanType);
        for (Chessman chessman : friends) {
            if ((chessman.getRow() == 4 && chessman.getColumn() == 4) ||
                    (chessman.getRow() == 4 && chessman.getColumn() == 5) ||
                    (chessman.getRow() == 5 && chessman.getColumn() == 4) ||
                    (chessman.getRow() == 5 && chessman.getColumn() == 5)) {
                if (chessman instanceof Pawn) i += 4;
                i++;
            }
        }
        return i;
    }


    public Board copyAndMove(Move move) {
        Board copyBoard = copy();
        copyBoard.getChessmanAt(move.getInitial()).move(move.getTerminal(), copyBoard);
        return copyBoard;
    }

    public int getFaultyPawn(ChessmanType chessmanType) {
        int i = 0;
        List<Pawn> pawns = chessmen.stream().filter(chessman -> chessman instanceof Pawn && chessman.getChessmanType() == chessmanType).map(chessman -> (Pawn) chessman).collect(Collectors.toList());
        for (Pawn p : pawns) {
            Chessman blockingChessman = getChessmanAt(p.getRow() + 1, p.getColumn());
            if (blockingChessman instanceof Pawn) {
                i++;
            }
            boolean check = false;
            for (int j = p.getRow() - 7; j < p.getRow() + 8; j++) {
                if (i < 0 || i > 7) continue;
                if (getChessmanAt(i, p.getColumn() - 1) != null || getChessmanAt(i, p.getColumn() + 1) != null) {
                    check = true;
                    break;
                }
            }
            if (!check) {
                i++;
            }
        }
        return i;
    }

}
