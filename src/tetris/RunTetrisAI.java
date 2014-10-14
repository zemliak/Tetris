/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tetris;

import javax.swing.Box;

/**
 *
 * @author justinbehymer
 */
public class RunTetrisAI extends RunTetris {
    
    
	private static final long serialVersionUID = 1L;
	/*public double[]s = {
	6.836253620278539,    //clears
	-5.2912668,           //holeCOunter
	-9.000317650165533,   //(h + (p.getHeight() - 1) /2)
	-6.771194449452725,   //openWellSums
	-6.4357765736975505,  //rowTransitions
	-18.697390610890398,  //columnTransitions
	-16,                  //connectedHoles
	-7,                   //altitudeDifference
	-5,                   //pileHeight
	-3,                   //blocks
	-2                  //weightedBlocks
	};*/
	private AI mBrain = new AI1();
	private Move mMove;
	protected javax.swing.Timer timerAI;
	int current_count = -1;

	/** Creates new JBrainTetris */
	public RunTetrisAI(int width, int height) {
		super(width, height);
		
	}
	
	public void startGame() {
		super.startGame();
		// Create the Timer object and have it send
		//timerAI.start();
	}
	
	public void stopGame() {
		super.stopGame();
		//timerAI.stop();
	}
	
	public void tick(int verb) {
		if (tickAI()) {
			super.tick(verb);
		}
	}

	public boolean tickAI() {
		if (current_count != tc.count) {
			current_count = tc.count;
			mMove = mBrain.bestMove(new Board(tc.board), tc.currentMove.piece, tc.nextPiece, tc.board.getHeight()-TetrisController.TOP_SPACE);
		}
		
		if (!tc.currentMove.piece.equals(mMove.piece)) { 
			super.tick(TetrisController.ROTATE);
		} else if (tc.currentMove.x != mMove.x) {
			super.tick(((tc.currentMove.x < mMove.x) ? TetrisController.RIGHT : TetrisController.LEFT));
		} else {
			return true;
		}
		return false;
	}


	public java.awt.Container createControlPanel() {
		java.awt.Container panel2 = Box.createVerticalBox();
		panel2 = super.createControlPanel();


		return (panel2);
	}
}
    

