package json.jayson.hysouls.ui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import json.jayson.hysouls.EssenceUtil;
import json.jayson.hysouls.components.ComponentTypes;
import json.jayson.hysouls.components.EssenceComponent;
import json.jayson.hysouls.components.EssenceStatComponent;
import json.jayson.hysouls.essence_stat.EssenceStat;
import json.jayson.hysouls.essence_stat.EssenceStats;
import json.jayson.hysouls.essence_stat.IEssenceStated;
import org.jetbrains.annotations.NotNull;


//Needs a major rework
public class LevelPage extends InteractiveCustomUIPage<LevelPage.LevelEventData> implements IEssenceStated {

    public int wantedLevel = 0;
    public int wantedVigor = 0;
    public int wantedEndurance = 0;
    public int wantedMind = 0;

    public LevelPage(@NotNull PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismiss, LevelEventData.CODEC);
    }

    @Override
    public void build(@NotNull Ref<EntityStore> ref, @NotNull UICommandBuilder uiCommandBuilder, @NotNull UIEventBuilder uiEventBuilder, @NotNull Store<EntityStore> store) {
        uiCommandBuilder.append("Pages/LevelUpPage.ui");
        setVars(ref, uiCommandBuilder);

        for (EssenceStat value : EssenceStats.getStatsMap().values()) {
            value.levelUiAction(uiEventBuilder);
        }

        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#ConfirmButton", EventData.of("Action", "Confirm"));
    }



    public void setVars(Ref<EntityStore> ref, UICommandBuilder builder) {
        if(ref.isValid()) {
            EssenceComponent essenceComponent = ref.getStore().getComponent(ref, ComponentTypes.ESSENCES);
            EssenceStatComponent essenceStatComponent = ref.getStore().getComponent(ref, ComponentTypes.ESSENCE_STAT);
            if(essenceComponent != null && essenceStatComponent != null) {
                int requiredEssences = EssenceUtil.calculateTotalRequiredEssence(essenceStatComponent.getLevel(), wantedLevel);
                builder.set("#EssencesHeld.TextSpans", Message.raw("" + essenceComponent.getEssences()));
                builder.set("#CurrentLevel.TextSpans", Message.raw("" + essenceStatComponent.getLevel()));
                builder.set("#NextLevel.TextSpans", Message.raw("" + (essenceStatComponent.getLevel() + wantedLevel)));
                builder.set("#EssencesNeeded.TextSpans", Message.raw("" + requiredEssences));
                if(wantedLevel == 0) {
                    builder.set("#EssencesHeldMinus.TextSpans", Message.raw("" + (essenceComponent.getEssences())));
                } else {
                    builder.set("#EssencesHeldMinus.TextSpans", Message.raw("" + (essenceComponent.getEssences() - requiredEssences)));
                }

                if(requiredEssences > essenceComponent.getEssences()) {
                    builder.set("#EssencesNeeded.Style.TextColor", "#ff8888");
                } else {
                    builder.set("#EssencesNeeded.Style.TextColor", "#ffffff");
                }

                System.out.println(EssenceStats.getStatsMap().values().size());
                for (EssenceStat value : EssenceStats.getStatsMap().values()) {
                    value.levelUiText(builder, essenceStatComponent, this);
                }
            }
        }
    }

    @Override
    public void handleDataEvent(@NotNull Ref<EntityStore> ref, @NotNull Store<EntityStore> store, @NotNull LevelPage.LevelEventData data) {
        EssenceStatComponent essenceStatComponent = ref.getStore().getComponent(ref, ComponentTypes.ESSENCE_STAT);
        EssenceComponent essenceComponent = ref.getStore().getComponent(ref, ComponentTypes.ESSENCES);
        if(essenceStatComponent != null && essenceComponent != null) {
            if(data.action.equalsIgnoreCase("Confirm")) {
                int requiredEssences = EssenceUtil.calculateTotalRequiredEssence(essenceStatComponent.getLevel(), wantedLevel);
                if(essenceComponent.getEssences() >= requiredEssences) {
                    for (EssenceStat value : EssenceStats.getStatsMap().values()) {
                        value.set(essenceStatComponent, value.get(essenceStatComponent) + value.get(this));
                    }
                    essenceComponent.setEssences(ref, essenceComponent.getEssences() - requiredEssences);
                    essenceStatComponent.apply(ref);
                }
                close();
            } else {
                for (EssenceStat value : EssenceStats.getStatsMap().values()) {
                    value.levelUiEventAction(this, data.action);
                }
            }
        }
        UICommandBuilder builder = new UICommandBuilder();
        setVars(ref, builder);
        sendUpdate(builder);
    }

    @Override
    public int getVigor() {
        return wantedVigor;
    }

    @Override
    public int getEndurance() {
        return wantedEndurance;
    }

    @Override
    public int getMind() {
        return wantedMind;
    }

    @Override
    public int getLevel() {
        return wantedLevel;
    }

    @Override
    public void setLevel(int level) {
        this.wantedLevel = level;
    }

    @Override
    public void setEndurance(int endurance) {
        this.wantedEndurance = endurance;
    }

    @Override
    public void setMind(int mind) {
        this.wantedMind = mind;
    }

    @Override
    public void setVigor(int vigor) {
        this.wantedVigor = vigor;
    }

    public static class LevelEventData {
        public static final BuilderCodec<LevelEventData> CODEC = BuilderCodec.builder(LevelEventData.class, LevelEventData::new)
                .append(new KeyedCodec<>("Action", Codec.STRING),
                        (d, v) -> d.action = v, d -> d.action).add()
                .build();

        public String action;

    }
}
