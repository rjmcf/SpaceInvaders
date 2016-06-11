/**
 * Created by Rjmcf on 10/06/2016.
 */
public class ShotEntity extends Entity {
    private double moveSpeed = -300;
    private Game game;

    public ShotEntity(Game game, String ref, int x, int y) {
        super(ref,x,y,false,false);

        this.game = game;

        setVerticalMovement(moveSpeed);
    }

    @Override
    public void move(long delta) {
        // proceed with normal move
        super.move(delta);

        // if we shot off the screen, remove ourselfs
        if (y < -100) {
            game.removeEntity(this);
        }
    }

    @Override
    public void collidedWith(Entity other) {
        // if we've hit an alien, kill it!
        if (other instanceof AlienEntity) {
            // remove the affected entities
            game.removeEntity(this);
            game.removeEntity(other);

            // notify the game that the alien has been killed
            game.notifyAlienKilled();
        }
    }
}
