package com.trufflemuffle;

//-------------------ANMERKUNGEN---------------------------------//
// Die Hoehe und Breite vom Display bekommen wir nur ueber das
// Canvas herraus: canvas.getHeight() etc.
// Am besten wir Teilen das Spiel dann in 3/3:
// 1: Ebene mit fallenden Muffins
// 2: Schlammloescher und Spielfigur
// 3: Steuerung (Pfeile)
// Android kuemmert sich selbststaendig um die Aufloesung, wir muessen nur die richtigen
// Ressourcen in die richtigen Ordner verschieben ( res->Drawable...)

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

public class GameView extends SurfaceView {
	
	private SurfaceHolder surfaceHolder;
	private GameLoopThread gameLoopThread;
	private GameActivity gameActivity = new GameActivity();
	
	private Highscore highscore;
	private int score;
	private SharedPreferences pref;
	
	private Util util;
	
	/**
	 * Defines if the game is in "pause-" state or not
	 */
	private boolean pause;
	
	private Level level;
	
	/**
	 * Timer, that will give us the time between each draw
	 */
	private Timer timer;
	
	/**
	 * 	Muffin-Timer, that will estimate when a new Muffin will be
	 *	draw
	 */
	private long muffinTimer;
	
	/**
	 * 	Position of the pipes:
	 *	0 - pipes[0]
	 *	pipes[0] - pipes[1]
	 *	pipes[1] - pipes[2]
	 */
	private int[] pipes;
	
	/**
	 * Y-Position of the ground
	 */
	private int ground;

	// The Player
	private Player player;
	private LinkedList<Muffin> listMuffin;
	
	private Combo combo;
	private String comboName;
	
	// The Arrows
	private Sprite spArrowLeft;
	private Sprite spArrowRight;
	
	private Sprite spBubble;
	
	// Mudholes
	private Sprite spMud;
	
	// Bitmaps
	private Bitmap bmpMuffin;
	private Bitmap bmpEvilMuffin;
	private Bitmap bmpGoldenMuffin;
	private Bitmap bmpPlayer;
	private Bitmap bmpLife;
	private Bitmap bmpMud;
	private Bitmap bmpArrowLeft;
	private Bitmap bmpArrowRight;
	private Bitmap bmpBubble;
	
	// SoundEffects
	private SoundManager sm; 
	
	// ----------------------------
	// VARIABLEN FUR FONT (syso geht nicht, kann ja spaeter
	// weiter benutzt werden (scores anzeigen etc) oder so
	// Bis jetzt nur als Funktion fuer Testausgaben (Displaygroesse und so))
	final int fontSize = 40;
	int yTextPosition = 40;
	Typeface font = Typeface.create("Comic Sans", Typeface.NORMAL);
	String output = "";
	//-----------------------------
		
	public GameView(Context context) {
		super(context);
		this.util = new Util(context);
		
		this.sm = new SoundManager(context);
		this.gameActivity = (GameActivity)context;
		this.gameLoopThread = new GameLoopThread(this);
		this.surfaceHolder = getHolder();
		this.surfaceHolder.addCallback(new SurfaceHolder.Callback() {

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				gameLoopThread.setRunning(true);
				gameLoopThread.start();
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				boolean retry = true;
				gameLoopThread.setRunning(false);
				while (retry) {
					try {
						gameLoopThread.join();
						retry = false;
					} catch (InterruptedException e) {
						
					}
				}
				
			}
			
		});
		
		// Player initialisieren
		this.bmpPlayer = BitmapFactory.decodeResource(this.getResources(), R.drawable.pig);
		this.player = new Player(new Sprite(bmpPlayer), ground, combo, context);
		
		this.combo = new Combo();
		this.comboName = null;
		this.level = new Level();
		
		// Muffin
		this.bmpMuffin = BitmapFactory.decodeResource(this.getResources(), R.drawable.muffin);
		this.bmpEvilMuffin = BitmapFactory.decodeResource(this.getResources(), R.drawable.evilmuffin);
		this.bmpGoldenMuffin = BitmapFactory.decodeResource(this.getResources(), R.drawable.goldenmuffin);
		this.listMuffin = new LinkedList<Muffin>();
		
		// Muffin-Timer
		this.timer = new Timer();
		this.muffinTimer = 0;
		
