package is.hi.comradefinder.Controllers;

import is.hi.comradefinder.Persistence.Entities.Company;
import is.hi.comradefinder.Services.AdService;
import is.hi.comradefinder.Services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;


@Controller
public class AdController {

    AdService adService;

    @Autowired
    public AdController (AdService adService) {
        this.adService = adService;
    }

    @RequestMapping(value="/makeAd", method = RequestMethod.GET)
    public String makeAdGET(Model model, HttpSession session) {


        return "makeAd";
    }

    @RequestMapping(value="/makeAd", method = RequestMethod.POST)
    public String makeAd(Model model) {

        return "makeAd";
    }
/*
    @RequestMapping(path= "/CreateAd/{companyId}", method=RequestMethod.GET)
    public String Ad(@PathVariable String companyId, /*Model model*/ /*) {
       // Company company = companyService.findCompany(companyId);
        return "CreateAd";
    }
    */

}
