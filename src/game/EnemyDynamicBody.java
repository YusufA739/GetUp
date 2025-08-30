package game;

import city.cs.engine.DynamicBody;
import city.cs.engine.Shape;
import city.cs.engine.World;

/**
 * Class wrapper for DynamicBody, so that CollisionListeners will ignore all other DynamicBodies and only respond to collision events between the specific PlayerDynamicBody.
 */
public class EnemyDynamicBody extends DynamicBody {
    public EnemyDynamicBody(World w, Shape s) {
        super(w, s);
    }

    public EnemyDynamicBody(World w) {
        super(w);
    }
}
