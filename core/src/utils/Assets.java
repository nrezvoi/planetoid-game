package utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

/**
 * Created by Rezvoi on 21.12.2015.
 */
public class Assets {

    private static TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("pack.atlas"));

    //---------------Stage 1-------------------
    public static TextureRegion spaceBg;
    public static TextureRegion sunText, planetText;

    public static TextureRegion hero_push_left, hero_push_right, hero_push_up, hero_push_down;
    public static TextureRegion hero_idle_left, hero_idle_right;

    public static TextureRegion arrow_right;

    public static TextureRegion successBar;
    public static TextureRegion hudArrow;

    public static TextureRegion asteroid;

    public static TextureRegion e_cold, e_grav, e_heat, e_rock;
    public static TextureRegion e_cold_tiny, e_grav_tiny, e_heat_tiny, e_rock_tiny;
    public static TextureRegion small_bar, bar_mark;

    public static BitmapFont normalFont;

    public static Sound hit, jump, orbit, select, catched, element, error;

    public static Music stageOneMusic, menuMusic, stageTwoMusic;

    //------------End Stage 1------------------

    //---------------Stage 2-------------------

    public static TextureRegion planet_atmosphere, planet_big, planet_grass;
    public static TextureRegion titleArrow, title;

    public static TextureRegion e_slot, e_slot_arrow, e_slot_sel, e_list_arrow, cross, mix;

    public static TextureRegion animal, atmosphere, food, gas, hearth, hut, metal,
            mountains, ore, organism, plants, slime, tool, tree, water, creature;


    //------------End Stage 2------------------
    public static void loadAssets() {

        loadFonts();


        //LOAD STAGE 1----------------
        spaceBg = atlas.findRegion("space");
        sunText = atlas.findRegion("sun");
        planetText = atlas.findRegion("planet");
        hero_idle_left = atlas.findRegion("hero_idle_left");
        hero_idle_right = atlas.findRegion("hero_idle_right");
        hero_push_left = atlas.findRegion("hero_push_left");
        hero_push_right = atlas.findRegion("hero_push_right");
        hero_push_up = atlas.findRegion("hero_push_up");
        hero_push_down = atlas.findRegion("hero_push_down");
        arrow_right = atlas.findRegion("arrow_right");
        successBar = atlas.findRegion("success_bar");
        hudArrow = atlas.findRegion("hud_arrow");
        asteroid = atlas.findRegion("a_asteroid");
        e_cold = atlas.findRegion("e_cold");
        e_grav = atlas.findRegion("e_grav");
        e_heat = atlas.findRegion("e_heat");
        e_rock = atlas.findRegion("e_rock");
        e_cold_tiny = atlas.findRegion("e_cold_tiny");
        e_grav_tiny = atlas.findRegion("e_grav_tiny");
        e_heat_tiny = atlas.findRegion("e_heat_tiny");
        e_rock_tiny = atlas.findRegion("e_rock_tiny");
        small_bar = atlas.findRegion("small_bar");
        bar_mark = atlas.findRegion("bar_mark");

        //SFX

        hit = Gdx.audio.newSound(Gdx.files.internal("sfx/hit.wav"));
        jump = Gdx.audio.newSound(Gdx.files.internal("sfx/jump.wav"));
        orbit = Gdx.audio.newSound(Gdx.files.internal("sfx/orbit.wav"));
        select = Gdx.audio.newSound(Gdx.files.internal("sfx/select.wav"));
        catched = Gdx.audio.newSound(Gdx.files.internal("sfx/catch.wav"));
        element = Gdx.audio.newSound(Gdx.files.internal("sfx/element.wav"));
        error = Gdx.audio.newSound(Gdx.files.internal("sfx/error.wav"));


        /*----------------------------MUSIC BY WATERFLAME-----------------------------------*/
        //http://www.waterflamemusic.com/
        //MENU - Unicycle! Remix
        //StageOne - Electroman Adventures V2
        //StageTwo - -Dreamscape-
        /*----------------------------------------------------------------------------------*/

        stageOneMusic = Gdx.audio.newMusic(Gdx.files.internal("sfx/stageone.mp3"));
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sfx/menu.mp3"));
        stageTwoMusic = Gdx.audio.newMusic(Gdx.files.internal("sfx/stagetwo.mp3"));

        //END LOAD STAGE 1-----------

        //LOAD STAGE 2----------------

        planet_atmosphere = atlas.findRegion("planet_atmosphere");
        planet_big = atlas.findRegion("planet_big");
        planet_grass = atlas.findRegion("planet_grass");
        titleArrow = atlas.findRegion("arrow");
        title = atlas.findRegion("title");
        animal = atlas.findRegion("animal");
        atmosphere = atlas.findRegion("atmosphere");
        food = atlas.findRegion("food");
        gas = atlas.findRegion("gas");
        hearth = atlas.findRegion("hearth");
        hut = atlas.findRegion("hut");
        metal = atlas.findRegion("metal");
        mountains = atlas.findRegion("mountains");
        ore = atlas.findRegion("ore");
        organism = atlas.findRegion("organism");
        plants = atlas.findRegion("plants");
        slime = atlas.findRegion("slime");
        tool = atlas.findRegion("tool");
        tree = atlas.findRegion("tree");
        water = atlas.findRegion("water");
        e_slot = atlas.findRegion("e_slot");
        e_slot_arrow = atlas.findRegion("e_slot_arrow");
        e_slot_sel = atlas.findRegion("e_slot_sel");
        e_list_arrow = atlas.findRegion("e_list_arrow");
        cross = atlas.findRegion("cross");
        mix = atlas.findRegion("mix");
        creature = atlas.findRegion("f1");

        //END LOAD STAGE 2-----------


    }

    private static void loadFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.minFilter = Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.size = 8; //Gdx.graphics.getWidth() * 8 / LifeIsSpace.WIDTH;

        parameter.borderWidth = 1;
        parameter.borderColor = Color.BLACK;

        normalFont = generator.generateFont(parameter);


        generator.dispose();

    }

}
