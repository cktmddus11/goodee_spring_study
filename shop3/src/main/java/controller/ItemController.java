package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import logic.Item;
import logic.ShopService;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller // @Component + Controller기능 => action 클래스 같은거? 
@RequestMapping("item")  // item/xxx.shop 
public class ItemController {
	@Autowired
	private ShopService service;
	
	@RequestMapping("list") // item/list.shop요청
	public ModelAndView list() {
		// itemList : item 테이블의 모든 레코드와 모든 컬럼정보를 저장
		List<Item> itemList = service.getItemList(); 
		ModelAndView mav = new ModelAndView(); // 뷰 : item/list 
		mav.addObject("itemList", itemList);
		return mav; // /WEB-INF/view/item/list.jsp
	}
	
	@RequestMapping("create")
	public ModelAndView create() {
		ModelAndView mav = new ModelAndView("item/add");
		mav.addObject(new Item()); // add.jsp에 modelAttribute= "item" 때문에 해주는거
		return mav;
	}
	@RequestMapping("register") // @Valid 유효성 검사하라고 붙이는거
	// 저거 붙이면 알아서 유효성 하는거(bindResult)에 넣는과정 이 생략됨 ??
	public ModelAndView register(@Valid Item item, BindingResult bresult, HttpServletRequest request ) { 
		//                                        입력한 내용    유효성 검사때문에   파일 업로드 정보도 request객체에 있음 
		ModelAndView mav = new ModelAndView("item/add");
		if(bresult.hasErrors()) {
			mav.getModel().putAll(bresult.getModel());
			return mav;
		}
		service.itemCreate(item, request);
		mav.setViewName("redirect:/item/list.shop");
		return mav;
	}
	
	
	
	
}
