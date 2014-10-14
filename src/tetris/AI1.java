package tetris;

import java.awt.Point;

import AIHelper.BoardRater;

public class AI1 implements AI{
	/**This method selects the best move possible for the piece. The method loops through
	 * every possible column where the piece can be placed, and does this loop for each
	 * possible rotation of the piece. Based on the results of the scoreBoard method
	 * below, it determines the highest score possible, and thus the best move possible.
	 * @param board the current state of the game board
	 * @param piece the current piece being tested
	 * @param nextPiece the next piece that will be placed onto the board next
	 * @param limitHeight the height of the board
	 * @return the best move possible with this combination of board and piece
	 * */
	public Move bestMove(Board board, Piece piece, Piece nextPiece, int limitHeight) 
	{
			double bestScore = Double.NEGATIVE_INFINITY;
			int bestX = -1;
			int bestY = -1;
			Piece bestPiece = piece;
			Piece current = piece;
			do {
				int clears = 0;
				final int yBound = limitHeight - current.getHeight() + 1;
				final int xBound = board.getWidth() - current.getWidth() + 1;
				for (int x = 0; x < xBound; x++) {
					//try placing in all of the columns
					int y = board.dropHeight(current, x);
					if ((y < yBound) && board.canPlace(current, x, y)) {
						Board testBoard = new Board(board);
						testBoard.place(current, x, y);
						//clear the rows
						clears = testBoard.clearRows();
						//calculate the score for the placement of this current piece onto the board
						double score = scoreBoard(testBoard, piece, clears, y);
						//CHECK THE NEXT PIECE
						double newbestScore = 0;
						Piece newcurrent = nextPiece;
						do {
							int newclears = 0;
							final int newyBound = limitHeight - newcurrent.getHeight() + 1;
							final int newxBound = board.getWidth() - newcurrent.getWidth() + 1;

							for (int x1 = 0; x1 < newxBound; x1++) {
								//try dropping it into each column
								int y1 = board.dropHeight(newcurrent, x1);
								if ((y1 < newyBound) && board.canPlace(newcurrent, x1, y1)) {
									testBoard.place(current, x, y);
									newclears = testBoard.clearRows();
									//calculate the score for the next piece with the current piece already in
									double newscore = scoreBoard(testBoard, nextPiece, newclears, y1);
									if (newscore > newbestScore) {
										newbestScore = newscore;
									}
								}
							}

							newcurrent = newcurrent.nextRotation();//rotate it
						} while (newcurrent != nextPiece);//do loop for all rotations of the piece
						score += newbestScore;
						if (score > bestScore) {
							bestScore = score;
							bestX = x;
							bestY = y;
							bestPiece = current;
						}
					}
				}
				current = current.nextRotation();//rotate the piece
			} while (current != piece);//do loop for all rotations of the piece
			//set the best move to be returned
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
		
		/**These are the coefficients by which we will multiply the returned values
		 * of the methods that rate the board. These have been obtained using a 
		 * genetic algorithm.
		 * */
		public double []s = {-4.905962890625, -20.6781689453125, -4.2738046875, -4.8296445312500005, -11.334140624999998, -10.7455693359375};
		
		/**This is the method that scores the board, adding in all of the previous 
		 * functions.
		 * @param b the current state of the game board
		 * @param p the current piece being tested and placed onto the board
		 * @param clears the amount of rows cleared on this move
		 * @param h the height of the column the piece is being placed in
		 * @return the total score of the current board
		 * */
		double scoreBoard(Board b, Piece p, int clears, int h){
			int a0 = clears;
			int a1 = holeCounter(b);
			double a2 = h + (p.getHeight()-1)/2;
			int a3 = openWellSums(b);
			int a4 = rowTransitions(b);
			int a5 = columnTransitions(b);
			/*double a6 = altitudeDifference(b);
			double a7 = pileHeight(b);
			double a8 = blocks(b);
			double a9 = weightedBlocks(b);
			double a10 = connectedHoles(b);*/
			return a0*s[0] + a1*s[1] + a2*s[2] + a3*s[3] + a4*s[4] + a5*s[5] /*+ a6*s[6]
					+ a7*s[7] + a8*s[8] + a9*s[9] + a10*s[10] */;
		}
		
		/**Counts the amount of holes. A space is considered to be a hole when it doesn't
		 * contain a block and has a block covering it on top.
		 * @param board the current state of the game board
		 * @return the total amount of holes
		 * */
		int holeCounter(Board board){
			int amount = 0;
			for(int x = 0; x < board.getWidth(); x++){
				int y = board.getColumnHeight(x) - 2;			
				while (y>0) {
					if  (!(board.getGrid(x,y)))
							amount ++;
					--y;
				}
			}
			return amount;
		}
		
		/**The total number of column transitions. A column transition occurs when 
		 * an empty cell is adjacent(vertically) to a filled cell on the same 
		 * column and vice versa. 
		 * @param board the current state of the game board
		 * @return the total amount of column transitions on the game board.
		 */
		int columnTransitions(Board board){
			int answer = 1;
			for(int x=0; x < board.getWidth(); x++){
				for (int i = 0; i < board.getColumnHeight(x); i++){
					if(board.getGrid(x, i) != board.getGrid(x, i+1))
						answer++;
				}
			}
			return answer;
		}
		
		/**The total number of row transitions. A row transition occurs when 
		 * an empty cell is adjacent(horizontally) to a filled cell on the same row 
		 * and vice versa.
		 * @param board the current state of the game board
		 * @return the total amount of row transitions on the game board
		*/
		int rowTransitions(Board board){
			int answer = 0;
			for(int i = 0; i < board.getHeight(); i++){
				for(int x=0; x < board.getWidth()-1; x++){
					if(board.getGrid(x, i) != board.getGrid(x+1, i))
						answer++;
				}
			}
			return answer;
		}
		
		/**A well is a succession of empty cells such that their left cells and 
		 * right cells are both filled. The well must be open at the top. This method
		 * counts up the total amount of cells that are contained in all wells of the 
		 * board.
		 * @param board the current state of the game board
		 * @return the total amount of cells contained in open wells on the board
		 * 
		 * */
		
		int openWellSums(Board board){
			int wells = 0;
			//does the leftmost column
			if(board.getColumnHeight(1)>board.getColumnHeight(0))
				wells+=board.getColumnHeight(1)-board.getColumnHeight(0);
			//does the rightmost column
			if(board.getColumnHeight(8)>board.getColumnHeight(9))
				wells+=board.getColumnHeight(8)-board.getColumnHeight(9);
			//does everything in betwen
			for(int x = 1; x < board.getWidth()-1; ++x){
				if(board.getColumnHeight(x-1) > board.getColumnHeight(x) && board.getColumnHeight(x+1) > board.getColumnHeight(x)){
					if(board.getColumnHeight(x-1) < board.getColumnHeight(x+1))
						wells += board.getColumnHeight(x-1) - board.getColumnHeight(x);
					else if(board.getColumnHeight(x-1) > board.getColumnHeight(x+1))
						wells += board.getColumnHeight(x-1) - board.getColumnHeight(x);
				}
			}
			return wells;
		}
		
		/**The total amount of connected holes on the board. Connected holes are 
		 * wells but closed at the top. This counts the total amount of cells that
		 * are contained in connected wells on the board.
		 * @param board the current state of the game board
		 * @return the total amount of cells contained in connected holes on the board
		 * */
		int connectedHoles(Board board) {
			int count = 0;
			for (int c = 0; c < board.getWidth(); c++) {
				for (int r = board.getColumnHeight(c) - 2; r >= 0; r--) {
					if (!board.getGrid(r, c)) {
						count++;
						do {
							r--;
						} while (r >= 0 && !board.getGrid(r, c));
					}
				}
			}
			return count;
		}
		
		/**The difference in height between the highest column on the board and 
		 * the lowest column on the board.
		 * @param board the current state of the game board
		 * @return the difference between highest and lowest column
		 * */
		int altitudeDifference(Board board){
			int max = 0;
			int min = 20;
			for(int x=0; x < board.getWidth(); x++){
				if(board.getColumnHeight(x) > max)
					max = board.getColumnHeight(x);
				if(board.getColumnHeight(x) < min)
					min = board.getColumnHeight(x);
			}
			return max-min;
		}
		
		/**The highest column on the board
		 * @param board the current state of the game board
		 * @return the height of the highest column on the board
		 * */
		int pileHeight(Board board){
			int ans = board.getColumnHeight(0);
			for(int x=1; x < board.getWidth(); x++){
				if(board.getColumnHeight(x) > ans)
					ans = board.getColumnHeight(x);
			}
			return ans;
		}
		
		/**The total amount of filled blocks on the board
		 * @param board the current state of the game board
		 * @return the total filled blocks on the board
		 * */
		int blocks(Board board){
			int ans = 0;
			for(int x=0; x < board.getWidth(); x++){
				for(int y = 0; y < board.getColumnHeight(x); y++){
					if(board.getGrid(x, y))
						ans++;
				}		
			}
			return ans;
		}
		
		/**This is the same as blocks, except blocks in the row n count n times as 
		 * much as blocks in row 1
		 * @param board the current state of the game board
		 * @return the total of filled blocks on the board, where blocks in row n count 
		 * 		   n times as much as those in row 1
		 * */
		int weightedBlocks(Board board){
			int ans = 0;
			for(int x=0; x < board.getWidth(); x++){
				for(int y = 0; y < board.getColumnHeight(x); y++){
					if(board.getGrid(x, y))
						ans += y;
				}		
			}
			return ans;
		}
	}