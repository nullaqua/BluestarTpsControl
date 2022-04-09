package me.lanzhi.bluestartpscontrol;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class maincommand implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender,Command command,String label,String[] args)
    {
        if (args.length < 1)
        {
            sender.sendMessage(ChatColor.RED+"请输入tps");
            return false;
        }
        double tps;
        try
        {
            tps = Double.parseDouble(args[0]);
        }
        catch (NumberFormatException e)
        {
            sender.sendMessage(ChatColor.RED+"请输入正确的数");
            return false;
        }
        if (tps<=0||tps>1000)
        {
            sender.sendMessage(ChatColor.RED+"tps应该大于0小于等于1000");
        }
        try
        {
            BluestarTpsControl.setmspt.invoke(null,(long)(1000D/tps));
        }
        catch (Throwable e)
        {
            sender.sendMessage(ChatColor.RED+"出现错误,请向开发者反馈此bug");
            return false;
        }
        sender.sendMessage(ChatColor.GREEN+"tps已设置为"+tps);
        return true;
    }
}
