package com.example.healthme.domain.admin.controller;

import com.example.healthme.domain.admin.entity.TransactionHistoryEntity;
import com.example.healthme.domain.admin.service.TranscationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trans")
public class PaginationTranscationController {

    @Autowired
    private TranscationService transcationService;

    // 페이지내이션된 거래내역 가져오기
    @GetMapping("/selectAll")
    public Page<TransactionHistoryEntity> select_transcation(@RequestParam int page, @RequestParam int size){
        Page<TransactionHistoryEntity> trans_select = transcationService.selectAll(page,size);
        return trans_select;
    }

}