		// Three Pipes
		this.pipes = new int[3];
		
		// Arrows
		this.bmpArrowRight = BitmapFactory.decodeResource(this.getResources(), R.drawable.arrright);
		this.bmpArrowLeft = BitmapFactory.decodeResource(this.getResources(), R.drawable.arrleft);
		
		this.spArrowLeft = new Sprite(this.bmpArrowLeft);
		this.spArrowRight = new Sprite(this.bmpArrowRight);
		
		// Creates the Bubble
		this.bmpBubble = BitmapFactory.decodeResource(this.getResources(), R.drawable.bubble);
		
		this.spBubble = new Sprite(this.bmpBubble);
		
		// Mudholes
		this.bmpMud = BitmapFactory.decodeResource(this.getResources(), R.drawable.mud);
		
		
		// Life
		this.bmpLife = BitmapFactory.decodeResource(this.getResources(), R.drawable.life);
		
		this.pref = context.getSharedPreferences(Highscore.KEY, Context.MODE_PRIVATE);
		this.highscore = new Highscore(pref);
		this.score = 0;
		
		this.pause = true;
		

	}
	
	@SuppressLint("NewApi")
	@Override
	public void draw(Canvas canvas) {
		
		// Fuer Testausgaben
		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTypeface(font);
		paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.stdFontSize));
		paint.setAntiAlias(true);
		
		// If the game isn't on pause, the objects will be drawn and the game logic will be
		// executed
		if (!pause) {
			if (player.getLifeCount() == 0) {
				this.highscore.setNewHighscore(this.pref);
				this.score = player.getPoint();
				
				if (this.score > this.highscore.getHighScore()) {
					// Load Party GameOverScreen
					this.gameActivity.onGameOverHighscore();
				} else {
					// Load normal GameOverScreen
					this.gameActivity.onGameOver();
				}
				
				Muffin.resetMuffinStats();
				this.gameActivity.finish();
				
				/*
				 * Stops the game Loop
				 * 
				 * This will prevent, that the game over screen won't show up.
				 */
				this.gameLoopThread.setRunning(false);
			} else {
				this.timer.update();
				this.player.checkCollision(listMuffin);
				this.comboName = combo.getComboName(player.getCombo());
				

				this.drawGame(canvas, paint);
				Paint pText = new Paint();
				pText.setTextAlign(Align.CENTER);
				pText.setColor(Color.GREEN);
				pText.setTextSize(getResources().getDimensionPixelSize(R.dimen.bigFontSize));
				int xPos = (int)(canvas.getWidth() * 0.5f);
				int yPos = (int)(canvas.getWidth() * 0.7f);
				canvas.drawText(comboName, xPos, yPos, pText);
				
				this.spawnMuffin();


				// If a new combo appear, the life will increase by 1
				if (this.combo.setNextCombo(this.player.getCombo())) {
					this.player.increaseLife();
					this.sm.playExtraLife();
				}

				// Increases the Level and the difficulty if the player has
				// specific points
				this.level.checkPointsAndIncraseLevel(this.player.getPoint());


				// Removes the "dead" Muffins
				this.removeMuffin();
			}
		// If the game is on pause, the objects will be drawn, but the logic wont be
		// executed
		} else {
			this.drawGame(canvas, paint);
			this.showDescription(canvas);
			
			Paint p = new Paint();
			p.setTextAlign(Align.CENTER);
			p.setColor(Color.WHITE);
			p.setTextSize(getResources().getDimensionPixelSize(R.dimen.stdFontSize));
			int xPos = (int)(canvas.getWidth() * 0.5f);
			int yPos = (int)(canvas.getWidth() * 0.5f);
			canvas.drawText("Tab to play", xPos, yPos, p);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			if (spArrowLeft.isTouched(event.getX(), event.getY())) {
				this.player.move("left");
			}
			
			if (spArrowRight.isTouched(event.getX(), event.getY())) {
				this.player.move("right");
			}
			
			if (this.pause) {
				this.pause = false;
			}
			
			// If you touch the top right corner, it'll pause
			if (event.getX() >= 600 && event.getY() <= 100) {
				this.switchPauseState();
			}
				
			
		}
		return true;
	}
	
	/**
	 * Draws the game relevant Objects to the Screen
	 * 
	 * @param canvas The canvas where the objects are drawn
	 * @param paint 
	 */
	private void drawGame(Canvas canvas, Paint paint) {
		this.calculatePipes(canvas);
		output = "Points: " + player.getPoint();

		int score = this.highscore.getCurrentScore() > this.highscore
				.getHighScore() ? this.highscore.getCurrentScore()
				: this.highscore.getHighScore();

		canvas.drawColor(Color.BLACK);
		canvas.drawText("Highscore: " + score, 300, yTextPosition,
				paint);
		canvas.drawText(this.combo.getCurrentComboName(), 300,
				yTextPosition + 40, paint);

		int distance = 10;
		for (int i = 0; i < this.player.getLifeCount(); i++) {
			canvas.drawBitmap(this.bmpLife, distance, yTextPosition + 40,
					null);
			distance += this.bmpLife.getWidth() + 5;
		}
		// Set position of Mudholes
		for (int i = 0; i < 3; i++) {
			this.spMud = new Sprite(this.bmpMud);
			this.spMud.setPosition(this.getPipe(i, this.bmpMud), this.ground);
			canvas.drawBitmap(this.bmpMud, this.spMud.getPosition().x,
					this.spMud.getPosition().y, null);
		}
		canvas.drawText(output, 10, yTextPosition, paint);
		
		this.drawBubble(canvas);
		
		// drawing the player
		this.ground = this.player.getPosition().y;

		if (player.getCurPosition() == 'A') {
			this.player.draw(canvas, this.getPipe(0, this.player.getSprite()
					.getBitmap()));
		} else if (this.player.getCurPosition() == 'B') {
			player.draw(canvas, this.getPipe(1, this.player.getSprite()
					.getBitmap()));
		} else if (this.player.getCurPosition() == 'C') {
			this.player.draw(canvas, this.getPipe(2, this.player.getSprite()
					.getBitmap()));
		}
		
		// drawing the muffins
		for (Muffin m : this.listMuffin) {
			if (m.isAlive()) {
				
				// Just update the position if the game is not
				// on pause state
				if (!this.pause) {
					if (!m.animation(this.ground)) {
						this.highscore.setScore(this.player.getPoint());
					}
				}
				
				canvas.drawBitmap(m.getSprite().getBitmap(),
						m.getPosition().x, m.getPosition().y, null);
			}
		}
		
		// drawin Arrows
		this.spArrowLeft.draw(canvas,
				canvas.getWidth() / 4 - (this.bmpArrowLeft.getWidth()),
				canvas.getHeight() - 2 * this.bmpArrowLeft.getHeight());
		this.spArrowRight.draw(canvas,
				canvas.getWidth() / 2 + canvas.getWidth() / 4,
				canvas.getHeight() - 2 * this.bmpArrowRight.getHeight());

		// Set position of Arrows
		this.spArrowLeft.setPosition(
				canvas.getWidth() / 4 - (this.bmpArrowLeft.getWidth()),
				canvas.getHeight() - 2 * this.bmpArrowLeft.getHeight());
		this.spArrowRight.setPosition(
				canvas.getWidth() / 2 + canvas.getWidth() / 4,
				canvas.getHeight() - 2 * this.bmpArrowRight.getHeight());
	}
	
	/**
	 * Draws a short description of all muffins
	 * 
	 * @param canvas canvas where the description should be drawn
	 */
	private void showDescription(Canvas canvas) {
		
		Paint paint = new Paint();
		paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.smallFontSize));
		paint.setColor(Color.WHITE);
		
		int xMuffin = canvas.getWidth() / 6;
		int yMuffin = (int)(canvas.getHeight() / 3.2);
		
		int xText = xMuffin + this.bmpMuffin.getWidth() + 20;
		int yText = yMuffin + this.bmpMuffin.getHeight() / 2;
		
		// Normal Muffin
		canvas.drawBitmap(this.bmpMuffin, xMuffin, yMuffin, paint);
		canvas.drawText("Gain 1 point", xText, yText, paint);
		
		// Evil Muffin
		canvas.drawBitmap(this.bmpEvilMuffin, xMuffin,  yMuffin + this.bmpMuffin.getHeight(), paint);
		canvas.drawText("Decreases the points by 20%", xText, yText + this.bmpMuffin.getHeight(), paint);
		
		// Golden Muffin
		canvas.drawBitmap(this.bmpGoldenMuffin, xMuffin, yMuffin + this.bmpMuffin.getHeight() * 2, paint);
		canvas.drawText("Gains 3 Points + Shield", xText, yText + this.bmpMuffin.getHeight() * 2, paint);
	}
	
	@SuppressLint("NewApi")
	/**
	 * Spawns an Muffin on a random pipe.
	 * 
	 * If the player hits level four, there'll spawn evil- and golden muffins too
	 */
	private void spawnMuffin() {
		char chPipe='A';
		this.muffinTimer += this.timer.getElapsedTime();
		if(this.muffinTimer >= Muffin.getSpawnInterval()) {
			int rndPipe = new Random().nextInt(3);
			int xPosition = this.getPipe(rndPipe, this.bmpMuffin);
			if (rndPipe == 0) {
				chPipe = 'A';
			}
			else if (rndPipe == 1) {
				chPipe = 'B';
			}
			else if (rndPipe == 2) {
				chPipe = 'C';
			}
			
			Muffin muffin;
			// From level 4 on, there are spawning evil muffins
			if(this.level.getLevel() >= 4) {
				// There is a 20% chance that a evil muffin will additional spawn
				if(new Random().nextInt(5) == 0) {
					muffin = new EvilMuffin(new Sprite(this.bmpEvilMuffin), new Vector(xPosition, 0), chPipe);
				} else if (new Random().nextInt(50) == 0) {
					// 2% chance that a golden muffin will spawn
					muffin = new GoldenMuffin(new Sprite(this.bmpGoldenMuffin), new Vector(xPosition, 0), chPipe);
				} else {
					muffin = new NormalMuffin(new Sprite(this.bmpMuffin), new Vector(xPosition, 0), chPipe);
				}
			} else {
				muffin = new NormalMuffin(new Sprite(this.bmpMuffin), new Vector(xPosition, 0), chPipe);
			}
			
			this.listMuffin.push(muffin);
			
			this.muffinTimer = 0;
		}
	}
	
	/**
	 * Switches the states depending on the previous state
	 */
	private void switchPauseState() {
		this.pause = pause == true ? false : true;
	}
	
	/**
	 * Removes the Muffin from the list if it's not alive
	 */
	private void removeMuffin() {
		Iterator<Muffin> iter = this.listMuffin.iterator();
		while(iter.hasNext()) {
			Muffin m = iter.next();
			if(!m.isAlive()) {
				iter.remove(); 
			}
		}
	}
	
	/**
	 * Calculates the three pipes
	 * This'll be just calculated once, because we don't support
	 * the rotation of the Handy. Therefore there's no need for 
	 * multiple calculations
	 */
	private void calculatePipes(Canvas canvas) {
		if(this.pipes[0] == 0) {
			int oneThird = canvas.getWidth() / 3;
			this.pipes[0] = oneThird;
			this.pipes[1] = oneThird * 2;
			this.pipes[2] = oneThird * 3;
		}
	}
	
	private void drawBubble(Canvas canvas) {
		if (this.player.hasGoldenMuffin()) {
			int pipe = this.player.getCurPosition() == 'A' ? 0 : 
					   this.player.getCurPosition() == 'B' ? 1 : 2;
			
			int x = this.getPipe(pipe, this.bmpBubble);
			int y = this.ground - this.bmpBubble.getHeight() / 3;
			
			
			this.spBubble.draw(canvas, x, y);
		}
	}
	
	/**
	 *  Returns the calculated x Position for the Sprite
	 * 	Returns 0, if the pipe value is > 2
	 */
	private int getPipe(int pipe, Bitmap bitmap) {
		if(pipe <= 2) {
			switch(pipe) {
				case 0:
					return this.pipes[0] / 2 - bitmap.getWidth() / 2;
				case 1:
					return this.pipes[1] / 2 - this.pipes[0] / 2 +  this.pipes[0] - bitmap.getWidth() / 2;
				case 2:
					return this.pipes[2] / 2 - this.pipes[1] / 2 + this.pipes[1] - bitmap.getWidth() / 2;
				default:
					return 0;
			}
		}
		
		return 0;
	}

	/**
	 * Returns the current score
	 * 
	 * @return current score
	 */
	public int getScore() {
		return this.score;
	}
}
