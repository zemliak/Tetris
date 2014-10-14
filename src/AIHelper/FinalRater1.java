package AIHelper;
import java.awt.Point;
import java.util.ArrayList;

import tetris.Board;
import tetris.Piece;
//import tetris.Move;
//import tetris.TetrisController;
public class FinalRater1 extends BoardRater{
	double rate(Board board,Piece piece){
		double score = 0;
		score += holeCounter(board)*-5.2912668;//-7.899265427351652;
		score += heightCounter(board, piece)*-4.4633817;//-4.500158825082766;
		score += openWellSums(board)*-4.2974978;//-3.3855972247263626;
		score += rowTransitions(board)*-2.1985560;//-3.2178882868487753;
		score += columnTransitions(board)*-8.7569305;//-9.348695305445199;
		////score += blockadeCounter(board)*-1.4;
		////score += clearCounter(board, height)*10;
		//taken away
		//score += touchAnotherBlock(piece, board)*4.8;
		//score += touchWall(piece, board)*3.22;
		//added
		
		//score += connectedHoles(board)*-16; 	
		//score += altitudeDifference(board)*-7;
		//score += pileHeight(board)*-5;
		//score += blocks(board)*-3;
		//score += weightedBlocks(board)*-2;
		//end added
	    //score += touchFloor(piece)*40000;
		return score;
	}
	//methods
	int holeCounter(Board board){
		int amount = 0;
		for(int x = 0; x < board.getWidth(); x++){
			int y = board.getColumnHeight(x) - 2;			
			while (y>=0) {
				if  (!(board.getGrid(x,y)) && board.getGrid(x, y+1)	)
						amount ++;
				--y;
			}
		}
		return amount;
	}
	double heightCounter(Board board, Piece piece){
		/*int total =0;
        for(int x=0; x < board.getWidth(); x++)
            total += board.getColumnHeight(x);
        return total;*/
		int columnTotal= 0;
		ArrayList<Integer> pts = new ArrayList<Integer>();
		for(Point p : piece.getBody()){
			if(!pts.contains(p.x)){
				columnTotal += board.getColumnHeight(p.x);
				pts.add(p.x);
			}
		}
		return piece.getHeight()/2 + columnTotal; 
	}
	/*int blockadeCounter(Board board){
		int amount = 0;
		for(int x = 0; x < board.getWidth(); x++){
			int y = board.getColumnHeight(x) - 2;		
			while (y>=0) {
				if  (!board.getGrid(x,y) && board.getGrid(x,y+1)) {
					amount ++;
				}
				y--;
			}
		}
		return amount;
	}*/
	/*int clearCounter(Board board, int height){
		int clears = 0;
		/*for(int j = 0; j < board.getHeight(); j++){
			boolean clear = true;
			for(int i = 0; i < board.getWidth(); i++){
				if(!(board.getGrid(i, 0)))
					clear = false;
			}
			if (clear)
				clears++;
		}
		return clears;
		if (board.getHeight() == height -10)
			clears++;
		else if (board.getHeight() == height -20)
			clears+=2;
		else if (board.getHeight() == height -30)
			clears+=3;
		else if (board.getHeight() == height -40)
			clears+=3;
		else if (board.getHeight() == height -50)
			clears+=4;
		System.out.println(clears);
		return clears;
	}*/
	int touchAnotherBlock(Piece piece, Board board){
		int edges = 0;
			for(Point p : piece.getBody()){
				if(p.x +1 < board.getWidth()){
					if(board.getGrid(p.x+1, p.y))
						edges++;	
				}
				if(p.x-1 > 0){
					if(board.getGrid(p.x-1, p.y))
						edges++;
				}
				if(p.y-1 > 0){
					if(board.getGrid(p.x, p.y-1))
						edges++;
				}
				if(p.y+1 < board.getHeight()){
					if(board.getGrid(p.x+1, p.y))
						edges++;
				}
			}
			return edges;
		}
	int touchWall(Piece piece, Board board){
		int answer = 0;
		for(Point p : piece.getBody()){
	    	  if(p.x == board.getWidth()-1)
	    		  answer++;
	    	  if(p.x == 0)
	    		  answer++;
	      }
		return answer;
	}
	int touchFloor(Piece piece){
		int answer = 0;
		for(Point p : piece.getBody()){
	    	  if(p.y == 0)
	    		  answer++;
	      }
	      return answer;
		}
	int columnTransitions(Board board){
		int answer = 0;
		for(int x=0; x < board.getWidth(); x++){
			for (int i = 0; i < board.getColumnHeight(x); i++){
				if(board.getGrid(x, i) != board.getGrid(x, i+1))
					answer++;
			}
		}
		return answer;
	}
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
	int connectedHoles(Board board){
		int holes = 0;
		for(int x=0; x < board.getWidth(); x++){
			for(int i = 0; i < board.getColumnHeight(x); i++){
				if(!(board.getGrid(x, i)) && !(board.getGrid(x, i+1)))
					holes++;
			}
		}
		//System.out.println(holes);
		return holes;
	}
	//new stuff
	int openWellSums(Board board){
		int wells = 0;
		if(board.getColumnHeight(1)>board.getColumnHeight(0))
			wells+=board.getColumnHeight(1)-board.getColumnHeight(0);
		if(board.getColumnHeight(8)>board.getColumnHeight(9))
			wells+=board.getColumnHeight(8)-board.getColumnHeight(9);
		for(int x = 1; x < board.getWidth()-1; ++x){
			if(board.getColumnHeight(x-1) > board.getColumnHeight(x) && board.getColumnHeight(x+1) > board.getColumnHeight(x)){
				if(board.getColumnHeight(x-1) > board.getColumnHeight(x+1))
					wells += board.getColumnHeight(x-1) - board.getColumnHeight(x);
				else if(board.getColumnHeight(x-1) < board.getColumnHeight(x+1))
					wells += board.getColumnHeight(x-1) - board.getColumnHeight(x);
			}
		}
		return wells;
	}
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
	int pileHeight(Board board){
		int ans = board.getColumnHeight(0);
		for(int x=1; x < board.getWidth(); x++){
			if(board.getColumnHeight(x) > ans)
				ans = board.getColumnHeight(x);
		}
		return ans;
	}
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
	double altitudeOfPiece(Board board, Piece piece){
		int ans = 0;
		int i = 0;
		ArrayList<Integer> pts = new ArrayList<Integer>();
		for(Point p: piece.getBody()){
			if(!(pts.contains(p.x))){
				ans += p.x;
				pts.add(p.x);
				i++;
			}
		}
		return ans/i;
	}
}
