package com.jumanji.actor;

import static com.jumanji.extra.Util.KOALA_FIXTURE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jumanji.screens.GameScreen;

public class Koala extends Actor {
    public float koala_width;
    public static float kola_height;
    private int next_level = 5;
    public static Vector2 posicion;
    private boolean nextLevel;

    //Estado del koala(vivo, muerto)
    private static boolean canJump = false;
    public static int state;
    public static final int STATE_NORMAL = 0;
    public static final int STATE_DIE = 1;
    private static final float JUM_SPEED = 6.8f;

    //Creamos el atributo con la animación de regiones de texturas
    private final Animation<TextureRegion> koalaAnimation;
    private final World world;
    private Fixture fixture;

    //En el constructor debemos pasarle la animación previamente cargada de AssetMan y la posición
    //donde queramos que se dibuje el actor.
    private float statetime;
    private Body body_koala;//es el cuerpo fisico del koala

    public Koala(World world, Animation<TextureRegion> animation, Vector2 position) {
        this.koalaAnimation = animation;
        this.world = world;
        statetime = 0f;
        state = STATE_NORMAL;
        koala_width = 0.6f;
        kola_height = 0.6f;
        posicion = position;
        createBody(position);
        createFixture();
    }

    public void createBody(Vector2 position) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position);//la fisica lleva la misma posicion que el dibujo
        bodyDef.type = BodyDef.BodyType.DynamicBody;//dinamico es el que se mueve y le afecta la fisica
        this.body_koala = this.world.createBody(bodyDef);
    }

    public void createFixture() {
        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox((koala_width - koala_width / 2.4f) / 2, kola_height / 2.1f);
        this.fixture = this.body_koala.createFixture(boxShape,0);
        //Asinga un nombre a la fisica para poder hacerle luego referencia a la hora de ralizar las colisiones
        this.fixture.setUserData(KOALA_FIXTURE);
        boxShape.dispose();
    }

    public void detach() {
        this.body_koala.destroyFixture(this.fixture);
        this.world.destroyBody(this.body_koala);
    }

    //determina si el koala puede saltar(a no ser que este en contacto con el suelo,
    // no podra volver a saltar para evitar que vuele)
    public void setCanJump(boolean valor) {
        canJump = valor;
    }

    public void setNextLevel(boolean nextLevel) {
        this.nextLevel = nextLevel;
    }

    @Override
    //Indica la accion que puede realizar el actor
    public void act(float delta) {
        //al pulsar con el raton/barra espaciadora en el pc, o tocar la pnatalla en un dispositivo, el koala saltara una altura determinada
        //si la puntucaión es igual a 5, el koala aumentara su tamanio(para ello es necesario eliminar y volver a crear la fisica)
        // e incrementará en 5 el numero para conseguir el siguiente nivel (NEXT_LEVEL= NEXT_LEVEL+5 =10)
        if ((Gdx.input.justTouched() || Gdx.input.isKeyJustPressed(62)) && state == STATE_NORMAL && canJump) {
            this.body_koala.setLinearVelocity(0, JUM_SPEED);
        }
    }

    @Override
    //Dibuja el actor en pantalla en relacion a la posicion indicada
    public void draw(Batch batch, float parentAlpha) {
        setPosition(body_koala.getPosition().x - (koala_width / 2), body_koala.getPosition().y - (kola_height / 2));
        batch.draw(this.koalaAnimation.getKeyFrame(statetime, true), getX(), getY(), koala_width, kola_height);
        if(state == STATE_NORMAL){
            statetime += Gdx.graphics.getDeltaTime(); //acumula el delta que le indicamos en AssetMan
        }
        if (GameScreen.scoreNumber == next_level && nextLevel) {
            koala_width += 0.05f;
            kola_height += 0.05f;
            next_level += 5;
            this.body_koala.destroyFixture(this.fixture);
            createFixture();
        }
    }
}
