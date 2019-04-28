package lando.systems.ld44.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ShaderProgramLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import lando.systems.ld44.entities.AnimationGameEntity;

public class Assets implements Disposable {

    private final AssetDescriptor<TextureAtlas> atlasAsset = new AssetDescriptor<TextureAtlas>("images/sprites.atlas", TextureAtlas.class);
    private final AssetDescriptor<Texture> titleTextureAsset = new AssetDescriptor<Texture>("images/title.png", Texture.class);
    private final AssetDescriptor<Texture> arcadeTextureAsset = new AssetDescriptor<Texture>("images/arcade.png", Texture.class);
    private final AssetDescriptor<Texture> pixelTextureAsset = new AssetDescriptor<Texture>("images/pixel.png", Texture.class);

    private final AssetDescriptor<BitmapFont> pixelFont16Asset = new AssetDescriptor<BitmapFont>("fonts/emulogic-16pt.fnt", BitmapFont.class);

    private final ShaderProgramLoader.ShaderProgramParameter defaultVertParam = new ShaderProgramLoader.ShaderProgramParameter() {{ vertexFile = "shaders/default.vert"; }};

    public enum Loading { SYNC, ASYNC }

    public SpriteBatch batch;
    public ShapeRenderer shapes;
    public GlyphLayout layout;

    public AssetManager mgr;

    public TextureAtlas atlas;
    public Texture titleTexture;
    public Texture arcadeTexture;
    public Texture pixelTexture;

    public TextureRegion testTexture;
    public TextureRegion whitePixel;
    public TextureRegion whiteCircle;

    public TextureRegion player;
    public Animation<TextureRegion> playerShootAnimation;
    public Animation<TextureRegion> playerOpenAnimation;
    public Animation<TextureRegion> pennyAnimation;
    public Animation<TextureRegion> nickelAnimation;
    public Animation<TextureRegion> dimeAnimation;
    public Animation<TextureRegion> quarterAnimation;
    public Animation<TextureRegion> pennyPickupAnimation;
    public Animation<TextureRegion> nickelPickupAnimation;
    public Animation<TextureRegion> dimePickupAnimation;
    public Animation<TextureRegion> quarterPickupAnimation;
    public Animation<TextureRegion> springAnimationUp;
    public Animation<TextureRegion> springAnimationDown;
    public Animation<TextureRegion> springAnimationLeft;
    public Animation<TextureRegion> springAnimationRight;
    public Animation<TextureRegion> tackAnimationUp;
    public Animation<TextureRegion> tackAnimationDown;
    public Animation<TextureRegion> tackAnimationLeft;
    public Animation<TextureRegion> tackAnimationRight;

    public NinePatch ninePatch;

    public BitmapFont font;
    public BitmapFont fontPixel16;

    public boolean initialized;

    public Array<ShaderProgram> randomTransitions;
    public ShaderProgram blindsShader;
    public ShaderProgram fadeShader;
    public ShaderProgram radialShader;
    public ShaderProgram doomShader;
    public ShaderProgram pizelizeShader;
    public ShaderProgram doorwayShader;
    public ShaderProgram crosshatchShader;
    public ShaderProgram rippleShader;
    public ShaderProgram heartShader;

    public Assets() {
        this(Loading.SYNC);
    }

    public Assets(Loading loading) {
        // Let us write shitty shader programs
        ShaderProgram.pedantic = false;

        batch = new SpriteBatch();
        shapes = new ShapeRenderer();
        layout = new GlyphLayout();

        initialized = false;

        mgr = new AssetManager();
        mgr.load(atlasAsset);
        mgr.load(titleTextureAsset);
        mgr.load(arcadeTextureAsset);
        mgr.load(pixelTextureAsset);
        mgr.load(pixelFont16Asset);

        if (loading == Loading.SYNC) {
            mgr.finishLoading();
            updateLoading();
        }
    }

