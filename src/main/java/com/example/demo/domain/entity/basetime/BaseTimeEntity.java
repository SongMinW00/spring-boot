package com.example.demo.domain.entity.basetime;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Getter
@MappedSuperclass   // JPA Entity 클래스들이 BaseTimeEntity를 상속할경우 필드들도 컬럼으로 인식하도록함.
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {  // 모든 Entity 의 상위 클래스가 되어 Entity 들의 createDate, updateDate를 자동 관리
    @CreatedDate    //Entity 가 생성되어 저장될 때 시간이 자동저장.
    private LocalDateTime createDate;
    @LastModifiedDate
    private LocalDateTime updateDate;
}
