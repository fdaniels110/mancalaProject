import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.util.*;

/**
	This class holds data and logistics for the Mancala game.
*/

public class DataModel {
	private int undoCounter;
	private int[] pebblesBackup;
	private boolean redo, extraTurn;
	private String currentPlayer, pA = "Player A", pB = "Player B";
	private ArrayList<Pit> pits;
	private ArrayList<ChangeListener> listeners;
	
	/**
		Constructor.
	*/
	public DataModel() {
		undoCounter = 0;
		pebblesBackup = new int[14];
		redo = false;
		extraTurn = false;
		currentPlayer = pA;
		pits = new ArrayList<Pit>();
		listeners = new ArrayList<ChangeListener>();
		
		for (int i=0; i<=6; i++)
			pits.add(new Pit(0, i, pA, new StyleRectangle()));
		for (int i=7; i<=13; i++)
			pits.add(new Pit(0, i, pB, new StyleRectangle()));
	}
	
	/**
		To attach/add a ChangeListener.
		@param l The ChangeListener to attach.
	*/
	public void attach(ChangeListener l) {
		listeners.add(l);
	}
	
	/**
		To initialize the game with data that user has selected.
		@param initialPebbles The number of pebbles to initialize the game with.
		@param style The style of the Pit.
	*/
	public void initDataModel(int initialPebbles, Style style) {
		for (int i=0; i<=13; i++) {
			pits.get(i).setStyle(style);
			if (i==6 || i==13)
				pebblesBackup[i] = 0;
			else {
				pits.get(i).setPebbles(initialPebbles);
				pebblesBackup[i] = initialPebbles;
			}
		}
		
		for (ChangeListener l : listeners)
			l.stateChanged(new ChangeEvent(this));
	}
	
	/**
		To return all the Pits.
		@return The collection of all Pits.
	*/
	public ArrayList<Pit> getData() {
		return pits;
	}
	
	/**
		To clear data in all Pits.
	*/
	public void clearData() {
		for (int i=0; i<=13; i++)
			pits.get(i).setPebbles(0);
	}
	
	/**
		To return the name of the current player.
		@return The name of the current player.
	*/
	public String getCurrentPlayer() {
		return currentPlayer;
	}
	
	/**
		To switch turn to the other player.
	*/
	public void switchPlayer() {
		if (currentPlayer.equals(pA))
			currentPlayer = pB;
		else if (currentPlayer.equals(pB))
			currentPlayer = pA;
		extraTurn = false;
	}
	
	/**
		To return the result of the game.
		@return The result of the game.
	*/
	public String getWinner() {
		if (getMancala(pA).getPebbles() > getMancala(pB).getPebbles())
			return "Player A Won";
		else if (getMancala(pA).getPebbles() < getMancala(pB).getPebbles())
			return "Player B Won";
		else
			return "It's a tie";
	}
	
	public Pit getMancala(String player) {
		if (player.equals(pA))
			return pits.get(6);
		else
			return pits.get(13);
	}
	
	public void makeAMove(Pit pit) {
		if (!redo)
			undoCounter = 0;
		if (!pit.getPlayer().equals(currentPlayer))
			return;
		if (pit.getPebbles() == 0)
			return;
		for (Pit p : pits)
			pebblesBackup[p.getIndex()] = p.getPebbles();
			
		extraTurn = hasExtraTurn(pit);
		int endingIndex = getEndingIndex(pit);
		int numOfPebbles = pit.getPebbles();
		pit.setPebbles(0);
		distributePebbles(pit.getIndex(), numOfPebbles);
		stealPebbles(endingIndex);
		
		if (isGameOver())
			accumulatePebbles();
		if (!extraTurn)
			switchPlayer();
		redo = false;
		
		for (ChangeListener l : listeners)
			l.stateChanged(new ChangeEvent(this));
	}
	
	public boolean hasExtraTurn(Pit p) {
		int totalMoves = p.getIndex() + p.getPebbles();
		if (currentPlayer.equals(pA) && (totalMoves-6) % 13 == 0)
			return true;
		else if (currentPlayer.equals(pB) && (totalMoves-13) % 13 == 0)
			return true;
		else
			return false;
	}
	
	public int getEndingIndex(Pit p) {
		int numOfPebbles = p.getPebbles();
		int currentIndex = p.getIndex();
		while(numOfPebbles > 0) {
			if (currentPlayer.equals(pB) && currentIndex == 5)
				currentIndex = 7;
			else if (currentPlayer.equals(pA) && currentIndex == 12)
				currentIndex = 0;
			else
				currentIndex++;
			if (currentIndex == 14)
				currentIndex = 0;
			numOfPebbles--;
		}
		return currentIndex;
	}
	
	public void distributePebbles(int index, int n) {
		int numOfPebbles = n;
		int currentIndex = index;
		while (numOfPebbles > 0) {
			if (currentPlayer.equals(pB) && currentIndex == 5)
				currentIndex = 7;
			else if (currentPlayer.equals(pA) && currentIndex == 12)
				currentIndex = 0;
			else
				currentIndex++;
			if (currentIndex == 14)
				currentIndex = 0;
			pits.get(currentIndex).setPebbles(pits.get(currentIndex).getPebbles() + 1);
			numOfPebbles--;
		}
	}
	
	public void stealPebbles(int index) {
		int mancalaIndex = 6;
		if (index == 6 || index == 13)
			return;
		else if (pits.get(index).getPebbles() == 1 && pits.get(index).getPlayer().equals(currentPlayer)) {
			if (pits.get(12-index).getPebbles() == 0)
				return;
			if (currentPlayer.equals(pB))
				mancalaIndex = 13;
			pits.get(mancalaIndex).setPebbles(pits.get(mancalaIndex).getPebbles() + pits.get(index).getPebbles() + pits.get(12-index).getPebbles());
			pits.get(12-index).setPebbles(0);
			pits.get(index).setPebbles(0);
		}
	}
	
	public boolean isGameOver() {
		boolean rowIsEmpty = true;
		for (int i=0; i<=5; i++) {
			if (pits.get(i).getPebbles() != 0)
				rowIsEmpty = false;
		}
		if (rowIsEmpty)
			return rowIsEmpty;
		
		rowIsEmpty = true;
		for (int i=7; i<=12; i++) {
			if (pits.get(i).getPebbles() != 0)
				rowIsEmpty = false;
		}
		return rowIsEmpty;
	}
	
	public void accumulatePebbles() {
		for (int i=0; i<=5; i++) {
			getMancala(pA).setPebbles(getMancala(pA).getPebbles() + pits.get(i).getPebbles());
			pits.get(i).setPebbles(0);
		}
		for (int i=7; i<=12; i++) {
			getMancala(pB).setPebbles(getMancala(pB).getPebbles() + pits.get(i).getPebbles());
			pits.get(i).setPebbles(0);
		}
	}
	
	public void undo() {
		if(!allowUndo())
			return;
		redo = true;
		undoCounter++;
		for (Pit p : pits)
			p.setPebbles(pebblesBackup[p.getIndex()]);
		if (!extraTurn)
			switchPlayer();
		
		for (ChangeListener l : listeners)
			l.stateChanged(new ChangeEvent(this));
	}
	
	public boolean allowUndo() {
		if (isGameOver())
			return false;
		if (hasNoPebblesBackup())
			return false;
		if (undoCounter == 3)
			return false;
		return true;
	}
	
	public boolean hasNoPebblesBackup() {
		for (Pit p : pits) {
			if (p.getPebbles() != pebblesBackup[p.getIndex()])
				return false;
		}
		return true;
	}
}