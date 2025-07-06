package com.eflix.acc.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

/* ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-07-05
  - 설명     : 손익계산서 표준 양식 템플릿 (완전판)
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-07-05 (김희정): 표준 손익계산서 구조 정의
  - 2025-07-06 (김희정): 99개 전체 코드 완성
=============================================== */
@Component
public class IncomeStatementTemplate {
    
    /**
     * 표준 손익계산서 구조 생성 (코드 01~99 완전판)
     * @return 표준 손익계산서 구조 리스트
     */
    public List<Map<String, Object>> getStandardTemplate() {
        List<Map<String, Object>> template = new ArrayList<>();
        
        // 영업수익
        template.add(createRow("영 업 수 익", "", "", "", "", "", "", "", true, 0, false, false));
        
        // Ⅰ. 매출액
        template.add(createRow("", "Ⅰ. 매출액", "", "01", "", "", "", "", true, 1, false, false));
        template.add(createRow("", "", "", "02", "(1) 제품매출", "", "", "제품매출", false, 3, false, false));
        template.add(createRow("", "", "", "03", "(2) 상품매출", "", "", "상품매출", false, 3, false, false));
        template.add(createRow("", "", "", "04", "(3) 공사수입", "", "", "공사수입", false, 3, false, false));
        template.add(createRow("", "", "", "05", "(4) 분양수입", "", "", "분양수입", false, 3, false, false));
        template.add(createRow("", "", "", "06", "(5) 임대수입", "", "", "임대수입", false, 3, false, false));
        template.add(createRow("", "", "", "07", "(6) 서비스수입", "", "", "서비스수입", false, 3, false, false));
        template.add(createRow("", "", "", "08", "(7) 기타", "", "", "매출 기타", false, 3, false, false));
        
        // 영업비용
        template.add(createRow("영 업 비 용", "", "", "", "", "", "", "", true, 0, false, false));
        
        // Ⅱ. 매출원가
        template.add(createRow("", "Ⅱ. 매출원가", "", "09", "", "", "", "", true, 1, false, false));
        template.add(createRow("", "", "", "10", "(1) 상품매출원가 ( ①+②-③-④ )", "", "", "상품매출원가", false, 3, false, false));
        template.add(createRow("", "", "", "11", "① 기초재고액", "", "", "상품매출원가 기초재고액", false, 4, false, false));
        template.add(createRow("", "", "", "12", "② 당기매입액", "", "", "상품매출원가 당기매입액", false, 4, false, false));
        template.add(createRow("", "", "", "13", "③ 기말재고액", "", "", "상품매출원가 기말재고액", false, 4, false, false));
        template.add(createRow("", "", "", "14", "④ 타계정대체액", "", "", "상품매출원가 타계정대체액", false, 4, false, false));
        template.add(createRow("", "", "", "15", "(2) 제조ㆍ공사ㆍ분양ㆍ기타원가", "", "", "제조ㆍ공사ㆍ분양ㆍ기타원가", false, 3, false, false));
        template.add(createRow("", "", "", "16", "① 기초재고액", "", "", "제조ㆍ공사ㆍ분양ㆍ기타원가 기초재고액", false, 4, false, false));
        template.add(createRow("", "", "", "17", "② 당기매입액", "", "", "제조ㆍ공사ㆍ분양ㆍ기타원가 당기매입액", false, 4, false, false));
        template.add(createRow("", "", "", "18", "③ 기말재고액", "", "", "제조ㆍ공사ㆍ분양ㆍ기타원가 기말재고액", false, 4, false, false));
        template.add(createRow("", "", "", "19", "④ 타계정대체액", "", "", "제조ㆍ공사ㆍ분양ㆍ기타원가 타계정대체액", false, 4, false, false));
        
        // Ⅲ. 매출총이익
        template.add(createRow("", "Ⅲ. 매출총이익 ( Ⅰ-Ⅱ )", "", "20", "", "", "", "", true, 1, false, true));
        
        // Ⅳ. 판매비와관리비
        template.add(createRow("", "Ⅳ. 판매비와관리비", "", "21", "", "", "", "", true, 1, false, false));
        template.add(createRow("", "", "", "22", "(1) 급여와 임금ㆍ제수당", "", "", "급여와 임금ㆍ제수당", false, 3, false, false));
        template.add(createRow("", "", "", "23", "(2) 일용급여", "", "", "일용급여", false, 3, false, false));
        template.add(createRow("", "", "", "24", "(3) 퇴직급여", "", "", "퇴직급여", false, 3, false, false));
        template.add(createRow("", "", "", "25", "(4) 복리후생비", "", "", "복리후생비", false, 3, false, false));
        template.add(createRow("", "", "", "26", "(5) 여비교통비", "", "", "여비교통비", false, 3, false, false));
        template.add(createRow("", "", "", "27", "(6) 임차료", "", "", "임차료", false, 3, false, false));
        template.add(createRow("", "", "", "28", "(7) 통신비", "", "", "통신비", false, 3, false, false));
        template.add(createRow("", "", "", "29", "(8) 전력비", "", "", "전력비", false, 3, false, false));
        template.add(createRow("", "", "", "30", "(9) 가스ㆍ수도비", "", "", "가스ㆍ수도비", false, 3, false, false));
        template.add(createRow("", "", "", "31", "(10) 유류비", "", "", "유류비", false, 3, false, false));
        template.add(createRow("", "", "", "32", "(11) 보험료", "", "", "보험료", false, 3, false, false));
        template.add(createRow("", "", "", "33", "(12) 리스료", "", "", "리스료", false, 3, false, false));
        template.add(createRow("", "", "", "34", "(13) 세금과공과", "", "", "세금과공과", false, 3, false, false));
        template.add(createRow("", "", "", "35", "(14) 감가상각비", "", "", "감가상각비", false, 3, false, false));
        template.add(createRow("", "", "", "36", "(15) 무형자산상각비", "", "", "무형자산상각비", false, 3, false, false));
        template.add(createRow("", "", "", "37", "(16) 수선비", "", "", "수선비", false, 3, false, false));
        template.add(createRow("", "", "", "38", "(17) 건물관리비", "", "", "건물관리비", false, 3, false, false));
        template.add(createRow("", "", "", "39", "(18) 접대비", "", "", "접대비", false, 3, false, false));
        template.add(createRow("", "", "", "40", "① 해외접대비", "", "", "접대비 해외접대비", false, 4, false, false));
        template.add(createRow("", "", "", "41", "② 국내접대비", "", "", "접대비 국내접대비", false, 4, false, false));
        template.add(createRow("", "", "", "42", "(19) 광고선전비", "", "", "광고선전비", false, 3, false, false));
        template.add(createRow("", "", "", "43", "(20) 도서인쇄비", "", "", "도서인쇄비", false, 3, false, false));
        template.add(createRow("", "", "", "44", "(21) 운반비", "", "", "운반비", false, 3, false, false));
        template.add(createRow("", "", "", "45", "(22) 차량유지비", "", "", "차량유지비", false, 3, false, false));
        template.add(createRow("", "", "", "46", "(23) 교육훈련비", "", "", "교육훈련비", false, 3, false, false));
        template.add(createRow("", "", "", "47", "(24) 지급수수료", "", "", "지급수수료", false, 3, false, false));
        template.add(createRow("", "", "", "48", "(25) 판매수수료", "", "", "판매수수료", false, 3, false, false));
        template.add(createRow("", "", "", "49", "(26) 대손상각비", "", "", "대손상각비", false, 3, false, false));
        template.add(createRow("", "", "", "50", "(27) 경상개발비", "", "", "경상개발비", false, 3, false, false));
        template.add(createRow("", "", "", "51", "(28) 소모품비", "", "", "소모품비", false, 3, false, false));
        template.add(createRow("", "", "", "52", "(29) 의약품비", "", "", "의약품비", false, 3, false, false));
        template.add(createRow("", "", "", "53", "(30) 의료소모품비", "", "", "의료소모품비", false, 3, false, false));
        template.add(createRow("", "", "", "54", "(31) 경영위탁수수료", "", "", "경영위탁수수료", false, 3, false, false));
        template.add(createRow("", "", "", "55", "(32) 외주용역비", "", "", "외주용역비", false, 3, false, false));
        template.add(createRow("", "", "", "56", "(33) 인적용역비", "", "", "인적용역비", false, 3, false, false));
        template.add(createRow("", "", "", "57", "(34) 기타소계", "", "", "판관비 기타소계", false, 3, false, false));
        template.add(createRow("", "", "", "58", "①", "", "", "판관비 기타1", false, 4, false, false));
        template.add(createRow("", "", "", "59", "②", "", "", "판관비 기타2", false, 4, false, false));
        template.add(createRow("", "", "", "60", "③", "", "", "판관비 기타3", false, 4, false, false));
        template.add(createRow("", "", "", "61", "④ 기타", "", "", "판관비 기타4", false, 4, false, false));
        
        // Ⅴ. 영업손익
        template.add(createRow("", "Ⅴ. 영업손익( Ⅲ－Ⅳ )", "", "62", "", "", "", "", true, 1, false, true));
        
        // Ⅵ. 영업외수익
        template.add(createRow("", "Ⅵ. 영업외수익", "", "63", "", "", "", "", true, 1, false, false));
        template.add(createRow("", "", "", "64", "(1) 이자수익", "", "", "이자수익", false, 3, false, false));
        template.add(createRow("", "", "", "65", "(2) 배당금수익", "", "", "배당금수익", false, 3, false, false));
        template.add(createRow("", "", "", "66", "(3) 외환차익", "", "", "외환차익", false, 3, false, false));
        template.add(createRow("", "", "", "67", "(4) 외화환산이익", "", "", "외화환산이익", false, 3, false, false));
        template.add(createRow("", "", "", "68", "(5) 단기투자자산 처분이익", "", "", "단기투자자산 처분이익", false, 3, false, false));
        template.add(createRow("", "", "", "69", "(6) 투자자산 처분이익", "", "", "투자자산 처분이익", false, 3, false, false));
        template.add(createRow("", "", "", "70", "(7) 유ㆍ무형자산 처분이익", "", "", "유ㆍ무형자산 처분이익", false, 3, false, false));
        template.add(createRow("", "", "", "71", "(8) 판매장려금", "", "", "판매장려금", false, 3, false, false));
        template.add(createRow("", "", "", "72", "(9) 국고보조금", "", "", "국고보조금", false, 3, false, false));
        template.add(createRow("", "", "", "73", "(10) 보험차익", "", "", "보험차익", false, 3, false, false));
        template.add(createRow("", "", "", "74", "(11) 충당금ㆍ준비금 환입액", "", "", "충당금ㆍ준비금 환입액", false, 3, false, false));
        template.add(createRow("", "", "", "75", "(12) 전기오류수정이익", "", "", "전기오류수정이익", false, 3, false, false));
        template.add(createRow("", "", "", "76", "(13) 기타 소계", "", "", "영업외수익 기타소계", false, 3, false, false));
        template.add(createRow("", "", "", "77", "①", "", "", "영업외수익 기타1", false, 4, false, false));
        template.add(createRow("", "", "", "78", "②", "", "", "영업외수익 기타2", false, 4, false, false));
        template.add(createRow("", "", "", "79", "③", "", "", "영업외수익 기타3", false, 4, false, false));
        template.add(createRow("", "", "", "80", "④ 기타", "", "", "영업외수익 기타4", false, 4, false, false));
        
        // Ⅶ. 영업외비용
        template.add(createRow("", "Ⅶ. 영업외비용", "", "81", "", "", "", "", true, 1, false, false));
        template.add(createRow("", "", "", "82", "(1) 이자비용", "", "", "이자비용", false, 3, false, false));
        template.add(createRow("", "", "", "83", "(2) 외환차손", "", "", "외환차손", false, 3, false, false));
        template.add(createRow("", "", "", "84", "(3) 외화환산손실", "", "", "외화환산손실", false, 3, false, false));
        template.add(createRow("", "", "", "85", "(4) 기타 대손상각비", "", "", "기타 대손상각비", false, 3, false, false));
        template.add(createRow("", "", "", "86", "(5) 기부금", "", "", "기부금", false, 3, false, false));
        template.add(createRow("", "", "", "87", "(6) 단기투자자산 처분손실", "", "", "단기투자자산 처분손실", false, 3, false, false));
        template.add(createRow("", "", "", "88", "(7) 투자자산 처분손실", "", "", "투자자산 처분손실", false, 3, false, false));
        template.add(createRow("", "", "", "89", "(8) 유ㆍ무형자산 처분손실", "", "", "유ㆍ무형자산 처분손실", false, 3, false, false));
        template.add(createRow("", "", "", "90", "(9) 재고자산 감모손실", "", "", "재고자산 감모손실", false, 3, false, false));
        template.add(createRow("", "", "", "91", "(10) 재해손실", "", "", "재해손실", false, 3, false, false));
        template.add(createRow("", "", "", "92", "(11) 충당금ㆍ준비금 전입액", "", "", "충당금ㆍ준비금 전입액", false, 3, false, false));
        template.add(createRow("", "", "", "93", "(12) 전기오류수정손실", "", "", "전기오류수정손실", false, 3, false, false));
        template.add(createRow("", "", "", "94", "(13) 기타 소계", "", "", "영업외비용 기타소계", false, 3, false, false));
        template.add(createRow("", "", "", "95", "①", "", "", "영업외비용 기타1", false, 4, false, false));
        template.add(createRow("", "", "", "96", "②", "", "", "영업외비용 기타2", false, 4, false, false));
        template.add(createRow("", "", "", "97", "③", "", "", "영업외비용 기타3", false, 4, false, false));
        template.add(createRow("", "", "", "98", "④ 기타", "", "", "영업외비용 기타4", false, 4, false, false));
        
        // Ⅷ. 당기순손익
        template.add(createRow("", "Ⅷ. 당기순손익( Ⅴ＋Ⅵ－Ⅶ )", "", "99", "", "", "", "당기순손익", true, 1, false, true));
        
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