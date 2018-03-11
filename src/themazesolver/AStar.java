package themazesolver;

import java.util.ArrayList;
import javax.swing.JTextArea;

public class AStar {

    private int x;
    private int y;
    private int xOrigin;
    private int yOrigin;
    private int xGoal;
    private int yGoal;
    private int width;
    private int height;
    private int[][] map;
    private boolean impossible;
    private boolean goalFound;
    private boolean moved;
    private int animatedWalk;
    private JTextArea textArea;
    

    private ArrayList<Integer> pathX;
    private ArrayList<Integer> pathY;

    public AStar(int x, int y, JTextArea textArea) {
        this.textArea = textArea;
        xOrigin = x;
        yOrigin = y;
        pathX = new ArrayList();
        pathY = new ArrayList();
    }
    
    public void resetAI(){
        x = xOrigin;
        y = yOrigin;
        pathX.clear();
        pathY.clear();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (map[x][y] > 0) {
                    map[x][y] = 0;
                }
            }
        }
    }

    public void start(int[][] mapIn, int width, int height) {
        x = xOrigin;
        y = yOrigin;
        map = mapIn;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (map[x][y] > 0) {
                    map[x][y] = 0;
                }
            }
        }
        map[x][y] = 1;
        this.width = width;
        this.height = height;
        impossible = false;
        goalFound = false;
        
        move();
    }

    public void startBackTrack() {
        pathX.clear();
        pathY.clear();
        x = xGoal;
        y = yGoal;
        pathX.add(x);
        pathY.add(y);
        backTrack();
    }

    public void move() { //This is so ugly it burns my eyes, A* better be good.
        if (impossible) {
            textArea.setText(textArea.getText() + "\n I deem this maze impossible...");
            return;
        }
        if (!goalFound) {
            moved = false;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (map[x][y] > 0) {
                        if (map[x + 1][y] == 0) {
                            map[x + 1][y] = map[x][y] + 1;
                            moved = true;
                        } else if (map[x + 1][y] == -1) {
                            goalFound = true;
                            xGoal = x;
                            yGoal = y;
                            startBackTrack();
                            return;
                        }
                        if (map[x][y + 1] == 0) {
                            map[x][y + 1] = map[x][y] + 1;
                            moved = true;
                        } else if (map[x][y + 1] == -1) {
                            goalFound = true;
                            xGoal = x;
                            yGoal = y;
                            startBackTrack();
                            return;
                        }
                        if (map[x - 1][y] == 0) {
                            map[x - 1][y] = map[x][y] + 1;
                            moved = true;
                        } else if (map[x - 1][y] == -1) {
                            goalFound = true;
                            xGoal = x;
                            yGoal = y;
                            startBackTrack();
                            return;
                        }
                        if (map[x][y - 1] == 0) {
                            map[x][y - 1] = map[x][y] + 1;
                            moved = true;
                        } else if (map[x][y - 1] == -1) {
                            goalFound = true;
                            xGoal = x;
                            yGoal = y;
                            startBackTrack();
                            return;
                        }
                    }

                }
            }
            if (!moved) {
                impossible = true;
            }
            move();
        }
    }

    public void backTrack() {
        if (map[x][y] == 1) {
            textArea.setText(textArea.getText() + "\n [Goal]\n Shortest route was " + pathX.size() + " steps.");
            return;
        }
        pathX.add(x);
        pathY.add(y);
        int lowestValue = 99999;
        int newX = x;
        int newY = y;
        if (map[x + 1][y] > 0 && map[x][y] > map[x + 1][y]) {
            lowestValue = map[x + 1][y];
            newX = x + 1;
            newY = y;
        }
        if (map[x - 1][y] > 0 && map[x][y] > map[x - 1][y] && map[x - 1][y] < lowestValue) {
            lowestValue = map[x - 1][y];
            newX = x - 1;
            newY = y;
        }
        if (map[x][y + 1] > 0 && map[x][y] > map[x][y + 1] && map[x][y + 1] < lowestValue) {
            lowestValue = map[x][y + 1];
            newX = x;
            newY = y + 1;
        }
        if (map[x][y - 1] > 0 && map[x][y] > map[x][y - 1] && map[x][y - 1] < lowestValue) {
            lowestValue = map[x][y - 1];
            newX = x;
            newY = y - 1;
        }
        x = newX;
        y = newY;
        backTrack();

    }
    
    public void startAnimWalk(){
        animatedWalk = pathX.size() - 1;
    }
    
    public void moveAnimWalk(){
        if(animatedWalk > 0){
            animatedWalk--;
        }
    }
    
    public int getAnimWalk(){
        return animatedWalk;
    }

    public ArrayList<Integer> getPathX() {
        return pathX;
    }

    public ArrayList<Integer> getPathY() {
        return pathY;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
