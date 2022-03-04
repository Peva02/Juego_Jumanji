package com.jumanji.screens;

import static com.jumanji.extra.Util.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.jumanji.actor.Barrel;
import com.jumanji.actor.Koala;

/**
 * @author Eduardo Pérez Vargas
 */

public class GameScreen extends BaseScreen implements ContactListener {
    private final Stage stage;//Actores, aqui va a estar el pajaros y la tuberias, van a interactuar con el world(tienen fisicas)
    // objeto imagen que se va a llamar BackGround(en Stage)
    //El mundo gestiona todas las entidades físicas, la simulación dinámica
    private World world; //Van a tener la clase suelo y counter, el counter se hara un rectangulo que cuando pase sume puntuacion
    private Koala koala;
    private Body floor;
    private final Music musicbg;
    private final Sound explosionSound;
    protected float TIME_TO_SPAWN_BARREL = 1.35f;
    private float timeToCreateBarrel;
    private final Array<Barrel> arrayBarrel;

    //Camara y fuente del contador
    private OrthographicCamera fontCamera;
    private BitmapFont score;
    public static int scoreNumber;

    //Camara y fuente de los fps
    private BitmapFont fps;
    private OrthographicCamera fpsCamera;

    public GameScreen(Main mainGame) {
        super(mainGame);
        //FISICAS
        this.world = new World(new Vector2(0, -11f), true);//el true hace que cuando el elemento ya no se va a mover, deja de ejercer fuerza
        this.world.setContactListener(this);

        //Inicializamos el stage creando previamente nuestra viewport
        FillViewport fillViewport = new FillViewport(WORLD_WIDTH, WORLD_HEIGTH);// Esto es la camara
        this.stage = new Stage(fillViewport);
        this.stage.addActor(mainGame.assetManager.addBackground());
        this.musicbg = mainGame.assetManager.getMusicBg();
        this.explosionSound = mainGame.assetManager.getSoundImpacto();
        createBodyFloor();
        this.arrayBarrel = new Array<>();
        this.timeToCreateBarrel = 0f;
        prepareScore();
    }

    public void createBodyFloor() {
        BodyDef bodyDef = new BodyDef();
        //la fisica lleva la misma posicion que el dibujo,
        // como va ser un vector el cuerpo no necesita posicion porque se le asigan con los vectores
        bodyDef.type = BodyDef.BodyType.StaticBody;
        this.floor = this.world.createBody(bodyDef);
        createFixtureFloor();
    }

    public void createFixtureFloor() {
        EdgeShape boxShape = new EdgeShape();
        boxShape.set(0f, 1f, WORLD_WIDTH, 1f);
        Fixture fixture_floor = this.floor.createFixture(boxShape, 3);
        fixture_floor.setUserData(FLOOR_FIXTURE);
        boxShape.dispose();

    }

    public void addArrayBarrel(float delta) {
        if (Koala.state == Koala.STATE_NORMAL) {
            this.timeToCreateBarrel += delta;
            if (this.timeToCreateBarrel >= TIME_TO_SPAWN_BARREL) {
                this.timeToCreateBarrel -= TIME_TO_SPAWN_BARREL;
                Barrel barrel = new Barrel(this.world, mainGame.assetManager.getAguilaAnimation(), mainGame.assetManager.getExplosionAnimation(), mainGame.assetManager.getBarrel(), mainGame.assetManager.getEucalipto(), new Vector2(WORLD_WIDTH + (Barrel.getBARREL_X() / 2), 1.42f));
                arrayBarrel.add(barrel);
                this.stage.addActor(barrel);
            }
        }
    }

    public void removeBarrel() {
        for (Barrel barril : this.arrayBarrel) {
            if (!this.world.isLocked()) {
                if (barril.isOutOfScreen()) {
                    barril.detach();//eliminamos parte fisica
                    barril.remove();//eliminamos parte grafica
                    this.arrayBarrel.removeValue(barril, false);
                }
            }
        }
    }

    public void addKoala(World world, Animation<TextureRegion> animation, Vector2 vector) {
        this.koala = new Koala(world, animation, vector);
        this.stage.addActor(this.koala);
    }

