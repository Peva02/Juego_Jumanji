package com.jumanji.actor;

import static com.jumanji.extra.Util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Barrel extends Actor {
    //Creamos los atributos del barril
    private static final float BARREL_WIDTH = 0.8f;
    private static final float BARREL_HEIGHT = 0.7f;
    //determina el estado del barril, si el koala se choca, el estado cambia y se produce
    // la animacion de explosion
    public static boolean STATE_NORMAL;

    //obtengo las imagenes tanto del barril como del contador(eucalipto)
    private final TextureRegion barrel_img;
    private final TextureRegion eucalipto;

    //Obtengo la animacion de la explosion y declaro un statetime para la animacion
    private final Animation<TextureRegion> explosion;
    private final Animation<TextureRegion> aguila;

    private float statetime;
    private float statetimeAguila = 0;


    private final World world;
    //En el constructor debemos pasarle la animación previamente cargada de AssetMan y la posición
    //donde queramos que se dibuje el actor.
    //Creao los cuerpos y las texturas del barril y el contador
    private Body body_barrel;
    private Body counter;
    private Fixture barrel_fixture;
    private Fixture counter_fixture;

    //Numero aleatorio que genera la posicion del contador, y el numero de variar su posicion
    private final int positionContador = MathUtils.random(0, 5);
    private final float randomPosition = MathUtils.random(0.3f, 0.8f);

    //Como va a crearse a la vez el barril y el contador, necesito pasarle al constructor, tanto el barril,
    //como el contador y el efecto de la explosion
    public Barrel(World world, Animation<TextureRegion> aguila, Animation<TextureRegion> explosion, TextureRegion barerlSprite, TextureRegion eucalipto, Vector2 position) {
        this.barrel_img = barerlSprite;
        this.eucalipto = eucalipto;
        this.world = world;
        this.explosion = explosion;
        this.aguila = aguila;
        STATE_NORMAL = true;
        createBody(position);
        createFixture();
    }

    public void createBody(Vector2 position) {
        final float SPEED = -1.5f;

        BodyDef bodyDef = new BodyDef();
        //Si la positionContados es menor o igual a 3, se generará el contador(Eucalipto),
        // en la posicion alta, de lo contrario, tendrá que saltar para poder cogerlo y puntuar
        if (positionContador <= 3) {
            bodyDef.position.set(position);//la fisica lleva la misma posicion que el dibujo
            bodyDef.type = BodyDef.BodyType.KinematicBody;//tiene masa cero y velocidad distinta de cero establecida por el usuario,
            this.body_barrel = this.world.createBody(bodyDef);
            this.body_barrel.setLinearVelocity(SPEED, 0);

            BodyDef bodyCounter = new BodyDef();
            //La posicion del contador sera la posicion maxima del coala en Y, para que cuando arriba salga un obstaculo,
            // el koala pueda pasarlo por debajo sin probelma
            bodyCounter.position.set(position.x, position.y + (Koala.kola_height * 2) + randomPosition);
            bodyCounter.type = BodyDef.BodyType.KinematicBody;
            this.counter = this.world.createBody(bodyCounter);
        } else {
            bodyDef.position.set(position.x, Koala.kola_height + 1.5f + randomPosition);//la fisica lleva la misma posicion que el dibujo
            bodyDef.type = BodyDef.BodyType.KinematicBody;//tiene masa cero y velocidad distinta de cero establecida por el usuario,
            this.body_barrel = this.world.createBody(bodyDef);
            this.body_barrel.setLinearVelocity(SPEED, 0);

            BodyDef bodyCounter = new BodyDef();
            //La posicion del contador sera la posicion maxima del coala en Y, para que cuando arriba salgaun obstaculo,
            // el koala pueda pasarlo por debajo sin probelma
            bodyCounter.position.set(position);
            bodyCounter.type = BodyDef.BodyType.KinematicBody;
            this.counter = this.world.createBody(bodyCounter);
        }
        this.counter.setLinearVelocity(SPEED, 0);

    }

    public void createFixture() {
        //Crea la fisica cuadrada
        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox((BARREL_WIDTH - 0.2f) / 2, (BARREL_HEIGHT) / 2);
        this.barrel_fixture = this.body_barrel.createFixture(boxShape, 0);
        //Asinga un nombre a la fisica para poder hacerle luego referncia a la ora de ralizar las colisiones
        this.barrel_fixture.setUserData(BARREL_FIXTURE);

        boxShape.setAsBox((BARREL_WIDTH - 0.1f) / 2, BARREL_HEIGHT / 3f);
        this.counter_fixture = this.counter.createFixture(boxShape, 3);
        //Activamos la fisica del eucalipto como un sensor, es decir, si nos chocamos con el, podremos atravesarlo
        this.counter_fixture.setSensor(true);
        //Asinga un nombre a la fisica para poder hacerle luego referncia a la ora de ralizar las colisiones
        this.counter_fixture.setUserData(EUCALIPTO_FIXTURE);

        //al termianr debemos eliminar los elementos que no utilicemos
        boxShape.dispose();
    }

    public static float getBARREL_X() {
        return BARREL_WIDTH;
    }

    //Controla si el barril ha desaparecido de la pantalla por completo
    public boolean isOutOfScreen() {
        return this.body_barrel.getPosition().x <= -2f;
    }

    //para el movimiento de los barriles
    public void stopBarrel() {
        this.body_barrel.setLinearVelocity(0, 0);
        this.counter.setLinearVelocity(0, 0);
    }

    //Destruye las fisicas y los cuerpos cuando no los necesitemos
    public void detach() {
        this.body_barrel.destroyFixture(this.barrel_fixture);
        this.world.destroyBody(this.body_barrel);
        this.counter.destroyFixture(this.counter_fixture);
        this.world.destroyBody(this.counter);
    }

    @Override
    //Dibuja el actor en pantalla en relacion a la posicion indicada
    public void draw(Batch batch, float parentAlpha) {
        //Si el estado del barril es normal, pintara por pantalla el barril con el contador;
        //de lo contrario, mostrará la animacion de explosion en todos los barriles
        if (STATE_NORMAL) {
            //Si la positionContados es menor o igual a 3, se generará el contador(Eucalipto),
            // en la posicion alta, de lo contrario, tendrá que saltar para poder cogerlo y puntuar
            if (positionContador <= 3) {
                setPosition(body_barrel.getPosition().x - 0.4f, body_barrel.getPosition().y - 0.4f);
                batch.draw(this.barrel_img, getX(), getY(), 0.8f, 0.8f);

                setPosition(counter.getPosition().x - 0.4f, counter.getPosition().y - 0.25f);
                batch.draw(this.eucalipto, getX(), getY(), 0.8f, 0.5f);
            } else {
                setPosition(body_barrel.getPosition().x - 0.4f, body_barrel.getPosition().y - 0.4f);
                batch.draw(this.aguila.getKeyFrame(statetimeAguila, true), getX(), getY(), 0.8f, 0.8f);

                setPosition(counter.getPosition().x - 0.4f, counter.getPosition().y - 0.25f);
                batch.draw(this.eucalipto, getX(), getY(), 0.8f, 0.5f);
                statetimeAguila += Gdx.graphics.getDeltaTime(); //acumula el delta que le indicamos en AssetMan

            }

        } else {
            setPosition(body_barrel.getPosition().x - 0.4f, body_barrel.getPosition().y - 0.4f);
            batch.draw(this.explosion.getKeyFrame(statetime, false), getX(), getY(), 1f, 1);
            statetime += Gdx.graphics.getDeltaTime(); //acumula el delta que le indicamos en AssetMan
        }
    }
}