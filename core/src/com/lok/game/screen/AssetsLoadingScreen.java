package com.lok.game.screen;

import java.util.EnumSet;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.TimeUtils;
import com.lok.game.LegendOfKaminalyuyu;
import com.lok.game.assets.loader.AnimationLoader.AnimationParameter;
import com.lok.game.assets.loader.EntityConfigurationLoader.EntityConfigurationParameter;
import com.lok.game.conversation.Conversation;
import com.lok.game.conversation.Conversation.ConversationID;
import com.lok.game.ecs.EntityConfiguration;
import com.lok.game.ecs.EntityEngine.EntityID;
import com.lok.game.map.Map;
import com.lok.game.map.MapManager.MapID;
import com.lok.game.ui.Animation;
import com.lok.game.ui.Animation.AnimationID;
import com.lok.game.ui.AssetsLoadingUI;

public class AssetsLoadingScreen extends Screen<AssetsLoadingUI> {
    private final static String	TAG = AssetsLoadingScreen.class.getSimpleName();

    private long		startTime;

    public AssetsLoadingScreen(LegendOfKaminalyuyu game, AssetManager assetManager, Skin uiSkin) {
	super(game, assetManager, AssetsLoadingUI.class, uiSkin);
    }

    @Override
    public void show() {
	startTime = TimeUtils.millis();
	Gdx.app.debug(TAG, "Start loading of assets");

	// load sounds
	assetManager.load("sounds/music/town.ogg", Music.class);
	assetManager.load("sounds/music/demon_lair_01.ogg", Music.class);
	assetManager.load("sounds/effects/menu_selection.wav", Sound.class);
	assetManager.load("sounds/effects/teleport.wav", Sound.class);

	// load texture atlas
	assetManager.load("effects/effects.atlas", TextureAtlas.class);
	assetManager.load("units/units.atlas", TextureAtlas.class);
	assetManager.load("lights/lights.atlas", TextureAtlas.class);

	// load animations
	final AnimationParameter aniParam = new AnimationParameter("json/animations.json");
	for (AnimationID aniID : AnimationID.values()) {
	    assetManager.load(aniID.name(), Animation.class, aniParam);
	}

	// load maps
	for (MapID mapID : MapID.values()) {
	    assetManager.load(mapID.name(), Map.class);
	}

	// load conversations
	for (ConversationID convID : ConversationID.values()) {
	    assetManager.load(convID.name(), Conversation.class);
	}

	// load entity configurations
	EntityConfigurationParameter entityParam = new EntityConfigurationParameter("json/player.json");
	assetManager.load(EntityID.PLAYER.name(), EntityConfiguration.class, entityParam);
	entityParam = new EntityConfigurationParameter("json/townfolk.json");
	assetManager.load(EntityID.ELDER.name(), EntityConfiguration.class, entityParam);
	assetManager.load(EntityID.SHAMAN.name(), EntityConfiguration.class, entityParam);
	assetManager.load(EntityID.BLACKSMITH.name(), EntityConfiguration.class, entityParam);
	assetManager.load(EntityID.PORTAL.name(), EntityConfiguration.class, entityParam);
	final EnumSet<EntityID> remainingEntities = EnumSet.allOf(EntityID.class);
	remainingEntities.remove(EntityID.PLAYER);
	remainingEntities.remove(EntityID.ELDER);
	remainingEntities.remove(EntityID.SHAMAN);
	remainingEntities.remove(EntityID.BLACKSMITH);
	remainingEntities.remove(EntityID.PORTAL);
	entityParam = new EntityConfigurationParameter("json/monsters.json");
	for (EntityID entityID : remainingEntities) {
	    assetManager.load(entityID.name(), EntityConfiguration.class, entityParam);
	}
    }

    @Override
    public void onUpdate(float fixedPhysicsStep) {
	if (assetManager.update()) {
	    Gdx.app.debug(TAG, "Finished loading of assets in " + TimeUtils.timeSinceMillis(startTime) / 1000.0f + " seconds");
	    game.setScreen(TownScreen.class);
	}

    }

    @Override
    public void hide() {
	super.hide();
	dispose();
    }

    @Override
    public void onUIEvent(Actor triggerActor, UIEvent event) {
	// not needed
    }

    @Override
    public void onSave(Json json, Preferences preferences) {
	// not needed
    }

    @Override
    public void onLoad(Json json, Preferences preferences) {
	// not needed
    }

}
