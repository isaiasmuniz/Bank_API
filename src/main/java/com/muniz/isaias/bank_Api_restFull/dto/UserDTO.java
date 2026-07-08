package com.muniz.isaias.bank_Api_restFull.dto;

import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class UserDTO extends RepresentationModel<UserDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long UserId;
    private String name;
    private String email;
    private String password;
    private Date creationDate;
    private List<AccountDTO> accountDTOList = new ArrayList<>();

    public UserDTO() {
    }

    public UserDTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        this.UserId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<AccountDTO> getAccountDTOList() {
        return accountDTOList;
    }

    public void setAccountDTOList(List<AccountDTO> accountDTOList) {
        this.accountDTOList = accountDTOList;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO dto = (UserDTO) o;
        return Objects.equals(UserId, dto.UserId) && Objects.equals(name, dto.name) && Objects.equals(email, dto.email) && Objects.equals(password, dto.password) && Objects.equals(creationDate, dto.creationDate) && Objects.equals(accountDTOList, dto.accountDTOList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(UserId, name, email, password, creationDate, accountDTOList);
    }
}
