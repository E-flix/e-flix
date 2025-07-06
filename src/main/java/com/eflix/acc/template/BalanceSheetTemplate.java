package com.eflix.acc.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-07-05
  - 설명     : 재무상태표 표준 양식 템플릿
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-07-05 (김희정): 표준 재무상태표 구조 정의
=============================================== */
@Component
public class BalanceSheetTemplate {
    
    /**
     * 표준 재무상태표 구조 생성
     * @return 표준 재무상태표 구조 리스트
     */
    public List<Map<String, Object>> getStandardTemplate() {
        List<Map<String, Object>> template = new ArrayList<>();
        
        // 자산
        template.add(createRow("자 산", "", "", "", "", "", "", "", true, 0, false, false));
        
        // Ⅰ. 유동자산
        template.add(createRow("", "Ⅰ. 유동자산", "", "01", "", "", "", "", true, 1, false, false));
        
        // 1. 당좌자산
        template.add(createRow("", "", "1. 당좌자산", "02", "", "", "", "", true, 2, false, false));
        template.add(createRow("", "", "", "03", "(1) 현금 및 현금성자산", "", "", "현금 및 현금성자산", false, 3, false, false));
        template.add(createRow("", "", "", "04", "(2) 단기금융상품", "", "", "단기금융상품", false, 3, false, false));
        template.add(createRow("", "", "", "05", "(3) 단기투자증권", "", "", "단기투자증권", false, 3, false, false));
        template.add(createRow("", "", "", "06", "(4) 단기대여금", "", "", "단기대여금", false, 3, false, false));
        template.add(createRow("", "", "", "07", "① 관계회사", "", "", "단기대여금 관계회사", false, 3, false, false));
        template.add(createRow("", "", "", "08", "② 임원 및 종업원", "", "", "단기대여금 임원", false, 3, false, false));
        template.add(createRow("", "", "", "09", "③ 기타", "", "", "단기대여금 기타", false, 3, false, false));
        template.add(createRow("", "", "", "10", "(5) 매출채권", "", "", "매출채권", false, 3, false, false));
        template.add(createRow("", "", "", "11", "(6) 선급금", "", "", "선급금", false, 3, false, false));
        template.add(createRow("", "", "", "12", "(7) 미수금", "", "", "미수금", false, 3, false, false));
        template.add(createRow("", "", "", "13", "① 공사미수금", "", "", "미수금 공사", false, 3, false, false));
        template.add(createRow("", "", "", "14", "② 분양미수금", "", "", "미수금 분양", false, 3, false, false));
        template.add(createRow("", "", "", "15", "③ 기타", "", "", "미수금 기타", false, 3, false, false));
        template.add(createRow("", "", "", "16", "(8) 선급비용", "", "", "선급비용", false, 3, false, false));
        template.add(createRow("", "", "", "17", "(9) 기타", "", "", "당좌자산 기타", false, 3, false, false));
        
        // 2. 재고자산
        template.add(createRow("", "", "2. 재고자산", "18", "", "", "", "", true, 2, false, false));
        template.add(createRow("", "", "", "19", "(1) 상품", "", "", "상품", false, 3, false, false));
        template.add(createRow("", "", "", "20", "(2) 제품", "", "", "제품", false, 3, false, false));
        template.add(createRow("", "", "", "21", "(3) 반제품 및 재공품", "", "", "반제품 및 재공품", false, 3, false, false));
        template.add(createRow("", "", "", "22", "(4) 원재료", "", "", "원재료", false, 3, false, false));
        template.add(createRow("", "", "", "23", "(5) 부재료", "", "", "부재료", false, 3, false, false));
        template.add(createRow("", "", "", "24", "(6) 미착상품 (미착재료)", "", "", "미착상품 (미착재료)", false, 3, false, false));
        template.add(createRow("", "", "", "25", "(7) 건설용지", "", "", "건설용지", false, 3, false, false));
        template.add(createRow("", "", "", "26", "(8) 완성건물", "", "", "완성건물", false, 3, false, false));
        template.add(createRow("", "", "", "27", "(9) 미성공사", "", "", "미성공사", false, 3, false, false));
        template.add(createRow("", "", "", "28", "(10) 기타", "", "", "재고자산 기타", false, 3, false, false));
        
        // Ⅱ. 비유동자산
        template.add(createRow("", "Ⅱ. 비유동자산", "", "29", "", "", "", "", true, 1, false, false));
        
        // 1. 투자자산
        template.add(createRow("", "", "1. 투자자산", "30", "", "", "", "", true, 2, false, false));
        template.add(createRow("", "", "", "31", "(1) 장기금융상품", "", "", "장기금융상품", false, 3, false, false));
        template.add(createRow("", "", "", "32", "(2) 장기투자증권", "", "", "장기투자증권", false, 3, false, false));
        template.add(createRow("", "", "", "33", "(3) 장기대여금", "", "", "장기대여금", false, 3, false, false));
        template.add(createRow("", "", "", "34", "① 관계회사", "", "", "장기대여금 관계회사", false, 3, false, false));
        template.add(createRow("", "", "", "35", "② 임원 및 종업원", "", "", "장기대여금 임원", false, 3, false, false));
        template.add(createRow("", "", "", "36", "③ 기타", "", "", "장기대여금 기타", false, 3, false, false));
        template.add(createRow("", "", "", "37", "(4) 기타", "", "", "투자자산 기타", false, 3, false, false));
        
        // 2. 유형자산
        template.add(createRow("", "", "2. 유형자산", "38", "", "", "", "", true, 2, false, false));
        template.add(createRow("", "", "", "39", "(1) 토지", "", "", "토지", false, 3, false, false));
        template.add(createRow("", "", "", "40", "(2) 건물", "", "", "건물", false, 3, false, false));
        template.add(createRow("", "", "", "41", "(3) 구축물 (시설장치 포함)", "", "", "구축물 (시설장치 포함)", false, 3, false, false));
        template.add(createRow("", "", "", "42", "(4) 기계장치", "", "", "기계장치", false, 3, false, false));
        template.add(createRow("", "", "", "43", "(5) 선박", "", "", "선박", false, 3, false, false));
        template.add(createRow("", "", "", "44", "(6) 건설용 장비", "", "", "건설용 장비", false, 3, false, false));
        template.add(createRow("", "", "", "45", "(7) 차량운반구", "", "", "차량운반구", false, 3, false, false));
        template.add(createRow("", "", "", "46", "(8) 공구 및 기구", "", "", "공구 및 기구", false, 3, false, false));
        template.add(createRow("", "", "", "47", "(9) 비품", "", "", "비품", false, 3, false, false));
        template.add(createRow("", "", "", "48", "(10) 건설 중인 자산", "", "", "건설 중인 자산", false, 3, false, false));
        template.add(createRow("", "", "", "49", "(11) 기타", "", "", "유형자산 기타", false, 3, false, false));
        
        // 3. 무형자산
        template.add(createRow("", "", "3. 무형자산", "50", "", "", "", "", true, 2, false, false));
        template.add(createRow("", "", "", "51", "(1) 영업권", "", "", "영업권", false, 3, false, false));
        template.add(createRow("", "", "", "52", "(2) 산업재산권 (특허권, 상표권 등)", "", "", "산업재산권 (특허권, 상표권 등)", false, 3, false, false));
        template.add(createRow("", "", "", "53", "(3) 개발비", "", "", "개발비", false, 3, false, false));
        template.add(createRow("", "", "", "54", "(4) 기타", "", "", "무형자산 기타", false, 3, false, false));

        // 4. 기타 비유동자산
        template.add(createRow("", "", "4. 기타 비유동자산", "55", "", "", "", "", true, 2, false, false));
        template.add(createRow("", "", "", "56", "(1) 장기매출채권", "", "", "장기매출채권", false, 3, false, false));
        template.add(createRow("", "", "", "57", "(2) 장기선급금", "", "", "장기선급금", false, 3, false, false));
        template.add(createRow("", "", "", "58", "(3) 장기미수금", "", "", "장기미수금", false, 3, false, false));
        template.add(createRow("", "", "", "59", "(4) 임차보증금", "", "", "임차보증금", false, 3, false, false));
        template.add(createRow("", "", "", "60", "(5) 기타보증금", "", "", "기타보증금", false, 3, false, false));
        template.add(createRow("", "", "", "61", "(6) 기타", "", "", "기타비유동자산 기타", false, 3, false, false));
        
        // 자산총계
        template.add(createRow("", "", "", "62", "자산총계( Ⅰ+Ⅱ )", "", "", "", false, 0, false, true));
        
        // 부채
        template.add(createRow("부 채", "", "", "", "", "", "", "", true, 0, false, false));
        
        // Ⅰ. 유동부채
        template.add(createRow("", "Ⅰ. 유동부채", "", "63", "", "", "", "", true, 1, false, false));
        template.add(createRow("", "", "1. 단기차입금", "64", "1. 단기차입금", "", "", "단기차입금", false, 3, false, false));
        template.add(createRow("", "", "2. 매입채무", "65", "2. 매입채무", "", "", "매입채무", false, 3, false, false));
        template.add(createRow("", "", "3. 선수금", "66", "3. 선수금", "", "", "선수금", false, 3, false, false));
        template.add(createRow("", "", "4. 미지급금", "67", "4. 미지급금", "", "", "미지급금", false, 3, false, false));
        template.add(createRow("", "", "5. 예수금", "68", "5. 예수금", "", "", "예수금", false, 3, false, false));
        template.add(createRow("", "", "6. 미지급비용", "69", "6. 미지급비용", "", "", "미지급비용", false, 3, false, false));
        template.add(createRow("", "", "7. 유동성장기부채", "70", "7. 유동성장기부채", "", "", "유동성장기부채", false, 3, false, false));
        template.add(createRow("", "", "8. 유동성충당부채", "71", "8. 유동성충당부채", "", "", "유동성충당부채", false, 3, false, false));
        template.add(createRow("", "", "9. 기타", "72", "9. 기타", "", "", "유동부채 기타", false, 3, false, false));

        // Ⅱ. 비유동부채
        template.add(createRow("", "Ⅱ. 비유동부채", "", "73", "", "", "", "", true, 1, false, false));
        template.add(createRow("", "", "1. 장기차입금", "74", "1. 장기차입금", "", "", "장기차입금", false, 3, false, false));
        template.add(createRow("", "", "", "75", "① 관계회사", "", "", "장기차입금 관계회사", false, 3, false, false));
        template.add(createRow("", "", "", "76", "② 임원 및 종업원", "", "", "장기차입금 임원", false, 3, false, false));
        template.add(createRow("", "", "", "77", "③ 기타", "", "", "장기차입금 기타", false, 3, false, false));
        template.add(createRow("", "", "2. 장기매입채무", "78", "2. 장기매입채무", "", "", "장기매입채무", false, 3, false, false));
        template.add(createRow("", "", "3. 장기선수금", "79", "3. 장기선수금", "", "", "장기선수금", false, 3, false, false));
        template.add(createRow("", "", "4. 장기미지급금", "80", "4. 장기미지급금", "", "", "장기미지급금", false, 3, false, false));
        template.add(createRow("", "", "5. 임대보증금", "81", "5. 임대보증금", "", "", "임대보증금", false, 3, false, false));
        template.add(createRow("", "", "6. 기타보증금", "82", "6. 기타보증금", "", "", "기타보증금", false, 3, false, false));
        template.add(createRow("", "", "7. 퇴직급여충당부채", "83", "7. 퇴직급여충당부채", "", "", "퇴직급여충당부채", false, 3, false, false));
        template.add(createRow("", "", "8. 기타충당부채", "84", "8. 기타충당부채", "", "", "기타충당부채", false, 3, false, false));
        template.add(createRow("", "", "9. 제준비금", "85", "9. 제준비금", "", "", "제준비금", false, 3, false, false));
        template.add(createRow("", "", "10. 기타", "86", "10. 기타", "", "", "비유동부채 기타", false, 3, false, false));
        
        // 부채총계
        template.add(createRow("", "", "", "87", "부채총계( Ⅰ+Ⅱ )", "", "", "", false, 0, false, true));
        
        // 자본
        template.add(createRow("자 본", "", "", "", "", "", "", "", true, 0, false, false));
        
        // Ⅲ. 자본금
        template.add(createRow("", "Ⅲ. 자본금", "", "88", "", "", "", "자본금", true, 2, false, false));

        // Ⅳ. 당기순손익
        template.add(createRow("", "Ⅳ. 당기순손익", "", "89", "", "", "", "당기순손익", true, 2, false, false));

        // 자본총계
        template.add(createRow("", "", "", "90", "자본총계( Ⅲ+Ⅳ )", "", "", "", false, 0, false, true));
        
        // 부채와 자본총계
        template.add(createRow("", "", "", "91", "부채와 자본총계( Ⅰ+Ⅱ+Ⅲ+Ⅳ )", "", "", "", false, 0, false, true));
        
        return template;
    }
    
    /**
     * 그리드 행 생성 헬퍼 메서드 (매칭용 컬럼 추가)
     */
    private Map<String, Object> createRow(String section, String subsection, String subcategory,
            String accountCode, String accountName, String amount, String total, 
            String matchKey, boolean isHeader, int level, boolean isSubTotal, boolean isGrandTotal) {
        
        Map<String, Object> row = new HashMap<>();
        row.put("section", section);
        row.put("subsection", subsection);
        row.put("subcategory", subcategory);
        row.put("accountCode", accountCode);
        row.put("accountName", accountName);
        row.put("amount", amount);
        row.put("total", total);
        row.put("matchKey", matchKey); // 매칭용 키 추가
        row.put("isHeader", isHeader);
        row.put("level", level);
        row.put("isSubTotal", isSubTotal);
        row.put("isGrandTotal", isGrandTotal);
        
        return row;
    }
}