package com.example.demo.Repository;



import com.example.demo.domain.dto.request.SignUpRequestDTO;
import com.example.demo.domain.entity.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Created, Read, Update, Delete

@Repository
public interface UserRepository extends JpaRepository<Member, Long> {
    //부모 클래스는 걍 정의만 interface(부모에서)-> implements(자식에서)

//    Optional<SignUpRequestDTO> findByUsername(String userId);

   boolean existsByUsername(String username);
   boolean existsByEmail(String email);
//    boolean existsByUserId(String userId);
//    boolean existsByUserEmail(String userEmail);

    //List<UserVo> findAll();

   Optional<Member> findByUsername(String username);
}
// Spring automatically implements this repository interface in a bean that has the same name (with a change in the case == it is called userRepository).