package com.kanbee.project.mapper;

import com.kanbee.project.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper // MyBatis 매퍼 인터페이스임을 나타냅니다. 이 인터페이스의 메서드들은 대응하는 XML 파일의 SQL 문과 연결됩니다.
public interface UserMapper {
    // 새로운 사용자를 등록하는 메서드입니다. (UserMapper.xml의 <insert id="register">와 연결됨)
    void register(User user);

    // 아이디(username)로 사용자 한 명의 정보를 가져오는 메서드입니다.
    // @Param 어노테이션은 XML에서 #{username}과 같이 파라미터를 사용할 수 있게 해줍니다.
    User findByUsername(@Param("username") String username);

    // 해당 아이디가 몇 개 존재하는지 카운트합니다. (중복 확인용, 0이면 사용 가능)
    int checkUsername(@Param("username") String username);

    // 해당 닉네임이 몇 개 존재하는지 카운트합니다.
    int checkNickname(@Param("nickname") String nickname);

    // 비밀번호 변경
    void updatePassword(@Param("username") String username, @Param("password") String password);
}
