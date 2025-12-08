package com.tetris.core;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class GameEngine {

    private Board board;
    private Tetromino currentPiece;
    private Tetromino heldPiece;
    private Queue<Tetromino> nextPieces;
    private boolean canHold;
    private int score;
    private int level;
    private int linesCleared;
    private int totalLinesCleared;
    private boolean gameOver;
    private boolean paused;
    private long lastDropTime;
    private int dropSpeed;
    private String gameMode;
    private Random random;
    private SoundManager soundManager;
    private AchievementManager achievementManager;
    private StatisticsTracker statistics;
    private PowerUpManager powerUpManager;
    private ComboTracker comboTracker;
    private int consecutiveTetrises;
    private int sprintTarget;
    private long gameStartTime;

    public GameEngine(String gameMode) {
        this.gameMode = gameMode;
        this.random = new Random();
        this.soundManager = new SoundManager();
        this.achievementManager = new AchievementManager();
        this.statistics = new StatisticsTracker();
        this.powerUpManager = new PowerUpManager();
        this.comboTracker = new ComboTracker();

        board = new Board();
        nextPieces = new LinkedList<>();

        sprintTarget = gameMode.equals("SPRINT") ? 40 : 0;
    }

    public void startNewGame() {
        board.clear();
        score = 0;
        level = 1;
        linesCleared = 0;
        totalLinesCleared = 0;
        gameOver = false;
        paused = false;
        heldPiece = null;
        canHold = true;
        consecutiveTetrises = 0;
        lastDropTime = System.currentTimeMillis();
        gameStartTime = System.currentTimeMillis();

        nextPieces.clear();
        for (int i = 0; i < 5; i++) {
            nextPieces.add(generateRandomPiece());
        }

        spawnNewPiece();
        updateDropSpeed();
        statistics.reset();
        comboTracker.reset();
    }

    private Tetromino generateRandomPiece() {
        TetrominoType[] types = TetrominoType.values();
        TetrominoType type = types[random.nextInt(types.length)];
        return new Tetromino(type);
    }

    private void spawnNewPiece() {
        currentPiece = nextPieces.poll();
        nextPieces.add(generateRandomPiece());
        currentPiece.setPosition(3, 0);
        canHold = true;

        if (!board.isValidPosition(currentPiece)) {
            gameOver = true;
            soundManager.playGameOver();
            saveScore();
        } else {
            statistics.incrementPieceCount(currentPiece.getType());
        }
    }

    public void update() {
        if (gameOver || paused) return;

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastDropTime >= dropSpeed) {
            moveDown();
            lastDropTime = currentTime;
        }

        powerUpManager.update();

        if (gameMode.equals("SPRINT") && totalLinesCleared >= sprintTarget) {
            gameOver = true;
            soundManager.playVictory();
            saveScore();
        }
    }

    private void moveDown() {
        if (currentPiece.moveDown()) {
            if (!board.isValidPosition(currentPiece)) {
                currentPiece.moveUp();
                lockPiece();
            }
        }
    }

    public void moveLeft() {
        if (currentPiece.moveLeft()) {
            if (!board.isValidPosition(currentPiece)) {
                currentPiece.moveRight();
            } else {
                soundManager.playMove();
            }
        }
    }
    
    public void moveRight() {
        if (currentPiece.moveRight()) {
            if (!board.isValidPosition(currentPiece)) {
                currentPiece.moveLeft();
            } else {
                soundManager.playMove();
            }
        }
    }
}
