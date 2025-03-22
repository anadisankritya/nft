package com.nft.app.mapper;


import com.nft.app.dto.InvestmentTypeDto;
import com.nft.app.entity.InvestmentType;
import java.util.Optional;

public class InvestmentTypeMapper {

    // Convert Entity to DTO
    public static InvestmentTypeDto toDto(InvestmentType investmentType) {
        InvestmentTypeDto dto = new InvestmentTypeDto();
        dto.set_id(investmentType.get_id());
        dto.setName(investmentType.getName());
        dto.setMetadata(investmentType.getMetadata());
        return dto;
    }

    // Convert DTO to Entity
    public static InvestmentType toEntity(InvestmentTypeDto dto) {
        InvestmentType entity = new InvestmentType();
        entity.set_id(dto.get_id());
        entity.setName(dto.getName());
        entity.setMetadata(dto.getMetadata());
        return entity;
    }

    // Optional Conversion for findById
    public static InvestmentTypeDto toDto(Optional<InvestmentType> optionalEntity) {
        return optionalEntity.map(InvestmentTypeMapper::toDto).orElse(null);
    }
}