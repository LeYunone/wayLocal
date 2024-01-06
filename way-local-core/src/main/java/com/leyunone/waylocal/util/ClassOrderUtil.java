package com.leyunone.waylocal.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author leyunone
 * @create 2022-02-21 16:32
 */
public class ClassOrderUtil {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    //通过loader加载所有类
    public static List<Class> loadClassByLoader(ClassLoader load){
        Enumeration<URL> urls = null;
        try {
            urls = load.getResources("");
        } catch (IOException e) {
        }
        //放所有类型
        List<Class> classes = new ArrayList<>();
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            //文件类型
            if (url.getProtocol().equals("file")) {
                loadClassByPath(null, url.getPath(), classes, load);
            }
        }
        return classes;
    }

    /**
     * 解析根路径下文件夹的类名
     * @param root
     * @param path
     * @param list
     * @param load
     */
    private static void loadClassByPath(String root, String path, List<Class> list, ClassLoader load) {
        File f = new File(path);
        if(root==null) {
            root = f.getPath();
        }
        //判断是否是class文件
        if (f.isFile() && f.getName().matches("^.*\\.class$")) {
            try {
                String classPath = f.getPath();
                //截取出className 
                String className = classPath.substring(root.length()+1,classPath.length()-6).replace('/','.').replace('\\','.');
                list.add(load.loadClass(className));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            File[] fs = f.listFiles();
            if (fs == null){
                return;
            }
            for (File file : fs) {
                loadClassByPath(root,file.getPath(), list, load);
            }
        }
    }
}
