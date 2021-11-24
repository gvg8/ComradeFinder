package is.hi.comradefinder.Controllers;

import is.hi.comradefinder.ComradeFinderApplication;
import is.hi.comradefinder.Persistence.Entities.Ad;
import is.hi.comradefinder.Persistence.Entities.Company;
import is.hi.comradefinder.Services.AdService;
import is.hi.comradefinder.Services.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class  AdController {

    AdService adService;
    private static final Logger log =  LoggerFactory.getLogger(ComradeFinderApplication.class);


    @Autowired
    public AdController (AdService adService) {
        this.adService = adService;
    }

    @RequestMapping(value="/makeAd", method = RequestMethod.GET)
    public String makeAdGET(Ad ad) {
        return "makeAd";
    }


    @RequestMapping(value="/makeAd", method = RequestMethod.POST)
    public String makeAd(Ad ad, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            return "makeAd";
        }


        Company company = (Company) session.getAttribute("LoggedInUser");
        ad.setCompany(company);
        log.info("ad");
        log.info(ad.toString());


        if (ad != null && company != null ) {
            log.info("1");
           // log.info(company.getAdvertisements().toString());
         //   List<Ad> ads = company.getAdvertisements();
         //   session.setAttribute("ads",ads);
            log.info("2");

           // adService.save(ad);
            log.info("3");
          //  log.info(ad.toString());
           // log.info(ads.toString());

           // ads.add(ad);
            adService.save(ad);
            log.info("4");

            return "redirect:/company/" + company.getID();
        }
        return "redirect:/";
    }
/*
    @RequestMapping(path= "/CreateAd/{companyId}", method=RequestMethod.GET)
    public String Ad(@PathVariable String companyId, /*Model model*/ /*) {
       // Company company = companyService.findCompany(companyId);
        return "CreateAd";
    }
    */

}
