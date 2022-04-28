package com.mininglamp.km.nebula.generator.tools;

import com.intellij.ide.plugins.cl.PluginClassLoader;
import com.intellij.util.lang.UrlClassLoader;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.security.ProtectionDomain;
import java.util.Set;

/**
 * @author daiyi
 * @date 2021/9/22
 */
public enum JarLoader {

    INSTANCE;

    public void loadJar(String jarPath) {
        File jarFile = new File(jarPath);
        Method method = null;
        try {
            method = UrlClassLoader.class.getDeclaredMethod("addURL", URL.class);
        } catch (NoSuchMethodException | SecurityException e1) {
            e1.printStackTrace();
        }
        //获取方法的访问权限以便写回
        boolean accessible = method.isAccessible();
        try {

            method.setAccessible(true);
            // 获取系统类加载器
            PluginClassLoader classLoader = (PluginClassLoader) this.getClass().getClassLoader();

            URL url = jarFile.toURI().toURL();
            // 避免windows /C:/Users/*情况出现
            if (url.getPath().startsWith("/")) {
                url = new URL("file", "", -1, url.getPath().substring(1));
            }
            method.invoke(classLoader, url);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            method.setAccessible(accessible);
        }
//        reloadJar(jarPath);
    }

    public void loadJar2(String jarPath) {
        Set<String> clzFromPkg = ClassHelper.getClzFromPath(jarPath);

        DynamicJarClassLoader dynamicJarClassLoader = new DynamicJarClassLoader(jarPath, null);

        File jarFile = new File(jarPath);
//        Method method = null;
//        try {
//            method = UrlClassLoader.class.getDeclaredMethod("addURL", URL.class);
//        } catch (NoSuchMethodException | SecurityException e1) {
//            e1.printStackTrace();
//        }
        //获取方法的访问权限以便写回
//        boolean accessible = method.isAccessible();
        try {
            dynamicJarClassLoader.addURL(jarFile.toURI().toURL());
//            for (String s : clzFromPkg) {
//                dynamicJarClassLoader.loadClass(s.replace("/","."));
//            }

//            method.setAccessible(true);
//            // 获取系统类加载器
//            PluginClassLoader classLoader = (PluginClassLoader) this.getClass().getClassLoader();
//
//            URL url = jarFile.toURI().toURL();
//            // 避免windows /C:/Users/*情况出现
//            if (url.getPath().startsWith("/")) {
//                url = new URL("file", "", -1, url.getPath().substring(1));
//            }
//            method.invoke(classLoader, url);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            method.setAccessible(accessible);
        }
        reloadJar(jarPath);
    }


    public void reloadJar(String jarPath) {
        Set<String> clzFromPkg = ClassHelper.getClzFromPath(jarPath);
        Method method = null;
        try {
            method = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class, ProtectionDomain.class);
        } catch (NoSuchMethodException | SecurityException e1) {
            e1.printStackTrace();
        }
        //获取方法的访问权限以便写回
        boolean accessible = method.isAccessible();
        try {
            method.setAccessible(true);
            // 获取系统类加载器
            PluginClassLoader classLoader = (PluginClassLoader) this.getClass().getClassLoader();

            for (String s : clzFromPkg) {
                byte[] data = new byte[1024 * 1024 * 1];
                URL url = new URL("jar:file:/" + jarPath + "!/" + s + ".class");
                InputStream inputStream = url.openStream();
                int length = inputStream.read(data);
                method.invoke(classLoader, s.replace("/", "."), data, 0, length, classLoader.getClass().getProtectionDomain());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            method.setAccessible(accessible);
        }

    }

    public void resetJdbcPrefix(String jarPath) {
        try {
            Method method = ClassLoader.class.getDeclaredMethod("findLoadedClass", String.class);
            method.setAccessible(true);

            PluginClassLoader classLoader = (PluginClassLoader) this.getClass().getClassLoader();
            Class driverClass = (Class) method.invoke(classLoader, "com.vesoft.nebula.jdbc.GraphDriver");
            Field field = driverClass.getDeclaredField("DRIVER_URL_PREFIX");
            field.setAccessible(true);
            Field modifiers = field.getClass().getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.set(field, field.getModifiers() & ~Modifier.FINAL);
            if (jarPath.contains("1.1")) {
                field.set(driverClass.newInstance(), "jdbc:graph:");
            } else {
                field.set(driverClass.newInstance(), "jdbc:nebula:");
            }
        } catch (NoSuchFieldException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

}
