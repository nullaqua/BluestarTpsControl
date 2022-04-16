package me.lanzhi.bluestartpscontrol;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Method;

public final class BluestarTpsControl extends JavaPlugin
{
    public static Class<?> minecraftServerClass;
    public static Method setmspt;
    public static Method getmspt;
    public static Method BluestarTpsControlFormat;
    public static Plugin plugin;
    private BukkitTask task;
    @Override
    public void onEnable()
    {
        System.out.println("BluestarTpsControl已启用");
        plugin = this;
        try
        {
            minecraftServerClass=Class.forName("net.minecraft.server.MinecraftServer");
            setmspt=minecraftServerClass.getMethod("setmspt",long.class);
            getmspt=minecraftServerClass.getMethod("getmspt");
            BluestarTpsControlFormat=Class.forName("org.spigotmc.TicksPerSecondCommand").getMethod("BluestarTpsControlFormat",double.class);
        }
        catch (Throwable e)
        {
            Bukkit.getLogger().warning("[BluestarTpsControl]错误!未找到类或方法,请反馈此bug谢谢");
        }
        getCommand("settps").setExecutor(new maincommand());
        new Metrics(this,14894);
        task = new checkUpdata().runTaskTimerAsynchronously(this,0,12000);
    }

    @Override
    public void onDisable()
    {
        task.cancel();
        System.out.println("BluestarTpsControl已禁用");
    }
}
