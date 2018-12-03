package mobivik.controllers;


import org.json.JSONObject;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    @RequestMapping(value={"/","/index","/default","/home","/welcome"})
    public String home(Model model){

        String username = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        model.addAttribute("username", username);

        return "index";
    }

    @RequestMapping(value="/admin")
    public String admin(){

        return "admin";
    }
   
    @RequestMapping(value={"/login"})
    public String login(Model model) {
        if (isRememberMeAuthenticated()) {
            System.out.println("isRememberMeAuthenticated="+isRememberMeAuthenticated());
        }
        return "login";
    }
    /**
     *   Login form with error
     */
    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    @RequestMapping(value="/403")
    public String accessDenied(){
        return "403";
    }

    /**
     * Check if user is login by remember me cookie, refer
     * org.springframework.security.authentication.AuthenticationTrustResolverImpl
     */
    private boolean isRememberMeAuthenticated() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }

        return RememberMeAuthenticationToken.class.isAssignableFrom(authentication.getClass());
    }



    @GetMapping(path = "/test")
    public JSONObject sayHello()
    {
        return new JSONObject("{'aa':'bb'}");
    }
}