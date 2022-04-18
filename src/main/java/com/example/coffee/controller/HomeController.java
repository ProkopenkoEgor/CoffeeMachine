package com.example.coffee.controller;

import com.example.coffee.model.Counts;
import com.example.coffee.model.LevelOfIngredients;
import com.example.coffee.model.OrderMenu;
import com.example.coffee.model.Orders;
import com.example.coffee.service.CountsService;
import com.example.coffee.service.LevelOfIngredientsService;
import com.example.coffee.service.OrderMenuService;
import com.example.coffee.service.OrdersService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class HomeController{
    public final LevelOfIngredientsService levelofingredientsService;
    public final CountsService countsService;
    public final OrderMenuService ordermenuService;
    public final OrdersService ordersService;

    public HomeController(LevelOfIngredientsService levelofingredientsService, CountsService countsService, OrderMenuService ordermenuService, OrdersService ordersService) {
        this.levelofingredientsService = levelofingredientsService;
        this.countsService = countsService;
        this.ordermenuService = ordermenuService;
        this.ordersService = ordersService;
    }
        @GetMapping("/home")
        public String homePage(Model model) {
            LevelOfIngredients levelOfIngredients = levelofingredientsService.findById((long) 1);
            Counts counts = countsService.findById((long) 1);
            List<OrderMenu> orderMenuList = ordermenuService.findAll();
            model.addAttribute("levelOfIngredients", levelOfIngredients);
            model.addAttribute("ordersMenuList", orderMenuList);
            model.addAttribute("counts", counts);
            return "HomePage";
        }

        @PostMapping("/createNewOrders/{idOrderMenu}")
        public String createNewOrders(@PathVariable long idOrderMenu,Model model) {
            OrderMenu orderMenu = ordermenuService.findById(idOrderMenu);
            LevelOfIngredients levelOfIngredients = levelofingredientsService.findById((long) 1);
            Counts counts = countsService.findById((long) 1);
            if(levelOfIngredients.getLevelOfWater() - orderMenu.getQuantityOfWater() < 0){
                List<OrderMenu> orderMenuList = ordermenuService.findAll();
                model.addAttribute("levelOfIngredients", levelOfIngredients);
                model.addAttribute("ordersMenuList", orderMenuList);
                model.addAttribute("counts", counts);
                model.addAttribute("responseForNotEnoughLevel", "notEnoughLevelOfWater");
                return "HomePage";
            }
            if(levelOfIngredients.getLevelOfCoffee() - orderMenu.getQuantityOfCoffee() < 0){
                List<OrderMenu> orderMenuList = ordermenuService.findAll();
                model.addAttribute("levelOfIngredients", levelOfIngredients);
                model.addAttribute("ordersMenuList", orderMenuList);
                model.addAttribute("counts", counts);
                model.addAttribute("responseForNotEnoughLevel", "notEnoughLevelOfCoffee");
                return "HomePage";
            }
            if(levelOfIngredients.getLevelOfMilk() - orderMenu.getQuantityOfMilk() < 0){
                List<OrderMenu> orderMenuList = ordermenuService.findAll();
                model.addAttribute("levelOfIngredients", levelOfIngredients);
                model.addAttribute("ordersMenuList", orderMenuList);
                model.addAttribute("counts", counts);
                model.addAttribute("responseForNotEnoughLevel", "notEnoughLevelOfMilk");
                return "HomePage";
            }
            if(levelOfIngredients.getLevelOfCream() - orderMenu.getQuantityOfCream() < 0){
                List<OrderMenu> orderMenuList = ordermenuService.findAll();
                model.addAttribute("levelOfIngredients", levelOfIngredients);
                model.addAttribute("ordersMenuList", orderMenuList);
                model.addAttribute("counts", counts);
                model.addAttribute("responseForNotEnoughLevel", "notEnoughLevelOfCream");
                return "HomePage";
            }
            Orders newOrder = new Orders();
            newOrder.setNameOfOrder(orderMenu.getTypeOfOrder());
            newOrder.setOrderMenu(orderMenu);
            ordersService.saveOrders(newOrder);
            counts.setCountOfOrder(counts.getCountOfOrder() + 1);
            countsService.saveCounts(counts);
            levelOfIngredients.setLevelOfWater(levelOfIngredients.getLevelOfWater() - orderMenu.getQuantityOfWater());
            levelOfIngredients.setLevelOfCoffee(levelOfIngredients.getLevelOfCoffee() - orderMenu.getQuantityOfCoffee());
            levelOfIngredients.setLevelOfMilk(levelOfIngredients.getLevelOfMilk() - orderMenu.getQuantityOfMilk());
            levelOfIngredients.setLevelOfCream(levelOfIngredients.getLevelOfCream() - orderMenu.getQuantityOfCream());
            levelofingredientsService.saveLevelofingredients(levelOfIngredients);
            if(counts.getCountOfOrder()%100==0) {
                List<OrderMenu> orderMenuList = ordermenuService.findAll();
                model.addAttribute("filterReplacementRequired1", "filterReplacementRequired");
                model.addAttribute("levelOfIngredients", levelOfIngredients);
                model.addAttribute("orderMenuList", orderMenuList);
                return "HomePage";
            }
            return "redirect:/home";
        }

        @GetMapping("/createNewCoffee")
        public String createNewCoffee(@ModelAttribute("newOrderMenu") OrderMenu newOrderMenu) {
        return "CreateNewCoffee";
        }

        @PostMapping("/createNewOrderMenu")
        public String createNewOrderMenu(@Valid
                                         @ModelAttribute("newOrderMenu") OrderMenu newOrderMenu, BindingResult bindingResult) {
            if(bindingResult.hasErrors()){
                return "CreateNewCoffee";
            }
            List<String> orderMenuList = ordermenuService.findTypeOfOrder();
            if(orderMenuList.stream().anyMatch(newOrderMenu.getTypeOfOrder()::equals)){
                bindingResult.rejectValue("typeOfOrder", "error.typeOfOrder", "Такой тип кофе уже существует");
                return "CreateNewCoffee";
            }
            newOrderMenu.setQuantityOfWater(newOrderMenu.getQuantityOfCoffee());
            ordermenuService.saveOrderMenu(newOrderMenu);
            return "redirect:/home";
        }

        @PostMapping("/rinseWithWater")
        public String rinseWithWater(Model model) {
            LevelOfIngredients levelofingredients = levelofingredientsService.findById((long) 1);
            List<OrderMenu> orderMenuList = ordermenuService.findAll();
            Counts counts = countsService.findById((long) 1);
            if (levelofingredients.getLevelOfWater() < 250) {
                model.addAttribute("levelOfIngredients", levelofingredients);
                model.addAttribute("ordersMenuList", orderMenuList);
                model.addAttribute("counts", counts);
                model.addAttribute("response", "notEnoughLevelOfWater");
                return "HomePage";
            }
            Orders orders = new Orders();
            orders.setNameOfOrder("Промывка водой");
            orders.setOrderMenu(null);
            levelofingredients.setLevelOfWater(levelofingredients.getLevelOfWater() - 250);
            levelofingredientsService.saveLevelofingredients(levelofingredients);
            ordersService.saveOrders(orders);
            return "redirect:/home";
        }

        @PostMapping("/fillTheWater")
        public String fillTheWater(Model model) {
            LevelOfIngredients levelofingredients = levelofingredientsService.findById((long) 1);
            List<OrderMenu> orderMenuList = ordermenuService.findAll();
            Counts counts = countsService.findById((long) 1);
            if (levelofingredients.getLevelOfWater() == 1000) {
                model.addAttribute("levelOfIngredients", levelofingredients);
                model.addAttribute("ordersMenuList", orderMenuList);
                model.addAttribute("counts", counts);
                model.addAttribute("RefuelingIsNotRequired", "responseForLevelOfWater");
                return "HomePage";
            }
            levelofingredients.setLevelOfWater(1000);
            counts.setCountOfWater(counts.getCountOfWater() + 1);
            countsService.saveCounts(counts);
            levelofingredientsService.saveLevelofingredients(levelofingredients);
            return "redirect:/home";
        }

        @PostMapping("/fillTheCoffee")
        public String fillTheCoffee(Model model) {
            LevelOfIngredients levelofingredients = levelofingredientsService.findById((long) 1);
            List<OrderMenu> orderMenuList = ordermenuService.findAll();
            Counts counts = countsService.findById((long) 1);
            if (levelofingredients.getLevelOfCoffee() == 1000) {
                model.addAttribute("levelOfIngredients", levelofingredients);
                model.addAttribute("ordersMenuList", orderMenuList);
                model.addAttribute("counts", counts);
                model.addAttribute("RefuelingIsNotRequired", "responseForLevelOfCoffee");
                return "HomePage";
            }
            levelofingredients.setLevelOfCoffee(1000);
            counts.setCountOfCoffee(counts.getCountOfCoffee() + 1);
            countsService.saveCounts(counts);
            levelofingredientsService.saveLevelofingredients(levelofingredients);
            return "redirect:/home";
        }

        @PostMapping("/fillTheMilk")
        public String fillTheMilk(Model model) {
            LevelOfIngredients levelofingredients = levelofingredientsService.findById((long) 1);
            List<OrderMenu> orderMenuList = ordermenuService.findAll();
            Counts counts = countsService.findById((long) 1);
            if (levelofingredients.getLevelOfMilk() == 1000) {
                model.addAttribute("levelOfIngredients", levelofingredients);
                model.addAttribute("ordersMenuList", orderMenuList);
                model.addAttribute("counts", counts);
                model.addAttribute("RefuelingIsNotRequired", "responseForLevelOfMilk");
                return "HomePage";
            }
            levelofingredients.setLevelOfMilk(1000);
            counts.setCountOfMilk(counts.getCountOfMilk() + 1);
            countsService.saveCounts(counts);
            levelofingredientsService.saveLevelofingredients(levelofingredients);
            return "redirect:/home";
        }

        @PostMapping("/fillTheCream")
        public String fillTheCream(Model model) {
            LevelOfIngredients levelofingredients = levelofingredientsService.findById((long) 1);
            List<OrderMenu> orderMenuList = ordermenuService.findAll();
            Counts counts = countsService.findById((long) 1);
            if (levelofingredients.getLevelOfCream() == 1000) {
                model.addAttribute("levelOfIngredients", levelofingredients);
                model.addAttribute("ordersMenuList", orderMenuList);
                model.addAttribute("counts", counts);
                model.addAttribute("RefuelingIsNotRequired", "responseForLevelOfCream");
                return "HomePage";
            }
            levelofingredients.setLevelOfCream(1000);
            counts.setCountOfCream(counts.getCountOfCream() + 1);
            countsService.saveCounts(counts);
            levelofingredientsService.saveLevelofingredients(levelofingredients);
            return "redirect:/home";
        }

    @GetMapping("/updateOrderMenu/{idOrderMenu}")
    public String UpdateCoffeeForm(@PathVariable("idOrderMenu") long idOrderMenu, Model model){
        OrderMenu orderMenu = ordermenuService.findById(idOrderMenu);
        model.addAttribute("orderMenu", orderMenu);
        return "UpdateNewCoffee";
    }

    @PostMapping("/updateOrderMenu/{idOrderMenu}")
    public String UpdateCoffee(@PathVariable("idOrderMenu") long idOrderMenu, @Valid OrderMenu orderMenu,
                               BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "UpdateNewCoffee";
        }
        List<String> orderMenuList = ordermenuService.findTypeOfOrder();
        OrderMenu existingOrderMenu = ordermenuService.findById(idOrderMenu);
        if (orderMenu.getTypeOfOrder().equalsIgnoreCase(existingOrderMenu.getTypeOfOrder())){
            ordermenuService.saveOrderMenu(orderMenu);
            return "redirect:/home";
        }else
        if(orderMenuList.stream().anyMatch(orderMenu.getTypeOfOrder()::equals)) {
            bindingResult.rejectValue("typeOfOrder", "error.typeOfOrder", "Такой тип кофе уже существует");
            return "UpdateNewCoffee";
        }
        ordermenuService.saveOrderMenu(orderMenu);
        return"redirect:/home";
    }

    @DeleteMapping("/coffee-delete/{id}")
    public String deletePrinters(@PathVariable("id") long id) {
        ordermenuService.deleteById(id);
        return "redirect:/home";
    }
}
