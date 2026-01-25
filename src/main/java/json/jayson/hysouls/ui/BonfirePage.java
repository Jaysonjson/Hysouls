package json.jayson.hysouls.ui;

import com.hypixel.hytale.builtin.crafting.component.CraftingManager;
import com.hypixel.hytale.builtin.crafting.state.BenchState;
import com.hypixel.hytale.builtin.crafting.window.BenchWindow;
import com.hypixel.hytale.builtin.crafting.window.DiagramCraftingWindow;
import com.hypixel.hytale.builtin.crafting.window.SimpleCraftingWindow;
import com.hypixel.hytale.builtin.crafting.window.StructuralCraftingWindow;
import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3i;
import com.hypixel.hytale.protocol.BlockPosition;
import com.hypixel.hytale.protocol.InteractionType;
import com.hypixel.hytale.protocol.packets.interface_.CustomPageLifetime;
import com.hypixel.hytale.protocol.packets.interface_.CustomUIEventBindingType;
import com.hypixel.hytale.protocol.packets.interface_.Page;
import com.hypixel.hytale.server.core.entity.InteractionContext;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.entities.player.pages.InteractiveCustomUIPage;
import com.hypixel.hytale.server.core.entity.entities.player.windows.Window;
import com.hypixel.hytale.server.core.inventory.ItemStack;
import com.hypixel.hytale.server.core.modules.interaction.interaction.CooldownHandler;
import com.hypixel.hytale.server.core.ui.builder.EventData;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.ui.builder.UIEventBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.meta.BlockState;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

public class BonfirePage extends InteractiveCustomUIPage<BonfirePage.BonfireEventData> {


    BlockPosition block;
    public BonfirePage(@NotNull PlayerRef playerRef, BlockPosition block) {
        super(playerRef, CustomPageLifetime.CanDismiss, BonfireEventData.CODEC);
        this.block = block;
    }

    @Override
    public void build(@NotNull Ref<EntityStore> ref, @NotNull UICommandBuilder uiCommandBuilder, @NotNull UIEventBuilder uiEventBuilder, @NotNull Store<EntityStore> store) {
        uiCommandBuilder.append("Pages/BonfirePage.ui");
        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#LevelUpButton", EventData.of("Action", "LevelUp"));
        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#RestButton", EventData.of("Action", "Rest"));
        uiEventBuilder.addEventBinding(CustomUIEventBindingType.Activating, "#CraftingButton", EventData.of("Action", "Craft"));
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

            case "Craft": {
                openCrafting(store.getExternalData().getWorld());
                break;
            }

            case "Rest": {
                close();
                break;
            }
        }

    }


    protected void openCrafting(@Nonnull World world) {
        Ref<EntityStore> ref = playerRef.getReference();
        Store<EntityStore> store = ref.getStore();
        Player playerComponent = store.getComponent(ref, Player.getComponentType());
        System.out.println("D1");
        if (playerComponent != null) {
            System.out.println("D2");
            CraftingManager craftingManagerComponent = (store.getComponent(ref, CraftingManager.getComponentType()));
            assert craftingManagerComponent != null;
            if (!craftingManagerComponent.hasBenchSet()) {
                System.out.println("D3");
                BlockState var13 = world.getState(block.x, block.y, block.z, true);
                if (var13 instanceof BenchState) {
                    System.out.println("D4");
                    BenchState benchState = (BenchState)var13;
                    BenchWindow benchWindow = new SimpleCraftingWindow(benchState);
                    UUIDComponent uuidComponent = store.getComponent(ref, UUIDComponent.getComponentType());
                    assert uuidComponent != null;
                    UUID uuid = uuidComponent.getUuid();
                    if (benchState.getWindows().putIfAbsent(uuid, benchWindow) == null) {
                        benchWindow.registerCloseEvent((event) -> benchState.getWindows().remove(uuid, benchWindow));
                    }
                    System.out.println("D5");

                    playerComponent.getPageManager().setPageWithWindows(ref, store, Page.Bench, true, new Window[]{benchWindow});
                }

            }
        }
    }


    @Override
    protected void close() {
        super.close();
    }
}
