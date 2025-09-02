package cz.rohlik.commerce.application.module.product;

import cz.rohlik.commerce.domain.model.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductFinderService {

    private final ProductRepository productRepository;
}
