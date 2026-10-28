package PlanetaryExpansionPack1;

import city.cs.engine.DynamicBody;
import city.cs.engine.Shape;
import city.cs.engine.World;

/**
 * Class wrapper for DynamicBody, so that CollisionListeners will ignore all other DynamicBodies and only respond to collision events between this specific variation of DynamicBody.
 */
public class PlanetDynamicBody extends DynamicBody {
    public PlanetDynamicBody(World w, Shape s) {
        super(w, s);
    }

    public PlanetDynamicBody(World w) {
        super(w);
    }
}
