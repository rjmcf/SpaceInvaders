/**
 * Created by Rjmcf on 11/06/2016.
 */
public class AlienShotEntity extends Entity {
    private Game game;
    private int moveSpeed = 300;

    public AlienShotEntity(Game game, String ref, int x, int y) {
        super(ref,x,y,false);

        this.game = game;
        dy = moveSpeed;
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
