package com.example.demo.account.service;

import com.example.demo.account.dto.request.saveDto.AccountUpdateDto;
import com.example.demo.account.dto.request.saveDto.AddRegisterDto;
import com.example.demo.account.dto.response.viewDto.AccountProfileDto;
import com.example.demo.account.entity.Account;
import com.example.demo.account.entity.enums.Role;
import com.example.demo.account.repository.AccountRepository;
import com.example.demo.s3.dto.ProfileDto;
import com.example.demo.s3.service.AwsService;
import com.example.demo.security.oauth.ProviderUser;
import com.example.demo.address.entity.Address;
import com.example.demo.address.entity.AddressList;
import com.example.demo.address.repository.AddressListRepository;
import com.example.demo.address.repository.AddressRepository;
import com.example.demo.address.repository.Home;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final AddressRepository addressRepository;
    private final AddressListRepository addressListRepository;
    private final AwsService awsService;

    @Transactional(readOnly = true)
    public boolean duplicateEmail(String email) {
        boolean checkEmail = accountRepository.existsAccountByEmail(email);
        if (checkEmail) {
            return true;
        }
        return false;
    }
    @Transactional(readOnly = true)
    public boolean duplicateNickName(String nickName) {
        boolean byNickName = accountRepository.existsAccountByNickName(nickName);
        if (byNickName) {
            return true;
        }
        return false;
    }

    @Transactional
    public void register(String registrationId, ProviderUser providerUser) {
        Account account = Account.builder()
                .snsId(providerUser.getId())
                .email(providerUser.getEmail())
                .profileUrl(providerUser.getOAuth2Url())
                .provider(registrationId)
                .role(Role.CLIENT)
                .registerFirst(false) //0
                .build();
        accountRepository.save(account);
    }

    @Transactional
    public void UpdateUser(ProviderUser providerUser) {
        Account account = accountRepository.findBySnsId(providerUser.getId());
        account.updateRegisterFirst(true); //1
    }

    @Transactional
    public Long saveAccount(Account account, AddRegisterDto addRegisterDto) throws IOException, ParseException, java.text.ParseException {

        //회원 프록시
        Account findAccount = accountRepository.getById(account.getId());
        //다른 유저와 동일한 주소가 있는지 체크
        Optional<Home> home = addressRepository.findOnlyByHome(addRegisterDto.getAddress());

        if (!home.isPresent()) {
            //주소 찾기
            AddressList addressList = addressListRepository.findByHome(addRegisterDto.getAddress());

            //주소 저장
            Address saveAddress = new Address(addressList.getHome(), addressList.getX(), addressList.getY(), addressList.getAddressCode());
            addressRepository.save(saveAddress);
        }
        findAccount.addRegister(addRegisterDto.getNickName(), addRegisterDto.getAddress(), addRegisterDto.getTos());
        return account.getId();
    }

    @Transactional(readOnly = true)
    public String pictureView(Account account) {
        Account auth2Url = accountRepository.findByProfileUrl(account.getProfileUrl());
        return auth2Url.getProfileUrl();
    }

    @Transactional
    public Account updateAccount(Account account, AccountUpdateDto accountUpdateDto, MultipartFile file) throws IOException {

        //회원 프록시
        Optional<Account> optionalAccount = accountRepository.findById(account.getId());
        optionalAccount.get().updateAccount(accountUpdateDto.getNickName(), accountUpdateDto.getSelfInfo(), accountUpdateDto.getAddress());
        if(file == null) {
            log.info("사진업로드를 하지 않았습니다.");
        } else { //aws 사진이면 삭제하고 저장해야함
            if(account.getProfileUrl().contains("amazonaws")) {
                awsService.deleteProfile("/account", account.getProfileUrlName());
            }
            ProfileDto profileDto = awsService.uploadOneProfile(file, "/account");
            account.updateProfileUrl(profileDto.getUrl(), profileDto.getFileName());
        }
        return account;
    }

    @Transactional(readOnly = true)
    public AccountProfileDto profileView(Account account) {
        AccountProfileDto profileDto = AccountProfileDto.of(account);
        return profileDto;
    }

    @Transactional
    public void deleteUser(Account account) {
        accountRepository.delete(account);
    }
}
