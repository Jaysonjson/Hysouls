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
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import json.jayson.hysouls.util.EssenceUtil;
import json.jayson.hysouls.components.ComponentTypes;
import json.jayson.hysouls.components.EssenceAttributeComponent;
import json.jayson.hysouls.components.EssenceComponent;
import json.jayson.hysouls.essence_attribute.EssenceAttribute;
import json.jayson.hysouls.essence_attribute.EssenceAttributeModifier;
import json.jayson.hysouls.essence_attribute.EssenceAttributes;
import json.jayson.hysouls.essence_attribute.EssenceAttributeHolder;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;


//Needs a major rework, plan is to make the UI dynamically with the EssenceStats in mind
public class LevelPage extends InteractiveCustomUIPage<LevelPage.LevelEventData> implements EssenceAttributeHolder {

    public int wantedLevel = 0;
    public int wantedVigor = 0;
    public int wantedEndurance = 0;
    public int wantedMind = 0;
    public int wantedDex = 0;
    public int wantedStr = 0;
    public int wantedLuck = 0;
    public int statueLevelCap;

    public LevelPage(@NotNull PlayerRef playerRef, int statueLevelCap) {
        super(playerRef, CustomPageLifetime.CanDismiss, LevelEventData.CODEC);
        this.statueLevelCap = statueLevelCap;
    }

    @Override
    public void build(@NotNull Ref<EntityStore> ref, @NotNull UICommandBuilder uiCommandBuilder, @NotNull UIEventBuilder uiEventBuilder, @NotNull Store<EntityStore> store) {
        uiCommandBuilder.append("Pages/LevelUpPage.ui");

        for (EssenceAttribute value : EssenceAttributes.getAttributeMap().values()) {
            uiCommandBuilder.insertBefore("#ButtonGroup", value.getLevelPageUIPath());
        }

        //uiCommandBuilder.set("#LevelUpContainer.Anchor", "Anchor(Width: 665, Height: " + (250 * 60 * EssenceAttributes.getAttributeMap().size()) + ")");
        setVars(ref, uiCommandBuilder);

        for (EssenceAttribute value : EssenceAttributes.getAttributeMap().values()) {
            value.levelUiAction(uiEventBuilder);
        }

        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#ConfirmButton", EventData.of("Action", "Confirm"));
    }



    public void setVars(Ref<EntityStore> ref, UICommandBuilder builder) {
        if(ref.isValid()) {
            EssenceComponent essenceComponent = ref.getStore().getComponent(ref, ComponentTypes.ESSENCES);
            EssenceAttributeComponent essenceAttributeComponent = ref.getStore().getComponent(ref, ComponentTypes.ESSENCE_ATTRIBUTE);
            if(essenceComponent != null && essenceAttributeComponent != null) {
                int requiredEssences = EssenceUtil.calculateTotalRequiredEssence(essenceAttributeComponent.getLevel(), wantedLevel);
                builder.set("#EssencesHeld.TextSpans", Message.raw("" + essenceComponent.getEssences()));
                builder.set("#CurrentLevel.TextSpans", Message.raw("" + essenceAttributeComponent.getLevel()));
                builder.set("#NextLevel.TextSpans", Message.raw("" + (essenceAttributeComponent.getLevel() + wantedLevel)));
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
                for (EssenceAttribute value : EssenceAttributes.getAttributeMap().values()) {
                    value.levelUiText(builder, essenceAttributeComponent, this);
                }

                float currentDamage = calculateLinear(essenceAttributeComponent, EssenceAttributeModifier.Type.DMG_BUFF);
                float currentDropChance = calculateLinear(essenceAttributeComponent, EssenceAttributeModifier.Type.EXTRA_DROP_CHANCE);
                DecimalFormat dformat = new DecimalFormat("#0.0");

                builder.set("#CurrentHealth.TextSpans", Message.raw(dformat.format(calculateStat(essenceAttributeComponent, DefaultEntityStatTypes.getHealth()))));
                builder.set("#CurrentStamina.TextSpans", Message.raw(dformat.format(calculateStat(essenceAttributeComponent, DefaultEntityStatTypes.getStamina()))));
                builder.set("#CurrentMana.TextSpans", Message.raw(dformat.format(calculateStat(essenceAttributeComponent, DefaultEntityStatTypes.getMana()))));
                builder.set("#CurrentDamage.TextSpans", Message.raw(dformat.format(currentDamage)));
                builder.set("#NextHealth.TextSpans", Message.raw(dformat.format(calculateNextStat(essenceAttributeComponent, DefaultEntityStatTypes.getHealth()))));
                builder.set("#NextStamina.TextSpans", Message.raw(dformat.format(calculateNextStat(essenceAttributeComponent, DefaultEntityStatTypes.getStamina()))));
                builder.set("#NextMana.TextSpans", Message.raw(dformat.format(calculateNextStat(essenceAttributeComponent, DefaultEntityStatTypes.getMana()))));
                builder.set("#NextDamage.TextSpans", Message.raw(dformat.format(currentDamage + calculateLinear(this, EssenceAttributeModifier.Type.DMG_BUFF))));
                builder.set("#CurrentDropChance.TextSpans", Message.raw(dformat.format(currentDropChance  + "%")));
                builder.set("#NextDropChance.TextSpans", Message.raw(dformat.format(currentDropChance + calculateLinear(this,  EssenceAttributeModifier.Type.EXTRA_DROP_CHANCE)) + "%"));

            }
        }
    }


