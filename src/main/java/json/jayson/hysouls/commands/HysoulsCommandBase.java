package json.jayson.hysouls.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import json.jayson.hysouls.Hysouls;
import org.jetbrains.annotations.NotNull;

public class HysoulsCommandBase extends AbstractCommandCollection {
    public HysoulsCommandBase() {
        super("hysouls", "Hysouls command base");
        addSubCommand(new EssencesCommand());
        addSubCommand(new Version());
    }

    public static class Version extends AbstractPlayerCommand {

        public Version() {
            super("version", "Hysouls version");
        }

        @Override
        protected void execute(@NotNull CommandContext commandContext, @NotNull Store<EntityStore> store, @NotNull Ref<EntityStore> ref, @NotNull PlayerRef playerRef, @NotNull World world) {
            playerRef.sendMessage(Message.raw("Hysouls is running on Version " + Hysouls.VERSION));
        }
    }

}
