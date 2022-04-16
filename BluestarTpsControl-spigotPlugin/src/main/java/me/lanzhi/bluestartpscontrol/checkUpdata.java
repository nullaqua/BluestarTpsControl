package me.lanzhi.bluestartpscontrol;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class checkUpdata extends BukkitRunnable
{
    @Override
    public void run()
    {
        System.out.println("BluestarTpsControl检测更新...");
        String upDataUrl="https://api.github.com/repos/lanzhi6/BluestarTpsControl/releases/latest";
        String result = "";
        BufferedReader in = null;
        try
        {
            URL url = new URL(upDataUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null)
            {
                result=result+line;
            }
        }
        catch (Exception e)
        {
            Bukkit.getLogger().warning("[BluestarTpsControl]无法获取最新插件版本 code:0X01");
            return;
        }
        Matcher matcher = Pattern.compile("\"tag_name\":\"[0-9.]+\"").matcher(result);
        String latestVerMessage="";
        if (matcher.find())
        {
            latestVerMessage = matcher.group();
        }
        else
        {
            Bukkit.getLogger().warning("[BluestarTpsControl]无法获取最新插件版本 code:0X02");
            return;
        }
        Matcher matcher1=Pattern.compile("[0-9.]+").matcher(latestVerMessage);
        String latestVer="";
        if (matcher1.find())
        {
            latestVer = matcher1.group();
        }
        else
        {
            Bukkit.getLogger().warning("[BluestarTpsControl]无法获取最新插件版本 code:0X03");
            return;
        }
        if (!latestVer.equals(BluestarTpsControl.plugin.getDescription().getVersion()))
        {
            Bukkit.getLogger().warning(ChatColor.RED + "---------------------------");
            Bukkit.getLogger().warning(" ");
            Bukkit.getLogger().warning("[BluestarTpsControl]");
            Bukkit.getLogger().warning("发现新版本: "+latestVer);
            Bukkit.getLogger().warning("请尽快更新!");
            Bukkit.getLogger().warning(" ");
            Bukkit.getLogger().warning(ChatColor.RED + "---------------------------");
        }
        else
        {
            System.out.println("BluestarTpsControl已是最新版本!");
        }
    }
}
