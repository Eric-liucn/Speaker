package zyzgmc.icu.speaker.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import zyzgmc.icu.speaker.config.Config;

import java.io.IOException;

public class Reload implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        try {
            Config.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        src.sendMessage(Text.of(TextColors.GREEN,"插件重载成功"));

        return CommandResult.success();
    }
    public static CommandSpec build(){
        return CommandSpec.builder()
                .description(Text.of("重载插件"))
                .permission("speaker.command.reload")
                .executor(new Reload())
                .build();
    }
}
