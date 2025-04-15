package com.iafenvoy.nee.command;

import com.iafenvoy.nee.trade.PlayerExchangeHolder;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.TickEvent;
import it.unimi.dsi.fastutil.objects.Object2LongLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TradeCommand {
    private static final Map<PlayerEntity, PlayerEntity> REQUESTS = new HashMap<>();
    private static final Object2LongMap<PlayerEntity> REQUEST_TIME = new Object2LongLinkedOpenHashMap<>();
    private static final long EXPIRE_TIME = (long) 60 * 1000;

    public static void register() {
        CommandRegistrationEvent.EVENT.register((dispatcher, access, env) -> dispatcher.register(CommandManager.literal("trade")
                .requires(ServerCommandSource::isExecutedByPlayer)
                .then(CommandManager.argument("target", EntityArgumentType.player())
                        .executes(ctx -> {
                            PlayerEntity self = ctx.getSource().getPlayerOrThrow(), target = EntityArgumentType.getPlayer(ctx, "target");
                            if (self.squaredDistanceTo(target) > 5 * 5) {
                                self.sendMessage(Text.translatable("command.not_enough_economy.trade_too_far"));
                                return 1;
                            }
                            if (REQUESTS.get(target) == self) {
                                PlayerExchangeHolder.launchTrade(self, target);
                                REQUESTS.remove(self);
                                REQUESTS.remove(target);
                                return 1;
                            }
                            REQUESTS.put(self, target);
                            REQUEST_TIME.put(self, System.currentTimeMillis());
                            self.sendMessage(Text.translatable("command.not_enough_economy.trade_request_sent"));
                            target.sendMessage(Text.translatable("command.not_enough_economy.trade_request", self.getDisplayName()).fillStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/trade " + self.getGameProfile().getName()))));
                            return 1;
                        })
                )));
        TickEvent.SERVER_POST.register(server -> {
            List<PlayerEntity> forRemoval = new LinkedList<>();
            long cur = System.currentTimeMillis();
            for (Object2LongMap.Entry<PlayerEntity> entry : REQUEST_TIME.object2LongEntrySet())
                if (entry.getLongValue() + EXPIRE_TIME < cur)
                    forRemoval.add(entry.getKey());
            forRemoval.forEach(REQUESTS::remove);
            forRemoval.forEach(REQUEST_TIME::removeLong);
        });
    }
}
