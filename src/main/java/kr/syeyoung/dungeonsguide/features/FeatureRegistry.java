/*
 *     Dungeons Guide - The most intelligent Hypixel Skyblock Dungeons Mod
 *     Copyright (C) 2021  cyoung06
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package kr.syeyoung.dungeonsguide.features;

import kr.syeyoung.dungeonsguide.config.types.AColor;
import kr.syeyoung.dungeonsguide.features.impl.advanced.FeatureDebuggableMap;
import kr.syeyoung.dungeonsguide.features.impl.advanced.FeatureRoomCoordDisplay;
import kr.syeyoung.dungeonsguide.features.impl.advanced.FeatureRoomDebugInfo;
import kr.syeyoung.dungeonsguide.features.impl.boss.*;
import kr.syeyoung.dungeonsguide.features.impl.boss.terminal.FeatureSimonSaysSolver;
import kr.syeyoung.dungeonsguide.features.impl.boss.terminal.FeatureTerminalSolvers;
import kr.syeyoung.dungeonsguide.features.impl.cosmetics.FeatureNicknameColor;
import kr.syeyoung.dungeonsguide.features.impl.cosmetics.FeatureNicknamePrefix;
import kr.syeyoung.dungeonsguide.features.impl.dungeon.*;
import kr.syeyoung.dungeonsguide.features.impl.etc.*;
import kr.syeyoung.dungeonsguide.features.impl.etc.ability.FeatureAbilityCooldown;
import kr.syeyoung.dungeonsguide.features.impl.party.APIKey;
import kr.syeyoung.dungeonsguide.features.impl.party.FeatureGoodParties;
import kr.syeyoung.dungeonsguide.features.impl.party.playerpreview.FeatureViewPlayerOnJoin;
import kr.syeyoung.dungeonsguide.features.impl.secret.FeatureActions;
import kr.syeyoung.dungeonsguide.features.impl.secret.FeatureFreezePathfind;
import kr.syeyoung.dungeonsguide.features.impl.secret.FeatureMechanicBrowse;
import kr.syeyoung.dungeonsguide.features.impl.secret.FeatureSoulRoomWarning;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeatureRegistry {
    @Getter
    private static final List<AbstractFeature> featureList = new ArrayList<AbstractFeature>();
    private static final Map<String, AbstractFeature> featureByKey = new HashMap<String, AbstractFeature>();
    @Getter
    private static final Map<String, List<AbstractFeature>> featuresByCategory = new HashMap<String, List<AbstractFeature>>();

    public static AbstractFeature getFeatureByKey(String key) {
        return featureByKey.get(key);
    }

    public static <T extends AbstractFeature> T register(T abstractFeature) {
        featureList.add(abstractFeature);
        featureByKey.put(abstractFeature.getKey(), abstractFeature);
        List<AbstractFeature> features = featuresByCategory.get(abstractFeature.getCategory());
        if (features == null)
            features = new ArrayList<AbstractFeature>();
        features.add(abstractFeature);
        featuresByCategory.put(abstractFeature.getCategory(), features);

        return abstractFeature;
    }
    public static final SimpleFeature DEBUG = register(new SimpleFeature("hidden", "Debug", "Toggles debug mode", "debug", false));

    public static final SimpleFeature ADVANCED_ROOMEDIT = register(new SimpleFeature("Advanced", "Room Edit", "Allow editing dungeon rooms\n\nWarning: using this feature can break or freeze your Minecraft\nThis is only for advanced users only", "advanced.roomedit", false));
    public static final FeatureRoomDebugInfo ADVANCED_DEBUG_ROOM = register(new FeatureRoomDebugInfo());
    public static final FeatureDebuggableMap ADVANCED_DEBUGGABLE_MAP = register(new FeatureDebuggableMap());
    public static final FeatureRoomCoordDisplay ADVANCED_COORDS = register(new FeatureRoomCoordDisplay());
    public static final SimpleFeature ADVANCED_RICHPRESENCE = register(new SimpleFeature("Advanced", "Discord Rich presence", "Discord rich presence with ASK-TO-JOIN Support!\n\nSimply type /dg asktojoin or /dg atj to toggle whether ask-to-join would be presented as option on discord!", "advanced.richpresence", true) {
        {
            parameters.put("disablenotskyblock", new FeatureParameter<Boolean>("disablenotskyblock", "Disable When not on Skyblock", "Disable When not on skyblock", false, "boolean"));
        }
    });

    public static final SimpleFeature SOLVER_RIDDLE = register(new SimpleFeature("Solver", "Riddle Puzzle (3 weirdo) Solver", "Highlights the correct box after clicking on all 3 weirdos",  "solver.riddle"));
    public static final SimpleFeature SOLVER_KAHOOT = register(new SimpleFeature("Solver", "Trivia Puzzle (Omnicrescent) Solver", "Highlights the correct solution for trivia puzzle",  "solver.trivia"));
    public static final SimpleFeature SOLVER_BLAZE = register(new SimpleFeature("Solver", "Blaze Puzzle Solver", "Highlights the blaze that needs to be killed in an blaze room", "solver.blaze") {{
        parameters.put("blazeborder", new FeatureParameter<AColor>("blazeborder", "Blaze Border Color", "Blaze border color", new AColor(255,255,255,0), "acolor"));
    }});
    public static final SimpleFeature SOLVER_TICTACTOE = register(new SimpleFeature("Solver", "Tictactoe Solver", "Shows the best move that could be taken by player in the tictactoe room", "solver.tictactoe"));
    public static final SimpleFeature SOLVER_ICEPATH = register(new SimpleFeature("Solver", "Icepath Puzzle Solver (Advanced)", "Calculates solution for icepath puzzle and displays it to user",  "solver.icepath"));
    public static final SimpleFeature SOLVER_SILVERFISH = register(new SimpleFeature("Solver", "Silverfish Puzzle Solver (Advanced)", "Actively calculates solution for silverfish puzzle and displays it to user",  "solver.silverfish"));
    public static final SimpleFeature SOLVER_WATERPUZZLE = register(new SimpleFeature("Solver", "Waterboard Puzzle Solver (Advanced)", "Calculates solution for waterboard puzzle and displays it to user",  "solver.waterboard"));
    public static final SimpleFeature SOLVER_BOX = register(new SimpleFeature("Solver", "Box Puzzle Solver (Advanced)", "Calculates solution for box puzzle room, and displays it to user",  "solver.box"));
    public static final SimpleFeature SOLVER_BOX_DISABLE_TEXT = register(new SimpleFeature("Solver", "Box Puzzle Solver Disable text", "Disable 'Type recalc to recalculate solution' showing up on top left.\nYou can still type recalc to recalc solution after disabling this feature",  "solver.boxrecalc", false));
    public static final SimpleFeature SOLVER_CREEPER = register(new SimpleFeature("Solver", "Creeper Puzzle Solver", "Draws line between prismarine lamps in creeper room",  "solver.creeper"));
    public static final SimpleFeature SOLVER_TELEPORT = register(new SimpleFeature("Solver", "Teleport Puzzle Solver", "Shows teleport pads you've visited in a teleport maze room",  "solver.teleport"));
    public static final SimpleFeature SOLVER_BOMBDEFUSE = register(new SimpleFeature("Solver", "Bomb Defuse Puzzle Solver", "Communicates with others dg using key 'F' for solutions and displays it",  "solver.bombdefuse"));

    public static final FeatureTooltipDungeonStat ETC_DUNGEONSTAT = register(new FeatureTooltipDungeonStat());
    public static final FeatureTooltipPrice ETC_PRICE = register(new FeatureTooltipPrice());
    public static final FeatureAbilityCooldown ETC_ABILITY_COOLDOWN = register(new FeatureAbilityCooldown());
    public static final FeatureCooldownCounter ETC_COOLDOWN =  register(new FeatureCooldownCounter());
    public static final FeatureRepartyCommand ETC_REPARTY =  register(new FeatureRepartyCommand());
    public static final FeatureDecreaseExplosionSound ETC_EXPLOSION_SOUND =  register(new FeatureDecreaseExplosionSound());
    public static final FeatureAutoAcceptReparty ETC_AUTO_ACCEPT_REPARTY =  register(new FeatureAutoAcceptReparty());
    public static final FeatureUpdateAlarm ETC_TEST = register(new FeatureUpdateAlarm());

    public static final SimpleFeature FIX_SPIRIT_BOOTS = register(new SimpleFeature("ETC", "Spirit Boots Fixer", "Fix Spirit boots messing up with inventory", "fixes.spirit", true));
    public static final FeatureDisableMessage FIX_MESSAGES = register(new FeatureDisableMessage());

    public static final FeatureCopyMessages ETC_COPY_MSG = register(new FeatureCopyMessages());

    public static final FeaturePenguins ETC_PENGUIN = register(new FeaturePenguins());

    public static final APIKey PARTYKICKER_APIKEY = register(new APIKey());
    public static final FeatureViewPlayerOnJoin PARTYKICKER_VIEWPLAYER = register(new FeatureViewPlayerOnJoin());
    public static final FeatureGoodParties PARTYKICKER_GOODPARTIES = register(new FeatureGoodParties());

    public static final FeatureWarningOnPortal BOSSFIGHT_WARNING_ON_PORTAL = register(new FeatureWarningOnPortal());
    public static final SimpleFeature BOSSFIGHT_CHESTPRICE = register(new FeatureChestPrice());
    public static final FeatureAutoReparty BOSSFIGHT_AUTOREPARTY = register(new FeatureAutoReparty());
    public static final FeatureBoxRealLivid BOSSFIGHT_BOX_REALLIVID = register(new FeatureBoxRealLivid());
    public static final FeatureBossHealth BOSSFIGHT_HEALTH = register(new FeatureBossHealth());
    public static final FeatureHideAnimals BOSSFIGHT_HIDE_ANIMALS = register(new FeatureHideAnimals());
    public static final FeatureThornBearPercentage BOSSFIGHT_BEAR_PERCENT = register(new FeatureThornBearPercentage());
    public static final FeatureThornSpiritBowTimer BOSSFIGHT_BOW_TIMER = register(new FeatureThornSpiritBowTimer());
    public static final FeatureTerracotaTimer BOSSFIGHT_TERRACOTTA_TIMER = register(new FeatureTerracotaTimer());
    public static final FeatureCurrentPhase BOSSFIGHT_CURRENT_PHASE = register(new FeatureCurrentPhase());
    public static final FeatureTerminalSolvers BOSSFIGHT_TERMINAL_SOLVERS = register(new FeatureTerminalSolvers());
    public static final FeatureSimonSaysSolver BOSSFIGHT_SIMONSAYS_SOLVER = register(new FeatureSimonSaysSolver());


    public static final FeatureDungeonMap DUNGEON_MAP = register(new FeatureDungeonMap());
    public static final FeatureDungeonRoomName DUNGEON_ROOMNAME = register(new FeatureDungeonRoomName());
    public static final FeaturePressAnyKeyToCloseChest DUNGEON_CLOSECHEST = register(new FeaturePressAnyKeyToCloseChest());
    public static final FeatureBoxSkelemaster DUNGEON_BOXSKELEMASTER = register(new FeatureBoxSkelemaster());
    public static final FeatureBoxBats DUNGEON_BOXBAT = register(new FeatureBoxBats());
    public static final FeatureBoxStarMobs DUNGEON_BOXSTARMOBS = register(new FeatureBoxStarMobs());
    public static final FeatureWatcherWarning DUNGEON_WATCHERWARNING = register(new FeatureWatcherWarning());
    public static final FeatureDungeonDeaths DUNGEON_DEATHS = register(new FeatureDungeonDeaths());
    public static final FeatureDungeonMilestone DUNGEON_MILESTONE = register(new FeatureDungeonMilestone());
    public static final FeatureDungeonRealTime DUNGEON_REALTIME = register(new FeatureDungeonRealTime());
    public static final FeatureDungeonSBTime DUNGEON_SBTIME = register(new FeatureDungeonSBTime());
    public static final FeatureDungeonSecrets DUNGEON_SECRETS = register(new FeatureDungeonSecrets());
    public static final FeatureDungeonCurrentRoomSecrets DUNGEON_SECRETS_ROOM = register(new FeatureDungeonCurrentRoomSecrets());
    public static final FeatureDungeonTombs DUNGEON_TOMBS = register(new FeatureDungeonTombs());
    public static final FeatureDungeonScore DUNGEON_SCORE = register(new FeatureDungeonScore());
    public static final FeatureWarnLowHealth DUNGEON_LOWHEALTH_WARN = register(new FeatureWarnLowHealth());
    public static final SimpleFeature DUNGEON_INTERMODCOMM = register(new SimpleFeature("Dungeon", "Communicate With Other's Dungeons Guide", "Sends total secret in the room to others\nSo that they can use the data to calculate total secret in dungeon run\n\nThis automates player chatting action, (chatting data) Thus it might be against hypixel's rules.\nBut mods like auto-gg which also automate player action and is kinda allowed mod exist so I'm leaving this feature.\nThis option is use-at-your-risk and you'll be responsible for ban if you somehow get banned because of this feature\n(Although it is not likely to happen)\nDefaults to off", "dungeon.intermodcomm", false));
    public static final FeaturePlayerESP DUNGEON_PLAYERESP = register(new FeaturePlayerESP());
    public static final FeatureHideNameTags DUNGEON_HIDENAMETAGS = register(new FeatureHideNameTags());

    public static final FeatureMechanicBrowse SECRET_BROWSE = register(new FeatureMechanicBrowse());
    public static final FeatureActions SECRET_ACTIONS = register(new FeatureActions());
    public static final FeatureSoulRoomWarning SECRET_FAIRYSOUL = register(new FeatureSoulRoomWarning());
    public static final SimpleFeature SECRET_AUTO_BROWSE_NEXT = register(new SimpleFeature("Secret", "Auto browse next secret.", "Auto browse best next secret after current one completes.\nthe first pathfinding of first secret needs to be triggered first in order for this option to work", "secret.autobrowse", false));
    public static final SimpleFeature SECRET_AUTO_START = register(new SimpleFeature("Secret", "Auto browse secret upon entering room", "Auto browse best secret upon entering the room.", "secret.autouponenter", false));
    public static final SimpleFeature SECRET_NEXT_KEY = register(new SimpleFeature("Secret", "Auto browse next secret upon pressing a key", "Auto browse the best next secret when you press key.\nChange key at your key settings (Settings -> Controls)", "secret.keyfornext", false));
    public static final SimpleFeature SECRET_TOGGLE_KEY = register(new SimpleFeature("Secret", "Press a key to toggle pathfind lines", "A key for toggling pathfound line visibility.\nChange key at your key settings (Settings -> Controls)", "secret.togglePathfind"));
    public static final SimpleFeature SECRET_FREEZE_LINES = register(new FeatureFreezePathfind());

    public static final FeatureNicknamePrefix COSMETIC_PREFIX = register(new FeatureNicknamePrefix());
    public static final FeatureNicknameColor COSMETIC_NICKNAMECOLOR = register(new FeatureNicknameColor());
}
