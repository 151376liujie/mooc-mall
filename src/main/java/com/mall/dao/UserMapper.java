package com.mall.dao;

import com.mall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectUserByNameAndPass(@Param("username") String username, @Param("password") String password);

    int checkUsername(String username);

    int checkEmail(String email);

    String selectQuestion(String username);

    int checkAnswer(@Param("username") String username, @Param("question") String question, @Param("answer") String answer);

    int updateUserPassword(@Param("username") String username, @Param("newPass") String newPass);

    int checkUserPassword(@Param("oldPass") String oldPass, @Param("userId") int userId);

    int checkUserEmail(@Param("email") String email, @Param("userId") int userId);
}