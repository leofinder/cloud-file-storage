package com.craftelix.filestorage.service;

import com.craftelix.filestorage.dto.DataInfoDto;
import com.craftelix.filestorage.dto.DataRenameRequestDto;
import com.craftelix.filestorage.dto.DataRequestDto;
import com.craftelix.filestorage.dto.DataStreamResponseDto;
import com.craftelix.filestorage.exception.MinioObjectAlreadyExistsException;
import com.craftelix.filestorage.exception.MinioObjectNotFoundException;
import com.craftelix.filestorage.exception.MinioPathNotFoundException;
import com.craftelix.filestorage.util.PathUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileManagerService {

    private final MinioService minioService;

    private final DataInfoService dataInfoService;

    public void createFolder(DataRequestDto dataRequestDto, Long userId) {
        validateParentPath(dataRequestDto.getParentPath(), userId);

        String path = PathUtil.getFullPath(dataRequestDto.getParentPath(), dataRequestDto.getName(), true);
        String minioPath = PathUtil.getMinioPath(path, userId);

        validateObjectNotExists(minioPath, dataRequestDto.getName(), dataRequestDto.getParentPath());

        minioService.createFolder(minioPath);

        DataInfoDto dataInfoDto = new DataInfoDto(dataRequestDto.getName(), dataRequestDto.getParentPath(), true, null);

        dataInfoService.saveMetadata(dataInfoDto, userId);
    }

    public void uploadFiles(MultipartFile[] files, String parentPath, Long userId) {
        validateParentPath(parentPath, userId);

        for (MultipartFile file : files) {
            String filename = file.getOriginalFilename();
            String path = PathUtil.getFullPath(parentPath, filename, false);
            String minioPath = PathUtil.getMinioPath(path, userId);

            validateObjectNotExists(minioPath, PathUtil.getFilename(path), PathUtil.getParentPath(path));

            minioService.uploadFile(minioPath, file);

            DataInfoDto dataInfoDto = new DataInfoDto(PathUtil.getFilename(path), PathUtil.getParentPath(path), false, file.getSize());

            dataInfoService.saveMetadataWithIntermediateFolders(dataInfoDto, userId);

        }
    }

    public void rename(DataRenameRequestDto dataRenameRequestDto, Long userId) {
        validateParentPath(dataRenameRequestDto.getParentPath(), userId);

        String oldPath = PathUtil.getFullPath(dataRenameRequestDto.getParentPath(), dataRenameRequestDto.getName(), dataRenameRequestDto.getIsFolder());
        String newPath = PathUtil.getFullPath(dataRenameRequestDto.getParentPath(), dataRenameRequestDto.getNewName(), dataRenameRequestDto.getIsFolder());

        String minioOldPath = PathUtil.getMinioPath(oldPath, userId);
        String minioNewPath = PathUtil.getMinioPath(newPath, userId);

        validateObjectExists(minioOldPath, dataRenameRequestDto.getName(), dataRenameRequestDto.getParentPath());
        validateObjectNotExists(minioNewPath, dataRenameRequestDto.getNewName(), dataRenameRequestDto.getParentPath());

        if (dataRenameRequestDto.getIsFolder()) {
            minioService.renameFolder(minioOldPath, minioNewPath);
        } else {
            minioService.renameFile(minioOldPath, minioNewPath);
        }

        dataInfoService.updateMetadata(oldPath, newPath, dataRenameRequestDto.getNewName(), userId);
    }

    public void delete(DataRequestDto dataRequestDto, Long userId) {
        validateParentPath(dataRequestDto.getParentPath(), userId);

        String path = PathUtil.getFullPath(dataRequestDto.getParentPath(), dataRequestDto.getName(), dataRequestDto.getIsFolder());
        String minioPath = PathUtil.getMinioPath(path, userId);

        validateObjectExists(minioPath, dataRequestDto.getName(), dataRequestDto.getParentPath());

        if (dataRequestDto.getIsFolder()) {
            minioService.deleteFolder(minioPath);
        } else {
            minioService.deleteFile(minioPath);
        }

        dataInfoService.deleteMetadata(path, userId);
    }

    public DataStreamResponseDto download(DataRequestDto dataRequestDto, Long userId) {
        validateParentPath(dataRequestDto.getParentPath(), userId);

        String filename = dataRequestDto.getName();
        String path = PathUtil.getFullPath(dataRequestDto.getParentPath(), dataRequestDto.getName(), dataRequestDto.getIsFolder());
        String minioPath = PathUtil.getMinioPath(path, userId);

        validateObjectExists(minioPath, filename, dataRequestDto.getParentPath());

        DataStreamResponseDto dataStreamResponseDto = new DataStreamResponseDto();

        if (dataRequestDto.getIsFolder()) {
            dataStreamResponseDto.setFilename(filename + ".zip");
            dataStreamResponseDto.setResource(minioService.getFolderAsZipStream(minioPath, filename));
        } else {
            dataStreamResponseDto.setFilename(filename);
            dataStreamResponseDto.setResource(minioService.getFileAsStream(minioPath));
        }

        return dataStreamResponseDto;
    }

    private void validateParentPath(String path, Long userId) {
        String minioPath = PathUtil.getMinioPath(path, userId);
        if (!path.equals("/") && !isMinioPathExists(minioPath)) {
            throw new MinioPathNotFoundException("The folder at path '" + path + "' was not found or is inaccessible for the user with ID " + userId + ".");
        }
    }

    private void validateObjectExists(String minioPath, String name, String parentPath) {
        if (!isMinioPathExists(minioPath)) {
            throw new MinioObjectNotFoundException("Object '" + name + "' not found at '" + parentPath + "'");
        }
    }

    private void validateObjectNotExists(String minioPath, String name, String parentPath) {
        if (isMinioPathExists(minioPath)) {
            throw new MinioObjectAlreadyExistsException("Object '" + name + "' already exists at '" + parentPath + "'");
        }
    }

    private boolean isMinioPathExists(String path) {
        return minioService.isObjectExist(path) || minioService.isPrefixExist(path);
    }

}
