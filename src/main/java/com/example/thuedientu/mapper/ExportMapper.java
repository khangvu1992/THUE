package com.example.thuedientu.mapper;

import com.example.thuedientu.dto.ExportDTO;
import com.example.thuedientu.model.ExportEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ExportMapper {

    ExportMapper INSTANCE = Mappers.getMapper(ExportMapper.class);

    ExportDTO toDTO(ExportEntity entity);

    ExportEntity toEntity(ExportDTO dto);
}
