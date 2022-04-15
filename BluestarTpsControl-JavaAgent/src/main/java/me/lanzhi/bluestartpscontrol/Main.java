package me.lanzhi.bluestartpscontrol;

import javassist.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class Main
{
    private final static ClassPool pool=ClassPool.getDefault();
    public static void premain(final String agentArgs,final Instrumentation inst)
    {
        inst.addTransformer(new Transformer());
    }
    private static final class Transformer implements ClassFileTransformer
    {
        @Override
        public byte[] transform(final ClassLoader loader,String className,final Class<?> classBeingRedefined,final ProtectionDomain protectionDomain,final byte[] classfileBuffer)
        {
            className=className.replace('/','.');
            if (className.equals("org.spigotmc.TicksPerSecondCommand"))
            {
                try
                {
                    pool.insertClassPath(new LoaderClassPath(loader));
                    CtClass clazz=pool.get(className);
                    clazz.getDeclaredMethod("format").setBody(
                            "{ return ($1 > 21.0D ? org.bukkit.ChatColor.AQUA : $1 > 18.0D ? org.bukkit.ChatColor.GREEN : $1 > 16.0D ?"+
                                    "org.bukkit.ChatColor.YELLOW : org.bukkit.ChatColor.RED).toString() +"+
                                    "((double)Math.round($1 * 100.0D) / 100.0D); }");
                    System.out.println("[BluestarTpsControl] Class "+className+" 变更成功");
                    return clazz.toBytecode();
                }
                catch (Throwable e)
                {
                    System.out.println("[BluestarTpsControl] Class "+className+" 修改失败!");
                }
            }
            if (className.equals("net.minecraft.server.MinecraftServer"))
            {
                pool.insertClassPath(new LoaderClassPath(loader));
                try
                {
                    CtClass clazz=pool.get(className);
                    clazz.addField(CtField.make("private static long BluestarMSPT=50L;",clazz));
                    clazz.getDeclaredMethod("bg").insertBefore("{ $0.ao+=BluestarMSPT-50L; }");
                    clazz.getDeclaredMethod("x").insertBefore("{ $0.ap=java.lang.Math.max(net.minecraft.SystemUtils.b()+BluestarMSPT,$0.ao); }");
                    clazz.addMethod(CtNewMethod.make("public static void setmspt(long mspt){ if(mspt!=0L)BluestarMSPT=mspt; }",clazz));
                    clazz.addMethod(CtNewMethod.make("public static void getmspt(){ return BluestarMSPT; }",clazz));
                    System.out.println("[BluestarTpsControl] Class "+className+" 变更成功");
                    return clazz.toBytecode();
                }
                catch (Throwable e)
                {
                    System.out.println("[BluestarTpsControl] Class "+className+" 修改失败!");
                }
            }
            return null;
        }
    }
}
