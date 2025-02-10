package com.example.gestionressourcebanque.controllers;

import com.example.gestionressourcebanque.entities.Entite;
import com.example.gestionressourcebanque.entities.Role;
import com.example.gestionressourcebanque.services.EntiteService;
import com.example.gestionressourcebanque.services.RoleService;
import com.example.gestionressourcebanque.services.RoleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
public class RoleController {
    @Autowired
    private RoleService roleService;

    @RequestMapping("/CreateRole")
    public String createRole(ModelMap modelMap) {
        modelMap.addAttribute("role", new Role());

        return "/pages/CreateRole";
    }

    @RequestMapping("saveRole")
    public String saveRole(@Valid @ModelAttribute("role") Role roleController, BindingResult bindingResult, ModelMap modelMap) {
        if (bindingResult.hasErrors()) return "/pages/CreateRole";

        roleService.saveRole(roleController);
        return "redirect:/CreateRole";
    }

    @RequestMapping("/RolesList")
    public String RoleList(ModelMap modelMap) {
        List<Role> roles = roleService.getAllRoles();
        modelMap.addAttribute("roles", roles);
        return "/pages/RolesList";
    }

    @RequestMapping("/deleteRole")
    public String deleteRole(@RequestParam("id") long id, ModelMap modelMap) {
        roleService.deleteRoleById(id);
        List<Role> roles = roleService.getAllRoles();
        modelMap.addAttribute("roles", roles);
        return "/pages/RolesList";
    }

    @RequestMapping("/EditRole")
    public String editRole(@RequestParam("id") long id, ModelMap modelMap) {
        Role role = roleService.getRoleById(id);
        modelMap.addAttribute("role", role);
        return "/pages/EditRole";
    }

    @RequestMapping("/updateRole")
    public String updateRole(@ModelAttribute("role") Role roleController, ModelMap modelMap) {
        roleService.updateRole(roleController);
        List<Role> roles = roleService.getAllRoles();
        modelMap.addAttribute("roles", roles);
        return "/pages/RolesList";
    }

}
