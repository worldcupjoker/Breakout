/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;//60
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;
	
	/** Animation delay or pause time between ball moves */
	private static final int DELAY = 15;
	
	private static final double DIAGONAL = BALL_RADIUS * (Math.sqrt(2) - 1) / Math.sqrt(2);

	/* Method: init */
	/**
	 * Set the size of the window.
	 */
	public void init() {
		this.setSize(WIDTH, HEIGHT);
	}
	
/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		/* You fill this in, along with any subsidiary methods */
		
		setup();
		playGame();
	}
	
	/* Method: setUp */
	/**
	 * Setup the map.
	 */
	private void setup() {
		addBricks();
		addPaddle();
		addBall();
		addScoreBoard();
		addAttemptsBoard();
	}
	
	/* Method: addBricks */
	/**
	 * Add bricks.
	 */
	private void addBricks() {
		for (int i = 1; i <= NBRICK_ROWS; i++) {
			drawBricks(i);
		}
	}
	
	/* Method: drawBricks */
	/**
	 * Draw bricks.
	 * @param rowNumber
	 */
	private void drawBricks(int rowNumber) {
		for (int i = 1; i <= NBRICKS_PER_ROW; i++) {
			
			/* Set locations */
			int x = BRICK_SEP - 2 + (BRICK_SEP + BRICK_WIDTH) * (i - 1);
			int y = BRICK_Y_OFFSET + (BRICK_SEP + BRICK_HEIGHT) * (rowNumber - 1);
			GRect brick = new GRect(x, y, BRICK_WIDTH, BRICK_HEIGHT);
			
			/* Set colors */
			switch(rowNumber) {
			case 1: brick.setColor(Color.RED); brick.setFillColor(Color.RED); break;
			case 2: brick.setColor(Color.RED); brick.setFillColor(Color.RED);break;
			case 3: brick.setColor(Color.ORANGE); brick.setFillColor(Color.ORANGE); break;
			case 4: brick.setColor(Color.ORANGE); brick.setFillColor(Color.ORANGE); break;
			case 5: brick.setColor(Color.YELLOW); brick.setFillColor(Color.YELLOW); break;
			case 6: brick.setColor(Color.YELLOW); brick.setFillColor(Color.YELLOW); break;
			case 7: brick.setColor(Color.GREEN); brick.setFillColor(Color.GREEN); break;
			case 8: brick.setColor(Color.GREEN); brick.setFillColor(Color.GREEN); break;
			case 9: brick.setColor(Color.CYAN); brick.setFillColor(Color.CYAN); break;
			case 10: brick.setColor(Color.CYAN); brick.setFillColor(Color.CYAN); break;
			default: break;
			}
			
			/* Add bricks */
			brick.setFilled(true);			
			add(brick);
		}
	}
	
	/* Method: addPaddle */
	/**
	 * Draw a paddle.
	 */
	private void addPaddle() {
		int x = (WIDTH - PADDLE_WIDTH) / 2;
		int y = HEIGHT - PADDLE_Y_OFFSET;
		paddle = new GRect(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle);
	}
	
	/**
	 * Move the paddle.
	 */
	public void mouseMoved(MouseEvent e) {
		int y = HEIGHT - PADDLE_Y_OFFSET;
		int x = e.getX() - PADDLE_WIDTH / 2;
		if (e.getX() < PADDLE_WIDTH / 2) {
			paddle.setLocation(0, y);
		} else if (e.getX() > WIDTH - PADDLE_WIDTH / 2) {
			paddle.setLocation(WIDTH - PADDLE_WIDTH, y);
		} else {
			paddle.setLocation(x, y);
		}	
	}
	
	/* Method: addBall */
	/**
	 * Add a ball to the game board.
	 */
	private void addBall() {
		int x = WIDTH / 2 - BALL_RADIUS;
		int y = HEIGHT / 2 - BALL_RADIUS;
		ball = new GOval(x, y, BALL_RADIUS * 2, BALL_RADIUS * 2);
		ball.setFilled(true);
		add(ball);
	}
	
	/* Method: addScoreBoard */
	/**
	 * Add a score board.
	 */
	private void addScoreBoard() {
		scoreBoard = new GLabel("Score: " + score);
		scoreBoard.setFont("Times New Roman-18");
		add(scoreBoard, WIDTH - scoreBoard.getWidth(), 1 + scoreBoard.getHeight());
		
		bricksLeft = new GLabel("Bricks left: " + brickNumbers);
		bricksLeft.setFont("Times New Roman-18");
		add(bricksLeft, WIDTH - bricksLeft.getWidth(), 1 + scoreBoard.getHeight() + bricksLeft.getHeight());
	}
	
	/* Method: addAttemptsBoard */
	/**
	 * Add attempt number board to show attempts left.
	 */
	private void addAttemptsBoard() {
		attemptsBoard = new GLabel("Attempts left: " + NTURNS);
		attemptsBoard.setFont("Times New Roman-18");
		add(attemptsBoard, 0, 1 + scoreBoard.getHeight());
	}
	
	/* Method: youWin */
	/**
	 * Add "You Win" board.
	 */
	private void youWon() {
		GLabel youWon = new GLabel("You Won!!!");
		youWon.setFont("Times New Roman-18");
		add(youWon, (WIDTH - youWon.getWidth()) / 2, HEIGHT / 2 + youWon.getHeight());
	}
	
	/* Method: youLost */
	/**
	 * Add "You Lost" board.
	 */
	private void youLost() {
		GLabel youLost = new GLabel("You Lost!!!");
		youLost.setFont("Times New Roman-18");
		add(youLost, (WIDTH - youLost.getWidth()) / 2, HEIGHT / 2 + youLost.getHeight());
	}
	
	/* Method: playGame */
	/**
	 * Start the game.
	 */
	private void playGame() {
		mouseClicked = false;
		score = 0;
		attempts = 1;
		addMouseListeners();
		while(true) {
			setSpeed();
			if (attempts > NTURNS) {
				youLost();
				break;
			}
			if (brickNumbers == 0) {
				youWon();
				break;
			}
			countDown();
			resetBall();
			while(true) {
				moveBall();
				int temp = brickNumbers;
				checkForCollision();
				changeScore(temp);
				pause(DELAY);
				if (brickNumbers == 0) {
					break;
				}
				if (missedBall()) {
					changeAttemptsLeft();
					break;
				}
			}
			
		}
	}
	
	public void MouseClicked(MouseEvent e) {
		mouseClicked = true;
		add(new GLabel("asdfasdfasfd", 10, 10));
	}
	
	/* Method: moveBall */
	/**
	 * Move the ball.
	 */
	private void moveBall() {
		ball.move(vx, vy);
	}
	
	/* Method: setSpeed */
	/**
	 * Set the speed of the ball.
	 */
	private void setSpeed() {
		vx = rgen.nextDouble(1.5, 3.0) * 1;
		if (rgen.nextBoolean(0.5)) {
			vx = -vx;
		}
		vy = 3.0 * 1;
	}
	
	/* Method: checkForCollision */
	/**
	 * Reflect the ball when the ball hits the surface of the side walls and bricks.
	 */
	private void checkForCollision() {
		/* check the six points around the GOval ball */
		/* 0.01 is added to avoid returning the ball as the object. */
		p1 = new GPoint(ball.getX() + DIAGONAL - 0.01, ball.getY() + DIAGONAL);
		p2 = new GPoint(ball.getX(), ball.getY() + BALL_RADIUS + 0.01);
		p3 = new GPoint(ball.getX() + DIAGONAL - 0.01, ball.getY() + BALL_RADIUS * 2 - DIAGONAL);
		p4 = new GPoint(ball.getX() + BALL_RADIUS + 0.01, ball.getY() + BALL_RADIUS * 2);
		p5 = new GPoint(ball.getX() + BALL_RADIUS * 2 - DIAGONAL + 0.01, ball.getY() + BALL_RADIUS * 2 - DIAGONAL);
		p6 = new GPoint(ball.getX() + BALL_RADIUS * 2, ball.getY() + BALL_RADIUS + 0.01);
		p7 = new GPoint(ball.getX() + BALL_RADIUS * 2 - DIAGONAL + 0.01, ball.getY() + DIAGONAL);
		p8 = new GPoint(ball.getX() + BALL_RADIUS + 0.01, ball.getY());
		checkWalls();
		checkBricks(getCollidingObject());
		checkPaddle();
	}
	
	/* Method: checkWalls */
	/**
	 * Reflect the ball when it hits the side walls and the roof.
	 */
	private void checkWalls() {
		if (ball.getY() < 0) {
			reverseY();
		}
		//if (ball.getY() > HEIGHT - 2 * BALL_RADIUS) vy = -vy;
		if (ball.getX() < 0) {
			reverseX();
		}
		if (ball.getX() > WIDTH - 2 * BALL_RADIUS) {
			reverseX();
		}
	}
	
	/* Method: checkBricks */
	/**
	 * If there is a visible brick, set it to invisible.
	 * @param gObj
	 * @return
	 */
	private boolean checkBricks(GObject gObj) {
		if (gObj != null) {
			gObj.setVisible(false);
			brickNumbers--;
			return true;
		} else {
			return false;
		}
	}
	
	/* Method: checkPaddle */
	/**
	 * Reflect the ball when it hits the paddle.
	 */
	private void checkPaddle() {
		if (getElementAt(p3) == paddle) {
			ball.setLocation(ball.getX(), paddle.getY() - 20); // To prevent the ball from being glued to the paddle by separating them immediately.
			reverseY();
			reverseX();
		}
		if (getElementAt(p4) == paddle) {
			ball.setLocation(ball.getX(), paddle.getY() - 20);
			reverseY();
		}
		if (getElementAt(p5) == paddle) { // bottom middle point. 0.01 is added to avoid returning the ball as an object.
			ball.setLocation(ball.getX(), paddle.getY() - 20);
			reverseY();
			reverseX();
		}
	}
	
	/* Method: reveseX */
	/**
	 * Reverse the speed in x direction.
	 */
	private void reverseX() {
		vx = -vx;
		bounceClip.play();
	}
	
	/* Method: reveseY */
	/**
	 * Reverse the speed in y direction.
	 */
	private void reverseY() {
		vy = -vy;
		bounceClip.play();
	}
	
	/* Method: getCollidingObject */
	/**
	 * Check if there are bricks that colliding points. If there are visible
	 * bricks, reflect the ball.
	 * @return Return true if there are visible bricks at colliding points.
	 */
	private GObject getCollidingObject() {
		if (isNotBrick(paddle)) {
			if (isNotBrick(scoreBoard)) {
				if (isNotBrick(bricksLeft)) {
					if (isNotBrick(attemptsBoard)) {
						if (getElementAt(p1) != null && getElementAt(p1).isVisible()) {
							reverseY();
							return getElementAt(p1);
						} else if (getElementAt(p2) != null && getElementAt(p2).isVisible()) {
							reverseX();
							return getElementAt(p2);
						} else if (getElementAt(p3) != null && getElementAt(p3).isVisible()) {
							reverseY();
							return getElementAt(p3);
						} else if (getElementAt(p4) != null && getElementAt(p4).isVisible()) {
							reverseY();
							return getElementAt(p4);
						} else if (getElementAt(p5) != null && getElementAt(p5).isVisible()) {
							reverseY();
							return getElementAt(p5);
						} else if (getElementAt(p6) != null && getElementAt(p6).isVisible()) {
							reverseX();
							return getElementAt(p6);
						} else if (getElementAt(p7) != null && getElementAt(p7).isVisible()) {
							reverseY();
							return getElementAt(p7);
						} else if (getElementAt(p8) != null && getElementAt(p8).isVisible()) {
							reverseY();
							return getElementAt(p8);
						} else {
							return null;
						}
					} else {
						return null;
					}
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	/*Method: checkBricks*/
	/**
	 * Return true if the element is not a brick.
	 * @param obj
	 */
	private boolean isNotBrick(GObject obj) {
		if (getElementAt(p1) != obj && getElementAt(p2) != obj && getElementAt(p3) != obj && getElementAt(p4) != obj && getElementAt(p5) != obj && getElementAt(p6) != obj && getElementAt(p7) != obj && getElementAt(p8) != obj) {
			return true;
		} else {
			return false;
		}
	}
	
	/* Method: changeScore */
	/**
	 * Change the score when the ball hits a brick.
	 * @param temp
	 */
	private void changeScore(int temp) {
		if (temp != brickNumbers) {
			score++;
			scoreBoard.setLabel("Score: " + score);
			scoreBoard.setLocation(WIDTH - scoreBoard.getWidth(), 1 + scoreBoard.getHeight());
			
			bricksLeft.setLabel("Bricks left: " + brickNumbers);
			bricksLeft.setLocation(WIDTH - bricksLeft.getWidth(), 1 + scoreBoard.getHeight() + bricksLeft.getHeight());
		}
	}
	
	/* Method: resetBall */
	/**
	 * Reset the location of the ball.
	 */
	private void resetBall() {
		ball.setLocation(WIDTH / 2 - BALL_RADIUS, HEIGHT / 2 - BALL_RADIUS);
	}
	
	/* Method: missedBall */
	/**
	 * Check if the paddle misses the ball.
	 * @return True if the paddle misses the ball.
	 */
	private boolean missedBall() {
		if (ball.getY() + 2 * BALL_RADIUS > HEIGHT) {
			return true;
		} else {
			return false;
		}
	}
	
	/* Method: changeAttemptsLeft */
	/**
	 * Count the attempts left.
	 */
	private void changeAttemptsLeft() {
		attempts ++;
		attemptsBoard.setLabel("Attempts left: " + (NTURNS + 1 - attempts));
	}
	
	/* Method: countDown */
	private void countDown() {
		GLabel countdown = new GLabel("3");
		countdown.setFont("Times New Roman-45");
		countdown.setColor(Color.GREEN);
		add(countdown, (WIDTH - countdown.getWidth()) / 2, HEIGHT / 2 - 20);
		pause(1000);
		countdown.setLabel("2");
		pause(1000);
		countdown.setLabel("1");
		pause(1000);
		countdown.setVisible(false);
	}
		
	/* Instance variables */
	private GRect paddle;
	private GLabel scoreBoard, bricksLeft, attemptsBoard;
	private double vx, vy;
	private boolean mouseClicked;
	private int brickNumbers = NBRICKS_PER_ROW * NBRICK_ROWS, attempts = 1, score;
	private GOval ball;
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private GObject gObj;
	private GPoint p1, p2, p3, p4, p5, p6, p7, p8;
	private AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
}