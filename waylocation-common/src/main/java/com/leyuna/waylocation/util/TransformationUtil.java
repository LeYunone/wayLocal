package com.leyuna.waylocation.util;

import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengli
 * @create 2021-08-10 16:46
 * 转换对象工具类
 */
public class TransformationUtil {


    /**
     *
     * @param copyClass  复制源
     * @param toClass    出参对象
     * @return
     */
    public static <D>D copyToDTO(Object copyClass, Class<D> toClass){
        D d = null;
        try {
            d = toClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if(null!=copyClass){
            BeanUtils.copyProperties(copyClass, d);
        }
        return d;
    }

    /**
     *
     * @param copyClass  复制源
     * @param toClass    出参对象
     * @return
     */
    public static <D>List<D> copyToLists(List<?> copyClass, Class<D> toClass){
        List<D> result=new ArrayList<>();
        copyClass.stream().forEach(list->{
            try {
                Object tarObject = toClass.newInstance();
                BeanUtils.copyProperties(list, tarObject);
                result.add((D)tarObject);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return result;
    }
}
