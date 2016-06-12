package source;

public class AlienShotEntity extends Entity {
    private Game game;
    private int moveSpeed = 300;

    public AlienShotEntity(Game game, String ref, int x, int y) {
        super(ref,x,y,false,false);

        this.game = game;
        setVerticalMovement(moveSpeed);
    }

    @Override
    public void move(long delta) {
        // proceed with normal move
        super.move(delta);

        // if shot off the screen, remove ourselfs
        if (y > 900) {
            game.removeEntity(this);
        }
    }

    @Override
    public void collidedWith(Entity other) {
        if (other instanceof ShipEntity) {
            game.notifyDeath();
        }
    }
}
