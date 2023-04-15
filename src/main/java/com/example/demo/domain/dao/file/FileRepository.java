package com.example.demo.domain.dao.file;

import com.example.demo.domain.entity.file.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FileRepository extends JpaRepository<FileEntity,Long> {
    @Query("select f from FileEntity f where f.savedNm = :savedNm")
    FileEntity getByFileName(@Param("savedNm") String savedNm);

}