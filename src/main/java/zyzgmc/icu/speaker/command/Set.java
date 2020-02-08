package zyzgmc.icu.speaker.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import zyzgmc.icu.speaker.command.childCommandofSet.*;

public class Set implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&c请使用子命令: content/fix/interval/mode/cmd/hover/display/join/boss/title/enable"));
        return CommandResult.success();
    }

    public static CommandSpec build(){
        return CommandSpec.builder()
                .description(Text.of("设置公告信息"))
                .child(modeChange.build(),"mode")
                .child(contentChange.build(),"content")
                .child(intervalChange.build(),"interval")
                .child(fixTimeChange.build(),"fix")
                .child(enableChange.build(),"enable")
                .child(hoverChange.build(),"hover")
                .child(urlChange.build(),"url")
                .child(cmdChange.build(),"cmd")
                .child(displayChange.build(),"display")
                .child(tiTileChange.build(),"title")
                .child(bossChange.build(),"boss")
                .child(joinChange.build(),"join")
                .executor(new Set())
                .build();
    }
}
