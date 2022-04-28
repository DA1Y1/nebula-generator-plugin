package com.mininglamp.km.nebula.generator.tools;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class DynamicJarClassLoader extends URLClassLoader {
    private static boolean canCloseJar = false;
    private List<JarURLConnection> cachedJarFiles;

    static {
        // JDK1.7以上版本支持直接调用close方法关闭打开的jar
        // 如果不支持close方法，需要手工释放缓存，避免卸载模块后无法删除jar
        try {
            URLClassLoader.class.getMethod("close");
            canCloseJar = true;
        } catch (NoSuchMethodException e) {
            System.out.println(e);
        } catch (SecurityException e) {
            System.out.println(e);
        }
    }

    public DynamicJarClassLoader(String libDir, ClassLoader parent) {
        super(new URL[]{}, null == parent ? Thread.currentThread().getContextClassLoader() : parent);
        File base = new File(libDir);
        URL[] urls = null;
        if (null != base && base.canRead() && base.isDirectory()) {
            File[] files = base.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if (pathname.getName().contains(".jar")) {
                        return true;
                    } else return false;
                }
            });
            urls = new URL[files.length];
            for (int j = 0; j < files.length; j++) {
                try {
                    URL element = files[j].toURI().normalize().toURL();
                    System.out.println("Adding '" + element.toString() + "' to classloader");
                    urls[j] = element;
                } catch (MalformedURLException e) {
                    System.out.println(e);
                }
            }
        }
        init(urls);
    }

    private void init(URL[] urls) {
        cachedJarFiles = canCloseJar ? null : new ArrayList<JarURLConnection>();
        if (urls != null) {
            for (URL url : urls) {
                this.addURL(url);
            }
        }
    }

    @Override
    protected void addURL(URL url) {
        if (!canCloseJar) {
            try {
                // 打开并缓存文件url连接
                URLConnection uc = url.openConnection();
                if (uc instanceof JarURLConnection) {
                    uc.setUseCaches(true);
                    ((JarURLConnection) uc).getManifest();
                    cachedJarFiles.add((JarURLConnection) uc);
                }
            } catch (Exception e) {
            }
        }
        super.addURL(url);
    }

    public void close() throws IOException {
        if (canCloseJar) {
            try {
                super.close();
            } catch (IOException e) {
                System.out.println(e);
            }
        } else {
            for (JarURLConnection conn : cachedJarFiles) {
                conn.getJarFile().close();
            }
            cachedJarFiles.clear();
        }
    }

}