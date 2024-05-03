package controller;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dao.FreeDao;
import oracle.net.aso.n;
import service.FreeService;
import util.ScanUtil;
import util.View;
import view.Print;
 
public class MainController extends Print {
	
    // 여기에 로그인 중인 사용자 정보를 담는 용도
	// String에는 테이블 행의 LEVEL을, Object에는 SQL문의 속성과 값을 넣음
	// String에 들어가는 키값은 변경될 수 있음
	static public Map<String, Object> sessionStorage = new HashMap<>();
	
	// 개발 완료됐을 때, 사용하지 않는 개발용 메시지들을 한꺼번에 비활성화하기 위한 용도
	boolean debug = true; 
	FreeService freeService = FreeService.getInstance();

	public static void main(String[] args) {
		new MainController().start();
	}

	private void start() {
		View view = View.HOME;
		while (true) {
			switch (view) {
			case HOME:
				view = home();
				break;
			case FREEBOAD_LIST:
				view = list();
				break;
			case FREEBOAD_DETAIL:
				view = detail();
				break;
			case FREEBOAD_UPDATE:
				view = update();
				break;
			case FREEBOAD_DELETE:
				view = delete();
				break;
			case FREEBOAD_INSERT: 
				view = insert();
				break;
			default:
				break;
			}
		}
	}
	
	
	private View insert() {
		if (debug) System.out.println("========게시판 등록========");
		
		// 입력받을 내용
		// NAME, CONTENT, WRITER
		String name = ScanUtil.nextLine("제목");
		String content = ScanUtil.nextLine("내용");
		String writer = ScanUtil.nextLine("작성자");
		
		List<Object> param = new ArrayList<Object>();
		// 이렇게도 가능
//		param.add(ScanUtil.nextLine("제목"));
		param.add(name);
		param.add(content);
		param.add(writer);
		
		freeService.insert(param);
		
		return View.FREEBOAD_LIST;
	}
	
	
	private View delete() {
		if (debug) System.out.println("========게시판 삭제========");
		
		int boardNo = (int) sessionStorage.get("boardNo");
		
		List<Object> param = new ArrayList<Object>();
		param.add(boardNo);
		freeService.delete(param);
		
		return View.FREEBOAD_LIST;
	}
	
	
	private View update() {
		if (debug) System.out.println("========게시판 수정========");
		
		return View.FREEBOAD_DETAIL;
	}
	
	
	private View detail() {
		if (debug) System.out.println("========게시판 상세========");
		
		int boardNo = (int) sessionStorage.get("boardNo");
		
		
		List<Object> param = new ArrayList<Object>();
		param.add(boardNo);
		
		Map<String, Object> detail = freeService.detail(param);
		System.out.println(detail);
		
		
		System.out.println("1. 게시판 수정");
		System.out.println("2. 게시판 삭제");
		System.out.println("3. 게시판 전체 출력");
		
		int sel = ScanUtil.menu();
		
		if (sel==1) return View.FREEBOAD_UPDATE;
		else if (sel==2) return View.FREEBOAD_DELETE;
		else if (sel==3) return View.FREEBOAD_LIST;
		return View.HOME;
	}
	
	
	private View list() {
		// 개발자의 개발을 위한 출력 메시지
		// 개발이 완료된 후, 개발용 메시지들을 한꺼번에 비활성화하기 위한 debug 변수 사용
		if (debug) System.out.println("======게시판 전체 리스트======");
		
		List<Map<String, Object>> list = freeService.list();
		for (Map<String, Object> map : list) {
			System.out.println(map);
		}
		
		System.out.println("1. 게시판 상세 조회");
		System.out.println("2. 홈");
		
		int sel = ScanUtil.menu();
		
		if (sel==1) {
			int boardNo = ScanUtil.nextInt("게시판 번호 입력 : ");
			sessionStorage.put("boardNo", boardNo);
			
			return View.FREEBOAD_DETAIL;
		}
		else if (sel==2) return View.HOME;
		else return View.HOME;
	}
	
	
	private View home() {
		if (debug) System.out.println("=========게시판 홈========");
		
		System.out.println("1. 게시판 전체 출력");
		System.out.println("2. 게시판 추가");
		
		int sel = ScanUtil.menu();
		
		if (sel==1) return View.FREEBOAD_LIST;
		else if (sel==2) return View.FREEBOAD_INSERT;
		else return View.HOME;
	}

}