    public float updateLoading() {
        if (!mgr.update()) return mgr.getProgress();
        if (initialized) return 1f;
        initialized = true;

        // Cache TextureRegions from TextureAtlas in fields for quicker access
        atlas = mgr.get(atlasAsset);
        testTexture = atlas.findRegion("dogsuit");
        whitePixel = atlas.findRegion("white-pixel");
        whiteCircle = atlas.findRegion("white-circle");

        player = atlas.findRegion("purse_image");

        Array playerShoot = atlas.findRegions("purse_spit");
        playerShootAnimation = new Animation<TextureRegion>(0.3f, playerShoot, Animation.PlayMode.NORMAL);

        Array openPurse = atlas.findRegions("purse_open");
        playerOpenAnimation = new Animation<TextureRegion>(0.1f, openPurse, Animation.PlayMode.NORMAL);

        Array pennies = atlas.findRegions("penny_walk");
        Array nickels = atlas.findRegions("nickel_walk");
        Array dimes = atlas.findRegions("dime_walk");
        Array quarters = atlas.findRegions("quarter_walk");
        pennyAnimation = new Animation<TextureRegion>(0.1f, pennies, Animation.PlayMode.LOOP);
        nickelAnimation = new Animation<TextureRegion>(0.1f, nickels, Animation.PlayMode.LOOP);
        dimeAnimation = new Animation<TextureRegion>(0.1f, dimes, Animation.PlayMode.LOOP);
        quarterAnimation = new Animation<TextureRegion>(0.1f, quarters, Animation.PlayMode.LOOP);
        Array pennyPickup = atlas.findRegions("pickup-penny");
        Array nickelPickup = atlas.findRegions("pickup-nickel");
        Array dimePickup = atlas.findRegions("pickup-dime");
        Array quarterPickup = atlas.findRegions("pickup-quarter");
        pennyPickupAnimation = new Animation<TextureRegion>(0.075f, pennyPickup, Animation.PlayMode.LOOP_PINGPONG);
        nickelPickupAnimation = new Animation<TextureRegion>(0.075f, nickelPickup, Animation.PlayMode.LOOP_PINGPONG);
        dimePickupAnimation = new Animation<TextureRegion>(0.075f, dimePickup, Animation.PlayMode.LOOP_PINGPONG);
        quarterPickupAnimation = new Animation<TextureRegion>(0.075f, quarterPickup, Animation.PlayMode.LOOP_PINGPONG);

        Array springUp    = atlas.findRegions("spring-up");
        Array springDown  = atlas.findRegions("spring-down");
        Array springLeft  = atlas.findRegions("spring-left");
        Array springRight = atlas.findRegions("spring-right");
        springAnimationUp    = new Animation<TextureRegion>(0.075f, springUp,    Animation.PlayMode.NORMAL);
        springAnimationDown  = new Animation<TextureRegion>(0.075f, springDown,  Animation.PlayMode.NORMAL);
        springAnimationLeft  = new Animation<TextureRegion>(0.075f, springLeft,  Animation.PlayMode.NORMAL);
        springAnimationRight = new Animation<TextureRegion>(0.075f, springRight, Animation.PlayMode.NORMAL);

        Array tackUp    = atlas.findRegions("tack-up");
        Array tackDown  = atlas.findRegions("tack-down");
        Array tackLeft  = atlas.findRegions("tack-left");
        Array tackRight = atlas.findRegions("tack-right");
        tackAnimationUp    = new Animation<TextureRegion>(0.075f, tackUp,    Animation.PlayMode.NORMAL);
        tackAnimationDown  = new Animation<TextureRegion>(0.075f, tackDown,  Animation.PlayMode.NORMAL);
        tackAnimationLeft  = new Animation<TextureRegion>(0.075f, tackLeft,  Animation.PlayMode.NORMAL);
        tackAnimationRight = new Animation<TextureRegion>(0.075f, tackRight, Animation.PlayMode.NORMAL);

        titleTexture = mgr.get(titleTextureAsset);
        arcadeTexture = mgr.get(arcadeTextureAsset);
        arcadeTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        pixelTexture = mgr.get(pixelTextureAsset);

        ninePatch = new NinePatch(atlas.findRegion("ninepatch-screws"), 6, 6, 6, 6);

        fontPixel16 = mgr.get(pixelFont16Asset);
        font = fontPixel16;

        randomTransitions = new Array<ShaderProgram>();
        blindsShader = loadShader("shaders/default.vert", "shaders/blinds.frag");
        fadeShader = loadShader("shaders/default.vert", "shaders/dissolve.frag");
        radialShader = loadShader("shaders/default.vert", "shaders/radial.frag");
        doomShader = loadShader("shaders/default.vert", "shaders/doomdrip.frag");
        pizelizeShader = loadShader("shaders/default.vert", "shaders/pixelize.frag");
        doorwayShader = loadShader("shaders/default.vert", "shaders/doorway.frag");
        crosshatchShader = loadShader("shaders/default.vert", "shaders/crosshatch.frag");
        rippleShader = loadShader("shaders/default.vert", "shaders/ripple.frag");
        heartShader = loadShader("shaders/default.vert", "shaders/heart.frag");

//        randomTransitions.add(blindsShader);
        randomTransitions.add(fadeShader);
        randomTransitions.add(radialShader);
        randomTransitions.add(rippleShader);
//        randomTransitions.add(pizelizeShader);

        return 1f;
    }

    @Override
    public void dispose() {
        mgr.clear();
        font.dispose();
        shapes.dispose();
        batch.dispose();
    }

    private static ShaderProgram loadShader(String vertSourcePath, String fragSourcePath) {
        ShaderProgram.pedantic = false;
        ShaderProgram shaderProgram = new ShaderProgram(
                Gdx.files.internal(vertSourcePath),
                Gdx.files.internal(fragSourcePath));

        if (!shaderProgram.isCompiled()) {
            Gdx.app.error("LoadShader", "compilation failed:\n" + shaderProgram.getLog());
            throw new GdxRuntimeException("LoadShader: compilation failed:\n" + shaderProgram.getLog());
        } else {
            Gdx.app.debug("LoadShader", "ShaderProgram compilation log:\n" + shaderProgram.getLog());
        }

        return shaderProgram;
    }

}
