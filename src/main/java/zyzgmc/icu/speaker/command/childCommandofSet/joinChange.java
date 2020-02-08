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

public class joinChange implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        String name = args.<String>getOne("公告名称").get();
        Boolean status = args.<Boolean>getOne("true/false").get();
        if(!Config.rootNode.getNode("All",name).isVirtual()){
            Config.rootNode.getNode("All",name,"Join").setValue(status);
            try{
                Config.save();
                Config.load();
            }catch (IOException e){
                e.printStackTrace();
            }
            src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(String.format("&a已将公告 &e%s &a的进服显示状态改为 &e%b",name,status)));
        }else {
            src.sendMessage(Text.of(TextColors.RED,"该公告不存在，请检查名称！"));
        }

        return CommandResult.success();
    }

    public static CommandSpec build(){
        return CommandSpec.builder()
                .description(Text.of("改变公告是否为加入游戏显示：开启/关闭"))
                .arguments(
                        GenericArguments.seq(
                                GenericArguments.onlyOne(
                                        GenericArguments.withSuggestions(
                                                GenericArguments.string(Text.of("公告名称")),
                                                Config.nameCompletion
                                        )
                                ),
                                GenericArguments.onlyOne(
                                        GenericArguments.bool(Text.of("true/false"))
                                )
                        )
                )
                .executor(new joinChange())
                .permission("speaker.command.set.join")
                .build();
    }
}
