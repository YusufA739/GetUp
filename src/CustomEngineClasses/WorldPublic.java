//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package CustomEngineClasses;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import city.cs.engine.SimulationSettings;
import city.cs.engine.StepListener;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Collision;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.Collision.PointState;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.Contact;

public class WorldPublic {
    public static final float DEFAULT_GRAVITY = 9.8F;
    public static final Vec2 UNIT_UP = new Vec2(0.0F, 1.0F);
    public List<DeferredContact> contactEvents;
    public Queue<DeferredContact> contactEventDispatchQueue;
    public List<StepListener> stepListeners;
    public Queue<StepListener> stepListenerDispatchQueue;
    public List<ChangeListener> changeListeners;
    public org.jbox2d.dynamics.World b2world;
    public float pendingSimTime;
    public Timer simTimer;
    public SimulationSettings simulationSettings;
    public static final int version = 40;

    public World(int framesPerSecond) {
        this.simulationSettings = new SimulationSettings(framesPerSecond);
        Vec2 worldGravity = new Vec2(0.0F, -9.8F);
        this.b2world = new org.jbox2d.dynamics.World(worldGravity);
        this.stepListeners = new LinkedList();
        this.contactEvents = new LinkedList();
        this.contactEventDispatchQueue = null;
        this.changeListeners = new LinkedList();
        this.b2world.setContactListener(new CollisionUpdater());
        this.pendingSimTime = 0.0F;
        this.simTimer = new Timer(this.simulationSettings.getTimeStepMilli(), new ActionListener() {
            public int tsm;

            {
                this.tsm = World.this.simulationSettings.getTimeStepMilli();
            }

            public void actionPerformed(ActionEvent evt) {
                World.this.step();
                int tsmNow = World.this.simulationSettings.getTimeStepMilli();
                if (tsmNow != this.tsm) {
                    System.err.println("changing frame rate");
                    this.tsm = tsmNow;
                    Timer t = (Timer)evt.getSource();
                    t.setDelay(this.tsm);
                }

            }
        });
    }

    public World() {
        this(24);
    }

    public void start() {
        if (!this.simTimer.isRunning()) {
            this.simTimer.start();
        }

    }

    public void stop() {
        if (this.simTimer.isRunning()) {
            this.simTimer.stop();
        }

    }

    public boolean isRunning() {
        return this.simTimer.isRunning();
    }

    public void oneStep() {
        if (!this.isRunning()) {
            float simTimeStep = this.simulationSettings.getSimTimeStep();
            StepEvent e = new StepEvent(this, simTimeStep);
            this.preStep(e);
            this.simulationStep(simTimeStep);
            this.postStep(e);
            this.propagateChanges();
        }

    }

    public void step() {
        float simTimeStep = this.simulationSettings.getSimTimeStep();
        this.pendingSimTime += this.simulationSettings.getTimeStep();
        if (this.pendingSimTime >= simTimeStep) {
            while(true) {
                StepEvent e = new StepEvent(this, simTimeStep);
                this.preStep(e);
                if (this.isRunning()) {
                    this.simulationStep(simTimeStep);
                    this.postStep(e);
                    this.pendingSimTime -= simTimeStep;
                    if (this.pendingSimTime >= simTimeStep) {
                        continue;
                    }
                }

                this.propagateChanges();
                break;
            }
        }

        this.simulationSettings.step();
    }

    public void simulationStep(float simTimeStep) {
        this.b2world.step(simTimeStep, this.simulationSettings.getVelocityIterations(), this.simulationSettings.getPositionIterations());
        this.dispatchContacts();
    }

    public SimulationSettings getSimulationSettings() {
        return this.simulationSettings;
    }

    void destroyBody(Body b) {
        org.jbox2d.dynamics.Body b2body = b.getBox2DBody();
        if (b.getWorld() == this) {
            if (b instanceof StepListener) {
                this.removeStepListener((StepListener)b);
            }

            this.removeCollisions(b);
            b2body.setUserData((Object)null);
            this.b2world.destroyBody(b2body);
            b.dispose();
        }
    }

    public void removeCollisions(Body b) {
        this.removeCollisions(this.contactEvents, (Body)b);
        if (this.contactEventDispatchQueue != null) {
            this.removeCollisions(this.contactEventDispatchQueue, (Body)b);
        }

    }

