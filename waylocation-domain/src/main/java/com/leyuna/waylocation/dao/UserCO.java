package com.leyuna.waylocation.dao;

import lombok.*;

import java.io.Serializable;

/**
 * @author pengli
 * @create 2021-08-10 16:09
 *
 * user的出入参
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String userName;

    private String passWord;
}
