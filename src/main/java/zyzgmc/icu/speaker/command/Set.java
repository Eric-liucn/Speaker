package zyzgmc.icu.speaker.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import zyzgmc.icu.speaker.command.childCommandofSet.contentChange;
import zyzgmc.icu.speaker.command.childCommandofSet.fixTimeChange;
import zyzgmc.icu.speaker.command.childCommandofSet.intervalChange;
import zyzgmc.icu.speaker.config.Config;
import zyzgmc.icu.speaker.command.childCommandofSet.modeChange;

public class Set implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&c请使用子命令: content/fix/interval/mode"));
        return CommandResult.success();
    }

    public static CommandSpec build(){
        return CommandSpec.builder()
                .description(Text.of("设置公告信息"))
                .permission("speaker.command.set")
                .child(modeChange.build(),"mode")
                .child(contentChange.build(),"content")
                .child(intervalChange.build(),"interval")
                .child(fixTimeChange.build(),"fix")
                .executor(new Set())
                .build();
    }
}
