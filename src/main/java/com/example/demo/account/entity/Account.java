package com.example.demo.account.entity;

import com.example.demo.account.entity.enums.Role;
import com.example.demo.common.BaseTimeEntity;
import com.example.demo.mygarden.entity.MyPlant;
import com.example.demo.security.entity.RefreshToken;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
public class Account extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id")
	private Long id;

	@Size(min = 2, max = 15)
	private String nickName;
	private String address;

	@Lob
	private String tos;

	@Size(min = 2, max = 40)
	private String selfInfo;

	private Boolean registerFirst;
	private String profileUrlName;

	@Enumerated(EnumType.STRING)
	private Role role;

	//oauth2
	private String snsId;
	private String profileUrl;
	private String email;
	private String provider;

	@OneToMany
	private List<MyPlant> plantList;

	//토큰
	@OneToOne(mappedBy = "account", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private RefreshToken refreshToken;

	//추가 정보 입력
	public void addRegister(String nickName, String address, String tos) {
		this.nickName = nickName;
		this.address = address;
		this.tos = tos;
	}

	//회원 수정 입력
	public void updateAccount(String nickName, String selfInfo, String address) {
		this.nickName = nickName;
		this.selfInfo = selfInfo;
		this.address = address;
	}

	//회원 사진 정보 수정
	public void updateProfileUrl(String url, String pictureName) {
		this.profileUrl = url;
		this.profileUrlName = pictureName;
	}

	//oauth2.0
	@Builder
	public Account(String snsId, String profileUrl, String email, String provider,
				   Boolean registerFirst, Role role) {
		this.snsId = snsId;
		this.profileUrl = profileUrl;
		this.email = email;
		this.provider = provider;
		this.registerFirst = registerFirst;
		this.role = role;
	}

	//기존 회원 or 신규 회원
	public void updateRegisterFirst(boolean b) {
		this.registerFirst = b;
	}

}
