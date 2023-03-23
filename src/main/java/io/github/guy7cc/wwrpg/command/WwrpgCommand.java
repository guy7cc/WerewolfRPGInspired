package io.github.guy7cc.wwrpg.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.guy7cc.wwrpg.core.GameManager;
import io.github.guy7cc.wwrpg.core.SettingsModifier;
import io.github.guy7cc.wwrpg.core.werewolf.WerewolfGame;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fml.loading.FMLLoader;

public class WwrpgCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        LiteralArgumentBuilder<CommandSourceStack> wwrpg = Commands.literal("wwrpg");
        wwrpg.then(Commands.literal("abort")
                .executes(ctx -> abort(ctx.getSource()))
        );
        if(!FMLLoader.isProduction()){
            wwrpg.then(Commands.literal("debug")
                    .then(Commands.literal("getGame")
                            .executes(ctx -> getGame(ctx.getSource()))
                    )
                    .then(Commands.literal("forceStart")
                            .then(Commands.argument("value", BoolArgumentType.bool())
                                    .executes(ctx -> forceStart(ctx.getSource(), BoolArgumentType.getBool(ctx, "value")))
                            )
                    )
                    .then(Commands.literal("dontFinish")
                            .then(Commands.argument("value", BoolArgumentType.bool())
                                    .executes(ctx -> dontFinish(ctx.getSource(), BoolArgumentType.getBool(ctx, "value")))
                            )
                    )
            );
        }
        dispatcher.register(wwrpg);
    }

    private static int abort(CommandSourceStack source) throws CommandSyntaxException{
        if(GameManager.apply(WerewolfGame.class, WerewolfGame::abort)){
            source.getServer().getPlayerList().getPlayers().forEach(p -> p.displayClientMessage(Component.translatable("wwrpg.game.aborted").withStyle(ChatFormatting.RED), false));
            return 1;
        } else {
            source.sendFailure(Component.translatable("wwrpg.game.abortionFailure"));
            return 0;
        }
    }

    private static int getGame(CommandSourceStack source) throws CommandSyntaxException {
        source.sendSuccess(Component.literal(GameManager.getGameAsString()), true);
        return 1;
    }

    private static int forceStart(CommandSourceStack source, boolean forceStart){
        SettingsModifier.forceStart = forceStart;
        source.sendSuccess(Component.literal("Set forceStart " + forceStart + "."), true);
        return 1;
    }

    private static int dontFinish(CommandSourceStack source, boolean dontFinish){
        SettingsModifier.dontFinish = dontFinish;
        source.sendSuccess(Component.literal("Set dontFinish " + dontFinish + "."), true);
        return 1;
    }
}
