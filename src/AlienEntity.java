
public class AlienEntity extends Entity {

    private Game game;
    private double moveSpeed = 75;

    public AlienEntity(Game game, String ref, int x, int y) {
        super(ref,x,y,true,true);

        this.game = game;
        setHorizontalMovement(-moveSpeed);
    }

    @Override
    public void move(long delta) {
        // if we have reached the left hand side of the screen and
        // are moving left then request a logic update
        if ((getHorizontalMovement() < 0) && (x < 10)) {
            game.updateLogic();
        }
        // and vice vesa, if we have reached the right hand side of
        // the screen and are moving right, request a logic update
        if ((getHorizontalMovement() > 0) && (x > 750)) {
            game.updateLogic();
        }

        // proceed with normal move
        super.move(delta);
    }

    @Override
    public void collidedWith(Entity other) {
        // collisions with aliens are handled elsewhere
    }

    @Override
    public void doLogic() {
        // swap over horizontal movement and move down the
        // screen a bit
        setHorizontalMovement(-getHorizontalMovement());
        y += 10;

        // if we've reached the bottom of the screen then the player
        // dies
        if (y > 570) {
            game.notifyDeath();
        }
    }

    @Override
    public void animate() {
        if (!((SpriteSheet)sprites).isChangedThisLoop()) {
            ((SpriteSheet) sprites).next();
            ((SpriteSheet) sprites).setChangedThisLoop(true);
        }
    }
}
