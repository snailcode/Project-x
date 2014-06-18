package com.trufflemuffle;


import java.util.LinkedHashMap;

public class Combo {
	
	/**
	 * Name of the current combo
	 */
	private String currentCombo;
	
	/**
	 * Index of the current combo
	 */
	private int comboIndex;
	
	
	/**
	 * Hold all combo names
	 */
	private String[] comboNames;
	
	private int[] comboScores;
	private int scoreIndex;
	private int nextScore;
	
	/**
	 * String is the name of the combo
	 * Boolean: if true, the combo can be used,
	 *          if false, the combo was already used
	 */
	private LinkedHashMap<String, Boolean> combos;
	
	
	/**
	 * Constructor
	 */
	public Combo() {		
		this.comboNames = new String[]{"Good!", "Wow!", "Nice!", "Amazing!", "Unbelievable!", "Truffle Muffle!", "God like!"};
		this.comboScores = new int[]{10, 25, 50, 100, 250, 500, 1000};
		this.fillCombos();
		this.comboIndex = -1;
		this.currentCombo = "";
		this.scoreIndex = 0;
		this.nextScore = this.comboScores[this.scoreIndex];
		
		System.out.println(this.currentCombo);
	}

	/**
	 * Set the next Combo and returns true if it was a success and false if not
	 * 
	 * @param pointsInRow
	 * @return
	 */
	public boolean setNextCombo(int pointsInRow) {
		if(this.scoreIndex < this.comboScores.length){
			// Checks if it's a valid combo, if the combo was already used, and if the given points are the next one in 
			// the array
			if(checkCombo(pointsInRow) && ((Boolean)this.combos.values().toArray()[this.comboIndex+1]) && pointsInRow == nextScore) {
				// Sets the combo to false (false = used)
				this.combos.put(this.comboNames[this.comboIndex+1], false);
				
				// Sets the new name of the current combo
				this.currentCombo = (String) this.combos.keySet().toArray()[this.comboIndex+1];
				
				// increases the combo index
				this.comboIndex++;
				
				// increases the score index
				this.scoreIndex++;
				
				// Sets the next score that'll be needed for the next combo
				this.nextScore = this.scoreIndex < this.comboScores.length ? this.comboScores[this.scoreIndex] : 0;
				
				return true;
			}

		}
		
		return false;
	}
	
	/**
	 * Checks if its a valid combo based on pointsInRow
	 * 
	 * @param pointsInRow 
	 * 
	 * @return true if it's a valid combo, false if it's not
	 */
	public boolean checkCombo(int pointsInRow) {		
		for(int i : this.comboScores) {
			if(i == pointsInRow) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * Fills the HashMap based on the enum with combo names and the corresponding
	 * boolean (true)
	 */
	private void fillCombos() {
		this.combos = new LinkedHashMap<String, Boolean>();
		
		for(int i = 0; i < this.comboNames.length; i++) {
			this.combos.put(this.comboNames[i], true);
		}
	}
	
	/**
	 * Get the combo name based on the setNextCombo - Method
	 * 
	 * @return name of the current combo
	 */
	public String getCurrentComboName() {
		return this.currentCombo;
	}
	
	/**
	 * Gets the current Comboname independent of the setNextCombo Method
	 * 
	 * @return Current combo name based on pointsInRow or "" if the index is -1
	 */
	public String getComboName(int pointsInRow) {
		int index = this.getComboIndex(pointsInRow);
		return index == -1 ? "" : this.comboNames[index];
	}
	
	/**
	 * Gets the combo index based of the given points 
	 */
	private int getComboIndex(int pointsInRow) {
		for (int i=0; i<this.comboScores.length; i++) {
			if (this.comboScores[i]==pointsInRow) {
				return i;
			}
		}
		return -1;
	}
}