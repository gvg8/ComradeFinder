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

    //==================================================================================================================
    // REGISTER & LOGIN
    //==================================================================================================================

    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    public ResponseEntity<?> POSTUser(@Valid @RequestBody ArrayList<String> user) {
        log.info("Logging out info: " + user.toString());
        String username = user.get(0);
        String password = user.get(1);
        String phone = user.get(2);
        String email = user.get(3);
        log.info("Register request received: " + username + " & " + password);
        if (username == null || username.equals("") ||
                password == null || password.equals("") ||
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

    @RequestMapping(value = "/registerCompany", method = RequestMethod.POST)
    public ResponseEntity<?> POSTCompany(@Valid @RequestBody ArrayList<String> company) {
        log.info("Logging out info: " + company.toString());
        String username = company.get(0);
        String password = company.get(1);
        String phone = company.get(2);
        String email = company.get(3);
        String companyName = company.get(4);
        String SSN = company.get(5);
        log.info("Register request received: " + username + " & " + password);
        if (username == null || username.equals("") ||
                password == null || password.equals("") ||
                phone == null || email == null ||
                companyName == null || SSN == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User info is null");
        }
        if (companyService.findByUsername(username) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already taken");
        }
        // As of right now, displayName and description is unsupported. Might be added later.
        Company companyCreated = new Company(username, password, phone, email, companyName, "", Integer.parseInt(SSN), "", "");
        log.info("Register checks passed");
        return new ResponseEntity<>(companyService.save(companyCreated), HttpStatus.CREATED);
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

    //==================================================================================================================
    // AD - CREATE, GET, GET BY COMPANY, DELETE
    //==================================================================================================================

    @RequestMapping(value = "/createAd", method = RequestMethod.POST)
    public ResponseEntity<?> POSTAd(@Valid @RequestBody ArrayList<String> ad) {
        // 1. GATHER INPUT
        // prefix t- means temporary
        log.info("Logging out info: " + ad.toString());
        String title = ad.get(0);
        String description = ad.get(1);
        String salaryRange = ad.get(2);
        String tExtraQuestions = ad.get(3);
        log.info("Logging out info:  extraQuestions=" + tExtraQuestions);
        String companyUsername = ad.get(4);
        List<Application> applications = List.of();
        String tTags = ad.get(5);
        log.info("Logging out info:  tags=" + tTags);

        // 2. CONVERT EXTRAQUESTIONS & TAGS FROM STRING TO LIST<STRING>
        // The front end adds the symbol ␟ around each string so we can decipher it.
        String[] EQArray = tExtraQuestions.split("␟");
        List<String> extraQuestions = new ArrayList();
        // i=i+2 because the string is only between every other ␟ symbol.
        for (int i = 1; i<EQArray.length; i=i+2) {
            extraQuestions.add(EQArray[i]);
        }
        log.info("tTags before regex:" + tTags);
        tTags = tTags.replaceAll("␟+", "␟");
        log.info("tTags after regex:" + tTags);
        String[] tagsArray = tTags.split("␟");
        List<String> tags = new ArrayList();
        for (int i = 1; i<tagsArray.length; i=i+2) {
            tags.add(tagsArray[i]);
        }
        log.info("Logging out info: tags=" + tags.toString());
        log.info("Logging out info: tags.get(0)=" + tags.get(0));
        // 3. CREATE AD
        Ad newAd = new Ad(title, description, extraQuestions, companyUsername, "", tags);

        // 4. CHECK IF AD IS VALID
        // Check if ad already exists
        List<Ad> AdsSameTitle = adService.findByTitle(title);
        for (Ad adSameTitle : AdsSameTitle) {
            if (adSameTitle.getCompanyUsername().equals(newAd.getCompanyUsername())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Ad with this title already exists for this company");
            }
        }
        // Check if required inputs were null
        String isMissing = "required but found empty";
        if (title == null || title.equals("")) isMissing = "{title} " + isMissing;
        if (description == null || description.equals("")) isMissing = "{description} " + isMissing;
        if (salaryRange == null || salaryRange.equals("")) isMissing = "{salaryRange} " + isMissing;
        if (companyUsername == null || companyUsername.equals("")) isMissing = "{companyUsername} " + isMissing;
        if (!isMissing.equals("required but found empty")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, isMissing);
        }

        // 5. CREATE AD
        log.info("New Ad checks passed");
        return new ResponseEntity<>(adService.save(newAd), HttpStatus.CREATED);
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
