package is.hi.comradefinder.Controllers;

import is.hi.comradefinder.ComradeFinderApplication;
import is.hi.comradefinder.Persistence.Entities.*;
import is.hi.comradefinder.Services.AdService;
import is.hi.comradefinder.Services.CompanyService;
import is.hi.comradefinder.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
public class HomeController {

    CompanyService companyService;
    UserService userService;
    AdService adService;


    private static final Logger log =  LoggerFactory.getLogger(ComradeFinderApplication.class);

    @Autowired
    public HomeController(CompanyService companyService, UserService userService, AdService adService) {
        this.companyService = companyService;
        this.userService = userService;
        this.adService = adService;
    }

    @RequestMapping(value = "/")
    public String HomePage() {
        return "home";
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String loginGET(Company company, User user) {
       // log.info(model.toString());
        return "home";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)//, params = "", path = "" )
    public String loginPOST(Company company, User user, BindingResult result, Model model, HttpSession session) {

        if (result.hasErrors()) {
            return "redirect:/";
        }

        log.info("user " +user);
        log.info("company " + company);

        Company companyExists = companyService .login(company);
        User userExists = userService .login(user);

        log.info("user " +userExists);
        log.info("company " + companyExists);

        if (userExists != null) {
            log.info(userExists.getUsername());
            session.setAttribute("LoggedInUser", userExists);
            model.addAttribute("LoggedInUser", userExists);
            return "redirect:/user/" + userExists.getID();
        }
        else if (companyExists != null) {
            log.info(companyExists.getUsername());

            session.setAttribute("LoggedInUser", companyExists);
            model.addAttribute("LoggedInUser", companyExists);
            model.addAttribute("type", companyExists.getType());
            return "redirect:/company/" + companyExists.getID();

        }
        return "redirect:/";
    }


    //==================================================================================================================
    // BACKEND MAPPING FOR THE ANDROID APP
    //==================================================================================================================

    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    public ResponseEntity<?> POSTUser(@Valid @RequestBody ArrayList<String> user) {
        log.info("Logging out info: " + user.toString());
        String username = user.get(0);
        String password = user.get(1);
        String phone = user.get(2);
        String email = user.get(3);
        log.info("Register request received: " + username + " & " + password);
        if (username == null || username == "" ||
                password == null || password == "" ||
                phone == null || email == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User info is null");
        }
        if (userService.findByUsername(username) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already taken");
        }
        // As of right now, displayName and description is unsupported. Might be added later.
        User userCreated = new User(username, password, phone, email, "", "");
        log.info("Register checks passed");
        return new ResponseEntity<>(userService.save(userCreated), HttpStatus.CREATED);
    }

    @RequestMapping("/Login/{username}/{password}")
    public Account GETUser(
            @PathVariable(value="username") String username,
            @PathVariable(value="password") String password)
    {
        log.info("Login request received: " + username + " & " + password);
        User user = new User(username, password, "", "", "", "");
        log.info("Password check: " + user.getPassword());
        if (userService.login(user) != null) {
            user.setPassword(""); // For security reasons (no need to store password in app after login)
            return user;
        }
        Company company = new Company(username, password, "", "", "", "", 0, "", "");
        if (companyService.login(company) != null) {
            company.setPassword(""); // For security reasons (no need to store password in app after login)
            return company;
        }

        return null;
    }

    @RequestMapping(value = "/createAd", method = RequestMethod.POST)
    public ResponseEntity<?> POSTAd(@Valid @RequestBody ArrayList<String> ad) {
        log.info("Logging out info: " + ad.toString());
        String title = ad.get(0);
        String description = ad.get(1);
        String salaryRange = ad.get(2);
        String extraQuestions = ad.get(3);
        log.info("Logging out info:  extraQuestions=" + extraQuestions);
        String companyUsername = ad.get(4);
        List<Application> applications = List.of();
        String tags = ad.get(5);
        log.info("Logging out info:  tags=" + tags);
        // TODO: Find a way to convert extraQuestions and Tags to List<String>
        // TODO: Check if ad from this company with this title already exists
        // TODO: NonNull: Title, Description, SalaryRange, CompanyUsername


        // As of right now, displayName and description is unsupported. Might be added later.
        //Ad adCreated = new User(title, description);
        //log.info("Register checks passed");
        //return new ResponseEntity<>(adService.save(adCreated), HttpStatus.CREATED);
        return null;
    }

    // TODO: Maybe I should do some try catches here.
    @RequestMapping(value = "/getAds", method = RequestMethod.GET)
    public List<Ad> GETAds() {
        return adService.findAll();
    }

    @RequestMapping(value = "/getAdsByCompany/{username}", method = RequestMethod.GET)
    public List<Ad> GETAdsByCompany(@PathVariable(value="username") String username) {
        return adService.findAdsByCompany(username);
    }

    @RequestMapping(value = "/deleteAd/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> POSTDeleteAd(@PathVariable(value="id") long id) {
        try {
            adService.deleteByID(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch(Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Returned error: " + e.toString());
        }
    }



}
