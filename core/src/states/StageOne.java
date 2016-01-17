package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.rezzo.libgdxjam.GameStateManager;
import com.rezzo.libgdxjam.LifeIsSpace;
import com.rezzo.libgdxjam.State;
import entities.Asteroid;
import entities.Creature;
import entities.Element;
import entities.Planet;
import utils.Assets;
import utils.StageOneUI;

import java.util.ArrayList;
import java.util.Iterator;

public class StageOne extends State {

    private ShapeRenderer sr;

    private Planet planet;
    private Creature creature;

    private TextureRegion spaceBg;

    private float angle = -180;
    private float orbitWidth = 150;
    private float orbitHeight = 130;

    private float changeOrbitSpeed = 100, minOrbit = 90, maxOrbit = 425;

    private enum PlanetPosition {UP, RIGHT, DOWN, LEFT}

    private PlanetPosition planetPosition;

    private Ellipse orbit;

    private float planetSpeed;

    private StageOneUI stageOneUI;

    private Sprite sun;
    private Circle sunHitbox;

    float stateTime = 0;

    boolean hitByAst = false;
    float randomWidth, randomHeight, initWidth, initHeight;

    ArrayList<Asteroid> asteroids;
    long lastAsteroidSpawn = 0;
    long lastElementSpawn = 0;

    ArrayList<Element> elements;

    boolean debug = false;

    //Particle effect

    ParticleEffect hitEffect, part_blue, part_brown, part_purple, part_red;
    ParticleEffectPool hitEffectPool, bluePool, brownPool, purplePool, redPool;
    ArrayList<ParticleEffectPool.PooledEffect> pooledEffects;

    public StageOne(GameStateManager gms) {
        super(gms);
        cam.setToOrtho(false, LifeIsSpace.WIDTH, LifeIsSpace.HEIGHT);
        cam.update();

        sr = new ShapeRenderer();

        planet = new Planet(LifeIsSpace.WIDTH / 2, LifeIsSpace.HEIGHT / 2);

        orbit = new Ellipse(new Vector2(LifeIsSpace.WIDTH / 2, LifeIsSpace.HEIGHT / 2), orbitWidth, orbitHeight);

        creature = new Creature(0, 0);

        spaceBg = Assets.spaceBg;

        sun = new Sprite(Assets.sunText);
        sun.setPosition(LifeIsSpace.WIDTH / 2 - sun.getWidth() / 2, LifeIsSpace.HEIGHT / 2 - sun.getHeight() / 2);

        sunHitbox = new Circle(sun.getX() + sun.getWidth() / 2, sun.getY() + sun.getHeight() / 2, 22);

        planetPosition = PlanetPosition.UP;

        stageOneUI = new StageOneUI(this);
        asteroids = new ArrayList<Asteroid>();
        elements = new ArrayList<Element>();

        //Particles
        hitEffect = new ParticleEffect();
        hitEffect.load(Gdx.files.internal("particles/collision"), Gdx.files.internal("particles"));

        part_blue = new ParticleEffect();
        part_blue.load(Gdx.files.internal("particles/part_blue"), Gdx.files.internal("particles"));

        part_brown = new ParticleEffect();
        part_brown.load(Gdx.files.internal("particles/part_brown"), Gdx.files.internal("particles"));

        part_purple = new ParticleEffect();
        part_purple.load(Gdx.files.internal("particles/part_purple"), Gdx.files.internal("particles"));

        part_red = new ParticleEffect();
        part_red.load(Gdx.files.internal("particles/part_red"), Gdx.files.internal("particles"));

        pooledEffects = new ArrayList<>();

        hitEffectPool = new ParticleEffectPool(hitEffect, 1, 7);
        bluePool = new ParticleEffectPool(part_blue, 1, 5);
        brownPool = new ParticleEffectPool(part_brown, 1, 5);
        purplePool = new ParticleEffectPool(part_purple, 1, 5);
        redPool = new ParticleEffectPool(part_red, 1, 5);

        ////
        createElement();
        spawnAsteroid();

    }

    private void createElement() {
        Element element = new Element(this, MathUtils.random(1, 4), MathUtils.random(1, 2),
                MathUtils.random(0, LifeIsSpace.HEIGHT), MathUtils.random(50, 100), MathUtils.random(10, 20));
        elements.add(element);

        lastElementSpawn = TimeUtils.millis();
    }

    private void spawnAsteroid() {
        Asteroid asteroid = new Asteroid(this, MathUtils.random(1, 2), MathUtils.random(0, LifeIsSpace.HEIGHT),
                MathUtils.random(100, 500), MathUtils.random(0, 2), MathUtils.random(50, 201));

        asteroids.add(asteroid);

        lastAsteroidSpawn = TimeUtils.millis();

    }

