package zyzgmc.icu.speaker.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

public class Vmessage implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        PaginationList.builder()
                .title(Text.of(TextColors.LIGHT_PURPLE,"SPEAKER"))
                .contents(TextSerializers.FORMATTING_CODE.deserialize("&6使用&b /speaker help &6或&b /spk help &6查看帮助信息，当前版本：&d 1.1(Beta)"))
                .padding(Text.of(TextColors.GREEN,"="))
                .sendTo(src);

        return CommandResult.success();
    }
}
