package game;

import city.cs.engine.DynamicBody;
import city.cs.engine.Shape;
import city.cs.engine.World;

/**
 * Class wrapper for DynamicBody, so that CollisionListeners will ignore all other DynamicBodies and only respond to collision events between the specific PlayerDynamicBody.
 * It is just a DynamicBody wrapper with no extra code, just the Parent constructors from DynamicBody.
 */
public class PlayerDynamicBody extends DynamicBody {
    public PlayerDynamicBody(World w, Shape s) {
        super(w, s);
    }

    public PlayerDynamicBody(World w) {
        super(w);
    }
}
