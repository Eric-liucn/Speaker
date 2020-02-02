package zyzgmc.icu.speaker.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.ArrayList;
import java.util.List;

public class Help implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        List<Text> help = new ArrayList<>();
        help.add(TextSerializers.FORMATTING_CODE.deserialize("&espeaker add &d---> &b添加一条公告 "));
        help.add(TextSerializers.FORMATTING_CODE.deserialize("&espeaker remove &d---> &b删除一条公告 "));
        help.add(TextSerializers.FORMATTING_CODE.deserialize("&espeaker show &d---> &b显示一条公告的详情 "));
        help.add(TextSerializers.FORMATTING_CODE.deserialize("&espeaker list &d---> &b显示所有已经公告 "));
        help.add(TextSerializers.FORMATTING_CODE.deserialize("&espeaker set &d---> &b设置一条公告 "));
        help.add(TextSerializers.FORMATTING_CODE.deserialize("&espeaker reload &d---> &b重制插件 "));

        PaginationList.builder()
                .contents(help)
                .title(Text.of(TextColors.YELLOW,"Speaker帮助信息"))
                .padding(Text.of(TextColors.GREEN,"="))
                .sendTo(src);


        return CommandResult.success();
    }

    public static CommandSpec build(){
        return CommandSpec.builder()
                .description(Text.of("显示帮助信息"))
                .permission("speaker.command.base")
                .executor(new Help())
                .build();
    }
}
