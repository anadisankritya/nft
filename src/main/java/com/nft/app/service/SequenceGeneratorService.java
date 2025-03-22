package com.nft.app.service;

import com.nft.app.entity.DatabaseSequence;
import com.nft.app.exception.ErrorCode;
import com.nft.app.exception.SequenceGeneratorException;
import com.nft.app.repository.SequenceGeneratorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
@RequiredArgsConstructor
@Slf4j
public class SequenceGeneratorService {

    private final SequenceGeneratorRepository sequenceGeneratorRepository;


    public synchronized long generateSequence(String seqName) {
        Optional<DatabaseSequence> seq = sequenceGeneratorRepository.findBySeqName(seqName);
        try {
            if (seq.isEmpty()) {
                DatabaseSequence databaseSequence = new DatabaseSequence();
                databaseSequence.setSeq(1);
                databaseSequence.setStatus(Boolean.TRUE);
                databaseSequence.setSeqName(seqName);
                sequenceGeneratorRepository.save(databaseSequence);
                return databaseSequence.getSeq();
            }
            long id = seq.get().getSeq()+1;
            seq.get().setSeq(id);
            sequenceGeneratorRepository.save(seq.get());
            return id;
        } catch (Exception e) {
            throw new SequenceGeneratorException(ErrorCode.GENERIC_EXCEPTION);
        }
    }
}