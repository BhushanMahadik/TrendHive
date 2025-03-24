package com.TrendHive.TrendHive.controllers;

import com.TrendHive.TrendHive.dto.MerchantDto.MerchantPartialRequestDto;
import com.TrendHive.TrendHive.dto.MerchantDto.MerchantRequestDto;
import com.TrendHive.TrendHive.dto.MerchantDto.MerchantResponseDto;
import com.TrendHive.TrendHive.entities.Merchant;
import com.TrendHive.TrendHive.services.MerchantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/merchants")
public class MerchantController {

    @Autowired
    MerchantService merchantService;

    @PostMapping
    public ResponseEntity<MerchantResponseDto> createMerchant(@Valid @RequestBody MerchantRequestDto merchantRequestDto){
        Merchant merchant =merchantService.convertToMerchant(merchantRequestDto);
        Merchant createdMerchant = merchantService.create(merchant);
        MerchantResponseDto merchantResponse= merchantService.convertToMerchantReponseDto(createdMerchant);
        return ResponseEntity.status(201).body(merchantResponse);
    }

    @GetMapping()
    public ResponseEntity<Page<MerchantResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        Page<MerchantResponseDto> allUsers = merchantService.getAll(page, size, sortDirection, sortBy)
                .map(merchant -> merchantService.convertToMerchantReponseDto(merchant));
        return ResponseEntity.ok().body(allUsers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MerchantResponseDto> getUserById(@PathVariable int id){
        Merchant merchant = merchantService.getById(id);
        MerchantResponseDto merchantResponse = merchantService.convertToMerchantReponseDto(merchant);
        return ResponseEntity.ok(merchantResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable int id) {
        merchantService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<MerchantResponseDto> updateUserById(@PathVariable int id,@Valid @RequestBody MerchantRequestDto merchantRequestDto){
        Merchant merchant =merchantService.convertToMerchant(merchantRequestDto);
        Merchant updatedMerchant = merchantService.updateById(id, merchant);
        MerchantResponseDto merchantResponse = merchantService.convertToMerchantReponseDto(merchant);
        return ResponseEntity.ok(merchantResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MerchantResponseDto> updatePartialUserById(@PathVariable int id, @Valid @RequestBody MerchantPartialRequestDto merchantPartialRequestDto){
        Merchant merchant = merchantService.converToMerchant(merchantPartialRequestDto);
        Merchant updatedMerchant = merchantService.updateById(id, merchant);
        MerchantResponseDto merchantResponse = merchantService.convertToMerchantReponseDto(updatedMerchant);
        return ResponseEntity.ok(merchantResponse);
    }
}
