package com.example.healthme.domain.admin.service;

import com.example.healthme.domain.admin.dto.InsertProductDto;
import com.example.healthme.domain.admin.entity.ProductEntity;
import com.example.healthme.domain.admin.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service

public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    // 모든 제품 다 가져오기
    public List<ProductEntity> selectAllProduct(){

        List<ProductEntity> list=productRepository.findAll();
        if (!list.isEmpty()){
            return list;
        }
        return null;

    }

    // 페이지네이션에 맞춰서 제품 가져오기
    public Page<ProductEntity> select_Page_Product(int page, int size){
        PageRequest pageRequest = PageRequest.of(page, size);

        return productRepository.findAllByOrderByNoDesc(pageRequest);
    }


    // 제품 등록하기
    public void insert_product(InsertProductDto insertProductDto){

        if (insertProductDto == null) return;

        String saveDir = "C:/uploads/product/";
        new File(saveDir).mkdirs();

        String thumbFileName = System.currentTimeMillis() + "_" + insertProductDto.getThumbnailUrl().getOriginalFilename();
        String detailFileName = System.currentTimeMillis() + "_" + insertProductDto.getDetailUrl().getOriginalFilename();

        File thumbDest = new File(saveDir + thumbFileName);
        File detailDest = new File(saveDir + detailFileName);

        try {
            insertProductDto.getThumbnailUrl().transferTo(thumbDest);
            insertProductDto.getDetailUrl().transferTo(detailDest);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        ProductEntity product = new ProductEntity();
        product.setProductName(insertProductDto.getProductName());
        product.setCategory(insertProductDto.getCategory());
        product.setAmount(insertProductDto.getAmount());
        product.setDetailUrl("/images/product" + thumbFileName);
        product.setThumbnailUrl("/images/product" + detailFileName);
        product.setProductPrice(insertProductDto.getProductPrice());
        product.setDescription(insertProductDto.getDescription());

        productRepository.save(product);
        // insertProductDto는 InsertProductDto 이고 이걸 넣으면 안된다.
        // 왜냐하면 productRepository는 Product를 상속하기 때문이다.


    }

    // 제품 수정
    public boolean update_product(int no, InsertProductDto dto) {
        Optional<ProductEntity> optional = productRepository.findById(no); // 원래 DB에 있던 값
        if (optional.isEmpty()) {
            return false;
        }

        ProductEntity product = optional.get();
        product.setCategory(dto.getCategory()); // 수정할 데이터가 product에 들어간다.
        product.setProductName(dto.getProductName());
        product.setProductPrice(dto.getProductPrice());
        product.setAmount(dto.getAmount());
        product.setDescription(dto.getDescription());

        String saveDir = "C:/uploads/product/";
        new File(saveDir).mkdirs();

        try {
            if (dto.getThumbnailUrl() != null && !dto.getThumbnailUrl().isEmpty()) {
                String thumbFileName = System.currentTimeMillis() + "_" + dto.getThumbnailUrl().getOriginalFilename();
                dto.getThumbnailUrl().transferTo(new File(saveDir + thumbFileName));
                product.setThumbnailUrl("/images/product" + thumbFileName);
            }

            if (dto.getDetailUrl() != null && !dto.getDetailUrl().isEmpty()) {
                String detailFileName = System.currentTimeMillis() + "_" + dto.getDetailUrl().getOriginalFilename();
                dto.getDetailUrl().transferTo(new File(saveDir + detailFileName));
                product.setDetailUrl("/images/product" + detailFileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        productRepository.save(product);
        return true;
    }

    public boolean product_delete(int noArray[]) {
        if (noArray.length != 0){
            for (int no : noArray){
                productRepository.deleteById(no);
            }
            return true;
        }
        return false;
    }
}
