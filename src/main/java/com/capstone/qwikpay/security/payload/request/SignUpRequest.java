package com.capstone.qwikpay.security.payload.request;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SignUpRequest {
	@NotBlank
	@Size(min = 3, max = 20)
	private String username;

	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	private Set<String> role;

	@NotBlank
	@Size(min = 6, max = 40)
	private String password;

    @NotBlank
    //@Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits")
    private String mobile;

	@NotBlank
	@Size(max = 220)
	private String address;

	@NotBlank
	@Size(max = 11)
	private String gender;

	private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

	public SignUpRequest() {
		// TODO Auto-generated constructor stub
	}


    public SignUpRequest(@NotBlank @Size(min = 3, max = 20) String username,
                         @NotBlank @Size(max = 50) @Email String email, Set<String> role,
                         @NotBlank @Size(min = 6, max = 40) String password,
                         @NotBlank @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be 10 digits") String mobile,
                         @NotBlank @Size(max = 220) String address, @NotBlank @Size(max = 11) String gender) {
		this.username = username;
		this.email = email;
		this.role = role;
		this.password = password;
		this.mobile = mobile;
		this.address = address;
		this.gender = gender;
		this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<String> getRole() {
		return role;
	}

	public void setRole(Set<String> role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}