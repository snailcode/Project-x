package com.trufflemuffle;

public class GoldenMuffin extends Muffin { 
	private Timer timer;
	private Thread t;
	private long effectTimer;
	
	
	public GoldenMuffin(Sprite sprite, Vector position, char pipe) {
		super(sprite, position, pipe);
	}
	
	@Override
	public void doEffect(final Player player) {
		
		/*
		 * The Effect wont stack. It will only start if
		 * the player isn't in effect mode
		 */
		if (!player.hasGoldenMuffin()) {
			player.setGoldenMuffin(true);
			
			this.timer = new Timer();
			
			this.t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while (player.hasGoldenMuffin()) {
						
						// Update and increase timer
						timer.update();
						effectTimer += timer.getElapsedTime();
						
						// The effect will hold eight seconds
						if (effectTimer >= 8000 || player.getLastCollected() == 'e') {
							player.setGoldenMuffin(false);
						}
					}
					
					effectTimer = 0;
				}
			});
			
			t.start();
		} 
		
		// Adds three points to the player
		player.setPoints(player.getPoint() + 3);
	}

	@Override
	public void setToken() {
		this.token = 'g';
	}
}
