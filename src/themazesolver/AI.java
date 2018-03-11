package themazesolver;

import java.util.ArrayList;
import java.util.Random;
import javax.swing.JTextArea;

public class AI {

    private int x;
    private int y;
    private int xOrigin;
    private int yOrigin;
    private ArrayList<Integer> pathX;
    private ArrayList<Integer> pathY;
    private ArrayList<Integer> direction;
    private int[][] maze;
    private int mazeW;
    private int mazeH;
    private int maxAliterations;
    private int currAliterations;
    private int attempts;
    private int maxAttempts;
    private boolean moved;
    private boolean goalFound;
    private Random rng;
    private int animatedWalk;
    private JTextArea textArea;

    public AI(int x, int y, JTextArea textArea) {
        this.textArea = textArea;
        pathX = new ArrayList();
        pathY = new ArrayList();
        direction = new ArrayList();
        rng = new Random();
        this.x = x;
        this.y = y;
        xOrigin = x;
        yOrigin = y;
    }
    
    public void resetAI(){
        x = xOrigin;
        y = yOrigin;
        pathX.clear();
        pathY.clear();
        for (int x = 0; x < mazeW; x++) {
                for (int y = 0; y < mazeH; y++) {
                    if (maze[x][y] == -3) {
                        maze[x][y] = 0;
                    }
                }
        }
    }

    public void startAI(int[][] mazeIn, int width, int height,int maxAttempts) {
        maze = mazeIn;
        mazeW = width;
        mazeH = height;
        pathX.clear();
        pathY.clear();
        maxAliterations = width * height;
        currAliterations = maxAliterations;
        this.maxAttempts = maxAttempts;
        x = xOrigin;
        y = yOrigin;
        attempts = 0;
        goalFound = false;
        calcPath();
    }

    private void restartPath() {
        if (maxAttempts > 0 && !goalFound && currAliterations > 0) {
            maxAttempts--;
            attempts++;
            currAliterations = maxAliterations;
            pathX.clear();
            pathY.clear();
            x = xOrigin;
            y = yOrigin;
            for (int x = 0; x < mazeW; x++) {
                for (int y = 0; y < mazeH; y++) {
                    if (maze[x][y] == -3) {
                        maze[x][y] = 0;
                    }
                }
            }
            calcPath();
        }
    }

    private void calcPath() {
        
        currAliterations--;
        moved = false;
        direction.add(0);
        direction.add(1);
        direction.add(2);
        direction.add(3);
        for (int i = 0; i < 4; i++) {
            switch (getDirection()) {
                case 0:
                    if (maze[x + 1][y] >= -1 && !moved) {
                        maze[x][y] = -3;
                        x++;
                        pathX.add(x);
                        pathY.add(y);
                        moved = true;
                    }
                    break;
                case 1:
                    if (maze[x][y + 1] >= -1 && !moved) {
                        maze[x][y] = -3;
                        y++;
                        pathX.add(x);
                        pathY.add(y);
                        moved = true;
                    }
                    break;
                case 2:
                    if (maze[x - 1][y] >= -1 && !moved) {
                        maze[x][y] = -3;
                        x--;
                        pathX.add(x);
                        pathY.add(y);
                        moved = true;
                    }
                    break;
                case 3:
                    if (maze[x][y - 1] >= -1 && !moved) {
                        maze[x][y] = -3;
                        y--;
                        pathX.add(x);
                        pathY.add(y);
                        moved = true;
                    }
                    break;
            }

        }
        if (maze[x][y] == -1) {
            
            textArea.setText(textArea.getText() + "\n [Goal]\n I had to try " + attempts + " times.\n My rout was " + pathX.size() + " steps.");
            goalFound = true;
            maxAttempts = 0;
            currAliterations = 0;
            return;
        }
        
        if (!moved) {
            restartPath();
        }
        
        if (!goalFound && maxAttempts > 0 && currAliterations > 0) {
            calcPath();
        }

    }

    private int getDirection() {
        int i = rng.nextInt(direction.size());
        int j = direction.get(i);
        direction.remove(i);
        return j;

    }
    
    public void startAnimWalk(){
        animatedWalk = 0;
    }
    
    public void moveAnimWalk(){
        if(animatedWalk + 1 < pathX.size()){
            animatedWalk++;
        }
    }
    
    public int getAnimWalk(){
        return animatedWalk;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public ArrayList<Integer> getPathX() {
        return pathX;
    }

    public ArrayList<Integer> getPathY() {
        return pathY;
    }

}
