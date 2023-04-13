package com.example.demo.domain.dao.file;

import com.example.demo.domain.entity.file.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity,Long> {}