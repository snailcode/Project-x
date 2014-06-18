package com.trufflemuffle;

public class NormalMuffin extends Muffin {
	public NormalMuffin(Sprite sprite, Vector position, char pipe) {
		super(sprite, position, pipe);
	}

	@Override
	public void doEffect(Player player) {
		player.increasePoint(); 
	}

	@Override
	public void setToken() {
		this.token = 'n';
	}
}
