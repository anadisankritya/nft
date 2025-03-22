package com.nft.app.service;

import com.mongodb.DuplicateKeyException;
import com.nft.app.dto.request.CreateUserLevelRequest;
import com.nft.app.dto.request.ImageData;
import com.nft.app.dto.request.UpdateUserLevelRequest;
import com.nft.app.dto.response.CreateUserLevelResponse;
import com.nft.app.entity.UserLevel;
import com.nft.app.enums.SequenceType;
import com.nft.app.exception.ErrorCode;
import com.nft.app.exception.InvestmentTypException;
import com.nft.app.exception.UserLevelException;
import com.nft.app.repository.UserLevelRepository;
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
public class UserLevelService {

    private final UserLevelRepository userLevelRepository;
    private final GridFsService gridFsService;
    private final SequenceGeneratorService sequenceGeneratorService;

    public List<CreateUserLevelResponse> getAllUserLevels(Integer page, Integer size) {
        if (Objects.nonNull(page) && Objects.nonNull(size)) {
            Pageable pageable = PageRequest.of(page, size);
            Page<UserLevel> investmentTypePage = userLevelRepository.findAll(pageable);

            List<UserLevel> content = investmentTypePage.getContent();
            List<String> imageIds = content.stream().map(UserLevel::getImageId).toList();
            List<ImageData> imageData = gridFsService.getFileDetailsByIds(imageIds);
            Map<String, ImageData> imageDataMap = imageData.stream().collect(Collectors.toMap(ImageData::getImageId, img -> img));
            return getCreateInvestmentResponses(content, imageDataMap);
        }
        return List.of();
    }

    private static List<CreateUserLevelResponse> getCreateInvestmentResponses(List<UserLevel> userLevels, Map<String, ImageData> imageDataMap) {

        List<CreateUserLevelResponse> userLevelResponses = new ArrayList<>();

        for (UserLevel userLevel : userLevels) {
            userLevelResponses.add(
                    new CreateUserLevelResponse(
                            userLevel, imageDataMap.get(userLevel.getImageId())
                    )
            );
        }
        return userLevelResponses;
    }

    public void createUserLevel(CreateUserLevelRequest userLevelRequest) {

        Optional<UserLevel> userLevel = userLevelRepository.findByName(
                userLevelRequest.getName()
        );
        if (userLevel.isPresent())
            throw new InvestmentTypException(ErrorCode.DUPLICATE_USER_LEVEL);

        try {
            Base64MultipartFileVo multipartFileVo = new Base64MultipartFileVo(
                    userLevelRequest.getImage().getImage(),
                    userLevelRequest.getImage().getName(),
                    userLevelRequest.getImage().getContentType());

            String checkSum = ChecksumUtil.getCheckSum(multipartFileVo.getBytes());

            Optional<UserLevel> userLevelCheckSum = userLevelRepository.findByCheckSum(checkSum);
            if (userLevelCheckSum.isPresent())
                throw new UserLevelException(ErrorCode.DUPLICATE_USER_LEVEL_IMAGE);
            String fileId = gridFsService.uploadFile(multipartFileVo);
            Long seqNo = sequenceGeneratorService.generateSequence(SequenceType.USER_LEVEL.name());
            UserLevel investmentTypeEntity = getUserLevel(userLevelRequest, seqNo, fileId, checkSum);
            userLevelRepository.save(investmentTypeEntity);
        } catch (UserLevelException e) {
          throw e;
        } catch (DuplicateKeyException e) {
            throw new InvestmentTypException(ErrorCode.DUPLICATE_USER_LEVEL);
        } catch (Exception e) {
            throw new InvestmentTypException(ErrorCode.CREATE_USER_LEVEL_FAILED);
        }
    }

    private static UserLevel getUserLevel(CreateUserLevelRequest userLevelRequest, Long seqNo,
                                          String fileId, String checkSum) {
        UserLevel userLevel = new UserLevel();
        userLevel.setSeq(seqNo);
        userLevel.setName(userLevelRequest.getName());
        userLevel.setImageId(fileId);
        userLevel.setBaseLevel(userLevelRequest.getBaseLevel());
        userLevel.setCheckSum(checkSum);
        return userLevel;
    }

