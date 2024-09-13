package com.flamontech.ecommerce.product;

import com.flamontech.ecommerce.exception.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public Integer createProduct(ProductRequest request) {

        var product = mapper.toProduct(request);

        return repository.save(product).getId();
    }

    public List<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> request) {

        // EXTRACT ALL THE PRODUCT ID FROM REQUEST
        var productIds = request
                .stream()
                .map(ProductPurchaseRequest::productId)
                .toList();

        // GET ALL THE PRODUCT INFORMATION FROM DATABASE & CHECK ALL THE PRODUCTS ARE AVAILABLE
        var storedProducts = repository.findAllByIdInOrderById(productIds);
        if (productIds.size() != storedProducts.size())
            throw new ProductPurchaseException("One or more product does not exists");


        var storedRequests = request
                .stream()
                .sorted(Comparator.comparing(ProductPurchaseRequest::productId))
                .toList();

        // EMPTY PURCHASED PRODUCT LIST
        var purchasedProducts = new ArrayList<ProductPurchaseResponse>();

       for (var i = 0; i < storedProducts.size(); i++) {
           var product = storedProducts.get(i);
           var productRequest = storedRequests.get(i);

           // CHECK QUANTITY
           if (product.getQuantity() < productRequest.quantity()){
               throw new ProductPurchaseException(
                       String.format("Insufficient stock quantity for product id %s",productRequest.productId()));
           }

           // ADJUST QUANTITY
           var newAvailableQuantity = product.getQuantity() - productRequest.quantity();

           // UPDATE QUANTITY
           product.setQuantity(newAvailableQuantity);
           repository.save(product);

           // ADD PRODUCT IN PURCHASED PRODUCT LIST
           purchasedProducts.add(mapper.toProductPurchaseResponse(product, productRequest.quantity()));
       }

        return purchasedProducts;
    }

    public ProductResponse findById(Integer productId) {
        return repository.findById(productId)
                .map(mapper::fromProductResponse)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Product with id '%s' not found", productId)
                ));
    }

    public List<ProductResponse> findAll() {

        return repository.findAll()
                .stream()
                .map(mapper::fromProductResponse)
                .collect(Collectors.toList());
    }
}
