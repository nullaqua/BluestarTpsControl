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
        String version=null;
        @Override
        public byte[] transform(final ClassLoader loader,String className,final Class<?> classBeingRedefined,final ProtectionDomain protectionDomain,final byte[] classfileBuffer)
        {
            pool.insertClassPath(new LoaderClassPath(loader));
            className=className.replace('/','.');
            if (className.startsWith("org.bukkit.craftbukkit.v")&&version==null)
            {
                System.out.println(className);
                version=className.split("\\.")[3];
                return null;
            }
            if (className.equals("org.spigotmc.TicksPerSecondCommand"))
            {
                try
                {
                    CtClass clazz=pool.get(className);
                    clazz.getDeclaredMethod("format").setBody(
                            "{ return ($1 > 21.0D ? org.bukkit.ChatColor.AQUA : $1 > 18.0D ? org.bukkit.ChatColor.GREEN : $1 > 16.0D ?"+
                                    "org.bukkit.ChatColor.YELLOW : org.bukkit.ChatColor.RED).toString() +"+
                                    "((double)Math.round($1 * 100.0D) / 100.0D); }");
                    //clazz.addMethod(CtNewMethod.make("public static String BluestarTpsControlFormat(double tps){return format(tps);}",clazz));
                    System.out.println("[BluestarTpsControl] Class "+className+" 变更成功");
                    return clazz.toBytecode();
                }
                catch (Throwable e)
                {
                    System.out.println("[BluestarTpsControl] Class "+className+" 修改失败!");
                    e.printStackTrace();
                }
            }
            if (className.equals("net.minecraft.server.MinecraftServer"))
            {
                if (version==null)
                {
                    System.err.println("无法识别版本,可能无法完成修改");
                    try
                    {
                        CtClass clazz=pool.get(className);
                        clazz.addField(CtField.make("private static long BluestarMSPT=50L;",clazz));
                        //clazz.getDeclaredMethod("a",pool.get).insertBefore("{ $0.ag+=BluestarMSPT-50L; }");
                        clazz.getDeclaredMethod("w").insertBefore("{ $0.ag+=BluestarMSPT-50L;$0.ah+=BluestarMSPT-50L; }");
                        clazz.addMethod(CtNewMethod.make("public static void setmspt(long mspt){ if(mspt!=0L)BluestarMSPT=mspt; }",clazz));
                        clazz.addMethod(CtNewMethod.make("public static long getmspt(){ return BluestarMSPT; }",clazz));
                        System.out.println("[BluestarTpsControl] Class "+className+" 变更成功");
                        return clazz.toBytecode();
                    }
                    catch (Throwable e)
                    {
                        System.out.println("[BluestarTpsControl] Class "+className+" 修改失败!");
                        e.printStackTrace();
                        return null;
                    }
                }
                if (version.startsWith("v1_17_R"))
                {
                    try
                    {
                        CtClass clazz=pool.get(className);
                        clazz.addField(CtField.make("private static long BluestarMSPT=50L;",clazz));
                        clazz.getDeclaredMethod("sleepForTick").insertBefore("{ $0.ao+=BluestarMSPT-50L;$0.ap+=BluestarMSPT-50L; }");
                        clazz.addMethod(CtNewMethod.make("public static void setmspt(long mspt){ if(mspt!=0L)BluestarMSPT=mspt; }",clazz));
                        clazz.addMethod(CtNewMethod.make("public static long getmspt(){ return BluestarMSPT; }",clazz));
                        System.out.println("[BluestarTpsControl] Class "+className+" 变更成功 "+version);
                        return clazz.toBytecode();
                    }
                    catch (Throwable e)
                    {
                        System.out.println("[BluestarTpsControl] Class "+className+" 修改失败 "+version);
                        e.printStackTrace();
                        return null;
                    }
                }
                if (version.startsWith("v1_18_R"))
                {
                    try
                    {
                        CtClass clazz=pool.get(className);
                        clazz.addField(CtField.make("private static long BluestarMSPT=50L;",clazz));
                        clazz.getDeclaredMethod("x").insertBefore("{ $0.ao+=BluestarMSPT-50L;$0.ap+=BluestarMSPT-50L; }");
                        clazz.addMethod(CtNewMethod.make("public static void setmspt(long mspt){ if(mspt!=0L)BluestarMSPT=mspt; }",clazz));
                        clazz.addMethod(CtNewMethod.make("public static long getmspt(){ return BluestarMSPT; }",clazz));
                        System.out.println("[BluestarTpsControl] Class "+className+" 变更成功 "+version);
                        return clazz.toBytecode();
                    }
                    catch (Throwable e)
                    {
                        System.out.println("[BluestarTpsControl] Class "+className+" 修改失败 "+version);
                        e.printStackTrace();
                        return null;
                    }
                }
                if (version.startsWith("v1_19_R"))
                {
                    try
                    {
                        CtClass clazz=pool.get(className);
                        clazz.addField(CtField.make("private static long BluestarMSPT=50L;",clazz));
                        clazz.getDeclaredMethod("w").insertBefore("{ $0.ag+=BluestarMSPT-50L;$0.ah+=BluestarMSPT-50L; }");
                        clazz.addMethod(CtNewMethod.make("public static void setmspt(long mspt){ if(mspt!=0L)BluestarMSPT=mspt; }",clazz));
                        clazz.addMethod(CtNewMethod.make("public static long getmspt(){ return BluestarMSPT; }",clazz));
                        System.out.println("[BluestarTpsControl] Class "+className+" 变更成功 "+version);
                        return clazz.toBytecode();
                    }
                    catch (Throwable e)
                    {
                        System.out.println("[BluestarTpsControl] Class "+className+" 修改失败 "+version);
                        e.printStackTrace();
                        return null;
                    }
                }
                System.err.println("未知服务器版本: "+version);
            }
            return null;
        }
    }
}
