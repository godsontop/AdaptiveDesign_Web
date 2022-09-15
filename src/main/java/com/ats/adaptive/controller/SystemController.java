package com.ats.adaptive.controller;

import com.ats.adaptive.model.User;
import com.ats.adaptive.service.ArchOutputService;
import com.ats.adaptive.service.EntitiesReadService;
import com.ats.adaptive.service.UserService;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class SystemController {

    @Autowired
    private UserService userService;

    @Autowired
    private EntitiesReadService entitiesReadService;

    @Autowired
    private ArchOutputService archOutputService;

//    @Value("${file.uploadurl}")
//    private String uploadPath;


    @GetMapping(value={"/", "/login"})
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }


    @GetMapping(value="/registration")
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @PostMapping(value = "/registration")
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByUserName(user.getUserName());
        if (userExists != null) {
            bindingResult
                    .rejectValue("userName", "error.user",
                            "用户名已被注册");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "注册成功！！");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }

    @GetMapping(value="/admin/home")
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", "欢迎 " + user.getName() + " " + user.getLastName() + "的访问"+" (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","当前页面内容只有管理员能访问");
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }

    @GetMapping(value="/admin/declareKG")
    public ModelAndView declareKG(){
        ModelAndView modelAndView = new ModelAndView();
        SecurityContextHolder.getContext().getAuthentication();
        return modelAndView;
    }

    @GetMapping(value="/admin/uploadGenerationSelect")
    public ModelAndView uploadGenerationDeclare(HttpSession session){
        List<String> generations = entitiesReadService.getAbsoluteDictionaries();
        session.setAttribute("generations",generations);
        ModelAndView modelAndView = new ModelAndView();
        SecurityContextHolder.getContext().getAuthentication();
        return modelAndView;
    }
    //    @PostMapping(value="/admin/importKG")
//    public ModelAndView uploadOperation(HttpSession session, @RequestParam("file") MultipartFile file, RedirectAttributes attributes){
//        ModelAndView modelAndView = new ModelAndView();
//        SecurityContextHolder.getContext().getAuthentication();
//        return modelAndView;
//    }
    @GetMapping(value="/admin/importKG")
    public ModelAndView importKG(HttpSession session,@RequestParam(name = "generation") String generation){
        ModelAndView modelAndView = new ModelAndView();
        SecurityContextHolder.getContext().getAuthentication();
        session.setAttribute("generation",generation);
        ArrayList<String> requirements = entitiesReadService.getEntitiesMap().get(generation);
        session.setAttribute("requirementsInGeneration",requirements);
        modelAndView.addObject("currentGeneration","导入到"+generation+"版本的实体集");
        return modelAndView;
    }


    @GetMapping(value="/admin/designArchitecture")
    public ModelAndView designArchitecture(){
        ModelAndView modelAndView = new ModelAndView();
        SecurityContextHolder.getContext().getAuthentication();
        return modelAndView;
    }

    @GetMapping(value="/admin/generationSelect")
    public ModelAndView generationDeclare(HttpSession session){
        List<String> generations = entitiesReadService.getAbsoluteDictionaries();
        session.setAttribute("generations",generations);
        ModelAndView modelAndView = new ModelAndView();
        SecurityContextHolder.getContext().getAuthentication();
        return modelAndView;
    }
    @GetMapping(value="/admin/requirementDeclare")
    public ModelAndView requirementDeclare(HttpSession session, @RequestParam(name = "generation") String generation){
        ModelAndView modelAndView = new ModelAndView();
        SecurityContextHolder.getContext().getAuthentication();
        ArrayList<String> requirements = entitiesReadService.getEntitiesMap().get(generation);
        session.setAttribute("selectedGeneration",generation);
        session.setAttribute("requirements",requirements);
        return modelAndView;
    }

    @GetMapping(value="/admin/viewArchitecture")
    public ModelAndView viewArchitecture(HttpSession session, @RequestParam(name = "requirements") ArrayList<String> requirements) throws OWLOntologyCreationException, IOException, OWLOntologyStorageException {
        ModelAndView modelAndView = new ModelAndView();
        SecurityContextHolder.getContext().getAuthentication();
        String output = archOutputService.designArchitecture((String) session.getAttribute("selectedGeneration"),requirements.toString());
        session.setAttribute("output",output);
        modelAndView.addObject("ontology",new File("/json/"+output+".json"));
        return modelAndView;
    }



}
