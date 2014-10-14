package Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Random;

import tetris.Move;
import tetris.Piece;
import tetris.Board;
import tetris.AI1;

public class Evaluate {
	private static final Piece[] pieces = Piece.getPieces();

	private int gridHeight;
	private int gridWidth;

	private Random rand = new Random();

	public Evaluate(int gridHeight, int gridWidth) {
		this.gridHeight = gridHeight;
		this.gridWidth = gridWidth;
	}

	public double evalN(AI1 ai, int seekSize, int n) {
		double total = 0;
		for (int i = 0; i < n; i++) {
			int score = evalOnce(ai, seekSize);
			total += score / n;
		}
		return total;
	}

	public int evalOnce(AI1 ai, int seekSize) {
		Deque<Piece> next = new ArrayDeque<Piece>(seekSize);
		for (int i = 0; i < seekSize; i++) {
			next.addLast(pieces[rand.nextInt(pieces.length)]);
		}
		Board grid = new Board(gridHeight, gridWidth);

		int score = 0;
		while (true) {
			Piece t = next.removeFirst();
			next.addLast(pieces[rand.nextInt(pieces.length)]);
			Piece t1 = next.removeFirst();
			next.addLast(pieces[rand.nextInt(pieces.length)]);
			Move move = ai.bestMove(new Board(grid), t, t1, 20);
			if (move == null)
				break;
			int column = move.x;
			int dropRow = grid.getColumnHeight(column);
			if (dropRow >= grid.getHeight())
				break;

			grid.place(move);
			score += grid.clearRows();
		}

		return score;
	}
}
