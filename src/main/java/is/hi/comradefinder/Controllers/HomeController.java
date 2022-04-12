package is.hi.comradefinder.Controllers;

import is.hi.comradefinder.ComradeFinderApplication;
import is.hi.comradefinder.Persistence.Entities.Account;
import is.hi.comradefinder.Persistence.Entities.Company;
import is.hi.comradefinder.Persistence.Entities.User;
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


@RestController
public class HomeController {

    CompanyService companyService;
    UserService userService;


    private static final Logger log =  LoggerFactory.getLogger(ComradeFinderApplication.class);

    @Autowired
    public HomeController(CompanyService companyService, UserService userService) {
        this.companyService = companyService;
        this.userService = userService;
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

    @RequestMapping("/Login/{username}/{password}")
    public Account getUser(
            @PathVariable(value="username") String username,
            @PathVariable(value="password") String password)
    {
        log.info("Login request received: " + username + " & " + password);
        User user = userService.findByUsername(username);
        if (user != null) {
            user.setPassword(password);
            if (userService.login(user) != null) {
                user.setPassword(""); // For security reasons (no need to store password in app after login)
                return user;
            }
        }
        Company company = companyService.findByUsername(username);
        if (user != null) {
            company.setPassword(password);
            if (companyService.login(company) != null) {
                company.setPassword(""); // For security reasons (no need to store password in app after login)
                return company;
            }
        }

        return null;
    }

    @RequestMapping(value = "/registerUser", method = RequestMethod.POST)
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User info is null");
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already taken");
        }
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

}
