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
import java.util.Optional;

public class tiTileChange implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        String name = args.<String>getOne("公告名称").get();
        Integer stay = args.<Integer>getOne("持续时间").get();
        Optional<Integer> fadein = args.<Integer>getOne("淡入时间");
        Optional<Integer> fadeout = args.<Integer>getOne("淡出时间");

        if (!Config.rootNode.getNode("All",name).isVirtual()){
            Config.rootNode.getNode("All",name,"Setting","Title","持续时间").setValue(stay);
            if(fadein.isPresent()){
                Config.rootNode.getNode("All",name,"Setting","Title","淡入时间").setValue(fadein.get());
            }
            if(fadeout.isPresent()){
                Config.rootNode.getNode("All",name,"Setting","Title","淡出时间").setValue(fadeout.get());
            }
            try{
                Config.save();
                Config.load();
            }catch (IOException e){
                e.printStackTrace();
            }

            src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(String.format("&a已将公告 &e%s &a的title公告模式设定改为 持续 &e%d 秒 ",name,stay)));
        }else {
            src.sendMessage(Text.of(TextColors.RED,"该公告不存在，请检查名称！"));
        }


        return CommandResult.success();
    }

    public static CommandSpec build(){
        return CommandSpec.builder()
                .description(Text.of("修改公告的title模式设定"))
                .permission("speaker.command.set.title")
                .arguments(
                        GenericArguments.seq(
                                GenericArguments.onlyOne(
                                        GenericArguments.withSuggestions(
                                                GenericArguments.string(Text.of("公告名称")),
                                                Config.nameCompletion
                                        )
                                ),
                                GenericArguments.onlyOne(
                                        GenericArguments.integer(Text.of("持续时间"))
                                ),
                                GenericArguments.optionalWeak(
                                        GenericArguments.onlyOne(
                                                GenericArguments.integer(Text.of("淡入时间"))
                                        )
                                ),
                                GenericArguments.optionalWeak(
                                        GenericArguments.onlyOne(
                                                GenericArguments.integer(Text.of("淡出时间"))
                                        )
                                )
                        )
                )
                .executor(new tiTileChange())
                .build();
    }
}
