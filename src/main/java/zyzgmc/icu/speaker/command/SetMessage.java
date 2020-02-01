package zyzgmc.icu.speaker.command;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import zyzgmc.icu.speaker.config.Config;

import java.util.Optional;

public class SetMessage implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(args.getOne("序号").isPresent() && args.getOne("公告内容").isPresent()) {

            String index = args.<String>getOne("序号").get();
            String message = args.<String>getOne("公告内容").get();
            src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(String.format("输入这个指令将会修改名称为%s的公告内容设置为%s", index,message)));
            System.out.println( Config.rootNode.getNode("All").getChildrenList());

        }
        else {
            src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&l&c输入有误"));
        }
        return CommandResult.success();
    }

    public static CommandSpec build(){
        return CommandSpec.builder()
                .description(Text.of("设置公告信息"))
                .permission("speaker.command.set")
                .arguments(GenericArguments.seq(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("名称"))),
                        GenericArguments.remainingJoinedStrings(Text.of("公告内容"))
                        )
                )
                .executor(new SetMessage())
                .build();
    }
}
