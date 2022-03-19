package learning.spring.binarytea.controller;

import learning.spring.binarytea.controller.request.NewOrderForm;
import learning.spring.binarytea.model.MenuItem;
import learning.spring.binarytea.model.Order;
import learning.spring.binarytea.model.OrderStatus;
import learning.spring.binarytea.service.MenuService;
import learning.spring.binarytea.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Order> listOrders() {
        return orderService.getAllOrders();
    }

    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createNewOrder(@Valid NewOrderForm form, BindingResult result,
                                 ModelMap modelMap) {
        if (result.hasErrors()) {
            modelMap.addAttribute("orders", orderService.getAllOrders());
            return "order";
        }
        createOrder(form);
        modelMap.addAttribute("orders", orderService.getAllOrders());
        return "order";
    }

    @ResponseBody
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> createNewOrder(@RequestBody @Valid NewOrderForm form,
                                                BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(null);
        }
        Order order = createOrder(form);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().build().toUri();
        return ResponseEntity.created(uri).body(order);
    }

    @PutMapping
    public String modifyOrdersToPaid(@RequestParam("id") String id, ModelMap modelMap) {
        int successCount = 0;
        if (StringUtils.isNotBlank(id)) {
            List<Long> orderIdList = Arrays.stream(id.split(","))
                    .map(s -> NumberUtils.toLong(s, -1))
                    .filter(l -> l > 0)
                    .collect(Collectors.toList());
            successCount = orderService
                    .modifyOrdersState(orderIdList, OrderStatus.ORDERED, OrderStatus.PAID);
        }
        modelMap.addAttribute(new NewOrderForm());
        modelMap.addAttribute("success_count", successCount);
        modelMap.addAttribute("orders", orderService.getAllOrders());
        return "order";
    }

    private Order createOrder(NewOrderForm form) {
        List<MenuItem> itemList = form.getItemIdList().stream()
                .map(i -> NumberUtils.toLong(i))
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        list -> menuService.getByIdList(list)));
        Order order = orderService.createOrder(itemList, form.getDiscount());
        log.info("创建新订单，Order={}", order);
        return order;
    }
}
