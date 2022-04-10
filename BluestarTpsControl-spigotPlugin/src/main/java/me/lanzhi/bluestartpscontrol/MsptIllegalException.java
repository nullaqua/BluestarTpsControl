package me.lanzhi.bluestartpscontrol;

public class MsptIllegalException extends Exception
{
    public MsptIllegalException()
    {
        super("MSPT必须为大于0的整数!");
    }
}
