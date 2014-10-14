/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AIHelper;

import tetris.Board;
import tetris.Piece;

/**
 *
 * @author justinbehymer
 */
public abstract class BoardRater {
    
    int callCount = 0;
    int runTime = 0;
    
    abstract double rate(Board board, Piece piece);
    public double rateBoard(Board board, Piece piece)
    {
        this.callCount++;
        board.enableCaching();
        long start = System.nanoTime();
        double ret = this.rate(board, piece);
        this.runTime += System.nanoTime()-start;
        board.disableCaching();
        return ret;
        
    }
}
