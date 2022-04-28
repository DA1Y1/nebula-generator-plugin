package com.mininglamp.km.nebula.generator.tools;

import java.util.concurrent.Callable;

public class PluginUtils {
    public static <T> T invokeInServiceLoader(Callable<T> callable) {
        ClassLoader current = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(PluginUtils.class.getClassLoader());
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            Thread.currentThread().setContextClassLoader(current);
        }
    }


    public static void invokeInServiceLoader(String path) {
        ClassLoader current = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(PluginUtils.class.getClassLoader());
            JarLoader.INSTANCE.loadJar(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            Thread.currentThread().setContextClassLoader(current);
        }
    }

}