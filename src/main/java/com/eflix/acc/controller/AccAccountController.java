package com.eflix.acc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.eflix.acc.service.AccountService;
import lombok.RequiredArgsConstructor;
/** ============================================
  - 작성자   : 김희정
  - 최초작성 : 2025-06-19
  - 설명     : 회계 계정과목관리 컨트롤러
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김희정): AccController에서 분리 => 계정과목Controller 생성
============================================  */
@Controller
@RequestMapping("/acc")
@RequiredArgsConstructor
public class AccAccountController {

    private final AccountService accountService;

    /**
     * 계정과목관리 화면 요청 처리
     */
    @GetMapping("/act")
    public String account(Model model) {
        model.addAttribute("accountList", accountService.getList());
        return "acc/account";
    }

    
// 	/**
// 	 * 게시글 목록 조회
// 	 * 
// 	 * @param model
// 	 * @param board : 검색조건
// 	 * @return : 목록페이지명
// 	 */
// //	@GetMapping("/list")
// //	public String list(Model model, BoardVO board) {
// //		model.addAttribute("list", boardService.getList());
// //		return "board/list";
// //	}
// 	@GetMapping("/list")
// 	public void list(Criteria cri, Model model) {

// 		model.addAttribute("list", boardService.getList(cri));

// 		// paing 처리
// 		long total = boardService.getTotal(cri);
// 		model.addAttribute("pageMaker", new PageDTO(cri, total));
// 	}

// 	// 등록페이지로 이동
// 	@GetMapping("/register")
// 	public void register() {

// 	}

// 	// 등록 처리하고 목록으로 이동
// 	@PostMapping("/register")
// 	public String register(BoardVO vo, RedirectAttributes rttr) {
// 		boardService.insert(vo);
// 		rttr.addFlashAttribute("result", vo.getBno());
// 		return "redirect:list";
// 	}

// 	// 상세보기 페이지로 이동(단건조회)
// 	@GetMapping("get")
// 	public String get(@RequestParam("bno") int bno, Model model) {
// 		model.addAttribute("board", boardService.findById(bno));
// 		return "board/get";
// 	}

// 	// 수정페이지로 이동
// 	@GetMapping("modify")
// 	public String modify(@RequestParam("bno") int bno, Model model) {
// 		model.addAttribute("board", boardService.findById(bno));
// 		return "board/modify";
// 	}

// 	// 수정처리
// 	@PostMapping("modify")
// 	public String modify(BoardVO board, RedirectAttributes rttr) {
// 		boardService.update(board);
// 		rttr.addAttribute("bno", board.getBno());
// 		// return "redirect:get?bno=" + board.getBno() ;
// 		return "redirect:get";
// 	}

// 	// 삭제처리
// 	@PostMapping("remove")
// 	public String remove(@RequestParam("bno") int bno) {
// 		boardService.delete(bno);
// 		return "redirect:list"; // 상대경로
// 	}
}