    public void removeCollisions(Iterable<DeferredContact> cl, Body b) {
        Iterator<DeferredContact> ci = cl.iterator();

        while(ci.hasNext()) {
            DeferredContact ce = (DeferredContact)ci.next();
            if (ce.getReportingFixture().getBody() == b || ce.getOtherFixture().getBody() == b) {
                ci.remove();
            }
        }

    }

    void removeCollisions(Fixture f) {
        this.removeCollisions(this.contactEvents, (Fixture)f);
        if (this.contactEventDispatchQueue != null) {
            this.removeCollisions(this.contactEventDispatchQueue, (Fixture)f);
        }

    }

    public void removeCollisions(Iterable<DeferredContact> cl, Fixture f) {
        Iterator<DeferredContact> ci = cl.iterator();

        while(ci.hasNext()) {
            DeferredContact ce = (DeferredContact)ci.next();
            if (ce.getReportingFixture() == f || ce.getOtherFixture() == f) {
                ci.remove();
            }
        }

    }

    public void setGravity(float g) {
        this.b2world.setGravity(new Vec2(0.0F, -g));
    }

    public float getGravity() {
        return this.b2world.getGravity().y * -1.0F;
    }

    org.jbox2d.dynamics.World getBox2DWorld() {
        return this.b2world;
    }

    public void addStepListener(StepListener listener) {
        this.stepListeners.add(listener);
    }

    public void removeStepListener(StepListener listener) {
        this.stepListeners.remove(listener);
        if (this.stepListenerDispatchQueue != null) {
            this.stepListenerDispatchQueue.remove(listener);
        }

    }

    public void preStep(StepEvent e) {
        this.stepListenerDispatchQueue = new LinkedList();
        this.stepListenerDispatchQueue.addAll(this.stepListeners);

        for(StepListener sl = (StepListener)this.stepListenerDispatchQueue.poll(); sl != null; sl = (StepListener)this.stepListenerDispatchQueue.poll()) {
            sl.preStep(e);
        }

        this.stepListenerDispatchQueue = null;
        this.contactEvents.clear();
    }

    public void postStep(StepEvent se) {
        this.stepListenerDispatchQueue = new LinkedList();
        this.stepListenerDispatchQueue.addAll(this.stepListeners);

        for(StepListener sl = (StepListener)this.stepListenerDispatchQueue.poll(); sl != null; sl = (StepListener)this.stepListenerDispatchQueue.poll()) {
            sl.postStep(se);
        }

        this.stepListenerDispatchQueue = null;
    }

    void addChangeListener(ChangeListener listener) {
        this.changeListeners.add(listener);
    }

    void removeChangeListener(ChangeListener listener) {
        this.changeListeners.remove(listener);
    }

    public void propagateChanges() {
        ChangeEvent e = new ChangeEvent(this);

        for(ChangeListener cl : this.changeListeners) {
            cl.stateChanged(e);
        }

    }

    public void dispatchContacts() {
        this.contactEventDispatchQueue = new LinkedList();

        for(DeferredContact e : this.contactEvents) {
            this.contactEventDispatchQueue.add(e);
            if (e instanceof DeferredCollision) {
                DeferredCollision ce = (DeferredCollision)e;
                this.contactEventDispatchQueue.add(ce.inverse());
            }
        }

        for(DeferredContact ce = (DeferredContact)this.contactEventDispatchQueue.poll(); ce != null; ce = (DeferredContact)this.contactEventDispatchQueue.poll()) {
            ce.dispatch();
        }

        this.contactEventDispatchQueue = null;
    }

    List<CollisionEvent> getCollisionEvents() {
        List<CollisionEvent> collisions = new LinkedList();

        for(DeferredContact ce : this.contactEvents) {
            if (ce instanceof DeferredCollision) {
                collisions.add(((DeferredCollision)ce).event);
            }
        }

        return collisions;
    }

    List<Body> getBodies() {
        LinkedList<Body> bodiesList = new LinkedList();

        for(org.jbox2d.dynamics.Body b2body = this.b2world.getBodyList(); b2body != null; b2body = b2body.getNext()) {
            Body body = Body.getGameBody(b2body);
            if (body != null) {
                bodiesList.add(body);
            }
        }

        return bodiesList;
    }

    public List<StaticBody> getStaticBodies() {
        LinkedList<StaticBody> bodiesList = new LinkedList();

        for(org.jbox2d.dynamics.Body b2body = this.b2world.getBodyList(); b2body != null; b2body = b2body.getNext()) {
            Body body = Body.getGameBody(b2body);
            if (body instanceof StaticBody) {
                bodiesList.add((StaticBody)body);
            }
        }

        return bodiesList;
    }

