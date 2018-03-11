package themazesolver;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MazeSolver {

    private MazeSolverFrame frame;
    private Image buffImg;
    private Graphics buffG;
    private boolean gameOn;
    private JFileChooser fc;
    private JButton fcButton;
    private int[][] map;
    private int mapWidth;
    private int mapHeight;
    private int tileSize;
    private int aiMaxAttempts = 1000;
    private int currentAI;
    private boolean mapLoaded = false;
    private boolean animatedSolve = false;
    public static final int WALL_COLOR = -3947581;
    public static final int GROUND_COLOR = -32985;
    public static final int AI_COLOR = -16735512;
    public static final int GOAL_COLOR = -14503604;
    
    private JTextArea textArea;

    private ArrayList<AI> ais;
    private ArrayList<AStar> aStars;

    public MazeSolver() {
        frame = new MazeSolverFrame(this);
        currentAI = 1;
        gameOn = true;
        textArea = frame.textOutPutArea;

        ais = new ArrayList();
        aStars = new ArrayList();

        fc = new JFileChooser();
        fc.setDialogTitle("The Maze Solver");
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "bmp"));
        fc.setAcceptAllFileFilterUsed(false);
        fcButton = new JButton();

        run();
    }

    public void run() {
        while (gameOn) {
            try {
                Thread.sleep(200);
                draw();

            } catch (Exception e) {
            }
        }
    }

    public void draw() {
        buffG = frame.renderPanel.getGraphics();
        buffG.clearRect(0, 0, frame.renderPanel.getWidth(), frame.renderPanel.getHeight());
        buffG.setColor(Color.black);
        if (mapLoaded) {
            for (int x = 0; x < mapWidth; ++x) {
                for (int y = 0; y < mapHeight; ++y) {
                    if (map[x][y] == -2) {
                        buffG.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
                    }
                    if (map[x][y] == -1) {
                        buffG.setColor(Color.green);
                        buffG.fillRect(x * tileSize, y * tileSize, tileSize, tileSize);
                        buffG.setColor(Color.black);
                    }
                }
            }
        }
        if (animatedSolve) {
            boolean aiMoving = false;
            switch (currentAI) {
                case 1:
                    for (AI a : ais) {
                        if (a.getAnimWalk() + 1 != a.getPathX().size()) {
                            aiMoving = true;
                        }
                        a.moveAnimWalk();
                        buffG.setColor(Color.blue);
                        buffG.fillRect(a.getPathX().get(a.getAnimWalk()) * tileSize, a.getPathY().get(a.getAnimWalk()) * tileSize, tileSize, tileSize);
                    }
                    break;
                case 2:
                    for (AStar a : aStars) {
                        if (a.getAnimWalk() != 0) {
                            aiMoving = true;
                        }
                        a.moveAnimWalk();
                        buffG.setColor(Color.blue);
                        buffG.fillRect(a.getPathX().get(a.getAnimWalk()) * tileSize, a.getPathY().get(a.getAnimWalk()) * tileSize, tileSize, tileSize);
                    }
                    break;
            }

            if (!aiMoving) {
                animatedSolve = false;
            }

        } else {
            switch (currentAI) {
                case 1:
                    for (AI a : ais) {
                        for (int i = 0; i < a.getPathX().size(); i++) {

                            buffG.setColor(Color.red);
                            buffG.fillRect(a.getPathX().get(i) * tileSize, a.getPathY().get(i) * tileSize, tileSize, tileSize);
                        }
                        buffG.setColor(Color.blue);
                        buffG.fillRect(a.getX() * tileSize, a.getY() * tileSize, tileSize, tileSize);
                    }
                    break;
                case 2:
                    for (AStar a : aStars) {
                        for (int i = 0; i < a.getPathX().size(); i++) {
                            buffG.setColor(Color.red);
                            buffG.fillRect(a.getPathX().get(i) * tileSize, a.getPathY().get(i) * tileSize, tileSize, tileSize);
                        }
                        buffG.setColor(Color.blue);
                        buffG.fillRect(a.getX() * tileSize, a.getY() * tileSize, tileSize, tileSize);
                    }
                    break;
            }
        }

    }

    public void loadMaze() {

        fc.setCurrentDirectory(new java.io.File("."));
        if (fc.showOpenDialog(fcButton) == JFileChooser.APPROVE_OPTION) {

        }
        String str = fc.getSelectedFile().getAbsolutePath();
        textArea.setText(textArea.getText() + "\n " + str);
        try {
            BufferedImage mapImg = ImageIO.read(new File(str));
            mapWidth = mapImg.getWidth();
            mapHeight = mapImg.getHeight();
            map = new int[mapWidth][mapHeight];
            ais.clear();
            aStars.clear();
            if (mapWidth >= mapHeight) {
                tileSize = frame.renderPanel.getWidth() / mapWidth;
            } else {
                tileSize = frame.renderPanel.getHeight() / mapHeight;
            }

            for (int y = 0; y < mapHeight; ++y) {
                map[y] = mapImg.getRGB(0, y, mapWidth, 1, null, 0, mapWidth);
            }

            for (int x = 0; x < mapWidth; ++x) {

                for (int y = 0; y < mapHeight; ++y) {

                    switch (map[x][y]) {
                        case GROUND_COLOR:
                            map[x][y] = 0;
                            break;
                        case WALL_COLOR:
                            map[x][y] = -2;
                            break;
                        case AI_COLOR:
                            map[x][y] = 0;
                            ais.add(new AI(x, y, textArea));
                            aStars.add(new AStar(x, y, textArea));
                            break;
                        case GOAL_COLOR:
                            map[x][y] = -1;
                            break;
                        default:
                            textArea.setText(textArea.getText() + "\n WHAT THE FUCK ARE YOU FORCING INSIDE ME?");
                            break;

                    }
                }
            }

        } catch (Exception e) {
            textArea.setText(textArea.getText() + "\n Failed Opening Mapfile: " + str);
        }
        mapLoaded = true;
    }

    public void runAI() {
            currentAI = 1;
        for (AI a : ais) {
            a.startAI(map.clone(), mapWidth, mapHeight, aiMaxAttempts);
        }
    }

    public void runAStar() {
        currentAI = 2;
        for (AStar a : aStars) {
            a.start(map.clone(), mapWidth, mapHeight);
        }
    }

    public void resetAI() {
        for (AI a : ais) {
            a.resetAI();
        }
        for (AStar a : aStars) {
            a.resetAI();
        }
        textArea.setText(textArea.getText() + "\n AI's where reset.");
    }

    public void animatedSolve() {
        animatedSolve = true;
        for (AI a : ais) {
            a.startAnimWalk();
        }
        for (AStar a : aStars) {
            a.startAnimWalk();
        }
        textArea.setText(textArea.getText() + "\n Starting animation.");
    }

    public void setAIMaxAttempts(int attm) {
        aiMaxAttempts = attm;
    }

}
