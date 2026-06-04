package com.lcy.literal;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.Random;

public class TetrisGame extends JPanel implements ActionListener, KeyListener {
    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 20;
    private static final int CELL_SIZE = 30;
    private static final int SIDE_WIDTH = 180;
    private static final int PADDING = 16;
    private static final int START_DELAY = 520;

    private static final Color BACKGROUND = new Color(22, 24, 30);
    private static final Color BOARD_BACKGROUND = new Color(12, 14, 18);
    private static final Color GRID = new Color(43, 48, 58);
    private static final Color TEXT = new Color(236, 239, 244);
    private static final Color MUTED_TEXT = new Color(166, 173, 187);

    private static final Color[] PIECE_COLORS = {
            new Color(0, 188, 212),
            new Color(63, 105, 255),
            new Color(255, 145, 49),
            new Color(255, 216, 64),
            new Color(58, 199, 93),
            new Color(169, 91, 255),
            new Color(239, 75, 89)
    };
//new class//////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final int[][][][] SHAPES = {
            {
                    {{0, 1}, {1, 1}, {2, 1}, {3, 1}},
                    {{2, 0}, {2, 1}, {2, 2}, {2, 3}},
                    {{0, 2}, {1, 2}, {2, 2}, {3, 2}},
                    {{1, 0}, {1, 1}, {1, 2}, {1, 3}}
            },
            {
                    {{0, 0}, {0, 1}, {1, 1}, {2, 1}},
                    {{1, 0}, {2, 0}, {1, 1}, {1, 2}},
                    {{0, 1}, {1, 1}, {2, 1}, {2, 2}},
                    {{1, 0}, {1, 1}, {0, 2}, {1, 2}}
            },
            {
                    {{2, 0}, {0, 1}, {1, 1}, {2, 1}},
                    {{1, 0}, {1, 1}, {1, 2}, {2, 2}},
                    {{0, 1}, {1, 1}, {2, 1}, {0, 2}},
                    {{0, 0}, {1, 0}, {1, 1}, {1, 2}}
            },
            {
                    {{1, 0}, {2, 0}, {1, 1}, {2, 1}},
                    {{1, 0}, {2, 0}, {1, 1}, {2, 1}},
                    {{1, 0}, {2, 0}, {1, 1}, {2, 1}},
                    {{1, 0}, {2, 0}, {1, 1}, {2, 1}}
            },
            {
                    {{1, 0}, {2, 0}, {0, 1}, {1, 1}},
                    {{1, 0}, {1, 1}, {2, 1}, {2, 2}},
                    {{1, 1}, {2, 1}, {0, 2}, {1, 2}},
                    {{0, 0}, {0, 1}, {1, 1}, {1, 2}}
            },
            {
                    {{1, 0}, {0, 1}, {1, 1}, {2, 1}},
                    {{1, 0}, {1, 1}, {2, 1}, {1, 2}},
                    {{0, 1}, {1, 1}, {2, 1}, {1, 2}},
                    {{1, 0}, {0, 1}, {1, 1}, {1, 2}}
            },
            {
                    {{0, 0}, {1, 0}, {1, 1}, {2, 1}},
                    {{2, 0}, {1, 1}, {2, 1}, {1, 2}},
                    {{0, 1}, {1, 1}, {1, 2}, {2, 2}},
                    {{1, 0}, {0, 1}, {1, 1}, {0, 2}}
            }
    };

    private final int[][] board = new int[BOARD_HEIGHT][BOARD_WIDTH];
    private final Random random = new Random();
    private final Timer timer = new Timer(START_DELAY, this);

    private Piece currentPiece;
    private Piece nextPiece;
    private int score;
    private int lines;
    private int level;
    private boolean paused;
    private boolean gameOver;

    public TetrisGame() {
        int width = PADDING * 3 + BOARD_WIDTH * CELL_SIZE + SIDE_WIDTH;
        int height = PADDING * 2 + BOARD_HEIGHT * CELL_SIZE;
        setPreferredSize(new Dimension(width, height));
        setBackground(BACKGROUND);
        setFocusable(true);
        addKeyListener(this);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Tetris Game");
            TetrisGame game = new TetrisGame();

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.add(game);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            game.start();
        });
    }

    private void start() {
        restart();
        timer.start();
        requestFocusInWindow();
    }

    private void restart() {
        for (int[] row : board) {
            Arrays.fill(row, 0);
        }

        score = 0;
        lines = 0;
        level = 1;
        paused = false;
        gameOver = false;
        timer.setDelay(START_DELAY);

        nextPiece = randomPiece();
        spawnPiece();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (!paused && !gameOver) {
            stepDown();
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        int key = event.getKeyCode();

        if (key == KeyEvent.VK_ENTER && gameOver) {
            restart();
            timer.start();
            return;
        }

        if (key == KeyEvent.VK_P) {
            paused = !paused;
            repaint();
            return;
        }

        if (paused || gameOver) {
            return;
        }

        switch (key) {
            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_A:
                moveCurrent(-1, 0);
                break;
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_D:
                moveCurrent(1, 0);
                break;
            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_S:
                softDrop();
                break;
            case KeyEvent.VK_UP:
            case KeyEvent.VK_W:
            case KeyEvent.VK_X:
                rotateCurrent(1);
                break;
            case KeyEvent.VK_Z:
                rotateCurrent(-1);
                break;
            case KeyEvent.VK_SPACE:
                hardDrop();
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
    }

    @Override
    public void keyTyped(KeyEvent event) {
    }

    private void stepDown() {
        if (!moveCurrent(0, 1)) {
            lockCurrentPiece();
        }
    }

    private void softDrop() {
        if (moveCurrent(0, 1)) {
            score += 1;
        } else {
            lockCurrentPiece();
        }
    }

    private void hardDrop() {
        int dropped = 0;
        while (moveCurrent(0, 1)) {
            dropped++;
        }
        score += dropped * 2;
        lockCurrentPiece();
    }

    private boolean moveCurrent(int dx, int dy) {
        int newX = currentPiece.x + dx;
        int newY = currentPiece.y + dy;
        if (canPlace(currentPiece, newX, newY, currentPiece.rotation)) {
            currentPiece.x = newX;
            currentPiece.y = newY;
            repaint();
            return true;
        }
        return false;
    }

    private void rotateCurrent(int direction) {
        int newRotation = Math.floorMod(currentPiece.rotation + direction, 4);
        int[] wallKicks = {0, -1, 1, -2, 2};

        for (int offset : wallKicks) {
            int newX = currentPiece.x + offset;
            if (canPlace(currentPiece, newX, currentPiece.y, newRotation)) {
                currentPiece.x = newX;
                currentPiece.rotation = newRotation;
                repaint();
                return;
            }
        }
    }

    private void lockCurrentPiece() {
        for (int[] cell : getCells(currentPiece, currentPiece.rotation)) {
            int x = currentPiece.x + cell[0];
            int y = currentPiece.y + cell[1];
            if (y >= 0 && y < BOARD_HEIGHT && x >= 0 && x < BOARD_WIDTH) {
                board[y][x] = currentPiece.type + 1;
            }
        }

        clearFullLines();
        spawnPiece();
        repaint();
    }

    private void clearFullLines() {
        int cleared = 0;

        for (int y = BOARD_HEIGHT - 1; y >= 0; y--) {
            if (isLineFull(y)) {
                removeLine(y);
                cleared++;
                y++;
            }
        }

        if (cleared > 0) {
            int[] lineScores = {0, 100, 300, 500, 800};
            score += lineScores[cleared] * level;
            lines += cleared;
            level = lines / 10 + 1;
            timer.setDelay(Math.max(90, START_DELAY - (level - 1) * 42));
        }
    }

    private boolean isLineFull(int y) {
        for (int x = 0; x < BOARD_WIDTH; x++) {
            if (board[y][x] == 0) {
                return false;
            }
        }
        return true;
    }

    private void removeLine(int line) {
        for (int y = line; y > 0; y--) {
            board[y] = Arrays.copyOf(board[y - 1], BOARD_WIDTH);
        }
        board[0] = new int[BOARD_WIDTH];
    }

    private void spawnPiece() {
        currentPiece = nextPiece;
        currentPiece.x = BOARD_WIDTH / 2 - 2;
        currentPiece.y = 0;
        currentPiece.rotation = 0;
        nextPiece = randomPiece();

        if (!canPlace(currentPiece, currentPiece.x, currentPiece.y, currentPiece.rotation)) {
            gameOver = true;
            timer.stop();
        }
    }

    private Piece randomPiece() {
        return new Piece(random.nextInt(SHAPES.length));
    }

    private boolean canPlace(Piece piece, int pieceX, int pieceY, int rotation) {
        for (int[] cell : getCells(piece, rotation)) {
            int x = pieceX + cell[0];
            int y = pieceY + cell[1];

            if (x < 0 || x >= BOARD_WIDTH || y >= BOARD_HEIGHT) {
                return false;
            }
            if (y >= 0 && board[y][x] != 0) {
                return false;
            }
        }
        return true;
    }

    private int[][] getCells(Piece piece, int rotation) {
        return SHAPES[piece.type][rotation];
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
// //////padding?
        int boardX = PADDING;
        int boardY = PADDING;
        drawBoard(g, boardX, boardY);
        drawSidePanel(g, boardX + BOARD_WIDTH * CELL_SIZE + PADDING * 2, boardY);
        drawOverlay(g, boardX, boardY);

        g.dispose();

    }

    private void drawBoard(Graphics2D g, int boardX, int boardY) {
        int boardPixelWidth = BOARD_WIDTH * CELL_SIZE;
        int boardPixelHeight = BOARD_HEIGHT * CELL_SIZE;

        g.setColor(BOARD_BACKGROUND);
        g.fillRect(boardX, boardY, boardPixelWidth, boardPixelHeight);

        for (int y = 0; y < BOARD_HEIGHT; y++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {
                int value = board[y][x];
                if (value > 0) {
                    drawCell(g, boardX + x * CELL_SIZE, boardY + y * CELL_SIZE,
                            CELL_SIZE, PIECE_COLORS[value - 1], true);
                }
            }
        }

        drawGhostPiece(g, boardX, boardY);

        if (currentPiece != null) {
            for (int[] cell : getCells(currentPiece, currentPiece.rotation)) {
                int x = currentPiece.x + cell[0];
                int y = currentPiece.y + cell[1];
                drawCell(g, boardX + x * CELL_SIZE, boardY + y * CELL_SIZE,
                        CELL_SIZE, PIECE_COLORS[currentPiece.type], true);
            }
        }

        g.setColor(GRID);
        g.setStroke(new BasicStroke(1f));
        for (int x = 0; x <= BOARD_WIDTH; x++) {
            int lineX = boardX + x * CELL_SIZE;
            g.drawLine(lineX, boardY, lineX, boardY + boardPixelHeight);
        }
        for (int y = 0; y <= BOARD_HEIGHT; y++) {
            int lineY = boardY + y * CELL_SIZE;
            g.drawLine(boardX, lineY, boardX + boardPixelWidth, lineY);
        }

        g.setColor(new Color(82, 90, 105));
        g.drawRect(boardX, boardY, boardPixelWidth, boardPixelHeight);
    }

    private void drawGhostPiece(Graphics2D g, int boardX, int boardY) {
        if (currentPiece == null) {
            return;
        }

        int ghostY = currentPiece.y;
        while (canPlace(currentPiece, currentPiece.x, ghostY + 1, currentPiece.rotation)) {
            ghostY++;
        }

        Color ghostColor = new Color(255, 255, 255, 52);
        g.setStroke(new BasicStroke(2f));
        g.setColor(ghostColor);
        for (int[] cell : getCells(currentPiece, currentPiece.rotation)) {
            int x = currentPiece.x + cell[0];
            int y = ghostY + cell[1];
            int drawX = boardX + x * CELL_SIZE + 4;
            int drawY = boardY + y * CELL_SIZE + 4;
            g.drawRect(drawX, drawY, CELL_SIZE - 8, CELL_SIZE - 8);
        }
    }

    private void drawSidePanel(Graphics2D g, int x, int y) {
        g.setColor(TEXT);
        g.setFont(new Font("SansSerif", Font.BOLD, 28));
        g.drawString("Tetris", x, y + 30);

        g.setFont(new Font("SansSerif", Font.BOLD, 16));
        g.drawString("Next", x, y + 74);
        drawNextPiece(g, x, y + 90);

        int infoY = y + 220;
        g.setFont(new Font("SansSerif", Font.BOLD, 15));
        drawInfoLine(g, "Score", String.valueOf(score), x, infoY);
        drawInfoLine(g, "Lines", String.valueOf(lines), x, infoY + 34);
        drawInfoLine(g, "Level", String.valueOf(level), x, infoY + 68);

        int controlsY = y + 350;
        g.setColor(MUTED_TEXT);
        g.setFont(new Font("SansSerif", Font.PLAIN, 13));
        g.drawString("Move: Arrow keys / WASD", x, controlsY);
        g.drawString("Rotate: Up / W / X / Z", x, controlsY + 24);
        g.drawString("Drop: Down / Space", x, controlsY + 48);
        g.drawString("Pause: P", x, controlsY + 72);
        g.drawString("Restart after game: Enter", x, controlsY + 96);
    }

    private void drawInfoLine(Graphics2D g, String label, String value, int x, int y) {
        g.setColor(MUTED_TEXT);
        g.drawString(label, x, y);
        g.setColor(TEXT);
        g.drawString(value, x, y + 18);
    }

    private void drawNextPiece(Graphics2D g, int x, int y) {
        int previewSize = 24;
        int boxSize = previewSize * 4;

        g.setColor(new Color(15, 18, 24));
        g.fillRect(x, y, boxSize + 14, boxSize + 14);
        g.setColor(new Color(62, 69, 84));
        g.drawRect(x, y, boxSize + 14, boxSize + 14);

        if (nextPiece == null) {
            return;
        }

        for (int[] cell : getCells(nextPiece, 0)) {
            int drawX = x + 7 + cell[0] * previewSize;
            int drawY = y + 7 + cell[1] * previewSize;
            drawCell(g, drawX, drawY, previewSize, PIECE_COLORS[nextPiece.type], true);
        }
    }

    private void drawOverlay(Graphics2D g, int boardX, int boardY) {
        if (!paused && !gameOver) {
            return;
        }

        int width = BOARD_WIDTH * CELL_SIZE;
        int height = BOARD_HEIGHT * CELL_SIZE;
        g.setColor(new Color(0, 0, 0, 165));
        g.fillRect(boardX, boardY, width, height);

        String title = gameOver ? "Game Over" : "Paused";
        String subtitle = gameOver ? "Press Enter to restart" : "Press P to continue";

        g.setFont(new Font("SansSerif", Font.BOLD, 30));
        drawCenteredText(g, title, boardX, boardY + height / 2 - 18, width);
        g.setFont(new Font("SansSerif", Font.PLAIN, 16));
        drawCenteredText(g, subtitle, boardX, boardY + height / 2 + 18, width);
    }

    private void drawCenteredText(Graphics2D g, String text, int x, int y, int width) {
        FontMetrics metrics = g.getFontMetrics();
        int textX = x + (width - metrics.stringWidth(text)) / 2;
        g.setColor(TEXT);
        g.drawString(text, textX, y);
    }

    private void drawCell(Graphics2D g, int x, int y, int size, Color color, boolean bevel) {
        int gap = Math.max(2, size / 12);
        int cellX = x + gap;
        int cellY = y + gap;
        int cellSize = size - gap * 2;

        g.setColor(color);
        g.fillRect(cellX, cellY, cellSize, cellSize);


        if (bevel) {//
            g.setColor(color.brighter());//
            g.drawLine(cellX, cellY, cellX + cellSize - 1, cellY);
            g.drawLine(cellX, cellY, cellX, cellY + cellSize - 1);

            g.setColor(color.darker());
            g.drawLine(cellX, cellY + cellSize - 1, cellX + cellSize - 1, cellY + cellSize - 1);
            g.drawLine(cellX + cellSize - 1, cellY, cellX + cellSize - 1, cellY + cellSize - 1);
        }
    }

    private static class Piece {
        private final int type;
        private int x;
        private int y;
        private int rotation;

        private Piece(int type) {
            this.type = type;
        }
    }
}
