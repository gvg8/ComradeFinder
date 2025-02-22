package is.hi.comradefinder.Services;

import is.hi.comradefinder.Persistence.Entities.Ad;

import java.util.List;
import java.util.Optional;

public interface AdService {
    Ad save(Ad ad);
    void delete(Ad ad);
    void deleteByID(long ID);

    List<Ad> findAll();
    Ad findByID(Long id);

    List<Ad> findByTitle(String title);
    List<Ad> findByTagsContaining(String tag);
    List<Ad> findAdsByCompany(String username);
}
