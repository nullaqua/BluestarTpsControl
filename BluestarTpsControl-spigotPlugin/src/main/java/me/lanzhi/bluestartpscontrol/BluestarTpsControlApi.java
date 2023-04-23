package me.lanzhi.bluestartpscontrol;

import org.bukkit.ChatColor;

public class BluestarTpsControlApi implements BluestarTpsControlAPI
{
    private final BluestarTpsControl plugin;

    public BluestarTpsControlApi(BluestarTpsControl plugin)
    {
        this.plugin=plugin;
    }

    @Override
    public boolean setmspt(long mspt) throws MsptIllegalException
    {
        if (mspt<=0)
        {
            throw new MsptIllegalException();
        }
        try
        {
            plugin.getSetmspt().invoke(null,mspt);
            return true;
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public long getmspt()
    {
        try
        {
            return (long) plugin.getGetmspt().invoke(null);
        }
        catch (Throwable e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String tpsFormat(double tps)
    {
        return (tps>21.0D?ChatColor.AQUA:tps>18.0D?ChatColor.GREEN:tps>16.0D?ChatColor.YELLOW:ChatColor.RED).toString()+((double) Math.round(tps*100.0D)/100.0D);
    }
}
