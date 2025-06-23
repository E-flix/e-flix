package com.eflix.common.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 상품 정보 데이터 클래스
 * <p>
 * 결제 대상 상품의 기본 정보를 담는 데이터 전송 객체(DTO)입니다.
 * <br><br>
 * 상품 ID, 이름, 가격, 통화 정보를 포함하며, 결제 검증 시
 * 실제 결제 데이터와 비교하는데 사용됩니다.
 * </p>
 *
 * <h3>필드 설명</h3>
 * <ul>
 *   <li><b>id</b> : 상품 고유 식별자</li>
 *   <li><b>name</b> : 상품명 (주문명으로 사용)</li>
 *   <li><b>price</b> : 상품 가격 (정수형)</li>
 *   <li><b>currency</b> : 통화 코드 (예: KRW, USD)</li>
 * </ul>
 *
 * @author 복성민 (bokseongmin@gmail.com)
 * @version 1.0
 * @since 2025-06-19
 *
 * @see
 *
 * @changelog
 * <ul>
 *   <li>2025-06-19: 최초 생성 (복성민)</li>
 *   <li>2025-06-22: currency 필드 추가(복성민)</li>
 * </ul>
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {
    private String id;
    private String name;
    private int price;
    private String currency;
}
