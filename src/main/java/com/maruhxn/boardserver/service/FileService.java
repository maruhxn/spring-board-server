package com.maruhxn.boardserver.service;

import com.maruhxn.boardserver.common.exception.ErrorCode;
import com.maruhxn.boardserver.common.exception.GlobalException;
import com.maruhxn.boardserver.domain.PostImage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {
    @Value("${file.upload_dir}")
    private String fileDir;

    /**
     * 파일 단건 저장
     *
     * @param file
     * @return
     */
    public PostImage storeOneFile(MultipartFile file) {
        if (file.isEmpty()) throw new GlobalException(ErrorCode.EMPTY_FILE);

        String originalFilename = file.getOriginalFilename(); // 파일 원본 이름
        String storeFileName = createStoreFileName(originalFilename); // 서버에 저장된 파일 이름 (랜덤)
        String savePath = getFullPath(storeFileName); // 서버에 저장된 경로
        try {
            file.transferTo(new File(savePath)); // 파일 저장
        } catch (IOException e) {
            throw new GlobalException(ErrorCode.INTERNAL_ERROR, e);
        }

        return PostImage.builder()
                .originalName(originalFilename)
                .storedName(storeFileName)
                .build();
    }

    public String saveAndExtractUpdatedProfileImage(MultipartFile profileImage) {
        if (profileImage.isEmpty()) throw new GlobalException(ErrorCode.EMPTY_FILE);

        String originalFilename = profileImage.getOriginalFilename(); // 파일 원본 이름
        String storeFileName = createStoreFileName(originalFilename); // 서버에 저장된 파일 이름 (랜덤)
        String savePath = getFullPath(storeFileName); // 서버에 저장된 경로
        try {
            profileImage.transferTo(new File(savePath)); // 파일 저장
        } catch (IOException e) {
            throw new GlobalException(ErrorCode.INTERNAL_ERROR, e);
        }

        return storeFileName;
    }

    /**
     * 파일 전체 저장
     *
     * @param multipartFiles
     * @return
     */
    public List<PostImage> storeFiles(List<MultipartFile> multipartFiles) {
        List<PostImage> storedFileURLResult = new ArrayList<>();
        multipartFiles.forEach(multipartFile ->
                storedFileURLResult.add(storeOneFile(multipartFile)));
        return storedFileURLResult;
    }

    /**
     * 이미지 조회
     *
     * @param fileName
     * @return
     */
    public Resource getImage(String fileName) {
        Resource resource = null;

        try {
            resource = new UrlResource("file:" + getFullPath(fileName));
        } catch (Exception e) {
            throw new GlobalException(ErrorCode.INTERNAL_ERROR, e);
        }
        return resource;
    }

    /**
     * 서버에 저장될 실제 경로 생성
     *
     * @param fileName
     * @return
     */
    private String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    /**
     * 서버에 저장될 파일명 생성
     *
     * @param originalFilename
     * @return
     */
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    /**
     * 파일 확장자 추출
     *
     * @param originalFilename
     * @return
     */
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    /**
     * 실제 물리 이미지 삭제
     *
     * @param storeFileName
     */
    public void deleteFile(String storeFileName) {
        String savePath = getFullPath(storeFileName); // 서버에 저장된 경로
        try {
            File file = new File(savePath);
            file.delete();
        } catch (Exception e) {
            throw new GlobalException(ErrorCode.INTERNAL_ERROR, e);
        }
    }
}
