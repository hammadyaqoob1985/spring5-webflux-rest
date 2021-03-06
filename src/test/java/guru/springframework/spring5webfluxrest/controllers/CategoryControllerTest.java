package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

public class CategoryControllerTest {

    WebTestClient webTestClient;
    CategoryRepository categoryRepository;
    CategoryController categoryController;
    @Before
    public void setUp() throws Exception {

        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();

    }

    @Test
    public void list() {
        given(categoryRepository.findAll())
                .willReturn(Flux.just(Category.builder().description("testDescription1").build(),
                        Category.builder().description("testDescription2").build()));
        webTestClient.get().uri("/api/v1/categories/")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    public void getById() {

        given(categoryRepository.findById("someId"))
                .willReturn(Mono.just(Category.builder().description("testDescription1").build()));

        webTestClient.get().uri("/api/v1/categories/someId")
                .exchange()
                .expectBody(Category.class);
    }

    @Test
    public void testCreateCategory() {
        given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().build()));

        Mono<Category> catToSaveMono = Mono.just(Category.builder().description("cat1").build());

        webTestClient
                .post()
                .uri("/api/v1/categories/")
                .body(catToSaveMono, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();

    }

    @Test
    public void testUpdateCategory() {
        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToUpdateMono = Mono.just(Category.builder().description("cat1").build());

        webTestClient
                .put()
                .uri("/api/v1/categories/any")
                .body(catToUpdateMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

    }

    @Test
    public void testPatchCategory() {
        given(categoryRepository.findById(any(String.class)))
                .willReturn(Mono.just(Category.builder().description("cat1").build()));

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().description("cat1U").build()));

        Mono<Category> catToUpdateMono = Mono.just(Category.builder().description("cat1U").build());

        webTestClient
                .patch()
                .uri("/api/v1/categories/any")
                .body(catToUpdateMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository).save(any());

    }
}