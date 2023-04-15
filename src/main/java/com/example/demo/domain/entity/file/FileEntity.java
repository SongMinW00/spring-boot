package com.example.demo.domain.entity.file;

import com.example.demo.domain.entity.user.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Table(name = "file")
@Entity
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="file_id")
    private Long id;

    private String orgNm;

    private String savedNm;
    private String savedPath;

    @ManyToOne
    private Member uploader;

    @Builder
    public FileEntity(Long id, String orgNm, String savedNm, String savedPath, Member uploader) {
        this.id = id;
        this.orgNm = orgNm;
        this.savedNm = savedNm;
        this.savedPath = savedPath;
        this.uploader = uploader;
    }
}