    public List<DynamicBody> getDynamicBodies() {
        LinkedList<DynamicBody> bodiesList = new LinkedList();

        for(org.jbox2d.dynamics.Body b2body = this.b2world.getBodyList(); b2body != null; b2body = b2body.getNext()) {
            Body body = Body.getGameBody(b2body);
            if (body instanceof DynamicBody) {
                bodiesList.add((DynamicBody)body);
            }
        }

        return bodiesList;
    }

    public class DeferredSensor implements DeferredContact {
        public boolean begin;
        public SensorEvent event;

        public DeferredSensor(Sensor sensor, SolidFixture f, boolean begin) {
            this.event = new SensorEvent(sensor, f);
            this.begin = begin;
        }

        public Fixture getReportingFixture() {
            return this.event.getSensor();
        }

        public Fixture getOtherFixture() {
            return this.event.getContactFixture();
        }

        public void dispatch() {
            if (this.begin) {
                this.event.getSensor().beginContact(this.event);
            } else {
                this.event.getSensor().endContact(this.event);
            }

        }
    }

    public class DeferredCollision implements DeferredContact {
        public CollisionEvent event;

        public DeferredCollision(CollisionEvent event) {
            this.event = event;
        }

        public Fixture getReportingFixture() {
            return this.event.getReportingFixture();
        }

        public Fixture getOtherFixture() {
            return this.event.getOtherFixture();
        }

        public void dispatch() {
            this.event.getReportingBody().collide(this.event);
        }

        public DeferredCollision inverse() {
            return World.this.new DeferredCollision(this.event.inverse());
        }
    }

    public class CollisionUpdater implements ContactListener {
        public CollisionUpdater() {
        }

        public void beginContact(Contact contact) {
            this.addSensorEvent(contact.getFixtureA(), contact.getFixtureB(), true);
        }

        public void endContact(Contact contact) {
            this.addSensorEvent(contact.getFixtureA(), contact.getFixtureB(), false);
        }

        public void addSensorEvent(org.jbox2d.dynamics.Fixture b2fixtureA, org.jbox2d.dynamics.Fixture b2fixtureB, boolean begin) {
            if (b2fixtureA.isSensor() != b2fixtureB.isSensor()) {
                Fixture fixtureA = Fixture.getGameFixture(b2fixtureA);
                Fixture fixtureB = Fixture.getGameFixture(b2fixtureB);
                if (b2fixtureA.isSensor() && fixtureA instanceof Sensor && fixtureB instanceof SolidFixture) {
                    World.this.contactEvents.add(World.this.new DeferredSensor((Sensor)fixtureA, (SolidFixture)fixtureB, begin));
                } else if (b2fixtureB.isSensor() && fixtureB instanceof Sensor && fixtureA instanceof SolidFixture) {
                    World.this.contactEvents.add(World.this.new DeferredSensor((Sensor)fixtureB, (SolidFixture)fixtureA, begin));
                }

            }
        }

        public void preSolve(Contact contact, Manifold oldManifold) {
            org.jbox2d.dynamics.Fixture b2fixtureA = contact.getFixtureA();
            org.jbox2d.dynamics.Fixture b2fixtureB = contact.getFixtureB();
            Fixture fixtureA = Fixture.getGameFixture(b2fixtureA);
            Fixture fixtureB = Fixture.getGameFixture(b2fixtureB);
            if (fixtureA != null && fixtureB != null && fixtureA instanceof SolidFixture && fixtureB instanceof SolidFixture) {
                Body bodyA = Body.getGameBodyFromFixture(b2fixtureA);
                Body bodyB = Body.getGameBodyFromFixture(b2fixtureB);
                if (bodyA != null && bodyB != null) {
                    Collision.PointState[] state1 = new Collision.PointState[2];
                    Collision.PointState[] state2 = new Collision.PointState[2];
                    Collision.getPointStates(state1, state2, oldManifold, contact.getManifold());
                    if (state2[0] == PointState.ADD_STATE) {
                        CollisionEvent ce = new CollisionEvent((SolidFixture)fixtureA, (SolidFixture)fixtureB, contact);
                        World.this.contactEvents.add(World.this.new DeferredCollision(ce));
                    }

                }
            }
        }

        public void postSolve(Contact contact, ContactImpulse impulse) {
        }
    }

    public interface DeferredContact {
        Fixture getReportingFixture();

        Fixture getOtherFixture();

        void dispatch();
    }
}
