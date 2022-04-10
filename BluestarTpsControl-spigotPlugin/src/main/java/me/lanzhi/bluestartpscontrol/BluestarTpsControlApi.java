package me.lanzhi.bluestartpscontrol;

public class BluestarTpsControlApi
{
    public static boolean setmspt(long mspt) throws MsptIllegalException
    {
        if(mspt<=0)
        {
            throw new MsptIllegalException();
        }
        try
        {
            BluestarTpsControl.setmspt.invoke(null,mspt);
            return true;
        }
        catch (Throwable e)
        {
            return false;
        }
    }
    public static long getmspt()
    {
        try
        {
            return (long)BluestarTpsControl.getmspt.invoke(null);
        }
        catch (Throwable e)
        {
            return 0;
        }
    }
}
