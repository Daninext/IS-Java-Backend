package ru.itmo.kotiki.authcontollersservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import ru.itmo.kotiki.authcontollersservice.service.UserService;
import ru.itmo.kotiki.servicedata.entity.Cat;
import ru.itmo.kotiki.servicedata.entity.User;
import ru.itmo.kotiki.servicedata.transfer.CatBuffer;
import ru.itmo.kotiki.servicedata.transfer.CatTransfer;

import java.util.List;

@RestController
@RequestMapping("cats")
public class CatController {

    private final RabbitTemplate template;
    private final UserService userService;

    @Autowired
    public CatController(RabbitTemplate template, UserService userService) {
        this.template = template;
        this.userService = userService;
    }

    @PostMapping()
    public void addCat(@RequestBody Cat cat) throws JsonProcessingException {
        template.convertAndSend("addCat", catBufferToJson(new CatBuffer(null, new CatTransfer(cat))));
    }

    @PutMapping(value = "/{id}")
    public void updateCat(@PathVariable(name = "id") int id, @RequestBody Cat cat) throws JsonProcessingException {
        CatTransfer ct = new CatTransfer(cat);
        ct.setId(id);
        template.convertAndSend("updateCat", catBufferToJson(new CatBuffer(getUser(), ct)));
    }

    @PutMapping(value = "/{firstFriendId}/addFriendship/{secondFriendId}")
    public void addCatFriend(@PathVariable(name = "firstFriendId") int id, @PathVariable(name = "secondFriendId") int friendId) throws JsonProcessingException {
        if (id != friendId)
            template.convertAndSend("addCatFriend", catBufferToJson(new CatBuffer(getUser(), new CatTransfer(id), new CatTransfer(friendId))));
    }

    @PutMapping(value = "/{firstFriendId}/removeFriendship/{secondFriendId}")
    public void removeCatFriend(@PathVariable(name = "firstFriendId") int id, @PathVariable(name = "secondFriendId") int friendId) throws JsonProcessingException {
        if (id != friendId)
            template.convertAndSend("removeCatFriend", catBufferToJson(new CatBuffer(getUser(), new CatTransfer(id), new CatTransfer(friendId))));
    }

    @GetMapping(value = "/{id}")
    public List<CatTransfer> getCatById(@PathVariable(name = "id") int id) throws JsonProcessingException {
        User user = getUser();
        String response = (String) template.convertSendAndReceive("getCatById", catBufferToJson(new CatBuffer(user, new CatTransfer(id))));

        if (response == null)
            return null;

        return jsonToCatBuffer(response).getCats();
    }

    @GetMapping
    public List<CatTransfer> getAllCats() throws JsonProcessingException {
        User user = getUser();
        String response = (String) template.convertSendAndReceive("getAllCats", catBufferToJson(new CatBuffer(user)));

        if (response == null)
            return null;

        return jsonToCatBuffer(response).getCats();
    }

    @GetMapping(value = "breed/{breed}")
    public List<CatTransfer> getBreedCats(@PathVariable(name = "breed") String breed) throws JsonProcessingException {
        User user = getUser();
        CatTransfer cat = new CatTransfer();
        cat.setBreedString(breed);
        String response = (String) template.convertSendAndReceive("getCatsByBreed", catBufferToJson(new CatBuffer(user, cat)));

        if (response == null)
            return null;

        return jsonToCatBuffer(response).getCats();
    }

    @GetMapping(value = "color/{color}")
    public List<CatTransfer> getColorCats(@PathVariable(name = "color") String color) throws JsonProcessingException {
        User user = getUser();
        CatTransfer cat = new CatTransfer();
        cat.setColorString(color);
        String response = (String) template.convertSendAndReceive("getCatsByColor", catBufferToJson(new CatBuffer(user, cat)));

        if (response == null)
            return null;

        return jsonToCatBuffer(response).getCats();
    }

    @DeleteMapping(value = "/{id}")
    public void removeCat(@PathVariable(name = "id") int id) throws JsonProcessingException {
        template.convertAndSend("removeCat", catBufferToJson(new CatBuffer(getUser(), new CatTransfer(id))));
    }

    private User getUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        return userService.getByUsername(username);
    }

    private String catBufferToJson(CatBuffer buffer) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(buffer);
    }

    private CatBuffer jsonToCatBuffer(String json) throws JsonProcessingException {
        return new ObjectMapper().readValue(json, CatBuffer.class);
    }
}
