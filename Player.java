import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Player {
    private int x, y;
    private int width, height;
    private int dx, dy;
    private boolean left, right;
    private boolean jumping, falling;
    private boolean touchingWallLeft, touchingWallRight;
    private final int defaultSPEED = 8;
    private int SPEED = 8;
    private final int JUMP_SPEED = 18;
    private final int GRAVITY = 1;
    private final int WALL_JUMP_SPEED_X = 11; // Speed to move away from the wall when wall jumping
    private Color color;
    private Level level;
    private int leftKey, rightKey, jumpKey;
    private static int playerNum = 0;
    private boolean isTagged;
    private long tagCooldownEndTime; // Time when the player can tag again

    public Player(int x, int y, Color color, Level level, int leftKey, int rightKey, int jumpKey) {
        this.x = x;
        this.y = y;
        this.width = 16;
        this.height = 16;
        this.dx = 0;
        this.dy = 0;
        this.left = false;
        this.right = false;
        this.jumping = false;
        this.falling = true;
        this.touchingWallLeft = false;
        this.touchingWallRight = false;
        this.color = color;
        this.level = level;
        this.leftKey = leftKey;
        this.rightKey = rightKey;
        this.jumpKey = jumpKey;
        this.isTagged = playerNum == 0; // First player is tagged initially
        this.tagCooldownEndTime = System.currentTimeMillis();
        playerNum += 1;
    }

    public void update() {
        checkTagged();
        
        if (left) {
            if (dx > -SPEED) {
                dx -= 1;
            } else {
                dx = -SPEED;
            }
        } else if (right) {
            if (dx < SPEED) {
                dx += 1;
            } else {
                dx = SPEED;
            }
        } else {
            if (dx > 0) {
                dx -= 0.1;
            } else if (dx < 0) {
                dx -= -0.1;
            } else {
                dx = 0;
            }
        }

        // Always apply gravity
        if (falling) {
            dy += GRAVITY;
        }

        if (jumping) {
            dy = -JUMP_SPEED;
            jumping = false;
            falling = true;
        }

        // First, handle horizontal movement and collision
        x += dx;
        checkHorizontalCollision();

        // Then, handle vertical movement and collision
        y += dy;
        checkVerticalCollision();

        // Check if touching walls
        checkWallTouch();
    }

    private void checkHorizontalCollision() {
        int tileX = x / 50;
        int tileY = y / 50;

        if (dx > 0) { // Moving right
            if (isSolidTile(tileY, (x + width) / 50)) {
                x = ((x + width) / 50) * 50 - width - 1; // Set x to the left side of the tile
                dx = 0;
            }
        } else if (dx < 0) { // Moving left
            if (isSolidTile(tileY, x / 50)) {
                x = ((x / 50) + 1) * 50; // Set x to the right side of the tile
                dx = 0;
            }
        }
    }

    private void checkVerticalCollision() {
        int tileX = x / 50;
        int tileXMax = (x + width) / 50;
        int tileY = y / 50;

        if (dy < 0) { // Moving up (ceiling)
            if (isSolidTile((y + dy) / 50, tileX) || isSolidTile((y + dy) / 50, tileXMax)) {
                y = tileY * 50;
                dy = 0;
            }
        } else if (dy > 0) { // Moving down (floor)
            if (isSolidTile((y + height + dy) / 50, tileX) || isSolidTile((y + height + dy) / 50, tileXMax)) {
                y = (tileY + 1) * 50 - height;
                dy = 0;
                falling = false;
            } else {
                falling = true;
            }
        }

        // Check if the player is standing on solid ground
        if (!falling && !isSolidTile((y + height + 1) / 50, tileX)) {
            falling = true;
        }
    }

    private void checkWallTouch() {
        int tileY = y / 50;
        touchingWallLeft = isSolidTile(tileY, (x - 1) / 50);
        touchingWallRight = isSolidTile(tileY, (x + width + 1) / 50);
    }

    private boolean isSolidTile(int row, int col) {
        return level.getTile(row, col) == 1 || level.getTile(row, col) == 2;
    }

    public void draw(Graphics g) {
        Color DARK_ORANGE = new Color(255,69,0);
        if (isTagged){
            g.setColor(DARK_ORANGE);
        }
        else{
            g.setColor(color);
        }
        g.fillRect(x, y, width, height);
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void jump() {
        if (!falling) {
            jumping = true;
        } else if (falling) {
            if (touchingWallLeft) {
                dy = -JUMP_SPEED;
                dx = WALL_JUMP_SPEED_X; // Jump to the right
                falling = true;
            } else if (touchingWallRight) {
                dy = -JUMP_SPEED;
                dx = -WALL_JUMP_SPEED_X; // Jump to the left
                falling = true;
            }
        }
    }

    private void checkTagged(){
        if (isTagged){
            SPEED = (int) (defaultSPEED * 1.15);
        }
        else{
            SPEED = defaultSPEED;
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == leftKey) {
            setLeft(true);
        }
        if (key == rightKey) {
            setRight(true);
        }
        if (key == jumpKey) {
            jump();
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == leftKey) {
            setLeft(false);
        }
        if (key == rightKey) {
            setRight(false);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    public boolean isTagged() {
        return isTagged;
    }

    public void setTagged(boolean isTagged) {
        this.isTagged = isTagged;
    }

    public boolean canTag() {
        return System.currentTimeMillis() > tagCooldownEndTime;
    }

    public void resetCooldown() {
        tagCooldownEndTime = System.currentTimeMillis() + 2000; // 3-second cooldown
    }
}