package learning.spring.binarytea.controller;

import learning.spring.binarytea.controller.request.NewOrderForm;
import learning.spring.binarytea.model.MenuItem;
import learning.spring.binarytea.model.Order;
import learning.spring.binarytea.service.MenuService;
import learning.spring.binarytea.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private MenuService menuService;

    @ModelAttribute("items")
    public List<MenuItem> items() {
        return menuService.getAllMenu();
    }

    @GetMapping
    public ModelAndView orderPage() {
        return new ModelAndView("order")
                .addObject(new NewOrderForm())
                .addObject("orders", orderService.getAllOrders());
    }

    @PostMapping
    public String createNewOrder(@Valid NewOrderForm form, BindingResult result,
                                 ModelMap modelMap) {
        if (result.hasErrors()) {
            modelMap.addAttribute("orders", orderService.getAllOrders());
            return "order";
        }
        List<MenuItem> itemList = form.getItemIdList().stream()
                .map(i -> NumberUtils.toLong(i))
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        list -> menuService.getByIdList(list)));
        Order order = orderService.createOrder(itemList, form.getDiscount());
        log.info("创建新订单，Order={}", order);
        modelMap.addAttribute("orders", orderService.getAllOrders());
        return "order";
    }
}
