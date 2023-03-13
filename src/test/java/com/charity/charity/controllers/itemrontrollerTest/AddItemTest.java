//package com.charity.charity.controllers.itemrontrollerTest;
//
//import com.charity.charity.controllers.ItemController;
//import com.charity.charity.models.Item;
//import com.charity.charity.repositirys.ItemRepository;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultMatcher;
//
//import static org.hamcrest.Matchers.containsString;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class AddItemTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ItemRepository ItemRepositoryService;
//
//    @MockBean
//    private Item ItemService = new Item();
//
//    @Test
//    void addItem() throws Exception {
////        ItemService.setInfo("test info");
////        ItemRepositoryService.save(ItemService);
//
//        this.mockMvc.perform(get("/item/add-item")).andDo(print()).andExpect(status().isOk())
//                .andExpect(content().string(containsString("")));
//
//    }
//}