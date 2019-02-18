package guru.springframework.spring5webfluxrest.controllers;

import guru.springframework.spring5webfluxrest.domain.Category;
import guru.springframework.spring5webfluxrest.domain.Vendor;
import guru.springframework.spring5webfluxrest.repositories.CategoryRepository;
import guru.springframework.spring5webfluxrest.repositories.VendorRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

public class VendorControllerTest {

    WebTestClient webTestClient;
    VendorRepository vendorRepository;
    VendorController vendorController;

    @Before
    public void setUp() throws Exception {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();

    }

    @Test
    public void list() {

        given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("testFirstname1").lastName("lastFirstname1").build(),
                        Vendor.builder().firstName("testFirstname2").lastName("lastFirstname2").build()));
        webTestClient.get().uri("/api/v1/vendors/")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);
    }

    @Test
    public void getById() {

        given(vendorRepository.findById("someId"))
                .willReturn(Mono.just( Vendor.builder().firstName("testFirstname2").lastName("lastFirstname2").build()));

        webTestClient.get().uri("/api/v1/vendors/someId")
                .exchange()
                .expectBody(Category.class);
    }

    @Test
    public void testCreateVendor() {
        given(vendorRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().build()));

        Mono<Vendor> vendToSaveMono = Mono.just(Vendor.builder()
                .firstName("firstName1")
                .lastName("lastName1")
                .build());

        webTestClient
                .post()
                .uri("/api/v1/vendors/")
                .body(vendToSaveMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void testUpdateVendor() {
        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendToSaveMono = Mono.just(Vendor.builder()
                .firstName("firstName1")
                .lastName("lastName1")
                .build());

        webTestClient
                .put()
                .uri("/api/v1/vendors/any")
                .body(vendToSaveMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

    }

    @Test
    public void testPatchVendor() {
        given(vendorRepository.findById(any(String.class)))
                .willReturn(Mono.just(Vendor.builder()
                        .firstName("firstName1")
                        .lastName("lastName1")
                        .build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder()
                        .firstName("firstName1U")
                        .lastName("lastName1U")
                        .build()));

        Mono<Vendor> vendToUpdateMono = Mono.just(Vendor.builder()
                .firstName("firstName1U")
                .lastName("lastName1U")
                .build());

        webTestClient
                .patch()
                .uri("/api/v1/vendors/any")
                .body(vendToUpdateMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository).save(any());

    }
}