package com.trufflemuffle;

public class EvilMuffin extends Muffin {

	public EvilMuffin(Sprite sprite, Vector position, char pipe) {
		super(sprite, position, pipe);
	}

	@Override
	public void doEffect(Player player) {
	
		/*
		 * Effect will only trigger, if the effect of the
		 * golden - Muffin isn't activated
		 */
		if (!player.hasGoldenMuffin()) {
			
			// Decreases the points by 20%
			player.setPoints((int)(player.getPoint() * 0.8));
			
			
			// Reset the combo to 0 
			player.resetCombo();
		}

		// Plays the sound
		player.playEvilMuffin();

	}

	@Override
	public void setToken() {
		this.token = 'e';		
	}

}
