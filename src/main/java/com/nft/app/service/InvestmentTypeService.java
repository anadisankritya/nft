package com.nft.app.service;

import com.mongodb.DuplicateKeyException;
import com.nft.app.dto.request.CreateInvestmentRequest;
import com.nft.app.dto.response.CreateInvestmentResponse;
import com.nft.app.entity.InvestmentType;
import com.nft.app.exception.ErrorCode;
import com.nft.app.exception.InvestmentTypException;
import com.nft.app.repository.InvestmentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class InvestmentTypeService {

    @Autowired
    private InvestmentTypeRepository investmentTypeRepository;

    public List<CreateInvestmentResponse> getAllInvestmentTypes(Integer page, Integer size) {
        if (Objects.nonNull(page) && Objects.nonNull(size)) {
            Pageable pageable = PageRequest.of(page, size);
            Page<InvestmentType> investmentTypePage = investmentTypeRepository.findAll(pageable);
            return getCreateInvestmentResponses(investmentTypePage.getContent());
        }
        return List.of();
    }

    private static List<CreateInvestmentResponse> getCreateInvestmentResponses(List<InvestmentType> investmentTypes) {
        return investmentTypes.stream().map(CreateInvestmentResponse::new).toList();
    }

    public void createInvestmentType(CreateInvestmentRequest createInvestmentRequest) {
        try {
            List<String> allowedLevels = List.of(createInvestmentRequest.getAllowedLevel().split(","));
            createInvestmentRequest.setAllowedLevels(allowedLevels);
        } catch (Exception e) {
            throw new InvestmentTypException(ErrorCode.INVALID_LEVEL_INSERTED);
        }

        Optional<InvestmentType> investmentType = investmentTypeRepository.findByName(
                createInvestmentRequest.getName()
        );
        if (investmentType.isPresent())
            throw new InvestmentTypException(ErrorCode.DUPLICATE_INVESTMENT);
        try {
            InvestmentType investmentTypeEntity = new InvestmentType(createInvestmentRequest);
            investmentTypeRepository.save(investmentTypeEntity);
        } catch (DuplicateKeyException e) {
            throw new InvestmentTypException(ErrorCode.DUPLICATE_INVESTMENT);
        } catch (Exception e) {
            throw new InvestmentTypException(ErrorCode.CREATE_INVESTMENT_FAILED);
        }
    }

    public void updateInvestmentType(CreateInvestmentRequest createInvestmentRequest) {

        Optional<InvestmentType> investmentTypeById = investmentTypeRepository.findById(createInvestmentRequest.getId());
        if (investmentTypeById.isEmpty())
            throw new InvestmentTypException(ErrorCode.INVALID_INVESTMENT_TYPE);

        try {
            List<String> allowedLevels = List.of(createInvestmentRequest.getAllowedLevel().split(","));
            createInvestmentRequest.setAllowedLevels(allowedLevels);
        } catch (Exception e) {
            throw new InvestmentTypException(ErrorCode.INVALID_LEVEL_INSERTED);
        }

        Optional<InvestmentType> investmentTypeByName = investmentTypeRepository.findByName(
                createInvestmentRequest.getName()
        );
        if (investmentTypeByName.isPresent()) {
            if (!investmentTypeByName.get().getId().equals(
                    investmentTypeById.get().getId()))
                throw new InvestmentTypException(ErrorCode.DUPLICATE_INVESTMENT);
        }
        try {
            investmentTypeById.get().setAllowedLevels(createInvestmentRequest.getAllowedLevels());
            investmentTypeById.get().setName(createInvestmentRequest.getName());
            investmentTypeRepository.save(investmentTypeById.get());
        } catch (DuplicateKeyException e) {
            throw new InvestmentTypException(ErrorCode.DUPLICATE_INVESTMENT);
        } catch (Exception e) {
            throw new InvestmentTypException(ErrorCode.UPDATE_INVESTMENT_FAILED);
        }
    }

    public CreateInvestmentResponse getInvestmentTypeById(String id) {
        Optional<InvestmentType> investmentType = investmentTypeRepository.findById(id);
        if (investmentType.isEmpty())
            throw new InvestmentTypException(ErrorCode.INVALID_INVESTMENT_TYPE);
        return new CreateInvestmentResponse(
                investmentType.get()
        );
    }

//    public InvestmentType updateInvestmentType(String id, InvestmentType updatedInvestmentType) {
//        return investmentTypeRepository.findById(id)
//                .map(existingInvestmentType -> {
//                    existingInvestmentType.setName(updatedInvestmentType.getName());
//                    existingInvestmentType.setMetadata(updatedInvestmentType.getMetadata());
//                    return investmentTypeRepository.save(existingInvestmentType);
//                })
//                .orElseThrow(() -> new RuntimeException("InvestmentType not found with ID: " + id));
//    }

    public void deleteInvestmentType(String id) {
        investmentTypeRepository.deleteById(id);
    }
}