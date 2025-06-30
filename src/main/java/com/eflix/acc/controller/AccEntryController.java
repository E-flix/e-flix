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
import com.eflix.common.code.service.CommonService;
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
  private final CommonService commonService;

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
  public ResponseEntity<EntryMasterDTO> createEntry(@RequestBody EntryMasterDTO entryMaster) {
    try {
      EntryMasterDTO savedEntry = entryService.insertEntry(entryMaster);// insert 실행
      return ResponseEntity.ok(savedEntry); // 성공 응답
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body(null); // 실패 응답
    }
  }
  
  // update
  @PutMapping("/en")
  public ResponseEntity<EntryMasterDTO> updateEntry(@RequestBody EntryMasterDTO entryMaster) {
    try { 
      entryService.updateEntry(entryMaster); // update 실행
      return ResponseEntity.ok(entryMaster); // 성공 응답
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body(null); // 실패 응답
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
  public String entryPurchaseSales(Model model) {
    model.addAttribute("code0A", commonService.getCommon("0A")); // 여부
    model.addAttribute("code0H", commonService.getCommon("0H")); // 과세유형
    model.addAttribute("code0G", commonService.getCommon("0G")); // 차변대변
    return "acc/entryPurchaseSales";
  }


  /**
   * 매입매출전표 master 목록 조회
   * 
   * @return : 매입매출전표 master 목록
   */
  @ResponseBody
  @GetMapping("/enps/ma") 
  public List<EntryMasterDTO> getPSMasterList() {
    return entryService.getPSMasterList(); // 매입매출전표 master 목록 조회
  }

  /**
   * 매입매출전표 detail 목록 조회
   * 
   * @param entryNumber : 전표번호
   * @return : 매입매출전표 detail 목록
   */
  @ResponseBody
  @GetMapping("/enps/de/{entryNumber}")
  public List<EntryDetailDTO> getPSDetailList(@PathVariable int entryNumber) {
    return entryService.getPSDetailList(entryNumber); // 매입매출전표 detail 목록 조회
  }

  /**
   * 매입매출전표 저장 (신규)
   * 
   * @param entryMaster : 매입매출전표 마스터 + 상세 데이터
   * @return : 저장된 매입매출전표 데이터
   */
  @PostMapping("/enps")
  public ResponseEntity<EntryMasterDTO> createPurchaseSalesEntry(@RequestBody EntryMasterDTO entryMaster) {
    try {
      EntryMasterDTO savedEntry = entryService.insertPurchaseSalesEntry(entryMaster);
      return ResponseEntity.ok(savedEntry);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body(null);
    }
  }

  /**
   * 매입매출전표 수정
   * 
   * @param entryMaster : 매입매출전표 마스터 + 상세 데이터
   * @return : 수정된 매입매출전표 데이터
   */
  @PutMapping("/enps")
  public ResponseEntity<EntryMasterDTO> updatePurchaseSalesEntry(@RequestBody EntryMasterDTO entryMaster) {
    try {
      entryService.updatePurchaseSalesEntry(entryMaster);
      return ResponseEntity.ok(entryMaster);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body(null);
    }
  }

  /**
   * 매입매출전표용 신규 전표번호 조회
   * 
   * @return : 신규 전표번호 (max+1)
   */
  @ResponseBody
  @GetMapping("/enps/enNo")
  public int getPSEntryNumber() {
    return entryService.selectMaxPlusOneEntryNumberPS();
  }

  /**
   * 매입매출전표 삭제
   * 
   * @param entryMasters : 삭제할 매입매출전표 마스터 리스트
   * @return : 삭제 결과
   */
  @PutMapping("/enps/del")
  public ResponseEntity<String> deletePurchaseSalesEntry(@RequestBody List<EntryMasterDTO> entryMasters) {
    try {
      entryService.deletePurchaseSalesEntry(entryMasters);
      return ResponseEntity.ok("Delete success");
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body("Delete failed: " + e.getMessage());
    }
  }

  /**
   * 매입매출전표 상세 저장 (신규)
   * 
   * @param entryDetails : 저장할 상세 데이터 리스트
   * @return : 저장 결과
   */
  @PostMapping("/enps/detail")
  public ResponseEntity<?> insertPurchaseSalesDetail(@RequestBody List<EntryDetailDTO> entryDetails) {
    try {
      List<EntryDetailDTO> savedDetails = entryService.insertPurchaseSalesDetail(entryDetails);
      return ResponseEntity.ok(savedDetails);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body("Detail insert failed: " + e.getMessage());
    }
  }

  /**
   * 매입매출전표 상세 수정
   * 
   * @param entryDetails : 수정할 상세 데이터 리스트
   * @return : 수정 결과
   */
  @PutMapping("/enps/detail")
  public ResponseEntity<?> updatePurchaseSalesDetail(@RequestBody List<EntryDetailDTO> entryDetails) {
    try {
      List<EntryDetailDTO> updatedDetails = entryService.updatePurchaseSalesDetail(entryDetails);
      return ResponseEntity.ok(updatedDetails);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body("Detail update failed: " + e.getMessage());
    }
  }

  /**
   * 매입매출전표 상세 삭제
   * 
   * @param entryDetails : 삭제할 상세 데이터 리스트
   * @return : 삭제 결과
   */
  @PutMapping("/enps/detail/del")
  public ResponseEntity<String> deletePurchaseSalesDetail(@RequestBody List<EntryDetailDTO> entryDetails) {
    try {
      entryService.deletePurchaseSalesDetail(entryDetails);
      return ResponseEntity.ok("Detail delete success");
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(500).body("Detail delete failed: " + e.getMessage());
    }
  }

  /**
   * 매입매출전표 상세 라인번호 조회 (max+1)
   * 
   * @param entryNumber : 전표번호
   * @return : 다음 라인번호
   */
  @GetMapping("/enps/detail/lineNo/{entryNumber}")
  @ResponseBody
  public int getPSDetailLineNumber(@PathVariable int entryNumber) {
    return entryService.selectMaxPlusOneLineNumber(entryNumber);
  }

  /**
   * 매입매출전표 상세 데이터 조회
   * 
   * @param entryNumber : 전표번호
   * @return : 매입매출전표 상세 데이터
   */
  @ResponseBody
  @GetMapping("/enps/detail/{entryNumber}")
  public List<EntryDetailDTO> getPSDetailData(@PathVariable int entryNumber) {
    return entryService.getPSDetailList(entryNumber);
  }
}
