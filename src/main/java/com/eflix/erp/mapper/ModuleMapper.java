package com.eflix.erp.mapper;

import java.util.List;

import com.eflix.erp.dto.ModuleDTO;

/**
 * 구독 모듈 관리 매퍼 인터페이스
 * <p>
 * 이 인터페이스는 MyBatis를 사용하여 구독 관련 데이터베이스 작업을 처리하는
 * 매퍼 메서드들을 정의합니다.
 * <br><br>
 * 구독 모듈 등록, 조회, 수정, 삭제 등의 CRUD 작업과 구독 상태 관리
 * </p>
 *
 * <h3>주요 기능</h3>
 * <ul>
 *   <li><b>모듈 등록</b> : 새로운 모듈 정보를 데이터베이스에 저장</li>
 *   <li><b>모듈 조회</b> : 사용자별, 상품별 구독 모듈 정보 조회</li>
 * </ul>
 *
 * @author 복성민 (bokseongmin@gmail.com)
 * @version 1.0
 * @since 2025-06-23
 *
 * @see com.eflix.erp.domain.Subscribe
 * @see com.eflix.erp.service.SubscribeService
 *
 * @changelog
 * <ul>
 *   <li>2025-06-23 최초 생성 (복성민)</li>
 * </ul>
 */

public interface ModuleMapper {
    public List<ModuleDTO> findAll();
}
