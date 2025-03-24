package com.nft.app.service;

import com.mongodb.DuplicateKeyException;
import com.nft.app.dto.request.CreateNFTRequest;
import com.nft.app.dto.request.ImageData;
import com.nft.app.dto.response.CreateNFTResponse;
import com.nft.app.entity.NFTDetails;
import com.nft.app.enums.SequenceType;
import com.nft.app.exception.ErrorCode;
import com.nft.app.exception.InvestmentTypException;
import com.nft.app.exception.NftException;
import com.nft.app.repository.NFTRepository;
import com.nft.app.util.ChecksumUtil;
import com.nft.app.vo.Base64MultipartFileVo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NFTService {

    private final NFTRepository nftRepository;
    private final GridFsService gridFsService;
    private final SequenceGeneratorService sequenceGeneratorService;

    public List<CreateNFTResponse> getAllNFTDetails(Integer page, Integer size) {
        if (Objects.nonNull(page) && Objects.nonNull(size)) {
            Pageable pageable = PageRequest.of(page, size);
            Page<NFTDetails> investmentTypePage = nftRepository.findAll(pageable);

            List<NFTDetails> content = investmentTypePage.getContent();
            List<String> imageIds = content.stream().map(NFTDetails::getImageId).toList();
            List<ImageData> imageData = gridFsService.getFileDetailsByIds(imageIds);
            Map<String, ImageData> imageDataMap = imageData.stream().collect(Collectors.toMap(ImageData::getImageId, img -> img));
            return getCreateInvestmentResponses(content, imageDataMap);
        }
        return List.of();
    }

    private static List<CreateNFTResponse> getCreateInvestmentResponses(List<NFTDetails> NFTDetailss, Map<String, ImageData> imageDataMap) {

        List<CreateNFTResponse> NFTDetailsResponses = new ArrayList<>();

        for (NFTDetails NFTDetails : NFTDetailss) {
            NFTDetailsResponses.add(
                    new CreateNFTResponse(

                    )
            );
        }
        return NFTDetailsResponses;
    }

    public void createNFTDetails(CreateNFTRequest NFTDetailsRequest) {

        Optional<NFTDetails> NFTDetails = nftRepository.findByName(
                NFTDetailsRequest.getName().toUpperCase(Locale.ROOT)
        );
        if (NFTDetails.isPresent())
            throw new InvestmentTypException(ErrorCode.DUPLICATE_USER_LEVEL);

        try {
            Base64MultipartFileVo multipartFileVo = new Base64MultipartFileVo(
                    NFTDetailsRequest.getImage().getImage(),
                    NFTDetailsRequest.getImage().getName(),
                    NFTDetailsRequest.getImage().getContentType());

            String checkSum = ChecksumUtil.getCheckSum(multipartFileVo.getBytes());

            Optional<NFTDetails> NFTDetailsCheckSum = nftRepository.findByCheckSum(checkSum);
            if (NFTDetailsCheckSum.isPresent())
                throw new RuntimeException("");
            String fileId = gridFsService.uploadFile(multipartFileVo);
            Long seqNo = sequenceGeneratorService.generateSequence(SequenceType.USER_LEVEL.name());
            NFTDetails investmentTypeEntity = getNFTDetails(NFTDetailsRequest, seqNo, fileId, checkSum);
            nftRepository.save(investmentTypeEntity);
        } catch (NftException e) {
          throw e;
        } catch (DuplicateKeyException e) {
            throw new InvestmentTypException(ErrorCode.DUPLICATE_USER_LEVEL);
        } catch (Exception e) {
            throw new InvestmentTypException(ErrorCode.CREATE_USER_LEVEL_FAILED);
        }
    }

    private static NFTDetails getNFTDetails(CreateNFTRequest NFTDetailsRequest, Long seqNo,
                                          String fileId, String checkSum) {
        NFTDetails NFTDetails = new NFTDetails();
        NFTDetails.setName(NFTDetailsRequest.getName().toUpperCase(Locale.ROOT));
        NFTDetails.setImageId(fileId);
        NFTDetails.setCheckSum(checkSum);
        return NFTDetails;
    }


    public CreateNFTResponse getNFTDetailsById(String id) {
        Optional<NFTDetails> NFTDetails = nftRepository.findById(id);
        if (NFTDetails.isEmpty())
            throw new InvestmentTypException(ErrorCode.INVALID_INVESTMENT_TYPE);
        List<ImageData> imageData = gridFsService.getFileDetailsByIds(List.of(NFTDetails.get().getImageId()));
        if (Objects.nonNull(imageData) && imageData.isEmpty())
            throw new InvestmentTypException(ErrorCode.INVALID_INVESTMENT_TYPE);
        return new CreateNFTResponse();
    }


    public void deleteNFTDetails(String id) {
        Optional<NFTDetails> NFTDetails = nftRepository.findById(id);
        if (NFTDetails.isEmpty())
            throw new RuntimeException("");
        gridFsService.deleteFile(NFTDetails.get().getImageId());
        nftRepository.deleteById(id);
    }
}