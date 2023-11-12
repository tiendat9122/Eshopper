package com.ecommerce.api.eshopper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
@EnableJpaRepositories
public class EshopperApplication {

	public static void main(String[] args) {
		SpringApplication.run(EshopperApplication.class, args);
	}

//	@Bean
//	CommandLineRunner runner(UserService userService, RoleService roleService) {
//		return args -> {
//			roleService.saveRole(new Role(null, "ROLE_USER", new HashSet<>()));
//			roleService.saveRole(new Role(null, "ROLE_MANAGER", new HashSet<>()));
//			roleService.saveRole(new Role(null, "ROLE_ADMIN", new HashSet<>()));
//			roleService.saveRole(new Role(null, "ROLE_SUPER_ADMIN", new HashSet<>()));
//
//			userService.saveUser(new User(null, "Tien Dat", "tiendat", "tiendat@gmail.com", "1", new HashSet<>()));
//			userService.saveUser(new User(null, "Tra My", "tramy", "tramy@gmail.com", "1", new HashSet<>()));
//
//			userService.addRoleToUser("tiendat@gmail.com", "ROLE_USER");
//			userService.addRoleToUser("tiendat@gmail.com", "ROLE_MANAGER");
//			userService.addRoleToUser("tiendat@gmail.com", "ROLE_ADMIN");
//			userService.addRoleToUser("tiendat@gmail.com", "ROLE_SUPER_ADMIN");
//
//			userService.addRoleToUser("tramy@gmail.com", "ROLE_USER");
//			userService.addRoleToUser("tramy@gmail.com", "ROLE_MANAGER");
//		};
//	}

//	ManyToMany
//	@Bean
//	CommandLineRunner commandLineRunner(ProductService productService, CategoryService categoryService) {
//		return args -> {
//			productService.saveProduct(new Product(null, "Hai so phan", new HashSet<>()));
//			productService.saveProduct(new Product(null, "Dac nhan tam", new HashSet<>()));
//
//			categoryService.saveCategory(new Category(null, "Tieu thuyet", new HashSet<>()));
//			categoryService.saveCategory(new Category(null, "Ky nang song", new HashSet<>()));
//
//			productService.addCategoryToProduct("Hai so phan", "Tieu thuyet");
//			productService.addCategoryToProduct("Hai so phan", "Ky nang song");
//			productService.addCategoryToProduct("Dac nhan tam", "Ky nang song");
//		};
//	}

//	OneToMany
//	@Bean
//	CommandLineRunner commandLineRunner(ProductService productService, CategoryService categoryService) {
//		return args -> {
//			categoryService.saveCategory(Category.builder().id(null).name("Tieu thuyet").build());
//			categoryService.saveCategory(Category.builder().id(null).name("Ky nang song").build());
//
//			productService.saveProduct(Product.builder().id(null).name("Hai so phan").build());
//			productService.saveProduct(Product.builder().id(null).name("Dac nhan tam").build());
//			productService.saveProduct(Product.builder().id(null).name("Nha gia kim").build());
//			productService.saveProduct(Product.builder().id(null).name("Nguoi dua dieu").build());
//
//			productService.addCategoryToProduct("Hai so phan", "Tieu thuyet");
//			productService.addCategoryToProduct("Dac nhan tam", "Ky nang song");
//			productService.addCategoryToProduct("Nha gia kim", "Tieu thuyet");
//			productService.addCategoryToProduct("Nguoi dua dieu", "Ky nang song");
//		};
//	}
}

