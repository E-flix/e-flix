package com.eflix.acc.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eflix.acc.dto.EntryDetailDTO;
import com.eflix.acc.dto.EntryMasterDTO;
import com.eflix.acc.service.EntryService;
import lombok.RequiredArgsConstructor;
/**
 * ============================================
 * - 작성자 : 김희정
 * - 최초작성 : 2025-06-20
 * - 설명 : 회계 전표관리 컨트롤러
 * -----------------------------------------------
 * [ 변경 이력 ]
 * - 2025-06-20 (김희정): AccController에서 분리 => 전표관리Controller 생성
 * - 2025-06-22 (김희정): 전표 목록 조회 작성
 * - 2025-06-24 (김희정): insert, update, delete 작성
 * ============================================
 */
@Controller
@RequestMapping("/acc")
@RequiredArgsConstructor
public class AccEntryController {

  private final EntryService entryService;

  /**
   * 전표 목록 조회
   * 
   * @param model : 뷰에 전달할 데이터 모델
   * @return : 일반전표
   */
  @GetMapping("/en")
  public String entry(Model model) {
    model.addAttribute("entryList", entryService.getList());
     // select max+1 entry_number
    model.addAttribute("enNo", entryService.selectMaxPlusOneEntryNumber());
    return "acc/entry";
  }

  // 신규 전표번호 조회
  @ResponseBody
  @GetMapping("/en/enNo")
  public int enNo() {
     // select max+1 entry_number
    return entryService.selectMaxPlusOneEntryNumber();
  }

  // 신규 라인번호 조회
  @ResponseBody
  @GetMapping("/en/{entryNumber}")
  public int lineNo(@PathVariable int entryNumber) {
     // select max+1 line_number
    return entryService.selectMaxPlusOneLineNumber(entryNumber);
  }

  // insert
  @PostMapping("/en")
  public ResponseEntity<String> createEntry(@RequestBody EntryMasterDTO entryMaster) {
    try {
      entryService.insertEntry(entryMaster); // insert 실행
      return ResponseEntity.ok("Insert success"); // 성공 응답
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Insert failed: " + e.getMessage()); // 실패 응답
    }
  }
  
  // update
  @PutMapping("/en")
  public ResponseEntity<String> updateEntry(@PathVariable int entryNumber, @RequestBody EntryMasterDTO entryMaster) {
    try {
      entryMaster.setEntryNumber(entryNumber); // entryNumber지정 
      entryService.updateEntry(entryMaster); // update 실행
      return ResponseEntity.ok("Update success"); // 성공 응답
    } catch (Exception e) {
      return ResponseEntity.status(500).body("Update failed: " + e.getMessage()); // 실패 응답
    }
  }

  // delete
  @PutMapping("/en/del")
  public ResponseEntity<String> deleteEntry(@RequestBody List<EntryDetailDTO> entryDetail) {
    try {
        entryService.deleteEntry(entryDetail); // delete 실행
        return ResponseEntity.ok("Delete success"); // 성공 응답
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Delete failed: " + e.getMessage()); // 실패 응답
    }
  }

  /**
   * 매입매출전표 화면 요청 처리
   */
  @GetMapping("/enps")
  public String entryPurchaseSales() {
    return "acc/entryPurchaseSales";
  }
}
