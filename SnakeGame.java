package com.liuchenyu.test;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Random;

public class SnakeGame extends JFrame {
    private static final int CELL_SIZE = 25;
    private static final int COLS = 28;
    private static final int ROWS = 22;
    private static final int BOARD_WIDTH = COLS * CELL_SIZE;
    private static final int BOARD_HEIGHT = ROWS * CELL_SIZE;

    public SnakeGame() {
        setTitle("Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        add(new GamePanel());
        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SnakeGame().setVisible(true));
    }

    private enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT
    }

    private static class GamePanel extends JPanel {
        private static final int GAME_SPEED_MS = 110;
        private static final Color BACKGROUND = new Color(18, 24, 32);
        private static final Color GRID = new Color(34, 43, 56);
        private static final Color SNAKE_HEAD = new Color(0, 255, 24);
        private static final Color SNAKE_BODY = new Color(37, 135, 86);
        private static final Color FOOD = new Color(235, 90, 90);
        private static final Color TEXT = new Color(235, 239, 245);

        private final Deque<Point> snake = new ArrayDeque<>();
        private final Random random = new Random();
        private final Timer timer;

        private Point food;
        private Direction direction = Direction.RIGHT;
        private Direction nextDirection = Direction.RIGHT;
        private boolean running = false;
        private boolean paused = false;
        private int score = 0;
        private int bestScore = 0;

        GamePanel() {
            setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
            setBackground(BACKGROUND);
            setFocusable(true);
            setupKeyBindings();

            timer = new Timer(GAME_SPEED_MS, this::onGameTick);
            startNewGame();
        }

        private void setupKeyBindings() {
            InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
            ActionMap actionMap = getActionMap();

            bindKey(inputMap, actionMap, KeyEvent.VK_UP, "up", () -> changeDirection(Direction.UP));
            bindKey(inputMap, actionMap, KeyEvent.VK_W, "w", () -> changeDirection(Direction.UP));
            bindKey(inputMap, actionMap, KeyEvent.VK_DOWN, "down", () -> changeDirection(Direction.DOWN));
            bindKey(inputMap, actionMap, KeyEvent.VK_S, "s", () -> changeDirection(Direction.DOWN));
            bindKey(inputMap, actionMap, KeyEvent.VK_LEFT, "left", () -> changeDirection(Direction.LEFT));
            bindKey(inputMap, actionMap, KeyEvent.VK_A, "a", () -> changeDirection(Direction.LEFT));
            bindKey(inputMap, actionMap, KeyEvent.VK_RIGHT, "right", () -> changeDirection(Direction.RIGHT));
            bindKey(inputMap, actionMap, KeyEvent.VK_D, "d", () -> changeDirection(Direction.RIGHT));
            bindKey(inputMap, actionMap, KeyEvent.VK_P, "pause", this::togglePause);
            bindKey(inputMap, actionMap, KeyEvent.VK_SPACE, "restart", this::startNewGame);
        }

        private void bindKey(InputMap inputMap, ActionMap actionMap, int keyCode, String name, Runnable action) {
            inputMap.put(KeyStroke.getKeyStroke(keyCode, 0), name);
            actionMap.put(name, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    action.run();
                }
            });
        }

        private void startNewGame() {
            snake.clear();
            snake.addFirst(new Point(8, 10));
            snake.addLast(new Point(7, 10));
            snake.addLast(new Point(6, 10));

            direction = Direction.RIGHT;
            nextDirection = Direction.RIGHT;
            score = 0;
            paused = false;
            running = true;
            spawnFood();
            timer.start();
            repaint();
        }

        private void changeDirection(Direction newDirection) {
            if (isOpposite(direction, newDirection)) {
                return;
            }
            nextDirection = newDirection;
        }

        private boolean isOpposite(Direction current, Direction next) {
            return current == Direction.UP && next == Direction.DOWN
                    || current == Direction.DOWN && next == Direction.UP
                    || current == Direction.LEFT && next == Direction.RIGHT
                    || current == Direction.RIGHT && next == Direction.LEFT;
        }

        private void togglePause() {
            if (!running) {
                return;
            }
            paused = !paused;
            repaint();
        }

        private void onGameTick(ActionEvent event) {
            if (!running || paused) {
                return;
            }

            direction = nextDirection;
            Point head = snake.peekFirst();
            Point newHead = new Point(head);

            switch (direction) {
                case UP -> newHead.y--;
                case DOWN -> newHead.y++;
                case LEFT -> newHead.x--;
                case RIGHT -> newHead.x++;
            }

            if (hitsWall(newHead) || hitsBody(newHead)) {
                gameOver();
                return;
            }

            snake.addFirst(newHead);
            if (newHead.equals(food)) {
                score += 10;
                bestScore = Math.max(bestScore, score);
                spawnFood();
            } else {
                snake.removeLast();
            }

            repaint();
        }

        private boolean hitsWall(Point point) {
            return point.x < 0 || point.x >= COLS || point.y < 0 || point.y >= ROWS;
        }

        private boolean hitsBody(Point point) {
            return snake.contains(point);
        }

        private void gameOver() {
            running = false;
            timer.stop();
            repaint();
        }

        private void spawnFood() {
            do {
                food = new Point(random.nextInt(COLS), random.nextInt(ROWS));
            } while (snake.contains(food));
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            drawGrid(g);
            drawFood(g);
            drawSnake(g);
            drawScore(g);

            if (paused) {
                drawCenteredMessage(g, "Paused", "Press P to continue");
            } else if (!running) {
                drawCenteredMessage(g, "Game Over", "Press Space to restart");
            }
        }

        private void drawGrid(Graphics2D g) {
            g.setColor(GRID);
            for (int x = 0; x <= BOARD_WIDTH; x += CELL_SIZE) {
                g.drawLine(x, 0, x, BOARD_HEIGHT);
            }
            for (int y = 0; y <= BOARD_HEIGHT; y += CELL_SIZE) {
                g.drawLine(0, y, BOARD_WIDTH, y);
            }
        }

        private void drawFood(Graphics2D g) {
            g.setColor(FOOD);
            int margin = 4;
            g.fillOval(
                    food.x * CELL_SIZE + margin,
                    food.y * CELL_SIZE + margin,
                    CELL_SIZE - margin * 2,
                    CELL_SIZE - margin * 2
            );
        }

        private void drawSnake(Graphics2D g) {
            boolean first = true;
            for (Point part : snake) {
                g.setColor(first ? SNAKE_HEAD : SNAKE_BODY);
                int margin = first ? 2 : 3;
                g.fillRoundRect(
                        part.x * CELL_SIZE + margin,
                        part.y * CELL_SIZE + margin,
                        CELL_SIZE - margin * 2,
                        CELL_SIZE - margin * 2,
                        10,
                        10
                );
                first = false;
            }
        }

        private void drawScore(Graphics2D g) {
            g.setColor(TEXT);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Score: " + score, 16, 24);
            g.drawString("Best: " + bestScore, 16, 46);
            g.setFont(new Font("Arial", Font.PLAIN, 13));
            g.drawString("Arrows/WASD: Move   P: Pause   Space: Restart", 16, BOARD_HEIGHT - 14);
        }

        private void drawCenteredMessage(Graphics2D g, String title, String subtitle) {
            g.setColor(new Color(0, 0, 0, 170));
            g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

            g.setColor(TEXT);
            g.setFont(new Font("Arial", Font.BOLD, 42));
            drawCenteredText(g, title, BOARD_HEIGHT / 2 - 20);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            drawCenteredText(g, subtitle, BOARD_HEIGHT / 2 + 24);
        }

        private void drawCenteredText(Graphics2D g, String text, int y) {
            FontMetrics metrics = g.getFontMetrics();
            int x = (BOARD_WIDTH - metrics.stringWidth(text)) / 2;
            g.drawString(text, x, y);
        }
    }
}
