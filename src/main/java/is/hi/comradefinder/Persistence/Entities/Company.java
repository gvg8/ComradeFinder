package is.hi.comradefinder.Persistence.Entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "companies")
public class Company extends Account {

    private String SSN; // assigned -1 if missing
    private String street;
    private String companyPhone;
    @OneToMany(fetch= FetchType.LAZY)
    private List<Ad> advertisements;


    public Company() {
    }

    public Company(String username, String password, String phone,
                   String email, String displayName, String description,
                   String SSN, String street, String companyPhone) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
        this.displayName = displayName;
        this.description = description;
        this.SSN = SSN;
        this.street = street;
        this.companyPhone = companyPhone;
    }

    // GETTERS AND SETTERS

    public String getSSN() {
        return SSN;
    }

    public void setSSN(String SSN) {
        this.SSN = SSN;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public List<Ad> getAdvertisements() {
        return advertisements;
    }

    public void setAdvertisements(List<Ad> advertisements) {
        this.advertisements = advertisements;
    }

    public String getType() {
        return "company";
    }

    
}
