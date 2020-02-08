package zyzgmc.icu.speaker.tasks;

import me.rojo8399.placeholderapi.PlaceholderService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.BossBarColors;
import org.spongepowered.api.boss.BossBarOverlays;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.text.title.Title;
import zyzgmc.icu.speaker.config.Config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import static zyzgmc.icu.speaker.Speaker.economyService;

public class JoinTask {

    public static String content,hoverText,url,cmd;


    public static void builderAndSender(String name,Player player) throws Exception {
        content = Config.rootNode.getNode("All", name, "Content").getString();
        hoverText = Config.rootNode.getNode("All", name, "Hover").getString();
        url = Config.rootNode.getNode().getNode("All", name, "Url").getString();
        cmd = Config.rootNode.getNode("All", name, "Cmd").getString();

        Text.Builder build = Text.builder();
        build.append(
                replaceDeal(content, player)
        );

        if (!Objects.equals(hoverText, "")) {
            build.onHover(TextActions.showText(
                    replaceDeal(hoverText, player)
                    )
            );
        }

        if (!Objects.equals(url, "None")) {
            URL thisUrl = null;

            try {
                thisUrl = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Sponge.getServer().getConsole().sendMessage(
                        TextSerializers.FORMATTING_CODE.deserialize(
                                "&c公告 &e%s &c的 &eUrl &c有误，例：https://www.baidu.com"
                        )
                );
            }

        build.onClick(TextActions.openUrl(thisUrl));

        }

        if (!Objects.equals(cmd, "None")) {
            if (!cmd.startsWith("/")) {
                build.onClick(TextActions.runCommand(replaceDeal(cmd, player).toPlain()));
            } else {
                build.onClick(TextActions.runCommand(replaceDeal(cmd, player).toPlain()));
            }
        }

        player.sendMessage(build.build());

    }

    public static void titleBuilder(String name, Player player) throws Exception {
        content = Config.rootNode.getNode("All", name, "Content").getString();
        hoverText = Config.rootNode.getNode("All", name, "Hover").getString();
        Integer titleStay = Config.rootNode.getNode("All",name,"Setting","Title","持续时间").getInt();

        Title.Builder builder = Title.builder();
        builder.title(
                Text.builder().append(
                        replaceDeal(content,player)
                ).build()
        );

        builder.fadeIn(Config.rootNode.getNode("All",name,"Setting","Title","淡入时间").getInt()*20);
        builder.fadeOut(Config.rootNode.getNode("All",name,"Setting","Title","淡出时间").getInt()*20);
        builder.stay(titleStay*20);

        builder.subtitle(
                Text.builder().append(
                        replaceDeal(hoverText,player)
                ).build()
        );


        player.sendTitle(builder.build());

    }





    public static void bossBarBuilder(String name,Player player) throws Exception{
        content = Config.rootNode.getNode("All", name, "Content").getString();
        float stayTime = Config.rootNode.getNode("All",name,"Setting","Boss","持续时间").getFloat();
        String color = Config.rootNode.getNode("All",name,"Setting","Boss","颜色").getString();
        ServerBossBar bar=ServerBossBar.builder()
                .percent(1.0f)
                .color(BossBarColors.PURPLE)
                .overlay(BossBarOverlays.PROGRESS)
                .name(Text.of("None"))
                .visible(false)
                .build();

        if(color.equals("BLUE")){
            bar.setColor(BossBarColors.BLUE);
        }else if (color.equals("YELLOW")){
            bar.setColor(BossBarColors.YELLOW);
        }else if (color.equals("WHITE")){
            bar.setColor(BossBarColors.WHITE);
        }else if (color.equals("GREEN")){
            bar.setColor(BossBarColors.GREEN);
        }else if(color.equals("RED")){
            bar.setColor(BossBarColors.RED);
        }else if (color.equals("PINK")){
            bar.setColor(BossBarColors.PINK);
        }else {
            bar.setColor(BossBarColors.PURPLE);
        }

        bar.setName(
                replaceDeal(content,player)
        );
        bar.addPlayer(player);
        bar.setVisible(true);
        final float[] per = {1f};
        float stayTimePer = (1/stayTime);
        Timer timer=new Timer();
        timer.scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        per[0] = per[0] - (stayTimePer/10);
                        if (per[0] <= 0) {
                            bar.setVisible(false);
                            this.cancel();
                        } else {
                            bar.setPercent(per[0]);
                        }
                    }
                },0,100
        );

    }


    public static Text replaceDeal(String string, Player player) throws Exception {
        String str = string;
        str=str.replace("{Player}", player.getName());
        str=str.replace("{World}",player.getWorld().getName());
        str=str.replace("{GameMode}",player.getGameModeData().type().get().toString());
        str=str.replace("{Health}",player.getHealthData().health().get().toString());
        str=str.replace("{Food}",player.getFoodData().foodLevel().get().toString());
        Optional<EconomyService> serviceOpt = Sponge.getServiceManager().provide(EconomyService.class);
        if (serviceOpt.isPresent()) {
            Optional<UniqueAccount> uOpt = economyService.getOrCreateAccount(player.getUniqueId());
            UniqueAccount acc = uOpt.get();
            str=str.replace("{Balance}", acc.getDefaultBalance(economyService.getDefaultCurrency()).toString());
        }
        Text text = Text.of(str);

        if(Sponge.getPluginManager().isLoaded("placeholderapi")) {
            PlaceholderService placeholderService = (PlaceholderService) Sponge.getServiceManager().provideUnchecked(PlaceholderService.class);
            text = placeholderService.replaceSourcePlaceholders(str, player);
            return text;
        }else {
            text = TextSerializers.FORMATTING_CODE.deserialize(str);
            return text;
        }
    }
}