    private static UserLevel getUserLevel(UpdateUserLevelRequest userLevelRequest, Long seqNo,
                                          String fileId, String checkSum) {
        UserLevel userLevel = new UserLevel();
        userLevel.setSeq(seqNo);
        userLevel.setName(userLevelRequest.getName());
        userLevel.setImageId(fileId);
        userLevel.setBaseLevel(userLevelRequest.getBaseLevel());
        userLevel.setCheckSum(checkSum);
        return userLevel;
    }

    public void updateUserLevel(UpdateUserLevelRequest userLevelRequest) {

        Optional<UserLevel> userLevelId = userLevelRepository.findById(userLevelRequest.getId());

        if (userLevelId.isEmpty())
            throw new UserLevelException(ErrorCode.USER_LEVEL_NOT_FOUND);

        Optional<UserLevel> userLevelName = userLevelRepository.findByName(
                userLevelRequest.getName()
        );
        if (userLevelName.isPresent())
            if (!userLevelName.get().getId().equals(userLevelId.get().getId()))
                throw new InvestmentTypException(ErrorCode.DUPLICATE_USER_LEVEL);

        boolean isDifferentFile = Boolean.TRUE;
        String checkSum = null;
        Base64MultipartFileVo multipartFileVo = null;
        if (Objects.nonNull(userLevelRequest.getImage())) {
             multipartFileVo = new Base64MultipartFileVo(
                    userLevelRequest.getImage().getImage(),
                    userLevelRequest.getImage().getName(),
                    userLevelRequest.getImage().getContentType());

             checkSum = ChecksumUtil.getCheckSum(multipartFileVo.getBytes());

            Optional<UserLevel> userLevelCheckSum = userLevelRepository.findByCheckSum(checkSum);
            if (userLevelCheckSum.isPresent()) {
                if (!userLevelCheckSum.get().getId().equals(userLevelId.get().getId()))
                    throw new UserLevelException(ErrorCode.DUPLICATE_USER_LEVEL_IMAGE);
                else
                    isDifferentFile = Boolean.FALSE;
            }
        } else {
            isDifferentFile = Boolean.FALSE;
            checkSum = userLevelId.get().getCheckSum();
        }

        try {
            String fileId;
            if (isDifferentFile)
                fileId = gridFsService.uploadFile(multipartFileVo);
            else
                fileId = userLevelId.get().getImageId();

            UserLevel investmentTypeEntity = getUserLevel(userLevelRequest,
                    userLevelId.get().getSeq(), fileId, checkSum);
            investmentTypeEntity.setId(userLevelId.get().getId());
            userLevelRepository.save(investmentTypeEntity);
        } catch (DuplicateKeyException | org.springframework.dao.DuplicateKeyException e) {
            throw new InvestmentTypException(ErrorCode.DUPLICATE_USER_LEVEL);
        } catch (Exception e) {
            throw new InvestmentTypException(ErrorCode.CREATE_USER_LEVEL_FAILED);
        }
    }

    public CreateUserLevelResponse getUserLevelById(String id) {
        Optional<UserLevel> userLevel = userLevelRepository.findById(id);
        if (userLevel.isEmpty())
            throw new InvestmentTypException(ErrorCode.INVALID_INVESTMENT_TYPE);
        List<ImageData> imageData = gridFsService.getFileDetailsByIds(List.of(userLevel.get().getImageId()));
        if (Objects.nonNull(imageData) && imageData.isEmpty())
            throw new InvestmentTypException(ErrorCode.INVALID_INVESTMENT_TYPE);
        return new CreateUserLevelResponse(
                userLevel.get(),
                imageData.getFirst()
        );
    }

    public void deleteInvestmentType(String id) {
        Optional<UserLevel> userLevel = userLevelRepository.findById(id);
        if (userLevel.isEmpty())
            throw new UserLevelException(ErrorCode.USER_LEVEL_NOT_FOUND);
        gridFsService.deleteFile(userLevel.get().getImageId());
        userLevelRepository.deleteById(id);
    }
}