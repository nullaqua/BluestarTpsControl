package me.lanzhi.bluestartpscontrol;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Method;

public final class BluestarTpsControl extends JavaPlugin
{
    private Class<?> minecraftServerClass;
    private Method setmspt;
    private Method getmspt;
    private BukkitTask task;
    private final BluestarTpsControlApi api;

    public BluestarTpsControl()
    {
        try
        {
            this.minecraftServerClass=Class.forName("net.minecraft.server.MinecraftServer");
            this.setmspt=minecraftServerClass.getMethod("setmspt",long.class);
            this.getmspt=minecraftServerClass.getMethod("getmspt");
        }
        catch (Throwable e)
        {
            Bukkit.getLogger().warning("[BluestarTpsControl]错误!未找到类或方法,请反馈此bug谢谢");
        }
        api=new BluestarTpsControlApi(this);
    }

    @Override
    public void onEnable()
    {
        saveDefaultConfig();
        try
        {
            getApi().setmspt(getConfig().getInt("mspt"));
        }
        catch (MsptIllegalException e)
        {
            Bukkit.getLogger().warning(ChatColor.RED+"错误!配置文件中的mspt不合法");
        }
        getCommand("settps").setExecutor(new maincommand(this));
        new Metrics(this,14894);
        task=new checkUpdata(this).runTaskTimerAsynchronously(this,0,72000);
        Bukkit.getServicesManager().register(BluestarTpsControlAPI.class,api,this,ServicePriority.Normal);
        System.out.println("BluestarTpsControl已启用");
    }

    @Override
    public void onDisable()
    {
        task.cancel();
        saveConfig();
        Bukkit.getServicesManager().unregisterAll(this);
        System.out.println("BluestarTpsControl已禁用");
    }

    Method getGetmspt()
    {
        return getmspt;
    }

    Method getSetmspt()
    {
        return setmspt;
    }

    public BluestarTpsControlApi getApi()
    {
        return api;
    }
}
