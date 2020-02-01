package zyzgmc.icu.speaker;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.serializer.TextSerializers;

import java.util.Optional;

public class SetMessage implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(args.getOne("<序号>").isPresent() && args.getOne("<公告内容>").isPresent()) {

            Integer index = args.<Integer>getOne("序号").get();
            String message = args.<String>getOne("公告内容").get();
            src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(String.format("输入这个指令将会修改序号为%d的公告内容为%s", index,message)));
        }
        else {
            src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize("&l&c输入有误"));
        }
        return CommandResult.success();
    }
}
