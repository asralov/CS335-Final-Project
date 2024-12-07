package controller; 

import java.util.ArrayList;
import java.util.EventObject;

import model.Piece;

public class GetMovedPieces extends EventObject {
		private final ArrayList<Piece> pieces;

		public GetMovedPieces(Object source, ArrayList<Piece> pieces) {
			super(source);
			this.pieces = pieces;
		}

		public ArrayList<Piece> getPieces() {
			return pieces;
		}
	}