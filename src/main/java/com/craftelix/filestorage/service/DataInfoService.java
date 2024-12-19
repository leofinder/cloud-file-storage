package com.craftelix.filestorage.service;

import com.craftelix.filestorage.dto.DataInfoDto;
import com.craftelix.filestorage.dto.DataResponseDto;
import com.craftelix.filestorage.entity.DataInfo;
import com.craftelix.filestorage.exception.PathNotFoundException;
import com.craftelix.filestorage.mapper.DataInfoMapper;
import com.craftelix.filestorage.repository.DataInfoRepository;
import com.craftelix.filestorage.util.PathUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DataInfoService {

    private final DataInfoRepository dataInfoRepository;

    private final DataInfoMapper dataInfoMapper;

    private final UserService userService;

    public List<DataResponseDto> findByPath(String path, Long userId) {
        if (!path.equals("/") && !isDataInfoExist(path, true, userId)) {
            throw new PathNotFoundException("The folder at path '" + path + "' was not found or is inaccessible for the user with ID " + userId + ".");
        }

        Sort sort = Sort.by(Sort.Order.desc("isFolder"), Sort.Order.asc("name"));
        List<DataInfo> dataInfos = dataInfoRepository.findByParentPathAndUserId(path, userId, sort);
        return dataInfoMapper.toDto(dataInfos);
    }

    public List<DataResponseDto> findByName(String name, Long userId) {
        Sort sort = Sort.by(Sort.Order.desc("isFolder"), Sort.Order.asc("name"));
        List<DataInfo> dataInfos = dataInfoRepository.findByNameIgnoreCaseContainingAndUserId(name, userId, sort);
        return dataInfoMapper.toDto(dataInfos);
    }

    public void saveMetadata(DataInfoDto dataInfoDto, Long userId) {
        DataInfo dataInfo = dataInfoMapper.toEntity(dataInfoDto);
        dataInfo.setUser(userService.findByUserId(userId));

        try {
            dataInfoRepository.save(dataInfo);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void saveMetadataWithIntermediateFolders(DataInfoDto dataInfoDto, Long userId) {
        saveIntermediateFoldersMetadata(dataInfoDto.getParentPath(), userId);
        saveMetadata(dataInfoDto, userId);
    }

    @Transactional
    public void updateMetadata(String oldPath, String newPath, String newName, Long userId) {
        try {
            dataInfoRepository.updateByPath(oldPath, newPath, newName, userId);
            dataInfoRepository.updateByParentPathPrefix(oldPath, newPath, userId);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteMetadata(String path, Long userId) {
        try {
            dataInfoRepository.deleteByPathPrefix(path, userId);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveIntermediateFoldersMetadata(String parentPath, Long userId) {
        List<String> folderNames = PathUtil.splitPath(parentPath);
        String accumulatedPath = "/";

        for (String folderName : folderNames) {
            if (folderName.isEmpty()) {
                continue;
            }
            DataInfoDto folderInfoDto = new DataInfoDto(folderName, accumulatedPath, true, null);
            saveIfNotExist(folderInfoDto, userId);
            accumulatedPath = PathUtil.getFullPath(accumulatedPath, folderName, true);
        }
    }

    private void saveIfNotExist(DataInfoDto dataInfoDto, Long userId) {
        if (!isDataInfoExist(dataInfoDto.getPath(), dataInfoDto.getIsFolder(), userId)) {
            saveMetadata(dataInfoDto, userId);
        }
    }

    private boolean isDataInfoExist(String path, boolean isFolder, Long userId) {
        return dataInfoRepository.existsByPathAndIsFolderAndUserId(path, isFolder, userId);
    }

}
