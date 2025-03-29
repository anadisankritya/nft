package com.nft.app.service;

import com.mongodb.DuplicateKeyException;
import com.nft.app.dto.request.CreateInvestmentRequest;
import com.nft.app.dto.response.CreateInvestmentResponse;
import com.nft.app.dto.response.PageResponse;
import com.nft.app.entity.InvestmentType;
import com.nft.app.entity.UserLevel;
import com.nft.app.exception.ErrorCode;
import com.nft.app.exception.InvestmentTypException;
import com.nft.app.repository.InvestmentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class InvestmentTypeService {

    private final InvestmentTypeRepository investmentTypeRepository;
    private final UserLevelService userLevelService;


    public PageResponse<List<CreateInvestmentResponse>> getAllInvestmentTypes(Integer page, Integer size) {
        if (Objects.nonNull(page) && Objects.nonNull(size)) {
            Pageable pageable = PageRequest.of(page, size);
            Page<InvestmentType> investmentTypePage = investmentTypeRepository.findAll(pageable);
            List<CreateInvestmentResponse> createInvestmentResponses = getCreateInvestmentResponses(investmentTypePage.getContent());
            return new PageResponse<>(
                    investmentTypePage.getTotalElements(),
                    createInvestmentResponses
            );
        }
        List<InvestmentType> allResult = investmentTypeRepository.findAll();
        List<CreateInvestmentResponse> response = getCreateInvestmentResponses(
                allResult
        );
        return new PageResponse<>((long) response.size(), response);
    }

    private List<CreateInvestmentResponse> getCreateInvestmentResponses(List<InvestmentType> investmentTypes) {
        List<CreateInvestmentResponse> responses = new ArrayList<>();
        for (InvestmentType investmentType : investmentTypes) {
            responses.add(new CreateInvestmentResponse(investmentType));
        }
        return responses;
    }

    public void createInvestmentType(CreateInvestmentRequest createInvestmentRequest) {

        createInvestmentRequest.setName(createInvestmentRequest.getName().toUpperCase());

        Optional<InvestmentType> investmentType = investmentTypeRepository.findByName(
                createInvestmentRequest.getName()
        );
        if (investmentType.isPresent())
            throw new InvestmentTypException(ErrorCode.DUPLICATE_INVESTMENT);
        try {

            List<UserLevel> userLevels = userLevelService.getUserLevelByIdIn(
                    createInvestmentRequest.getLevels()
            );
            if (userLevels.isEmpty())
                throw new InvestmentTypException(
                        ErrorCode.INVALID_LEVEL_INSERTED
                );
            createInvestmentRequest.setUserLevels(userLevels);
            InvestmentType investmentTypeEntity = new InvestmentType(createInvestmentRequest);
            investmentTypeRepository.save(investmentTypeEntity);
        } catch (DuplicateKeyException e) {
            throw new InvestmentTypException(ErrorCode.DUPLICATE_INVESTMENT);
        } catch (InvestmentTypException e) {
           throw e;
        }  catch (Exception e) {
            throw new InvestmentTypException(ErrorCode.CREATE_INVESTMENT_FAILED);
        }
    }

    public void updateInvestmentType(CreateInvestmentRequest createInvestmentRequest) {

        Optional<InvestmentType> investmentTypeById = investmentTypeRepository.findById(createInvestmentRequest.getId());
        if (investmentTypeById.isEmpty())
            throw new InvestmentTypException(ErrorCode.INVALID_INVESTMENT_TYPE);

        Optional<InvestmentType> investmentTypeByName = investmentTypeRepository.findByName(
                createInvestmentRequest.getName()
        );
        if (investmentTypeByName.isPresent()) {
            if (!investmentTypeByName.get().getId().equals(
                    investmentTypeById.get().getId()))
                throw new InvestmentTypException(ErrorCode.DUPLICATE_INVESTMENT);
        }
        try {
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