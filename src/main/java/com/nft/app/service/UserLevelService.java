package com.nft.app.service;

import com.mongodb.DuplicateKeyException;
import com.nft.app.dto.request.CreateUserLevelRequest;
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

@Service
@RequiredArgsConstructor
public class UserLevelService {

    private final UserLevelRepository userLevelRepository;
    private final GridFsService gridFsService;
    private final SequenceGeneratorService sequenceGeneratorService;

    public List<CreateUserLevelResponse> getAllUserLevels(Integer page, Integer size) {
        if (Objects.nonNull(page) && Objects.nonNull(size)) {
            if (page == 0) {
                List<UserLevel> userLevels = userLevelRepository.findAll();
                return getCreateInvestmentResponses(userLevels);
            } else {
                Pageable pageable = PageRequest.of(page - 1, size);
                Page<UserLevel> investmentTypePage = userLevelRepository.findAll(pageable);
                return getCreateInvestmentResponses(investmentTypePage.getContent());
            }
        }
        return List.of();
    }

    private static List<CreateUserLevelResponse> getCreateInvestmentResponses(List<UserLevel> userLevels) {
        return userLevels.stream().map(CreateUserLevelResponse::new).toList();
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
        } catch (DuplicateKeyException e) {
            throw new InvestmentTypException(ErrorCode.DUPLICATE_USER_LEVEL);
        } catch (Exception e) {
            throw new InvestmentTypException(ErrorCode.CREATE_USER_LEVEL_FAILED);
        }
    }

    private static UserLevel getUserLevel(CreateUserLevelRequest userLevelRequest, Long seqNo,
                                          String fileId, String checkSum) {
        UserLevel investmentTypeEntity = new UserLevel();
        investmentTypeEntity.setSeq(seqNo);
        investmentTypeEntity.setName(userLevelRequest.getName());
        investmentTypeEntity.setImageId(fileId);
        investmentTypeEntity.setBaseLevel(userLevelRequest.getBaseLevel());
        investmentTypeEntity.setCheckSum(checkSum);
        return investmentTypeEntity;
    }

    public void updateUserLevel(CreateUserLevelRequest userLevelRequest) {

        Optional<UserLevel> userLevelId = userLevelRepository.findById(userLevelRequest.getId());

        if (userLevelId.isEmpty())
            throw new UserLevelException(ErrorCode.USER_LEVEL_NOT_FOUND);

        Optional<UserLevel> userLevelName = userLevelRepository.findByName(
                userLevelRequest.getName()
        );
        if (userLevelName.isPresent())
            if (!userLevelName.get().getId().equals(userLevelId.get().getId()))
                throw new InvestmentTypException(ErrorCode.DUPLICATE_USER_LEVEL);

        Base64MultipartFileVo multipartFileVo = new Base64MultipartFileVo(
                userLevelRequest.getImage().getImage(),
                userLevelRequest.getImage().getName(),
                userLevelRequest.getImage().getContentType());
        String checkSum = ChecksumUtil.getCheckSum(multipartFileVo.getBytes());

        Optional<UserLevel> userLevelCheckSum = userLevelRepository.findByCheckSum(checkSum);
        boolean isDifferentFile = Boolean.TRUE;
        if (userLevelCheckSum.isPresent()) {
            if (!userLevelCheckSum.get().getId().equals(userLevelId.get().getId()))
                throw new UserLevelException(ErrorCode.DUPLICATE_USER_LEVEL_IMAGE);
            else
                isDifferentFile = Boolean.FALSE;
        }

        try {
            String fileId;
            if (isDifferentFile)
                fileId = gridFsService.uploadFile(multipartFileVo);
            else
                fileId = userLevelId.get().getImageId();
            Long seqNo = sequenceGeneratorService.generateSequence(SequenceType.USER_LEVEL.name());
            UserLevel investmentTypeEntity = getUserLevel(userLevelRequest, seqNo, fileId, checkSum);
            userLevelRepository.save(investmentTypeEntity);
        } catch (DuplicateKeyException e) {
            throw new InvestmentTypException(ErrorCode.DUPLICATE_USER_LEVEL);
        } catch (Exception e) {
            throw new InvestmentTypException(ErrorCode.CREATE_USER_LEVEL_FAILED);
        }
    }

    public CreateUserLevelResponse getUserLevelById(String id) {
        Optional<UserLevel> userLevel = userLevelRepository.findById(id);
        if (userLevel.isEmpty())
            throw new InvestmentTypException(ErrorCode.INVALID_INVESTMENT_TYPE);
        return new CreateUserLevelResponse(
                userLevel.get()
        );
    }

    public void deleteInvestmentType(String id) {
        userLevelRepository.deleteById(id);
    }
}