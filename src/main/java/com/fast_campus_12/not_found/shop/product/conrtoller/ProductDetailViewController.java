package com.fast_campus_12.not_found.shop.product.conrtoller;
import com.fast_campus_12.not_found.shop.product.dto.ProductDetailDto;
import com.fast_campus_12.not_found.shop.product.service.ProductDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductDetailViewController {

    private final ProductDetailService productDetailService;

    @Autowired
    public ProductDetailViewController(ProductDetailService productDetailService) {
        this.productDetailService = productDetailService;
    }

    @GetMapping("/product/{id}")
    public String showProductDetail(@PathVariable Long id, Model model) {
        ProductDetailDto dto = productDetailService.getProductDetail(id);

        if (dto == null) {
            return "error/404"; // 또는 "redirect:/error"
        }

        model.addAttribute("product", dto); // View에서 ${product.xxx}로 사용
        return "page/product/detail"; // /WEB-INF/templates/page/product/detail.html
    }
}
