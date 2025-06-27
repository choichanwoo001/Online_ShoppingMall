package com.fast_campus_12.not_found.shop.global.controller;

import com.fast_campus_12.not_found.shop.global.dto.MainBannerDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PageController {
    @GetMapping("/")
    public String renderPage( Model model) {
        model.addAttribute("contentPath", "home");
        model.addAttribute("contentPath", "main");
        List<MainBannerDto> bigBanners = List.of(
                new MainBannerDto("index.jpg", "/event/1"), // image: 사진링크 path: href링크
                new MainBannerDto("index3.jpg", "/event/2"));
        model.addAttribute("bigBanners", bigBanners);
        return "layout/base";
    }


    /**
     * 지정한 HTML 페이지를 base 레이아웃의 content 영역에 렌더링합니다.
     * 예: localhost:8080/page/sample 접속 시,
     *     templates/page/sample.html 파일의 content fragment가
     *     base.html의 content 영역에 삽입되어 렌더링됩니다.
     *
     * @param pageName 렌더링할 페이지 이름 (HTML 파일 이름)
     * @param model    뷰에 전달할 모델 객체
     * @return         base 레이아웃 템플릿 이름
     */
    @GetMapping("/page/{pageName}")
    public String renderPage(@PathVariable("pageName") String pageName, Model model) {
        model.addAttribute("title", pageName + "aaaa");
//        model.addAttribute("pageName", pageName);
        model.addAttribute("contentPath", pageName);
        return "layout/base";
    }

    //url : localhost:8080/product/aa 로 테스트 각자 매핑 필요
//    @GetMapping("/product/{subCategory}")
//    public String renderCategoryPage(@PathVariable("subCategory") String subCategory, Model model) {
//        model.addAttribute("subCategory", subCategory);
//        model.addAttribute("contentPath", "product/productList");
//        return "layout/base";
//    }

}
