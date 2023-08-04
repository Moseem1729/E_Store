package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.config.AppConstants;
import com.lcwd.electronic.store.dtos.PageableResponse;
import com.lcwd.electronic.store.dtos.ProductDto;
import com.lcwd.electronic.store.entities.Product;
import com.lcwd.electronic.store.exceptions.ResourceNotFoundException;
import com.lcwd.electronic.store.helper.Helper;
import com.lcwd.electronic.store.repositories.ProductRepository;
import com.lcwd.electronic.store.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper modelMapper;

    //other dependency

    @Override
    public ProductDto create(ProductDto productDto) {
        String productId = UUID.randomUUID().toString();
        Product product = modelMapper.map(productDto, Product.class);
        product.setProductId(productId);
        product.setAddedDate(new Date());

        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto update(ProductDto productDto, String productId) {
        //fetch given Id
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product resource not found !!"));
        product.setTitle(productDto.getTitle());
        product.setDescription((productDto.getDescription()));
        product.setPrice((productDto.getPrice()));
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setLive(productDto.isLive());
        product.setStock(productDto.isLive());

        //save entity
        Product updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct, ProductDto.class);
    }

    @Override
    public void delete(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product resource not found !!"));
        productRepository.delete(product);
    }

    @Override
    public ProductDto getSingle(String productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product resource not found !!"));

        return modelMapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findAll(pageable);
        return Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findByLiveTrue(pageable);
        return Helper.getPageableResponse(page, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> page = productRepository.findByTitleContaining(subTitle, pageable);
        return Helper.getPageableResponse(page, ProductDto.class);
    }
}
