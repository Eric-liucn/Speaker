package zyzgmc.icu.speaker.textBuild;



import me.rojo8399.placeholderapi.PlaceholderService;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.BossBar;
import org.spongepowered.api.boss.BossBarColors;
import org.spongepowered.api.boss.BossBarOverlays;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Builder;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.text.title.Title;
import zyzgmc.icu.speaker.Speaker;
import zyzgmc.icu.speaker.config.Config;

import javax.swing.plaf.synth.SynthTableUI;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static zyzgmc.icu.speaker.Speaker.economyService;


public class TextBuilder {

    public static String content,hoverText,url,cmd;
    

    public static void builderAndSender(String name) throws Exception {
        content = Config.rootNode.getNode("All", name, "Content").getString();
        hoverText = Config.rootNode.getNode("All", name, "Hover").getString();
        url = Config.rootNode.getNode().getNode("All", name, "Url").getString();
        cmd = Config.rootNode.getNode("All", name, "Cmd").getString();


        if(!Sponge.getServer().getOnlinePlayers().isEmpty()) {
            for (Player player : Sponge.getServer().getOnlinePlayers()) {

                Builder build = Text.builder();
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

        }

    }

    public static void titleBuilder(String name) throws Exception {
        content = Config.rootNode.getNode("All", name, "Content").getString();
        hoverText = Config.rootNode.getNode("All", name, "Hover").getString();
        Integer titleStay = Config.rootNode.getNode("All",name,"Setting","Title","持续时间").getInt();

        if(!Sponge.getServer().getOnlinePlayers().isEmpty()){
            for (Player player:Sponge.getServer().getOnlinePlayers()
                 ) {


                Title.Builder builder = Title.builder();
                builder.title(
                        Text.builder().append(
                                replaceDeal(content,player)
                        ).build()
                );

                builder.fadeIn(Config.rootNode.getNode("All",name,"Setting","Title","淡入时间").getInt());
                builder.fadeOut(Config.rootNode.getNode("All",name,"Setting","Title","淡出时间").getInt());
                builder.stay(titleStay);

                builder.subtitle(
                        Text.builder().append(
                                replaceDeal(hoverText,player)
                        ).build()
                );


                player.sendTitle(builder.build());


            }
        }
    }





    public static void bossBarBuilder(String name) throws Exception{
        content = Config.rootNode.getNode("All", name, "Content").getString();
        float stayTime = Config.rootNode.getNode("All",name,"Setting","Boss","持续时间").getFloat();
        String color = Config.rootNode.getNode("All",name,"Setting","Boss","颜色").getString();
        if(!Sponge.getServer().getOnlinePlayers().isEmpty()){
            for (Player player:Sponge.getServer().getOnlinePlayers()
                 ) {
                ServerBossBar bar=ServerBossBar.builder()
                        .percent(1.0f)
                        .overlay(BossBarOverlays.PROGRESS)
                        .name(Text.of("None"))
                        .color(BossBarColors.PURPLE)
                        .visible(false)
                        .build();
                bar.setName(
                        replaceDeal(content,player)
                );
                bar.addPlayer(player);
                bar.setVisible(true);
                float per = 1f;
                float stayTimePer = (1/stayTime);
                for (float i = stayTime; i > 0 ; i--) {
                    per = per -stayTimePer;
                    if(per>0) {
                        Thread.sleep(1000);
                        bar.setPercent(per);
                    }else {
                        bar.setVisible(false);
                        break;
                    }
                }
            }
        }
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
