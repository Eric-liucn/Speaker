package zyzgmc.icu.speaker.command.childCommandofSet;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;
import zyzgmc.icu.speaker.config.Config;

import java.io.IOException;
import java.util.regex.Pattern;

public class urlChange implements CommandExecutor {

    public static boolean urlIsValid(String url){
        return Pattern.matches("[a-zA-z]+://[^\\s]*", url);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        String name = args.<String>getOne("公告名称").get();
        String url = args.<String>getOne("URL").get();



        if(urlIsValid(url)) {

            if (!Config.rootNode.getNode("All", name).isVirtual()) {
                Config.rootNode.getNode("All", name, "Url").setValue(url);
                try {
                    Config.save();
                    Config.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(String.format("&a已将公告 &e%s &a的点击链接改为 &e%s", name, url)));
            } else {
                src.sendMessage(Text.of(TextColors.RED, "该公告不存在，请检查名称！"));
            }
        }else {
            src.sendMessage(Text.of(TextColors.RED, "URL格式不正确，请检查，例：https://www.baidu.com"));
        }


        return CommandResult.success();
    }
    public static CommandSpec build(){
        return CommandSpec.builder()
                .description(Text.of("修改指定公告的URL信息"))
                .permission("speaker.command.set.url")
                .arguments(
                        GenericArguments.seq(
                                GenericArguments.onlyOne(
                                        GenericArguments.withSuggestions(
                                                GenericArguments.string(Text.of("公告名称")), Config.nameCompletion
                                        )
                                ),
                                GenericArguments.remainingJoinedStrings(Text.of("URL"))
                        )
                )
                .executor(new urlChange())
                .build();
    }
}
