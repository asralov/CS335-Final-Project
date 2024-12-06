package view;
import java.util.ArrayList;

import model.Piece;

public class MoveData {
    Piece startPiece;
    Piece endPiece;
    ArrayList<Piece> capturedPieces;

    public MoveData(Piece start, Piece end, ArrayList<Piece> captured) {
        startPiece = start;
        endPiece = end;
        capturedPieces = captured;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OLD LOCATION: ").append(startPiece).append("\nNEW LOCATION: ").append(endPiece).append("\n");
        for (Piece p : capturedPieces) {
            sb.append("\tEATEN PIECE: ").append(p).append("\n");
        }
        return sb.toString();
    }
}