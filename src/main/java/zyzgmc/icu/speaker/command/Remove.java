package zyzgmc.icu.speaker.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import zyzgmc.icu.speaker.Speaker;
import zyzgmc.icu.speaker.config.Config;

import java.io.IOException;

public class Remove implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        try {
            Config.load();
        }catch (IOException e){
            e.printStackTrace();
        }

        String Name = args.<String>getOne("公告名称").get();

        if(Config.rootNode.getNode("All",Name).isVirtual()){
            src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(String.format("&c%s &a不存在，请检查名称", Name)));
        }else {
            Config.rootNode.getNode("All").removeChild(Name);
            src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(String.format("&c%s &a已移除", Name)));
            try {
                Config.save();
                Config.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return CommandResult.success();
    }
    public static CommandSpec build(){
        return CommandSpec.builder()
                .description(Text.of("移除一个公告"))
                .permission("speaker.command.remove")
                .executor(new Remove())
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.withSuggestions(GenericArguments.string(Text.of("公告名称")),Config.nameCompletion))
                )
                .build();
    }
}
