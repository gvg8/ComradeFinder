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
    private String priceRange;
    @ElementCollection(fetch= FetchType.LAZY)
    private List<String> extraQuestions;
    //@OneToOne(mappedBy="ad", cascade= CascadeType.ALL, fetch= FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name="company_username")
    private long companyID;
    private String linkToPDFImage;
    @OneToMany(fetch=FetchType.LAZY)
    private List<Application> applications;
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> tags;

    // Constructor chain
    public Ad() {}


    public Ad(String title, String description, List<String> extraQuestions, long companyID, String linkToPDFImage) {
        this.title = title;
        this.description = description;
        this.priceRange = priceRange;
        this.extraQuestions = extraQuestions;
        this.companyID = companyID;
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

    public String getPriceRange() { return priceRange; }

    public void setPriceRange(String priceRange) { this.priceRange = priceRange; }

    public List<String> getExtraQuestions() {
        return extraQuestions;
    }

    public void setExtraQuestions(List<String> extraQuestions) {
        this.extraQuestions = extraQuestions;
    }

    public long getCompanyID() {
        return companyID;
    }

    public void setCompanyID(long company) {
        this.companyID = company;
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
                ", priceRange=" + priceRange +
                ", extraQuestions=" + extraQuestions +
                ", company=" + companyID +
                ", linkToPDFImage='" + linkToPDFImage + '\'' +
                ", applications=" + applications +
                ", tags=" + tags +
                '}';
    }
}
