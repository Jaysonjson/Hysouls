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
import json.jayson.hysouls.components.EssenceStats;
import org.jetbrains.annotations.NotNull;


//Needs a major rework
public class LevelPage extends InteractiveCustomUIPage<LevelPage.LevelEventData> {

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

        statAction(uiEventBuilder, EssenceStats.VIGOR);
        statAction(uiEventBuilder, EssenceStats.ENDURANCE);
        statAction(uiEventBuilder, EssenceStats.MIND);

        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#ConfirmButton", EventData.of("Action", "Confirm"));
    }

    public void statAction(UIEventBuilder builder, EssenceStats stats) {
        builder.addEventBinding(CustomUIEventBindingType.Activating, "#" + stats.getNamed() + "Minus", EventData.of("Action", stats.getNamed() + "Minus"));
        builder.addEventBinding(CustomUIEventBindingType.Activating, "#" + stats.getNamed() + "Plus", EventData.of("Action", stats.getNamed() + "Plus"));
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

                statText(builder, EssenceStats.VIGOR, essenceStatComponent.getVigor(), wantedVigor);
                statText(builder, EssenceStats.ENDURANCE, essenceStatComponent.getEndurance(), wantedEndurance);
                statText(builder, EssenceStats.MIND, essenceStatComponent.getMind(), wantedMind);
            }
        }
    }

    public void statText(UICommandBuilder builder, EssenceStats stats, int current, int wanted) {
        builder.set("#Next" + stats.getNamed() + ".TextSpans", Message.raw("" + (current + wanted)));
        builder.set("#Current" + stats.getNamed() + ".TextSpans", Message.raw("" + (current)));
    }

    @Override
    public void handleDataEvent(@NotNull Ref<EntityStore> ref, @NotNull Store<EntityStore> store, @NotNull LevelPage.LevelEventData data) {
        EssenceStatComponent essenceStatComponent = ref.getStore().getComponent(ref, ComponentTypes.ESSENCE_STAT);
        EssenceComponent essenceComponent = ref.getStore().getComponent(ref, ComponentTypes.ESSENCES);
        if(essenceStatComponent != null && essenceComponent != null) {
            switch (data.action) {

                case "VigorMinus" -> {
                    if (wantedVigor != 0) {
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

                case "EnduranceMinus" -> {
                    if (wantedEndurance != 0) {
                        wantedEndurance--;
                        wantedLevel--;
                    }
                }

                case "EndurancePlus" -> {
                    if (wantedEndurance != 99) {
                        wantedEndurance++;
                        wantedLevel++;
                    }
                }

                case "MindMinus" -> {
                    if (wantedMind != 0) {
                        wantedMind--;
                        wantedLevel--;
                    }
                }

                case "MindPlus" -> {
                    if (wantedMind != 99) {
                        wantedMind++;
                        wantedLevel++;
                    }
                }

                case "Confirm" -> {
                    int requiredEssences = EssenceUtil.calculateTotalRequiredEssence(essenceStatComponent.getLevel(), wantedLevel);
                    if(essenceComponent.getEssences() >= requiredEssences) {
                        essenceStatComponent.setVigor(essenceStatComponent.getVigor() + wantedVigor);
                        essenceStatComponent.setEndurance(essenceStatComponent.getEndurance() + wantedEndurance);
                        essenceStatComponent.setMind(essenceStatComponent.getMind() + wantedMind);
                        essenceComponent.setEssences(ref, essenceComponent.getEssences() - requiredEssences);
                        essenceStatComponent.apply(ref);
                    }

                    close();
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
