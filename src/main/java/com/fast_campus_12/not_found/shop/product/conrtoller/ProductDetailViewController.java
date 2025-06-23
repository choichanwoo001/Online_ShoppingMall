package com.fast_campus_12.not_found.shop.product.conrtoller;
import com.fast_campus_12.not_found.shop.product.dto.ProductDetailDto;
import com.fast_campus_12.not_found.shop.product.service.ProductService;
import com.fast_campus_12.not_found.shop.product.service.ColorsService;
import com.fast_campus_12.not_found.shop.product.service.ProductVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProductDetailViewController {
    private final ProductService productService;
    private final ColorsService colorsService;
    private final ProductVariantService productVariantService;
    @Autowired
    public ProductDetailViewController(ProductService productService,
                                       ColorsService colorsService,
                                       ProductVariantService productVariantService) {
        this.productService = productService;
        this.colorsService = colorsService;
        this.productVariantService = productVariantService;
    }
    @GetMapping("/product/{id}")
    public String showProductDetail(@PathVariable Long id, Model model) {
        ProductDetailDto dto = productService.getProductDetail(id);
        if (dto == null) {
            return "error/404"; // 또는 "redirect:/error"
        }
        model.addAttribute("product", dto);
        model.addAttribute("colors", colorsService.getAll());
        model.addAttribute("variants", productVariantService.getAll());
        // 핵심: base.html에서 이 값을 바탕으로 페이지 fragment를 결정
        model.addAttribute("contentPath", "product/detail");
        // 항상 base.html이 렌더링되고, 그 안에서 fragment를 대입
        return "layout/base";
    }
}
