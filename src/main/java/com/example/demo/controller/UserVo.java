//// create the @Entity Model
//package com.example.demo.controller;
//import com.example.demo.Repository.Role;
//import lombok.*;
////import org.springframework.security.core.GrantedAuthority;
////import org.springframework.security.core.authority.SimpleGrantedAuthority;
////import org.springframework.security.core.userdetails.UserDetails;
//
//
//import javax.persistence.*;
//
//
//@NoArgsConstructor(access= AccessLevel.PROTECTED)
//@Entity
//@Getter
//@Setter
//
//public class UserVo {
//
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id; //user 시리얼 넘버
//    private String userId;
//    private String password;
//    private String name; // 유저 아이디
//    private String userEmail; // 유저 이름
//    @Enumerated(EnumType.STRING)
//    @Column(name = "role")
//    private Role role;
//    //public String role;
//    public String toString(){
//        return "User [userId= " + userId + ", password=" + password + ", name=" + name + ", email=" + userEmail + "]";
//    }
//
//
//    @Builder
//    public UserVo(Long id, String userId, String userEmail,String password, Role role){
//        this.id = id;
//        this.userId = userId;
//        this.userEmail = userEmail;
//        this.password = password;
//        this.role = role;
//
//    }
//
//    public String getRoleKey(){
//        return this.role.getKey();
//    }
//
//    public UserVo update(String userId, String userEmail, String password, Role role){
//        this.userId = userId;
//        this.userEmail=userEmail;
//        this.password = password;
//        this.role = role;
//        return this;
//    }
//    // 사용자의 권한을 콜렉션 형태로 반환
//    // 단, 클래스 자료형은 GrantedAuthority를 구현해야함
////    @Override
////    public Collection<? extends GrantedAuthority> getAuthorities() {
////        Set<GrantedAuthority> roles = new HashSet<>();
////        for (String role : auth.split(",")) {
////            roles.add(new SimpleGrantedAuthority(role));
////        }
////        return roles;
////    }
//
//    // 사용자의 id를 반환 (unique한 값)
////    @Override
////    public String getUsername() {
////        return userId;
////    }
////
////    // 사용자의 password를 반환
////    @Override
////    public String getPassword() {
////        return password;
////    }
////
//    // 계정 만료 여부 반환
////    @Override
////    public boolean isAccountNonExpired() {
////        // 만료되었는지 확인하는 로직
////        return true; // true -> 만료되지 않았음
////    }
////
////    // 계정 잠금 여부 반환
////    @Override
////    public boolean isAccountNonLocked() {
////        // 계정 잠금되었는지 확인하는 로직
////        return true; // true -> 잠금되지 않았음
////    }
////
////    // 패스워드의 만료 여부 반환
////    @Override
////    public boolean isCredentialsNonExpired() {
////        // 패스워드가 만료되었는지 확인하는 로직
////        return true; // true -> 만료되지 않았음
////    }
////
////    // 계정 사용 가능 여부 반환
////    @Override
////    public boolean isEnabled() {
////        // 계정이 사용 가능한지 확인하는 로직
////        return true; // true -> 사용 가능
////    }
//
//
//}
//// Hibernate automatically translates the entity into a table.
