package com.fast_campus_12.not_found.shop.dao;

import com.fast_campus_12.not_found.shop.mapper.QnaMapper;
import com.fast_campus_12.not_found.shop.qna.dto.QnaDto;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public class QnaDAO {

    private final QnaMapper qnaMapper;

    public QnaDAO(QnaMapper qnaMapper) {
        this.qnaMapper = qnaMapper;
    }

    public List<QnaDto> getAllQnas() {
        return qnaMapper.getAllQnas();
    }

    public List<QnaDto> getQnaByUserId(BigInteger userId) {
        return qnaMapper.getQnaByUserId(userId);
    }
}
