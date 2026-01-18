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
import json.jayson.hysouls.components.ComponentTypes;
import json.jayson.hysouls.components.EssenceComponent;
import json.jayson.hysouls.components.EssenceStatComponent;
import org.jetbrains.annotations.NotNull;


//InteractiveCustomUIPage
public class LevelPage extends InteractiveCustomUIPage<LevelPage.LevelEventData> {

    public int wantedLevel = 0;
    public int wantedVigor = 0;

    public LevelPage(@NotNull PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismiss, LevelEventData.CODEC);
    }

    @Override
    public void build(@NotNull Ref<EntityStore> ref, @NotNull UICommandBuilder uiCommandBuilder, @NotNull UIEventBuilder uiEventBuilder, @NotNull Store<EntityStore> store) {
        uiCommandBuilder.append("Pages/LevelUpPage.ui");
        setVars(ref, uiCommandBuilder);

        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#VigorMinus", EventData.of("Action", "VigorMinus"));
        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#VigorPlus", EventData.of("Action", "VigorPlus"));
    }


    public void setVars(Ref<EntityStore> ref, UICommandBuilder builder) {
        if(ref.isValid()) {
            EssenceComponent essenceComponent = ref.getStore().getComponent(ref, ComponentTypes.ESSENCES);
            EssenceStatComponent essenceStatComponent = ref.getStore().getComponent(ref, ComponentTypes.ESSENCE_STAT);
            if(essenceComponent != null && essenceStatComponent != null) {
                int requiredEssences = EssenceStatComponent.calculateRequiredEssences(essenceStatComponent.getLevel() + wantedLevel);
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

                builder.set("#NextVigor.TextSpans", Message.raw("" + (essenceStatComponent.getVigor() + wantedVigor)));
                builder.set("#CurrentVigor.TextSpans", Message.raw("" + (essenceStatComponent.getVigor())));

            }
        }
    }

    @Override
    public void handleDataEvent(@NotNull Ref<EntityStore> ref, @NotNull Store<EntityStore> store, @NotNull LevelPage.LevelEventData data) {
        EssenceStatComponent essenceStatComponent = ref.getStore().getComponent(ref, ComponentTypes.ESSENCE_STAT);
        EssenceComponent essenceComponent = ref.getStore().getComponent(ref, ComponentTypes.ESSENCES);
        if(essenceStatComponent != null && essenceComponent != null) {
            switch (data.action) {

                case "VigorMinus" -> {
                    if (wantedVigor != essenceStatComponent.getVigor() - 1) {
                        wantedVigor--;
                        wantedLevel--;
                    }
                }

                case "VigorPlus" -> {
                    if (wantedVigor != 99) {
                       // if(EssenceStatComponent.calculateRequiredEssences(essenceStatComponent.getLevel() + wantedLevel + 1) <= essenceComponent.getEssences()) {
                            wantedVigor++;
                            wantedLevel++;
                      //  }
                    }
                }

            }
        }
        UICommandBuilder builder = new UICommandBuilder();
        setVars(ref, builder);
        sendUpdate(builder);
    }

    public static class LevelEventData {
        public static final BuilderCodec<LevelEventData> CODEC = BuilderCodec.builder(LevelEventData.class, LevelEventData::new)
                .append(new KeyedCodec<>("Action", Codec.STRING),
                        (d, v) -> d.action = v, d -> d.action).add()
                .build();

        public String action;

    }
}
