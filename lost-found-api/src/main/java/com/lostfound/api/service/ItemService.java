package com.lostfound.api.service;

import com.lostfound.api.model.entity.Item;
import com.lostfound.api.model.entity.User;
import com.lostfound.api.model.dto.ItemRequest;
import com.lostfound.api.repository.ItemRepository;
import com.lostfound.api.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public ItemService(ItemRepository itemRepository, UserRepository userRepository,
                       NotificationService notificationService) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public Item createItem(ItemRequest request, String username) {
        User publisher = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Item item = new Item();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setCategory(request.getCategory());
        item.setLocation(request.getLocation());
        item.setItemType(request.getItemType());
        item.setPublisher(publisher);
        item.setPublisherName(publisher.getUsername());
        item.setStatus("ACTIVE");
        item.setTime(LocalDateTime.now());

        if ("LOST".equals(request.getItemType())) {
            item.setLostTime(request.getLostTime());
            item.setReward(request.getReward() != null ? request.getReward() : 0.0);
        } else if ("FOUND".equals(request.getItemType())) {
            item.setFoundTime(request.getFoundTime());
            item.setStatusDescription(request.getStatusDescription());
        }

        return itemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public List<Item> getAllItems(String type, String status, String keyword, String category) {
        return itemRepository.findByFilters(type, status, keyword, category);
    }

    @Transactional(readOnly = true)
    public Page<Item> getAllItemsPaged(String type, String status, String keyword, String category, int page, int size) {
        return itemRepository.findByFiltersPaged(type, status, keyword, category,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "time")));
    }

    @Transactional(readOnly = true)
    public Item getItemById(Integer id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("物品不存在"));
    }

    @Transactional(readOnly = true)
    public List<Item> getItemsByPublisher(Integer publisherId) {
        return itemRepository.findByPublisherId(publisherId);
    }

    public Item updateItem(Integer id, ItemRequest request, String username) {
        Item item = getItemById(id);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (!item.getPublisher().getId().equals(user.getId()) && !user.isAdmin()) {
            throw new RuntimeException("无权修改此物品");
        }

        if (request.getName() != null) item.setName(request.getName());
        if (request.getDescription() != null) item.setDescription(request.getDescription());
        if (request.getCategory() != null) item.setCategory(request.getCategory());
        if (request.getLocation() != null) item.setLocation(request.getLocation());

        return itemRepository.save(item);
    }

    public void deleteItem(Integer id, String username) {
        Item item = getItemById(id);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (!item.getPublisher().getId().equals(user.getId()) && !user.isAdmin()) {
            throw new RuntimeException("无权删除此物品");
        }

        itemRepository.deleteById(id);
    }

    public Item updateItemStatus(Integer id, String status, String username) {
        Item item = getItemById(id);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (!item.getPublisher().getId().equals(user.getId()) && !user.isAdmin()) {
            throw new RuntimeException("无权修改此物品状态");
        }

        item.setStatus(status);
        return itemRepository.save(item);
    }
}
