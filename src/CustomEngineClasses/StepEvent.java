//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package CustomEngineClasses;

import java.util.EventObject;

public class StepEvent extends EventObject {
    private final float step;

    StepEvent(Object source, float step) {
        super(source);
        this.step = step;
    }

    public float getStep() {
        return this.step;
    }
}
