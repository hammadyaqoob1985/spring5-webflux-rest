package guru.springframework.spring5webfluxrest.bootstrap;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final VendorRepository vendorRepository;
    private final CategoryRepository categoryRepository;

    public Bootstrap(VendorRepository vendorRepository, CategoryRepository categoryRepository) {
        this.vendorRepository = vendorRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (vendorRepository.count().block() == 0) {
            loadVendors();
        }

        if (categoryRepository.count().block() == 0) {
            loadCategories();
        }

        log.info("" + vendorRepository.count().block());
        log.info("" + categoryRepository.count().block());
    }

    private void loadCategories() {
        categoryRepository.save(Category.builder().description("Fruits").build()).block();
        categoryRepository.save(Category.builder().description("Nuts").build()).block();
        categoryRepository.save(Category.builder().description("Breads").build()).block();
        categoryRepository.save(Category.builder().description("Meats").build()).block();
        categoryRepository.save(Category.builder().description("Eggs").build()).block();
    }

    private void loadVendors() {

        vendorRepository.save(Vendor.builder().firstName("Hammad").lastName("Yaqoob").build()).block();
        vendorRepository.save(Vendor.builder().firstName("Sana").lastName("Ilahi").build()).block();
    }


}
