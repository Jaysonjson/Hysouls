package json.jayson.hysouls.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import json.jayson.hysouls.components.ComponentTypes;
import json.jayson.hysouls.components.EssenceComponent;
import org.jetbrains.annotations.NotNull;

public class EssencesCommand extends AbstractCommandCollection {


    public EssencesCommand() {
        super("essences", "Essences command base");
        addSubCommand(new Set());
        addSubCommand(new Get());
        addSubCommand(new EssenceAttributeCommand());
    }


    public static class Get extends AbstractPlayerCommand {

        public Get() {
            super("get", "Gets the essences of a player");
        }

        RequiredArg<PlayerRef> playerArg = withRequiredArg("player", "Target", ArgTypes.PLAYER_REF);


        @Override
        protected void execute(@NotNull CommandContext commandContext, @NotNull Store<EntityStore> store, @NotNull Ref<EntityStore> ref, @NotNull PlayerRef playerRef, @NotNull World world) {
            PlayerRef player = playerArg.get(commandContext);

            if(!player.isValid() || player.getReference() == null) {
                playerRef.sendMessage(Message.raw("Couldnt find player"));
                return;
            }

            EssenceComponent essenceComponent = store.getComponent(player.getReference(), ComponentTypes.ESSENCES);
            if (essenceComponent != null) {
                playerRef.sendMessage(Message.raw(player.getUsername() + " has " + essenceComponent.getEssences() + " essences"));
            }

        }
    }


    public static class Set extends AbstractPlayerCommand {

        public Set() {
            super("set", "Sets the essences of a player");
        }

        RequiredArg<PlayerRef> playerArg = withRequiredArg("player", "Target", ArgTypes.PLAYER_REF);
        RequiredArg<Integer> essencesArg = withRequiredArg("essences", "Essences to give", ArgTypes.INTEGER);


        @Override
        protected void execute(@NotNull CommandContext commandContext, @NotNull Store<EntityStore> store, @NotNull Ref<EntityStore> ref, @NotNull PlayerRef playerRef, @NotNull World world) {
            PlayerRef player = playerArg.get(commandContext);

            if(!player.isValid() || player.getReference() == null) {
                playerRef.sendMessage(Message.raw("Couldnt find player"));
                return;
            }

            EssenceComponent essenceComponent = store.getComponent(player.getReference(), ComponentTypes.ESSENCES);
            if (essenceComponent != null) {
                essenceComponent.setEssences(player.getReference(), essencesArg.get(commandContext));
                playerRef.sendMessage(Message.raw("set " + player.getUsername() + "s essences to " + essenceComponent.getEssences()));
            }
        }
    }
}
