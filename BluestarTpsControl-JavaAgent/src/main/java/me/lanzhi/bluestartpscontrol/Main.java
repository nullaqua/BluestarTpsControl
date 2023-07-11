package me.lanzhi.bluestartpscontrol;

import javassist.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Map;

public final class Main
{
    private final static ClassPool pool=ClassPool.getDefault();
    private static final Map<String,String> methodName;
    private static final Map<String,Map.Entry<String,String>> fieldMap;

    static
    {
        pool.insertClassPath(new LoaderClassPath(Main.class.getClassLoader()));
        methodName=new HashMap<>();
        fieldMap=new HashMap<>();
        for (int i=1;i<=9;i++)
        {
            methodName.put("v1_17_R"+i,"sleepForTick");
        }
        for (int i=1;i<=5;i++)
        {
            methodName.put("v1_18_R"+i,"x");
        }
        methodName.put("v1_19_R1","w");
        methodName.put("v1_19_R2","i_");
        methodName.put("v1_19_R3","i_");
        methodName.put("v1_20_R1","p_");

        for (int i=1;i<=9;i++)
        {
            fieldMap.put("v1_17_R"+i,new HashMap.SimpleEntry<>("ao","ap"));
            fieldMap.put("v1_18_R"+i,new HashMap.SimpleEntry<>("ao","ap"));
        }
        fieldMap.put("v1_19_R1",new HashMap.SimpleEntry<>("ag","ah"));
        fieldMap.put("v1_19_R2",new HashMap.SimpleEntry<>("ag","ah"));
        fieldMap.put("v1_19_R3",new HashMap.SimpleEntry<>("ai","ah"));
        fieldMap.put("v1_20_R1",new HashMap.SimpleEntry<>("ai","ah"));
    }

    public static void premain(final String agentArgs,final Instrumentation inst)
    {
        inst.addTransformer(new Transformer());
    }

    private static final class Transformer implements ClassFileTransformer
    {
        String version=null;

        @Override
        public byte[] transform(final ClassLoader loader,String className,final Class<?> classBeingRedefined,
                                final ProtectionDomain protectionDomain,final byte[] classfileBuffer)
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
                    CtMethod method=clazz.getDeclaredMethod("format");
                    if (method.getReturnType().getName().endsWith("String"))
                    {
                        final String color="org.bukkit.ChatColor";
                        final String red=color+".RED";
                        final String yellow=color+".YELLOW";
                        final String green=color+".GREEN";
                        final String aqua=color+".AQUA";
                        method.setBody("{ return ($1>21.0D?"+aqua+":$1>18.0D?"+green+":$1>16.0D?"+yellow+":"+red+")" +
                                       ".toString() +"+"((double)Math.round($1 * 100.0D) / 100.0D); }");
                    }
                    else
                    {
                        final String color="net.kyori.adventure.text.format.NamedTextColor";
                        final String red=color+".RED";
                        final String yellow=color+".YELLOW";
                        final String green=color+".GREEN";
                        final String aqua=color+".AQUA";
                        final String component="net.kyori.adventure.text.Component";
                        method.setBody("{return "+component+".text(\"\"+((double)Math.round($1*100.0D)/100.0D),"+"($1" +
                                       ">21.0D?"+aqua+":$1>18.0D?"+green+":$1>16.0D?"+yellow+":"+red+"));}");
                    }
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
                String methodName=Main.methodName.get(version);
                Map.Entry<String,String> field=fieldMap.get(version);
                if (methodName==null||field==null)
                {
                    if (version==null)
                    {
                        System.err.println("无法识别版本,修改失败");
                    }
                    else
                    {
                        System.err.println("未知版本: "+version+",修改失败");
                    }
                    return null;
                }
                try
                {
                    CtClass clazz=pool.get(className);
                    clazz.addField(CtField.make("private static long BluestarMSPT=50L;",clazz));
                    String insert="{ $0."+field.getKey()+"+=BluestarMSPT-50L;$0."+field.getValue()+"+=BluestarMSPT" +
                                  "-50L; }";
                    System.err.println(insert);
                    clazz.getDeclaredMethod(methodName).insertBefore(insert);
                    clazz.addMethod(CtNewMethod.make(
                            "public static boolean setmspt(long mspt){ if(mspt>0L){BluestarMSPT=mspt;return true;" +
                            "}else return false; }",
                            clazz));
                    clazz.addMethod(CtNewMethod.make("public static long getmspt(){ return BluestarMSPT; }",clazz));
                    //CtClass api=pool.makeInterface("me.lanzhi.bluestartpscontrol.BluestarTpsControlAPI");
                    //api.addMethod(CtNewMethod.make("public boolean setmspt(long mspt);",api));
                    //api.addMethod(CtNewMethod.make("public long getmspt();",api));
                    //CtMethod method=CtNewMethod.make("public static me.lanzhi.bluestartpscontrol
                    // .BluestarTpsControlAPI getAPI();",api);
                    //method.setBody("{return net.minecraft.server.MinecraftServer.getServer();}");
                    //api.addMethod(method);
                    //api.writeFile("BluestarTpsControl");
                    //clazz.addInterface(pool.get("me.lanzhi.bluestartpscontrol.BluestarTpsControlAPI"));
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
            return null;
        }
    }
}
