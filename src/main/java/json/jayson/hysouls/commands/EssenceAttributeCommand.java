package json.jayson.hysouls.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatMap;
import com.hypixel.hytale.server.core.modules.entitystats.EntityStatsModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import json.jayson.hysouls.components.ComponentTypes;
import json.jayson.hysouls.components.EssenceAttributeComponent;
import json.jayson.hysouls.components.EssenceComponent;
import json.jayson.hysouls.essence_attribute.EssenceAttribute;
import json.jayson.hysouls.essence_attribute.EssenceAttributes;
import org.jetbrains.annotations.NotNull;

public class EssenceAttributeCommand extends AbstractCommandCollection {


    public EssenceAttributeCommand() {
        super("attribute", "Essence Attribute command base");
        addSubCommand(new Set());
        addSubCommand(new Get());
    }


    public static class Get extends AbstractPlayerCommand {

        public Get() {
            super("get", "Gets the essence attribute of a player");
        }

        RequiredArg<PlayerRef> playerArg = withRequiredArg("player", "Target", ArgTypes.PLAYER_REF);
        RequiredArg<String> attribute = withRequiredArg("attribute", "Attribute to change", ArgTypes.STRING);


        @Override
        protected void execute(@NotNull CommandContext commandContext, @NotNull Store<EntityStore> store, @NotNull Ref<EntityStore> ref, @NotNull PlayerRef playerRef, @NotNull World world) {
            PlayerRef player = playerArg.get(commandContext);
            EssenceAttribute essenceAttribute = EssenceAttributes.getAttribute(attribute.get(commandContext));

            if(essenceAttribute == null) {
                StringBuilder valids = new StringBuilder();
                for (String s : EssenceAttributes.getAttributeMap().keySet()) {
                    valids.append(s.toLowerCase());
                }
                playerRef.sendMessage(Message.raw("Couldnt find essence attribute, valid attributes are: " + valids));
                return;
            }

            if(!player.isValid() || player.getReference() == null) {
                playerRef.sendMessage(Message.raw("Couldnt find player"));
                return;
            }

            EssenceAttributeComponent essenceComponent = store.getComponent(player.getReference(), ComponentTypes.ESSENCE_ATTRIBUTE);
            if (essenceComponent != null) {
                playerRef.sendMessage(Message.raw(player.getUsername() + "s  " + essenceAttribute.getNamed() + " is at " + essenceAttribute.get(essenceComponent)));
            }
        }
    }


    public static class Set extends AbstractPlayerCommand {

        public Set() {
            super("set", "Sets the essence attribute of a player");
        }

        RequiredArg<PlayerRef> playerArg = withRequiredArg("player", "Target", ArgTypes.PLAYER_REF);
        RequiredArg<String> attribute = withRequiredArg("attribute", "Attribute to change", ArgTypes.STRING);
        RequiredArg<Integer> essencesArg = withRequiredArg("value", "Value to set Attribute to", ArgTypes.INTEGER);

        @Override
        protected void execute(@NotNull CommandContext commandContext, @NotNull Store<EntityStore> store, @NotNull Ref<EntityStore> ref, @NotNull PlayerRef playerRef, @NotNull World world) {
            PlayerRef player = playerArg.get(commandContext);
            EssenceAttribute essenceAttribute = EssenceAttributes.getAttribute(attribute.get(commandContext));

            if(essenceAttribute == null) {
                StringBuilder valids = new StringBuilder();
                for (String s : EssenceAttributes.getAttributeMap().keySet()) {
                    valids.append(s.toLowerCase());
                }
                playerRef.sendMessage(Message.raw("Couldnt find essence attribute, valid attributes are: " + valids));
                return;
            }

            if(!player.isValid() || player.getReference() == null) {
                playerRef.sendMessage(Message.raw("Couldnt find player"));
                return;
            }

            EssenceAttributeComponent essenceComponent = store.getComponent(player.getReference(), ComponentTypes.ESSENCE_ATTRIBUTE);
            if (essenceComponent != null) {
                essenceAttribute.set(essenceComponent, essencesArg.get(commandContext));
                playerRef.sendMessage(Message.raw("set " + player.getUsername() + "s " + essenceAttribute.getNamed() + " to " + essenceAttribute.get(essenceComponent)));

                EntityStatMap statMap = ref.getStore().getComponent(ref, EntityStatsModule.get().getEntityStatMapComponentType());
                if(statMap == null) {
                    return;
                }
                essenceAttribute.applyModifierBuff(statMap, essenceComponent);
            }
        }
    }
}
