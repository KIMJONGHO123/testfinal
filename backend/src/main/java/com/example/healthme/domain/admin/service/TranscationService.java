package com.example.healthme.domain.admin.service;
import com.example.healthme.domain.admin.dto.TranscationStatusDto;
import com.example.healthme.domain.admin.entity.TransactionHistoryEntity;
import com.example.healthme.domain.admin.repository.TranscationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TranscationService {
    
    @Autowired
    private TranscationRepository transcationRepository;

    // 페이지네이션된 거래 내역 가져오기
    public Page<TransactionHistoryEntity> selectAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page,size, Sort.by(Sort.Direction.DESC, "transcationTime"));

        return transcationRepository.findAll(pageRequest);
    }

    // 거래 취소 or 거래 완료
    public boolean update_transcations(TranscationStatusDto transStatusDto) {
        Optional<TransactionHistoryEntity> thEntity =transcationRepository.findById(transStatusDto.getNo());
        if (!thEntity.isEmpty()){
            TransactionHistoryEntity tranHisEntity =thEntity.get();
            tranHisEntity.setCancel(transStatusDto.getCancel());
            tranHisEntity.setSuccess(transStatusDto.getSuccess());
            transcationRepository.save(tranHisEntity);
            return true;
        }
        return false;

    }

    public Page<TransactionHistoryEntity> select_trans_name(String searchText, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page,size,Sort.by(Sort.Direction.DESC, "transcationTime"));
        return transcationRepository.findByTranscationPeopleContaining(searchText, pageRequest);
    }
}
