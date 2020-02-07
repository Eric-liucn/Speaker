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
import java.util.HashMap;
import java.util.Map;

public class displayChange implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String name = args.<String>getOne("公告名称").get();
        String display = args.<String>getOne("显示方式").get();
        if(!Config.rootNode.getNode("All",name).isVirtual()){
            Config.rootNode.getNode("All",name,"Display").setValue(display);
            try{
                Config.save();
                Config.load();
            }catch (IOException e){
                e.printStackTrace();
            }
            src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(String.format("&a已将公告 &e%s &a的显示状态改为&e %s",name,display)));
        }else {
            src.sendMessage(Text.of(TextColors.RED,"该公告不存在，请检查名称！"));
        }

        return CommandResult.success();
    }

    public static CommandSpec build(){
        Map<String,String> displayMap = new HashMap<String, String>();
        displayMap.put("normal","normal");
        displayMap.put("title","title");
        displayMap.put("boss","boss");
        return CommandSpec.builder()
                .permission("speaker.command.set.display")
                .description(Text.of("改变改公告的显示方式"))
                .arguments(
                        GenericArguments.seq(
                                GenericArguments.onlyOne(
                                        GenericArguments.withSuggestions(
                                                GenericArguments.string(Text.of("公告名称")),
                                                Config.nameCompletion
                                        )
                                ),
                                GenericArguments.onlyOne(
                                        GenericArguments.choices(
                                                Text.of("显示方式"),
                                                displayMap
                                        )
                                )
                        )
                )
                .executor(new displayChange())
                .build();
    }
}
