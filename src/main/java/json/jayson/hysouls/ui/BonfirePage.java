package json.jayson.hysouls.ui;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;

public class BonfirePage extends InteractiveCustomUIPage<BonfirePage.BonfireEventData> {


    public BonfirePage(@NotNull PlayerRef playerRef) {
        super(playerRef, CustomPageLifetime.CanDismiss, BonfireEventData.CODEC);
    }

    @Override
    public void build(@NotNull Ref<EntityStore> ref, @NotNull UICommandBuilder uiCommandBuilder, @NotNull UIEventBuilder uiEventBuilder, @NotNull Store<EntityStore> store) {
        uiCommandBuilder.append("Pages/BonfirePage.ui");
        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#LevelUpButton", EventData.of("Action", "LevelUp"));
        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#RestButton", EventData.of("Action", "Rest"));
    }

    public static class BonfireEventData {
        public static final BuilderCodec<BonfireEventData> CODEC = BuilderCodec.builder(BonfireEventData.class, BonfireEventData::new)
                .append(new KeyedCodec<>("Action", Codec.STRING),
                        (d, v) -> d.action = v, d -> d.action).add()
                .build();

        public String action;
    }

    @Override
    public void handleDataEvent(@NotNull Ref<EntityStore> ref, @NotNull Store<EntityStore> store, @NotNull BonfirePage.BonfireEventData data) {
        super.handleDataEvent(ref, store, data);

        switch (data.action) {
            case "LevelUp": {
                Player player = store.getComponent(ref, Player.getComponentType());
                if(player == null) return;
                player.getPageManager().openCustomPage(ref, store, new LevelPage(playerRef, 99));
                break;
            }

            case "Rest": {
                close();
                break;
            }
        }

    }

    @Override
    protected void close() {
        super.close();
    }
}
