package com.fast_campus_12.not_found.shop.product.conrtoller;


import com.fast_campus_12.not_found.shop.product.service.*;
import com.fast_campus_12.not_found.shop.product.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public String getAll() {
        return "get all products";
    }
}

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
class ProductViewController {
    private final ProductService productService;

    @GetMapping("/{id}")
    public String showProduct(@PathVariable Long id, Model model) {
        ProductDetailDto dto = productService.getProductDetail(id);
        if (dto == null) return "error/404";
        model.addAttribute("product", dto);
        return "page/product/detail";
    }
}

@RestController
@RequestMapping("/api/product-description-images")
@RequiredArgsConstructor
public class ProductDescriptionImageController {
    private final ProductDescriptionImageService productDescriptionImageService;

    @GetMapping
    public String getAll() {
        return "get all product description images";
    }
}

@Controller
@RequiredArgsConstructor
@RequestMapping("/product-description-image")
class ProductDescriptionImageViewController {
    private final ProductDescriptionImageService productDescriptionImageService;

    @GetMapping("/{id}")
    public String viewImage(@PathVariable Long id, Model model) {
        // TODO: 서비스 메서드 필요
        return "page/product/description-image";
    }
}

@RestController
@RequestMapping("/api/product-option-prices")
@RequiredArgsConstructor
public class ProductOptionPriceController {
    private final ProductOptionPriceService productOptionPriceService;

    @PostMapping
    public String create() {
        return "create product option price";
    }
}

@Controller
@RequiredArgsConstructor
@RequestMapping("/product-option-price")
class ProductOptionPriceViewController {
    private final ProductOptionPriceService productOptionPriceService;

    @GetMapping("/form")
    public String createForm(Model model) {
        return "page/product/option-price-form";
    }
}

@RestController
@RequestMapping("/api/product-variants")
@RequiredArgsConstructor
public class ProductVariantController {
    private final ProductVariantService productVariantService;

    @GetMapping
    public String getAll() {
        return "get all product variants";
    }
}

@Controller
@RequiredArgsConstructor
@RequestMapping("/product-variant")
class ProductVariantViewController {
    private final ProductVariantService productVariantService;

    @GetMapping("/list")
    public String showVariants(Model model) {
        return "page/product/variant-list";
    }
}

@RestController
@RequestMapping("/api/product-variant-details")
@RequiredArgsConstructor
public class ProductVariantDetailController {
    private final ProductVariantDetailService productVariantDetailService;

    @PostMapping
    public String create() {
        return "create product variant detail";
    }
}

@Controller
@RequiredArgsConstructor
@RequestMapping("/product-variant-detail")
class ProductVariantDetailViewController {
    private final ProductVariantDetailService productVariantDetailService;

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        return "page/product/variant-detail";
    }
}

@RestController
@RequestMapping("/api/product-variant-images")
@RequiredArgsConstructor
public class ProductVariantImageController {
    private final ProductVariantImageService productVariantImageService;

    @PostMapping
    public String create() {
        return "create product variant image";
    }
}

@Controller
@RequiredArgsConstructor
@RequestMapping("/product-variant-image")
class ProductVariantImageViewController {
    private final ProductVariantImageService productVariantImageService;

    @GetMapping("/form")
    public String uploadForm(Model model) {
        return "page/product/variant-image-form";
    }
}

@RestController
@RequestMapping("/api/colors")
@RequiredArgsConstructor
public class ColorsController {
    private final ColorsService colorsService;

    @GetMapping
    public String getAll() {
        return "get all colors";
    }
}

@Controller
@RequiredArgsConstructor
@RequestMapping("/color")
class ColorsViewController {
    private final ColorsService colorsService;

    @GetMapping("/list")
    public String colorList(Model model) {
        return "page/product/colors";
    }
}

@RestController
@RequestMapping("/api/models")
@RequiredArgsConstructor
public class ModelController {
    private final ModelService modelService;

    @PostMapping
    public String create() {
        return "create model";
    }
}

@Controller
@RequiredArgsConstructor
@RequestMapping("/model")
class ModelViewController {
    private final ModelService modelService;

    @GetMapping("/form")
    public String form(Model model) {
        return "page/product/model-form";
    }
}

// 실제 기능 메서드(GET/POST/PUT/DELETE)는 이후 필요에 따라 구체적으로 구현
