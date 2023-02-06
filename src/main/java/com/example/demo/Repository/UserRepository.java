package com.example.demo.Repository;
import com.example.demo.domain.entity.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<Member, Long> {
    //부모 클래스는 걍 정의만 interface(부모에서)-> implements(자식에서)
   /* 유저아이디 존재하는지 검사 */
   boolean existsByUsername(String username);
   /* 이메일 존재하는지 검사 */
   boolean existsByEmail(String email);
   /* 데이터베이스에서 아이디 찾기 */
   Optional<Member> findByUsername(String username);

}
// Spring automatically implements this repository interface in a bean that has the same name (with a change in the case == it is called userRepository).