    private void prepareScore() {
        //Cargamos la fuente y configuramos la escala (vamos probando el tamaño
        scoreNumber = 0;
        this.score = this.mainGame.assetManager.getFont();
        this.score.getData().scale(0.3f);

        //Creamos la cámara, y se le da el tamaño de la PANTALLA (EN PIXELES) y luego se actualiza
        this.fontCamera = new OrthographicCamera();
        this.fontCamera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        this.fontCamera.update();

        //Camara para los fps
        this.fps = this.mainGame.assetManager.getFpsFont();
        this.fps.getData().scale(0.1f);

        this.fpsCamera = new OrthographicCamera();
        this.fpsCamera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        this.fpsCamera.update();


    }

    @Override
    public void show() {
        addKoala(this.world, mainGame.assetManager.getKoalaAnimation(), new Vector2(WORLD_WIDTH / 2.5f, 1.5f));
        this.musicbg.setLooping(true);
        this.musicbg.setVolume(0.20f);
        this.musicbg.play();
    }

    //Elimina actores al abandonar la escena
    @Override
    public void hide() {
        this.koala.detach();
        this.koala.remove();
        this.musicbg.stop();
    }

    @Override
    public void render(float delta) {
        addArrayBarrel(delta);
        this.stage.act();
        this.world.step(delta, 6, 2);
        this.stage.draw();
        removeBarrel();

        this.stage.getBatch().setProjectionMatrix(this.fontCamera.combined);
        this.stage.getBatch().begin();
        this.score.draw(this.stage.getBatch(), String.valueOf(scoreNumber), SCREEN_WIDTH / 2.3f, SCREEN_HEIGHT - (SCREEN_HEIGHT / 5f));
        this.stage.getBatch().end();

        this.stage.getBatch().setProjectionMatrix(this.fpsCamera.combined);
        this.stage.getBatch().begin();
        this.fps.draw(this.stage.getBatch(), "fps:" + Gdx.graphics.getFramesPerSecond(), SCREEN_WIDTH / 10f, SCREEN_HEIGHT - 10);
        this.stage.getBatch().end();
    }

    @Override
    public void dispose() {
        this.stage.dispose();
        this.score.dispose();
        this.fps.dispose();
        this.world.destroyBody(floor);
        this.world.dispose();
    }

    //////////////
    //Colisiones//
    //////////////
    //Comprueba si un objetoA choca con un obejetoB o viceversa, porque ralmente, no se puede saber
    // quien choca con quien primero
    public boolean areCollide(Contact contact, Object objectA, Object objectB) {
        return (contact.getFixtureA().getUserData().equals(objectA) && contact.getFixtureB().getUserData().equals(objectB)
                || contact.getFixtureA().getUserData().equals(objectB) && contact.getFixtureB().getUserData().equals(objectA));
    }

    @Override
    public void beginContact(Contact contact) {
        if (areCollide(contact, KOALA_FIXTURE, EUCALIPTO_FIXTURE)) {
            scoreNumber++;
            this.koala.setNextLevel(false);

        } else if (areCollide(contact, KOALA_FIXTURE, FLOOR_FIXTURE)) {
            this.koala.setCanJump(true);
        } else if (areCollide(contact, KOALA_FIXTURE, BARREL_FIXTURE)) {
            this.world =new World(new Vector2(0f, 0f), true);
            this.musicbg.stop();
            this.explosionSound.play();
            Koala.state = 1;
            Barrel.STATE_NORMAL = false;
            for (Barrel barrel : this.arrayBarrel) {
                barrel.stopBarrel();
            }
            this.stage.addAction(Actions.sequence(
                    Actions.delay(1.5f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            mainGame.setScreen(mainGame.overScreen);
                        }
                    })
            ));

        }
    }

    @Override
    public void endContact(Contact contact) {
        if (areCollide(contact, KOALA_FIXTURE, FLOOR_FIXTURE)) {
            this.koala.setCanJump(false);
        }
        if (areCollide(contact, KOALA_FIXTURE, EUCALIPTO_FIXTURE)) {
            this.koala.setNextLevel(true);
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}