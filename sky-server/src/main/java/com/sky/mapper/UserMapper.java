package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface UserMapper {
    /*
    * 根据openid查询用户
     */
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    void insert(User user);

    /*
    * 根据id查询用户
     */
    @Select("select * from user where id = #{id}")
    User getById(Long userId);

    /*
     * 根据条件统计用户数量
     */
    Integer countByMap(Map map);



}
