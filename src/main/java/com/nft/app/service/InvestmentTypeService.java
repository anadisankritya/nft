package com.nft.app.service;

import com.nft.app.entity.InvestmentType;
import com.nft.app.repository.InvestmentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvestmentTypeService {

    @Autowired
    private InvestmentTypeRepository investmentTypeRepository;

    public List<InvestmentType> getAllInvestmentTypes(int page, int size) {
        if (page == 0) {
            return investmentTypeRepository.findAll();
        } else {
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<InvestmentType> investmentTypePage = investmentTypeRepository.findAll(pageable);
            return investmentTypePage.getContent();
        }
    }

    public InvestmentType createInvestmentType(InvestmentType investmentType) {
        return investmentTypeRepository.save(investmentType);
    }

    public Optional<InvestmentType> getInvestmentTypeById(String id) {
        return investmentTypeRepository.findById(id);
    }

    public InvestmentType updateInvestmentType(String id, InvestmentType updatedInvestmentType) {
        return investmentTypeRepository.findById(id)
                .map(existingInvestmentType -> {
                    existingInvestmentType.setName(updatedInvestmentType.getName());
                    existingInvestmentType.setMetadata(updatedInvestmentType.getMetadata());
                    return investmentTypeRepository.save(existingInvestmentType);
                })
                .orElseThrow(() -> new RuntimeException("InvestmentType not found with ID: " + id));
    }

    public void deleteInvestmentType(String id) {
        investmentTypeRepository.deleteById(id);
    }
}