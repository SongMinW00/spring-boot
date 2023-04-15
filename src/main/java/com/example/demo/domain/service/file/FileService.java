package com.example.demo.domain.service.file;


import com.example.demo.domain.dao.file.FileRepository;
import com.example.demo.domain.entity.file.FileEntity;
import com.example.demo.domain.entity.user.Member;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class FileService {

    @Value("${file.dir}")
    private String fileDir;
    private String savedPath;

    private final FileRepository fileRepository;


    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public Long saveFile(MultipartFile files, Member uploader) throws IOException {
        if (files.isEmpty()) {
            return null;
        }

        // 원래 파일 이름 추출
        String origName = files.getOriginalFilename();

        // 파일 이름으로 쓸 uuid 생성
        String uuid = UUID.randomUUID().toString();

        // 확장자 추출(ex : .png)
        String extension = origName.substring(origName.lastIndexOf("."));

        // uuid와 확장자 결합
        String savedName = uuid + extension;

        // 파일을 불러올 때 사용할 파일 경로
        savedPath = fileDir + savedName;

        // 파일 엔티티 생성
        FileEntity file = FileEntity.builder()
                .orgNm(origName)
                .savedNm(savedName)
                .savedPath(savedPath)
                .uploader(uploader)
                .build();

        // 실제로 로컬에 uuid를 파일명으로 저장
        files.transferTo(new File(getFullPath(savedName)));

        // 데이터베이스에 파일 정보 저장
        FileEntity savedFile = fileRepository.save(file);

        return savedFile.getId();
    }






    public FileEntity getId(Long id) {
        return fileRepository.getReferenceById(id);
    }

    public List<FileEntity> findAll() {
        return fileRepository.findAll();
    }

    public Optional<FileEntity> findById(Long id) {
        return fileRepository.findById(id);
    }

    public FileEntity getByFileName(String savedNm) {
        return fileRepository.getByFileName(savedNm);
    }
}