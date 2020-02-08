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
import java.util.Optional;

public class bossChange implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        String name = args.<String>getOne("公告名称").get();
        Integer stay = args.<Integer>getOne("持续时间").get();
        String color = args.<String>getOne("颜色").get();

        if (!Config.rootNode.getNode("All",name).isVirtual()){
            Config.rootNode.getNode("All",name,"Setting","Boss","持续时间").setValue(stay);
            Config.rootNode.getNode("All",name,"Setting","Boss","颜色").setValue(color);
            try{
                Config.save();
                Config.load();
            }catch (IOException e){
                e.printStackTrace();
            }

            src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(String.format("&a已将公告 &e%s &a的血条公告模式设定改为 &e%d 秒  &d%s",name,stay,color)));
        }else {
            src.sendMessage(Text.of(TextColors.RED,"该公告不存在，请检查名称！"));
        }

        return CommandResult.success();
    }

    public static CommandSpec build(){

        Map<String, String> colorMap = new HashMap<String, String >();
        colorMap.put("紫色","PURPLE");
        colorMap.put("黄色","YELLOW");
        colorMap.put("红色","RED");
        colorMap.put("蓝色","BLUE");
        colorMap.put("粉色","PINK");
        colorMap.put("绿色","GREEN");
        colorMap.put("白色","WHITE");

        return CommandSpec.builder()
                .description(Text.of("改变公告的boss模式设定"))
                .arguments(
                        GenericArguments.onlyOne(
                                GenericArguments.withSuggestions(
                                        GenericArguments.string(Text.of("公告名称")),
                                        Config.nameCompletion
                                )
                        ),
                        GenericArguments.onlyOne(
                                GenericArguments.integer(Text.of("持续时间"))
                        ),
                        GenericArguments.onlyOne(
                                GenericArguments.choices(
                                        Text.of("颜色"),
                                        colorMap
                                )
                        )
                )
                .executor(new bossChange())
                .permission("speaker.command.set.boss")
                .build();

    }
}
