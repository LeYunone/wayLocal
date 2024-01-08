package com.leyunone.waylocal.handler;


import cn.hutool.core.util.ObjectUtil;
import com.leyunone.waylocal.annotate.StrategyKey;
import com.leyunone.waylocal.handler.factory.AbstractStrategyFactory;
import org.springframework.beans.factory.InitializingBean;

/**
 * :)
 * 策略自动注册组件
 *
 * @Author leyunone
 * @Date 2024/1/9 11:40
 */
public abstract class StrategyAutoRegisterComponent implements InitializingBean {

    private void register(String strategyKey, Object o) {
        Class<?> aClass = o.getClass();
        StrategyKey annotation = aClass.getAnnotation(StrategyKey.class);
        if (ObjectUtil.isNotNull(annotation)) {
            strategyFactory().strategyStore().put(annotation.key(), o);
        } else {
            strategyFactory().strategyStore().put(strategyKey, o);
        }
    }

    public Object getStrategy(String key) {
        return strategyFactory().strategyStore().get(key);
    }


    public void afterPropertiesSet() {
        register(getKey(), this.getClass());
    }

    public String getKey() {
        return null;
    }

    public abstract AbstractStrategyFactory strategyFactory();
}