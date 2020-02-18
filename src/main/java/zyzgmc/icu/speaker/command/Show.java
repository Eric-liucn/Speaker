package zyzgmc.icu.speaker.command;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;
import zyzgmc.icu.speaker.config.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Show implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        String name = args.<String>getOne("公告名称").get();

        if(Config.rootNode.getNode("All",name).isVirtual()){
            src.sendMessage(Text.of(TextColors.RED,"该公告不存在"));
        }else {

            boolean enableThis = Config.rootNode.getNode("All", name, "Enable").getBoolean();

            String enable;
            if (enableThis) {
                enable = "开启";
            } else {
                enable = "关闭";
            }

            String mode = Config.rootNode.getNode("All", name, "ModeCode").getString();
            String modeString = (String) modeJudge(mode);

            String contentThis = Config.rootNode.getNode("All", name, "Content").getString();
            String intervalThis = Config.rootNode.getNode("All", name, "Interval").getString();
            String fixTimeThis = "";


            for (Object i : (List) Config.rootNode.getNode("All", name, "FixTime").getValue()
            ) {
                String l = (String) i;
                fixTimeThis = fixTimeThis + " " + l;
            }

            String display = Config.rootNode.getNode("All",name,"Display").getString();
            Integer tiTleStay = Config.rootNode.getNode("All",name,"Setting","Title","持续时间").getInt();
            Integer tiTleFadeIn = Config.rootNode.getNode("All",name,"Setting","Title","淡入时间").getInt();
            Integer tiTleFadeout = Config.rootNode.getNode("All",name,"Setting","Title","淡出时间").getInt();
            Integer bossStay = Config.rootNode.getNode("All",name,"Setting","Boss","持续时间").getInt();
            String bossColor = Config.rootNode.getNode("All",name,"Setting","Boss","颜色").getString();
            boolean join = Config.rootNode.getNode("All",name,"Setting","Boss","颜色").getBoolean();

            if (mode.equals("fix")) {
                List<Text> con = new ArrayList<>();

                con.add(TextSerializers.FORMATTING_CODE.deserialize(String.format("&b公告名称: &d%s", name)));
                con.add(TextSerializers.FORMATTING_CODE.deserialize(String.format("&b公告状态: &d%s", enable)));
                con.add(TextSerializers.FORMATTING_CODE.deserialize(String.format("&b公告模式: &d%s", modeString)));
                con.add(TextSerializers.FORMATTING_CODE.deserialize(String.format("&b公告时点: &d%s", fixTimeThis)));
                con.add(TextSerializers.FORMATTING_CODE.deserialize(String.format("&b公告内容: %s", contentThis)));
                con.add(TextSerializers.FORMATTING_CODE.deserialize(String.format("&b公告显示模式: &d%s",display)));
                con.add(TextSerializers.FORMATTING_CODE.deserialize(String.format("&bTitle模式显示持续时间: &d%d &e秒",tiTleStay)));
                con.add(TextSerializers.FORMATTING_CODE.deserialize(String.format("&bTitle模式显示淡入时间: &d%d &e秒",tiTleFadeIn)));
                con.add(TextSerializers.FORMATTING_CODE.deserialize(String.format("&bTitle模式显示淡出时间: &d%d &e秒",tiTleFadeout)));
                con.add(TextSerializers.FORMATTING_CODE.deserialize(String.format("&bBoss模式显示持续时间: &d%d &e秒",bossStay)));
                con.add(TextSerializers.FORMATTING_CODE.deserialize(String.format("&bBoss模式血条颜色: &d%s",bossColor)));
                con.add(TextSerializers.FORMATTING_CODE.deserialize(String.format("&b进服显示: &d%b",join)));

                PaginationList.builder()
                        .title(Text.of(TextColors.YELLOW, "公告信息"))
                        .padding(Text.of(TextColors.GREEN, "="))
                        .contents(con)
                        .sendTo(src);

            } else if(mode.equals("interval")){
                List<Text> cont = new ArrayList<>();
                cont.add(TextSerializers.FORMATTING_CODE.deserialize(String.format("&b公告名称: &d%s", name)));
                cont.add(TextSerializers.FORMATTING_CODE.deserialize(String.format("&b公告状态: &d%s", enable)));
                cont.add(TextSerializers.FORMATTING_CODE.deserialize(String.format("&b公告模式: &d%s", modeString)));
                cont.add(TextSerializers.FORMATTING_CODE.deserialize(String.format("&b公告间隔: &d%s", intervalThis)));
                cont.add(TextSerializers.FORMATTING_CODE.deserialize(String.format("&b公告内容: &d%s", contentThis)));
                cont.add(TextSerializers.FORMATTING_CODE.deserialize(String.format("&b公告显示模式: &d%s",display)));
                cont.add(TextSerializers.FORMATTING_CODE.deserialize(String.format("&bTitle模式显示持续时间: &d%d &e秒",tiTleStay)));
                cont.add(TextSerializers.FORMATTING_CODE.deserialize(String.format("&bTitle模式显示淡入时间: &d%d &e秒",tiTleFadeIn)));
                cont.add(TextSerializers.FORMATTING_CODE.deserialize(String.format("&bTitle模式显示淡出时间: &d%d &e秒",tiTleFadeout)));
                cont.add(TextSerializers.FORMATTING_CODE.deserialize(String.format("&bBoss模式显示持续时间: &d%d &e秒",bossStay)));
                cont.add(TextSerializers.FORMATTING_CODE.deserialize(String.format("&bBoss模式血条颜色: &d%s",bossColor)));
                cont.add(TextSerializers.FORMATTING_CODE.deserialize(String.format("&b进服显示: &d%b",join)));

                PaginationList.builder()
                        .title(Text.of(TextColors.YELLOW, "公告信息"))
                        .padding(Text.of(TextColors.GREEN, "="))
                        .contents(cont)
                        .sendTo(src);
            }
        }

        return CommandResult.success();
    }

    public static String fixMode = "固定时间点模式";
    public static String intervalMode = "间隔时间段模式";
    public static String errMode = "配置错误，无法读取";


    public static Object modeJudge(String  i){

        if (i.equals("fix")){
            return fixMode;
        }
        else if (i.equals("interval")){
            return intervalMode;
        }
        return errMode;
    }


    public static CommandSpec build(){

        return CommandSpec.builder()
                .description(Text.of("查看一条已经创建的广播"))
                .permission("speaker.command.show")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.withSuggestions(GenericArguments.string(Text.of("公告名称")),Config.nameCompletion))
                )
                .executor(new Show())
                .build();
    }
}
