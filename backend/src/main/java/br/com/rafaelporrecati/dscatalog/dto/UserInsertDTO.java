package br.com.rafaelporrecati.dscatalog.dto;

public class UserInsertDTO extends UserDTO{

    private String password;

    public UserInsertDTO() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
