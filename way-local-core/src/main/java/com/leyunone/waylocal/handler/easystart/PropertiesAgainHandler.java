package com.leyunone.waylocal.handler.easystart;

import com.leyunone.waylocal.bean.CustomStartInfo;
import org.springframework.core.env.MutablePropertySources;

/**
 * :)
 *
 * @author LeYunone
 * @email 365627310@qq.com
 * @date 2024/1/30
 */
public interface PropertiesAgainHandler {

    void enhance(MutablePropertySources mutablePropertySources, CustomStartInfo customStartInfo);
}
