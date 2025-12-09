package com.sparta.engineering72.sakilaproject.controller;

import com.sparta.engineering72.sakilaproject.entities.Customer;
import com.sparta.engineering72.sakilaproject.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private CustomerService customerService;

    @Autowired
    public AdminController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/manage-customers")
    public String manageCustomers(ModelMap modelMap) {
        List<Customer> customers = customerService.getAllCustomers();
        modelMap.addAttribute("customers", customers);
        return "/admin/manage-customers";
    }

    @GetMapping("/add-customer")
    public String showAddCustomerPage(ModelMap modelMap) {
        Customer customer = new Customer();
        modelMap.addAttribute("customer", customer);
        return "/admin/add-customer";
    }

    @GetMapping("/edit-customer/{id}")
    public ModelAndView showEditCustomerPage(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("/admin/edit-customer");
        Customer customer = customerService.getCustomerByID(id);
        mav.addObject("customer", customer);
        return mav;
    }

    @PostMapping("/save-customer")
    public String saveCustomer(Customer customer, 
                              @RequestParam(required = false) String address,
                              @RequestParam(required = false) String city,
                              @RequestParam(required = false) String postalCode,
                              @RequestParam(required = false) String phone) {
        if (customer.getCreateDate() == null) {
            customer.setCreateDate(new Timestamp(System.currentTimeMillis()));
        }
        if (customer.getAddressId() == 0 && address != null) {
            customer.setAddressId(1);
        }
        customer.setLastUpdate(new Timestamp(System.currentTimeMillis()));
        customerService.save(customer);
        return "redirect:/admin/manage-customers";
    }
}
