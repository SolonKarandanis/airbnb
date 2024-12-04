package com.solon.airbnb.util;

import com.solon.airbnb.fileinfo.domain.FileInfo;
import com.solon.airbnb.listing.application.dto.CreatedListingDTO;
import com.solon.airbnb.listing.application.dto.DisplayCardListingDTO;
import com.solon.airbnb.listing.application.dto.SaveListingDTO;
import com.solon.airbnb.listing.application.dto.sub.DescriptionDTO;
import com.solon.airbnb.listing.application.dto.sub.ListingInfoDTO;
import com.solon.airbnb.listing.application.dto.sub.PictureDTO;
import com.solon.airbnb.listing.application.dto.vo.*;
import com.solon.airbnb.listing.domain.BookingCategory;
import com.solon.airbnb.shared.common.AuthorityConstants;
import com.solon.airbnb.shared.dto.Paging;
import com.solon.airbnb.user.application.dto.ReadUserDTO;
import com.solon.airbnb.user.application.dto.UserDTO;
import com.solon.airbnb.user.application.dto.UsersSearchRequestDTO;
import com.solon.airbnb.user.domain.AccountStatus;
import com.solon.airbnb.user.domain.Authority;
import com.solon.airbnb.user.domain.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class TestUtil {

    public static Authority createTestAuthority(){
        Authority auth = new Authority();
        auth.setName(AuthorityConstants.ROLE_LANDLORD);
        return auth;
    }

    public static User createTestUser(final Long userId){
        User user = new User();
        user.setId(userId);
        user.setFirstName("Robert");
        user.setLastName("Smith");
        user.setUsername("admin1");
        user.setPassword("123");
        user.setAuthorities(Set.of(createTestAuthority()));
        user.setPublicId(UUID.fromString(TestConstants.TEST_USER_PUBLIC_ID));
        return user;
    }

    public static UserDTO createTestUserDto(final Long userId){
        UserDTO userDto = new UserDTO();
        userDto.setId(userId);
        userDto.setFirstName("Robert");
        userDto.setLastName("Smith");
        userDto.setUsername("admin1");
        userDto.setPassword("123");
        userDto.setPublicId(UUID.fromString(TestConstants.TEST_USER_PUBLIC_ID));
        userDto.setAuthorityNames(List.of(AuthorityConstants.ROLE_LANDLORD));
        return userDto;
    }

    public static ReadUserDTO createTestReadUserDTO(final String publicId){
        return new ReadUserDTO(
                publicId,
                "Robert",
                "Smith",
                "skarandanis@gmail.com",
                "test",
                AccountStatus.ACTIVE,
                Set.of(AuthorityConstants.ROLE_LANDLORD)
        );
    }

    public static Authentication getTestAuthenticationFromUserDTO(UserDTO userDto, String token) {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDto, token, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);
        return SecurityContextHolder.getContext().getAuthentication();

    }


    public static Authentication getTestAuthenticationFromUserDTO(UserDTO userDto) {
        return getTestAuthenticationFromUserDTO(userDto, TestConstants.TEST_TOKEN);
    }

    public static Paging createPaging(int pagingSize, int pagingIndex, String sortCol, String sortDirection) {
        Paging output = new Paging();
        output.setPagingSize(pagingSize);
        output.setPagingStart(pagingIndex);
        output.setSortingColumn(sortCol);
        output.setSortingDirection(sortDirection);
        return output;
    }

    public static FileInfo generateTestFileInfo() {
        FileInfo file = new FileInfo();
        file.setId(2L);
        file.setMimeType("application/octet-stream");
        file.setFileName("CATALOGUE_HISTORY_FILE");
        file.setFileRefId(2L);
        file.setFileName("catalogue_v.0.1.xls");
        file.setFileSize(15872);
        return file;
    }

    public static UsersSearchRequestDTO generateUsersSearchRequestDTO(){
        UsersSearchRequestDTO dto = new UsersSearchRequestDTO();
        dto.setPaging(createPaging(10,1,"id","ASC"));
        return dto;
    }

    public static SaveListingDTO generateSaveListingDTO(){
        SaveListingDTO dto = new SaveListingDTO();
        dto.setCategory(BookingCategory.ALL);
        dto.setLocation("Greece");
        dto.setDescription(generateDescriptionDTO());
        dto.setPrice(generatePriceVO(5));
        dto.setInfos(generateListingInfoDTO());
        return dto;
    }

    public static PriceVO generatePriceVO(int price){
        return new PriceVO(price);
    }

    public static TitleVO generateTitleVO(String title){
        return new TitleVO(title);
    }

    public static DescriptionVO generateDescriptionVO(String description){
        return new DescriptionVO(description);
    }

    public static DescriptionDTO generateDescriptionDTO(){
        return new DescriptionDTO(generateTitleVO("title"),generateDescriptionVO("description"));
    }

    public static GuestsVO generateGuestsVO(int value){
        return new GuestsVO(value);
    }

    public static BedroomsVO generateBedroomsVO(int value){
        return new BedroomsVO(value);
    }

    public static BedsVO generateBedsVO(int value){
        return new BedsVO(value);
    }

    public static BathsVO generateBathsVO(int value){
        return new BathsVO(value);
    }

    public static ListingInfoDTO generateListingInfoDTO(){
        return new ListingInfoDTO(generateGuestsVO(5),generateBedroomsVO(5),generateBedsVO(5),generateBathsVO(5));
    }

    public static PictureDTO generatePictureDTO(){
        return new PictureDTO(TestConstants.TEST_FILE_CONTENT,"/jpeg",true);
    }

    public static DisplayCardListingDTO generateDisplayCardListingDTO(){
        return new DisplayCardListingDTO(generatePriceVO(5),"Greece",generatePictureDTO(),BookingCategory.ALL,TestConstants.TEST_USER_PUBLIC_ID);
    }

    public static CreatedListingDTO generateCreatedListingDTO(){
        return new CreatedListingDTO(TestConstants.TEST_USER_PUBLIC_ID);
    }

}
