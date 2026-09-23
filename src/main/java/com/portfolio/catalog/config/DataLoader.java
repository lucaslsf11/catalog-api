package com.portfolio.catalog.config;

import com.portfolio.catalog.model.Category;
import com.portfolio.catalog.model.Product;
import com.portfolio.catalog.repository.CategoryRepository;
import com.portfolio.catalog.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {
        if (categoryRepository.count() == 0) {
            Category eletronicos = new Category(null, "Eletrônicos", "Dispositivos e aparelhos eletrônicos em geral");
            Category perifericos = new Category(null, "Periféricos", "Acessórios e periféricos para computadores");
            Category moveis = new Category(null, "Móveis", "Móveis para setup e escritório");

            categoryRepository.saveAll(List.of(eletronicos, perifericos, moveis));

            Product p1 = new Product(null, "Monitor Gamer 27\"", "Monitor 144Hz IPS com suporte regulável", new BigDecimal("1299.90"), 15, perifericos);
            Product p2 = new Product(null, "Mouse Gamer Óptico", "Sensor de alta precisão 8500 DPI e switches mecânicos", new BigDecimal("189.90"), 40, perifericos);
            Product p3 = new Product(null, "Teclado Mecânico RGB", "Switches lineares com layout compacto ABNT2", new BigDecimal("349.00"), 25, perifericos);
            Product p4 = new Product(null, "Cadeira Ergonômica", "Apoio lombar e braços 3D para escritório", new BigDecimal("899.00"), 10, moveis);

            productRepository.saveAll(List.of(p1, p2, p3, p4));
        }
    }
}