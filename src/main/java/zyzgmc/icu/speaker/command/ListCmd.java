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
import zyzgmc.icu.speaker.config.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListCmd implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        try {
            Config.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Text> Names = new ArrayList<>();
        for (Object name: Config.rootNode.getNode("All").getChildrenMap().keySet()
             ) {
            String oneName =(String) name;
            Names.add(TextSerializers.FORMATTING_CODE.deserialize("&e"+oneName));
            
        }
        PaginationList.builder()
                .contents(Names)
                .title(Text.of(TextColors.YELLOW,"所有已创建的公告"))
                .padding(Text.of(TextColors.GREEN,"="))
                .linesPerPage(10)
                .sendTo(src);


        return CommandResult.success();
    }

    public static CommandSpec build(){
        return CommandSpec.builder()
                .description(Text.of("显示所有已创建的广播"))
                .permission("speaker.command.list")
                .arguments()
                .executor(new ListCmd())
                .build();
    }
}
