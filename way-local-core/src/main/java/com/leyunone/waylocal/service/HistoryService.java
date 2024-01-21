package com.leyunone.waylocal.service;

import com.leyunone.waylocal.bean.dto.MethodInfoDTO;
import com.leyunone.waylocal.bean.vo.MethodInfoVO;

import java.util.List;

/**
 * :)
 *
 * @Author leyunone
 * @Date 2024/1/9 11:07
 */
public interface HistoryService {

    void export(List<MethodInfoDTO> methodInfos);

    List<MethodInfoVO> readHistory();

    void saveHistory(MethodInfoDTO methodInfoDTOS);
}
