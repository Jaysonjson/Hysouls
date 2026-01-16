package json.jayson.hysouls.ui;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.player.hud.CustomUIHud;
import com.hypixel.hytale.server.core.ui.builder.UICommandBuilder;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import json.jayson.hysouls.Hysouls;
import org.jetbrains.annotations.NotNull;

public class SoulsHud extends CustomUIHud {

    int souls = 0;
    public SoulsHud(@NotNull PlayerRef playerRef, int souls) {
        super(playerRef);
        this.souls = souls;
    }

    @Override
    protected void build(@NotNull UICommandBuilder var1) {
        var1.append("Souls.ui");
        var1.set("#SoulLabel.TextSpans", Message.raw(souls + ""));
        Hysouls.LOGGER.atWarning().log("Test1");
    }

    public void setSouls(int souls) {
        this.souls = souls;
        UICommandBuilder builder = new UICommandBuilder();
        builder.set("#SoulLabel.TextSpans", Message.raw(souls + ""));
        update(false, builder);
        Hysouls.LOGGER.atWarning().log("Test2 " + souls);
    }
}
