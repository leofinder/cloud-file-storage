package com.craftelix.filestorage.mapper;

import com.craftelix.filestorage.dto.DataInfoDto;
import com.craftelix.filestorage.dto.DataResponseDto;
import com.craftelix.filestorage.entity.DataInfo;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DataInfoMapper {

    List<DataResponseDto> toDto(List<DataInfo> dataInfos);

    DataInfo toEntity(DataInfoDto dataInfoDto);
}
