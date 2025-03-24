package com.TrendHive.TrendHive.services;

import com.TrendHive.TrendHive.dto.MerchantDto.MerchantPartialRequestDto;
import com.TrendHive.TrendHive.dto.MerchantDto.MerchantRequestDto;
import com.TrendHive.TrendHive.dto.MerchantDto.MerchantResponseDto;
import com.TrendHive.TrendHive.entities.Merchant;
import com.TrendHive.TrendHive.entities.Role;
import com.TrendHive.TrendHive.repository.MerchantRepository;
import com.TrendHive.TrendHive.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final ProductRepository productRepository;
    private final EmailService emailService;
    private final RoleService roleService;
//    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MerchantService(MerchantRepository merchantRepository, ProductRepository productRepository, EmailService emailService, RoleService roleService) {
        this.merchantRepository = merchantRepository;
        this.productRepository = productRepository;
        this.emailService = emailService;
        this.roleService = roleService;
//        this.passwordEncoder = passwordEncoder;
    }

    public Merchant create(Merchant merchant){
        Optional<Merchant> existingMerchant =merchantRepository.findByMerchantName(merchant.getMerchantName());
        if (existingMerchant.isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Merchant with username "+merchant.getMerchantName()+" is already exist!!");
        }
        Role role = roleService.findByName("ROLE_MERCHANT");
        merchant.setRoles(Set.of(role));
//        merchant.setPassword(passwordEncoder.encode(merchant.getPassword()));
        emailService.sendEmail(merchant.getEmail(), "\uD83D\uDE80 Welcome to TrendHive – Let’s Grow Your Business!",
                "Hello " + merchant.getMerchantName() + ",\n\nWelcome to TrendHive – where your business takes off! \uD83C\uDF89\uD83D\uDECD\uFE0F✨.\n\n" +
                        "Your seller account is now live, and you're ready to showcase your products to a growing community of eager shoppers.\nGet ready to boost your sales with seamless tools, powerful insights, and dedicated support." +
                        "\n\n\uD83D\uDD25 Start selling now\n Wishing you great success! \uD83D\uDCBC\uD83D\uDE80\n\n Team TrendHive");
        return merchantRepository.save(merchant);
    }

    public Page<Merchant> getAll(int page, int size, String sortDirection, String sortBy){
        Pageable pageable = PageRequest.of(page,  size, Sort.by(Sort.Direction.fromString(sortDirection),sortBy));
        return merchantRepository.findAll(pageable);
    }

    public Merchant getById(int id) {
        return merchantRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,""+
                "user with id "+id+" not found"));
    }

//    public void deleteById(int id){
//        getById(id);
//        merchantRepository.deleteById(id);
//    }

    public void deleteById(int id){
        Merchant merchant = merchantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Merchant Not found"));
//        Delete products first
        productRepository.deleteByMerchantId(id);

//        Delete merchant
        merchantRepository.deleteById(id);
    }

    public Merchant updateById(int id, Merchant merchant){
        Merchant existingMerchant = getById(id);
        if (merchant.getMerchantName() != null){
            existingMerchant.setMerchantName(merchant.getMerchantName());
        }
        if (merchant.getPassword() != null){
            existingMerchant.setPassword(merchant.getPassword());
        }
        if(merchant.getEmail() != null){
            existingMerchant.setEmail(merchant.getEmail());
        }
        return merchantRepository.save(existingMerchant);
    }

   

    public Merchant convertToMerchant(MerchantRequestDto merchantRequestDto){
        Merchant merchant = new Merchant();
        merchant.setMerchantName(merchantRequestDto.getMerchantName());
        merchant.setPassword(merchantRequestDto.getPassword());
        merchant.setEmail(merchantRequestDto.getEmail());
        return merchant;
    }

    public MerchantResponseDto convertToMerchantReponseDto(Merchant merchant){
        MerchantResponseDto merchantResponseDto = new MerchantResponseDto();
        merchantResponseDto.setId(merchant.getId());
        merchantResponseDto.setMerchantName(merchant.getMerchantName());
        merchantResponseDto.setPassword(merchant.getPassword());
        merchantResponseDto.setEmail(merchant.getEmail());
        merchantResponseDto.setCreatedDate(merchant.getCreatedDate());
        merchantResponseDto.setLastModifiedDate(merchant.getLastModifiedDate());
        merchantResponseDto.setRoles(merchant.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        return merchantResponseDto;
    }

    public Merchant converToMerchant(MerchantPartialRequestDto merchantPartialRequestDto){
        Merchant merchant = new Merchant();
        merchant.setMerchantName(merchantPartialRequestDto.getMerchantName());
        merchant.setPassword(merchantPartialRequestDto.getPassword());
        merchant.setEmail(merchantPartialRequestDto.getEmail());
        return merchant;
    }
}
