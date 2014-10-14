/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tetris;

import AIHelper.BoardRater;
import AIHelper.FinalRater1;

/**
 *
 * @author justinbehymer
 */
public class ITLPAI implements AI 
{
    
BoardRater boardRater = new FinalRater1();
    
public Move bestMove(Board board, Piece piece, Piece nextPiece, int limitHeight) 
{
		double bestScore = Double.NEGATIVE_INFINITY;
		int bestX = -1;
		int bestY = -1;
		Piece bestPiece = piece;
		Piece current = piece;

		// loop through all the rotations
		do {
			int clears = 0;
			final int yBound = limitHeight - current.getHeight() + 1;
			final int xBound = board.getWidth() - current.getWidth() + 1;

			// For current rotation, try all the possible columns
			for (int x = 0; x < xBound; x++) {
				int y = board.dropHeight(current, x);
				// piece does not stick up too far
				if ((y < yBound) && board.canPlace(current, x, y)) {
					Board testBoard = new Board(board);
					testBoard.place(current, x, y);
			        for(int i=0; i < board.getWidth(); i++)
					//testBoard.clearRows();
					clears = testBoard.clearRows();
					double score = boardRater.rateBoard(testBoard, piece);
					score += clears*4.49355334;//3.4181268101392694;
					//CHECK THE NEXT PIECE
					double newbestScore = 0;
					//int newbestX = 0;
					//int newbestY = 0;
					Piece newcurrent = nextPiece;

					// loop through all the rotations
					do {
						int newclears = 0;
						final int newyBound = limitHeight - newcurrent.getHeight() + 1;
						final int newxBound = board.getWidth() - newcurrent.getWidth() + 1;

						// For current rotation, try all the possible columns
						for (int x1 = 0; x1 < newxBound; x1++) {
							int y1 = board.dropHeight(newcurrent, x1);
							// piece does not stick up too far
							if ((y1 < newyBound) && board.canPlace(newcurrent, x1, y1)) {
								testBoard.place(current, x, y);
						        for(int i1=0; i1 < board.getWidth(); i1++)
								//testBoard.clearRows();
								newclears = testBoard.clearRows();
								double newscore = boardRater.rateBoard(testBoard, nextPiece);
								newscore += newclears*4.49355334;//3.4181268101392694;

								if (newscore > newbestScore) {
									newbestScore = newscore;
									//newbestX = x1;
									//newbestY = y1;
									//newbestPiece = current;
								}
							}
						}

						newcurrent = newcurrent.nextRotation();
					} while (newcurrent != nextPiece);
					score += newbestScore;

					if (score > bestScore) {
						bestScore = score;
						bestX = x;
						bestY = y;
						bestPiece = current;
					}
				}
			}

			current = current.nextRotation();
		} while (current != piece);

		Move move = new Move();
		move.x = bestX;
		move.y = bestY;
		move.piece = bestPiece;
		return (move);
	}
	@Override
	public void setRater(BoardRater r) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}