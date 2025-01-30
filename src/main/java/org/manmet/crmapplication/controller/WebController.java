package org.manmet.crmapplication.controller;

import org.manmet.crmapplication.config.CustomUserDetails;
import org.manmet.crmapplication.model.Customers;
import org.manmet.crmapplication.model.Lead;
import org.manmet.crmapplication.model.Roles;
import org.manmet.crmapplication.model.Opportunity;
import org.manmet.crmapplication.service.CustomerService;
import org.manmet.crmapplication.service.LeadService;
import org.manmet.crmapplication.service.OpportunityService;
import org.manmet.crmapplication.service.PipelineStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class WebController {

    @Autowired
    private LeadService leadService;

    @Autowired
    private PipelineStageService pipelineStageService;

    @Autowired
    private OpportunityService opportunityService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/signup")
    public String showSignUpPage() {
        return "sign-up";
    }

    @GetMapping("/signin")
    public String showLoginPage() {
        return "signin";
    }

    @GetMapping("/admin-signup")
    public String showAdminSignUpPage() {
        return "admin-sign-up";
    }

    @GetMapping("/dashboard")
    public String showDashboardPage(Model model) {
        // Add logging to ensure the user is authenticated
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            return "redirect:/home";
        }
        Set<String> roles = new HashSet<>(); // Initialize an empty set for roles

        // Handle JWT-based authentication (CustomUserDetails)
        if (auth.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            roles = userDetails.getRoles().stream()
                    .map(Roles::getRoleName)
                    .collect(Collectors.toSet());

            // Handle OAuth2/OIDC-based authentication (DefaultOidcUser)
        } else if (auth.getPrincipal() instanceof DefaultOidcUser) {
            DefaultOidcUser oidcUser = (DefaultOidcUser) auth.getPrincipal();
            roles = oidcUser.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());
        }

        // Add roles to the model so that they can be accessed in the Thymeleaf template
        model.addAttribute("roles", roles);

        return "dashboard";
    }

    @GetMapping("/customers")
    public String listCustomers() {
        return "list-customers";  // This should match the name of the HTML file without the extension
    }
    @GetMapping("/customers/add")
    public String addCustomer(Model model) {
        model.addAttribute("customer", new Customers());  // Add a new customer object to the model
        return "add-customer";
    }

    @GetMapping("/customers/edit/{id}")
    public String editCustomer(@PathVariable Long id, Model model) {
        Customers customer = customerService.findById(id);
        model.addAttribute("customer", customer);
        return "edit-customer";
    }

    @GetMapping("/leads")
    public String listLeads(Model model) {
        model.addAttribute("leads", leadService.findAll());
        model.addAttribute("stages", pipelineStageService.getAllStages());
        return "list-leads"; // Ensure you have a corresponding HTML template
    }

    @GetMapping("leads/add-lead")
    public String showAddLeadForm(Model model) {
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("pipelineStages", pipelineStageService.getAllStages());
        return "add-lead";
    }

    @GetMapping("leads/edit-lead/{id}")
    public String editLead(@PathVariable Long id, Model model) {
        Lead lead = leadService.findById(id);
        model.addAttribute("lead", lead);
        return "edit-lead";
    }

    @GetMapping("/pipeline-stages")
    public String listPipelineStages() {
        return "list-pipeline-stages";  // This should match the name of the HTML file without the extension
    }

    @GetMapping("/edit-stage")
    public String ediPipelineStages() {
        return "edit-pipeline-stage";  // This should match the name of the HTML file without the extension
    }

    @GetMapping("/opportunities")
    public String listOpportunities(Model model) {
        model.addAttribute("opportunities", opportunityService.findAll());
        return "list-opportunities";  // This should match the name of the HTML file without the extension
    }

    @GetMapping("/opportunities/add-opportunity")
    public String showCreateOpportunityForm(Model model) {
        List<Customers> customers = customerService.findAll();
        model.addAttribute("customers", customers);
        model.addAttribute("opportunity", new Opportunity());
        return "add-opportunity";  // Ensure this matches the Thymeleaf template name
    }


    @GetMapping("/opportunities/edit/{id}")
    public String editOpportunityForm(@PathVariable Long id, Model model) {
        Opportunity opportunity = opportunityService.findById(id)
                .orElseThrow(() -> new RuntimeException("Opportunity not found"));

        List<Customers> customers = customerService.findAll();

        model.addAttribute("opportunity", opportunity);
        model.addAttribute("customers", customers);

        return "edit-opportunity"; // This should match the name of the HTML file for editing opportunities
    }

    @GetMapping("/users")
    public String listUsers() {
        return "list-users";
    }

    @GetMapping("/users/add-user")
    public String showAddUserForm() {
        return "add-user";
    }

    @GetMapping("/permissions")
    public String listPermissions(){
        return "list-permissions";
    }

    @GetMapping("/assign-permissions")
    public String assignPermissions(){
        return "assign-permissions";
    }

    @GetMapping("/authorisation")
    public String diagnosticTool() {return "diagnosticTool";}
}
