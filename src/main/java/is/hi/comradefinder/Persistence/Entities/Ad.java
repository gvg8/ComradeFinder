package is.hi.comradefinder.Persistence.Entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long ID;
    private String title;
    private String description;
    private String salaryRange;
    @ElementCollection(fetch= FetchType.LAZY)
    private List<String> extraQuestions;
    //@OneToOne(mappedBy="ad", cascade= CascadeType.ALL, fetch= FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name="company_username")
    private String companyUsername;
    private String linkToPDFImage;
    @OneToMany(fetch=FetchType.LAZY)
    private List<Application> applications;
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> tags;

    // Constructor chain
    public Ad() {}


    public Ad(String title, String description, List<String> extraQuestions, String companyUsername, String linkToPDFImage) {
        this.title = title;
        this.description = description;
        this.salaryRange = salaryRange;
        this.extraQuestions = extraQuestions;
        this.companyUsername = companyUsername;
        this.linkToPDFImage = linkToPDFImage;
    }

    // GETTER ANS SETTERS

    public void addTag(String tag) {
        this.tags.add(tag);
    }
    public void addTags(List<String> tags) {
        this.tags.addAll(tags);
    }

    public void removeTag(String tag) {
        this.tags.remove(tag);
    }
    public void removeTags(List<String> tags) {
        this.tags.removeAll(tags);
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSalaryRange() { return salaryRange; }

    public void setSalaryRange(String priceRange) { this.salaryRange = priceRange; }

    public List<String> getExtraQuestions() {
        return extraQuestions;
    }

    public void setExtraQuestions(List<String> extraQuestions) {
        this.extraQuestions = extraQuestions;
    }

    public String getCompanyUsername() {
        return companyUsername;
    }

    public void setCompanyUsername(String company) {
        this.companyUsername = company;
    }

    public String getLinkToPDFImage() {
        return linkToPDFImage;
    }

    public void setLinkToPDFImage(String linkToPDFImage) {
        this.linkToPDFImage = linkToPDFImage;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Ad{" +
                "id=" + ID +
                ", title='" + title + '\'' +
                ", description=" + description +
                ", priceRange=" + salaryRange +
                ", extraQuestions=" + extraQuestions +
                ", company=" + companyUsername +
                ", linkToPDFImage='" + linkToPDFImage + '\'' +
                ", applications=" + applications +
                ", tags=" + tags +
                '}';
    }
}