    public float calculateLinear(EssenceAttributeHolder holder, EssenceAttributeModifier.Type type) {
        float dmg = 0;
        for (EssenceAttribute value : EssenceAttributes.getAttributeMap().values()) {
            for (EssenceAttributeModifier modifier : value.getModifiers()) {
                if(modifier.getType() == type) {
                    dmg += value.get(holder) * modifier.getValue();
                }
            }
        }
        return dmg;
    }



    public float calculateStat(EssenceAttributeHolder holder, int stat) {
        float health = 0;
        for (EssenceAttribute value : EssenceAttributes.getAttributeMap().values()) {
            for (EssenceAttributeModifier modifier : value.getModifiers()) {
                if(modifier.getStatIndex() == stat && modifier.getType() == EssenceAttributeModifier.Type.STAT_BUFF) {
                    health += (float) Math.pow(value.get(holder), modifier.getValue());
                }
            }
        }
        return health;
    }


    public float calculateNextStat(EssenceAttributeHolder holder, int stat) {
        float health = 0;
        for (EssenceAttribute value : EssenceAttributes.getAttributeMap().values()) {
            for (EssenceAttributeModifier modifier : value.getModifiers()) {
                if(modifier.getStatIndex() == stat && modifier.getType() == EssenceAttributeModifier.Type.STAT_BUFF) {
                    health += (float) Math.pow(value.get(holder) + value.get(this), modifier.getValue());
                }
            }
        }
        return health;
    }




    @Override
    public void handleDataEvent(@NotNull Ref<EntityStore> ref, @NotNull Store<EntityStore> store, @NotNull LevelPage.LevelEventData data) {
        EssenceAttributeComponent essenceAttributeComponent = ref.getStore().getComponent(ref, ComponentTypes.ESSENCE_ATTRIBUTE);
        EssenceComponent essenceComponent = ref.getStore().getComponent(ref, ComponentTypes.ESSENCES);
        if(essenceAttributeComponent != null && essenceComponent != null) {
            if(data.action.equalsIgnoreCase("Confirm")) {
                int requiredEssences = EssenceUtil.calculateTotalRequiredEssence(essenceAttributeComponent.getLevel(), wantedLevel);
                if(essenceComponent.getEssences() >= requiredEssences && statueLevelCap >= essenceAttributeComponent.getLevel() + wantedLevel ) {
                    for (EssenceAttribute value : EssenceAttributes.getAttributeMap().values()) {
                        value.set(essenceAttributeComponent, value.get(essenceAttributeComponent) + value.get(this));
                    }
                    essenceComponent.setEssences(ref, essenceComponent.getEssences() - requiredEssences);
                    essenceAttributeComponent.apply(ref);
                }
                close();
            } else {
                for (EssenceAttribute value : EssenceAttributes.getAttributeMap().values()) {
                    value.levelUiEventAction(this, essenceAttributeComponent, data.action);
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
    public int getDexterity() {
        return wantedDex;
    }

    @Override
    public int getStrength() {
        return wantedStr;
    }

    @Override
    public int getLuck() {
        return wantedLuck;
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
    public void setDexterity(int dexterity) {
        wantedDex = dexterity;
    }

    @Override
    public void setStrength(int strength) {
        wantedStr = strength;
    }

    @Override
    public void setLuck(int luck) {
        this.wantedLuck = luck;
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