    @Override
    protected void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            if (debug)
                debug = false;
            else
                debug = true;
        }
        creature.update(dt);
        movePlanetOrbit(dt);
    }

    @Override
    public void update(float dt) {
        calculatePosition(dt);
        handleInput(dt);
        planet.update();

        if (TimeUtils.millis() - lastAsteroidSpawn > 500)
            spawnAsteroid();

        if (TimeUtils.millis() - lastElementSpawn > 1000)
            createElement();

        Iterator<Asteroid> iter = asteroids.iterator();

        while (iter.hasNext()) {
            Asteroid asteroid = iter.next();
            asteroid.update(dt);
            if (asteroid.collisionDetectionSun()) {
                iter.remove();
                ParticleEffectPool.PooledEffect pooledEffect = hitEffectPool.obtain();
                pooledEffect.setPosition(asteroid.getHitbox().x, asteroid.getHitbox().y);
                pooledEffects.add(pooledEffect);
                Assets.hit.play(0.1f);
            } else if (asteroid.collisionDetectionPlanet()) {
                iter.remove();
                ParticleEffectPool.PooledEffect pooledEffect = hitEffectPool.obtain();
                pooledEffect.setPosition(asteroid.getHitbox().x, asteroid.getHitbox().y);
                pooledEffects.add(pooledEffect);
                Assets.hit.play(0.1f);
                stateTime = 0;
                initWidth = orbitWidth;
                initHeight = orbitHeight;
                randomWidth = MathUtils.random(-75, 75);
                randomHeight = MathUtils.random(-75, 75);
                hitByAst = true;
            }
        }

        if (hitByAst)
            hitByAsteroid(dt);

        Iterator<Element> elementIter = elements.iterator();

        while (elementIter.hasNext()) {
            Element element = elementIter.next();
            element.update(dt);
            if (element.destroy) {
                elementIter.remove();
                ParticleEffectPool.PooledEffect pooledEffect;
                switch (element.getType()) {
                    case COLD:
                        pooledEffect = bluePool.obtain();
                        break;
                    case HEAT:
                        pooledEffect = redPool.obtain();
                        break;
                    case GRAV:
                        pooledEffect = purplePool.obtain();
                        break;
                    case ROCK:
                        pooledEffect = brownPool.obtain();
                        break;
                    default:
                        pooledEffect = hitEffectPool.obtain();
                }
                pooledEffect.setPosition(element.hitbox.x, element.hitbox.y);
                pooledEffects.add(pooledEffect);
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {

        batch.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(cam.combined);

        batch.begin();
        batch.draw(spaceBg, 0, 0);
        batch.end();

        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(StageOneUI.orbitColor);
        sr.ellipse(orbit.x - orbitWidth / 2, orbit.y - orbitHeight / 2, orbitWidth, orbitHeight);
        sr.end();

        if (debug) {
            batch.begin();
            Assets.normalFont.draw(batch, "DEBUG ON", LifeIsSpace.WIDTH - 75, LifeIsSpace.HEIGHT - 10);
            batch.end();
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.setColor(Color.valueOf("1325a5"));
            for (Asteroid asteroid : asteroids) {
                sr.circle(asteroid.getHitbox().x, asteroid.getHitbox().y, asteroid.getHitbox().radius);
            }
            sr.circle(planet.getHitbox().x, planet.getHitbox().y, planet.getHitbox().radius);
            sr.circle(sunHitbox.x, sunHitbox.y, sunHitbox.radius);
            sr.end();
        }

        for (Element element : elements)
            element.draw(sr, batch);

        batch.begin();
        sun.draw(batch);
        planet.draw(batch);
        creature.draw(batch);
        for (Asteroid asteroid : asteroids) {
            asteroid.draw(batch);
        }

        for (int i = pooledEffects.size() - 1; i >= 0; i--) {
            ParticleEffectPool.PooledEffect effect = pooledEffects.get(i);
            effect.draw(batch, Gdx.graphics.getDeltaTime());
            if (effect.isComplete()) {
                effect.free();
                pooledEffects.remove(i);
            }
        }
        batch.end();

        stageOneUI.endStage(cam, planet, gms);

        stageOneUI.draw(batch);
    }

    private void calculatePosition(float dt) {

        float dX = orbitWidth / 2 * MathUtils.cosDeg(angle) + orbit.x;
        float dY = orbitHeight / 2 * MathUtils.sinDeg(angle) + orbit.y;

        planet.setX(dX);
        planet.setY(dY);

        angle -= planetSpeed * dt;

        if (angle < -360)
            angle = 0;

        if (angle <= -235 && angle >= -324.99f) {
            planetPosition = PlanetPosition.UP;
        }
        if (angle <= -325 || angle >= -54.99f) {
            planetPosition = PlanetPosition.RIGHT;
        }
        if (angle <= -55 && angle >= -144.99f) {
            planetPosition = PlanetPosition.DOWN;
        }
        if (angle <= -145 && angle >= -234.99f) {
            planetPosition = PlanetPosition.LEFT;
        }

        planetSpeed = 100 - (orbitWidth + orbitHeight) / 10f;

        if (planetSpeed < 5) {
            planetSpeed = 5;
        }

        dX = 17 * MathUtils.cosDeg(creature.getAngle()) + planet.getX();
        dY = 17 * MathUtils.sinDeg(creature.getAngle()) + planet.getY();

        creature.setX(dX);
        creature.setY(dY);

        dX = 15 * MathUtils.cosDeg(creature.getAngle() - 180) + planet.getX();
        dY = 15 * MathUtils.sinDeg(creature.getAngle() - 180) + planet.getY();

        creature.getArrow().setPosition(dX - creature.getArrow().getWidth() / 2, dY - creature.getArrow().getHeight() / 2);
        creature.getArrow().setRotation(creature.getAngle() - 180);

    }

    public void hitByAsteroid(float dt) {
        stateTime += dt;
        float progress = stateTime / 1;

        if (progress > 0.5) {
            hitByAst = false;
        }

        if (initWidth + randomWidth > minOrbit && initWidth + randomWidth < maxOrbit)
            orbitWidth = MathUtils.lerp(initWidth, initWidth + randomWidth, progress);
        if (initHeight + randomHeight > minOrbit && initHeight + randomHeight < maxOrbit)
            orbitHeight = MathUtils.lerp(initHeight, initHeight + randomHeight, progress);
    }

    private void movePlanetOrbit(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.X) && !creature.isMoving()) {
            switch (planetPosition) {
                case UP:
                    switch (creature.getPreviousState()) {
                        case UP:
                            /*if (canMoveOrbitTest(1)*//*canMoveOrbit(1) > 45f*//*) {
                                orbit.y -= changeOrbitSpeed / 2 * dt;
                                creature.setCreatureState(Creature.CreatureState.PUSH_DOWN);
                            }*/

                            orbitHeight -= changeOrbitSpeed * dt;
                            creature.setCreatureState(Creature.CreatureState.PUSH_DOWN);

                            break;
                        case RIGHT:
                            /*if (canMoveOrbitTest(3)*//*canMoveOrbit(3) > 45f*//*) {
                                orbit.y += changeOrbitSpeed / 2 * dt;
                                orbitHeight -= changeOrbitSpeed * dt;
                                creature.setCreatureState(Creature.CreatureState.PUSH_LEFT);
                            }*/

                            creature.setCreatureState(Creature.CreatureState.PUSH_LEFT);

                            break;
                        case DOWN:
                           /* if (canMoveOrbitTest(3)*//*canMoveOrbit(3) > 45f*//*) {
                                orbit.y += changeOrbitSpeed / 2 * dt;
                                creature.setCreatureState(Creature.CreatureState.PUSH_UP);
                            }*/
                            orbitHeight += changeOrbitSpeed * dt;
                            creature.setCreatureState(Creature.CreatureState.PUSH_UP);
                            break;
                        case LEFT:
                                /*creature.setCreatureState(Creature.CreatureState.PUSH_RIGHT);
                                orbit.y -= changeOrbitSpeed / 2 * dt;
                                orbitHeight += changeOrbitSpeed * dt;*/
                            creature.setCreatureState(Creature.CreatureState.PUSH_RIGHT);
                            break;
                    }
                    break;
                case RIGHT:
                    switch (creature.getPreviousState()) {
                        case UP:
                           /* if (canMoveOrbitTest(2)) {
                                creature.setCreatureState(Creature.CreatureState.PUSH_DOWN);
                                orbit.x -= changeOrbitSpeed / 2 * dt;
                                orbitWidth += changeOrbitSpeed * dt;
                            }*/
                            creature.setCreatureState(Creature.CreatureState.PUSH_DOWN);
                            break;
                        case RIGHT:
                           /* if (canMoveOrbitTest(2)*//*canMoveOrbit(2) > 45f*//*) {
                                creature.setCreatureState(Creature.CreatureState.PUSH_LEFT);
                                orbit.x -= changeOrbitSpeed / 2 * dt;
                            }*/
                            orbitWidth -= changeOrbitSpeed * dt;
                            creature.setCreatureState(Creature.CreatureState.PUSH_LEFT);
                            break;
                        case DOWN:
                           /* if (canMoveOrbitTest(4)*//*canMoveOrbit(4) > 45*//*) {
                                creature.setCreatureState(Creature.CreatureState.PUSH_UP);
                                orbit.x += changeOrbitSpeed / 2 * dt;
                                orbitWidth -= changeOrbitSpeed * dt;
                            }*/
                            creature.setCreatureState(Creature.CreatureState.PUSH_UP);
                            break;
                        case LEFT:
                           /* if (canMoveOrbitTest(4)*//*canMoveOrbit(4) > 45*//*) {
                                creature.setCreatureState(Creature.CreatureState.PUSH_RIGHT);
                                orbit.x += changeOrbitSpeed / 2 * dt;
                            }*/
                            orbitWidth += changeOrbitSpeed * dt;
                            creature.setCreatureState(Creature.CreatureState.PUSH_RIGHT);
                            break;
                    }
                    break;
                case DOWN:
                    switch (creature.getPreviousState()) {
                        case UP:
                            /*if (canMoveOrbitTest(1)*//*canMoveOrbit(1) > 45*//*) {
                                ;
                                creature.setCreatureState(Creature.CreatureState.PUSH_DOWN);
                                orbit.y -= changeOrbitSpeed / 2 * dt;
                            }*/
                            creature.setCreatureState(Creature.CreatureState.PUSH_DOWN);
                            orbitHeight += changeOrbitSpeed * dt;
                            break;
                        case RIGHT:
                           /* if (canMoveOrbitTest(3)) {
                                creature.setCreatureState(Creature.CreatureState.PUSH_LEFT);
                                orbit.y += changeOrbitSpeed / 2 * dt;
                                orbitHeight += changeOrbitSpeed * dt;
                            }*/
                            creature.setCreatureState(Creature.CreatureState.PUSH_LEFT);
                            break;
                        case DOWN:
                            /*if (canMoveOrbitTest(3)*//*canMoveOrbit(3) > 45*//*) {
                                creature.setCreatureState(Creature.CreatureState.PUSH_UP);
                                orbit.y += changeOrbitSpeed / 2 * dt;
                            }*/
                            orbitHeight -= changeOrbitSpeed * dt;
                            creature.setCreatureState(Creature.CreatureState.PUSH_UP);
                            break;
                        case LEFT:
                            /*if (canMoveOrbitTest(1)*//*canMoveOrbit(1) > 45*//*) {
                                creature.setCreatureState(Creature.CreatureState.PUSH_RIGHT);
                                orbit.y -= changeOrbitSpeed / 2 * dt;
                                orbitHeight -= changeOrbitSpeed * dt;
                            }*/

                            creature.setCreatureState(Creature.CreatureState.PUSH_RIGHT);

                            break;
                    }
                    break;
                case LEFT:
                    switch (creature.getPreviousState()) {
                        case UP:
                            /*if (canMoveOrbitTest(2)*//*canMoveOrbit(2) > 45*//*) {
                                creature.setCreatureState(Creature.CreatureState.PUSH_DOWN);
                                orbit.x -= changeOrbitSpeed / 2 * dt;
                                orbitWidth -= changeOrbitSpeed * dt;
                            }*/
                            creature.setCreatureState(Creature.CreatureState.PUSH_DOWN);
                            break;
                        case RIGHT:
                            /*if (canMoveOrbitTest(2)*//*canMoveOrbit(2) > 45*//*) {
                                creature.setCreatureState(Creature.CreatureState.PUSH_LEFT);
                                orbit.x -= changeOrbitSpeed / 2 * dt;
                            }*/
                            creature.setCreatureState(Creature.CreatureState.PUSH_LEFT);
                            orbitWidth += changeOrbitSpeed * dt;
                            break;
                        case DOWN:
                           /* if (canMoveOrbitTest(4)) {
                                creature.setCreatureState(Creature.CreatureState.PUSH_UP);
                                orbit.x += changeOrbitSpeed / 2 * dt;
                                orbitWidth += changeOrbitSpeed * dt;
                            }*/
                            creature.setCreatureState(Creature.CreatureState.PUSH_UP);
                            break;
                        case LEFT:
                            /*if (canMoveOrbitTest(4)*//*canMoveOrbit(4) > 45*//*) {
                                creature.setCreatureState(Creature.CreatureState.PUSH_RIGHT);
                                orbit.x += changeOrbitSpeed / 2 * dt;
                            }*/
                            creature.setCreatureState(Creature.CreatureState.PUSH_RIGHT);
                            orbitWidth -= changeOrbitSpeed * dt;
                            break;
                    }
                    break;
            }

            //System.out.println("WIDTH: " + orbitWidth + " HEIGHT: " + orbitHeight);

            if (orbitWidth < minOrbit)
                orbitWidth = minOrbit;
            if (orbitHeight < minOrbit)
                orbitHeight = minOrbit;
            if (orbitWidth > maxOrbit)
                orbitWidth = maxOrbit;
            if (orbitHeight > maxOrbit)
                orbitHeight = maxOrbit;
        } else {
            creature.setCreatureState(creature.getPreviousCreatureState());
        }
    }

    public Planet getPlanet() {
        return planet;
    }

    public float getOrbitWidth() {
        return orbitWidth;
    }

    public float getOrbitHeight() {
        return orbitHeight;
    }

    public Ellipse getOrbit() {
        return orbit;
    }

    private float canMoveOrbit(int dir) {

        float sunCenterX = sun.getX() + sun.getWidth() / 2;
        float sunCenterY = sun.getY() + sun.getHeight() / 2;

        float orbitCenterX = 0;
        float orbitCenterY = 0;

        switch (dir) {
            case 1:
                orbitCenterX = orbit.x;
                orbitCenterY = orbit.y + orbitHeight / 2;
                break;
            case 2:
                orbitCenterX = orbit.x + orbitWidth / 2;
                orbitCenterY = orbit.y;
                break;
            case 3:
                orbitCenterX = orbit.x;
                orbitCenterY = orbit.y - orbitHeight / 2;
                break;
            case 4:
                orbitCenterX = orbit.x - orbitWidth / 2;
                orbitCenterY = orbit.y;
                break;
        }


        float diffX = sunCenterX - orbitCenterX;
        float diffY = sunCenterY - orbitCenterY;

        float distance = (float) Math.sqrt(diffX * diffX + diffY * diffY);

        System.out.println("Distance: " + distance + " OrbitHeight: " + orbitHeight);

        return distance;
    }

    private boolean canMoveOrbitTest(int dir) {

        float sunCenterX = sun.getX() + sun.getWidth() / 2;
        float sunCenterY = sun.getY() + sun.getHeight() / 2;

        float distanceTop, distanceRight, distanceDown, distanceLeft;

        float orbitTopX = orbit.x;
        float orbitTopY = orbit.y + orbitHeight / 2;

        float orbitRightX = orbit.x + orbitWidth / 2;
        float orbitRightY = orbit.y;

        float orbitDownX = orbit.x;
        float orbitDownY = orbit.y - orbitHeight / 2;

        float orbitLeftX = orbit.x - orbitWidth / 2;
        float orbitLeftY = orbit.y;

        float diffTopX = sunCenterX - orbitTopX;
        float diffTopY = sunCenterY - orbitTopY;
        distanceTop = (float) Math.sqrt(diffTopX * diffTopX + diffTopY * diffTopY);

        float diffRightX = sunCenterX - orbitRightX;
        float diffRightY = sunCenterY - orbitRightY;
        distanceRight = (float) Math.sqrt(diffRightX * diffRightX + diffRightY * diffRightY);

        float diffDownX = sunCenterX - orbitDownX;
        float diffDownY = sunCenterY - orbitDownY;
        distanceDown = (float) Math.sqrt(diffDownX * diffDownX + diffDownY * diffDownY);

        float diffLeftX = sunCenterX - orbitLeftX;
        float diffLeftY = sunCenterY - orbitLeftY;
        distanceLeft = (float) Math.sqrt(diffLeftX * diffLeftX + diffLeftY * diffLeftY);

        float minDistance = 90;

        switch (dir) {
            case 1:
                return distanceTop > 45 && distanceLeft > minDistance && distanceRight > minDistance;
            case 2:
                return distanceRight > 45 && distanceTop > 45 && distanceDown > 45;
            case 3:
                return distanceDown > 45 && distanceLeft > 45 && distanceRight > 45;
            case 4:
                return distanceLeft > 45 && distanceTop > 45 && distanceDown > 45;
        }

        return false;
    }

    public Sprite getSun() {
        return sun;
    }

    public Circle getSunHitbox() {
        return sunHitbox;
    }

    public ArrayList<Element> getElements() {
        return elements;
    }
}
