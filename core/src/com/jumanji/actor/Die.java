package com.jumanji.actor;

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

public class Die extends Actor {
    //Creamos los atributos
    private static final float DIE_X = 1f;
    private static final float DIE_Y = 1f;
    private final float SPEED = -0.5f;
    private float statetime;

    //obtengo las imagenes de la animacion de muerte
    private final Animation<TextureRegion> dieAnimation;
    private final World world;

    private Body body_die;
    private Fixture die_fixture;

    //En el constructor debemos pasarle la animación previamente cargada de AssetMan y la posición
    //donde queramos que se dibuje el actor.
    public Die(World world, Animation<TextureRegion> dieSprite, Vector2 position) {
        this.dieAnimation = dieSprite;
        this.world = world;
        createBody(position);
        createFixture();
    }

    public void createBody(Vector2 position) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position);//la fisica lleva la misma posicion que el dibujo
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        this.body_die = this.world.createBody(bodyDef);
        this.body_die.setLinearVelocity(SPEED, 0);
    }

    public void createFixture() {
        PolygonShape boxShape = new PolygonShape();//fisica cuadrada
        boxShape.setAsBox((DIE_X - 0.2f) / 2, (DIE_Y) / 2);
        this.die_fixture = this.body_die.createFixture(boxShape, 0);
        boxShape.dispose();
    }

    public void detach() {
        this.body_die.destroyFixture(this.die_fixture);
        this.world.destroyBody(this.body_die);
    }

    public float getDeltaTime(){
        return this.statetime;
    }
    @Override
    //Dibuja el actor en pantalla en relacion a la posicion indicada
    public void draw(Batch batch, float parentAlpha) {
        setPosition(body_die.getPosition().x - 0.5f, body_die.getPosition().y - 0.5f);
        batch.draw(this.dieAnimation.getKeyFrame(statetime, true), getX(), getY(), 2.5f, 1.5f);
        this.statetime += Gdx.graphics.getDeltaTime();
    }
}
