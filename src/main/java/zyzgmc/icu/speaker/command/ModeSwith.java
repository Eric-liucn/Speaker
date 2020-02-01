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
import java.util.HashMap;
import java.util.Map;


public class ModeSwith implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String name = args.<String>getOne("公告名称").get();
        int modeCode = args.<Integer>getOne("fix(固定)/interval(间隔)").get();
        src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(String.format("输入这个指令将会切换公告%s的模式为%d",name,modeCode)));
        return CommandResult.success();
    }


    public static CommandSpec build(){

        Map<String, Integer> mapMode = new HashMap<>();
        mapMode.put("fix",1);
        mapMode.put("interval",2);

        Map<String, String> mapNames = new HashMap<String, String>();
        mapNames.put("first","first");
        mapNames.put("seconde","second");


        return CommandSpec.builder()
                .description(Text.of("设置插件工作模式：定时/间隔"))
                .permission("speaker.command.mode")
                .arguments(
                        GenericArguments.seq(
                                GenericArguments.onlyOne(GenericArguments.choices(Text.of("公告名称"),mapNames)),
                                GenericArguments.onlyOne(GenericArguments.choices(Text.of("fix(固定)/interval(间隔)"), mapMode ))
                        )
                )
                .executor(new ModeSwith())
                .build();
    }
}
