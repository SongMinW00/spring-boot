package com.example.demo.domain.dao.user;
import com.example.demo.domain.dto.request.SignUpRequestDTO;
import com.example.demo.domain.entity.user.Member;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
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

   @Transactional
   Optional<Member> findByEmail(String email);

//   Member getMemberByUsername(String username);
   @Transactional
   @Query("UPDATE Member m SET m.failCount = ?1 WHERE m.username = ?2")
   @Modifying
   void updateFailedAttempts(int failCount, String username);

   @Query("SELECT m FROM Member m WHERE m.username = :username")
   Member getMemberByUsername(@Param("username") String username);

   @Transactional
   @Query(value = "UPDATE Member m SET m.account_non_locked = ?2, m.lock_time = ?3 WHERE m.username = ?1", nativeQuery = true)
   @Modifying
   void isLocked(String username, boolean accountNonLocked, Date lockTime);

   @Transactional
   @Query(value = "UPDATE Member m SET m.account_non_locked = ?2, m.lock_time = ?3, m.fail_count = ?4 WHERE m.username = ?1", nativeQuery = true)
   @Modifying
   void unLocked(String username, boolean accountNonLocked, Date lockTime, int failCount);

   @Transactional
   @Query(value = "UPDATE Member m SET m.password = ?, m.email = ? WHERE m.id = ?", nativeQuery = true)
   @Modifying
   void update(String password, String email, Long id);


}
// Spring automatically implements this repository interface in a bean that has the same name (with a change in the case == it is called userRepository).