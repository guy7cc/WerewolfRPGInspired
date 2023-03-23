package io.github.guy7cc.wwrpg.core.werewolf.phase;

import io.github.guy7cc.wwrpg.core.BasePhase;
import io.github.guy7cc.wwrpg.core.werewolf.WerewolfGame;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

public class AbortedPhase extends WerewolfGamePhase {
    public final Context ctx;

    public AbortedPhase(WerewolfGame game, Context ctx) {
        super(game);
        this.ctx = ctx;
    }

    @Override
    public BasePhase tick() {
        ctx.showMessage();
        return this;
    }

    @Override
    public boolean finished() {
        return true;
    }

    public static class Context {
        private ServerPlayer sender;
        private Reason reason;

        private Context(ServerPlayer sender, Reason reason){
            this.sender = sender;
            this.reason = reason;
        }

        public static Context invalidSettings(ServerPlayer sender){
            return new Context(sender, Reason.INVALID_SETTINGS);
        }

        public static Context abortedByOp(){
            return new Context(null, Reason.ABORTED_BY_OP);
        }

        public void showMessage(){
            switch(reason){
                case INVALID_SETTINGS:
                    sender.displayClientMessage(Component.translatable("wwrpg.message.invalidSettings").withStyle(Style.EMPTY.withColor(ChatFormatting.RED)), false);
                    break;
                case ABORTED_BY_OP:
                    break;
            }
        }

        public ServerPlayer getSender(){
            return sender;
        }


    }

    public enum Reason{
        INVALID_SETTINGS,
        ABORTED_BY_OP
    }
}
