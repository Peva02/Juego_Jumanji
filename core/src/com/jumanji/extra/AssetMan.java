package com.jumanji.extra;

import static com.jumanji.extra.Util.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

//Esta clase será la encargada de obtener los recursos de la carpeta assest de Android para poder utilizarlos en nuestro juego
public class AssetMan{
    private final AssetManager assetManager;
    private final TextureAtlas textureAtlas;
    private final TextureAtlas explosionAtlas;
    private final TextureAtlas aguila;

    public AssetMan() {
        //inicialzamos el objeto assetManager que sera el encargado de cargar los diferentes recursos
        this.assetManager = new AssetManager();
        assetManager.load(ATLAS_MAP, TextureAtlas.class);
        assetManager.load(AGUILA_MAP, TextureAtlas.class);
        assetManager.load(EXPLOSION_MAP, TextureAtlas.class);
        assetManager.load(MUSIC_BG, Music.class);//Music para canciones mas largas, y Sound(10 segundos) para los sonidos
        assetManager.load(MUSIC_LOBBY, Music.class);
        assetManager.load(MUSIC_GAMEOVER, Music.class);
        assetManager.load(SOUND_IMPACTO, Sound.class);
        assetManager.finishLoading();

        //Cargamos los arlas dedonde sacaremos los sprites necesarios
        textureAtlas = assetManager.get(ATLAS_MAP);
        explosionAtlas = assetManager.get(EXPLOSION_MAP);
        aguila = assetManager.get(AGUILA_MAP);
    }

    //Creamos un metodo que devuelva la parte de la imagen que corresponde al fondo.
    public Image addBackground() {
        Image background;
        background = new Image(getBackground());
        background.setPosition(0, 0);
        background.setSize(WORLD_WIDTH, WORLD_HEIGTH);//Tienes que coger las medidas del mundo, no de la pantalla
        return background;
    }

    public TextureRegion getBackground() {
        return this.textureAtlas.findRegion(ATLAS_BACKGROUND);
    }

    public TextureRegion getGameOver() {
        return this.textureAtlas.findRegion(ATLAS_GAME_OVER);
    }

    public TextureRegion getTaptoPlay() {
        return this.textureAtlas.findRegion(ATLAS_GET_READY);
    }

    public TextureRegion getBarrel() {
        return this.textureAtlas.findRegion(ATLAS_BARREL);
    }
    /*public Animation<TextureRegion> getBarrel() {
        return new Animation<TextureRegion>(1f, //Tiempo que acumula el delta
                textureAtlas.findRegion(ATLAS_BARREL)

        );
    }*/
    public TextureRegion getEucalipto() {
        return this.textureAtlas.findRegion(ATLAS_EUCALIPTO);
    }

    //Animation<TextureRegion>: Devuelve una lista de objetos, que representa una animacion
    public Animation<TextureRegion> getKoalaAnimation() {
        return new Animation<TextureRegion>(
                //Tiempo que acumula el delta(1/frames)
                0.25f,
                textureAtlas.findRegion("koala_1"),
                textureAtlas.findRegion("koala_2"),
                textureAtlas.findRegion("koala_3"),
                textureAtlas.findRegion("koala_4")
                //tantos findRegion por foto que tengamos
        );
    }

    public Animation<TextureRegion> getDieAnimation() {
        return new Animation<TextureRegion>(
                //Tiempo que acumula el delta(podemos regularlo según la velocidad que queramos de la animacion)
                0.45f,
                textureAtlas.findRegion("die_1"),
                textureAtlas.findRegion("die_2")
        );
    }

    public Animation<TextureRegion> getExplosionAnimation() {
        return new Animation<TextureRegion>(0.05f, //Tiempo que acumula el delta
                explosionAtlas.findRegion("exp1"),
                explosionAtlas.findRegion("exp2"),
                explosionAtlas.findRegion("exp3"),
                explosionAtlas.findRegion("exp4"),
                explosionAtlas.findRegion("exp5"),
                explosionAtlas.findRegion("exp6"),
                explosionAtlas.findRegion("exp7"),
                explosionAtlas.findRegion("exp8"),
                explosionAtlas.findRegion("exp9"),
                explosionAtlas.findRegion("exp10"),
                explosionAtlas.findRegion("exp11"),
                explosionAtlas.findRegion("exp12"),
                explosionAtlas.findRegion("exp13"),
                explosionAtlas.findRegion("exp14"),
                explosionAtlas.findRegion("exp15"),
                explosionAtlas.findRegion("exp16"),
                explosionAtlas.findRegion("exp17"),
                explosionAtlas.findRegion("exp18"),
                explosionAtlas.findRegion("exp19")
                //tantos findRegion por foto que tengas
        );
    }

    public Animation<TextureRegion> getAguilaAnimation() {
        return new Animation<TextureRegion>(
                //Tiempo que acumula el delta(1/frames)
                0.05f,
                aguila.findRegion("0"),
                aguila.findRegion("1"),
                aguila.findRegion("2"),
                aguila.findRegion("3"),
                aguila.findRegion("4"),
                aguila.findRegion("5"),
                aguila.findRegion("6"),
                aguila.findRegion("7"),
                aguila.findRegion("8"),
                aguila.findRegion("9"),
                aguila.findRegion("10"),
                aguila.findRegion("11"),
                aguila.findRegion("12"),
                aguila.findRegion("13"),
                aguila.findRegion("14"),
                aguila.findRegion("15"),
                aguila.findRegion("16"),
                aguila.findRegion("17"),
                aguila.findRegion("18"),
                aguila.findRegion("19"),
                aguila.findRegion("20")
                //tantos findRegion por foto que tengamos
        );
    }


    //Cargamos los archivos de musica y sonido(sonido < 10 segundos)
    public Music getMusicBg() {
        return assetManager.get(MUSIC_BG);
    }

    public Music getMusicLobby() {
        return assetManager.get(MUSIC_LOBBY);
    }

    public Music getMusicGameOver() {
        return assetManager.get(MUSIC_GAMEOVER);
    }

    public Sound getSoundImpacto() {
        return assetManager.get(SOUND_IMPACTO);
    }


    //Cargamos el atlas de las fuentes(Marcador y fps)
    public BitmapFont getFont() {
        return new BitmapFont(Gdx.files.internal(MARCADOR_FNT), Gdx.files.internal(MARCADOR_PNG), false);
    }

    public BitmapFont getFpsFont() {
        return new BitmapFont(Gdx.files.internal(FPS_FNT), Gdx.files.internal(FPS_PNG), false);
    }

}
